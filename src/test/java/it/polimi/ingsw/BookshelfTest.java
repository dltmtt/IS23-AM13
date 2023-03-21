package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookshelfTest {
    @Test
    void insertAnElement() {
        Bookshelf bookshelf = new Bookshelf();
        Item item = new Item(Color.BLUE, 1);
        List<Item> items = new ArrayList<>();
        items.add(item);
        bookshelf.insert(0, items);
        assertEquals(bookshelf.getFreeCellsInColumn(0), bookshelf.getRows()-items.size());
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

    @Test
    void checkInsertedElements() {
        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();
        Color randomcolor;
        Optional<Item> it = Optional.empty();
        boolean val;
        Item check;
        for (int col = 0; col < b.getColumns(); col++) {
            items.clear();
            for (int row = 0; row < b.getRows(); row++) {
                randomcolor = Color.randomColor();
                check = new Item(randomcolor, 3);
                items.add(check);
            }
            b.insert(col, items);
            for (int row = 0; row < b.getRows(); row++) {
                try {
                    it = b.getItemAt(row, col);
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (row > b.getRows() || col > b.getColumns() || row < 0 || col < 0) {
                        assertEquals(e.getMessage(), "invalid input getItemAt");
                    } else {
                        fail();
                    }
                }
                assertEquals(it, Optional.ofNullable(items.get(row)));
            }
        }
    }


    @Test
    void InsertingOneItemPerTime(){
        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();
        List<Item> localCol= new ArrayList<>();
        Color randomcolor;
        Item it;
        for (int col = 0; col < b.getColumns(); col++) {
            localCol.clear();
            for (int row = 0; row < b.getRows(); row++) {
                items.clear();
                randomcolor = Color.randomColor();
                it = new Item(randomcolor, 3);
                items.add(it);
                b.insert(col, items);
                localCol.add(it);
                assertEquals(localCol, b.getColumnContent(col));
            }
        }
    }
}