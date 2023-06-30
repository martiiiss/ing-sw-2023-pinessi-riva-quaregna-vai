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

/**Class that represents a generic Server*/
public class Server extends UnicastRemoteObject implements Remote {
    private final List<Match> matchList;

    /**
     * Constructor of the Class. This creates the list of players for a match.
     * @throws IOException if an error occurs*/
    public Server() throws IOException {
        this.matchList= Collections.synchronizedList(new ArrayList<>());
        System.out.println("\u001B[32mServer Ready! \u001B[0m");
    }

    /**
     * Method used to create a connection.
     * @param client is a {@link ClientInterface}
     * @return an {@code ArrayList} of {@code Integer}
     * @throws IOException if an error occurs
     * @throws ClassNotFoundException if a class cannot be found*/
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

    /**
     * Method used to send a message from the Server.
     * @param gameIndex is an int that represents the index of a match
     * @param obj is an {@code Object} sent from the server
     * @param event is an {@code Event} used to identify the action to perform
     * @return an {@code Event}*/
    public Event sendServerMessage(int gameIndex, Object obj, Event event) {
        if(event==Event.ASK_NUM_PLAYERS)
            matchList.get(gameIndex).getGameController().updateController(matchList.get(gameIndex).getMaxSize(),Event.ASK_NUM_PLAYERS);
        return matchList.get(gameIndex).getGameController().updateController(obj,event);
    }

    /**
     * Method used to get objects from the model.
     * @param gameIndex is an int that represents the index of a match
     * @param event is an {@code Event} used to identify the action to perform
     * @param clientIndex is an {@code Object} used to identify a player
     * @return an {@code Object}*/
    public Object getServerModel(int gameIndex, Event event, Object clientIndex) {
        return matchList.get(gameIndex).getGameController().getControllerModel(event, clientIndex);
    }

    /**
     * Method used to get the disconnections.
     * @param matchIndex an int that identifies a match
     * @return a boolean, <b>true</b> id someone disconnected, <b>false</b> otherwise*/
    public boolean getDisconnections(int matchIndex) {
        return matchList.get(matchIndex).getClientDisconnected();
    }

    /**
     * Method used to check a client status.
     * */
    private void startClientStatusCheckTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkClientStatus();
            }
        }, 0, 1000);
    }

    /**
     * Method used to control if a client disconnects.*/
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

    /**
     * Method used to get the index of the player in turn.
     * @param matchIndex is an int that represents the index of a match
     * @return an int that represents the index of the player in turn */
    public int askPitController(int matchIndex) {
        return matchList.get(matchIndex).getGameController().getPITIndex();
    }

    /**
     * Method used to handle disconnections.
     * @param matchIndex is an int that represents the index of a match*/
    public void onDisconnect(int matchIndex) {
        Match match = matchList.get(matchIndex);
        match.setClientsConnected();
    }

    /**
     * Method used to handle the disconnections.
     * @param matchIndex is an int that represents the index of a match
     * @return a boolean, <b>true</b> if a client disconnected, <b>false</b> otherwise*/
    public boolean askMatchDisconnection(int matchIndex) {
        return matchList.get(matchIndex).getClientDisconnected();
    }
}