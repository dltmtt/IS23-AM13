package it.polimi.ingsw;

public class Corners extends Layout {
    public Corners(int width, int height, int minDifferent, int maxDifferent) {
        super(width, height, minDifferent, maxDifferent);
    }

    @Override
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
    }

    @Override
    public String getInfo() {
        return super.getInfo();
    }


}
