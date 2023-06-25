package distributed.Socket;

import distributed.Client;
import distributed.RMI.ClientInterface;
import distributed.messages.Message;
import distributed.messages.SocketMessage;
import util.Event;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

public class ClientSocket extends Client {
    private String address;
    private int port;
    private final Socket socket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ExecutorService executorService;
    private Object serverObj;
    private int myIndex;
    private int myMatch;


    public ClientSocket(String address, int port) throws IOException {
        super(address,port);
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
    @Override
    public void startConnection() throws IOException, ClassNotFoundException {
        Thread clientThread = new Thread(()-> {
          executorService.execute(()->{
              while(!executorService.isShutdown()){
                  SocketMessage message;
                  try{
                      message= receivedMessageC();
                      System.out.println("ho ricevuto " + message.getMessageEvent());
                      if(message.getMessageEvent() == Event.SET_CLIENT_INDEX){
                          this.myIndex = message.getClientIndex();
                          this.myMatch = message.getMatchIndex();
                          System.out.println("my index " + myIndex);
                      }

                 //     sendMessageC(new SocketMessage(myIndex, myMatch, "ciao", Event.SET_UP_BOARD));//FIXME this is to implement, now sendMessageC() has Message as a parameter
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
    @Override
    public void disconnected() {

    }

    ;

    public void disconnect(){
        //TODO implement this
    };

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public int askNumOfPlayers() throws IOException {
        return 0;
    }


}
