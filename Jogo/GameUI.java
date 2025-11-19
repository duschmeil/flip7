import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class GameUI extends JFrame {

    private GameState gs;
    private HumanPlayer player;

    private JLabel lblTotal;
    private JLabel lblRound;
    private JLabel lblLast;

    public GameUI() {
        super("Flip7");

        try {
            gs = new GameState("cartas.csv");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar cartas: " + e.getMessage());
            System.exit(1);
        }

        String name = JOptionPane.showInputDialog(this, "Digite seu nome:", "Jogador");
        if (name == null || name.trim().isEmpty()) {
            name = "Jogador";
        }
        player = new HumanPlayer(name);

        try {
            int previousScore = gs.loadScore();
            player.addScore(previousScore);
        } catch (Exception e) {
            // se der erro, começa do zero
        }

        initUI();
    }

    private void initUI() {
        setSize(400, 300);
        setLayout(new BorderLayout());

        lblTotal = new JLabel();
        lblRound = new JLabel();
        lblLast = new JLabel("Última carta: -");

        JPanel north = new JPanel(new GridLayout(3, 1));
        north.add(lblTotal);
        north.add(lblRound);
        north.add(lblLast);

        JButton flip = new JButton("Virar");
        JButton stop = new JButton("Parar");

        flip.addActionListener(e -> {
            try {
                if (player.isBusted()) {
                    JOptionPane.showMessageDialog(this, "Você estourou! Comece uma nova rodada.");
                    return;
                }

                Card c = gs.draw();
                c.play(player, gs);
                lblLast.setText("Última carta: " + c.toString());
                lblRound.setText("Rodada: " + gs.calculate(player));
            } catch (InvalidMoveException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        stop.addActionListener(e -> {
            int pts = gs.calculate(player);
            player.addScore(pts);

            try {
                gs.saveScore(player);
            } catch (Exception ex) {
                // falha ao salvar não impede o jogo
            }

            player.resetRound();
            lblRound.setText("Rodada: 0");
            lblTotal.setText("Total: " + player.getTotalScore());
            lblLast.setText("Última carta: -");

            if (player.getTotalScore() >= 200) {
                JOptionPane.showMessageDialog(this, "Parabéns, " + player.getName() + "! Você atingiu 200 pontos!");
            }
        });

        JPanel south = new JPanel();
        south.add(flip);
        south.add(stop);

        add(north, BorderLayout.NORTH);
        add(south, BorderLayout.SOUTH);

        lblTotal.setText("Total: " + player.getTotalScore());
        lblRound.setText("Rodada: 0");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameUI();
    }
}
