package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiagonalTest {

    public void bookshelfPrint(Bookshelf b){
        String singlerow;
        List<String> stringbook=new ArrayList<>();
        for(int row=0; row<b.getRows(); row++){
            singlerow=row+"\t";
            for(int col=0; col<b.getColumns(); col++){
                singlerow+="\t"+
                        (b.getItemAt(row, col).isPresent()? b.getItemAt(row, col).get().getColor().ordinal() : " ");
            }
            stringbook.add(singlerow);
        }

        for(int i=0; i<b.getRows(); i++) {
            System.out.println(stringbook.get(stringbook.size()-i-1));
        }

        String colnum=" ";
        for(int i=0; i<b.getColumns(); i++) {
            colnum+="\t"+i;
        }
        System.out.println(" \t"+colnum);
    }


    public void createSingleRightDiagonal(Bookshelf b, int startingrow, int startingcolumn, int dimension){
        List<Item> itemlist=new ArrayList<>();
        Color diagonalcolor=Color.randomColor();

        for(int i=0; i<startingrow; i++){
            itemlist.add(new Item(Color.randomColor(), 0));
        }

        for(int i=0; i<b.getColumns(); i++){
            if(itemlist.size()>0){
                b.insert(i, itemlist);
            }
        }


        for(int i=0; i<dimension; i++){
            itemlist.clear();
            for(int j=0; j<i; j++){
                itemlist.add(new Item(Color.randomColor(), 0));
            }
            itemlist.add(new Item(diagonalcolor, 0));
            b.insert(startingcolumn+i, itemlist);
        }
    }

    //in createLeftDiagonal, (startingrow, startingcolumn) refers to the rightmost element of the diagonal

    public void createSingleLeftDiagonal(Bookshelf b, int startingrow, int startingcolumn, int dimension){
        List<Item> itemlist=new ArrayList<>();
        Color diagonalcolor=Color.randomColor();

        for(int i=0; i<=startingrow; i++){
            itemlist.add(new Item(Color.randomColor(), 0));
        }

        for(int i=0; i<b.getColumns(); i++){
            b.insert(i, itemlist);
        }


        for(int i=0; i<dimension; i++){
            itemlist.clear();
            for(int j=0; j<i; j++){
                itemlist.add(new Item(Color.randomColor(), 0));
            }
            itemlist.add(new Item(diagonalcolor, 0));
           // try {
                b.insert(startingcolumn - i, itemlist);
           // }catch(IllegalArgumentException e){
                bookshelfPrint(b);
           // }
        }
    }

    @Test
    void checkSingleRightDiagonal() {
        Bookshelf b=new Bookshelf();
        Layout layout=new Diagonal(3, 1, 1, 'r', false);
        createSingleRightDiagonal(b, 1, 1, 3);
        assertTrue(layout.check(b));
    }

    @Test
    void checkSingleLeftDiagonal() {
        Bookshelf b=new Bookshelf();
        Layout layout=new Diagonal(3, 1, 1, 'l', false);
        createSingleLeftDiagonal(b, 3, 3, 3);
        boolean result=layout.check(b);
        if(!result){
            bookshelfPrint(b);
        }
        assertTrue(result);
    }

    //the following test checks every possible combination of diagonal, starting from any row or column, of any dimension, left and right

    @Test
    void checkSingleDiagonals_HEAVY(){
        Bookshelf b=new Bookshelf();
        Layout layout;
        boolean result;
        for(int dimension=2; dimension<Math.min(b.getRows(), b.getColumns())+1; dimension++){
            for(int row=0; row<b.getRows()-dimension; row++){
                for(int col=0; col<b.getColumns()-dimension; col++){
                    b=new Bookshelf();
                    createSingleRightDiagonal(b, row, col, dimension);
                    layout=new Diagonal(dimension, 1, 1, 'r', false);
                    result=layout.check(b);
                    if(!result){
                        bookshelfPrint(b);
                    }
                    assertTrue(result, "Single right failed, row: "+row+", column: "+col+" dimension: "+ dimension);
                }
            }

            for(int row=0; row<b.getRows()-dimension; row++) {
                for (int col = dimension; col < b.getColumns(); col++) {
                    b = new Bookshelf();
                    createSingleLeftDiagonal(b, row, col, dimension);
                    layout = new Diagonal(dimension, 1, 1, 'l', false);
                    result=layout.check(b);
                    if(!result){
                        bookshelfPrint(b);
                    }
                    assertTrue(result, "Single left failed, row: "+row+", column: "+col+" dimension: "+ dimension);
                }
            }
        }
    }


    @Test
    public void provaright(){
        Bookshelf b=new Bookshelf();
        Layout layout;
        boolean result;
        for(int dimension=1; dimension<Math.min(b.getRows(), b.getColumns())+1; dimension++) {
            for (int row = 0; row < b.getRows() - dimension+1; row++) {
                for (int col = 0; col < b.getColumns() - dimension+1; col++) {
                    b = new Bookshelf();
                    createSingleRightDiagonal(b, row, col, dimension);
                    bookshelfPrint(b);
                    System.out.println("row: " + row + ", column: " + col + " dimension: " + dimension);
                    }
                }
            }
    }
}


