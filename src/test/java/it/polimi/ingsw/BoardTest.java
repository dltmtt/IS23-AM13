package it.polimi.ingsw;

import it.polimi.ingsw.client.view.BoardView;
import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    // Tests if Cell 00 is null with 3 players
    @Test
    void filWith3playersCell00() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(0, 0));
    }

    // Tests if the expected cells of row 0 are null, with 3 players
    @Test
    void fillWith3playersAllCells0x() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(0, 0));
        assertNull(b.getItem(0, 1));
        assertNull(b.getItem(0, 2));
        assertNull(b.getItem(0, 3));
        assertNull(b.getItem(0, 4));
        assertNull(b.getItem(0, 6));
        assertNull(b.getItem(0, 7));
        assertNull(b.getItem(0, 8));
    }

    // Tests if the expected cells of row 1 are null, with 3 players
    @Test
    void fillWith3playersAllCells1x() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(1, 0));
        assertNull(b.getItem(1, 1));
        assertNull(b.getItem(1, 2));
        assertNull(b.getItem(1, 3));
        assertNull(b.getItem(1, 6));
        assertNull(b.getItem(1, 7));
        assertNull(b.getItem(1, 8));
    }

    // Tests if the expected cells of row 2 are null, with 3 players
    @Test
    void fillWith3playersAllCells2x() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(2, 0));
        assertNull(b.getItem(2, 1));
        assertNull(b.getItem(2, 7));
        assertNull(b.getItem(2, 8));
    }

    // Tests if the expected cells of row 3 are null, with 3 players
    @Test
    void fillWith3playersAllCells3x() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(3, 7));
        assertNull(b.getItem(3, 8));
    }

    // Tests if the expected cells of row 4 are null, with 3 players
    @Test
    void fillWith3playersAllCells4x() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(4, 0));
        assertNull(b.getItem(4, 8));
    }

    // Tests if the expected cells of row 5 are null, with 3 players
    @Test
    void fillWith3playersAllCells5x() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(5, 0));
        assertNull(b.getItem(5, 1));
    }

    // Tests if the expected cells of row 6 are null, with 3 players
    @Test
    void fillWith3playersAllCells6x() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(6, 0));
        assertNull(b.getItem(6, 1));
        assertNull(b.getItem(6, 7));
        assertNull(b.getItem(6, 8));
    }

    // Tests if the expected cells of row 7 are null, with 3 players
    @Test
    void fillWith3playersAllCells7x() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(7, 0));
        assertNull(b.getItem(7, 1));
        assertNull(b.getItem(7, 2));
        assertNull(b.getItem(7, 5));
        assertNull(b.getItem(7, 6));
        assertNull(b.getItem(7, 7));
        assertNull(b.getItem(7, 8));
    }

    // Tests if the expected cells of row 8 are null, with 3 players
    @Test
    void fillWith3playersAllCells8x() {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(8, 0));
        assertNull(b.getItem(8, 1));
        assertNull(b.getItem(8, 2));
        assertNull(b.getItem(8, 4));
        assertNull(b.getItem(8, 5));
        assertNull(b.getItem(8, 6));
        assertNull(b.getItem(8, 7));
        assertNull(b.getItem(8, 8));
    }

    // Tests if the expected cells of row 0 are NOT null, with 3 players
    @Test
    void fillWith3playersAllCells0xNotNull() {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(0, 5));
    }

    // Tests if the expected cells of row 1 are NOT null, with 3 players
    @Test
    void fillWith3playersAllCells1xNotNull() {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(1, 4));
        assertNotNull(b.getItem(1, 5));
    }

    // Tests if the expected cells of row 2 are NOT null, with 3 players
    @Test
    void fillWith3playersAllCells2xNotNull() {
        Board b = new Board(3);
        b.fill();

        assertNotNull(b.getItem(2, 2));
        assertNotNull(b.getItem(2, 3));
        assertNotNull(b.getItem(2, 4));
        assertNotNull(b.getItem(2, 5));
        assertNotNull(b.getItem(2, 6));
    }

    // Tests if the expected cells of row 3 are NOT null, with 3 players
    @Test
    void fillWith3playersAllCells3xNotNull() {
        Board b = new Board(3);
        b.fill();

        assertNotNull(b.getItem(3, 0));
        assertNotNull(b.getItem(3, 1));
        assertNotNull(b.getItem(3, 2));
        assertNotNull(b.getItem(3, 3));
        assertNotNull(b.getItem(3, 4));
        assertNotNull(b.getItem(3, 5));
        assertNotNull(b.getItem(3, 6));
    }

    // Tests if the expected cells of row 4 are NOT null, with 3 players
    @Test
    void fillWith3playersAllCells4xNotNull() {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(4, 1));
        assertNotNull(b.getItem(4, 2));
        assertNotNull(b.getItem(4, 3));
        assertNotNull(b.getItem(4, 4));
        assertNotNull(b.getItem(4, 5));
        assertNotNull(b.getItem(4, 6));
        assertNotNull(b.getItem(4, 7));
    }

    // Tests if the expected cells of row 5 are NOT null, with 3 players
    @Test
    void fillWith3playersAllCells5xNotNull() {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(5, 2));
        assertNotNull(b.getItem(5, 3));
        assertNotNull(b.getItem(5, 4));
        assertNotNull(b.getItem(5, 5));
        assertNotNull(b.getItem(5, 6));
        assertNotNull(b.getItem(5, 7));
        assertNotNull(b.getItem(5, 8));
    }

    // Tests if the expected cells of row 6 are NOT null, with 3 players
    @Test
    void fillWith3playersAllCells6xNotNull() {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(6, 2));
        assertNotNull(b.getItem(6, 3));
        assertNotNull(b.getItem(6, 4));
        assertNotNull(b.getItem(6, 5));
        assertNotNull(b.getItem(6, 6));
    }

    // Tests if the expected cells of row 7 are NOT null, with 3 players
    @Test
    void fillWith3playersAllCells7xNotNull() {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(7, 3));
        assertNotNull(b.getItem(7, 4));
    }

    // Tests if the expected cells of row 8 are NOT null, with 3 players
    @Test
    void fillWith3playersAllCells8xNotNull() {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(8, 3));
    }

    // This test controls the right cells filled with 4 players
    @Test
    void fillWith4players() {
        Board b = new Board(4);
        b.fill();
        assertNotNull(b.getItem(0, 4));
        assertNotNull(b.getItem(1, 3));
        assertNotNull(b.getItem(3, 7));
        assertNotNull(b.getItem(4, 0));
        assertNotNull(b.getItem(4, 8));
        assertNotNull(b.getItem(5, 1));
        assertNotNull(b.getItem(7, 5));
        assertNotNull(b.getItem(8, 4));
    }

    // This test controls that the item of bag is filled with 132 item with all possible number of players
    @Test
    void itemBagInitialized() {
        Board b2 = new Board(2);
        Board b3 = new Board(3);
        Board b4 = new Board(4);
        assertEquals(132, b2.getItemBag().size());
        assertEquals(132, b3.getItemBag().size());
        assertEquals(132, b4.getItemBag().size());
    }

    // Test pickFormBoard.
    @Test
    void pickFormTest() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertEquals(b.getItemBag().size(), 95);
        List<Coordinates> itemPicked = new ArrayList<>();
        itemPicked.add(new Coordinates(5, 7));
        itemPicked.add(new Coordinates(5, 8));
        List<Item> items = b.pickFromBoard(itemPicked);
        assertNull(b.getItem(5, 7));
        assertNull(b.getItem(5, 8));
    }

    @Test
    void pickedItemTest() {
        Board b = new Board(3);
        b.fill();
        List<Coordinates> cellCoordinates = new ArrayList<>();
        cellCoordinates.add(new Coordinates(3, 0));
        cellCoordinates.add(new Coordinates(3, 6));
        BoardView boardView = new BoardView(b);
        boardView.printBoard();
        System.out.println("\n");
        List<Item> items = b.pickFromBoard(cellCoordinates);
        boardView.updateBoard(b);
        boardView.printBoard();
    }

    @Test
    void fileNameTest(){
        Board b= new Board();
        Item i = new Item(Color.YELLOW, 0);
        b.setItem(0, 0, i);
        assertEquals(i, b.getItem(0, 0));
        assertEquals("y0.png", b.getItemFileName(0,0));
        assertEquals(1, b.numLeft());
        assertTrue(b.isAlone(0,0));
        b.setItem(2, 2, i);
        assertTrue(b.allIsolated());
        List<Coordinates> coord = new ArrayList<>();
        coord.add(new Coordinates(4,4));
        coord.add(new Coordinates(4,5));
        assertFalse(b.startEndNotNull(coord));
    }

    @Test
    void freeSideItems(){
        Item[][] items = new Item[Board.boardSize][Board.boardSize];
        items[0][0] = new Item(Color.YELLOW, 0);
        items[0][1] = new Item(Color.YELLOW, 1);
        items[0][2] = new Item(Color.YELLOW, 2);
        List<Item> itemBag= new ArrayList<>();
        itemBag.add(new Item(Color.WHITE, 3));

        Board b=new Board(items, itemBag, 2);
        List<Coordinates> coord = new ArrayList<>();
        coord.add(new Coordinates(0,0));
        coord.add(new Coordinates(0,2));
        assertTrue(b.checkBorder(new Coordinates(0,0)));
        assertTrue(b.AtLeastOneFree(coord));
        assertTrue(b.OrderAndMaxOf3(coord));
    }


}
