package it.polimi.ingsw;

import java.util.List;
import java.util.Random;

public class Board {
    //matrix 9x9
    private Item[][] boardMatrix;
    private List<Item> itemBag;
    private UsableCells usableCells;
    private int numOfPlayer;

    public Board(int numOfPlayer) {
        boardMatrix=new Item[9][9];
        usableCells=new UsableCells(numOfPlayer);
        this.numOfPlayer=numOfPlayer;
        /**
         * bag of item initialized
         *
         * i%3-> for the 3 possible images for each color, it is only used for the view.
         *
         * i<22->'cause each color has 22 occurences
         *
         * At the end we have a list with 132 elements(item)
         **/
        for(int i=0;i<22;i++){
            itemBag.add(new Item(Color.GREEN,i%3));
        }

        for(int i=0;i<22;i++){
            itemBag.add(new Item(Color.PINK,i%3));
        }
        for(int i=0;i<22;i++){
            itemBag.add(new Item(Color.WHITE,i%3));
        }
        for(int i=0;i<22;i++){
            itemBag.add(new Item(Color.YELLOW,i%3));
        }
        for(int i=0;i<22;i++){
            itemBag.add(new Item(Color.BLUE,i%3));
        }
        for(int i=0;i<22;i++){
            itemBag.add(new Item(Color.LIGHTBLUE,i%3));
        }


    }

    public void fill(){

        //inserire eccezione di sacca vuota

        /**
         * In every turn, it is drawn from the item bag a random item
         *
         * It is generated a random number, used to "choose" an item from the list, then it's removed
         */
        Random randNumberGenerator = new Random();
        for(int row=0;row<9;row++){
            for(int column=0;column<9;column++){
                if(usableCells.getList().contains(new IntegerPair(row,column))) {
                    int indexRandom = randNumberGenerator.nextInt(itemBag.size());
                    boardMatrix[row][column] = itemBag.get(indexRandom);
                    itemBag.remove(indexRandom);
                }

            }

        }
    }
}
