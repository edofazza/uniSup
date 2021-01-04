%%%-------------------------------------------------------------------
%%% @copyright (C) 2021, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 02. gen 2021 10:52
%%%-------------------------------------------------------------------
-module(listener).

%% API
-export([init_listener/0]).

init_listener() ->
  Serv_pid = spawn(fun() -> listener_server_loop() end ),
  %for process registration
  register(unisup_server, Serv_pid),
  uniSupErlangServer:start_server(),
  Serv_pid.


listener_server_loop() ->
  receive
    {From, Action, Argument} -> spawn(fun() -> From ! uniSupErlangServer:call_server({Action, Argument}) end)
  end,
  listener_server_loop().
