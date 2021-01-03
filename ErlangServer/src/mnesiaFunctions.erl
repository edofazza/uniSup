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
-export([init/0, login/4, register/4]).

-include_lib("stdlib/include/qlc.hrl").
-include("headers/records.hrl").

%%%===================================================================
%%% API
%%%===================================================================
init() ->
  mnesia:create_schema([node()]),
  mnesia:start(),
  mnesia:create_table(unisup_users,
    [{attributes, record_info(fields, unisup_users)},
      {disc_copies, node()},
      {type, ordered_set}]),
  mnesia:create_table(unisup_messages, [{attributes, record_info(fields, unisup_messages)},
    {disc_copies, node()},
    {type, bag}]).


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

login(Username, Password, NodeName, Pid) ->
  F = fun() ->
        case mnesia:read({unisup_users, Username}) =:= [] of
          true -> % User present
            %% GET PASSWORD
            Pass = select(qlc:q([X || X <- mnesia:table(unisup_users),
              (X#unisup_users.username == Username)])),

            %% CHECK IF THE PASSWORD IS CORRECT
            case Password == Pass of
               %% If the password is equal to the one inserted then I update the data in Mnesia
               %% and I return true. Otherwise, I return "password_not_correct"
               true ->
                 update_user(Username, NodeName, Pid),
                 true;
               false -> password_not_correct
            end;
          false ->
            user_not_present
        end
    end,
    mnesia:activity(transaction, F).

register(Username, Password, NodeName, Pid) ->
  F = fun() ->
        case mnesia:read({unisup_users, Username}) =:= [] of
          true -> % User present
            username_already_present;
          false ->
            add_user(Username, Password, NodeName, Pid)
        end
    end,
    mnesia:activity(transaction, F).

