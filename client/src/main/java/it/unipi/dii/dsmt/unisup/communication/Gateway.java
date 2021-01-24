package it.unipi.dii.dsmt.unisup.communication;

import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

import java.io.IOException;
import java.util.concurrent.*;

public abstract class Gateway {
    protected static final String serverNodeName = "unisup_server@localhost"; //configuration parameter
    protected static String serverRegisteredName = "unisup_server"; //configuration parameter
    protected static final String clientNodeName = "unisup_client_node@localhost"; //configuration parameter
    protected static String myName;
    protected static OtpMbox receiveMessagesMailbox;
    protected static String applicationCookie = "unisup";
    protected static OtpNode clientNode;
    private static ExecutorService myExecutor;


    protected static void prepareGateway(String username){
        try {
            myName = username + "_" + clientNodeName;
            if (clientNode != null)
                clientNode.close();
            clientNode = new OtpNode(myName, applicationCookie);
            if(myExecutor!=null)
                myExecutor.shutdown();
            myExecutor= Executors.newCachedThreadPool();
            if(receiveMessagesMailbox!=null)
                receiveMessagesMailbox.close();
            receiveMessagesMailbox = clientNode.createMbox(username + "_mailbox");
            receiveMessagesMailbox.registerName(receiveMessagesMailbox.getName());
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public Object addToExecutor(Callable task){
        try {
            return myExecutor.submit(task).get();
        } catch (InterruptedException e) {
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Future addToExecutorAndGetFuture(Callable task){
        return myExecutor.submit(task);
    }

    public void stopExecutor(){
        myExecutor.shutdown();
        try {
            if (!myExecutor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                myExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            myExecutor.shutdownNow();
        }
    }

    public void setApplicationCookie(String cookie){
        applicationCookie=cookie;
    }
}
