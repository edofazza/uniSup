package it.unipi.dii.dsmt.unisup.beans;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Rabbimq implements Runnable{

    private static final String HOST = "localhost";
    private final ConnectionFactory connection;
    private final String queue_name;
    private Channel sendChannel;
    private Channel receiveChannel;
    private DeliverCallback deliverCallback;

    public Rabbimq(String queue_name){
        this.queue_name = queue_name;
        this.connection = new ConnectionFactory();
        this.connection.setHost(HOST);
        try {
            this.receiveChannel = this.connection.newConnection().createChannel();
            this.receiveChannel.queueDeclare(this.queue_name, true, false, false, null);
            this.receiveChannel.basicQos(1);
            this.deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };

            this.sendChannel = this.connection.newConnection().createChannel();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (TimeoutException timeoutException) {
            timeoutException.printStackTrace();
        }


    }

    public void receiveMessage(){
        try {
            this.receiveChannel.basicConsume(this.queue_name, true, this.deliverCallback, consumerTag -> { });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String receiverQueueName, Message msg){
        if (!receiveChannel.isOpen()) {
            try {
                this.receiveChannel = this.connection.newConnection().createChannel();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        String message = "Hello World!";
        try {
            this.receiveChannel.basicPublish("", receiverQueueName, null, message.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(" [x] Sent '" + message + "'");

    }

    @Override
    public void run() {
        receiveMessage();
    }
}
