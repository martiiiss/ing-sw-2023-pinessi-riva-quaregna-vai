package distributed.messages;

import distributed.Socket.SocketServer;
import util.Event;

public class SocketMessage extends Message{
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
