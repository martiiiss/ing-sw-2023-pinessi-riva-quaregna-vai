package distributed.messages;

import distributed.Socket.SocketServer;
import util.Event;

import java.io.Serial;
import java.io.Serializable;

public class SocketMessage extends Message implements Serializable {
    @Serial
    private static final long serialVersionUID = -877833848389371489L;
    private int clientIndex;
    private int matchIndex;

    public SocketMessage(int clientIndex, int matchIndex, Object obj, Event event){
        super(obj, event);
        this.clientIndex = clientIndex;
        this.matchIndex = matchIndex;
    }

    public int getClientIndex(){
        return this.clientIndex;
    }

    public int getMatchIndex(){
        return this.matchIndex;
    }
}
