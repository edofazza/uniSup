%%%-------------------------------------------------------------------
%%% @author Mirco Ramo
%%% @copyright (C) 2020, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 27. dic 2020 19:35
%%%-------------------------------------------------------------------
-module(uniSupErlangServer).
-author("Mirco Ramo").
-import(rabbitmq, []).

-behaviour(gen_server).

%% API
-export([start_server/0, call_server/1]).

%% gen_server callbacks
-export([init/1, handle_call/3, handle_cast/2]).

-define(SERVER, ?MODULE).

%% RECORD


%%%===================================================================
%%% API
%%%===================================================================

%% @doc Spawns the server and registers the local name (unique)
start_server() ->
  gen_server:start({local, unisup_gen_server}, ?MODULE, [], []).

%% @doc Standard API for every possible request to the server.
call_server(Content) ->
  gen_server:call(unisup_gen_server, Content).


%%%===================================================================
%%% gen_server callbacks
%%%===================================================================

%% @private
%% @doc Initializes the server
init(_) ->
  rabbitmq:start_rabbitmq_server(),
  {ok,{}}.

%% @private
%% @doc Handles messages to be sent: checks the receiver username, if it exists pushes the message into RabbitMQ
handle_call({message, {Msg_Id, Sender, Receiver, Text}}, _From, _)->
  case mnesiaFunctions:is_user_present(Receiver) of    %%maybe to do at consuming time from RabbitMQ
    true->
      _ReceiverPid = mnesiaFunctions:retrieve_pid(Receiver),
      _ReceiverNodeName = mnesiaFunctions:retrieve_nodename(Receiver),
      Timestamp = mnesiaFunctions:insert_new_message(Sender, Receiver, Text),
      rabbitmq:push({Msg_Id, Sender, Receiver, Text, Timestamp}),
      {reply, {ack, Msg_Id},  _ = '_'};
    false ->
      {reply, {nack, Msg_Id},  _ = '_'}
  end;

%% @doc Handles login requests: calls mnesiaFunctions:login for credential checking and physical addresses updating
handle_call({log, {Pid, Username, Password, ClientNodeName}}, _From, _)->
  case mnesiaFunctions:login(Username, Password, ClientNodeName, Pid) of
    true->
      case rabbitmq:request_consuming(Username, Pid) of
        consumer_created -> {reply, true, _ = '_'};
        _ -> {reply, false, _ = '_'}
      end;
    _->
      {reply, false, _ = '_'}
  end;

%% @doc Handles register requests: calls MnesiaFunctions:register for username checking and user storaging
handle_call({register, {Pid, Username, Password, ClientNodeName}}, _From, _)->
  {reply, mnesiaFunctions:register(Username, Password, ClientNodeName, Pid), _ = '_'};

%% @doc handles chat history backup requests: gets every sent or received message and forward that list to the sender
handle_call({history, Username}, _From, _) ->
  {reply, mnesiaFunctions:get_user_related_messages(Username), _ = '_'};

%% @doc handles logout: terminates rabbitMQ consuming session
handle_call({logout, Username}, _From, _) ->
  {reply, rabbitmq:terminate_consuming_session(Username), _='_'};

handle_call(_, _From, _) ->
  {reply, false, _ = '_'}.

handle_cast(_, _) ->
 {noreply, _ = '_'}.