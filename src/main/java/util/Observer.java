package util;

import distributed.messages.Message;
import java.io.IOException;

/**Interface that represents the Observer*/
public interface Observer {

    /**
     * <p>
     *     Method that gets override in the implementations.<br>
     *     Given two parameters an {@code Observable} and a {@link Message} updates the class on which this is called.
     * </p>
     * @param observable is a {@code Observable} CLass, the one that has to be updated
     * @param message is a {@code Message}, that is used to identify a specific event*/
    void update(Observable observable, Message message);

    /**
     * <p>
     *      Method that gets override in the implementations.<br>
     *      Given a {@link Message} as a parameter this decides to do a specific action.
     * </p>
     * @param message is a {@code Message} that is needed to choose a specific action tha has to be done
     */
    void onUpdate(Message message) throws IOException;
}
