%%%-------------------------------------------------------------------
%%% @author edoardo
%%% @copyright (C) 2021, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 03. gen 2021 09:15
%%%-------------------------------------------------------------------
-module(time_format).
-author("edoardo").

%% API
-export([format_utc_timestamp/0]).

format_utc_timestamp() ->
  TS = {_, _, Micro } = os:timestamp(),
  {{Year, Month, Day}, {Hour, Minute, Second}} = calendar:now_to_universal_time(TS),
  %Mstr = element(Month, {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug" ,"Sep", "Oct", "Nov", "Dec"}),
  %erl_types:atom_to_string(Day) ++ " " ++ Mstr ++ " " ++
  %erl_types:atom_to_string(Year) ++ " " ++ erl_types:atom_to_string(Hour) ++ ":" ++
  %erl_types:atom_to_string(Minute) ++ ":" ++ erl_types:atom_to_string(Second) ++ "." ++
  %erl_types:atom_to_string(Micro).
  StringMonth = case Month < 10 of
    true ->
      "0" ++ erl_types:atom_to_string(Month);
    false ->
      erl_types:atom_to_string(Month)
  end,
  StringDay = case Day < 10 of
                true ->
                  "0" ++ erl_types:atom_to_string(Day);
                false ->
                  erl_types:atom_to_string(Day)
              end,
  StringHour = case Hour < 10 of
                 true ->
                   "0" ++ erl_types:atom_to_string(Hour);
                 false ->
                   erl_types:atom_to_string(Hour)
               end,
  StringMinute = case Minute < 10 of
                   true ->
                     "0" ++ erl_types:atom_to_string(Minute);
                   false ->
                     erl_types:atom_to_string(Minute)
                 end,
  StringSecond = case Second < 10 of
                   true ->
                     "0" ++ erl_types:atom_to_string(Second);
                   false ->
                     erl_types:atom_to_string(Second)
                 end,
  erl_types:atom_to_string(Year) ++ "-" ++ StringMonth ++ "-" ++
    StringDay ++ "T" ++ StringHour ++ ":" ++ StringMinute ++ ":" ++ StringSecond ++ "." ++
    string:substr(erl_types:atom_to_string(Micro), 1, 2) ++ "Z".