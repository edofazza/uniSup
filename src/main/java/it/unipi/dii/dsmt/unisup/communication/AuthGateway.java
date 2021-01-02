package it.unipi.dii.dsmt.unisup.communication;

import com.ericsson.otp.erlang.*;
import it.unipi.dii.dsmt.unisup.beans.User;

import java.util.concurrent.Callable;

public class AuthGateway extends Gateway implements Authenticator{
    private static AuthGateway ref;

    public static AuthGateway getInstance(){
        if(ref==null){
            prepareGateway();
            ref= new AuthGateway();
        }
        return ref;
    }

    @Override
    public boolean login(User u) {
        Callable<Boolean> toRun = new AuthGateway.LogTask(u);
        boolean result = (Boolean)addToExecutor(toRun);
        //import chats
        return result;
    }

    @Override
    public boolean register(User u) {
        Callable<Boolean> toRun = new AuthGateway.RegisterTask(u);
        boolean result = (Boolean)addToExecutor(toRun);
        return result;
    }

    @Override
    public boolean logout(User u) {
        //export chats
        return true;
    }

    @Override
    public boolean addContact(String username) {
        Callable<Boolean> toRun = new AuthGateway.NewContactTask(username);
        boolean result = (Boolean)addToExecutor(toRun);
        //save into db
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
           OtpErlangString myNodeName = new OtpErlangString(clientNodeName);
           OtpErlangTuple tuple = new OtpErlangTuple(new OtpErlangObject[]{pid, username, password, myNodeName});
           OtpErlangTuple reqMessage = new OtpErlangTuple(new OtpErlangObject[]{log, tuple});
           mbox.send(serverRegisteredName, serverNodeName, reqMessage);

           OtpErlangBoolean response = (OtpErlangBoolean) mbox.receive();
           return response.booleanValue();
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
            OtpErlangAtom register = new OtpErlangAtom("register");
            OtpErlangPid pid = mbox.self();
            OtpErlangString username = new OtpErlangString(me.getUsername());
            OtpErlangString password = new OtpErlangString(me.getPassword());
            OtpErlangString myNodeName = new OtpErlangString(clientNodeName);
            OtpErlangTuple tuple = new OtpErlangTuple(new OtpErlangObject[]{pid, username, password, myNodeName});
            OtpErlangTuple reqMessage = new OtpErlangTuple(new OtpErlangObject[]{register, tuple});
            mbox.send(serverRegisteredName, serverNodeName, reqMessage);

            OtpErlangBoolean response = (OtpErlangBoolean) mbox.receive();
            return response.booleanValue();
        }
    }

    private static class NewContactTask implements Callable<Boolean> {
        private String newContact;
        private final OtpMbox mbox;

        NewContactTask(String contact){
            this.newContact=contact;
            mbox=clientNode.createMbox();
        }

        @Override
        public Boolean call() throws Exception {
            OtpErlangAtom contact = new OtpErlangAtom("contact");
            OtpErlangString contactName = new OtpErlangString(newContact);
            OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{contact, contactName});
            mbox.send(serverRegisteredName, serverNodeName, reqMsg);

            OtpErlangBoolean response = (OtpErlangBoolean) mbox.receive();
            return response.booleanValue();
        }
    }
}
