package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Color;

/**
 * This class is used to print the board in the cli
 * @see GameCliView
 */
public class BoardView {

    private static Board board;

    public BoardView(Board board) {
        BoardView.board = board;
    }

    public void updateBoard(Board boardUpdated) {
        board = boardUpdated;
    }

    /**
     * This method prints the board in the cli
     * @see GameCliView
     */
    public void printBoard() {
        StringBuilder rowBuilder;
        Item[][] boardMatrix = board.getBoardMatrix();
        int boardSize = board.getBoardSize();
        for (int row = boardSize - 1; row >= 0; row--) {
            rowBuilder = new StringBuilder();
            rowBuilder.append(row).append(" "); // Row number (y-axis)
            for (int column = 0; column < boardSize; column++) {
                rowBuilder.append("[");
                if (boardMatrix[row][column] == null) {
                    rowBuilder.append(Color.BLACK)
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹").append(Color.RESET_COLOR);
                } else {
                    rowBuilder.append(Color.toANSItext(boardMatrix[row][column].color(), false))
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹").append(Color.RESET_COLOR);
                }
                rowBuilder.append("]");
            }
            System.out.println(rowBuilder);
        }
        System.out.print("   "); // Align the 0 of the x-axis
        for (int i = 0; i < boardSize; i++) {
            System.out.print(i + "  "); // Column number (x-axis)
        }
        System.out.println();
    }
}
