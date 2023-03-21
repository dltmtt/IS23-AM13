package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class BoardTest {
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

}
