package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class StairTest {

    void createLeftStair(Bookshelf b){
        List<Item> itemList=new ArrayList<>();

        for(int i=0; i<5; i++){
            itemList.add(new Item(Color.randomColor(), 0));
        }


            b.insert(0, itemList);
            itemList.remove(0);
        b.insert(1, itemList);
        itemList.remove(0);
        b.insert(2, itemList);
        itemList.remove(0);
        b.insert(3, itemList);
        itemList.remove(0);
        b.insert(4, itemList);
        itemList.remove(0);

    }

    void createRightStair(Bookshelf b){
        List<Item> itemList=new ArrayList<>();

        for(int i=0; i<5; i++){
            itemList.add(new Item(Color.randomColor(), 0));
        }

        b.insert(4, itemList);
        itemList.remove(0);
        b.insert(3, itemList);
        itemList.remove(0);
        b.insert(2, itemList);
        itemList.remove(0);
        b.insert(1, itemList);
        itemList.remove(0);
        b.insert(0, itemList);
        itemList.remove(0);
    }



    @Test
    void StairCheckRowZero(){
        Bookshelf b=new Bookshelf();
        Layout stair=new Stair(b.getColumns());
        createLeftStair(b);
        assertTrue(stair.check(b));
        b=new Bookshelf();
        createRightStair(b);
        assertTrue(stair.check(b));
    }
}
