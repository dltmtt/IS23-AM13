package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PlayerTest {

    @Test
    void rearrange() {
        Player player = new Player("test", 0, true, true, true);
        List<Item> coordinates = new ArrayList<>();
        coordinates.add(new Item(Color.BLUE, 1));
        coordinates.add(new Item(Color.BLUE, 2));
        System.out.println(coordinates);
        List<Integer> sort = new ArrayList<>();
        sort.add(1);
        sort.add(0);
        player.rearrangePickedItems(coordinates, sort);
        System.out.println(coordinates);
    }
}
