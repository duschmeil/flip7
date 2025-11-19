public class NumberCard extends Card {

    private int number;

    public NumberCard(int id, int number) {
        super(id);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public void play(Player p, GameState gs) throws InvalidMoveException {
        gs.playNumberCard(p, this);
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
}
