package distributed.messages;

import util.Event;
import java.io.Serializable;

/**Class that represents a Message. <br>
 * Messages are composed following this structure: <br>
 * <code>
 *     Message ({@link Object}, {@link Event})
 * </code>*/
public class Message implements Serializable {
    private static final long serialVersionUID = -3913971069968788739L;
    private Event event;
    private Object obj;

    /**
     * <p>
     *     Constructor of he Class.<br>
     *     This sets the {@code Object} and the {@code Event} based on the parameters.
     * </p>
     * @param event is the Event that has to be set
     * @param obj is the Object that has to be set*/
    public Message(Object obj, Event event) {
        this.obj = obj;
        this.event = event;
    }

    /**
     * <p>
     *     Method that given a parameter sets the{@code Object} of the message.
     * </p>
     * @param obj a {@code Object} passed by the method that calls this method*/
    public void setObj(Object obj) {
        this.obj = obj;
    }

    /**
     * <p>
     *     Method that returns the objects of a message.
     * </p>
     * @return Object, the object of the message*/
    public Object getObj() {
        return this.obj;
    }

    /**
     * <p>
     *     Method that returns the event of a message.
     * </p>
     * @return Event, the event of the message*/
    public Event getMessageEvent() {
        return this.event;
    }

    /**
     * <p>
     *     Method that given an {@code Event} as a parameter sets it as the message event.
     * </p>
     * @param event is the event that has to be set as the message event*/
    public void setMessageEvent(Event event) {
        this.event = event;
    }
}
