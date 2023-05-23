package distributed.messages;

import util.Event;

public class Message {

    private Event event;
    private String nickname;

    public Message(String nickname, Event event){
        this.nickname = nickname;
        this.event = event;

    }

    public String getNickname(){
        return this.nickname;
    }
    public Event getMessageType(){
        return this.event;
    }


}
