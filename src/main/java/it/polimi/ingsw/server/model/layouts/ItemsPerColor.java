package it.polimi.ingsw.server.model.layouts;

import it.polimi.ingsw.server.model.Bookshelf;
import it.polimi.ingsw.utils.Color;

/**
 * This is the common goal layout with eight items of the same color.
 */
public class ItemsPerColor extends Layout {

    /**
     * This is the constructor of the class.
     * @param minDifferent this is the minimum number of different items.
     * @param maxDifferent this is the maximum number of different items.
     * @throws IllegalArgumentException if the parameters are invalid
     */
    public ItemsPerColor(int minDifferent, int maxDifferent) throws IllegalArgumentException {
        super(1, 1, minDifferent, maxDifferent);
    }

    /**
     * This method gets the info of the layout.
     * @return the name of the layout
     */
    @Override
    public String getInfo() {
        return super.getInfo() + " -type=ItemsPerColor";
    }

    /**
     * This method gets the name of the layout.
     * @return the name of the layout
     */
    @Override
    public String getName() {
        return "itemsPerColor";
    }

    /**
     * This method checks if there are at least the minimum number of
     * items of any color in the bookshelf.
     *
     * @param b the bookshelf to check
     * @return true if the layout is valid for the bookshelf, false otherwise
     */
    public boolean check(Bookshelf b) throws IllegalArgumentException{
        if(b==null) {
            throw new IllegalArgumentException("Invalid bookshelf");
        }
        else{
            for(Color c: Color.values()){
                if(checkColors(b,c))
                    return true;
            }
            return false;
        }
    }

    /**
     * This method checks if there are at least the minimum number of items of the
     * specified color in the bookshelf.
     *
     * @param b the bookshelf to check
     * @param c the color to check
     * @return true if there are at least the minimum number of items of the
     * specified color in the bookshelf, false otherwise
     */
    public boolean checkColors(Bookshelf b, Color c){
        int count = 0;
        for(int i=0; i<Bookshelf.getRows();i++){
            for(int j=0; j<Bookshelf.getColumns();j++){
                if(b.getItemAt(i,j).isPresent() && b.getItemAt(i,j).get().color().equals(c))
                    count++;
            }
        }
        return count >=8;
    }
}
