package it.polimi.ingsw;

import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.client.view.CommonGoalView.print;
import static it.polimi.ingsw.utils.CliUtilities.GRAY;
import static it.polimi.ingsw.utils.CliUtilities.RESET;

public class CommonGoalCliTest {

    @Test
    void printTest() {
        String[] types = {"corners", "diagonal", "fullLine", "group", "xShape", "itemsPerColor", "stair", "square", "somethingUnknown"};
        for (String type : types) {
            System.out.println("Type: " + type);
            System.out.println(GRAY + "beginPrint ".repeat(5) + RESET);
            print(type, 10, 3, false);
            System.out.println(GRAY + "endPrint ".repeat(5) + "\n\n\n" + RESET);
        }
    }
}
