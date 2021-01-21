package it.unipi.dii.dsmt.unisup.communication;

import com.ericsson.otp.erlang.*;
import it.unipi.dii.dsmt.unisup.beans.Message;

import java.time.Instant;
import java.util.concurrent.Callable;

public class MessageGateway extends Gateway implements Communicator{
    private static MessageGateway ref=null;

    public static MessageGateway getInstance(){
        if(ref==null){
            ref = new MessageGateway();
        }
        return ref;
    }

    @Override
    public boolean sendMessage(Message m, int timeout) {
        Callable<Boolean> toRun = new SendTask(m, timeout);
        boolean result = (Boolean)addToExecutor(toRun);
        return result;
    }

    @Override
    public Message receiveMessage() {
        try {
            OtpErlangTuple incomingMessage;
            while(!Thread.currentThread().isInterrupted()){
                incomingMessage = (OtpErlangTuple) receiveMessagesMailbox.receive(5000);
                if(incomingMessage!=null){
                    String sender = ((OtpErlangString)incomingMessage.elementAt(1)).stringValue();
                    String receiver = ((OtpErlangString)incomingMessage.elementAt(2)).stringValue();
                    String text = ((OtpErlangString)incomingMessage.elementAt(3)).stringValue();
                    Instant timestamp = Instant.parse (((OtpErlangString)incomingMessage.elementAt(4)).stringValue());
                    Message m = new Message(sender, receiver, text, timestamp);
                    return m;
                }
                else
                    continue;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class SendTask implements Callable<Boolean> {
        private Message toSend;
        private final OtpMbox mbox;
        private final int timeout;

        SendTask(Message m, int timeout){
            toSend=m;
            mbox = clientNode.createMbox();
            this.timeout = timeout;
        }

        @Override
        public Boolean call() throws Exception {
            OtpErlangAtom message = new OtpErlangAtom("message");
            OtpErlangLong messageID = new OtpErlangLong(toSend.getMessageId());
            OtpErlangString sender = new OtpErlangString(toSend.getSender());
            OtpErlangString receiver = new OtpErlangString(toSend.getReceiver());
            OtpErlangString text = new OtpErlangString(toSend.getText());
            OtpErlangTuple messageInfo= new OtpErlangTuple(new OtpErlangObject[]{messageID, sender, receiver, text});
            OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[] {mbox.self(), message, messageInfo});
            mbox.send(serverRegisteredName, serverNodeName, reqMsg);

            OtpErlangObject ack = mbox.receive(timeout);
            if(ack==null) //timeout expired
                return false;

            OtpErlangTuple response = (OtpErlangTuple)ack;
            OtpErlangAtom ackAtom = (OtpErlangAtom) response.elementAt(0);
            OtpErlangLong ackedId = (OtpErlangLong) response.elementAt(1);
            if(!ackAtom.atomValue().equals("ack") || ackedId.longValue()!=toSend.getMessageId())
                return false;
            return true;
        }
    }

}
