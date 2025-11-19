import java.util.List;

public class BotPlayer extends Player {

    public BotPlayer(String name) {
        super(name);
    }

    @Override
    public boolean chooseFlip() {
        List<Card> hand = getRoundCards();
        
        int currentSum = 0;
        int cardsCount = hand.size();

        for (Card c : hand) {
            if (c instanceof NumberCard) {
                currentSum += ((NumberCard) c).getNumber();
            }
        }

        System.out.println("Bot " + getName() + " pensando... (Soma atual: " + currentSum + ")");

        if (currentSum >= 25 || cardsCount >= 6) {
            System.out.println("Bot " + getName() + " decidiu PARAR.");
            return false; 
        }

        System.out.println("Bot " + getName() + " decidiu VIRAR mais uma.");
        return true;
    }
}
