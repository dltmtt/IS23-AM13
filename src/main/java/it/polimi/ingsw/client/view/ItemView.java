package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.CliUtilities;

import java.util.List;

/**
 * This class is used to print the items in the cli.
 *
 * @see GameCliView
 * @see CliUtilities
 */
public class ItemView {

    /**
     * This method prints the items.
     *
     * @param items the list of items to print.
     */
    public static void printItems(List<Item> items) {
        for (int i = 0; i < items.size(); i++) {
            String item = (i) + ") " + CliUtilities.itemContent(items.get(i));
            System.out.println(item);
        }
    }
}
