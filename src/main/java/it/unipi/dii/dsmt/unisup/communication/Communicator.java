package it.unipi.dii.dsmt.unisup.communication;

import it.unipi.dii.dsmt.unisup.beans.Message;

public interface Communicator {

    boolean sendMessage(Message m, long timeout);

    Message receiveMessage();
}
