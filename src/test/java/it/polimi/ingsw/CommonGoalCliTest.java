package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.layouts.Corners;
import it.polimi.ingsw.server.model.layouts.Diagonal;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.server.model.layouts.Stair;
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
        corners.cli_print();
        System.out.println("\n");
        diagonal.cli_print();
        System.out.println("\n");
        stair.cli_print();
    }
}
