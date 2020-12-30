package it.unipi.dii.dsmt.unisup.communication;

import com.ericsson.otp.erlang.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ErlangGateway {
    private static final String serverNodeName = "avg_node@localhost"; //configuration parameter
    private static final String serverRegisteredName = "avg_server"; //configuration parameter
    private static final String clientNodeName = "avg_client_node@localhost"; //configuration parameter
    private static OtpNode clientNode;  //initialized in constructor

    private static final int NUM_TASKS = 2; //configuration parameter
    private static final int POOL_SIZE = 2; //configuration parameter
    private static final ExecutorService myExecutor = Executors.newFixedThreadPool(POOL_SIZE);

    private final List<AvgClientTask> myTasks; //initialized in constructor


    //main method; the first arg is interpreted as the cookie
    public static void main(String[] args) throws Exception {
        String cookie = "";
        if (args.length > 0) {
            cookie = args[0];
        }

        ErlangGateway driver = new ErlangGateway(cookie);
        driver.doTest();
    }

    //constructor
    public ErlangGateway(String cookie) throws IOException {
        if (cookie!="") {
            clientNode = new OtpNode(clientNodeName, cookie);
        }
        else {
            clientNode = new OtpNode(clientNodeName);
        }

        myTasks = Stream.generate( () -> {
            try {
                return new AvgClientTask();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        })
                .limit(NUM_TASKS)
                .collect(toList());
    }

    //method to run a test
    private void doTest() {
        for (Runnable t : myTasks)
            myExecutor.execute(t);
        myExecutor.shutdown();
    }


    /**
     * A static nested class to implement a task acting as an independent Erlang client for avgserver
     * (a Runnable one).
     */
    private static class AvgClientTask implements Runnable {
        private static final int NUM_RUNS = 3; //configuration parameter
        private final OtpMbox mbox; //one mailbox per task
        private final Random rndGen = new Random();
        private static final AtomicInteger counter= new AtomicInteger(0); //shared counter
        private final int taskID;  //one ID per task

        public AvgClientTask() throws IOException {
            taskID = counter.getAndIncrement();
            mbox = clientNode.createMbox("default_mbox_"+ taskID);
            System.out.println("Created mailbox "+ mbox.getName());
        }

        @Override
        public void run() {
            int drawnNum;
            double curr_avg;


                drawnNum = rndGen.nextInt(100); //a random int is generated, for the req body
                try {

                    //composing the request message
                    OtpErlangInt num = new OtpErlangInt(drawnNum);
                    OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{this.mbox.self(), num});

                    //sending out the request
                    mbox.send(serverRegisteredName, serverNodeName, reqMsg);
                    System.out.println("Request sent by " + Thread.currentThread().toString() + " : " +
                            reqMsg.toString());

                    //blocking receive operation
                    OtpErlangObject msg = mbox.receive(30);
                    //getting the message content (a number)
                    OtpErlangDouble curr_avg_erlang = (OtpErlangDouble) msg;  //it is supposed to be a tuple...
                    curr_avg = curr_avg_erlang.doubleValue();
                    System.out.println("Response received on mailbox "+mbox.getName()+" : " + msg.toString() +
                            "Content: " + Double.toString(curr_avg));

                } catch (Exception e) {
                    System.out.println("ERROR!\n" + e);
                    e.printStackTrace();
                }

        }
    }
}
