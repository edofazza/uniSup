%%%-------------------------------------------------------------------
%%% @author Edoardo Fazzari, Mirco Ramo, Sina Gholami
%%% @copyright (C) 2020, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 27. dic 2020 19:35
%%%-------------------------------------------------------------------
-module('UniSupErlangServer').

-behaviour(gen_server).

%% API
-export([start_server/0, call_server/1]).

%% gen_server callbacks
-export([init/1, handle_call/3]).

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
  %%save into mnesia
  %%get Receiver pid
  %%push into RabbitMq
  {ack, Msg_Id};
handle_call({log, {Pid, Username, Password, ClientNodeName}}, _From, _)->
  %%check username and password
  %%update mnesia physical addresses
  true;
handle_call({register, {Pid, Username, Password, ClientNodeName}}, _From, _)->
  %%check username and password
  %%update mnesia physical addresses
  true;
handle_call({contact, Username}, _From, _) ->
  %%check username
  %add contact
  true;
handle_call(_, _From, _) ->
  false.

%%%===================================================================
%%% private functions
%%%===================================================================

checkIntoMnesia(Username, Password) -> ok.

getAddressFromMnesia(Username) -> ok.

pushIntoRabbitMQ(Sender, Receiver, Message) ->ok.

%%%===================================================================
%%% Mnesia
%%%===================================================================


