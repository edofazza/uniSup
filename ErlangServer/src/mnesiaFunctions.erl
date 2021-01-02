%%%-------------------------------------------------------------------
%%% @author edoardo
%%% @copyright (C) 2021, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 02. gen 2021 18:05
%%%-------------------------------------------------------------------
-module(mnesiaFunctions).
-author("edoardo").

%% API
-export([init/0]).

-include_lib("stdlib/include/qlc.hrl").
-include("headers/records.hrl").

init() ->
  mnesia:create_schema([node()]),
  mnesia:start(),
  mnesia:create_table(users, [{attributes, record_info(fields, unisup_user)}]),
  mnesia:create_table(messages, [{attributes, record_info(fields, unisup_message)}]).


