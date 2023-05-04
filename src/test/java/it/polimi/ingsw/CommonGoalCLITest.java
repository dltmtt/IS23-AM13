package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.layouts.*;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommonGoalCLITest {

    @BeforeEach
    void setUp() {
        SettingLoader.loadBookshelfSettings();
    }

    @Test
    void firstTest() {
        Corners corners = new Corners(1, 1);
        Diagonal diagonal = new Diagonal(1, 1, Bookshelf.getColumns());
        Stair stair = new Stair(1, 6, Bookshelf.getColumns());
        ItemsPerColor itemPerColor = new ItemsPerColor(8, 8);
        XShape xShape = new XShape(1, 1, 3);
        Group group1 = new Group(0, 0, 4, 4);
        Group group2 = new Group(0, 0, 6, 2);
        Square square = new Square(1, 1, 2, 4);
        FullLine fullLine1 = new FullLine(1, 3, 3, false); //3 colonne con max 3 elem diversi
        FullLine fullLine2 = new FullLine(6, 6, 2, false); // 2 colonne tutte diverse e verticali
        FullLine fullLine3 = new FullLine(1, 3, 4, true); // 3 orizzontali con max 3
        FullLine fullLine4 = new FullLine(6, 6, 2, true); // 2 righe tutte diverse
        //        corners.cli_print();
        //        System.out.println("\n");
        ////        diagonal.cli_print();
        //        System.out.println("\n");
        ////        stair.cli_print();
        //        System.out.println("\n");
        ////        itemPerColor.cli_print();
        //        System.out.println("\n");
        //        xShape.cli_print();
        //        System.out.println("\n");
        //        group1.cli_print();
        //        System.out.println("\n");
        //        group2.cli_print();
        //        System.out.println("\n");
        //        square.cli_print();
        //        System.out.println("\n");
        //        fullLine1.cli_print();
        //        System.out.println("\n");
        //        fullLine2.cli_print();
        //        System.out.println("\n");
        //        fullLine3.cli_print();
        //        System.out.println("\n");
        //        fullLine4.cli_print();
        //    }
    }
}
