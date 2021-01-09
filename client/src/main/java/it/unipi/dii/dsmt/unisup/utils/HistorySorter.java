package it.unipi.dii.dsmt.unisup.utils;

import it.unipi.dii.dsmt.unisup.beans.*;

import java.util.*;
import java.util.concurrent.*;

public class HistorySorter {
    private final int HOWMANY = 6;

    private List<Chat> histories= new ArrayList<>();
    private ExecutorService myExecutor;

    public HistorySorter(List<Chat> histories){
        this.histories=histories;
    }

    public HistorySorter(Chat toSort){
        this.histories.add(toSort);
    }

    public List<Chat> sort(){
        if(histories.size()<1)
            return new ArrayList<>();
        List<Chat> toReturn = new ArrayList<>();
        List<Future<Chat>> futures= new ArrayList<>();

        myExecutor = Executors.newFixedThreadPool(HOWMANY);
        for(int i=0; i<HOWMANY; i++){
            Callable<Chat> ordinator = new SortTask(histories.get(i));
            Future<Chat> future = myExecutor.submit(ordinator);
            futures.add(future);
        }
        for(int i=0; i<HOWMANY; i++){
            try{
                Chat c = futures.get(i).get();
                toReturn.add(c);
            }catch(InterruptedException | ExecutionException e){
                toReturn.add(histories.get(i));
            }
        }
        myExecutor.shutdown();
        return toReturn;
    }

    private static class SortTask implements Callable<Chat>{
        private Chat toOrder;

        SortTask(Chat toOrder){
            this.toOrder=toOrder;
        }

        @Override
        public Chat call() throws Exception {
            String username = toOrder.getUsernameContact();
            List<Message> messages = toOrder.getHistory();
            messages.sort(Comparator.comparing(Message::getTimestamp));
            //messages.sort((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));
            return new Chat(username, messages);
        }
    }

}
