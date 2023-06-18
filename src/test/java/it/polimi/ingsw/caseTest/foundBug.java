package it.polimi.ingsw.caseTest;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.layouts.FullLine;
import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class foundBug {

    @BeforeAll
    static void setupAll() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    void test1() {
        Bookshelf bookshelf = new Bookshelf();
        List<Item> items = new ArrayList<>();
        items.add(new Item(Color.YELLOW, 0));
        items.add(new Item(Color.WHITE, 0));
        bookshelf.insert(0, items);

        items.clear();

        items.add(new Item(Color.WHITE, 0));
        items.add(new Item(Color.PINK, 0));

        bookshelf.insert(1, items);

        items.clear();

        items.add(new Item(Color.BLUE, 0));
        items.add(new Item(Color.LIGHTBLUE, 0));

        bookshelf.insert(2, items);

        items.clear();

        items.add(new Item(Color.LIGHTBLUE, 0));
        items.add(new Item(Color.GREEN, 0));

        bookshelf.insert(3, items);

        items.clear();

        FullLine fullLine = new FullLine(5, 5, 2, true);

        CliUtilities.stringifyBookshelf(bookshelf.getItems()).stream().forEach(System.out::println);

        assert !fullLine.check(bookshelf);

        items.add(new Item(Color.GREEN, 0));
        items.add(new Item(Color.YELLOW, 0));

        bookshelf.insert(4, items);

        CliUtilities.stringifyBookshelf(bookshelf.getItems()).stream().forEach(System.out::println);

        assert fullLine.check(bookshelf);
    }
}
