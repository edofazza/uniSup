%%%-------------------------------------------------------------------
%%% @author Sina
%%% @copyright (C) 2021, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 03. Jan 2021 12:25 PM
%%%-------------------------------------------------------------------
-module(rabbitmq).
-author("Sina").
-include_lib("../_build/default/lib/amqp_client/include/amqp_client.hrl").
-include_lib("eunit/include/eunit.hrl").
-behavior(gen_server).
%% API
-export([start_rabbitmq_server/0, stop_rabbitmq_server/0, reset_rabbitmq_server/0, delete_user/1]).
-export([start_consuming_handler/2, terminate_consuming_session/1, request_consuming/1, test_g/0, push/1]).
-export([init_consuming_handler/2]).
-export([init/1, handle_call/3, handle_cast/2]).


%% @doc Spawns the rabbitmq server and registers the local rabbitmq_server (unique)
start_rabbitmq_server() ->
  gen_server:start({local, rabbitmq_server}, ?MODULE, [rabbitmq_server], []).

%% @doc stop the rabbitmq server
stop_rabbitmq_server() ->
  gen_server:cast(rabbitmq_server, stop).

%% @doc reset the rabbitmq server
reset_rabbitmq_server()->
  gen_server:cast(rabbitmq_server, reset).

%% @doc request for consuming
%% @param the username of the User
request_consuming(Username)->
  gen_server:call(rabbitmq_server, {start_consumer, Username}).

%% @doc terminate the consuming of a user in case of logout or terminated session
%% @param the username of the User
terminate_consuming_session(Username)->
  gen_server:call(rabbitmq_server, {terminate_consuming_session, Username}).

%% @doc delete the user by its username
%% @param the username of the User
delete_user(Username)->
  gen_server:call(rabbitmq_server, {delete_user, Username}),
  gen_server:call(rabbitmq_server, {terminate_consuming_session, Username}).

%% @doc push the message into the rabbitmq
%% @param message id, Sender username, Receiver Username, text and timestamp
push({Msg_Id, Sender, Receiver, Text, Timestamp}) ->
  gen_server:call(rabbitmq_server, {push, {Msg_Id, Sender, Receiver, Text, Timestamp}}).


%% @private
%% @doc initialize the pulling consumer
%% @returns PID of the pulling consumer
start_consuming_handler(Channel, Receiver)->
  {ok, Pid} = init_consuming_handler(Channel, Receiver),
  io:format("consuming started over pid: ~p~n", [Pid]),
  {ok, Pid}.

%% @private
%% @doc create the queue and spawn the pulling consumer
%% @returns PID of the pulling consumer
init_consuming_handler(Channel, Receiver) ->
  create_queue(Channel, Receiver),
  Pid = spawn(fun() -> loop_consuming(Channel, Receiver) end),
  amqp_channel:subscribe(Channel, #'basic.consume'{queue = list_to_binary(Receiver)}, Pid),
  {ok, Pid}.

%% @private
%% @doc removing the channel from the channel list
%% @param Channel PID, Consumer PID
remove_channel(Channel, Consumer) ->
  gen_server:call(rabbitmq_server, {remove_channel, Channel, Consumer}).


%% @private
%% @doc Initializes the rabbitmq server
init([rabbitmq_server]) ->
  Connection = create_connection(),
  io:format("consumer server init: ~p~n", [{[Connection],[], []}]),
  {ok, {[Connection],[], []}}.

%% @private
handle_call({start_consumer, Receiver}, _From, {Connections, Channels, Consumers}) ->
  {New_Connections, New_Channels, New_Consumers} = consume({Connections, Channels, Consumers}, Receiver),
  io:format("The result: ~p~n", [{New_Connections, New_Channels,  New_Consumers}]),
  {reply, consumer_created, {New_Connections, New_Channels,  New_Consumers}};

%% @private
handle_call({terminate_consuming_session, Receiver}, _From, {Connections, Channels, Consumers}) ->
  {Receiver, Channel} = lists:keyfind(Receiver, 1, Channels),
  {Receiver, Consumer} = lists:keyfind(Receiver, 1, Consumers),
  stop_consuming(Consumer),
  {reply, consuming_session_terminated, {Connections, lists:delete(Channel, {Receiver, Channel}),
    lists:delete(Consumer, {Receiver, Consumer})}};

%% @private
handle_call({delete_user, Receiver}, _From, {Connections, Channels, Consumers}) ->
  {Receiver, Channel} = lists:keyfind(Receiver, 1, Channels),
  delete_queue(Channel, Receiver),
  {reply, user_deleted, {Connections, Channels,  Consumers}};

%% @private
handle_call({remove_channel, Channel, Consumer}, _From, {Connections, Channels, Consumers}) ->
  {reply, channel_deleted, {Connections, lists:delete(Channel, Channels), lists:delete(Consumer, Consumers)}};

%% @private
handle_call({push, {Msg_Id, Sender, Receiver, Text, Timestamp}}, _From, {Connections, Channels, Consumers}) ->
  Map = create_map({Msg_Id, Sender, Receiver, Text, Timestamp}),
  Payload = jsx:encode(Map),
  {New_Connections, Connection} = get_connection(Connections),
  {ok, Channel} = amqp_connection:open_channel(Connection),
  create_queue(Channel, list_to_binary(Receiver)),
  %% Queue name is equal to the Receiver which is the username of the Receiver
  Publish = #'basic.publish'{exchange = <<>>, routing_key = list_to_binary(Receiver)},
  Props = #'P_basic'{delivery_mode = 2}, %% persistent message
  %% the inserted message in rabbitmq has the following format
  %% {"Msg_Id": "*" , "Sender": "*", "Receiver": "*", "Text": "*", "Timestamp": "*"}
  Msg = #amqp_msg{props = Props, payload = Payload},
  amqp_channel:cast(Channel, Publish, Msg),
  {reply, pushed, {New_Connections, Channels, Consumers}}.

%% @private
handle_cast(stop, {Connections, _Channels, Consumers}) ->
  [stop_consuming(C) || C <- Consumers],
  [amqp_connection:close(L) || L <- Connections],
  {noreply, {[],[], []}};

%% @private
handle_cast(reset, _State) ->
  Connection = create_connection(),
  {restarted, {[Connection],[], []}}.

%% @private
stop_consuming(Consumer) ->
  Consumer ! server_shutdown.

%% @private
create_connection() ->
  {ok, Connection} = amqp_connection:start(#amqp_params_network{ host="localhost"}),
  io:format("New Connection created: ~p~n", [Connection]),
  Connection.

%% @private
get_connection(Connections) ->
  Connection = lists:nth(rand:uniform(length(Connections)), Connections),
  case is_process_alive(Connection) of
    false ->
      New_Connection = create_connection(),
      lists:delete(Connection, Connections),
      get_connection(Connections ++ [New_Connection]);
    true ->
      {Connections, Connection}
  end.

%% @private
consume({Connections, Channels, Consumers}, Receiver) ->
  {New_Connections, Connection} = get_connection(Connections),
  {ok, Channel} = amqp_connection:open_channel(Connection),
  {ok, New_Consumer} = start_consuming_handler(Channel, Receiver),
  {New_Connections, Channels ++ [{Channel, Receiver}], Consumers ++ [{New_Consumer, Receiver}]}. %return the new state

%% @private
loop_consuming(Channel, Receiver) ->
  case is_process_alive(Channel) of
    true->
      receive
        #'basic.consume_ok'{} ->
          io:format("basic.consume_ok~n"),
          ok,
          loop_consuming(Channel, Receiver);

        #'basic.cancel_ok'{} ->
          io:format("cancel~n"),
          cancel;

        {#'basic.deliver'{delivery_tag = Tag}, {amqp_msg,_, Msg}} ->
          io:format("this is the content: ~p~n", [Msg]),
          io:format("this is consumer tag~p~n", [Tag]),
          io:format("basic.deliver~n"),
          amqp_channel:cast(Channel, #'basic.ack'{delivery_tag = Tag}),
          loop_consuming(Channel, Receiver);

        _ ->
          loop_consuming(Channel, Receiver);

        server_shutdown ->
          shut_down

      after 5000 ->
        amqp_channel:close(Channel),
        remove_channel(Channel, self()),
        request_consuming(Receiver),
        loop_consuming(Channel, Receiver)
      end;

    false ->
      io:format("dead channel~n"),
      dead_channel
  end.

%% @private
%% just for test case
test_g() ->
  Receiver = "gholi",
  start_rabbitmq_server(),
  request_consuming(Receiver).

%% @private
delete_queue(Channel, Queue_Name) ->
  Delete = #'queue.delete'{queue = <<Queue_Name>>},
  #'queue.delete_ok'{} = amqp_channel:call(Channel, Delete).

%% @private
create_queue(Channel, Queue_Name) ->
  Declare = #'queue.declare'{
    queue = list_to_binary(Queue_Name),
    durable = true
  },
  #'queue.declare_ok'{} = amqp_channel:call(Channel, Declare).

%% @private
%% @doc create a map from the input tuple
create_map({Msg_Id, Sender, Receiver, Text, Timestamp}) ->
  #{<<"Msg_Id">> => list_to_binary(Msg_Id),
    <<"Sender">> => list_to_binary(Sender),
    <<"Receiver">> => list_to_binary(Receiver),
    <<"Text">> => list_to_binary(Text),
    <<"Timestamp">> => list_to_binary(Timestamp)
  }.


