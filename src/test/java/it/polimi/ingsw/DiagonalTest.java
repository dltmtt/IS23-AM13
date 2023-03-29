package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.Diagonal;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiagonalTest extends BookshelfUtilities {

    @Test
    void checkSingleRightDiagonal() {
        Bookshelf b = new Bookshelf();
        Layout layout = new Diagonal(5, 1, 1);
        createSingleRightDiagonal(b, 0, 0, 5);
        assertTrue(layout.check(b));
        Bookshelf b1 = new Bookshelf();
        createSingleRightDiagonal(b1, 1, 0, 5);
        assertTrue(layout.check(b1));
    }

    @Test
    void checkSingleLeftDiagonal() {
        Bookshelf b = new Bookshelf();
        Layout layout = new Diagonal(5, 1, 1);
        createSingleLeftDiagonal(b, 0, 4, 5);
        boolean result = layout.check(b);
        assertTrue(result);
        Bookshelf b1 = new Bookshelf();
        createSingleLeftDiagonal(b1, 1, 4, 5);
        assertTrue(layout.check(b1));
    }


//    @Test
//    void checkFakeRightDiagonal() {
//        Bookshelf b = new Bookshelf();
//        Layout layout = new Diagonal(4, 1, 1, false);
//        createFakeDiagonal(b);
//        assertFalse(layout.check(b));
//    }
    //the following test checks every possible combination of diagonal, starting from any row or column, of any dimension, left and right

//    @Test
//    void checkSingleDiagonals_HEAVY(){
//        Bookshelf b=new Bookshelf();
//        Layout layout;
//        boolean result;
//        for(int dimension=2; dimension<Math.min(b.getRows(), b.getColumns())+1; dimension++){
//            for(int row=0; row<b.getRows()-dimension; row++){
//                for(int col=0; col<b.getColumns()-dimension; col++){
//                    b=new Bookshelf();
//                    createSingleRightDiagonal(b, row, col, dimension);
//                    layout=new Diagonal(dimension, 1, 1, 'r', false);
//                    result=layout.check(b);
//                    if(!result){
//                        bookshelfPrint(b);
//                    }
//                    assertTrue(result, "Single right failed, row: "+row+", column: "+col+" dimension: "+ dimension);
//                }
//            }
//
//            for(int row=0; row<b.getRows()-dimension; row++) {
//                for (int col = dimension; col < b.getColumns(); col++) {
//                    b = new Bookshelf();
//                    createSingleLeftDiagonal(b, row, col, dimension);
//                    layout = new Diagonal(dimension, 1, 1, 'l', false);
//                    result=layout.check(b);
//                    if(!result){
//                        bookshelfPrint(b);
//                    }
//                    assertTrue(result, "Single left failed, row: "+row+", column: "+col+" dimension: "+ dimension);
//                }
//            }
//        }
//    }


    @Test
    public void provaRight() {
        Bookshelf b = new Bookshelf();
        Layout layout;
        boolean result;
        for (int dimension = 1; dimension < Math.min(b.getRows(), b.getColumns()) + 1; dimension++) {
            for (int row = 0; row < b.getRows() - dimension + 1; row++) {
                for (int col = 0; col < b.getColumns() - dimension + 1; col++) {
                    b = new Bookshelf();
                    createSingleRightDiagonal(b, row, col, dimension);
                    b.cli_print();
                    System.out.println("row: " + row + ", column: " + col + " dimension: " + dimension);
                }
            }
        }
    }

    @Test
    void checkFakeRightDiagonal() {
        Layout layout = new Diagonal(5, 1, 1);
        Bookshelf b0 = new Bookshelf();
        Bookshelf b1 = new Bookshelf();
        Bookshelf b2 = new Bookshelf();
        Bookshelf b3 = new Bookshelf();
        Bookshelf b4 = new Bookshelf();
        // Empty
        createFakeDiagonal(b0, 0);
        // Only 1 right
        createFakeDiagonal(b1, 1);
        // Dimension 2 right
        createFakeDiagonal(b2, 2);
        // Dimension 3 right
        createFakeDiagonal(b3, 3);
        // Dimension 4 right
        createFakeDiagonal(b4, 4);
        assertFalse(layout.check(b0));
        assertFalse(layout.check(b1));
        assertFalse(layout.check(b2));
        assertFalse(layout.check(b3));
        assertFalse(layout.check(b4));
    }
}
