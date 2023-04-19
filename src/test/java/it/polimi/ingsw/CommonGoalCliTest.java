package it.polimi.ingsw;

import it.polimi.ingsw.server.model.layouts.Corners;
import it.polimi.ingsw.server.model.layouts.Corners;
import it.polimi.ingsw.server.model.layouts.Layout;
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
        corners.cli_print();
    }
}
