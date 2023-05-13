package it.polimi.ingsw.client.view;

import it.polimi.ingsw.server.model.Board;
import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.server.model.Item;
import it.polimi.ingsw.utils.CliUtilities;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GameCliView extends GameView {

    /**
     * Reads the username from the command line and prompts the user to confirm it.
     * <p>If the user confirms the input, the method prints a welcome message.
     * <p>If the user does not confirm the input, the method prints a prompt to
     * insert the username again and calls itself recursively.
     *
     * @return the username inserted by the user
     */
    @Override
    public String readUsername() {
        String username = null;

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            username = in.readLine();
            boolean answerConfirmed = CliUtilities.confirmInput(username);

            while (!answerConfirmed) {
                System.out.print(insertUsernameAgainPrompt);
                username = in.readLine();
                answerConfirmed = CliUtilities.confirmInput(username);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while reading the username.");
        }

        return username;
    }

    @Override
    public int readNumber() {
        int n = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            n = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            System.err.println("An error occurred while reading the number.");
        }
        return n;
    }

    @Override
    public boolean readYesOrNo() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String response = in.readLine();
            return response.equalsIgnoreCase("y");
        } catch (IOException e) {
            System.err.println("An error occurred while reading the answer.");
            return false;
        }
    }

    @Override
    public String showLogin() {
        showMessage(insertUsernamePrompt);
        return readUsername();
    }

    public int promptAge() {
        showMessage(insertAgePrompt);
        return readNumber();
    }

    public boolean promptFirstGame() {
        try {
            return CliUtilities.askYesNoQuestion(firstGameQuestion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //        return readYesOrNo();
    }

    public int promptNumberOfPlayers() {
        showMessage(insertNumberOfPlayersPrompt);
        return readNumber();
    }

    @Override
    public void showPersonalGoal(int card) throws IOException, ParseException {
        PersonalGoalView personalGoalView = new PersonalGoalView(card);
        personalGoalView.printLayout();
    }

    @Override
    public void showCommonGoal(String card, int occurrences, int size, boolean horizontal) {
        CommonGoalView.print(card, occurrences, size, horizontal);
    }

    @Override
    public List<Integer> showPick() {
        System.out.println("It's your turn! Do your move!");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        List<Integer> move = new ArrayList<>();
        String[] coord = null;
        String start, end;
        do {
            do {
                try {
                    str = in.readLine();
                } catch (IOException e) {
                    System.err.println("An error occurred while reading the move.");
                }
                //1,2-2,3
                str = str.replace(" ", "");
                //split start and end coordinate
                coord = str.split("-");
            } while (coord.length != 2);
            start = coord[0];
            end = coord[1];
            //split start and end coordinate in row and column
        } while (start.split(",").length != 2 || end.split(",").length != 2);
        String[] startCoord = start.split(",");
        String[] endCoord = end.split(",");
        //convert string to int
        move.add(Integer.parseInt(startCoord[0]));
        move.add(Integer.parseInt(startCoord[1]));
        move.add(Integer.parseInt(endCoord[0]));
        move.add(Integer.parseInt(endCoord[1]));
        return move;
    }

    @Override
    public void showBoard(Board board) {
        BoardView boardView = new BoardView(board);
        System.out.println("The board:");
        boardView.printBoard();
    }

    @Override
    public void showBookshelf(Bookshelf bookshelf) {
        BookshelfView bookshelfView = new BookshelfView(bookshelf);
        System.out.println("Your Bookshelf:");
        bookshelfView.printBookshelf();
    }

    @Override
    public void showStartGame() {
        showMessage(welcomeMessage);
        //        String title = """
        //                ^?555J^                                                             \s
        //                         .7777~     !77?7.                       .?B#JJ5B#!                        ....   .^77!:                       \s
        //                         .YB#&Y.    Y&&&Y.                       7G&?.5##&5  :Y5P5^                :PG! .7GP?!75Y.                     \s
        //                           YB#?     ?&&P.                        ?G&7 ^?5J^   ~G#J.                 PB^ Y&J.?GY!&J                     \s
        //                           JB##~   ###5      .:^!   ~!!~:      ^5&#J:       :P#?                 .PB^~GB: YBBG5^                     \s
        //                           JBPPG: :GGP#5     75B##!  .Y&B7:       :Y#&#Y~     :P#J:~~~:            .PB^~PB:  :::~!^                    \s
        //                           JGP^G5.5B~5#5      .~P#5.:Y&G^           ~YB&#P7   ^P#BBBBBGJ.   .^~~:  .5G^ 7BJ    .Y#5.   .^~^.           \s
        //                           ?GG.^GGB!:5#5        :JBBG#5:              .7G##5. :P#BJ^:7#B~ .75?!!J5^ 5G^^JG#5YY::7J7. ^JY7!7YJ.         \s
        //                           ?GG: J#5 :5BY   .:::  :JBBJ.         :7J?!.  :GBB? :5BY   :GG^.JBP?777GY.YG^.^:5B!..^PBY.^5BY77!JG!         \s
        //                           ?PB^.?JJ:^5BY  75GGB? ~PG7          .PGBBP5: .PBPJ :5B?   ~GY :5B!^^~??^.YP^   !BY  .5B? 7P5^^^!J7:         \s
        //                          .?PB7     7PB5 :YGP5PYJGP~           .5BG555!!5GPJ~ :5G?   ?GY .YB?. 7GGY.JP^   7G5. .5G? PG .YBB!         \s
        //                          7Y555!   !Y555?.~JPPPPPY^             .!J555PP5J7:  ?55Y^ !555! .?55Y55J^:Y5!  ^Y5Y^ ^Y5?. ^J5YY55?:         \s
        //                          ......    .....   :^^:.                  ...::..    .....  ....    ....   ...   ....  ...    ....
        //                """;
        //        System.out.println(title);
    }

    @Override
    public void showEndGame(List<String> winners) {
        System.out.println("The game is over!");
        System.out.println("The winners are : ");
        for (String winner : winners) {
            System.out.println(winner);
        }
    }

    @Override
    public boolean showRearrange() throws IOException {
        return CliUtilities.askYesNoQuestion("Do you want to rearrange your picked items?");
    }

    @Override
    public List<Integer> rearrange(List<Item> items, int size) {
        ItemView.printItems(items);
        showMessage("Insert the new order of the picked items : \n");
        List<Integer> newOrder = new ArrayList<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        try {
            str = in.readLine();
        } catch (IOException e) {
            System.err.println("An error occurred while reading the move.");
        }
        str = str.replace(" ", "");
        String[] numb = str.split(",");
        newOrder.add(Integer.parseInt(numb[0]));
        newOrder.add(Integer.parseInt(numb[1]));
        return newOrder;
    }

    @Override
    public void showCurrentScore(int score) {
        System.out.println("Your current score is : " + score);
    }

    @Override
    public int showInsert() {
        System.out.println("Insert the index of the column where you want to insert the picked items : ");

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            return Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("An error occurred while reading the insert.");
        }
        return 0;
    }

    @Override
    public void showMessage(String message) {
        System.out.print(message);
    }
}
