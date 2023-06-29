package distributed.Socket;

import distributed.messages.Message;
import distributed.messages.SocketMessage;
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
    private final ExecutorService executor;
    private int numOfPlayers;


    public ClientController(UserView uView) {
        this.uView = uView;
        this.executor = Executors.newSingleThreadExecutor();
    }


    public void initClient(String address, int portSocket) throws IOException {
        this.clientSocket = new ClientSocket(address, portSocket);
        executor.execute(() -> {
            try {
                clientSocket.sendMessageC(new SocketMessage(clientSocket.getMyIndex(), clientSocket.getMyMatch(), numOfPlayers, Event.ASK_NUM_PLAYERS));
                SocketMessage socketMessage = clientSocket.receivedMessageC();
                if(socketMessage.getMessageEvent()==Event.ASK_NUM_PLAYERS){
                    numOfPlayers = uView.askNumOfPlayer();
                    while(numOfPlayers<2 || numOfPlayers>4){
                        System.err.println("Retry...");
                        numOfPlayers = uView.askNumOfPlayer();
                    }
                    clientSocket.sendMessageC(new SocketMessage(clientSocket.getMyIndex(), clientSocket.getMyMatch(), numOfPlayers, Event.ASK_NUM_PLAYERS));
                }else{
                    clientSocket.sendMessageC(new SocketMessage(clientSocket.getMyIndex(), clientSocket.getMyMatch(), numOfPlayers, Event.OK));
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }


    public void lobby(){
        executor.execute(() -> {
            try{
                if(uView!=null)
                    clientSocket.lobby(uView);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void update(Observable o, Message message) {

        switch (message.getMessageEvent()){
           case SET_UP_BOARD -> //show board
                    executor.execute(() -> {
                        uView.showTUIBoard((Board) message.getObj());
                    });

        }
    }

    @Override
    public void onUpdate(Message message){
    }

}
