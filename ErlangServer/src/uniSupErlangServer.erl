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

call_server(Content) ->
  gen_server:call(unisup_gen_server, Content).


%%%===================================================================
%%% gen_server callbacks
%%%===================================================================

%% @private
%% @doc Initializes the server
init([]) ->
  ok.

%% @private
%% @doc Handling call messages
handle_call({message, {Msg_Id, Sender, Receiver, Text}}, _From, _)->
  case mnesiaFunctions:insert_new_message(Sender, Receiver, Text) of    %%maybe to do at consuming time from RabbitMQ
    true->
      ReceiverPid = mnesiaFunctions:retrieve_pid(Receiver),
      ReceiverNodeName = mnesiaFunctions:retrieve_nodename(Receiver),
      %%push into RabbitMq({Msg_Id, Sender, Receiver, Text, Timestamp}, ReceiverPid, ReceiverNodeName)
      {ack, Msg_Id};
    false ->
      {nack, Msg_Id}
  end;
handle_call({log, {Pid, Username, Password, ClientNodeName}}, _From, _)->
  mnesiaFunctions:login(Username, Password, ClientNodeName, Pid);

handle_call({register, {Pid, Username, Password, ClientNodeName}}, _From, _)->
  mnesiaFunctions:register(Username, Password, ClientNodeName, Pid);

%%handle_call({contact, Username}, _From, _) ->
  %%check username
  %add contact
  %%true;
handle_call({history, Username}, _From, _) ->
  mnesiaFunctions:get_user_related_messages(Username);
handle_call(_, _From, _) ->
  false.

handle_cast(_, _) -> noreply.