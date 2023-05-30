package distributed.Socket;

import distributed.Client;
import distributed.messages.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
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


    public ClientSocket(String address, int port) throws IOException {
        super(address,port);
        this.port = port;
        this.address = address;
        this.socket = new Socket();
        this.socket.connect(new InetSocketAddress(address, port), 1000);
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }
    @Override
    public void startConnection() throws IOException, ClassNotFoundException {
      Thread clientThread = new Thread(()-> {
          executorService.execute(()->{
              while(!executorService.isShutdown()){
                  try{
                     sendMessageC(null);//FIXME this is to implement, now sendMessageC() has Message as a parameter
                      receivedMessage();
                  } catch (IOException | ClassNotFoundException e) {
                      throw new RuntimeException(e);
                  }
              }
          });
      });
      clientThread.start();
    }


    public void sendMessageC(Message mess) throws IOException {
        try{
            //view: legge da tastiera
            System.out.println("scrivi qualcosa: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
            Object message = reader.readLine();
            //invio messaggio:
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        }catch (IOException e){
            //disconnessione
            //notifyObserver con messaggio di errore
        }
    }

    public void receivedMessage() throws IOException, ClassNotFoundException {
        if((serverObj = inputStream.readObject())!=null){
            System.out.println("ogg da server " + serverObj);
        }
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




}
