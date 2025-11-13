public abstract class Card {

    private int id;

    public Card(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public abstract void play(Player p, GameState gs) throws InvalidMoveException;

    @Override
    public String toString() {
        return "Card(" + id + ")";
    }
}
