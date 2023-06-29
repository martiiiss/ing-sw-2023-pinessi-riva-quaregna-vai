package distributed;

import controller.Controller;
import distributed.RMI.*;
import distributed.Socket.SocketServer;
import model.Game;
import util.Event;
import util.Match;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class Server extends UnicastRemoteObject implements Remote {
    private final List<Match> matchList;
    private Controller controller ;
    private Game game ;
    private SocketServer serverSocket;
    private ServerRMIInterface rmiServer;

    public Server(int portSocket, int portRMI) throws IOException {
        this.matchList= Collections.synchronizedList(new ArrayList<>());
        System.out.println("\u001B[32mServer Ready! \u001B[0m");
    }

    public ArrayList<Integer> connection (ClientInterface client) throws IOException, ClassNotFoundException {
        Match match = new Match();
        ArrayList<Integer> indexes = new ArrayList<>();
        if(matchList.isEmpty()) {
            System.out.println("No match in list, new match");
            match.addPlayer(client);
            match.setMaxSize(client.askNumOfPlayers());
            match.getGameController().updateController(match.getMaxSize(),Event.ASK_NUM_PLAYERS);
            matchList.add(match);
            indexes.add(matchList.indexOf(match));
            indexes.add((match.getListOfClients().indexOf(client)));
            System.out.println(indexes.get(0)+" "+indexes.get(1));
            System.out.println("Match selected is: "+matchList.indexOf(match));
            startClientStatusCheckTimer();
            return indexes;
        }
        else {
            for(Match matchIterator : matchList) {
                if(matchIterator.getMaxSize()>matchIterator.getListOfClients().size()) {
                    matchIterator.addPlayer(client);
                    System.err.println(matchIterator.getListOfClients().indexOf(client));
                    System.out.println("Match selected is: "+matchList.indexOf(matchIterator));
                    indexes.add(matchList.indexOf(matchIterator));
                    indexes.add((matchIterator.getListOfClients().indexOf(client)));
                    return indexes;
                }
            }
        }
        System.out.println("All Matches are full, new match!");
        match.setMaxSize(client.askNumOfPlayers());
        matchList.add(match);
        match.addPlayer(client);
        System.out.println("Match selected is: "+matchList.indexOf(match));
        indexes.add(matchList.indexOf(match));
        indexes.add((match.getListOfClients().indexOf(client)));
        match.getGameController().updateController(match.getMaxSize(),Event.ASK_NUM_PLAYERS);
        return indexes;
    }

    public Event sendServerMessage(int gameIndex, Object obj, Event event) {
        if(event==Event.ASK_NUM_PLAYERS)
            matchList.get(gameIndex).getGameController().updateController(matchList.get(gameIndex).getMaxSize(),Event.ASK_NUM_PLAYERS);
        return matchList.get(gameIndex).getGameController().updateController(obj,event);
    }

    public Object getServerModel(int gameIndex, Event event, Object clientIndex) {
        return matchList.get(gameIndex).getGameController().getControllerModel(event, clientIndex);
    }

    public boolean getDisconnections(int matchIndex) {
        return matchList.get(matchIndex).getClientDisconnected();
    }

    private void startClientStatusCheckTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkClientStatus();
            }
        }, 0, 1000);
    }

    private void checkClientStatus() {
            for (Match match : matchList) {
                if (!match.getClientDisconnected()) {
                    Iterator<ClientInterface> iterator = match.getListOfClients().iterator();
                    while (iterator.hasNext()) {
                        ClientInterface client = iterator.next();
                        try {
                            client.ping();
                        } catch (RemoteException e) {
                            match.setClientsConnected();
                        }
                    }
                }
            }
    }
    public int askPitController(int matchIndex) {
        return matchList.get(matchIndex).getGameController().getPITIndex();
    }

    public void onDisconnect(int matchIndex) {
        Match match = matchList.get(matchIndex);
        match.setClientsConnected();
    }
    public boolean askMatchDisconnection(int matchIndex) {
        return matchList.get(matchIndex).getClientDisconnected();
    }
}