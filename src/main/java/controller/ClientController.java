package controller;

import distributed.Socket.ClientSocket;
import distributed.messages.Message;
import model.Board;
import util.Event;
import util.Observable;
import util.Observer;
import view.UserView;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientController implements Observer {
    private final UserView uView;
    private ClientSocket clientSocket;
    private ExecutorService executor;


    public ClientController(UserView uView) {
        this.uView = uView;
        this.executor = Executors.newSingleThreadExecutor();
    }

    //devo inizializzare il clientSocket


    public void initClient(String address, int portSocket) throws IOException {
        this.clientSocket = new ClientSocket(address, portSocket);
        try{
            clientSocket.startConnection();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    //in realtà questo metodo è update di observer e observable
    public void receivedMessage(){
        executor.execute(() -> {
            try {
                clientSocket.sendMessageC(new Message(uView.askNumOfPlayer(), Event.ASK_NUM_PLAYERS));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void update(Observable o, Message message) {
        //switch case per gestire il flusso di gioco con chiamate a view
        switch (message.getMessageEvent()){
            case SET_UP_BOARD -> //show board
                    executor.execute(() -> {
                        uView.showTUIBoard((Board) message.getObj());
                    });

        }

    }
}
