package view.GUI;

import distributed.messages.Message;
import model.Board;
import model.Bookshelf;
import model.Game;
import model.Player;
import util.Observable;
import util.Observer;
import util.TileForMessages;

import java.util.Objects;

public class GraphicUserInterface implements Observer {
    private GUIView guiView;

    private int playerNumber; //position of the player in the array players

    private String nickname;
    public GraphicUserInterface(String nickname) {
        guiView = new GUIView();
        this.nickname = nickname;
    }

    @Override
    public void update(Observable observable, Message message) {
        switch (message.getMessageEvent()) {
            case REMOVE_TILE_BOARD -> guiView.getBoardView().pickTile(((TileForMessages)message.getObj()).getRow(),((TileForMessages)message.getObj()).getColumn());
            case UPDATE_BOOKSHELF -> guiView.getBookshelfView().insertTile(((TileForMessages)message.getObj()).getColumn(), ((TileForMessages)message.getObj()).getTile() ); //modify it, will be only in case I'm visualizing another player's bookshelf
            //case SET_SCORING_TOKEN_1 -> guiView.changeSC(message.getObj()controller.getInstanceOfGame().getCommonGoalCard().get(0).getTokenStack().get((this.controller.getInstanceOfGame().getNumOfPlayers())-1).getValue(), 1); // these two will add the scoringToken I took to my hand
            //case SET_SCORING_TOKEN_2 -> guiView.changeSC(this.controller.getInstanceOfGame().getCommonGoalCard().get(0).getTokenStack().get((this.controller.getInstanceOfGame().getNumOfPlayers())-1).getValue(), 2);
            case SET_UP_BOARD -> guiView.getBoardView().updateBoard(((Board)message.getObj()));
            case SET_PGC -> guiView.getPGC().setDisplayedImage(((Game)message.getObj()).getPlayers().get(playerNumber).getPersonalGoalCard().getNumber());
            case SET_COMMONGC -> {
                guiView.getCGC(0).setCGCView(((Game) message.getObj()).getCommonGoalCard().get(0).getIdCGC());
                guiView.getCGC(1).setCGCView(((Game) message.getObj()).getCommonGoalCard().get(1).getIdCGC());
                guiView.getScv(0).setDisplayedImage(((Game) message.getObj()).getCommonGoalCard().get(1).getTokenStack().get(((Game) message.getObj()).getNumOfPlayers() -1).getValue());
                guiView.getScv(1).setDisplayedImage(((Game) message.getObj()).getCommonGoalCard().get(1).getTokenStack().get(((Game) message.getObj()).getNumOfPlayers() -1).getValue());
            }
            case SET_PLAYER_IN_TURN, SET_FIRST_PLAYER -> {
                if(((Game)message.getObj()).getPlayers().get(playerNumber).getIsFirstPlayer()){
                    guiView.setIsMyTurn(true);
                }
            }
            case SET_SCORING_TOKEN_1 -> {
                if(Objects.equals(((Player) message.getObj()).getNickname(), nickname))
                    guiView.getHandView().setSC(guiView.getScv(0).getValueDisplayed(), 0);
            }
            case SET_SCORING_TOKEN_2 -> {
                if(Objects.equals(((Player) message.getObj()).getNickname(), nickname))
                    guiView.getHandView().setSC(guiView.getScv(1).getValueDisplayed(), 0);
            }


        }
    }
}
