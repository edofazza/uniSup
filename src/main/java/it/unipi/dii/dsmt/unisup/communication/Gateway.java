package it.unipi.dii.dsmt.unisup.communication;

import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;
import it.unipi.dii.dsmt.unisup.beans.Message;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Gateway {
    private static final String serverNodeName = "unisup_server@localhost"; //configuration parameter
    private static final String serverRegisteredName = "unisup_server"; //configuration parameter
    private static OtpMbox ackMbox;
    private static OtpMbox receiveMbox;
    private static final String clientNodeName = "unisup_client_node@localhost"; //configuration parameter
    private static final String applicationCookie = "unisup";
    private static OtpNode clientNode;
    private static Gateway ref;
    private static final ExecutorService myExecutor = Executors.newCachedThreadPool();

    public static Gateway getInstance(String cookie){
        if(ref == null){
            ref = new Gateway();
        }
        return ref;
    }

    private Gateway(){
        try {
            clientNode = new OtpNode(clientNodeName, applicationCookie);
            ackMbox = clientNode.createMbox("ackMbox");
            receiveMbox = clientNode.createMbox("receiveMbox");
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }



    private static class CommunicateTask implements Runnable{
        private final int taskId;

        CommunicateTask(Message m){
            taskId=m.getMessageId();
        }

        @Override
        public void run() {

        }
    }
}
