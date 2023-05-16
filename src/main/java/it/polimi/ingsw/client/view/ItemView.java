package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.CliUtilities;

import java.util.List;

public class ItemView {

    public static void printItems(List<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            String item = (i) + ") " + CliUtilities.itemContent(items.get(i));
            System.out.println(item);
        }
    }
}
