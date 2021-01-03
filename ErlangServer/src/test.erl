%%%-------------------------------------------------------------------
%%% @author Sina
%%% @copyright (C) 2021, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 03. Jan 2021 12:25 PM
%%%-------------------------------------------------------------------
-module(test).
-author("Sina").
-include_lib("amqp_client/include/amqp_client.hrl").
%% API
-export([st/0]).

%%%  erl -pz ./amqp_client ./rabbit_common ./amqp_client/ebin ./rabbit_common/ebin ./lager/ebin ./goldrush/ebin ./jsx/ebin ./ranch/ebin ./recon/ebin ./credentials_obfuscation/ebin

delete_queue(Channel, Queue_Name) ->
  Delete = #'queue.delete'{queue = <<Queue_Name>>},
  #'queue.delete_ok'{} = amqp_channel:call(Channel, Delete).

%Queue_Name should has <<String>> format
create_queue(Channel, Queue_Name) ->
  Declare = #'queue.declare'{
    queue = list_to_binary(Queue_Name),
    durable = true
  },
  #'queue.declare_ok'{} = amqp_channel:call(Channel, Declare),
  push_to_rabbitmq(Channel, Queue_Name, "asdasdasd").

push_to_rabbitmq(Channel, Queue_Name, Message) ->
  Payload = list_to_binary(Message), %converting String to bits
  Publish = #'basic.publish'{exchange = <<>>, routing_key = list_to_binary(Queue_Name)},
  Props = #'P_basic'{delivery_mode = 2}, %% persistent message
  Msg = #amqp_msg{props = Props, payload = Payload},
  amqp_channel:cast(Channel, Publish, Msg).

st()->
  {ok, Connection} = amqp_connection:start(#amqp_params_network{}),
  {ok, Channel} = amqp_connection:open_channel(Connection),
  create_queue(Channel, "Sina").
