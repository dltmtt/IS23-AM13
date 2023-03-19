package it.polimi.ingsw;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Board {
    //matrix 9x9
    private Item boardMatrix[][];
    private List<Item> itemBag;
    private UsableCells usableCells;
    private int numOfPlayer;

    public Board(int numOfPlayer) {
        usableCells=new UsableCells(numOfPlayer)
        this.numOfPlayer=numOfPlayer;
        //initialization of bag of item
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
        //these cells have to be filled either way
        //inserire eccezione di sacca vuota
        //usable cells

        //random number generator
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
