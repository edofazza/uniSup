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
-export([init/1, handle_call/3, handle_cast/2, handle_info/2, terminate/2,
  code_change/3]).

-define(SERVER, ?MODULE).

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
handle_call({}, _From, )

%% @private
%% @doc Handling cast messages
-spec(handle_cast(Request :: term(), State :: #'UniSupErlangServer'_state{}) ->
{noreply, NewState :: #'UniSupErlangServer'_state{}} |
{noreply, NewState :: #'UniSupErlangServer'_state{}, timeout() | hibernate} |
{stop, Reason :: term(), NewState :: #'UniSupErlangServer'_state{}}).
handle_cast(_Request, State = #'UniSupErlangServer'_state{}) ->
{noreply, State}.

%% @private
%% @doc Handling all non call/cast messages
-spec(handle_info(Info :: timeout() | term(), State :: #'UniSupErlangServer'_state{}) ->
{noreply, NewState :: #'UniSupErlangServer'_state{}} |
{noreply, NewState :: #'UniSupErlangServer'_state{}, timeout() | hibernate} |
{stop, Reason :: term(), NewState :: #'UniSupErlangServer'_state{}}).
handle_info(_Info, State = #'UniSupErlangServer'_state{}) ->
{noreply, State}.

%% @private
%% @doc This function is called by a gen_server when it is about to
%% terminate. It should be the opposite of Module:init/1 and do any
%% necessary cleaning up. When it returns, the gen_server terminates
%% with Reason. The return value is ignored.
-spec(terminate(Reason :: (normal | shutdown | {shutdown, term()} | term()),
    State :: #'UniSupErlangServer'_state{}) -> term()).
terminate(_Reason, _State = #'UniSupErlangServer'_state{}) ->
ok.

%% @private
%% @doc Convert process state when code is changed
-spec(code_change(OldVsn :: term() | {down, term()}, State :: #'UniSupErlangServer'_state{},
Extra :: term()) ->
{ok, NewState :: #'UniSupErlangServer'_state{}} | {error, Reason :: term()}).
code_change(_OldVsn, State = #'UniSupErlangServer'_state{}, _Extra) ->
{ok, State}.

%%%===================================================================
%%% Internal functions
%%%===================================================================

checkIntoMnesia(Username, Password) -> ok.

getAddressFromMnesia(Username) -> ok.

pushIntoRabbitMQ(Sender, Receiver, Message) ->ok.
