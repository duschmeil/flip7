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
    private JLabel lblRoundCards;

    public GameUI() {
        super("Flip7");

        try {
            gs = new GameState("Jogo/cartas.csv");
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
        }

        initUI();
    }

    private void updateRoundCardsLabel() {
        StringBuilder sb = new StringBuilder("Cartas da rodada: ");
        for (Card c : player.getRoundCards()) {
            sb.append(c.toString()).append("  ");
        }
        lblRoundCards.setText(sb.toString());
    }

    private void initUI() {
        setSize(400, 300);
        setLayout(new BorderLayout());

        lblTotal = new JLabel();
        lblRound = new JLabel();
        lblLast = new JLabel("Última carta: -");
        lblRoundCards = new JLabel("Cartas da rodada: -");

        JPanel north = new JPanel(new GridLayout(4, 1));
        north.add(lblTotal);
        north.add(lblRound);
        north.add(lblLast);
        north.add(lblRoundCards);

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
                updateRoundCardsLabel();
            } catch (InvalidMoveException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        stop.addActionListener(e -> {
            int pts = gs.calculate(player);
            player.addScore(pts);

            if (player.getTotalScore() >= 200) {
                JOptionPane.showMessageDialog(this,
                        "Parabéns, " + player.getName() + "! Você atingiu 200 pontos! O total será zerado.");
                player.addScore(-player.getTotalScore()); // zera o total
            }

            try {
                gs.saveScore(player);
            } catch (Exception ex) {
            }

            player.resetRound();
            lblRound.setText("Rodada: 0");
            lblTotal.setText("Total: " + player.getTotalScore());
            lblLast.setText("Última carta: -");
            lblRoundCards.setText("Cartas da rodada: -");
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
