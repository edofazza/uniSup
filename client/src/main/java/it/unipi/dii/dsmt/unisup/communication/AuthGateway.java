package it.unipi.dii.dsmt.unisup.communication;

import com.ericsson.otp.erlang.*;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.utils.HistorySorter;
import it.unipi.dii.dsmt.unisup.utils.LastMessageTracker;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class AuthGateway extends Gateway implements Authenticator{
    private static AuthGateway ref=null;
    public Future chatHistory=null;

    public static AuthGateway getInstance(){
        if(ref==null){
            ref= new AuthGateway();
        }
        return ref;
    }

    @Override
    public boolean login(User u) {
        prepareGateway(u.getUsername());
        Callable<Boolean> toRunLogin = new AuthGateway.LogTask(u);
        Callable<List<Chat>> toRunHistory = new AuthGateway.GetHistoryTask(u);
        chatHistory = addToExecutorAndGetFuture(toRunHistory);
        boolean result = (Boolean)addToExecutor(toRunLogin);
        return result;
    }

    @Override
    public List<Chat> getChatHistory(User u) {
        try {
            List<Chat> result = (List<Chat>) chatHistory.get();
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean register(User u) {
        prepareGateway(u.getUsername());
        Callable<Boolean> toRun = new AuthGateway.RegisterTask(u);
        boolean result = (Boolean)addToExecutor(toRun);
        return result;
    }

    @Override
    public boolean logout(User u) {
        Callable<Boolean> toRun = new AuthGateway.LogoutTask(u);
        boolean result = (Boolean)addToExecutor(toRun);
        LastMessageTracker.storeLastMessages();
        stopExecutor();
        return result;
    }

    private static class LogTask implements Callable<Boolean> {
        private User me;
        private final OtpMbox mbox;

        LogTask(User u){
            me=u;
            mbox = clientNode.createMbox();
        }

        @Override
        public Boolean call() throws Exception {
           OtpErlangAtom log = new OtpErlangAtom("log");
           OtpErlangPid pid = mbox.self();
           OtpErlangString username = new OtpErlangString(me.getUsername());
           OtpErlangString password = new OtpErlangString(me.getPassword());
           OtpErlangString myNodeName = new OtpErlangString(myName);
           OtpErlangTuple tuple = new OtpErlangTuple(new OtpErlangObject[]{receiveMessagesMailbox.self(), username, password, myNodeName});
           OtpErlangTuple reqMessage = new OtpErlangTuple(new OtpErlangObject[]{pid, log, tuple});
           mbox.send(serverRegisteredName, serverNodeName, reqMessage);

           OtpErlangAtom response = (OtpErlangAtom) mbox.receive();
           return response.booleanValue();
        }
    }

    private static class GetHistoryTask implements Callable<List<Chat>> {
        private User me;
        private final OtpMbox mbox;

        GetHistoryTask(User u){
            me=u;
            mbox = clientNode.createMbox();
        }

        @Override
        public List<Chat> call() throws Exception {
            OtpErlangPid pid = mbox.self();
            OtpErlangAtom history = new OtpErlangAtom("history");
            OtpErlangString username = new OtpErlangString(me.getUsername());
            OtpErlangTuple reqMessage = new OtpErlangTuple(new OtpErlangObject[]{pid, history, username});
            mbox.send(serverRegisteredName, serverNodeName, reqMessage);

            OtpErlangList response = (OtpErlangList)mbox.receive();
            LastMessageTracker.fetchLastMessages(me.getUsername());
            ArrayList<Chat> toReturn = new ArrayList<>();
            TreeMap<String, Chat> tree = new TreeMap<>();
            for(OtpErlangObject o: response.elements()){
                OtpErlangTuple message = (OtpErlangTuple)o;
                String sender = ((OtpErlangString)message.elementAt(0)).stringValue();
                String receiver = ((OtpErlangString)message.elementAt(1)).stringValue();
                String text = ((OtpErlangString)message.elementAt(2)).stringValue();
                Instant timestamp = Instant.parse(((OtpErlangString)message.elementAt(3)).stringValue());
                Message m = new Message(sender, receiver, text, timestamp);
                String key;
                if(sender.equals(me.getUsername()))
                    key=receiver;
                else if(receiver.equals(me.getUsername()))
                    key=sender;
                else
                    continue;
                Chat c;
                if(tree.containsKey(key)){
                    c = tree.get(key);
                    if((LastMessageTracker.getLastTimestamp(c.getUsernameContact())==null) || (LastMessageTracker.getLastTimestamp(c.getUsernameContact()).compareTo(m.getTimestamp()) < 0)) {
                        c.increaseUnreadMessages(1);
                        System.out.println("new unread message on existing chat" + m.getMessageId());
                    }
                }

                else{
                    c = new Chat(key);
                    if((LastMessageTracker.getLastTimestamp(c.getUsernameContact())==null) || (LastMessageTracker.getLastTimestamp(c.getUsernameContact()).compareTo(m.getTimestamp()) < 0)) {
                        c.increaseUnreadMessages(1);
                        System.out.println("new unread message " + m.getMessageId());
                    }
                }

                c.addMessageToHistory(m);
                tree.put(key, c);
            }
            for(Map.Entry<String, Chat> entry : tree.entrySet()){
                System.out.println(entry.getValue().getUsernameContact());
                entry.getValue().getHistory().forEach((m) -> System.out.println(m.getMessageId() + m.getSender() + m.getReceiver() + m.getText() + m.getTimestamp()) );
                toReturn.add(entry.getValue());
            }
            HistorySorter h = new HistorySorter(toReturn);
            return h.sort();
        }
    }

    private static class RegisterTask implements Callable<Boolean> {
        private User me;
        private final OtpMbox mbox;

        RegisterTask(User u){
            me=u;
            mbox = clientNode.createMbox();
        }

        @Override
        public Boolean call() throws Exception {
            OtpErlangPid pid = mbox.self();
            OtpErlangAtom register = new OtpErlangAtom("register");

            OtpErlangString username = new OtpErlangString(me.getUsername());
            OtpErlangString password = new OtpErlangString(me.getPassword());
            OtpErlangString myNodeName = new OtpErlangString(myName);
            OtpErlangTuple tuple = new OtpErlangTuple(new OtpErlangObject[]{receiveMessagesMailbox.self(), username, password, myNodeName});
            OtpErlangTuple reqMessage = new OtpErlangTuple(new OtpErlangObject[]{pid, register, tuple});
            mbox.send(serverRegisteredName, serverNodeName, reqMessage);

            OtpErlangAtom response = (OtpErlangAtom) mbox.receive();
            return response.booleanValue();
        }
    }

    private static class LogoutTask implements Callable<Boolean>{
        private User me;
        private final OtpMbox mbox;

        LogoutTask(User u){
            me=u;
            mbox = clientNode.createMbox();
        }

        @Override
        public Boolean call() throws Exception {
            OtpErlangPid pid = mbox.self();
            OtpErlangAtom logout = new OtpErlangAtom("logout");

            OtpErlangString username = new OtpErlangString(me.getUsername());
            OtpErlangTuple reqMessage = new OtpErlangTuple(new OtpErlangObject[]{pid, logout, username});
            mbox.send(serverRegisteredName, serverNodeName, reqMessage);

            OtpErlangAtom response = (OtpErlangAtom) mbox.receive();
            return response.booleanValue();
        }
    }

}
