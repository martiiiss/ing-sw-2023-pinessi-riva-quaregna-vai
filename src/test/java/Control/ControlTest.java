package Control;

import controller.Controller;
import jdk.jshell.SourceCodeAnalysis;
import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;
import util.Event;
import view.UserView;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
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
    @Test
    void gameFlow() {
        System.out.println("╔═══════════════════╗");
        System.out.println("║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║");
        System.out.println("╠═══╬═══╬═══╬═══╬═══╣");
        System.out.println("║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║");
        System.out.println("╠═══╬═══╬═══╬═══╬═══╣");
        System.out.println("║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║");
        System.out.println("╠═══╬═══╬═══╬═══╬═══╣");
        System.out.println("║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║");
        System.out.println("╠═══╬═══╬═══╬═══╬═══╣");
        System.out.println("║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║\033[37;46;1m   \u001B[0m║");
        System.out.println("╠═══╩═══╩═══╩═══╩═══╣");
        System.out.println("║ 0   1   2   3   4 ║");
        System.out.println("╚═══════════════════╝");

        System.out.print("\033[37;100;1m   \u001B[0m");
        System.out.print("\033[37;42;1m   \u001B[0m");
        System.out.print("\033[37;107;1m   \u001B[0m");
        System.out.print("\033[37;44;1m   \u001B[0m");
        System.out.print("\033[37;43;1m   \u001B[0m");
        System.out.print("\033[37;45;1m   \u001B[0m");
        System.out.print("\033[37;46;1m   \u001B[0m");
        System.out.println();
        System.out.print("\u001B[90m □ \u001B[0m");
        System.out.print("\u001B[32m □ \u001B[0m");
        System.out.print("\u001B[97m □ \u001B[0m");
        System.out.print("\u001B[34m □ \u001B[0m");
        System.out.print("\u001B[33m □ \u001B[0m");
        System.out.print("\u001B[35m □ \u001B[0m");
        System.out.print("\u001B[36m □ \u001B[0m");

    }



    Tile[][] bookshelf2 = {
            {frame, nothing, frame, frame, frame},
            {frame, frame, frame, frame, frame},
            {frame, frame, frame, frame, frame},
            {frame, frame, frame, frame, frame},
            {frame, frame, frame, frame, frame},
            {frame, frame, frame, frame, frame}};
    Tile[][] bookshelf3 = {
            {frame, nothing, frame, frame, frame},
            {frame, nothing, frame, frame, frame},
            {frame, frame, frame, frame, frame},
            {frame, frame, frame, frame, frame},
            {frame, frame, frame, frame, frame},
            {frame, frame, frame, frame, frame}};
    @Test
    void checkIfGameEnd() throws IOException {
        controller.getInstanceOfGame().setNumOfPlayers(3);
        bookshelf.setBookshelf(bookshelf2);
        player.setMyBookshelf(bookshelf);
        player.setNickname("Numero 0");
        controller.getInstanceOfGame().addPlayer(player);
        controller.getInstanceOfGame().setPlayerInTurn(player);

        Player player1 = new Player();
        player1.setNickname("Centro");
        Bookshelf bookshelf4 = new Bookshelf();
        bookshelf4.setBookshelf(bookshelf3);
        player1.setMyBookshelf(bookshelf4);
        controller.getInstanceOfGame().addPlayer(player1);

        Player player2 = new Player();
        player2.setNickname("Coda");
        Bookshelf bookshelf5 = new Bookshelf();
        bookshelf5.setBookshelf(bookshelf1);
        player2.setMyBookshelf(bookshelf5);
        controller.getInstanceOfGame().addPlayer(player2);

        System.out.println("LIST OF THINGS: ");

        System.out.println("# Players: "+controller.getInstanceOfGame().getPlayers().size());

        System.out.println("Player 1: "+controller.getInstanceOfGame().getPlayers().get(0).getNickname());
        System.out.println("Bookshelf P1: ");
        userView.showTUIBookshelf(player.getMyBookshelf());

        System.out.println("Player 2: "+ controller.getInstanceOfGame().getPlayers().get(1).getNickname());
        System.out.println("Bookshelf P2: ");
        userView.showTUIBookshelf(player1.getMyBookshelf());
        controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().placeTile(1,frame);
        userView.showTUIBookshelf(controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf());
       /* Event event = controller.checkIfGameEnd();
        System.out.println(event);
        System.out.println("NEXT CALL..");
        System.out.println("Adding 2 tiles to second player...");
        controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().placeTile(1, frame);
        controller.getInstanceOfGame().getPlayerInTurn().getMyBookshelf().placeTile(1, frame);
        userView.showTUIBookshelf(player1.getMyBookshelf());
        */
        Event event;
        do {
            event = controller.checkIfGameEnd();
            System.out.println("Event rec: "+event);
        } while (event!=Event.GAME_OVER);


    }

}
