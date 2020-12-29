package it.unipi.dii.dsmt.unisup.communication;

import it.unipi.dii.dsmt.unisup.beans.Message;

import java.util.concurrent.Callable;

public class MessageGateway extends Gateway implements Communicator{
    private static MessageGateway ref;

    public static MessageGateway getInstance(){
        if(ref==null){
            prepareGateway();
            ref= new MessageGateway();
        }
        return ref;
    }

    @Override
    public boolean sendMessage(Message m, long timeout) {
        //instanciate task to run
        //save into db
        return false;
    }

    @Override
    public Message receiveMessage(String usernameSender) {
        //instanciate task to run
        //save into db
        return null;
    }

    private static class SendTask implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {
            //SEND PROTOCOL
            return false;
        }
    }

    private static class ReceiveTask implements Callable<Message> {

        @Override
        public Message call() throws Exception {
            //receive protocol
            return null;
        }
    }
}
