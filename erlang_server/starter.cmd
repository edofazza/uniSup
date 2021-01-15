@echo off
rem rebar3 compile
cd ./src
erlc listener.erl
erlc mnesiaFunctions.erl
erlc time_format.erl
erlc uniSupErlangServer.erl