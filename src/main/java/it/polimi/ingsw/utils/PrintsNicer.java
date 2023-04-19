package it.polimi.ingsw.utils;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PrintsNicer {
    public final static String topLeftBookshelfBorder = "╓─────";
    public final static String topCenterBookshelfBorder = "╥─────";
    public final static String topRightBookshelfBorder = "╥─────╖";

    public final static String bookshelfBorder = "║";

    public final static String middleLeftBookshelfBorder = "╟─────";

    public final static String middleCenterBookshelfBorder = "╫─────";
    public final static String middleRightBookshelfBorder = "╫─────╢";

    public final static String bottomLeftBookshelfBorder = "╙─────";
    public final static String bottomRightBookshelfBorder = "╨─────╜";

    public final static String bottomCenterBookshelfBorder = "╨─────";

    public final static String emptySpace = "     ";

    public final static String smallTop = "╓─────╖";
    public final static String smallVers = "╟─────╢";
    public final static String smallBottom = "╙─────╜";

    public static String emoji(Color color, int variant) {
        //switch for the emoji type
        switch (Objects.requireNonNull(color)) {
            case GREEN -> {
                switch (variant) {
                    case 1 -> {
                        return "\uD83D\uDE3C"; //😼
                    }
                    case 2 -> {
                        return "\uD83D\uDE3B"; // 😻
                    }
                    case 3 -> {
                        return "\uD83D\uDE38"; // 😸
                    }

                    default -> {
                        return "\uD83D\uDE3F"; //😿 not found :(
                    }
                }
            }

            case BLUE -> {
                switch (variant) {
                    case 1 -> {
                        return "\uD83D\uDCF0"; // 📰
                    }
                    case 2 -> {
                        return "\uD83D\uDDBC\uFE0F";// 🖼️
                    }
                    case 3 -> {
                        return "\uD83D\uDCF7"; // 📷
                    }
                    default -> {
                        return "\uD83D\uDDA8\uFE0F"; //🖨️ not found :(
                    }
                }
            }

            case LIGHTBLUE -> {
                switch (variant) {
                    case 1 -> {
                        return "\uD83C\uDFC6"; // 🏆
                    }
                    case 2 -> {
                        return "\uD83C\uDFC5"; // 🏅
                    }
                    case 3 -> {
                        return "\uD83E\uDE99"; //🪙
                    }
                    default -> {
                        return "\uD83E\uDD48"; //🥈 not found :(
                    }

                }
            }

            case YELLOW -> {
                switch (variant) {
                    case 1 -> {
                        return "\uD83C\uDFB2"; // 🎲
                    }
                    case 2 -> {
                        return "\uD83C\uDFAE"; // 🎮
                    }
                    case 3 -> {
                        return "\uD83D\uDD79\uFE0F"; // 🕹️
                    }
                    default -> {
                        return "\uD83D\uDC7E"; //👾 not found :(
                    }
                }
            }


            case PINK -> {
                switch (variant) {
                    case 1 -> {
                        return "\uD83E\uDEB4"; // 🪴
                    }
                    case 2 -> {
                        return "\uD83C\uDF35"; // 🌵
                    }
                    case 3 -> {
                        return "\uD83C\uDF8B"; // 🎋
                    }
                    default -> {
                        return "⚗\uFE0F"; //⚗️ not found :(
                    }
                }
            }

            case WHITE -> {
                switch (variant) {
                    case 1 -> {
                        return "\uD83D\uDCD4"; // 📔
                    }
                    case 2 -> {
                        return "\uD83D\uDCD5"; // 📕
                    }
                    case 3 -> {
                        return "\uD83D\uDCD7"; // 📗
                    }
                    default -> {
                        return "\uD83D\uDCCB"; //📋 not found :(
                    }
                }
            }

            default -> {
                return "⏳";
            }
        }

    }


    public static String cellContent(Optional<Item> cell) {
        return cell.isEmpty() ? emptySpace : Color.toANSItext(cell.get().color(), true) + " " + emoji(cell.get().color(), cell.get().number()) + "  " + Color.RESET_COLOR;
    }

    /**
     * Tranforms a bookshelf in a pretty list of strings :)
     *
     * @param items
     * @return a list of strings
     * @throws IllegalArgumentException
     */

    public static List<String> stringifyBookshelf(Optional<Item>[][] items) throws IllegalArgumentException {

        if (Bookshelf.getRows() == 1) {

            if (Bookshelf.getColumns() == 1) {
                //Single row, single column case
                //╓─────╖ top row
                //║     ║ middle row
                //╙─────╜ bottom row
                return List.of(smallTop, bookshelfBorder + emptySpace + bookshelfBorder, smallBottom);
            } else {
                //Single row, multiple columns case
                //╓─────      ╥─────      ╥─────╖ top row
                //║      [...]║      [...]║     ║ middle row
                //╙─────      ╨─────      ╨─────╜ bottom row
                StringBuilder topRow = new StringBuilder();
                StringBuilder middleRow = new StringBuilder();
                StringBuilder bottomRow = new StringBuilder();

                for (int col = Bookshelf.getColumns() - 1; col >= 0; col--) {
                    if (col == Bookshelf.getColumns() - 1) {
                        //first column
                        topRow.append(topLeftBookshelfBorder);
                        middleRow.append(bookshelfBorder + cellContent(items[0][col]));
                        bottomRow.append(bottomLeftBookshelfBorder);
                    } else if (col == 0) {
                        //last column
                        topRow.append(topRightBookshelfBorder);
                        middleRow.append(bookshelfBorder + cellContent(items[0][col]) + bookshelfBorder);
                        bottomRow.append(bottomRightBookshelfBorder);
                    } else {
                        //middle columns
                        topRow.append(topCenterBookshelfBorder);
                        middleRow.append(bookshelfBorder + cellContent(items[0][col]));
                        bottomRow.append(bottomCenterBookshelfBorder);
                    }
                }
                return List.of(topRow.toString(), middleRow.toString(), bottomRow.toString());
            }
        } else {
            if (Bookshelf.getColumns() == 1) {
                //Multiple row, single column case
                //╓─────╖ top row
                //║     ║ middle row
                //╟─────╢ bottom row
                // [...]
                //║     ║ middle row
                //╟─────╢ bottom row
                // [...]
                //║     ║ middle row
                //╙─────╜ bottom row

                List<String> column = new ArrayList<>();

                for (int i = Bookshelf.getRows(); i >= 0; i--) {
                    if (i == 0) {
                        column.add(smallTop);
                        column.add(bookshelfBorder + cellContent(items[i][0]) + bookshelfBorder);
                        column.add(smallVers);
                    } else if (i == Bookshelf.getRows() - 1) {
                        column.add(bookshelfBorder + cellContent(items[i][0]) + bookshelfBorder);
                        column.add(smallBottom);
                    } else {
                        column.add(bookshelfBorder + cellContent(items[i][0]) + bookshelfBorder);
                        column.add(smallVers);
                    }
                }
                return column;
            } else {

                //Multiple row, multiple columns case
                //╓─────      ╥─────      ╥─────╖ top row
                //║      [...]║      [...]║     ║ middle row
                //╟─────      ╫─────      ╫─────╢ bottom row
                // [...]       [...]       [...]
                //║      [...]║      [...]║     ║ middle row
                //╟─────      ╫─────      ╫─────╢ top row
                // [...]       [...]       [...]
                //║      [...]║      [...]║     ║ middle row
                //╙─────      ╨─────      ╨─────╜ bottom row
                List<String> printedBookshelf = new ArrayList<>();

                for (int row = Bookshelf.getRows() - 1; row >= 0; row--) {
                    if (row == Bookshelf.getRows() - 1) {
                        //╓─────      ╥─────      ╥─────╖ top row
                        //║      [...]║      [...]║     ║ middle row
                        //╟─────      ╫─────      ╫─────╢ bottom row
                        StringBuilder topRow = new StringBuilder();
                        StringBuilder middleRow = new StringBuilder();
                        StringBuilder bottomRow = new StringBuilder();

                        for (int col = 0; col < Bookshelf.getColumns(); col++) {
                            if (col == 0) {
                                //first column
                                topRow.append(topLeftBookshelfBorder);
                                middleRow.append(bookshelfBorder + cellContent(items[row][col]));
                                bottomRow.append(middleLeftBookshelfBorder);
                            } else if (col == Bookshelf.getColumns() - 1) {
                                //last column
                                topRow.append(topRightBookshelfBorder);
                                middleRow.append(bookshelfBorder + cellContent(items[row][col]) + bookshelfBorder);
                                bottomRow.append(middleRightBookshelfBorder);
                            } else {
                                //middle columns
                                topRow.append(topCenterBookshelfBorder);
                                middleRow.append(bookshelfBorder + cellContent(items[row][col]));
                                bottomRow.append(middleCenterBookshelfBorder);
                            }
                        }
                        printedBookshelf.add(topRow.toString());
                        printedBookshelf.add(middleRow.toString());
                        printedBookshelf.add(bottomRow.toString());

                    } else if (row == 0) {
                        //║      [...]║      [...]║     ║ middle row
                        //╙─────      ╨─────      ╨─────╜ bottom row
                        StringBuilder middleRow = new StringBuilder();
                        StringBuilder bottomRow = new StringBuilder();

                        for (int col = 0; col < Bookshelf.getColumns(); col++) {
                            if (col == 0) {
                                //first column
                                middleRow.append(bookshelfBorder + cellContent(items[0][col]));
                                bottomRow.append(bottomLeftBookshelfBorder);
                            } else if (col == Bookshelf.getColumns() - 1) {
                                //last column
                                middleRow.append(bookshelfBorder + cellContent(items[0][col]) + bookshelfBorder);
                                bottomRow.append(bottomRightBookshelfBorder);
                            } else {
                                //middle columns
                                middleRow.append(bookshelfBorder + cellContent(items[0][col]));
                                bottomRow.append(bottomCenterBookshelfBorder);
                            }
                        }
                        printedBookshelf.add(middleRow.toString());
                        printedBookshelf.add(bottomRow.toString());
                    } else {
                        //central row
                        //║      [...]║      [...]║     ║ middle row
                        //╟─────      ╫─────      ╫─────╢ top row
                        StringBuilder middleRow = new StringBuilder();
                        StringBuilder bottomRow = new StringBuilder();

                        for (int col = 0; col < Bookshelf.getColumns(); col++) {
                            if (col == 0) {
                                //first column
                                middleRow.append(bookshelfBorder + cellContent(items[0][col]));
                                bottomRow.append(middleLeftBookshelfBorder);
                            } else if (col == Bookshelf.getColumns() - 1) {
                                //last column
                                middleRow.append(bookshelfBorder + cellContent(items[0][col]) + bookshelfBorder);
                                bottomRow.append(middleRightBookshelfBorder);
                            } else {
                                //middle columns
                                middleRow.append(bookshelfBorder + cellContent(items[0][col]));
                                bottomRow.append(middleCenterBookshelfBorder);
                            }
                        }
                        printedBookshelf.add(middleRow.toString());
                        printedBookshelf.add(bottomRow.toString());
                    }
                }
                return printedBookshelf;
            }
        }
    }
}