package CG_03_UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Aa_00_GlobalVariable.GameResult_WinLose;
import Aa_00_GlobalVariable.LevelConditional;
import Aa_06_PopupWindow.GameResultFrame;
import Aa_06_PopupWindow.GameResultPanel;
import CG_01_Main.GameWindow;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import CG_02_System.TurnManager;

public class BattleUI {

	GameResult_WinLose getGameResult = GameResult_WinLose.GLOBAL;
	// Font used for text rendering
	private Font font;

	
	LevelConditional levelCond = LevelConditional.GLOBAL;
	// Reference to the game turn manager
	private TurnManager turnManager;

	// Images for UI elements
	private Image youWinImage;
	private Image gameOverImage;
	private Image yourTurnImage;
	private Image enemyTurnImage;
	private Image battleStartImage;
	private Image endTurnImage;

	// Battle start display control
	private boolean showBattleStart = false;
	private long battleStartTime;
	private final long BATTLE_START_DURATION = 2000; // 2 seconds

	private boolean showRetryOption = false;
	private boolean battleHasStarted = false;

	// End Turn button and panel
	private JButton endTurnButton;
	private final javax.swing.JPanel gamePanel;

	// Vertical positioning options for End Turn button
	public enum VerticalAnchor {
		BOTTOM, TOP, CENTER, ABSOLUTE
	}

	private VerticalAnchor verticalAnchor = VerticalAnchor.BOTTOM;
	private int endButtonBottomOffset = 150;
	private int endButtonTopOffset = 150;
	private Integer endButtonAbsoluteY = null;

	// Horizontal offset from right edge
	private int endButtonRightOffset = 50;

	// Constructor initializes images and end turn button
	public BattleUI(Font font, TurnManager turnManager, javax.swing.JPanel gamePanel) {
		this.font = font;
		this.turnManager = turnManager;
		this.gamePanel = gamePanel;

		loadResultImages(); // Load UI images
		initEndTurnButton(gamePanel); // Initialize End Turn button
	}

	// Cleanup: remove button from panel
	public void cleanup() {
		if (endTurnButton != null && gamePanel != null) {
			gamePanel.remove(endTurnButton);
			endTurnButton = null;
		}
	}

	// Reset UI for a new battle
	public void reset(TurnManager newTurnManager) {
		this.turnManager = newTurnManager;
		showBattleStart = false;
		showRetryOption = false;
		battleHasStarted = false;
		if (endTurnButton != null) {
			endTurnButton.setVisible(false);
		}
	}

	// Load images from resources
	private void loadResultImages() {
		try {
			youWinImage = ImageIO.read(getClass().getResourceAsStream("/ui/VICTORY.png"));
			gameOverImage = ImageIO.read(getClass().getResourceAsStream("/ui/DEFEAT.png"));
			yourTurnImage = ImageIO.read(getClass().getResourceAsStream("/ui/YourTurn.png"));
			enemyTurnImage = ImageIO.read(getClass().getResourceAsStream("/ui/EnemyTurn.png"));
			battleStartImage = ImageIO.read(getClass().getResourceAsStream("/ui/BattleStart.png"));
			endTurnImage = ImageIO.read(getClass().getResourceAsStream("/ui/EndTurn1.png"));
		} catch (Exception e) {
			System.out.println("Could not load result images, will use text instead.");
			e.printStackTrace();
		}
	}

	// Initialize the End Turn button
	private void initEndTurnButton(javax.swing.JPanel gamePanel) {
		endTurnButton = new JButton();
		endTurnButton.setContentAreaFilled(false);
		endTurnButton.setBorderPainted(false);
		endTurnButton.setFocusPainted(false);
		endTurnButton.setOpaque(false);
		endTurnButton.setBorder(null);

		int buttonWidth = 140;
		int buttonHeight = 55;

		if (endTurnImage != null) {
			Image scaledImage = endTurnImage.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
			endTurnButton.setIcon(new javax.swing.ImageIcon(scaledImage));
			endTurnButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		} else {
			endTurnButton.setText("END TURN");
			endTurnButton.setFont(font.deriveFont(Font.BOLD, 18f));
			endTurnButton.setForeground(Color.WHITE);
			endTurnButton.setBackground(new Color(200, 50, 50));
			endTurnButton.setOpaque(true);
			endTurnButton.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
		}

		// Button click ends player turn
		endTurnButton.addActionListener(e -> turnManager.endPlayerTurn());

		gamePanel.setLayout(null);
		gamePanel.add(endTurnButton);
		endTurnButton.setVisible(false);

		// Reposition button on resize/show
		gamePanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (endTurnButton.isVisible()) {
					positionEndTurnButtonCentered();
				}
			}

			@Override
			public void componentShown(ComponentEvent e) {
				positionEndTurnButtonCentered();
			}
		});
	}

	// Position End Turn button according to anchor and offsets
	private void positionEndTurnButtonCentered() {
		int panelWidth = Math.max(1, gamePanel.getWidth());
		int panelHeight = Math.max(1, gamePanel.getHeight());

		Dimension pref = endTurnButton.getPreferredSize();
		int bw = pref.width;
		int bh = pref.height;

		int bx = panelWidth - bw - endButtonRightOffset;
		int by;

		switch (verticalAnchor) {
		case TOP:
			by = endButtonTopOffset;
			break;
		case CENTER:
			by = (panelHeight - bh) / 2;
			break;
		case ABSOLUTE:
			by = (endButtonAbsoluteY != null) ? endButtonAbsoluteY : panelHeight - bh - endButtonBottomOffset;
			break;
		case BOTTOM:
		default:
			by = panelHeight - bh - endButtonBottomOffset;
			break;
		}

		endTurnButton.setBounds(bx, by, bw, bh);
	}

	// Setter methods for End Turn button positioning
	public void setEndButtonYOffset(int pixels) {
		this.endButtonBottomOffset = Math.max(0, pixels);
		if (endTurnButton.isVisible())
			positionEndTurnButtonCentered();
	}

	public void setEndButtonXOffset(int pixels) {
		this.endButtonRightOffset = Math.max(0, pixels);
		if (endTurnButton.isVisible())
			positionEndTurnButtonCentered();
	}

	public void setEndButtonBottomOffset(int px) {
		this.endButtonBottomOffset = Math.max(0, px);
		if (endTurnButton.isVisible())
			positionEndTurnButtonCentered();
	}

	public void setEndButtonTopOffset(int px) {
		this.endButtonTopOffset = Math.max(0, px);
		if (endTurnButton.isVisible())
			positionEndTurnButtonCentered();
	}

	public void setEndButtonAbsoluteY(Integer absoluteY) {
		if (absoluteY != null && absoluteY < 0)
			absoluteY = 0;
		this.endButtonAbsoluteY = absoluteY;
		if (endTurnButton.isVisible())
			positionEndTurnButtonCentered();
	}

	public void setEndButtonVerticalAnchor(VerticalAnchor anchor) {
		if (anchor == null)
			anchor = VerticalAnchor.BOTTOM;
		this.verticalAnchor = anchor;
		if (endTurnButton.isVisible())
			positionEndTurnButtonCentered();
	}

	// Update UI state
	public void update(int screenWidth, int screenHeight) {
		updateBattleStart();
		updateEndTurnButton();
	}

	public void update() {
		updateBattleStart();
		updateEndTurnButton();
	}

	// Show or hide End Turn button based on turn and dialogue state
	public void updateEndTurnButton() {
		if (!battleHasStarted) {
			endTurnButton.setVisible(false);
			return;
		}

		boolean dialogueActive = turnManager.getDialogueManager() != null
				&& turnManager.getDialogueManager().isActive();

		if (dialogueActive) {
			endTurnButton.setVisible(false);
			return;
		}

		// CHECKS IF IT'S PLAYER'S TURN
		boolean show = "PLAYER".equals(turnManager.getCurrentTurn()) && !turnManager.hasPlayerAttacked();

		if (show) {
			positionEndTurnButtonCentered();
			if (!endTurnButton.isVisible())
				endTurnButton.setVisible(true);
			positionEndTurnButtonCentered();
		} else {
			if (endTurnButton.isVisible())
				endTurnButton.setVisible(false);
		}
	}

	// Start Battle Start display
	public void triggerBattleStart() {
		showBattleStart = true;
		battleStartTime = System.currentTimeMillis();
	}

	public boolean isShowingBattleStart() {
		return showBattleStart;
	}

	// Update Battle Start timing and transition to player turn
	public void updateBattleStart() {
		if (!showBattleStart)
			return;

		long elapsed = System.currentTimeMillis() - battleStartTime;
		if (elapsed >= BATTLE_START_DURATION) {
			showBattleStart = false;
			battleHasStarted = true;
			turnManager.startPlayerTurn();
		}
	}

	// Draw Battle Start image or fallback text
	public void drawBattleStart(Graphics2D g2d, int screenWidth, int screenHeight) {
		if (!showBattleStart)
			return;

		if (battleStartImage != null) {
			int imgWidth = 600;
			int imgHeight = 300;
			int x = (screenWidth - imgWidth) / 2;
			int y = (screenHeight - imgHeight) / 2 - 50;
			g2d.drawImage(battleStartImage, x, y, imgWidth, imgHeight, null);
		} else {
			
			g2d.setFont(font.deriveFont(80f));
			String text = "BATTLE START!";
			int textWidth = g2d.getFontMetrics().stringWidth(text);
			int x = (screenWidth - textWidth) / 2;
			int y = screenHeight / 2;
			TextRenderer.drawOutlinedText(g2d, text, x, y, Color.WHITE, Color.BLACK, 6);
		}
	}

	// Draw turn indicator
	public void drawTurnIndicator(Graphics g, int screenWidth, int screenHeight) {
		Graphics2D g2d = (Graphics2D) g;

		boolean isPlayerTurn = "PLAYER".equals(turnManager.getCurrentTurn());
		Image turnImage = isPlayerTurn ? yourTurnImage : enemyTurnImage;

		int imgWidth = 450;
		int imgHeight = 220;
		int y = 100;

		int x = isPlayerTurn ? -120 : screenWidth - imgWidth + 120;

		if (turnImage != null) {
			g2d.drawImage(turnImage, x, y, imgWidth, imgHeight, null);
		} else {
			g2d.setFont(font.deriveFont(50f));
			g2d.setColor(Color.WHITE);
			String text = isPlayerTurn ? "YOUR TURN" : "ENEMY TURN";
			int textWidth = g2d.getFontMetrics().stringWidth(text);
			x = isPlayerTurn ? 40 : screenWidth - textWidth - 40;
			TextRenderer.drawOutlinedText(g2d, text, x, y + 80, Color.WHITE, Color.BLACK, 4);
		}
	}

	// Draw battle result overlay
	public void drawBattleResult(Graphics g, int screenWidth, int screenHeight) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(new Color(0, 0, 0, 180));
		g2d.fillRect(0, 0, screenWidth, screenHeight);

		boolean playerWon = turnManager.getWinner().equals("PLAYER");

		if (playerWon) {
				g2d.setFont(font.deriveFont(100f));
				String message = "YOU WIN!";
				
				String enterText = "Press Enter to Continue";
				int retryX = (screenWidth - g2d.getFontMetrics().stringWidth(enterText)) / 2;
				int retryY = screenHeight / 2 + 150;
				TextRenderer.drawOutlinedText(g2d, enterText, retryX, retryY, Color.WHITE, Color.BLACK, 4);
				
				int textWidth = g2d.getFontMetrics().stringWidth(message);
				int x = (screenWidth - textWidth) / 2;
				int y = screenHeight / 2;
				TextRenderer.drawOutlinedText(g2d, message, x, y, Color.YELLOW, Color.BLACK, 6);
			
		} else {

				g2d.setFont(font.deriveFont(100f));
				String message = "GAME OVER";
				int textWidth = g2d.getFontMetrics().stringWidth(message);
				int x = (screenWidth - textWidth) / 2;
				int y = screenHeight / 2;
				TextRenderer.drawOutlinedText(g2d, message, x, y, Color.RED, Color.BLACK, 6);
				
				g2d.setFont(font.deriveFont(50f));
				String retryText = "Press R to Retry"; // Draw retry
				int retryX = (screenWidth - g2d.getFontMetrics().stringWidth(retryText)) / 2;
				int retryY = screenHeight / 2 + 150;
				TextRenderer.drawOutlinedText(g2d, retryText, retryX, retryY, Color.WHITE, Color.BLACK, 4);

				showRetryOption = true;
		}

		
	}

	// Set font for UI and button
	public void setFont(Font font) {
		this.font = font;
		endTurnButton.setFont(font.deriveFont(24f));
		positionEndTurnButtonCentered();
	}

	public boolean isShowingRetry() {
		return showRetryOption;
	}

	public void setShowRetryOption(boolean show) {
		this.showRetryOption = show;
	}

	public void setBattleHasStarted(boolean started) {
		this.battleHasStarted = started;
	}
}
