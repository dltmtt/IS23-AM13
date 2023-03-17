package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookshelfTest {
    @Test
    void insertAnElement() {
        Bookshelf bookshelf = new Bookshelf();
        Item item = new Item(Color.BLUE, 1);
        List<Item> items = new ArrayList<>();
        items.add(item);
        bookshelf.insert(0, items);
        assertEquals(bookshelf.getFreeCellsInColumn(0), 4);
    }

    @Test
    void insertZeroElements() {
        Bookshelf bookshelf = new Bookshelf();
        List<Item> items = new ArrayList<>();
        try {
            bookshelf.insert(4, items);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "No items to insert");
        }
    }

    @Test
    void fillColumn() {
        Bookshelf bookshelf = new Bookshelf();
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            items.add(new Item(Color.GREEN, 2));
        }
        bookshelf.insert(0, items);
        assertEquals(bookshelf.getFreeCellsInColumn(0), 0);
    }

    @Test
    void insertMoreElementsThanFreeCells() {
        Bookshelf bookshelf = new Bookshelf();
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            items.add(new Item(Color.LIGHTBLUE, 3));
        }
        try {
            bookshelf.insert(2, items);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Not enough free cells in column 2");
        }
    }
}