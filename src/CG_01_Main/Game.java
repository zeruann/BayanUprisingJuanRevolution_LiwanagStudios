package CG_01_Main;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Aa_00_GlobalVariable.GameResult_WinLose;
import Aa_00_GlobalVariable.LevelConditional;
import Aa_03_GameMap.MapPanel;
import Aa_06_PopupWindow.GameResultFrame;
import Aa_06_PopupWindow.GameResultPanel;
//import Bb_GameMap.MapPanel;
import CG_02_System.*;
import CG_03_UI.*;
import CG_04_Inputs.CardClickHandler;
import CG_05_Utilz.FontLoader;
import CG_06_Entities.*;
import CG_07_Effects.AttackEffect;

public class Game implements Runnable {

	// ---------------------------
	// GLOBAL VARIABLES
	// ---------------------------
	LevelConditional levelCond = LevelConditional.GLOBAL;
	GameResult_WinLose getGameResult = GameResult_WinLose.GLOBAL;
	private static Game instance;
	private boolean gameResultShown = false;

	public static Game getInstance() {
		return instance;
	}

	// ---------------------------
	// Game Loop Constants
	// ---------------------------
	private final int FPS_SET = 120;
	private final int UPS_SET = 200;

	// ---------------------------
	// Window & Panel
	// ---------------------------
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;

	// ---------------------------
	// Entities
	// ---------------------------
	private Player player;
	private Enemy enemy;

	// ---------------------------
	// Managers
	// ---------------------------
	private TurnManager turnManager;
	private DialogueManager dialogueManager;
	private BackgroundManager backgroundManager;
	private CardManager cardManager;
	private HUDRenderer hudRenderer;
	private DamageTextManager damageTextManager;
	private BattleUI battleUI;
	private CardPileManager pileManager;

	// ---------------------------
	// Fonts
	// ---------------------------
	private Font bytebounce;

	// ---------------------------
	// Input Handlers
	// ---------------------------
	private CardClickHandler cardClickHandler;

	// ---------------------------
	// Attack Effects
	// ---------------------------
	private final ArrayList<AttackEffect> activeEffects = new ArrayList<>();

	// ---------------------------
	// Card Selection Tutorial
	// ---------------------------
	private boolean selectingCards = false;
	private int cardsSelected = 0;
	private static final int MAX_SELECTION = 3;

	public void startCardSelectionTutorial() {
		selectingCards = true;
		cardsSelected = 0;
	}

	public void onCardSelected() {
		if (!selectingCards)
			return;

		cardsSelected++;
		if (cardsSelected >= MAX_SELECTION) {
			selectingCards = false;
		}
	}

	public boolean isSelectingCards() {
		return selectingCards;
	}

	public int getCardsSelected() {
		return cardsSelected;
	}

	// ---------------------------
	// Constructor
	// ---------------------------
	public Game() {
		instance = this;

		// Load font
		bytebounce = FontLoader.loadFont("/fonts/ByteBounce.ttf", 60f);

		// Initialize entities
		player = new Player(100, 450);
		enemy = new Enemy(900, 340);

		// SET INITIAL HP BASED ON STARTING LEVEL
		int startingLevel = getCurrentLevel();
		player.setHealthForLevel(startingLevel);
		enemy.setHealthForLevel();

		hudRenderer = new HUDRenderer(bytebounce, player, enemy);
		hudRenderer.setVisible(false);

		dialogueManager = new DialogueManager(bytebounce);
		damageTextManager = new DamageTextManager(bytebounce);

		// Set background based on level state
		String GameBackground = "/BG1.png";
		if (levelCond.getLevelState(1) == 1)
			GameBackground = "/BG1.png";
		else if (levelCond.getLevelState(2) == 1)
			GameBackground = "/GameMap/Map2.png";
		else if (levelCond.getLevelState(3) == 1) {
			GameBackground = "/GameMap/Map2.png";
		} else if (levelCond.getLevelState(4) == 1) {
			GameBackground = "/GameMap/Map2.png";
		} else if (levelCond.getLevelState(5) == 1) {
			GameBackground = "/BG1.png";
		}

		backgroundManager = new BackgroundManager(GameBackground);

		// Initialize panel and input handler
		gamePanel = new GamePanel(this);
		cardClickHandler = gamePanel.getCardClickHandler();

		// Initialize CardManager after cardClickHandler
		cardManager = new CardManager(this);

		// Preload dialogue bubbles
		try {

			BufferedImage playerBubble = ImageIO.read(getClass().getResource("/ui/bubble_player.png"));
			BufferedImage enemyBubble = ImageIO.read(getClass().getResource("/ui/bubble_enemy.png"));
//addDialogue(String name, String text, int charX, int charY, BufferedImage bubbleImage, int bubbleX, int bubbleY)

			dialogueManager.addDialogue("Juan", "I won't let you win!", 330, 630, playerBubble, 120, 380);
			dialogueManager.addDialogue("Mang Gagantso", "Ha! You don't stand a chance!", 900, 560, enemyBubble, 690,
					300);
		} catch (IOException e) {
			e.printStackTrace();
		}
		dialogueManager.start();

		// Initialize pile manager
		pileManager = new CardPileManager(cardClickHandler);
		cardClickHandler.setPileManager(pileManager);
		cardManager.setPileManager(pileManager);

		// Create window
		gameWindow = new GameWindow(gamePanel);
		gamePanel.requestFocus();

		// Initialize turn manager and battle UI
		turnManager = new TurnManager(player, enemy, this);
		battleUI = new BattleUI(bytebounce, turnManager, gamePanel);
		battleUI.setEndButtonYOffset(80);
		battleUI.setEndButtonXOffset(80);

		// Show battle start and card selection tutorial after dialogues
		new Thread(() -> {
			try {
				Thread.sleep(5000);
				battleUI.triggerBattleStart();
				Thread.sleep(2000);
				hudRenderer.setVisible(true);
				startCardSelectionTutorial();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		// Start game loop
		startGameLoop();
	}

	// ---------------------------
	// Getters
	// ---------------------------
	public HUDRenderer getHUDRenderer() {
		return hudRenderer;
	}

	public Player getPlayer() {
		return player;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public TurnManager getTurnManager() {
		return turnManager;
	}

	public DialogueManager getDialogueManager() {
		return dialogueManager;
	}

	public CardManager getCardManager() {
		return cardManager;
	}

	public BattleUI getBattleUI() {
		return battleUI;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public CardClickHandler getCardClickHandler() {
		return cardClickHandler;
	}

	public CardPileManager getPileManager() {
		return pileManager;
	}

	// ---------------------------
	// Game Loop
	// ---------------------------
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;

		long previousTime = System.nanoTime();
		double deltaU = 0, deltaF = 0;
		int frames = 0, updates = 0;
		long lastCheck = System.currentTimeMillis();

		while (true) {
			long currentTime = System.nanoTime();
			deltaU += (currentTime - previousTime) / timePerUpdate;
			deltaF += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;

			if (deltaU >= 1) {
				update();
				updates++;
				deltaU--;
			}

			if (deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF--;
			}

			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}

	// ---------------------------
	// Update Methods
	// ---------------------------
	public void update() {
		dialogueManager.update();
		if (!dialogueManager.isActive())
			battleUI.updateEndTurnButton();
		battleUI.updateBattleStart();
		cardManager.updateAnimation();

		if (battleUI.isShowingBattleStart() || cardManager.isAnimating())
			return;

		player.update();
		enemy.update();
		damageTextManager.update();
		turnManager.update();
		battleUI.update();
		updateEffects();
	}

	public int getHoverCardIndex() {
		return gamePanel.hoverCardIndex;
	}

	private void updateEffects() {
		Iterator<AttackEffect> it = activeEffects.iterator();
		while (it.hasNext()) {
			AttackEffect e = it.next();
			e.update();
			if (e.isFinished())
				it.remove();
		}
	}

	// ---------------------------
	// Render Methods
	// ---------------------------
	public void render(Graphics g) {
		backgroundManager.draw(g);
		player.render(g);
		enemy.render(g);

		Graphics2D g2d = (Graphics2D) g;

		if (battleUI.isShowingBattleStart()) {
			battleUI.drawBattleStart(g2d, gamePanel.getWidth(), gamePanel.getHeight());
			return;
		}

		hudRenderer.draw(g2d);
		renderEffects(g2d);
		damageTextManager.draw(g2d);

		if (dialogueManager != null && dialogueManager.isActive()) {
			dialogueManager.render(g2d);
		} else {
			// ONLY SHOW CARDS DURING PLAYER'S TURN AND WHEN BATTLE IS NOT OVER
			if (turnManager.getCurrentTurn().equals("PLAYER") && !turnManager.isBattleOver()) {
				cardManager.draw(g2d, gamePanel.getWidth(), gamePanel.getHeight());
			}

			if (!turnManager.isBattleOver()) {
				battleUI.drawTurnIndicator(g2d, gamePanel.getWidth(), gamePanel.getHeight());
			}

			else {

				battleUI.drawBattleResult(g2d, gamePanel.getWidth(), gamePanel.getHeight());

//				if (levelCond.getLevelState(1) == 1) {
//					levelCond.setLevelState(1, 2); // level1 finished
//					levelCond.setLevelState(2, 1); // unlock level2
//					System.out.println("Level 1: VICTORY!");
//				}
//
//				else if (levelCond.getLevelState(2) == 1) {
//					levelCond.setLevelState(2, 2); // level1 finished
//					levelCond.setLevelState(3, 1); // unlock level2
//					System.out.println("Level 2: VICTORY! " + levelCond.getLevelState(2));
//				}
//
//				else if (levelCond.getLevelState(3) == 1) {
//					levelCond.setLevelState(3, 2); // level1 finished
//					levelCond.setLevelState(4, 1); // unlock level2
//					System.out.println("Level 3: VICTORY!" + levelCond.getLevelState(3));
//				}
//
//				else if (levelCond.getLevelState(4) == 1) {
//					levelCond.setLevelState(4, 2); // level1 finished
//					levelCond.setLevelState(5, 1); // unlock level2
//					System.out.println("Level 4: VICTORY!" + levelCond.getLevelState(4));
//				}
//
//				else if (levelCond.getLevelState(5) == 1) {
//					levelCond.setLevelState(5, 2); // level1 finished
//					levelCond.setLevelState(5, 2); // unlock level2
//					System.out.println("Level 5: VICTORY!" + levelCond.getLevelState(5));
//				}

//	            if (!gameResultShown) {  // Only create window once   	
//	                GameResultPanel gameResult = new GameResultPanel(null, null);
//	                new GameResultFrame(gameResult);
//	                gameResultShown = true;
//	            }
			}
		}

		// BATTLE CHECK
		if (isSelectingCards() && getTurnManager().getCurrentTurn().equals("PLAYER") && !isDialogueActive()
				&& !turnManager.isBattleOver()) { // Don't show during victory/defeat
			drawCardSelectionText(g2d);
		}
	}

	private void drawCardSelectionText(Graphics2D g2d) {
		String text = switch (cardsSelected) {
		case 0 -> "SELECT 1ST CARD";
		case 1 -> "SELECT 2ND CARD";
		case 2 -> "SELECT 3RD CARD";
		default -> "";
		};

		Font font = bytebounce.deriveFont(50f);
		g2d.setFont(font);

		FontMetrics fm = g2d.getFontMetrics();
		int x = (gamePanel.getWidth() - fm.stringWidth(text)) / 2;
		int y = gamePanel.getHeight() / 2;

		GlyphVector gv = font.createGlyphVector(g2d.getFontRenderContext(), text);
		Shape textShape = gv.getOutline(x, y);

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setStroke(new BasicStroke(4f));
		g2d.setColor(Color.BLACK);
		g2d.draw(textShape);

		g2d.setColor(Color.WHITE);
		g2d.fill(textShape);
	}

	private void renderEffects(Graphics g) {
		for (AttackEffect e : new ArrayList<>(activeEffects))
			e.render(g);
	}

	public void addEffect(AttackEffect effect) {
		activeEffects.add(effect);
	}

	public void addDamageText(DamageText text) {
		damageTextManager.addDamageText(text);
	}

	public void windowFocusLost() {
		player.resetDirectionBooleans();
	}

	// ---------------------------
	// Utility / Status Methods
	// ---------------------------
	public boolean isDialogueActive() {
		return dialogueManager.isDialogueRunning();
	}

	public boolean isCardsActive() {
		return !(dialogueManager != null && dialogueManager.isActive()) && !battleUI.isShowingBattleStart()
				&& !cardManager.isAnimating();
	}

	public void resetCardSelectionPrompt() {
		selectingCards = true;
		cardsSelected = 0;
	}

	// ---------------------------
	// START NEXT LEVEL
	// ---------------------------
	public void startNextLevel() {
		// Determine current level and next level
		int currentLevel = getCurrentLevel();
		int nextLevel = currentLevel + 1;

		if (nextLevel > 5) {
			System.out.println("All levels completed!");
			return;
		}

		// Update level states
		 //levelCond.setLevelState(currentLevel, 2); // Mark current as complete
		 //levelCond.setLevelState(nextLevel, 1); // Mark next as active

		// Update background for new level
		String newBackground = getBackgroundForLevel(nextLevel);
		backgroundManager = new BackgroundManager(newBackground);

		// *** SET HP FOR NEW LEVEL ***
		player.setHealthForLevel(nextLevel);
		enemy.setHealthForLevel();

		// *** REFRESH ENEMY PROFILE FOR NEW LEVEL ***
		hudRenderer.refreshEnemyProfile();

		// Restart battle
		restartBattle();

		System.out.println("Started Level " + nextLevel);
	}

	// Helper method to get current active level
	private int getCurrentLevel() {
		for (int i = 1; i <= 5; i++) {
			if (levelCond.getLevelState(i) == 1) {
				return i;
			}
		}
		return 1; // Default to level 1
	}

	// Helper method to get background for a specific level
	private String getBackgroundForLevel(int level) {
		return switch (level) {
		case 1 -> "/BG1.png";
		case 2 -> "/GameMap/Map2.png";
		case 3 -> "/GameMap/Map3.png"; // Add your level 3 background
		case 4 -> "/GameMap/Map4.png"; // Add your level 4 background
		case 5 -> "/GameMap/Map5.png"; // Add your level 5 background
		default -> "/BG1.png";
		};
	}

	// ---------------------------
	// Restart Battle (Same Level)
	// ---------------------------
	public void restartBattle() {

		player.setX(100);
		player.setY(450);
		player.resetHealth();
		enemy.resetForBattle();

		pileManager = new CardPileManager(cardClickHandler);
		cardClickHandler.setPileManager(pileManager);
		cardManager.setPileManager(pileManager);

		turnManager = new TurnManager(player, enemy, this);

		// *** DON'T CREATE NEW HUD RENDERER - KEEP EXISTING ONE ***
		// This preserves the enemy profile that was set
		hudRenderer.setVisible(true);
		startCardSelectionTutorial();

		battleUI.reset(turnManager);
		battleUI.setBattleHasStarted(true);

		damageTextManager = new DamageTextManager(bytebounce);
		activeEffects.clear();

		System.out.println("Battle restarted!");
	}
}