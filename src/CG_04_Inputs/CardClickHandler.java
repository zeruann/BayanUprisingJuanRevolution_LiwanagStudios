package CG_04_Inputs;

import javax.swing.JOptionPane;

import Aa_00_GlobalVariable.GameLevelAttempt;
import Aa_00_GlobalVariable.LevelConditional;

import static CG_03_UI.CardLayout.*;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import CG_01_Main.Game;
import CG_01_Main.GamePanel;
import CG_02_System.CardPileManager;
import CG_02_System.DialogueManager;
import CG_02_System.TurnManager;
import CG_03_UI.CardInfo;
import CG_03_UI.DamageText;
import CG_06_Entities.Enemy;
import CG_06_Entities.Player;
import CG_07_Effects.AttackEffect;

public class CardClickHandler {

	// ======================================================================
	// FIELDS
	// ======================================================================
	private GamePanel gamePanel;
	private Player player;
	private CardPileManager pileManager;
	
	private static final int PLAY_LINE_Y = 500;
	private static final long DIALOGUE_DURATION = 2500;
	
	private long playerDialogueStartTime = 0;
	private long enemyDialogueStartTime = 0;

	// ======================================================================
	// SET PLAYER DAMAGE BY LEVEL AND ATTEMPT
	// ======================================================================
    
	    LevelConditional levelCond = LevelConditional.GLOBAL;
	    GameLevelAttempt levelAttempt = GameLevelAttempt.GLOBAL;
	    public int damage, damageAddition;


	    
	    //CARD HOVER EFFECTS
		private CardInfo[] cardInfos = new CardInfo[] { 
				new CardInfo("", "Summons frost and deals heavy damage.", 1, damage, 0),
				new CardInfo("", "Summons lightning and shocks the enemy.", 2, damage, 1),
				new CardInfo("", "Unleashes sparks to damage the enemy.", 3, damage, 2),
				new CardInfo("", "Calls down thunder for massive damage.", 2, damage, 3),
				new CardInfo("", "Strikes with holy energy.", 4, damage, 4), 
				new CardInfo("", "Restores health.", 2, 0, 5),
				new CardInfo("", "Generates a temporary shield.", 2, 0, 6),
				new CardInfo("", "Executes a powerful final attack.", 4, damage, 7) 
		};
	
	
	

	// ======================================================================
	// CONSTRUCTOR
	// ======================================================================
		
	public CardClickHandler(GamePanel gamePanel) {

		
		this.gamePanel = gamePanel;
		this.pileManager = gamePanel.getGame().getPileManager();

		MouseListener[] listeners = gamePanel.getMouseListeners();
		for (MouseListener listener : listeners) {
			gamePanel.removeMouseListener(listener);
		}

		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!Game.getInstance().isCardsActive())
					return;

				CardPileManager pileManager = gamePanel.getGame().getPileManager();
				if (pileManager != null && pileManager.isDrawPileClicked(e.getX(), e.getY())) {

					if (pileManager.isHandFull()) {
						JOptionPane.showMessageDialog(gamePanel,
								"Hand is full!\nMaximum: " + CardPileManager.MAX_HAND_SIZE + " cards", "Hand Full",
								JOptionPane.WARNING_MESSAGE);
						return;
					}

					if (pileManager.tryDrawCard()) {
						gamePanel.getSoundEffectsPlayer().playSound("/soundfx/CardDraw.wav");
						gamePanel.repaint();
					} else {
						JOptionPane.showMessageDialog(gamePanel, "Cannot draw more cards!\nLimit: 2 cards per turn",
								"Draw Limit Reached", JOptionPane.WARNING_MESSAGE);
					}
					return;
				}

				int cardIndex = getClickedCardIndex(e.getX(), e.getY());
				if (cardIndex != -1) {
					handleCardClick(cardIndex);
					gamePanel.repaint();
				}
			}
		};

		gamePanel.addMouseListener(adapter);
	}

	// ======================================================================
	// SETTERS
	// ======================================================================
	public void setPileManager(CardPileManager manager) {
		this.pileManager = manager;
	}

	// ======================================================================
	// DIALOGUE DATA
	// ======================================================================
	public String getCardDialogueByIndex(int cardIndex) {
		java.util.Random r = new java.util.Random();

		String[][] dialogues = {
			{"Your lies end here!", "No more cover-ups!", "You can't hide forever!"},
			{"The truth strikes now!", "Your power is fading!", "Justice will find you!"},
			{"We're done being silent!", "We see everything now!", "This is for every lie you told!"},
			{"The people are awake!", "Your reign is over!", "You will answer for this!"},
			{"Justice is here!", "You will be judged!", "This is your reckoning!"},
			{"We stand back up!", "We won't give up!", "Still fighting!"},
			{"You won't break us!", "Together we stand!", "Not today!"},
			{"This ends now!", "No escape!", "Justice prevails!"}
		};

		return dialogues[cardIndex][r.nextInt(dialogues[cardIndex].length)];
	}

	public String getEnemyReactionByIndex(int cardIndex) {
		String[] reactions = {"Gahh!", "Argh!", "Tch!", "No!!", "Impossible!", "You heal?!", "Break already!", "NOOOOO!"};
		return reactions[cardIndex];
	}

	// ======================================================================
	// CARD POSITIONING
	// ======================================================================
	public int[][] getActiveCardPositions() {
		int cardWidth = WIDTH;
		int spacing = SPACING;

		ArrayList<Integer> handIndices = pileManager.getHandIndices();
		int activeCount = handIndices.size();

		int totalWidth = activeCount * cardWidth + Math.max(0, activeCount - 1) * spacing;
		int startX = (gamePanel.getWidth() - totalWidth) / 2;

		int[][] positions = new int[activeCount][2];
		for (int displayIndex = 0; displayIndex < activeCount; displayIndex++) {
			int cardIndex = handIndices.get(displayIndex);
			positions[displayIndex][0] = startX + displayIndex * (cardWidth + spacing);
			positions[displayIndex][1] = cardIndex;
		}
		return positions;
	}

	private int getCardX(int index) {
		int cardWidth = WIDTH;
		int spacing = SPACING;
		int totalWidth = cardInfos.length * cardWidth + (cardInfos.length - 1) * spacing;
		int startX = (gamePanel.getWidth() - totalWidth) / 2;
		return startX + index * (cardWidth + spacing);
	}

	private int getCardY() {
		return gamePanel.getHeight() - HEIGHT - BOTTOM_OFFSET;
	}

	// ======================================================================
	// CARD INTERACTION
	// ======================================================================
	private int getClickedCardIndex(int mouseX, int mouseY) {
		return getHoveredCardIndex(mouseX, mouseY);
	}

	public int getHoveredCardIndex(int mouseX, int mouseY) {
		int[][] positions = getActiveCardPositions();
		int cardWidth = WIDTH;
		int cardHeight = HEIGHT;
		int y = getCardY();

		for (int[] pos : positions) {
			int x = pos[0];
			int originalIndex = pos[1];
			if (mouseX >= x && mouseX <= x + cardWidth && mouseY >= y && mouseY <= y + cardHeight) {
				return originalIndex;
			}
		}
		return -1;
	}

	public void handleCardClick(int cardIndex) {
		Game game = gamePanel.getGame();

		if (game.getTurnManager().isBattleOver())
			return;

		if (!game.getTurnManager().getCurrentTurn().equals("PLAYER") || game.getTurnManager().hasPlayerAttacked())
			return;

		if (game.isSelectingCards() && game.getCardsSelected() >= 3)
			return;

		ArrayList<Integer> handIndices = pileManager.getHandIndices();
		if (!handIndices.contains(cardIndex)) {
			System.out.println("Card " + cardIndex + " not in hand!");
			return;
		}

		int cardCost = cardInfos[cardIndex].getCost();
		TurnManager turnManager = game.getTurnManager();

		if (!turnManager.hasPlayerEnoughCost(cardCost)) {
			JOptionPane.showMessageDialog(gamePanel,
					"Not enough cost!\nNeed: " + cardCost + "\nHave: " + turnManager.getPlayerCost(),
					"Insufficient Cost", JOptionPane.WARNING_MESSAGE);
			return;
		}

		cardInfos[cardIndex].setActive(false);
		pileManager.discardCard(cardIndex);

		playerDialogueStartTime = System.currentTimeMillis();
		turnManager.usePlayerCost(cardCost);

		Game gameRef = Game.getInstance();
		DialogueManager dm = gameRef.getDialogueManager();
		try {
			BufferedImage playerBubble = ImageIO.read(getClass().getResource("/ui/bubble_player.png"));
			dm.addDialogue("Juan", getCardDialogueByIndex(cardIndex), 330, 630, playerBubble, 120, 380);
			dm.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

		switch (cardIndex) {
		case 0, 1, 2, 3, 4 -> performAttack(game, cardIndex);
		case 5 -> performHeal(game, game.getPlayer());
		case 6 -> performShield(game, game.getPlayer());
		case 7 -> performSpecialAttack(game);
		}

		Enemy enemy = game.getEnemy();
		if (enemy.getCurrentHealth() > 0) {
			try {
				Thread.sleep(300);
				BufferedImage enemyBubble = ImageIO.read(getClass().getResource("/ui/bubble_enemy.png"));
				dm.addDialogue("Mang Gagantso", getEnemyReactionByIndex(cardIndex),  900, 560, enemyBubble,  690,300);
				enemyDialogueStartTime = System.currentTimeMillis();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (game.isSelectingCards()) {
			game.onCardSelected();
		}
		gamePanel.repaint();
	}

	// ======================================================================
	// CARD EFFECTS AND ATTACK DAMAGE || ATTACK DAMAGE || ATTACK DAMAGE
	// ======================================================================
	private void performAttack(Game game, int cardIndex) {
		Player player = game.getPlayer();
		Enemy enemy = game.getEnemy();
		player.setAttacking(true);

		
		String effectPath = null;
		float scale = 1f;
		int frames = 1;
		int[] xOffsets = new int[] { 0 };
		int yOffset = 0;
		
		setDamage(); // CALL DAMAGE FUNCTION
		
		
		switch (cardIndex) {
		case 0 -> {
			damage = 25 + damageAddition;
			float effectX = enemy.getX() + 128;
			float effectY = enemy.getY() + 128;

			String effectPath1 = "/a_attackeffects/1_Player_Frost1.png";
			game.addEffect(new AttackEffect(effectX, effectY, effectPath1, 14, 3f, -180, 20));

			new Thread(() -> {
				try {
					Thread.sleep(700);
					String effectPath2 = "/a_attackeffects/1_Player_Frost2.png";
					game.addEffect(new AttackEffect(effectX, effectY, effectPath2, 11, 4f, 0, 40));
					gamePanel.getSoundEffectsPlayer().playSound("/soundfx/07_AttackExplosion.wav");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();

			enemy.takeDamage(damage);
			enemy.playHurtAnimation();
			game.addDamageText(new DamageText(enemy.getX() + 64, enemy.getY() - 100, "-" + damage, Color.RED));
			return;
		}
		case 1 -> {
			damage = 30 + damageAddition; //30
			effectPath = "/a_attackeffects/1_Player_Lightning.png";
			scale = 5f;
			frames = 10;
			yOffset = -120;
			xOffsets = new int[] { -10, 0, 10 };
		}
		case 2 -> { 
			damage = 25 + damageAddition; //25
			effectPath = "/a_attackeffects/1_Player_spark.png";
			scale = 6f;
			frames = 7;
			yOffset = -15;
			xOffsets = new int[] { 10 };
		}
		case 3 -> {
			damage = 50 + damageAddition; //50
			effectPath = "/a_attackeffects/1_Player_Thunderstrike_wblur.png";
			scale = 7.5f;
			frames = 13;
			yOffset = -20;
			xOffsets = new int[] { 0 };
		}
		case 4 -> {
			damage = 80 + damageAddition; //80
			effectPath = "/a_attackeffects/1_Player_HolyVFX02.png";
			scale = 8.5f;
			frames = 16;
			yOffset = -20;
			xOffsets = new int[] { 0 };
		}
		default -> {
			damage = 20 + damageAddition;
			effectPath = "/a_attackeffects/Default.png";
			scale = 5f;
			frames = 10;
			yOffset = 0;
			xOffsets = new int[] { 0 };
			}
		}

		enemy.takeDamage(damage);
		enemy.playHurtAnimation();

		float effectX = enemy.getX() + 128;
		float effectY = enemy.getY() + 128;
		for (int xOffset : xOffsets) {
			game.addEffect(new AttackEffect(effectX, effectY, effectPath, frames, scale, xOffset, yOffset));
		}

		game.addDamageText(new DamageText(enemy.getX() + 64, enemy.getY() - 100, "-" + damage, Color.RED));
		gamePanel.getSoundEffectsPlayer().playSound("/soundfx/07_AttackExplosion.wav");
	}

	private void performHeal(Game game, Player player) {
		int heal = 30;
		player.heal(heal);
		game.addDamageText(new DamageText(player.getX() + 64, player.getY() - 100, "+" + heal, Color.GREEN));
		game.addEffect(new AttackEffect(player.getX() + 64, player.getY() + 64,
				"/a_healingeffects/1_Player_HEAL_Paladin Cross.png", 10, 2f, 40, 30));
	}

	private void performShield(Game game, Player player) {
		if (player.isShieldAppliedThisTurn())
			return;
			
		int shieldAmount = 50;
		player.addShield(shieldAmount);
		player.setShieldAppliedThisTurn(true);

		game.addDamageText(new DamageText(player.getX() + 64, player.getY() - 120, "Shield +" + shieldAmount, Color.CYAN));
		game.addEffect(new AttackEffect(player.getX() + 64, player.getY() + 64,
				"/a_shieldeffects/1_Player_SHIELD_Shield2.png", 20, 0.5f, 40, 30));
	}

	private void performSpecialAttack(Game game) {
		Player player = game.getPlayer();
		Enemy enemy = game.getEnemy();
		player.setAttacking(true);

		int damage = 300; //100
		enemy.takeDamage(damage);
		enemy.playHurtAnimation();

		float effectX = enemy.getX() + 128;
		float effectY = enemy.getY() + 128;
		game.addEffect(new AttackEffect(effectX, effectY, "/a_specialeffects/1_Player_SPECIAL_Paladin2.png", 12, 4f, 30, -30));
		game.addEffect(new AttackEffect(effectX, effectY, "/a_attackeffects/1_Player_Fire-bomb.png", 14, 11f, 30, -30));

		game.addDamageText(new DamageText(enemy.getX() + 64, enemy.getY() - 100, "-" + damage, Color.RED));
	}

	// ======================================================================
	// GETTERS
	// ======================================================================
	public CardInfo[] getCardInfos() {
		return cardInfos;
	}

	public long getPlayerDialogueEndTime() {
		return playerDialogueStartTime + DIALOGUE_DURATION;
	}
	
	// ======================================================================
	// SET DAMAGE
	// ======================================================================
	
	public void setDamage() {

		// ====================================================================================================================================================
		// SET ATTACKS BY ATTEMPT
		// ====================================================================================================================================================
		
		
			if(levelCond.getLevelState(1) == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=10;
	        	else damageAddition+=50;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
		     }

	        else if(levelCond.getLevelState(2)  == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=30;
	        	else damageAddition+=100;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
	        }
	        
	        else if(levelCond.getLevelState(3) == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=40;
	        	else damageAddition+=120;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
	        }
	        

	        else if(levelCond.getLevelState(4)  == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=50;
	        	else damageAddition+=130;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
	        }
	        

	        else if(levelCond.getLevelState(5)  == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=80;
	        	else damageAddition+=400;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
	        }
	}
}















