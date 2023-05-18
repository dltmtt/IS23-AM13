package it.polimi.ingsw.utils;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class CliUtilities {

    public static final String emptyCell = "⬜️";
    public static final String filledCell = "⬛";
    public static final String diffCell = "\uD83D\uDFE5";
    public static final String upperLeftBox = "╭";
    public static final String upperRightBox = "╮";
    public static final String lowerLeftBox = "╰";
    public static final String lowerRightBox = "╯";
    public static final String SUCCESS_COLOR = "\u001B[32m";
    public static final String ERROR_COLOR = "\u001B[31m";
    public static final String WARNING_COLOR = "\u001B[33m";
    public static final String GRAY = "\u001B[37m";
    public static final String RESET = "\u001B[0m";
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

    /**
     * Asks the user a close-ended question and returns the answer.
     * <p>The question is printed to the console and the user is asked to enter an answer.
     * <p>The list of possible answers is printed in brackets and the default answer, which
     * has to be among the possible answers, is printed in uppercase.
     * <p>If the user presses enter, the default answer is returned.
     * <p>If the user enters an invalid answer, instructions are printed and the user is
     * asked to enter an answer again.
     * <p>
     * Example: the invocation
     * <pre>    askCloseEndedQuestion("Do you want to exit", List.of("yes", "no"), "NO")</pre>
     * prints
     * <pre>    Do you want to exit [yes/NO]?</pre>
     *
     * @param question      the question to ask the user (with or without a question mark)
     * @param validAnswers  a list of valid answers (case is ignored)
     * @param defaultAnswer the default answer (case is ignored)
     * @return the answer given by the user
     */
    public static String askCloseEndedQuestion(String question, List<String> validAnswers, String defaultAnswer) {
        // Make a mutable copy of the answers and convert them to lowercase (case is ignored)
        String lowercaseDefaultAnswer = defaultAnswer.toLowerCase();
        List<String> lowercaseValidAnswers = new ArrayList<>(List.copyOf(validAnswers));
        lowercaseValidAnswers.replaceAll(String::toLowerCase);

        // Assume that there are at least two valid answers and that the default answer is among them
        assert lowercaseValidAnswers.size() >= 2;
        assert lowercaseValidAnswers.contains(lowercaseDefaultAnswer);

        // Remove the question mark if present (it will be added after the list of valid answers)
        if (question.endsWith("?")) {
            question = question.substring(0, question.length() - 1);
        }

        // Print the question with a list of valid answers in brackets
        // and the default answer in uppercase
        System.out.print(question + " [");
        for (int i = 0; i < lowercaseValidAnswers.size(); i++) {
            String answer = lowercaseValidAnswers.get(i);
            if (answer.equals(lowercaseDefaultAnswer)) {
                System.out.print(answer.toUpperCase());
            } else {
                System.out.print(answer);
            }
            if (i < lowercaseValidAnswers.size() - 1) {
                System.out.print("/");
            }
        }
        System.out.print("]? ");

        String answer = null;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            answer = in.readLine().toLowerCase();

            while (!answer.equals("") && !lowercaseValidAnswers.contains(answer)) {
                System.err.print("Invalid answer. Press enter to select the default answer (the uppercase one in the brackets)\n" + "or enter one of the answers in the brackets (case is ignored): ");
                answer = in.readLine().toLowerCase();
            }

            // If the user pressed enter, use the default answer
            if (answer.equals("")) {
                answer = lowercaseDefaultAnswer;
            }
        } catch (IOException e) {
            System.err.println("Error while reading the answer");
        }

        return answer;
    }

    /**
     * Asks the user to confirm the entered input.
     *
     * @param string the input to confirm
     * @return true if the user confirmed the input, false otherwise
     * @see #askCloseEndedQuestion(String, List, String)
     */
    public static boolean confirmInput(String string) {
        System.out.print("You entered " + string + ". ");
        String answer = askCloseEndedQuestion("Is that correct?", List.of("y", "n"), "y");
        return answer.equals("y");
    }

    /**
     * Asks the user a close-ended question with a yes/no answer and returns the answer.
     *
     * @param question
     * @return true if the user answered yes, false otherwise
     * @see #askCloseEndedQuestion(String, List, String)
     */
    public static boolean askYesNoQuestion(String question, String defaultAnswer) {
        String answer = askCloseEndedQuestion(question, List.of("y", "n"), defaultAnswer);
        return answer.equals("y");
    }

    public static String emoji(Color color, int variant) {
        // switch for the emoji type
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
        return cell.map(item -> Color.toANSItext(item.color(), true) + " " + emoji(item.color(), item.number()) + "  " + Color.RESET_COLOR).orElse(emptySpace);
    }

    public static String itemContent(Item item) {
        return Color.toANSItext(item.color(), true) + " " + emoji(item.color(), item.number()) + "  " + Color.RESET_COLOR;
    }

    /**
     * Transforms a bookshelf in a pretty list of strings :)
     *
     * @param items
     * @return a list of strings
     * @throws IllegalArgumentException
     */
    public static List<String> stringifyBookshelf(Optional<Item>[][] items) throws IllegalArgumentException {

        if (Bookshelf.getRows() == 1) {

            if (Bookshelf.getColumns() == 1) {
                // Single row, single column case
                //╓─────╖ top row
                //║     ║ middle row
                //╙─────╜ bottom row
                return List.of(smallTop, bookshelfBorder + emptySpace + bookshelfBorder, smallBottom);
            } else {
                // Single row, multiple columns case
                //╓─────      ╥─────      ╥─────╖ top row
                //║      [...]║      [...]║     ║ middle row
                //╙─────      ╨─────      ╨─────╜ bottom row
                StringBuilder topRow = new StringBuilder();
                StringBuilder middleRow = new StringBuilder();
                StringBuilder bottomRow = new StringBuilder();

                for (int col = Bookshelf.getColumns() - 1; col >= 0; col--) {
                    if (col == Bookshelf.getColumns() - 1) {
                        // first column
                        topRow.append(topLeftBookshelfBorder);
                        middleRow.append(bookshelfBorder).append(cellContent(items[0][col]));
                        bottomRow.append(bottomLeftBookshelfBorder);
                    } else if (col == 0) {
                        // last column
                        topRow.append(topRightBookshelfBorder);
                        middleRow.append(bookshelfBorder).append(cellContent(items[0][col])).append(bookshelfBorder);
                        bottomRow.append(bottomRightBookshelfBorder);
                    } else {
                        // middle columns
                        topRow.append(topCenterBookshelfBorder);
                        middleRow.append(bookshelfBorder).append(cellContent(items[0][col]));
                        bottomRow.append(bottomCenterBookshelfBorder);
                    }
                }
                return List.of(topRow.toString(), middleRow.toString(), bottomRow.toString());
            }
        } else {
            if (Bookshelf.getColumns() == 1) {
                // Multiple row, single column case
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

                for (int i = Bookshelf.getRows() - 1; i >= 0; i--) {
                    if (i == Bookshelf.getRows() - 1) {
                        column.add(smallTop);
                        column.add(bookshelfBorder + cellContent(items[i][0]) + bookshelfBorder);
                        column.add(smallVers);
                    } else if (i == 0) {
                        column.add(bookshelfBorder + cellContent(items[i][0]) + bookshelfBorder);
                        column.add(smallBottom);
                    } else {
                        column.add(bookshelfBorder + cellContent(items[i][0]) + bookshelfBorder);
                        column.add(smallVers);
                    }
                }
                return column;
            } else {

                // Multiple row, multiple columns case
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
                                // first column
                                topRow.append(topLeftBookshelfBorder);
                                middleRow.append(bookshelfBorder).append(cellContent(items[row][col]));
                                bottomRow.append(middleLeftBookshelfBorder);
                            } else if (col == Bookshelf.getColumns() - 1) {
                                // last column
                                topRow.append(topRightBookshelfBorder);
                                middleRow.append(bookshelfBorder).append(cellContent(items[row][col])).append(bookshelfBorder);
                                bottomRow.append(middleRightBookshelfBorder);
                            } else {
                                // middle columns
                                topRow.append(topCenterBookshelfBorder);
                                middleRow.append(bookshelfBorder).append(cellContent(items[row][col]));
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
                                // first column
                                middleRow.append(bookshelfBorder).append(cellContent(items[row][col]));
                                bottomRow.append(bottomLeftBookshelfBorder);
                            } else if (col == Bookshelf.getColumns() - 1) {
                                // last column
                                middleRow.append(bookshelfBorder).append(cellContent(items[row][col])).append(bookshelfBorder);
                                bottomRow.append(bottomRightBookshelfBorder);
                            } else {
                                // middle columns
                                middleRow.append(bookshelfBorder).append(cellContent(items[row][col]));
                                bottomRow.append(bottomCenterBookshelfBorder);
                            }
                        }
                        printedBookshelf.add(middleRow.toString());
                        printedBookshelf.add(bottomRow.toString());
                    } else {
                        // central row
                        //║      [...]║      [...]║     ║ middle row
                        //╟─────      ╫─────      ╫─────╢ top row
                        StringBuilder middleRow = new StringBuilder();
                        StringBuilder bottomRow = new StringBuilder();

                        for (int col = 0; col < Bookshelf.getColumns(); col++) {
                            if (col == 0) {
                                // first column
                                middleRow.append(bookshelfBorder).append(cellContent(items[row][col]));
                                bottomRow.append(middleLeftBookshelfBorder);
                            } else if (col == Bookshelf.getColumns() - 1) {
                                // last column
                                middleRow.append(bookshelfBorder).append(cellContent(items[row][col])).append(bookshelfBorder);
                                bottomRow.append(middleRightBookshelfBorder);
                            } else {
                                // middle columns
                                middleRow.append(bookshelfBorder).append(cellContent(items[row][col]));
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
