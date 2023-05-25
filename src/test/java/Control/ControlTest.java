package Control;

import controller.Controller;
import model.*;
import org.junit.jupiter.api.Test;
import view.UserView;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class ControlTest {

    Player player = new Player();

    Controller controller = new Controller(); //
    Bookshelf bookshelf = new Bookshelf();
    Tile cat = new Tile(Type.CAT,1);
    Tile book = new Tile(Type.BOOK,2);
    Tile game = new Tile(Type.GAME,3);
    Tile frame = new Tile(Type.FRAME,2);
    Tile trophy = new Tile(Type.TROPHY,1);
    Tile plant = new Tile(Type.PLANT,3);
    Tile nothing = new Tile(Type.NOTHING,0);

    CommonGoalCard commonGoalCard1= new CommonGoalCard(1,2,1);
    CommonGoalCard commonGoalCard2 = new CommonGoalCard(2,2,2);
    PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);
    Tile[][] bookshelf1 = {
            {nothing, nothing, nothing, nothing, nothing},
            {nothing, nothing, nothing, nothing, nothing},
            {nothing, nothing, nothing, nothing, nothing},
            {frame, nothing, nothing, nothing, nothing},
            {frame, nothing, nothing, nothing, nothing},
            {frame, nothing, nothing, nothing, nothing}};
    UserView userView = new UserView();
    Board board = new Board(2);

    public ControlTest() throws IOException {
    }

    @Test
    void calculateScore() {
        controller.getInstanceOfGame().setNumOfPlayers(2);
        player.setMyBookshelf(bookshelf);
        player.setAsFirstPlayer();
        player.setNickname("Foo");
        controller.getInstanceOfGame().setCommonGoalCards();
        controller.getInstanceOfGame().addPlayer(player);
        controller.getInstanceOfGame().setPlayerInTurn(controller.getInstanceOfGame().getPlayers().get(0));
        System.out.println("Size: "+controller.getInstanceOfGame().getPlayers().size());
        player.setPersonalGoalCard(personalGoalCard);
        bookshelf.setBookshelf(bookshelf1);
        userView.showTUIBookshelf(player.getMyBookshelf());

        System.out.println(controller.getInstanceOfGame().getCommonGoalCard().get(0).getIdCGC()+" "+controller.getInstanceOfGame().getCommonGoalCard().get(1).getIdCGC());

        controller.calculateScore();
        assertEquals(controller.getInstanceOfGame().getPlayerInTurn().getScore(),2);
        controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().placeTile(0,frame);
        controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().placeTile(0,frame);
        controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().placeTile(0,frame);
        controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().placeTile(0,frame);
        controller.calculateScore();
        assertEquals(controller.getInstanceOfGame().getPlayerInTurn().getScore(),3);

        userView.showTUIBookshelf(controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf());

    }
}
