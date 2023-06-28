package distributed.messages;

import util.Event;
import java.io.Serial;
import java.io.Serializable;

/**Class that represents a specific message for Socket protocol of communication*/
public class SocketMessage extends Message implements Serializable {
    @Serial
    private static final long serialVersionUID = -877833848389371489L;
    private final int clientIndex;
    private final int matchIndex;

    /**
     * Constructor of the Class. This initializes the {@code clientIndex} and the {@code matchIndex}.
     * @param clientIndex an int that represents the index of a player
     * @param matchIndex an int that represents the index of a match
     * @param obj an object that has to be passed through a message
     * @param event an {@code Event} that defines the type of message*/
    public SocketMessage(int clientIndex, int matchIndex, Object obj, Event event){
        super(obj, event);
        this.clientIndex = clientIndex;
        this.matchIndex = matchIndex;
    }

    /**
     * Method used to get the index of a client.
     * @return an int that represents the index of a player*/
    public int getClientIndex(){
        return this.clientIndex;
    }

    /**
     * Method used to get the index of a match.
     * @return an int that represents the index of a match*/
    public int getMatchIndex(){
        return this.matchIndex;
    }
}
