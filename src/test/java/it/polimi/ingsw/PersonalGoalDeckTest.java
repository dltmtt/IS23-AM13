package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.PersonalGoal;
import it.polimi.ingsw.utils.Color;
import it.polimi.ingsw.utils.Coordinates;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonalGoalDeckTest {

    PersonalGoal personalGoal;
    Bookshelf b;

    @BeforeAll
    static void setupAll() {
        SettingLoader.loadBookshelfSettings();
    }

    @BeforeEach
    void setup() {
        b = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
    }

    // Tests the right setup of personal goal
    @Test
    void zeroPoints() throws IOException, ParseException {
        personalGoal = SettingLoader.loadSpecificPersonalGoal(3);
        assertEquals(0, personalGoal.getPoints(b));
    }

    @Test
    void lessThan3Points() throws IOException, ParseException {
        Bookshelf b = new Bookshelf();
        List<Item> items = new ArrayList<>();
        personalGoal = SettingLoader.loadSpecificPersonalGoal(2);
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(0, items);

        items.clear();
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.WHITE, 1));
        b.insert(1, items);

        items.clear();
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.PINK, 1));
        b.insert(2, items);

        items.clear();
        items.add(new Item(Color.LIGHTBLUE, 1));
        items.add(new Item(Color.WHITE, 1));
        items.add(new Item(Color.PINK, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        b.insert(3, items);

        items.clear();
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.GREEN, 1));
        items.add(new Item(Color.BLUE, 1));
        items.add(new Item(Color.YELLOW, 1));
        items.add(new Item(Color.YELLOW, 1));
        b.insert(4, items);

        items.clear();

        personalGoal.colorReached();
        personalGoal.colorReached();
        assertEquals(12, personalGoal.getPoints(b));
    }

    @Test
    void getRightColor() throws IOException, ParseException {
        personalGoal = SettingLoader.loadSpecificPersonalGoal(0);
        assertEquals(Color.YELLOW, personalGoal.getColor(new Coordinates(0, 3)));
    }

    @Test
    void getIndex() throws IOException, ParseException {
        personalGoal = SettingLoader.loadSpecificPersonalGoal(0);
        assertEquals(0, personalGoal.getIndex());
    }
}
