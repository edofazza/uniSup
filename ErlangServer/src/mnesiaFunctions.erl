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
-export([init/0, login/4, register/4, all_user/0, readTest/1]).

-include_lib("stdlib/include/qlc.hrl").
-include("headers/records.hrl").

%%%===================================================================
%%% API
%%%===================================================================
init() ->
  mnesia:create_schema([node()]),
  mnesia:start(),
  mnesia:create_table(unisup_users,
    [{attributes, record_info(fields, unisup_users)}]),
      %{disc_copies, node()},
      %{type, ordered_set}]),
  mnesia:create_table(unisup_messages, [{attributes, record_info(fields, unisup_messages)}]).
    %{disc_copies, node()},
    %{type, bag}]).


%%%===================================================================
%%% USER OPERATIONS
%%%===================================================================

add_user(Username, Password, NodeName, Pid) ->
  Fun = fun() ->
    mnesia:write(#unisup_users{username = Username,
                        password = Password,
                        nodeName = NodeName,
                        pid = Pid
                        })
        end,
    mnesia:activity(transaction, Fun).

update_user(Username, NodeName, Pid) ->
  F = fun() ->
      [User] = mnesia:read(unisup_users, Username),
      mnesia:write(User#unisup_users{nodeName = NodeName, pid = Pid})
      end,
  mnesia:activity(transaction, F).

login(Username, Password, NodeName, Pid) ->
  F = fun() ->
        case mnesia:read({unisup_users, Username}) =:= [] of
          true -> % User present
            %% GET PASSWORD
            {_, [_, Pass, _, _]} = mnesia:select(unisup_users, [{username = Username, _ = '_', _ = '_', _ = '_'}]),
            %%Q = qlc:q([{R#unisup_users.username, R#unisup_users.password} || R <- mnesia:table(base)])
            %% CHECK IF THE PASSWORD IS CORRECT
            case [Password] == Pass of
               %% If the password is equal to the one inserted then I update the data in Mnesia
               %% and I return true. Otherwise, I return false
               true ->
                 update_user(Username, NodeName, Pid),
                 true;
               false -> false
            end;
          false ->
            false
        end
    end,
    mnesia:activity(transaction, F).

register(Username, Password, NodeName, Pid) ->
  F = fun() ->
        case mnesia:read({unisup_users, Username}) =:= [] of
          true -> % User not present
            add_user(Username, Password, NodeName, Pid),
            true;
          false ->
            false
        end
    end,
    mnesia:activity(transaction, F).

readTest(Username) ->
  F = fun() ->
    mnesia:read({unisup_users, Username})
      end,
  mnesia:activity(transaction, F).

all_user() ->
  F = fun() ->
    Q = qlc:q([{E#unisup_users.username, E#unisup_users.password, E#unisup_users.nodeName, E#unisup_users.pid} || E <- mnesia:table(unisup_users)]),
    qlc:e(Q)
      end,
  mnesia:transaction(F).

