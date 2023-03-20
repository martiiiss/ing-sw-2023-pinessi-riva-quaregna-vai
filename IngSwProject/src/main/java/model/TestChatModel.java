package model;

import java.util.ArrayList;
import java.util.HashMap;


/* if this implementation is ok, I think we'd have to add to Player some methods in which we let it call getMessages/setMessages...*/

public class TestChatModel {
    private ArrayList<String> messages;
    private HashMap<Player, ArrayList<String>> privateMessages;

    public TestChatModel() {
        messages = new ArrayList<String>();
        privateMessages = new HashMap<Player, ArrayList<String>>();
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void sendPrivateMessage(Player sender, Player recipient, String message) {
        String privateMessage = sender + " to " + recipient + ": " + message;
        if (!privateMessages.containsKey(sender)) {
            privateMessages.put(sender, new ArrayList<String>());
        }
        privateMessages.get(sender).add(privateMessage);
        if (!privateMessages.containsKey(recipient)) {
            privateMessages.put(recipient, new ArrayList<String>());
        }
        privateMessages.get(recipient).add(privateMessage);
    }

    public ArrayList<String> getPrivateMessages(String player) {
        if (privateMessages.containsKey(player)) {
            return privateMessages.get(player);
        } else {
            return new ArrayList<String>();
        }
    }
}