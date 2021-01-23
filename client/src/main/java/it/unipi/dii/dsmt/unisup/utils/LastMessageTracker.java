package it.unipi.dii.dsmt.unisup.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipi.dii.dsmt.unisup.NewMain;
import it.unipi.dii.dsmt.unisup.beans.Chat;
import it.unipi.dii.dsmt.unisup.beans.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;


public class LastMessageTracker {
    private static Map<String, String> lastMessages=new HashMap<>();
    private static String jsonFile = "client/lastMessages" + NewMain.getUserLogged().getUsername() + ".json";

    public static void fetchLastMessages(){
        try {
            jsonFile = "client/lastMessages" + NewMain.getUserLogged().getUsername() + ".json";
            File f = new File(jsonFile);
            f.createNewFile();
            if(f.length()==0)
                return;
            Map<String, String> result = new ObjectMapper().readValue(f, HashMap.class);
            lastMessages = result;
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void storeLastMessages(){
        try(FileWriter fw = new FileWriter(jsonFile)){
            String json = new ObjectMapper().writeValueAsString(lastMessages);
            fw.write(json);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static Instant getLastTimestamp(String contact){
        String instantString = lastMessages.get(contact);
        if(instantString==null || instantString.equals(""))
            return null;
        return Instant.parse(lastMessages.get(contact));
    }

    public static void setLastTimestamp(String contact, Instant timestamp){
        lastMessages.put(contact, timestamp.toString());
    }

    public static void setLastTimestamp(Message m){
        String contact = m.getSender().equals(NewMain.getUserLogged().getUsername()) ? m.getReceiver() : m.getSender();
        lastMessages.put(contact, m.getTimestamp().toString());
    }

    public static void setLastTimestamp(Chat c){
        lastMessages.put(c.getUsernameContact(), c.getHistory().get(c.getHistory().size()-1).getTimestamp().toString());
    }

}
