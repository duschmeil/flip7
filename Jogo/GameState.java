import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameState {

    private List<Card> deck;
    private List<Card> discard;

    public GameState(String csvPath) throws Exception {
        deck = loadCsv(csvPath);
        discard = new ArrayList<Card>();
        Collections.shuffle(deck);
    }
    
    private List<Card> loadCsv(String path) throws Exception {
        List<Card> list = new ArrayList<Card>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] p = line.split(";");
            if (p[0].equalsIgnoreCase("NUMBER")) {
                int id = Integer.parseInt(p[1]);
                int number = Integer.parseInt(p[2]);
                list.add(new NumberCard(id, number));
            } else if (p[0].equalsIgnoreCase("ACTION")) {
                int id = Integer.parseInt(p[1]);
                String type = p[2];
                int value = Integer.parseInt(p[3]);
                list.add(new ActionCard(id, type, value));
            }
        }
        br.close();
        return list;
    }

    public void saveScore(Player p) throws Exception {
        FileWriter fw = new FileWriter("save.txt");
        fw.write(p.getName() + ";" + p.getTotalScore());
        fw.close();
    }

    public int loadScore() throws Exception {
        File f = new File("save.txt");
        if (!f.exists()) {
            return 0;
        }
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = br.readLine();
        br.close();
        if (line == null || line.trim().isEmpty()) {
            return 0;
        }
        String[] parts = line.split(";");
        return Integer.parseInt(parts[1]);
    }

    // ------ Lógica do jogo ------
    public Card draw() throws InvalidMoveException {
        if (deck.isEmpty()) {
            throw new InvalidMoveException("Baralho acabou!");
        }
        return deck.remove(0);
    }

    public void playNumberCard(Player p, NumberCard c) {
        p.addCard(c);
        discard.add(c);

        int sameNumberCount = 0;
        for (Card card : p.getRoundCards()) {
            if (card instanceof NumberCard) {
                NumberCard nc = (NumberCard) card;
                if (nc.getNumber() == c.getNumber()) {
                    sameNumberCount++;
                }
            }
        }

        if (sameNumberCount >= 2 && c.getNumber() != 0) {
            if (p.hasSecondChance()) {
                p.setBusted(false);
            } else {
                p.setBusted(true);
            }
        }
    }

    public void playActionCard(Player p, ActionCard c) throws InvalidMoveException {
        p.addCard(c);
        discard.add(c);

        String type = c.getType();
        if (type.equalsIgnoreCase("SECOND")) {
            p.giveSecondChance();
        } else if (type.equalsIgnoreCase("ZERO")) {
            NumberCard zero = new NumberCard(c.getId(), 0);
            playNumberCard(p, zero);
        }
    }

    public int calculate(Player p) {
        int sum = 0;
        int bonus = 0;
        int mult = 1;
        Set<Integer> uniq = new HashSet<Integer>();

        for (Card c : p.getRoundCards()) {
            if (c instanceof NumberCard) {
                int n = ((NumberCard) c).getNumber();
                if (n != 0) {
                    sum += n;
                }
                uniq.add(n);
            } else if (c instanceof ActionCard) {
                ActionCard ac = (ActionCard) c;
                String type = ac.getType();
                if (type.equalsIgnoreCase("BONUS")) {
                    bonus += ac.getValue();
                } else if (type.equalsIgnoreCase("MULTIPLIER")) {
                    mult *= ac.getValue();
                }
            }
        }

        int score = sum * mult + bonus;

        if (uniq.size() >= 7) {
            score += 15;
        }

        if (p.isBusted()) {
            return 0;
        }

        return score;
    }
}
