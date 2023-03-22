package it.polimi.ingsw;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class Rectangle extends Layout{
    private int occurrences;
    public Rectangle(int width, int height, int minDifferent, int maxDifferent, int occurrences, char direction) {
        super(width, height, minDifferent, maxDifferent, direction);
        this.occurrences=occurrences;
    }

    public boolean check(Bookshelf b) {
        if(getCurrent(b)==getOccurrences()){
            return true;
        }else{
            return false;
        }
    }

    public int getOccurrences() {
        return occurrences;
    }

    public int getCurrent(@NotNull Bookshelf b){
        int counter=0;
        int validcells=0;
        boolean[][] valid = new boolean[b.getColumns()][b.getRows()];
        for(int i=0; i<b.getRows(); i++){
            for(int j=0; j<b.getColumns(); j++){
                valid[i][j]=true;
            }
        }
        for (int col = 0; col < b.getColumns() - getWidth(); col++) {
            for(int row=0; row< b.getRows()-getHeight(); row++) {
                validcells = 0;
                if(b.getRows()-b.getFreeCellsInColumn(col)-row>=getHeight() )
                for (int width = col; width < col + getWidth(); width++) {
                    for (int height = row; height<row+getHeight(); height++){
                        //if(b.get)
                    }
                }
            }
        }
        return 1;
    }

    public String getInfo(){
        return super.getInfo()+"-occurrences=" + occurrences +" -type=rectangle";
    }
}
