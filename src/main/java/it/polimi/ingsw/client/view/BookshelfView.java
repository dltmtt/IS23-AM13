package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.CliUtilities;
import it.polimi.ingsw.utils.Color;

import java.util.Optional;

public class BookshelfView {

    private final Bookshelf bookshelf;

    public BookshelfView(Bookshelf bookshelf) {
        this.bookshelf = bookshelf;
    }

    public void printBookshelf() {
        Optional<Item>[][] items = bookshelf.getItems();
        CliUtilities.stringifyBookshelf(items).forEach(System.out::println);
    }

    public void drawBookshelf() {

    }

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
