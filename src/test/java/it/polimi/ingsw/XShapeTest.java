package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class XShapeTest {
    private final Layout Xshape=new XShape(3, 1, 1);
    private Bookshelf b=new Bookshelf();

    @Test
    void base_IsolatedX() {
        //chooses the color to paint the X
        Color Xcolor = Color.randomColor();

        //left and right border of the X
        List<Item> Xborder = new ArrayList<>();
        Xborder.add(new Item(Xcolor, 0));
        Xborder.add(new Item(Color.randomColor(), 0));
        Xborder.add(new Item(Xcolor, 0));

        //central column of the X
        List<Item> Xcenter = new ArrayList<>();
        Xcenter.add(new Item(Color.randomColor(), 0));
        Xcenter.add(new Item(Xcolor, 0));
        Xcenter.add(new Item(Color.randomColor(), 0));

        List<Item> itemList = new ArrayList<>();


        //this test is composed by various batches, because of the nature of random generated colors...
        //for normal testing I think 100 would be enough
        int testnumber=100;

        for (int test = 0; test < testnumber; test++) {
            //for cycle to select every possible starting row for the X shape
            for (int startingrow = 0; startingrow < b.getRows() - Xshape.getHeight() + 1; startingrow++) {
                //for cycle to select every possible starting column for the X shape
                for (int startingcol = 0; startingcol < b.getColumns() - Xshape.getWidth() + 1; startingcol++) {
                    b = new Bookshelf();

                    //creation of the base
                    for (int col = 0; col < b.getColumns(); col++) {
                        itemList.clear();
                        for (int baserow = 0; baserow < startingrow; baserow++) {
                            itemList.add(new Item(Color.randomColor(), 0));
                        }
                        if (!itemList.isEmpty()) {
                            b.insert(col, itemList);
                        }
                    }

                    //insertion of the X shape
                    b.insert(startingcol, Xborder);
                    b.insert(startingcol + 1, Xcenter);
                    b.insert(startingcol + 2, Xborder);

                    //filling the remaining cells
                    for (int col = 0; col < b.getColumns(); col++) {
                        itemList.clear();
                        if(b.getFreeCellsInColumn(col)!=0) {
                            for (int i = 0; i < b.getFreeCellsInColumn(col); i++) {
                                itemList.add(new Item(Color.randomColor(), 0));
                            }
                            b.insert(col, itemList);
                        }
                    }

                /*if(!Xshape.check(b)) {
                    b.print();
                }*/
                    assertTrue(Xshape.check(b));
                }
            }
        }
    }
}
