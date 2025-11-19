import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameUI extends JFrame {

    private JPanel contentPane;
    private JLabel lblLastCard;
    private JLabel lblScore;
    private JLabel lblStatus;

    private GameState game;
    private Player player;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GameUI frame = new GameUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public GameUI() {
        setTitle("Flip 7 Simplificado - Single Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitle = new JLabel("Sua Carta:");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblTitle.setBounds(30, 30, 100, 20);
        contentPane.add(lblTitle);

        lblLastCard = new JLabel("[ Nenhuma ]");
        lblLastCard.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblLastCard.setBounds(130, 30, 300, 20);
        contentPane.add(lblLastCard);

        lblScore = new JLabel("Placar Total: 0");
        lblScore.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblScore.setBounds(30, 70, 400, 20);
        contentPane.add(lblScore);

        lblStatus = new JLabel("Status: Comece a jogar.");
        lblStatus.setBounds(30, 100, 400, 20);
        contentPane.add(lblStatus);

        // --- BOTÃO VIRAR ---
        JButton btnFlip = new JButton("Virar Carta");
        btnFlip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Card c = game.draw();
                    updateLastCardLabel(c);

                    if (c instanceof NumberCard) {
                        game.playNumberCard(player, (NumberCard) c);
                    } else if (c instanceof ActionCard) {
                        game.playActionCard(player, (ActionCard) c);
                    }

                    if (player.isBusted()) {
                        JOptionPane.showMessageDialog(null, "Você ESTOUROU! Perdeu os pontos desta rodada.");
                        endRound(0);
                    } else {
                        int currentRoundPoints = game.calculate(player);
                        lblStatus.setText("Status: Jogando... (Pontos na mesa: " + currentRoundPoints + ")");
                    }

                } catch (InvalidMoveException ex) {
                    JOptionPane.showMessageDialog(null, "O baralho acabou!");
                    int points = game.calculate(player);
                    endRound(points);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnFlip.setBounds(30, 150, 120, 40);
        contentPane.add(btnFlip);

        // --- BOTÃO PARAR ---
        JButton btnStop = new JButton("Parar");
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int points = game.calculate(player);
                JOptionPane.showMessageDialog(null, "Você parou e garantiu " + points + " pontos!");
                endRound(points);
            }
        });
        btnStop.setBounds(170, 150, 120, 40);
        contentPane.add(btnStop);

        // --- INICIALIZAÇÃO ---
        initGame();
    }

    private void initGame() {
        try {
            game = new GameState("cartas.csv");
            player = new HumanPlayer("Jogador 1");
            updateLabels();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cards.csv: " + e.getMessage());
        }
    }

    private void updateLastCardLabel(Card c) {
        if (c instanceof NumberCard) {
            lblLastCard.setText("Número " + ((NumberCard) c).getNumber());
        } else if (c instanceof ActionCard) {
            ActionCard ac = (ActionCard) c;
            lblLastCard.setText("Ação " + ac.getType() + " (Valor: " + ac.getValue() + ")");
        }
    }

    private void endRound(int roundPoints) {
        player.addScore(roundPoints);
        
        if (player.getTotalScore() >= 200) {
            JOptionPane.showMessageDialog(this, "PARABÉNS! Você atingiu " + player.getTotalScore() + " pontos e VENCEU o jogo!");
            resetGameTotal();
            return;
        }

        player.resetRound();
        
        lblLastCard.setText("[ Nova Rodada ]");
        lblStatus.setText("Status: Sua vez.");
        updateLabels();
    }
    
    private void resetGameTotal() {
        player = new HumanPlayer("Jogador 1");
        try {
            game = new GameState("cards.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        lblLastCard.setText("[ Jogo Reiniciado ]");
        lblStatus.setText("Status: Novo jogo iniciado.");
        updateLabels();
    }

    private void updateLabels() {
        lblScore.setText("Placar Total: " + player.getTotalScore());
    }
}