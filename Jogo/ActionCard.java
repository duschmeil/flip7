public class ActionCard extends Card {

    private String type;
    private int value;

    public ActionCard(int id, String type, int value) {
        super(id);
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public void play(Player p, GameState gs) throws InvalidMoveException {
        gs.playActionCard(p, this);
    }

    @Override
    public String toString() {
        return type;
    }
}
