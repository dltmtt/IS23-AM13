package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;

public class XShape extends Layout{
    public XShape(int dimension, int minDifferent, int maxDifferent) {
        //for now dimension parameter (width and height) are ignored
        super(3, 3, minDifferent, maxDifferent);
    }

    public boolean check(Bookshelf b){
        boolean check_iteration;
        List<Color> colorList=new ArrayList<>();
        int col;
        int row;

        for(row=0; row<b.getRows()-getHeight()+1; row++) {
            for (col = 0; col < b.getColumns() - getWidth()+1; col++) {
                if(b.getItemAt(row, col).isPresent()) {
                    //The first check to make sure there are the minimum number of item to begin the check
                    //in particular, if the check starts on (0,0) then the following cells must be filled: (2, 0), (2, 1), (2, 2)
                    //so in general, given the parameters row and column, the check can proceed if and only if, cell from (row+getHeight()-1, col) to (row+getHeight-1, col+getWidth()-1) are filled
                    check_iteration=true;
                    for (int index = col; index < col + getWidth() - 1; index++) {
                        check_iteration=check_iteration && b.getItemAt(row+getHeight()-1, index).isPresent();
                    }

                    if(check_iteration){

                        //bottom left
                        colorList.clear();
                        colorList.add(b.getItemAt(row, col).get().getColor());

                        //bottom right
                        Color color=b.getItemAt(row, col+2).get().getColor();
                        if(!colorList.contains(color)){colorList.add(color);}

                        //central
                        color=b.getItemAt(row+1, col+1).get().getColor();
                        if(!colorList.contains(color)){colorList.add(color);}

                        //upper left
                        color=b.getItemAt(row+2, col).get().getColor();
                        if(!colorList.contains(color)){colorList.add(color);}

                        //upper right
                        color=b.getItemAt(row+2, col).get().getColor();
                        if(!colorList.contains(color)){colorList.add(color);}


                        //the final check is all about the number of different colors found
                        if(colorList.size()>=getMinDifferent() && colorList.size()<=getMaxDifferent()){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public String getInfo() {
        return super.getInfo()+"X Shape";
    }
}
