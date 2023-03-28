package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardInsertPickTest {
    @Test
    /**
     * This tests if Cell 00 is null with 3 players.
     *
     * @throws IllegalAccessException
     */
    void filWith3playersCell00() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(0, 0));
    }

    /**
     * This tests if the expected cells of row 0 are null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells0x() throws IllegalAccessException {
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

    /**
     * This tests if the expected cells of row 1 are null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells1x() throws IllegalAccessException {
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

    /**
     * This tests if the expected cells of row 2 are null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells2x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(2, 0));
        assertNull(b.getItem(2, 1));
        assertNull(b.getItem(2, 7));
        assertNull(b.getItem(2, 8));

    }

    /**
     * This tests if the expected cells of row 3 are null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells3x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(3, 7));
        assertNull(b.getItem(3, 8));
    }

    /**
     * This tests if the expected cells of row 4 are null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells4x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(4, 0));
        assertNull(b.getItem(4, 8));
    }

    /**
     * This tests if the expected cells of row 5 are null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells5x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(5, 0));
        assertNull(b.getItem(5, 1));
    }

    /**
     * This tests if the expected cells of row 6 are null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells6x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(6, 0));
        assertNull(b.getItem(6, 1));
        assertNull(b.getItem(6, 7));
        assertNull(b.getItem(6, 8));
    }

    /**
     * This tests if the expected cells of row 7 are null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells7x() throws IllegalAccessException {
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

    /**
     * This tests if the expected cells of row 8 are null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells8x() throws IllegalAccessException {
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

    /**
     * This tests if the expected cells of row 0 are NOT null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells0xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(0, 5));
    }

    /**
     * This tests if the expected cells of row 1 are NOT null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells1xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(1, 4));
        assertNotNull(b.getItem(1, 5));
    }

    /**
     * This tests if the expected cells of row 2 are NOT null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells2xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();

        assertNotNull(b.getItem(2, 2));
        assertNotNull(b.getItem(2, 3));
        assertNotNull(b.getItem(2, 4));
        assertNotNull(b.getItem(2, 5));
        assertNotNull(b.getItem(2, 6));
    }

    /**
     * This tests if the expected cells of row 3 are NOT null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells3xNotNull() throws IllegalAccessException {
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

    /**
     * This tests if the expected cells of row 4 are NOT null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells4xNotNull() throws IllegalAccessException {
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

    /**
     * This tests if the expected cells of row 5 are NOT null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells5xNotNull() throws IllegalAccessException {
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

    /**
     * This tests if the expected cells of row 6 are NOT null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells6xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(6, 2));
        assertNotNull(b.getItem(6, 3));
        assertNotNull(b.getItem(6, 4));
        assertNotNull(b.getItem(6, 5));
        assertNotNull(b.getItem(6, 6));
    }

    /**
     * This tests if the expected cells of row 7 are NOT null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells7xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(7, 3));
        assertNotNull(b.getItem(7, 4));
    }

    /**
     * This tests if the expected cells of row 8 are NOT null, with 3 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3playersAllCells8xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(8, 3));
    }

    /**
     * This test controls the right cells filled with 4 players.
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith4players() throws IllegalAccessException {
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

    /**
     * This test controls that the item of bag is filled with 132 item with all possible number of players.
     */
    @Test
    void itemBagInitialized() {
        Board b2 = new Board(2);
        Board b3 = new Board(3);
        Board b4 = new Board(4);
        assertEquals(b2.getItemBag().size(), 132);
        assertEquals(b3.getItemBag().size(), 132);
        assertEquals(b4.getItemBag().size(), 132);
    }

    /**
     * This test controls the pickFormBoard method.
     *
     * @throws IllegalAccessException
     */
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
}
