package it.polimi.ingsw;

import it.polimi.ingsw.Models.CommonGoalLayout.Diagonal;
import it.polimi.ingsw.Models.CommonGoalLayout.Layout;
import it.polimi.ingsw.Models.Game.Bookshelf;
import it.polimi.ingsw.TestUtility.BookshelfUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.Integer.parseInt;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiagonalTest extends BookshelfUtilities {
    Bookshelf b;

    @BeforeEach
    void setup() {
        // Read the settings from the properties file
        int rowsSetting;
        int colsSetting;

        Properties prop = new Properties();
        // In case the file is not found, the default values will be used
        try (InputStream input = new FileInputStream("settings/settings.properties")) {
            // Load a properties file
            prop.load(input);
            rowsSetting = parseInt(prop.getProperty("bookshelf.rows"));
            colsSetting = parseInt(prop.getProperty("bookshelf.columns"));
        } catch (IOException ex) {
            ex.printStackTrace();
            // If there is an error, use the default values
            rowsSetting = 5;
            colsSetting = 6;
        }
        b = new Bookshelf(rowsSetting, colsSetting);
    }

    @Test
    void checkSingleRightDiagonal() {
        Layout layout = new Diagonal(5, 1, 1);
        createSingleRightDiagonal(b, 0, 0, 5);
        assertTrue(layout.check(b));
        b.clearBookshelf();
        createSingleRightDiagonal(b, 1, 0, 5);
        assertTrue(layout.check(b));
    }

    @Test
    void checkSingleLeftDiagonal() {
        Layout layout = new Diagonal(5, 1, 1);
        createSingleLeftDiagonal(b, 0, 4, 5);
        boolean result = layout.check(b);
        assertTrue(result);
        b.clearBookshelf();
        createSingleLeftDiagonal(b, 1, 4, 5);
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

        for (int i = 0; i < 5; i++) {
            b.clearBookshelf();
            createFakeDiagonal(b, i);
            assertFalse(layout.check(b));
        }
    }
}
