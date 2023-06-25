package distributed.Socket;

import distributed.Client;
import distributed.RMI.ClientInterface;
import distributed.messages.Message;
import distributed.messages.SocketMessage;
import util.Event;
import view.UserView;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

public class ClientSocket {
    private String address;
    private int port;
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ExecutorService executorService;
    private Object serverObj;
    private int myIndex;
    private int myMatch;
    private UserView uView;
    private int viewChosen;
    private String nickname;


    public ClientSocket(String address, int port) throws IOException {
        this.port = port;
        this.address = address;
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), 1000);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.myIndex = -1;
        this.myMatch = -1;
    }


    public void lobby(UserView userView) throws IOException, ClassNotFoundException {
        uView = userView;
        Thread clientThread = new Thread(()-> {
          executorService.execute(()->{
              while(!executorService.isShutdown()){
                  try{
                      System.out.println("attendo messaggio...");
                      SocketMessage message = receivedMessageC();
                      System.out.println("ho ricevuto " + message.getMessageEvent());
                      update(message);

                  } catch (IOException | ClassNotFoundException e) {
                      throw new RuntimeException(e);
                  }
                  //notifyObservers(message);
              }
              System.out.println("esco");
          });
      });

      clientThread.start();
    }

    public void update(SocketMessage message) throws IOException {
        switch (message.getMessageEvent()){
            case SET_CLIENT_INDEX ->{
                this.myIndex = message.getClientIndex();
                this.myMatch = message.getMatchIndex();
                update(new SocketMessage(myIndex, myMatch, null, Event.CHOOSE_VIEW));
            }
            case CHOOSE_VIEW -> {
                if(message.getObj()!=null){
                    System.out.println(((Event)message.getObj()).getMsg());
                }
                this.viewChosen = uView.userInterface();
                sendMessageC(new SocketMessage(myIndex, myMatch, this.viewChosen, Event.CHOOSE_VIEW));
            }
            case SET_NICKNAME -> {
                if(message.getObj()!=null){
                    System.out.println(((Event)message.getObj()).getMsg());
                }
                this.nickname = uView.askPlayerNickname();
                sendMessageC(new SocketMessage(myIndex, myMatch, this.nickname, Event.SET_NICKNAME));
            }

        }
    }
    public void sendMessageC(SocketMessage mess) throws IOException {
        try{
            //invio messaggio:
            outputStream.writeObject(mess);
            outputStream.flush();
            outputStream.reset();
        }catch (IOException e){
            //TODO disconnessione
            //notifyObserver con messaggio di errore
        }
    }

    public SocketMessage receivedMessageC() throws IOException, ClassNotFoundException {
        SocketMessage message = (SocketMessage) inputStream.readObject();
        return message;
    }

    public int getMyIndex(){
        return myIndex;
    }

    public int getMyMatch(){
        return myMatch;
    }


    public void closeConnection(){
        //TODO implement this
    }

    /**
     *
     */
    public void disconnected() {

    }

    ;

    public void disconnect(){
        //TODO implement this
    };


    public void setMatchIndex(int i){
        this.myMatch = i;
    }

    public void setMyIndex(int i){
        this.myIndex = i;
    }
}
