import javax.swing.JOptionPane;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public boolean chooseFlip() {
        int r = JOptionPane.showConfirmDialog(
                null,
                "Virar carta?",
                "Jogada",
                JOptionPane.YES_NO_OPTION
        );
        return r == JOptionPane.YES_OPTION;
    }
}
