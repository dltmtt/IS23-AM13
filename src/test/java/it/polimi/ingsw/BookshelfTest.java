package it.polimi.ingsw;

import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class BookshelfTest {
    Bookshelf bookshelf;
    List<Item> items;

    @BeforeEach
    void setUp() {
        bookshelf = new Bookshelf();
        items = new ArrayList<>();
    }

    @Test
    void insertAnElement() {
        Item item = new Item(Color.BLUE, 1);
        items.add(item);
        bookshelf.insert(0, items);
        assertEquals(bookshelf.getFreeCellsInColumn(0), bookshelf.getRows() - items.size());
    }

    @Test
    void insertZeroElements() {
        try {
            bookshelf.insert(4, items);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "No items to insert");
        }
    }

    @Test
    void fillColumn() {
        for (int i = 0; i < bookshelf.getRows(); i++) {
            items.add(new Item(Color.GREEN, 2));
        }
        bookshelf.insert(0, items);
        for (int i = 0; i < bookshelf.getRows(); i++) {
            assertEquals(bookshelf.getItemAt(i, 0), Optional.ofNullable(items.get(i)));
        }
    }

    @Test
    void insertMoreElementsThanFreeCells() {
        for (int i = 0; i < 6; i++) {
            items.add(new Item(Color.LIGHTBLUE, 3));
        }
        int column = 2;
        try {
            bookshelf.insert(column, items);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Not enough free cells in column " + column);
        }
    }

    @Test
    void checkInsertedElements() {
        Color randomcolor;
        Optional<Item> item;
        Item check;
        for (int col = 0; col < bookshelf.getColumns(); col++) {
            items.clear();
            for (int row = 0; row < bookshelf.getRows(); row++) {
                randomcolor = Color.randomColor();
                check = new Item(randomcolor, 3);
                items.add(check);
            }
            bookshelf.insert(col, items);
            for (int row = 0; row < bookshelf.getRows(); row++) {
                try {
                    item = bookshelf.getItemAt(row, col);
                    assertEquals(item, Optional.ofNullable(items.get(row)));
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (row > bookshelf.getRows() || col > bookshelf.getColumns()) {
                        assertEquals(e.getMessage(), "Invalid row or column for the method getItemAt");
                    } else {
                        fail();
                    }
                }
            }
        }
    }

    @Test
    void InsertingOneItemPerTime() {
        List<Item> localCol = new ArrayList<>();
        Color randomcolor;
        Item item;
        for (int col = 0; col < bookshelf.getColumns(); col++) {
            localCol.clear();
            for (int row = 0; row < bookshelf.getRows(); row++) {
                items.clear();
                randomcolor = Color.randomColor();
                item = new Item(randomcolor, 3);
                items.add(item);
                bookshelf.insert(col, items);
                localCol.add(item);
                assertEquals(localCol, bookshelf.getColumnContent(col));
            }
        }
    }
}