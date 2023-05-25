package distributed.messages;

import util.Event;
import java.io.Serializable;

public class Message implements Serializable{
    private static final long serialVersionUID = -39139710239L;
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
