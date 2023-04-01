package it.polimi.ingsw;

import it.polimi.ingsw.model.game.Bookshelf;
import it.polimi.ingsw.model.item.Item;
import it.polimi.ingsw.utils.BookshelfUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GroupTest {
    Bookshelf bookshelf;
    List<Item> items;

    @BeforeAll
    static void setupAll() {
        BookshelfUtilities.loadSettings();
    }

    @BeforeEach
    void setup() {
        bookshelf = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
        items = new ArrayList<>();
    }

    @Test
    public void testGroup() {
        // Testing possible problematics dispositions of the bookshelves
        bookshelf.clearBooleanMatrix();
        bookshelf.clearBookshelf();
    }
}
