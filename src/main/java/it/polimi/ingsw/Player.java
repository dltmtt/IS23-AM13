package it.polimi.ingsw;

import java.util.List;

public class Player {
    private static List<CommonGoal> commonGoals;
    private final String nickname;
    private final int age;
    private final boolean isFirstGame;
    private final boolean isFirstPlayer;
    private final boolean hasEndGameCard;
    private Bookshelf bookshelf;
    private PersonalGoal personalGoal;

    public Player(String nickname, int age, boolean isFirstGame, boolean isFirstPlayer, boolean hasEndGameCard) {
        this.nickname = nickname;
        this.age = age;
        this.isFirstGame = isFirstGame;
        this.isFirstPlayer = isFirstPlayer;
        this.hasEndGameCard = hasEndGameCard;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    public boolean isFirstGame() {
        return isFirstGame;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public boolean hasEndGameCard() {
        return hasEndGameCard;
    }
}
