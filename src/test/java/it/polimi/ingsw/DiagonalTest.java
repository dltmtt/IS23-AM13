package it.polimi.ingsw;

import it.polimi.ingsw.model.commonGoalLayout.Diagonal;
import it.polimi.ingsw.model.commonGoalLayout.Layout;
import it.polimi.ingsw.model.game.Bookshelf;
import it.polimi.ingsw.utils.BookshelfUtilities;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiagonalTest {
    Bookshelf b;

    @BeforeAll
    static void setupAll() {
        BookshelfUtilities.loadSettings();
    }

    @BeforeEach
    void setup() {
        b = new Bookshelf(Bookshelf.getRows(), Bookshelf.getColumns());
    }

    @Test
    void checkSingleRightDiagonal() {
        Layout layout = new Diagonal(5, 1, 1);
        BookshelfUtilities.createSingleRightDiagonal(b, 0, 0, 5);
        assertTrue(layout.check(b));
        b.clearBookshelf();
        BookshelfUtilities.createSingleRightDiagonal(b, 1, 0, 5);
        assertTrue(layout.check(b));
    }

    @Test
    void checkSingleLeftDiagonal() {
        Layout layout = new Diagonal(5, 1, 1);
        BookshelfUtilities.createSingleLeftDiagonal(b, 0, 4, 5);
        boolean result = layout.check(b);
        assertTrue(result);
        b.clearBookshelf();
        BookshelfUtilities.createSingleLeftDiagonal(b, 1, 4, 5);
        assertTrue(layout.check(b));
    }


//    @Test
//    void checkFakeRightDiagonal() {
//        Bookshelf b = new Bookshelf();
//        Layout layout = new Diagonal(4, 1, 1, false);
//        createFakeDiagonal(b);
//        assertFalse(layout.check(b));
//    }
    // The following test checks every possible combination of diagonal, starting from any row or column, of any dimension, left and right

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
        Layout layout;
        boolean result;
        for (int dimension = 1; dimension < Math.min(Bookshelf.getRows(), Bookshelf.getColumns()) + 1; dimension++) {
            for (int row = 0; row < Bookshelf.getRows() - dimension + 1; row++) {
                for (int col = 0; col < Bookshelf.getColumns() - dimension + 1; col++) {
                    b.clearBookshelf();
                    BookshelfUtilities.createSingleRightDiagonal(b, row, col, dimension);
                    b.cli_print();
                    System.out.println("row: " + row + ", column: " + col + " dimension: " + dimension);
                }
            }
        }
    }

    @Test
    void checkFakeRightDiagonal() {
        Layout layout = new Diagonal(5, 1, 1);

        for (int i = 0; i < 5; i++) {
            b.clearBookshelf();
            BookshelfUtilities.createFakeDiagonal(b, i);
            assertFalse(layout.check(b));
        }
    }
}
