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
        List<Item> coord = new ArrayList<>();
        coord.add(new Item(Color.BLUE, 1));
        coord.add(new Item(Color.BLUE, 2));
        System.out.println(coord);
        List<Integer> sort = new ArrayList<>();
        sort.add(1);
        sort.add(0);
        player.rearrangePickedItems(coord, sort);
        System.out.println(coord);
    }
}
