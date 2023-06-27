package it.polimi.ingsw;

import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemTest {
    @Test
    public void fileName(){
        boolean b = false;

        String im = "y0.png";
        Item item = new Item(Color.YELLOW, 0);
        if(item.fileName().equals(im))
            b=true;
        assertTrue(b);
    }
}
