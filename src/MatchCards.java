import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class MatchCards {

    // Create a class for the cards
    class Card {
        String cardName;
        ImageIcon cardImageIcon;

        Card(String cardName, ImageIcon cardImageIcon) {
            this.cardName = cardName;
            this.cardImageIcon = cardImageIcon;
        }

        public String toString() {
            return cardName;
        }
    }

    // Card names
    String[] cardList = {
            "darkness",
            "double",
            "fairy",
            "fighting",
            "fire",
            "grass",
            "lightning",
            "metal",
            "psychic",
            "water"
    };

    int rows = 4;
    int columns = 5;
    int cardWidth = 90;
    int cardHeight = 128;
    int errorCount = 0;

    Timer hideCardTimer;
    boolean gameReady = false;

    // Create a deck of cards with card names and image icons
    ArrayList<Card> cardSet;
    ImageIcon cardBackImageIcon;

    int boardWidth = columns * cardWidth;
    int boardHeight = rows * cardHeight;

    JFrame frame = new JFrame("Pokemon Match Cards");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel restartGamePanel = new JPanel();
    JButton restartButton = new JButton();
    JButton card1Selected;
    JButton card2Selected;

    ArrayList<JButton> board;

    MatchCards() {

        // Create and shuffle the cards
        setUpCards();
        shuffleCards();

        // Create window for game
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Display number of errors
        textLabel.setFont(new Font("Ink Free", Font.BOLD, 20));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Errors: " + Integer.toString(errorCount));

        textPanel.setPreferredSize(new Dimension(boardWidth, 30));
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        // Create game board
        board = new ArrayList<JButton>();
        boardPanel.setLayout(new GridLayout(rows, columns));
        for (int i = 0; i < cardSet.size(); i++) {
            JButton tile = new JButton();
            tile.setPreferredSize(new Dimension(cardWidth, cardHeight));
            tile.setOpaque(true);
            tile.setIcon(cardSet.get(i).cardImageIcon);
            tile.setFocusable(false);
            tile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!gameReady) {
                        return;
                    }

                    // If the game is running, flip the card we clicked on
                    JButton tile = (JButton) e.getSource();
                    if (tile.getIcon() == cardBackImageIcon) {
                        if (card1Selected == null) {
                            card1Selected = tile;
                            int index = board.indexOf(card1Selected);
                            card1Selected.setIcon(cardSet.get(index).cardImageIcon);
                        } else if (card2Selected == null) {
                            card2Selected = tile;
                            int index = board.indexOf(card2Selected);
                            card2Selected.setIcon(cardSet.get(index).cardImageIcon);

                            // If the cards we clicked on aren't equal, up the error count and flip them
                            // back
                            if (card1Selected.getIcon() != card2Selected.getIcon()) {
                                errorCount++;
                                textLabel.setText("Errors: " + Integer.toString(errorCount));
                                hideCardTimer.start();
                            } else {
                                card1Selected = null;
                                card2Selected = null;
                            }
                        }
                    }
                }
            });
            board.add(tile);
            boardPanel.add(tile);
        }
        frame.add(boardPanel);

        // Restart game button
        restartButton.setFont(new Font("Ink Free", Font.BOLD, 16));
        restartButton.setText("Restart Game");
        restartButton.setPreferredSize(new Dimension(boardWidth, 30));
        restartButton.setFocusable(false);
        restartButton.setEnabled(false);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameReady) {
                    return;
                }

                // Reset all the variables and shuffle the cards
                gameReady = false;
                restartButton.setEnabled(false);
                card1Selected = null;
                card2Selected = null;
                errorCount = 0;
                textLabel.setText("Errors: " + Integer.toString(errorCount));
                shuffleCards();

                // Reassign all the buttons to the new cards
                for (int i = 0; i < board.size(); i++) {
                    board.get(i).setIcon(cardSet.get(i).cardImageIcon);
                }

                // Start game
                hideCardTimer.start();
            }
        });
        restartGamePanel.add(restartButton);
        frame.add(restartGamePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

        // Start game
        hideCardTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideCards();
            }
        });
        hideCardTimer.setRepeats(false);
        hideCardTimer.start();
    }

    void setUpCards() {
        cardSet = new ArrayList<Card>();
        for (String cardName : cardList) {

            // Load each card image
            Image cardImage = new ImageIcon(getClass().getResource("img/" + cardName + ".jpg")).getImage();
            ImageIcon cardImageIcon = new ImageIcon(
                    cardImage.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));

            // Create card object and add to cardSet
            Card card = new Card(cardName, cardImageIcon);
            cardSet.add(card);
        }
        cardSet.addAll(cardSet);

        // Load the back card image
        Image cardBackImage = new ImageIcon(getClass().getResource("img/back.jpg")).getImage();
        cardBackImageIcon = new ImageIcon(
                cardBackImage.getScaledInstance(cardWidth, cardHeight, java.awt.Image.SCALE_SMOOTH));
    }

    void shuffleCards() {

        // Shuffle the cards
        for (int i = 0; i < cardSet.size(); i++) {

            // Get random index within the list
            int j = (int) (Math.random() * cardSet.size());

            // Swap cards
            Card temp = cardSet.get(i);
            cardSet.set(i, cardSet.get(j));
            cardSet.set(j, temp);

        }
    }

    void hideCards() {

        // If we clicked on two cards (and they aren't the same), flip them
        if (gameReady && card1Selected != null && card2Selected != null) {
            card1Selected.setIcon(cardBackImageIcon);
            card1Selected = null;
            card2Selected.setIcon(cardBackImageIcon);
            card2Selected = null;
        }

        else {
            // Flip all the cards down
            for (int i = 0; i < cardSet.size(); i++) {
                board.get(i).setIcon(cardBackImageIcon);
            }
            gameReady = true;
            restartButton.setEnabled(true);
        }

    }
}
