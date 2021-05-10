# uniSup

UniSup is chat application that allows users to exchange short text messages.
This application is developed by using Erlang and Java.
The server is implemented in Erlang. The GUI is developed using JavaFX while the connection between the server and cliend is handled by JInterface. We also used RabbitMQ to handle the asynchronous massage passing between two users. Every communication pass through the server. This may agains the user privacy, but the aim of this project is to use RabbitMQ in Erlang. To store the data we used mnesia which is a key value database. The main technologies used in the the application are as follow:

1- gen_server behaviour 

2- listener behaviour

3- rebar3 for compiling the Erlang code

4- rabbitmq direct exchange

5- JInterface

6- ExecutorService

7- Synchronization

8- mnesia to store the data



For more details about the implementaion you can check the documentation directory. 

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Fedofazza%2FuniSup&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=false)](https://hits.seeyoufarm.com)