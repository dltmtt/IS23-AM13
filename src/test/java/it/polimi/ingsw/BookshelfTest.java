package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookshelfTest {
    @org.junit.jupiter.api.Test
    void insertAnElement() {
        Bookshelf bookshelf = new Bookshelf();
        Item item = new Item(Color.BLUE, 1);
        List<Item> items = new ArrayList<>();
        items.add(item);

        bookshelf.insert(0, items);
        assertEquals(bookshelf.getFreeCellsInColumn(0), 4);
    }
}