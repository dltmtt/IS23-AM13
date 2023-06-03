package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.Color;

import java.util.Optional;

/**
 * This class is used to print the bookshelf in the cli
 *
 * @see GameCliView
 * @see CliUtilities
 */
public class BookshelfView {

    private final Bookshelf bookshelf;

    /**
     * This constructor initializes the bookshelf.
     *
     * @param bookshelf the bookshelf to be printed
     */
    public BookshelfView(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

    /**
     * This method prints the bookshelf in the console.
     */
    public void printBookshelf() {
        Optional<Item>[][] items = bookshelf.getItems();
        CliUtilities.stringifyBookshelf(items).forEach(System.out::println);
    }

    public void drawBookshelf() {

    }

    /**
     * This method prints the other bookshelves in the console.
     */
    public void printOtherBookshelf() {
        StringBuilder cell;
        Optional<Item>[][] items = bookshelf.getItems();
        for (int row = Bookshelf.getRows() - 1; row >= 0; row--) {
            cell = new StringBuilder();
            for (int column = 0; column < Bookshelf.getColumns(); column++) {
                cell.append("[");
                if (items[row][column].isEmpty()) {
                    cell.append(Color.BLACK)
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹").append(Color.RESET_COLOR);
                } else {
                    cell.append(Color.toANSItext(items[row][column].get().color(), false))
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹").append(Color.RESET_COLOR);
                }
                cell.append("]");
            }
            System.out.println(cell);
        }
    }
}
