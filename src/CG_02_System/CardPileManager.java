package CG_02_System;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;

import CG_03_UI.CardInfo;
import CG_04_Inputs.CardClickHandler;

public class CardPileManager {
    private BufferedImage drawPileImage;
    
    private Stack<Integer> drawPile; // Stack for draw pile (LIFO)
    private ArrayList<Integer> discardPile; // ArrayList for discard pile
    private ArrayList<Integer> handIndices; // Card indices currently in hand
    
    private static final int PILE_SIZE = 100;
    private int drawPileX;
    private int drawPileY;
    
    private CardClickHandler cardHandler;
    
    // Draw limit per turn
    private int cardsDrawnThisTurn = 0;
    private static final int MAX_DRAWS_PER_TURN = 2;
    
    // Hand size limit
    public static final int MAX_HAND_SIZE = 8;
    
    public CardPileManager(CardClickHandler handler) {
        this.cardHandler = handler;
        drawPile = new Stack<>();
        discardPile = new ArrayList<>();
        handIndices = new ArrayList<>();
        
        loadPileImages();
        initializeDeck();
    }
    
    private void loadPileImages() {
        try {
            drawPileImage = ImageIO.read(getClass().getResourceAsStream("/ui/StackedCards.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Initialize full deck in draw pile
    private void initializeDeck() {
        drawPile.clear();
        for (int i = 0; i < 8; i++) { // 8 cards total
            drawPile.push(i);
        }
        shuffleDrawPile();
        
        // Draw initial starting hand (not all cards)
        drawCards(3); // Start with 3 cards, player can draw 2 more
    }
    
    private void shuffleDrawPile() {
        // Convert stack to list, shuffle, then back to stack
        ArrayList<Integer> temp = new ArrayList<>(drawPile);
        Collections.shuffle(temp);
        drawPile.clear();
        for (int card : temp) {
            drawPile.push(card);
        }
        System.out.println("Draw pile shuffled! Cards: " + drawPile);
    }
    
    // Move card from hand to discard pile
    public void discardCard(int cardIndex) {
        if (handIndices.contains(cardIndex)) {
            handIndices.remove(Integer.valueOf(cardIndex));
            discardPile.add(cardIndex);
            System.out.println("Card " + cardIndex + " discarded. Discard pile: " + discardPile.size());
        }
    }
    
    // Draw cards from draw pile to hand (private method)
    private void drawCards(int count) {
        int spaceInHand = MAX_HAND_SIZE - handIndices.size();
        int cardsToDraw = Math.min(Math.min(count, spaceInHand), drawPile.size());
        
        for (int i = 0; i < cardsToDraw; i++) {
            if (!drawPile.isEmpty()) {
                int cardIndex = drawPile.pop(); // Draw from top of stack
                handIndices.add(cardIndex);
                cardHandler.getCardInfos()[cardIndex].setActive(true);
            }
        }
        
        System.out.println("Drew " + cardsToDraw + " cards. Hand: " + handIndices.size() + "/" + MAX_HAND_SIZE);
        
        // If draw pile is empty, reshuffle discard pile
        if (drawPile.isEmpty() && !discardPile.isEmpty()) {
            reshuffleDiscardIntoDraw();
        }
    }
    
    // Player clicks draw pile to manually draw a card
    public boolean tryDrawCard() {
        // Check hand size first
        if (handIndices.size() >= MAX_HAND_SIZE) {
            System.out.println("Hand is full! (" + MAX_HAND_SIZE + "/" + MAX_HAND_SIZE + ")");
            return false; // Signal hand is full
        }
        
        // Check if player can still draw this turn
        if (cardsDrawnThisTurn >= MAX_DRAWS_PER_TURN) {
            System.out.println("Already drew maximum cards this turn (" + MAX_DRAWS_PER_TURN + ")");
            return false;
        }
        
        // Check if draw pile has cards
        if (drawPile.isEmpty() && discardPile.isEmpty()) {
            System.out.println("No cards left to draw!");
            return false;
        }
        
        // Reshuffle if needed
        if (drawPile.isEmpty() && !discardPile.isEmpty()) {
            reshuffleDiscardIntoDraw();
        }
        
        // Draw one card
        if (!drawPile.isEmpty()) {
            drawCards(1);
            cardsDrawnThisTurn++;
            System.out.println("Drew 1 card manually. Draws this turn: " + cardsDrawnThisTurn + "/" + MAX_DRAWS_PER_TURN);
            return true;
        }
        
        return false;
    }
    
    // Check if draw pile was clicked
    public boolean isDrawPileClicked(int mouseX, int mouseY) {
        return mouseX >= drawPileX && mouseX <= drawPileX + PILE_SIZE &&
               mouseY >= drawPileY && mouseY <= drawPileY + PILE_SIZE &&
               !drawPile.isEmpty();
    }
    
    // Reshuffle discard pile back into draw pile
    private void reshuffleDiscardIntoDraw() {
        // Move all cards from discard to draw pile
        for (int card : discardPile) {
            drawPile.push(card);
        }
        discardPile.clear();
        shuffleDrawPile();
        System.out.println("Discard pile reshuffled into draw pile!");
    }
    
    // Draw the pile images on screen
    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        // Draw pile on BOTTOM LEFT CORNER
        drawPileX = 30;  // 30px from left edge
        drawPileY = screenHeight - PILE_SIZE - 30;
        
        // DRAW pile (only show if has cards)
        if (drawPileImage != null && !drawPile.isEmpty()) {
            g2d.drawImage(drawPileImage, drawPileX, drawPileY, PILE_SIZE, PILE_SIZE, null);
            
            // Draw count
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.setColor(Color.WHITE);
            String drawCount = String.valueOf(drawPile.size());
            int textX = drawPileX + PILE_SIZE / 2 - g2d.getFontMetrics().stringWidth(drawCount) / 2;
            g2d.drawString(drawCount, textX, drawPileY + PILE_SIZE / 2 + 8);
            
            // Draw label
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("DRAW", drawPileX + 10, drawPileY - 10);
            
            // Show draws remaining this turn
            g2d.setFont(new Font("Arial", Font.PLAIN, 14));
            String drawInfo = cardsDrawnThisTurn + "/" + MAX_DRAWS_PER_TURN + " draws";
            g2d.drawString(drawInfo, drawPileX, drawPileY + PILE_SIZE + 20);
        }
        
        // Optional: Show empty draw pile indicator
        if (drawPile.isEmpty() && !discardPile.isEmpty()) {
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.setColor(Color.YELLOW);
            g2d.drawString("Reshuffling...", drawPileX, drawPileY + PILE_SIZE / 2);
        }
    }
    
    // Reset for new turn - ONLY reset draw counter, DON'T discard hand
    public void resetForNewTurn() {
        cardsDrawnThisTurn = 0; // Reset draw limit
        System.out.println("New turn - draw counter reset. Can draw " + MAX_DRAWS_PER_TURN + " more cards.");
        
        // DO NOT discard hand or draw new cards
        // Player keeps their current hand
    }
    
    // Getters
    public ArrayList<Integer> getHandIndices() {
        return handIndices;
    }
    
    public int getDrawPileSize() {
        return drawPile.size();
    }
    
    public int getDiscardPileSize() {
        return discardPile.size();
    }
    
    public int getCardsDrawnThisTurn() {
        return cardsDrawnThisTurn;
    }
    
    public int getMaxDrawsPerTurn() {
        return MAX_DRAWS_PER_TURN;
    }
    
    public int getHandSize() {
        return handIndices.size();
    }
    
    public int getMaxHandSize() {
        return MAX_HAND_SIZE;
    }
    
    public boolean isHandFull() {
        return handIndices.size() >= MAX_HAND_SIZE;
    }
}