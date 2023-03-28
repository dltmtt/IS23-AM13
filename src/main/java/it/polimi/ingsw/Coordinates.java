package it.polimi.ingsw;

public record Coordinates(Integer x, Integer y) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates key)) return false;
        return x.equals(key.x) && y.equals(key.y);
    }
}
