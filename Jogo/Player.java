import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    private String name;
    private int totalScore;
    private List<Card> roundCards;
    private boolean busted;
    private boolean secondChance;

    public Player(String name) {
        this.name = name;
        this.totalScore = 0;
        this.roundCards = new ArrayList<Card>();
    }

    public String getName() {
        return name;
    }

    public void resetRound() {
        roundCards.clear();
        busted = false;
        secondChance = false;
    }

    public void addCard(Card c) {
        roundCards.add(c);
    }

    public List<Card> getRoundCards() {
        return roundCards;
    }

    public void setBusted(boolean b) {
        busted = b;
    }

    public boolean isBusted() {
        return busted;
    }

    public boolean hasSecondChance() {
        return secondChance;
    }

    public void giveSecondChance() {
        secondChance = true;
    }

    public void addScore(int v) {
        totalScore += v;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void resetTotalScore() {
        this.totalScore = 0;
    }

    public abstract boolean chooseFlip();
}