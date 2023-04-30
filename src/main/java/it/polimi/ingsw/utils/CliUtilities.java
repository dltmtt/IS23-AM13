package it.polimi.ingsw.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
     * @throws IOException if an I/O error occurs
     */
    public static String askCloseEndedQuestion(String question, List<String> validAnswers, String defaultAnswer) throws IOException {
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

        String answer;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        answer = in.readLine().toLowerCase();
        while (!answer.equals("") && !lowercaseValidAnswers.contains(answer)) {
            System.out.print("Invalid answer. Press enter to select the default answer (the uppercase one in the brackets)\n" +
                    "or enter one of the answers in the brackets (case is ignored): ");
            answer = in.readLine().toLowerCase();
        }

        // If the user pressed enter, use the default answer
        if (answer.equals("")) {
            answer = lowercaseDefaultAnswer;
        }

        return answer;
    }

    /**
     * Asks the user to confirm the entered input.
     *
     * @param string the input to confirm
     * @return true if the user confirmed the input, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public static boolean confirmInput(String string) throws IOException {
        System.out.print("You entered " + string + ". ");
        String answer = askCloseEndedQuestion("Is that correct?", List.of("y", "n"), "y");
        return answer.equals("y");
    }

}
