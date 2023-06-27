package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.CommonGoal;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.layouts.Diagonal;
import it.polimi.ingsw.server.model.layouts.Layout;
import it.polimi.ingsw.server.model.layouts.Stair;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    void getCommonGoalNames(){
        List <Boolean> commons = new ArrayList<>();
        List <CommonGoal> commonGoal = new ArrayList<>();
        commons.add(true);
        commons.add(false);

        Player player = new Player("test", 0, true, true, true, commons);
        Layout s = new Diagonal(1, 3, 2);
        Layout s2 = new Stair(1, 6, Bookshelf.getColumns());
        commonGoal.add(new CommonGoal(s,2));
        commonGoal.add(new CommonGoal(s2,2));
        player.setCommonGoals(commonGoal);

        assertEquals("diagonal", player.getCommonNames().get(0));
        assertEquals("stair", player.getCommonNames().get(1));

    }

    @Test
    void setPlayer(){
        Player p = new Player("Ciao", 0, true, true, true);
        p.setIsFirstPlayer(true);
        assertTrue(p.isFirstPlayer());
        assertEquals("Ciao",p.getNickname());
        p.setHasEndGameCard(true);
    }
}
