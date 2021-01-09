package it.unipi.dii.dsmt.unisup.communication;

import com.ericsson.otp.erlang.*;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;
import it.unipi.dii.dsmt.unisup.beans.User;
import it.unipi.dii.dsmt.unisup.utils.HistorySorter;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;

public class AuthGateway extends Gateway implements Authenticator{
    private static AuthGateway ref=null;

    public static AuthGateway getInstance(){
        if(ref==null){
            ref= new AuthGateway();
        }
        return ref;
    }

    @Override
    public boolean login(User u) {
        prepareGateway(u.getUsername());
        Callable<Boolean> toRun = new AuthGateway.LogTask(u);
        boolean result = (Boolean)addToExecutor(toRun);
        return result;
    }

    @Override
    public List<Chat> getChatHistory(User u) {
        Callable<List<Chat>> toRun = new AuthGateway.GetHistoryTask(u);
        List<Chat> result = (List<Chat>)addToExecutor(toRun);
        return result;
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
            ArrayList<Chat> toReturn = new ArrayList<>();
            TreeMap<String, ArrayList<Message>> tree = new TreeMap<>();
            for(OtpErlangObject o: response.elements()){
                OtpErlangTuple message = (OtpErlangTuple)o;
                String sender = "a";//((OtpErlangString)message.elementAt(0)).stringValue();
                String receiver = "b";//((OtpErlangString)message.elementAt(1)).stringValue();
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
                ArrayList<Message> al;
                if(tree.containsKey(key))
                    al = tree.get(key);
                else
                    al = new ArrayList<>();
                al.add(m);
                tree.put(key, al);
            }
            for(Map.Entry<String, ArrayList<Message>> entry : tree.entrySet()){
                Chat c = new Chat(entry.getKey(), entry.getValue());
                toReturn.add(c);
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
