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
-export([start_rabbitmq_server/0, start_consuming_handler/3, request_consuming/1, test_g/0, push/1]).
-export([init_consuming_handler/3]).
-export([init/1, handle_call/3, handle_cast/2]).


%% @doc Spawns the server and registers the local name (unique)
start_rabbitmq_server() ->
  gen_server:start({local, rabbitmq_server}, ?MODULE, [rabbitmq_server], []).

start_consuming_handler(Channels, Channel, Receiver)->
  {ok, Pid} = init_consuming_handler(Channels, Channel, Receiver),
  io:format("consuming started over pid: ~p~n", [Pid]),
  {ok, Pid}.

%% @private
%% @doc Initializes the consumer server
init([rabbitmq_server]) ->
  Connection = create_connection(),
  io:format("consumer server init: ~p~n", [{[Connection],[], []}]),
  {ok, {[Connection],[], []}}.

init_consuming_handler(Channels, Channel, Receiver) ->
  create_queue(Channel, Receiver),
  Pid = spawn(fun() -> loop_consuming(Channel, Receiver) end),
  amqp_channel:subscribe(Channel, #'basic.consume'{queue = list_to_binary(Receiver)}, Pid),
  {ok, Pid}.

request_consuming(Receiver)->
  gen_server:call(rabbitmq_server, {init, Receiver}).

% push the message into the rabbitmq
% Queue name is equal to the Receiver which is the username of the Receiver
% the inserted message in rabbitmq has the following format
% {"Msg_Id":  , "Sender": , "Receiver": , "Text": , "Timestamp": }
push({Msg_Id, Sender, Receiver, Text, Timestamp}) ->
  gen_server:call(rabbitmq_server, {push, {Msg_Id, Sender, Receiver, Text, Timestamp}}).

remove_channel(Channel, Consumer) ->
  gen_server:call(rabbitmq_server, {remove_channel, Channel, Consumer}).

handle_call({init, Receiver}, _From, {Connections, Channels, Consumers}) ->
  {New_Connections, New_Channels, New_Consumers} = consume({Connections, Channels, Consumers}, Receiver),
  io:format("The result: ~p~n", [{New_Connections, New_Channels,  New_Consumers}]),
  {reply, consumer_created, {New_Connections, New_Channels,  New_Consumers}};

handle_call({remove_channel, Channel, Consumer}, _From, {Connections, Channels, Consumers}) ->
  {reply, channel_deleted, {Connections, lists:delete(Channel, Channels), lists:delete(Consumer, Consumers)}};

handle_call({push, {Msg_Id, Sender, Receiver, Text, Timestamp}}, _From, {Connections, Channels, Consumers}) ->
  Map = create_map({Msg_Id, Sender, Receiver, Text, Timestamp}),
  Payload = jsx:encode(Map),
  Publish = #'basic.publish'{exchange = <<>>, routing_key = list_to_binary(Receiver)},
  Props = #'P_basic'{delivery_mode = 2}, %% persistent message
  Msg = #amqp_msg{props = Props, payload = Payload},
  {New_Connections, Connection} = get_connection(Connections),
  {ok, Channel} = amqp_connection:open_channel(Connection),
  amqp_channel:cast(Channel, Publish, Msg),
  {reply, pushed, {New_Connections, Channels, Consumers}}.

handle_cast(terminate, {Connections, _Channels, Consumers}) ->
  [remove_consumer(C) || C <- Consumers],
  [amqp_connection:close(L) || L <- Connections],
  {noreply, {[],[], []}};

handle_cast(reset, _State) ->
  Connection = create_connection(),
  {restarted, {[Connection],[], []}}.

remove_consumer(Consumer) ->
  Consumer ! server_shutdown.

close_connections([L]) ->
  amqp_connection:close(L),
  ok;
close_connections([H|T]) ->
  amqp_connection:close(H),
  close_connections(T).

create_connection() ->
  {ok, Connection} = amqp_connection:start(#amqp_params_network{ host="localhost"}),
  io:format("New Connection created: ~p~n", [Connection]),
  Connection.

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

consume({Connections, Channels, Consumers}, Receiver) ->
  {New_Connections, Connection} = get_connection(Connections),
  {ok, Channel} = amqp_connection:open_channel(Connection),
  {ok, Pid} = start_consuming_handler(Channels, Channel, Receiver),
  {New_Connections, Channels ++ [Channel], Consumers ++ [Pid]}.


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

test_g() ->
  Receiver = "gholi",
  start_rabbitmq_server(),
  request_consuming(Receiver).

delete_queue(Channel, Queue_Name) ->
  Delete = #'queue.delete'{queue = <<Queue_Name>>},
  #'queue.delete_ok'{} = amqp_channel:call(Channel, Delete).

%Queue_Name should has <<String>> format
create_queue(Channel, Queue_Name) ->
  Declare = #'queue.declare'{
    queue = list_to_binary(Queue_Name),
    durable = true
  },
  #'queue.declare_ok'{} = amqp_channel:call(Channel, Declare).



create_map({Msg_Id, Sender, Receiver, Text, Timestamp}) ->
  #{<<"Msg_Id">> => list_to_binary(Msg_Id),
    <<"Sender">> => list_to_binary(Sender),
    <<"Receiver">> => list_to_binary(Receiver),
    <<"Text">> => list_to_binary(Text),
    <<"Timestamp">> => list_to_binary(Timestamp)
  }.

%create a connection and a channel to the rabbitmq
create_channel()->
  {ok, Connection} = amqp_connection:start(#amqp_params_network{ host="localhost" }),
  {ok, _} = amqp_connection:open_channel(Connection).

% Consumer: the pid of a process to which the client library will deliver messages
%%start_consuming(Connection,Receiver) ->
%%  Server_Name = binary_to_atom(list_to_binary(Receiver)),
%%  %the problem is the server_name is a variable and I cannot access to it easily
%%  gen_server:start({local, Server_Name}, ?MODULE, [Connection], []),
%%  gen_server:call(Server_Name, consuming).
%%
%%init(Connection)->
%%  {ok, Channel} = amqp_connection:open_channel(Connection),
%%  create_queue(Channel, _).

start_consuming(Receiver) ->
  Pid = spawn(?MODULE, consume, [Receiver]),
  io:format("Consume PID: ~p~n", [Pid]).


close_Channel(Channels, Channel) ->
  lists:delete(Channel, Channels),
  amqp_channel:close(Channel).

%test consumer
test_c()->
  consume("gholi", "ali"),
  io:format("Check Connection PID: ~p~n", [whereis(connection)]).

%%  amqp_connection:close(whereis(connection)),
%%  io:format("Check Connection PID: ~p~n", [whereis(connection)]).
%%  start_consuming(Connection, "gholi").
%test producer
test_p() ->
  Queue_name = "gholi",
  {ok, Channel} = create_channel(),
  create_queue(Channel, Queue_name),
  push(Channel, {"Msg_Id", "Sender", Queue_name, "Text", "Timestamp"}).
