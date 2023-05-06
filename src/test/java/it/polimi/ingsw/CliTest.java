package it.polimi.ingsw;

import it.polimi.ingsw.client.view.BoardView;
import it.polimi.ingsw.client.view.BookshelfView;
import it.polimi.ingsw.client.view.CommonGoalView;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.SettingLoader;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliTest {

    @BeforeEach
    void setUp() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    void PersonalGoal() throws IOException, ParseException {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player1", 20, true, true, false));
        players.add(new Player("Player2", 20, false, false, false));
        players.add(new Player("Player3", 20, false, false, false));
        GameModel game = new GameModel(players);
        game.start();
        PersonalGoal personalGoal = game.getCurrentPlayer().getPersonalGoal();
        //        PersonalGoalView personalGoalview = new PersonalGoalView(personalGoal.getPersonalGoalCard());
        //        personalGoalview.printLayout();
    }

    @Test
    void CommonGoalAll() {
        CommonGoalView.itemsPerColorPrintLayout();
        System.out.println();
        CommonGoalView.squarePrintLayout();
        System.out.println();
        CommonGoalView.cornersPrintLayout();
        System.out.println();
        CommonGoalView.diagonalPrintLayout();
        System.out.println();
        CommonGoalView.fullLinePrintLayout(2, true);
        System.out.println();
        CommonGoalView.fullLinePrintLayout(4, true);
        System.out.println();
        CommonGoalView.fullLinePrintLayout(2, false);
        System.out.println();
        CommonGoalView.fullLinePrintLayout(3, false);
        System.out.println();
        CommonGoalView.groupPrintLayout(6, 2);
        System.out.println();
        CommonGoalView.groupPrintLayout(4, 4);
        System.out.println();
        CommonGoalView.stairPrintLayout();
        System.out.println();
        CommonGoalView.xShapePrintLayout();
    }

    @Test
    void CommonGoalSelected() {
        CommonGoalView.print("Stair", 0, 0, false);
        CommonGoalView.print("Corners", 0, 0, false);
        CommonGoalView.print("Diagonal", 0, 0, false);
        CommonGoalView.print("FullLine", 2, 0, true);
        CommonGoalView.print("Group", 6, 2, false);
        CommonGoalView.print("XShape", 0, 0, false);
        CommonGoalView.print("ItemsPerColor", 0, 0, false);
        CommonGoalView.print("Square", 0, 0, false);
        CommonGoalView.print("FullLine", 4, 0, true);
        CommonGoalView.print("FullLine", 2, 0, false);
        CommonGoalView.print("FullLine", 3, 0, false);
    }

    @Test
    void bookshelfsTest() {
        Bookshelf b1 = new Bookshelf();
        Bookshelf b2 = new Bookshelf();
        Bookshelf b3 = new Bookshelf();
        Bookshelf b4 = new Bookshelf();

        List<Item> items = new ArrayList<>();

        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        b1.insert(0, items);
        items.clear();

        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.GREEN, 1));
        b1.insert(1, items);
        items.clear();

        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        b2.insert(2, items);
        BookshelfView bookshelfView1 = new BookshelfView(b1);
        BookshelfView bookshelfView2 = new BookshelfView(b2);
        System.out.println("Your bookshelf:");
        bookshelfView1.printBookshelf();
        System.out.println("Opponent's bookshelf:");
        bookshelfView2.printOtherBookshelf();
    }

    @Test
    void boardTest() throws IOException, ParseException, IllegalAccessException {
        Board board = new Board(3);
        board.fill();
        BoardView boardView = new BoardView(board);
        boardView.printBoard();
    }
}
