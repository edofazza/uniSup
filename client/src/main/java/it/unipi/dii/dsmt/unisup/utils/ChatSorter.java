package it.unipi.dii.dsmt.unisup.utils;

import it.unipi.dii.dsmt.unisup.beans.Chat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatSorter {
    private List<Chat> chats;

    public ChatSorter(List<Chat> chats){
        this.chats=chats;
    }

    public List<Chat> sort(){
        if(chats ==null || chats.size()<1)
            return new ArrayList<>();
        //chats.sort((o2, o1) -> o1.getHistory().get(o1.getHistory().size()-1).getTimestamp().compareTo(o2.getHistory().get(o2.getHistory().size()-1).getTimestamp()));
        chats.sort(Comparator.comparing(o -> o.getHistory().get(o.getHistory().size()-1).getTimestamp(), Comparator.reverseOrder()));
        return chats;
    }
}
