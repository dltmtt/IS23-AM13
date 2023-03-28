package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Corners extends Layout {
    public Corners(int minDifferent, int maxDifferent) {
        //set width and height to 0 because it takes the number of rows and columns directly from the examined bookshelf
        super(0, 0, minDifferent, maxDifferent);
    }

    /*@Override
    public boolean check(Bookshelf b) {
        if (b.getItemAt(0, 0).isPresent()) {
            Color current_color = b.getItemAt(0, 0).get().getColor();
            if (b.getItemAt(0, 4).isPresent() && b.getItemAt(0, 4).get().getColor().equals(current_color)) {
                if (b.getItemAt(5, 0).isPresent() && b.getItemAt(5, 0).get().getColor().equals(current_color)) {
                    return b.getItemAt(5, 4).isPresent() && b.getItemAt(5, 4).get().getColor().equals(current_color);
                }
            }
        }
        return false;
    }*/

    @Override
    public boolean check(Bookshelf b) {
        List<Color> colorList=new ArrayList<>();
        Color color;
        if(b.getFreeCellsInColumn(0)!=0 || b.getFreeCellsInColumn(b.getColumns()-1)!=0){
            return false;
        }else{
            color=b.getItemAt(0, 0).get().getColor();
            colorList.add(color);
            color=b.getItemAt(b.getRows()-1, 0).get().getColor();
            colorList.add(color);
            color=b.getItemAt(b.getRows()-1, b.getColumns()-1).get().getColor();
            colorList.add(color);
            color=b.getItemAt(0, b.getColumns()-1).get().getColor();
            colorList.add(color);

            colorList=colorList.stream().distinct().collect(Collectors.toList());
            return colorList.size() >= getMinDifferent() && colorList.size() <= getMaxDifferent();
        }
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }


}
