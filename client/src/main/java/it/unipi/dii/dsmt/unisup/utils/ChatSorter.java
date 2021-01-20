package it.unipi.dii.dsmt.unisup.utils;

import it.unipi.dii.dsmt.unisup.beans.Chat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ChatSorter {
    private List<Chat> chats= new ArrayList<>();

    public ChatSorter(List<Chat> chats){
        this.chats=chats;
    }

    public List<Chat> sort(){
        if(chats.size()<1)
            return new ArrayList<>();
        //chats.sort((o1, o2) -> o1.getHistory().get(o1.getHistory().size()).getTimestamp().compareTo(o2.getHistory().get(o2.getHistory().size()).getTimestamp()));
        chats.sort(Comparator.comparing(o -> o.getHistory().get(o.getHistory().size()).getTimestamp()));
        return chats;
    }
}
