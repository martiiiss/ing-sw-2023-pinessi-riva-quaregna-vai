package distributed.messages;

import util.Event;
import util.Error;
import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = -39139710239L;
    private Event event;
    private Object obj;

    public Message(Object obj, Event event) {
        this.obj = obj;
        this.event = event;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Object getObj() {
        return this.obj;
    }

    public Event getMessageEvent() {
        return this.event;
    }

    public void setMessageEvent(Event event) {
        this.event = event;
    }
}
