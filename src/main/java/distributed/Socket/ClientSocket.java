package distributed.Socket;

import distributed.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Stream;

public class ClientSocket extends Client{

    private String address;
    private int port;

    public ClientSocket(String address, int port){
        super(address,port);
        this.port = port;
        this.address = address;
    }
    @Override
    public void startConnection() throws IOException {
      Socket socket = new Socket(address, port);
      System.out.println("Connession enstablished!");
      Scanner in = new Scanner(socket.getInputStream());
      PrintWriter out = new PrintWriter(socket.getOutputStream());
      Scanner stdin = new Scanner(System.in);

      try{
          while(true){
              String clientLine = stdin.nextLine();
              out.println(clientLine);
              out.flush();
              Object socketLine = in.nextLine();
              System.out.println(socketLine);
          }
      }catch (NoSuchElementException e){
          System.out.println("Connection closed");
      } finally{
          stdin.close();
          in.close();
          out.close();
          socket.close();
      }

    }

    /*

    @Override
    public void sendMessage(Message message) {
        //TODO implement this
    };*/

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
