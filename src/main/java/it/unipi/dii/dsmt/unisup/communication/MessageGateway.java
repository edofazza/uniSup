package it.unipi.dii.dsmt.unisup.communication;

import com.ericsson.otp.erlang.*;
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
        Callable<Boolean> toRun = new SendTask(m, timeout);
        boolean result = (Boolean)addToExecutor(toRun);
        //save into db
        return result;
    }

    @Override
    public Message receiveMessage(String usernameSender) {
        //instanciate task to run
        //save into db
        return null;
    }

    private static class SendTask implements Callable<Boolean> {
        private Message toSend;
        private final OtpMbox mbox;
        private final long timeout;

        SendTask(Message m, long timeout){
            toSend=m;
            mbox = clientNode.createMbox();
            this.timeout = timeout;
        }

        @Override
        public Boolean call() throws Exception {
            OtpErlangAtom message = new OtpErlangAtom("message");
            OtpErlangInt messageID = new OtpErlangInt(toSend.getMessageId());
            OtpErlangString sender = new OtpErlangString(toSend.getSender());
            OtpErlangString receiver = new OtpErlangString(toSend.getReceiver());
            OtpErlangString text = new OtpErlangString(toSend.getText());
            OtpErlangTuple messageInfo= new OtpErlangTuple(new OtpErlangObject[]{messageID, sender, receiver, text});
            OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[] {message, mbox.self(), messageInfo});
            mbox.send(serverRegisteredName, serverNodeName, reqMsg);

            OtpErlangObject ack = mbox.receive(timeout);
            if(ack==null) //timeout expired
                return false;

            OtpErlangTuple response = (OtpErlangTuple)ack;
            OtpErlangAtom ackAtom = (OtpErlangAtom) response.elementAt(0);
            OtpErlangInt ackedId = (OtpErlangInt) response.elementAt(1);
            if(!ackAtom.atomValue().equals("ack") || ackedId.intValue()!=toSend.getMessageId())
                return false;
            return true;
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
