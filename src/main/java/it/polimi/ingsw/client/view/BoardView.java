package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.Color;


public class BoardView {
    private static Board board;

    public BoardView(Board board) {
        //
        BoardView.board = board;
    }

    public void printBoard() {
        StringBuilder cell;
        Item[][] boardMatrix = board.getBoardMatrix();
        int boardSize = board.getBoardSize();
        for (int row = boardSize - 1; row >= 0; row--) {
            cell = new StringBuilder();
            for (int column = 0; column < boardSize; column++) {
                cell.append("[");
                if (boardMatrix[row][column] == null) {
                    cell.append(Color.BLACK)
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹")
                            .append(Color.RESET_COLOR);
                } else {
                    cell.append(Color.toANSItext(boardMatrix[row][column].color(), false))
                            //.append(boardMatrix[row][column].color().toString().charAt(0))
                            .append("⏹")
                            .append(Color.RESET_COLOR);
                }
                cell.append("]");
            }
            System.out.println(cell);
        }
    }

    public void drawBoard() {
        // work in progress...
    }
}
