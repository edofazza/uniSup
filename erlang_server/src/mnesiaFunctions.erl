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
-export([init/0, login/4, register/4, all_user/0, readTest/1, insert_new_message/3,
  all_messages/0, get_user_related_messages/1, retrieve_nodename/1, retrieve_pid/1,
  is_user_present/1]).

-include_lib("stdlib/include/qlc.hrl").
-include("headers/records.hrl").

%%%===================================================================
%%% API
%%%===================================================================
init() ->
  mnesia:create_schema([node()]),
  mnesia:start(),
  case mnesia:wait_for_tables([unisup_messages, unisup_users], 5000) == ok of
    true ->
      ok;
    false ->
      mnesia:create_table(unisup_users,
        [{attributes, record_info(fields, unisup_users)},
          {disc_copies, [node()]}
        ]),
      mnesia:create_table(unisup_messages,
        [{attributes, record_info(fields, unisup_messages)},
          {disc_copies, [node()]}
        ])
  end.


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
      false -> % User present
        %% GET PASSWORD
        [{unisup_users, Username, Pass, _, _}] = mnesia:read({unisup_users, Username}),

        %% CHECK IF THE PASSWORD IS CORRECT
        case Password =:= Pass of
          %% If the password is equal to the one inserted then I update the data in Mnesia
          %% and I will return true. Otherwise, I will return false
          true ->
            update_user(Username, NodeName, Pid),
            true;
          false -> false
        end;
      true ->
        false
    end
      end,
  mnesia:activity(transaction, F).

is_user_present(Username) ->
  F = fun() ->
    case mnesia:read({unisup_users, Username}) =:= [] of
      true ->
        false;
      false ->
        true
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

retrieve_nodename(Username) ->
  F = fun() ->
    [{unisup_users, _, _, NodeName, _}] = mnesia:read({unisup_users, Username}),
    NodeName
      end,
  mnesia:activity(transaction, F).

retrieve_pid(Username) ->
  F = fun() ->
    [{unisup_users, _, _, _, Pid}] = mnesia:read({unisup_users, Username}),
    Pid
      end,
  mnesia:activity(transaction, F).


%%%===================================================================
%%% MESSAGE OPERATIONS
%%%===================================================================

insert_new_message(Sender, Receiver, Text) ->
  Fun = fun() ->
    %% CHECK IF THE SENDER AND THE RECEIVER DO EXIST
    case mnesia:read({unisup_users, Sender}) =:= []  of
      true ->
        false;
      false ->
        case mnesia:read({unisup_users, Receiver}) =:= [] of
          true ->
            false;
          false ->
            add_message(Sender, Receiver, Text)
        end
    end
        end,
  mnesia:activity(transaction, Fun).

add_message(Sender, Receiver, Text) ->
  Fun = fun() ->
    Timestamp = time_format:format_utc_timestamp(),
    mnesia:write(#unisup_messages{
      sender = {Sender, time_format:format_utc_timestamp()},
      receiver = Receiver,
      text = Text,
      timestamp = Timestamp
    }),
    Timestamp
        end,
  mnesia:activity(transaction, Fun).

all_messages() ->
  F = fun() ->
    Q = qlc:q([{E#unisup_messages.sender, E#unisup_messages.receiver, E#unisup_messages.text, E#unisup_messages.timestamp} || E <- mnesia:table(unisup_messages)]),
    qlc:e(Q)
      end,
  mnesia:transaction(F).

message_sent(Username) ->
  F = fun() ->
    Q = qlc:q([{Username, E#unisup_messages.receiver, E#unisup_messages.text, E#unisup_messages.timestamp} || E <- mnesia:table(unisup_messages), hd(tuple_to_list(E#unisup_messages.sender)) == Username]),
    qlc:e(Q)
      end,
  mnesia:transaction(F).

get_message_sent(Username) ->
  {_, Messages} = message_sent(Username),
  Messages.

message_received(Username) ->
  F = fun() ->
    Q = qlc:q([{hd(tuple_to_list(E#unisup_messages.sender)), Username, E#unisup_messages.text, E#unisup_messages.timestamp} || E <- mnesia:table(unisup_messages), E#unisup_messages.receiver == Username]),
    qlc:e(Q)
      end,
  mnesia:transaction(F).

get_message_received(Username) ->
  {_, Messages} = message_received(Username),
  Messages.


%% FORMAT:
%%      [{"edo","pippo","Test1","3 Jan 2021 13:32:40.857139"}, ....]
get_user_related_messages(Username) ->
  get_message_sent(Username) ++ get_message_received(Username).