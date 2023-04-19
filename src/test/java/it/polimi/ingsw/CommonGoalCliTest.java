package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.layouts.*;
import it.polimi.ingsw.utils.SettingLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommonGoalCliTest {

    @BeforeEach
    void setUp() {
        SettingLoader.loadBookshelfSettings();
    }


    @Test
    void firstTest() {
        Layout corners = new Corners(1, 1);
        Layout diagonal = new Diagonal(1, 1, Bookshelf.getColumns());
        Layout stair = new Stair(1, 6, Bookshelf.getColumns());
        Layout itemPerColor = new ItemsPerColor(8, 8);
        Layout xShape = new XShape(1, 1, 3);
        Layout group1 = new Group(0, 0, 4, 4);
        Layout group2 = new Group(0, 0, 6, 2);
        Layout square = new Square(1, 1, 2, 4);
        Layout fullLine1 = new FullLine(1, 3, 3, false); //3 colonne con max 3 elem diversi
        Layout fullLine2 = new FullLine(6, 6, 2, false); // 2 colonne tutte diverse e verticali
        Layout fullLine3 = new FullLine(1, 3, 4, true); // 3 orizzontali con max 3
        Layout fullLine4 = new FullLine(6, 6, 2, true); // 2 righe tutte diverse
        corners.cli_print();
        System.out.println("\n");
        diagonal.cli_print();
        System.out.println("\n");
        stair.cli_print();
        System.out.println("\n");
        itemPerColor.cli_print();
        System.out.println("\n");
        xShape.cli_print();
        System.out.println("\n");
        group1.cli_print();
        System.out.println("\n");
        group2.cli_print();
        System.out.println("\n");
        square.cli_print();
        System.out.println("\n");
        fullLine1.cli_print();
        System.out.println("\n");
        fullLine2.cli_print();
        System.out.println("\n");
        fullLine3.cli_print();
        System.out.println("\n");
        fullLine4.cli_print();

    }
}
