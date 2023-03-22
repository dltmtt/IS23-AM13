package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BoardInsertTest {
    @Test
    /**
     * this tests if Cell 00 is null with 3 player
     * @throws IllegalAccessException
     */
    void filWith3PlayerCell00() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(0, 0));
    }

    /**
     * this tests if the expected cells of row 0 are null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells0x() throws IllegalAccessException {
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
     * this tests if the expected cells of row 1 are null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells1x() throws IllegalAccessException {
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
     * this tests if the expected cells of row 2 are null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells2x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(2, 0));
        assertNull(b.getItem(2, 1));
        assertNull(b.getItem(2, 7));
        assertNull(b.getItem(2, 8));

    }

    /**
     * this tests if the expected cells of row 3 are null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells3x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(3, 7));
        assertNull(b.getItem(3, 8));

    }

    /**
     * this tests if the expected cells of row 4 are null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells4x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(4, 0));
        assertNull(b.getItem(4, 8));

    }

    /**
     * this tests if the expected cells of row 5 are null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells5x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(5, 0));
        assertNull(b.getItem(5, 1));

    }

    /**
     * this tests if the expected cells of row 6 are null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells6x() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNull(b.getItem(6, 0));
        assertNull(b.getItem(6, 1));
        assertNull(b.getItem(6, 7));
        assertNull(b.getItem(6, 8));

    }

    /**
     * this tests if the expected cells of row 7 are null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells7x() throws IllegalAccessException {
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
     * this tests if the expected cells of row 8 are null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells8x() throws IllegalAccessException {
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
     * this tests if the expected cells of row 0 are NOT null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells0xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(0, 5));
    }

    /**
     * this tests if the expected cells of row 1 are NOT null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells1xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(1, 4));
        assertNotNull(b.getItem(1, 5));

    }

    /**
     * this tests if the expected cells of row 2 are NOT null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells2xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();

        assertNotNull(b.getItem(2, 2));
        assertNotNull(b.getItem(2, 3));
        assertNotNull(b.getItem(2, 4));
        assertNotNull(b.getItem(2, 5));
        assertNotNull(b.getItem(2, 6));

    }

    /**
     * this tests if the expected cells of row 3 are NOT null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells3xNotNull() throws IllegalAccessException {
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
     * this tests if the expected cells of row 4 are NOT null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells4xNotNull() throws IllegalAccessException {
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
     * this tests if the expected cells of row 5 are NOT null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells5xNotNull() throws IllegalAccessException {
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
     * this tests if the expected cells of row 6 are NOT null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells6xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(6, 2));
        assertNotNull(b.getItem(6, 3));
        assertNotNull(b.getItem(6, 4));
        assertNotNull(b.getItem(6, 5));
        assertNotNull(b.getItem(6, 6));


    }

    /**
     * this tests if the expected cells of row 7 are NOT null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells7xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(7, 3));
        assertNotNull(b.getItem(7, 4));

    }

    /**
     * this tests if the expected cells of row 8 are NOT null, with 3 player
     *
     * @throws IllegalAccessException
     */
    @Test
    void fillWith3PlayerAllCells8xNotNull() throws IllegalAccessException {
        Board b = new Board(3);
        b.fill();
        assertNotNull(b.getItem(8, 3));

    }

    @Test
    void fillWith4Player() throws IllegalAccessException {
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
}
