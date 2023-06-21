package view;

import distributed.Socket.ClientHandlerSocket;
import distributed.messages.Message;
import util.Event;
import util.Observable;
import util.Observer;

import java.io.IOException;

public class View implements Observer, ViewInterface {

    private final ClientHandlerSocket clientHandler;


    public View (ClientHandlerSocket cl){
        this.clientHandler = cl;
    }
    @Override
    public void update(Observable observable, Message message) {

    }

    /**
     * @param message
     * @throws IOException
     */
    @Override
    public void onUpdate(Message message) throws IOException {

    }


    public void ask(){
        System.out.println("ricevo in ask in view " + this.clientHandler);
        this.clientHandler.sendMessage("ciao");
    }
}
