package CG_02_System;

import static CG_03_UI.CardLayout.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import CG_01_Main.Game;
import CG_03_UI.CardInfo;
import CG_03_UI.CardLayout;
import CG_04_Inputs.CardClickHandler;
import CG_05_Utilz.FontLoader;

public class CardManager {

    // Card images for the hand
    private BufferedImage[] cardImages;

    // Reference to main game and card input handler
    private Game game;
    private CardClickHandler cardClickHandler;

    // Track last hovered card
    private int lastHoveredIndex = -1;

    // Animation fields
    private float[] cardY;          // Y positions of cards during entrance
    private float targetY;          // Target Y for animation
    private boolean cardsEntering = false;
    private float speed = 8f;       // pixels per frame for slide animation
    private int[] delays;           // staggered animation delays per card
    private int frameCount = 0;

    // Card pile manager
    private CardPileManager pileManager;

    public void setPileManager(CardPileManager manager) {
        this.pileManager = manager;
    }

    // Constructor loads card images and sets references
    public CardManager(Game game) {
        this.game = game;
        this.cardClickHandler = game.getCardClickHandler();
        loadCards();
    }

    // Load card images from resources
    private void loadCards() {
        cardImages = new BufferedImage[8];
        try {
            cardImages[0] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/cards/Attack1.png"));
            cardImages[1] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/cards/Attack2.png"));
            cardImages[2] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/cards/Attack3.png"));
            cardImages[3] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/cards/Attack4.png"));
            cardImages[4] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/cards/Attack5.png"));
            cardImages[5] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/cards/HealCard.png"));
            cardImages[6] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/cards/ShieldCard.png"));
            cardImages[7] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/cards/JusticeCard2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Start slide-in animation for hand cards
    public void startCardEntrance(int screenHeight) {
        targetY = screenHeight - HEIGHT - BOTTOM_OFFSET;

        cardY = new float[cardImages.length];
        delays = new int[cardImages.length];

        for (int i = 0; i < cardY.length; i++) {
            cardY[i] = screenHeight + 50; // start offscreen
            delays[i] = i * 10;           // staggered entry
        }

        cardsEntering = true;
        frameCount = 0;
    }

    // Update slide-in animation each frame
    public void updateAnimation() {
        if (!cardsEntering)
            return;

        boolean done = true;

        for (int i = 0; i < cardY.length; i++) {
            if (frameCount >= delays[i]) {
                if (cardY[i] > targetY) {
                    cardY[i] -= speed;
                    if (cardY[i] < targetY) cardY[i] = targetY;
                    done = false;
                }
            } else {
                done = false; // still waiting for delay
            }
        }

        frameCount++;
        if (done) cardsEntering = false;
    }

    public boolean isAnimating() {
        return cardsEntering;
    }

    // Draw cards and piles
    public void draw(Graphics2D g2d, int screenWidth, int screenHeight) {
        if (cardClickHandler == null) {
            cardClickHandler = game.getCardClickHandler();
        }

        if (pileManager == null || cardClickHandler == null) return;

        // Get synchronized positions from CardClickHandler
        int[][] activePositions = cardClickHandler.getActiveCardPositions();

        // Hover info from GamePanel
        int hoveredIndex = game.getGamePanel().getHoveredCardIndex();
        float hoverProgress = game.getGamePanel().hoverProgress;

        // Draw active cards at their positions
        int y = screenHeight - HEIGHT - BOTTOM_OFFSET;
        for (int[] pos : activePositions) {
            int x = pos[0];
            int originalIndex = pos[1];
            CardInfo card = cardClickHandler.getCardInfos()[originalIndex];
            boolean isHovered = (originalIndex == hoveredIndex);

            drawCard(g2d, card, x, y, isHovered, originalIndex, hoverProgress);
        }

        // Draw card piles
        pileManager.draw(g2d, screenWidth, screenHeight);
    }

    // Draw a single card
    private void drawCard(Graphics2D g2d, CardInfo card, int x, int y, boolean isHovered, int cardIndex, float hoverProgress) {
        int cardWidth = CardLayout.WIDTH;
        int cardHeight = CardLayout.HEIGHT;

        // Hover lift effect
        int hoverOffset = isHovered ? (int)(-20 * hoverProgress) : 0;
        int drawY = y + hoverOffset;

        // Draw card image or placeholder
        if (cardImages != null && cardIndex < cardImages.length && cardImages[cardIndex] != null) {
            g2d.drawImage(cardImages[cardIndex], x, drawY, cardWidth, cardHeight, null);
        } else {
            g2d.setColor(new Color(50, 50, 100));
            g2d.fillRoundRect(x, drawY, cardWidth, cardHeight, 15, 15);
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(x, drawY, cardWidth, cardHeight, 15, 15);
        }

        // Draw card cost with outline
        int cost = card.getCost();
        String costText = String.valueOf(cost);
        Font costFont = FontLoader.loadFont("/fonts/ByteBounce.ttf", 40f);
        g2d.setFont(costFont);

        int costX = x + 15;
        int costY = drawY + 35;

        // Black outline
        g2d.setColor(Color.BLACK);
        g2d.drawString(costText, costX-1, costY-1);
        g2d.drawString(costText, costX+1, costY-1);
        g2d.drawString(costText, costX-1, costY+1);
        g2d.drawString(costText, costX+1, costY+1);

        // White main text
        g2d.setColor(Color.WHITE);
        g2d.drawString(costText, costX, costY);
    }

    public BufferedImage[] getCardImages() {
        return cardImages;
    }

    // Check if mouse is over any active card
    public boolean isMouseOverCard(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        if (cardClickHandler == null) {
            cardClickHandler = game.getCardClickHandler();
        }

        if (cardImages == null || pileManager == null || cardClickHandler == null)
            return false;

        ArrayList<Integer> handIndices = pileManager.getHandIndices();
        CardInfo[] allCards = cardClickHandler.getCardInfos();

        // Count active cards
        int activeCount = 0;
        for (int idx : handIndices) {
            if (allCards[idx].isActive()) activeCount++;
        }

        int totalWidth = activeCount * WIDTH + (activeCount - 1) * SPACING;
        int startX = (screenWidth - totalWidth) / 2;
        int y = (cardY != null) ? (int) cardY[0] : screenHeight - HEIGHT - BOTTOM_OFFSET;

        int displayIndex = 0;
        for (int cardIdx : handIndices) {
            if (allCards[cardIdx].isActive()) {
                int x = startX + displayIndex * (WIDTH + SPACING);
                if (mouseX >= x && mouseX <= x + WIDTH &&
                    mouseY >= y && mouseY <= y + HEIGHT) {
                    return true;
                }
                displayIndex++;
            }
        }

        return false;
    }
}
