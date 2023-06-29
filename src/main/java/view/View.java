package view;

import distributed.Socket.ClientHandlerSocket;
import distributed.messages.Message;
import util.Observable;
import util.Observer;

import java.io.IOException;
//TODO: STA CLASSE SERVER ANCORA?????
public class View implements Observer {

    private final ClientHandlerSocket clientHandler;
    private UserView uView;

    public View (ClientHandlerSocket cl){
        this.uView = new UserView();
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


    public void ask(Message message) throws IOException {
        System.out.println("ricevo in ask in view " + this.clientHandler);
        //this.clientHandler.sendMessage("ciao");
        switch (message.getMessageEvent()){
            case SET_NICKNAME -> {
               // uView.askPlayerNickname();
            }

        }
    }
}
