package distributed;

import java.util.Collections;
import java.util.Map;

public class Server implements Runnable {
    private int socketPort;
    private int RMIPort;
    private Map<String, Connection> clients; //username - connection
    private Object clientsLock;

    public Server(Server server, int port){
        //TODO
    }
    private void startServers(){
        //TODO fa partire server socket e rmi
    }

    public void login(String username, Connection connection){
        //TODO registra un client (utente) in partita
    }

    private void reconnectionOfPlayer(String username, Connection connection){
        //TODO riconnette un giocatore (funzionalià aggiuntive)
    }

    private void firstConnection (String username, Connection connection){
        //TODO
    }

    private void readyToStart(){
        //TODO: raggiunto il numero di giocatori necessario la partita può iniziare
    }

    public void playerDisconnected(Connection connection){
        //TODO: avvisa utenti che un giocatore si è disconnesso
    }

    //TODO: metodi sendMessage e getMessage

    @Override
    public void run() {

    }
}
