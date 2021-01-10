@echo off
rem rebar3 compile
cd ./src
erlc listener.erl
erlc mnesiaFunctions.erl
erlc time_format.erl
erlc uniSupErlangServer.erl
erlc rabbitmq.erl
erl -sname unisup_server@localhost -setcookie unisup --mnesia dir ../../ldisc/scratch -run listener init_listener