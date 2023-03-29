package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.FullLine;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Games.Bookshelf;
import it.polimi.ingsw.Models.Item.Color;
import it.polimi.ingsw.Models.Item.Item;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FullLineTest {

    public void createColumn(Bookshelf b, int different, int numberofcol) {
        Color[] colors = Color.values();
        List<Item> itemList = new ArrayList<>();
        for (int col = 0; col < numberofcol; col++) {
            itemList.clear();
            for (int i = 0; i < different; i++) {
                itemList.add(new Item(colors[i], 0));
            }

            for (int i = 0; i < b.getRows() - different; i++) {
                itemList.add(new Item(colors[0], 0));
            }

            b.insert(col, itemList);
        }
        //fill the remaining columns with random colors
        for (int col = numberofcol; col < b.getColumns(); col++) {
            itemList.clear();
            for (int i = 0; i < b.getRows(); i++) {
                itemList.add(new Item(Color.randomColor(), 0));
            }
            b.insert(col, itemList);
        }


    }

    public void createRow(Bookshelf b, int different, int numberofrow) {
        Color[] colors = Color.values();
        List<Item> itemList = new ArrayList<>();
        List<Item> goalRow = new ArrayList<>();
        for (int i = 0; i < different; i++) {
            goalRow.add(new Item(colors[i], 0));
        }
        for (int i = 0; i < b.getColumns() - different; i++) {
            goalRow.add(new Item(colors[0], 0));
        }


        for (int number = 0; number < numberofrow; number++) {
            for (int i = 0; i < b.getColumns(); i++) {
                b.insert(i, Collections.singletonList(goalRow.get(i)));
            }
        }

        for (int col = 0; col < b.getColumns(); col++) {
            itemList.clear();
            for (int i = 0; i < b.getFreeCellsInColumn(col); i++) {
                itemList.add(new Item(Color.randomColor(), 0));
            }
            b.insert(col, itemList);

        }
    }

    @Test
    public void ThreeColumMax3DiffCol() {
        Bookshelf b = new Bookshelf();
        Layout fullLine = new FullLine(0, 0, 1, 3, 3, false);
        createColumn(b, 2, 3);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }

    @Test
    public void TwoColumn6Diff() {
        Bookshelf b = new Bookshelf();
        Layout fullLine = new FullLine(0, 0, 6, 6, 2, false);
        createColumn(b, 6, 2);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }

    @Test
    public void FourRowsMax3DiffCol() {
        Bookshelf b = new Bookshelf();
        Layout fullLine = new FullLine(0, 0, 1, 3, 3, true);
        createRow(b, 2, 3);
        if (!fullLine.check(b)) {
            b.cli_print();
        }
        assertTrue(fullLine.check(b));
    }
}
