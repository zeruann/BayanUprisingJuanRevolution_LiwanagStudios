package CG_02_System;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import Aa_00_GlobalVariable.LevelConditional;
import CG_01_Main.Game;
import CG_03_UI.DamageText;
import CG_04_Inputs.CardClickHandler;
import CG_06_Entities.Enemy;
import CG_06_Entities.Player;
import CG_07_Effects.AttackEffect;

import Aa_00_GlobalVariable.GameInfo_Condition;
import Aa_00_GlobalVariable.GameLevelAttempt;
import Aa_00_GlobalVariable.GameResult_WinLose;
import Aa_06_PopupWindow.GameResultFrame;
import Aa_06_PopupWindow.GameResultPanel;



public class TurnManager {

	
	// =========================================================
	// REFERENCES TO GAME ENTITIES
	// =========================================================
	private Player player; // Player instance
	private Enemy enemy; // Enemy instance
	private Game game; // Main game instance
	private CardClickHandler cardClickHandler; // Handles player card clicks
	private CardPileManager pileManager; // Reference to card pile manager

	
    //GLOBAL VARIABLE: GET ATTEMPT TO ADJUST DAMAGE
    GameLevelAttempt levelAttempt = GameLevelAttempt.GLOBAL;
    public int damage, damageAddition ;
	
    
	 
	// =========================================================
	// GAME STATE VARIABLES
	// =========================================================
	private boolean battleOver = false; // Is the battle finished?
	private String winner = ""; // Stores winner ("PLAYER"/"ENEMY")
	private boolean roundOver = false; // Flag to prevent duplicate end-of-round events
	private boolean enemySkipNextTurn = false; // Skip enemy next turn

	// =========================================================
	// TURN MANAGEMENT
	// =========================================================
	private Queue<String> turnQueue; // Queue storing turn order
	private String currentTurn; // Who's turn currently

	private boolean playerAttacked = false; // Player finished their turn
	private boolean enemyAttacked = false; // Enemy finished their turn
	private boolean waitingForNextTurn = false; // Waiting delay before next turn
	private long nextTurnTime = 0; // Timestamp for next turn
	private final long TURN_DELAY = 1000; // 1 second delay between turns
	private boolean waitingForPlayer = false; // Waiting for player input

	// =========================================================
	// PLAYER COST SYSTEM
	// =========================================================
	private int playerCost;
	public int MAX_PLAYER_COST = 4; // default cost

	// =========================================================
	// ENEMY TURN LOGIC
	// =========================================================
	public enum EnemyAction {
		ATTACK_1, ATTACK_2, HEAL, SHIELD
	}

	private int currentActionIndex = 0; // Index of current enemy action
	private int totalActions = 0; // Total enemy actions for this turn
	private EnemyAction[] plannedActions; // Enemy planned actions
	private boolean executingActions = false; // Is enemy executing actions now?
	private long nextActionTime = 0; // Timestamp for next enemy action
	private final long ACTION_DELAY = 1500; // 1.5 sec between enemy actions

	// =========================================================
	// DIALOGUE TIMING
	// =========================================================
	private long playerDialogueEndTime = 0; // When player's dialogue finishes
	private static final long POST_DIALOGUE_DELAY = 800; // Wait 800ms after dialogue

	// =========================================================
	// LEVEL PROGRESS
	// =========================================================
	LevelConditional levelCond = LevelConditional.GLOBAL;

	// =========================================================
	// CONSTRUCTOR
	// =========================================================
	public TurnManager(Player player, Enemy enemy, Game game) {
		this.player = player;
		this.enemy = enemy;
		this.game = game;
		this.cardClickHandler = game.getCardClickHandler();

		resetPlayerCost(); // Set player cost to max

		// Setup initial turn queue
		turnQueue = new LinkedList<>();
		turnQueue.add("PLAYER");
		turnQueue.add("ENEMY");
		currentTurn = turnQueue.peek(); // Player starts first
	}

	// =========================================================
	// PLAYER COST METHODS
	// =========================================================
	public boolean hasPlayerEnoughCost(int cost) {
		return playerCost >= cost;
	}

	public void usePlayerCost(int cost) {
		
		
		
		if(levelCond.getLevelState(1) == 1) {
			MAX_PLAYER_COST = 4;
        	System.out.println("Total Cost:" + MAX_PLAYER_COST);
	     }

        else if(levelCond.getLevelState(2)  == 1) {
			MAX_PLAYER_COST = 6;
        	System.out.println("Total Cost:" + MAX_PLAYER_COST);
        }
        
        else if(levelCond.getLevelState(3) == 1) {
			MAX_PLAYER_COST = 8;
        	System.out.println("Total Cost:" + MAX_PLAYER_COST);
        }
        

        else if(levelCond.getLevelState(4)  == 1) {
			MAX_PLAYER_COST = 9;
        	System.out.println("Total Cost:" + MAX_PLAYER_COST);
        }
        

        else if(levelCond.getLevelState(5)  == 1) {
			MAX_PLAYER_COST = 10;
        	System.out.println("Total Cost:" + MAX_PLAYER_COST);
        }
		
		
		
		if (hasPlayerEnoughCost(cost)) {
			playerCost -= cost; // Deduct cost
			System.out.println("Player used " + cost + " cost. Remaining: " + playerCost);

			if (playerCost <= 0) { // Auto-end turn if no cost
				if (cardClickHandler != null) {
					playerDialogueEndTime = cardClickHandler.getPlayerDialogueEndTime();
				}
				endPlayerTurn();
			}
		} else {
			System.out.println("Not enough cost to play this card!");
		}
	}



	private void resetPlayerCost() {
		playerCost = MAX_PLAYER_COST;
		System.out.println("Player cost reset to " + playerCost);
	}

	//==========================================================
	// ENEMY DIALOGUES
	//==========================================================
	
	// Enemy turn states
	private boolean enemyDialogueActive = false;    // True while enemy dialogue is showing
	private boolean enemyActionPending = false;     // True when actions should start after dialogue

	
	private String getEnemyAttackDialogue() {
	    String[] dialogues = {
	        "Take this!", "Feel my wrath!", "You can't escape!", "This will end you!", "Hah! Weakling!"
	    };
	    return dialogues[new Random().nextInt(dialogues.length)];
	}

	private String getPlayerReactionDialogue() {
	    String[] reactions = {
	        "Ouch!", "Not fair!", "I'll get you next turn!", "Ugh!", "I won't let this stop me!"
	    };
	    return reactions[new Random().nextInt(reactions.length)];
	}
	private void showEnemyReactionDialogue() {
	    DialogueManager dm = game.getDialogueManager();
	    try {
	        java.io.InputStream stream = getClass().getResourceAsStream("/ui/bubble_enemy.png");
	        if (stream != null) {
	            BufferedImage enemyBubble = javax.imageio.ImageIO.read(stream);
	            dm.addDialogue("Mang Gagantso", getEnemyAttackDialogue(), 900, 560,
	                           enemyBubble, 690, 300);
	            dm.start();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	
	// =========================================================
	// GETTERS
	// =========================================================
	public DialogueManager getDialogueManager() {
		return game.getDialogueManager();
	}

	public String getCurrentTurn() {
		return currentTurn;
	}

	public boolean isBattleOver() {
		return battleOver;
	}
	public int getPlayerCost() {
		return playerCost;
	}
	public void setPlayerAttacked(boolean attacked) {
		this.playerAttacked = attacked;
	}
	
	public String getWinner() {
		return winner;
	}

	public boolean hasPlayerAttacked() {
		return playerAttacked;
	}



	public boolean isWaitingForPlayer() {
		return waitingForPlayer;
	}

	public void setWaitingForPlayer(boolean waiting) {
		this.waitingForPlayer = waiting;
	}

	public boolean isWaitingForNextTurn() {
		return waitingForNextTurn;
	}

	public void skipEnemyNextTurn() {
		enemySkipNextTurn = true;
	}

	public CardPileManager getPileManager() {
		return pileManager;
	}

	// =========================================================
	// TURN DELAYS
	// =========================================================
	public void startNextTurnWithDelay() {
		startNextTurnWithDelay(TURN_DELAY);
	}

	public void startNextTurnWithDelay(long delayMillis) {
		waitingForNextTurn = true;
		nextTurnTime = System.currentTimeMillis() + delayMillis;
	}

	// =========================================================
	// MAIN UPDATE LOOP
	// =========================================================
	public void update() {
		if (battleOver)
			return; // Stop if battle finished

		// Check defeat conditions
		if (player.isDead()) {
			handlePlayerDefeat();

			return;
		}
		if (enemy.getCurrentHealth() <= 0) {
			handleEnemyDefeat();
			return;
		}

		// Handle delay before next turn
		if (waitingForNextTurn) {
			if (System.currentTimeMillis() >= nextTurnTime) {
				nextTurn();
				waitingForNextTurn = false;
			} else {
				return;
			}
		}

		if (currentTurn.equals("ENEMY") && !enemyAttacked) {

		    DialogueManager dm = game.getDialogueManager();

		    // Wait until player's dialogue finishes
		    if (dm.isActive()) return;

		    // Show enemy reaction dialogue (once per turn)
		    if (!enemyDialogueActive) {
		        showEnemyReactionDialogue();
		        enemyDialogueActive = true;
		        return; // Wait one frame to let dialogue render
		    }

		    //  Wait until enemy dialogue finishes
		    if (dm.isActive()) return;

		    //  Start enemy actions if not already started
		    if (!enemyActionPending) {
		        if (enemySkipNextTurn) {
		            System.out.println("Enemy skips turn!");
		            enemySkipNextTurn = false;
		            enemyAttacked = true;
		            enemyDialogueActive = false;
		            startNextTurnWithDelay();
		            return;
		        }
		        handleEnemyTurn();   // Plan enemy actions
		        enemyActionPending = true;
		        return; // Wait for next update to perform actions
		    }

		    // 5️⃣ Update enemy actions (attacks, heal, shield)
		    updateEnemyActions();

		    // 6️⃣ Check if actions finished
		    if (!executingActions) {
		        enemyAttacked = true;
		        enemyDialogueActive = false;
		        enemyActionPending = false;
		        startNextTurnWithDelay();
		    }
		}

		// Wait for player input
		else if (currentTurn.equals("PLAYER") && !playerAttacked) {
			waitingForPlayer = true;
		}
	}

	// =========================================================
	// PLAYER & ENEMY DEFEAT HANDLERS
	// =========================================================
	private void handlePlayerDefeat() {
		if (!roundOver) {
			roundOver = true;
			showPlayerDefeatDialogue();

			new Thread(() -> {
				try {
					Thread.sleep(2500);
					battleOver = true;
					winner = "ENEMY";
					System.out.println("Player is defeated! GAME OVER.");
					game.getGamePanel().getSoundEffectsPlayer().stopBackgroundMusic();
					game.getGamePanel().getSoundEffectsPlayer().playSound("/soundfx/06_GameOver-1.wav");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

	private void handleEnemyDefeat() {
		if (!roundOver) {
			roundOver = true;
			showEnemyDefeatDialogue();

			new Thread(() -> {
				try {
					Thread.sleep(2500);
					battleOver = true;
					winner = "PLAYER";
					System.out.println("Enemy defeated! YOU WIN!");
					game.getGamePanel().getSoundEffectsPlayer().stopBackgroundMusic();
					game.getGamePanel().getSoundEffectsPlayer().playSound("/soundfx/06_GameVictory.wav");

					//UPDATE WINNER HERE HERE
					//gameResult.setGameResult(1);		
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

	private void showPlayerDefeatDialogue() {
		DialogueManager dm = game.getDialogueManager();
		try {
			java.io.InputStream stream = getClass().getResourceAsStream("/ui/bubble_player.png");
			if (stream != null) {
				java.awt.image.BufferedImage playerBubble = javax.imageio.ImageIO.read(stream);
				dm.addDialogue("Juan", "I failed...", 330, 630, playerBubble, 120, 380);
				dm.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showEnemyDefeatDialogue() {
		DialogueManager dm = game.getDialogueManager();
		try {
			java.io.InputStream stream = getClass().getResourceAsStream("/ui/bubble_enemy.png");
			if (stream != null) {
				java.awt.image.BufferedImage enemyBubble = javax.imageio.ImageIO.read(stream);
				dm.addDialogue("Mang Gagantso", "NO!!!", 890, 630, enemyBubble, 680, 360);
				dm.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// =========================================================
	// ENEMY TURN LOGIC
	// =========================================================
	private void handleEnemyTurn() {
		if (executingActions)
			return;

		Random rand = new Random();
		float hpPercent = (float) enemy.getCurrentHealth() / enemy.getMaxHealth();

		// Aggressive if healthy, defensive if low HP
		if (hpPercent >= 0.5f) {
			int strategy = rand.nextInt(2);
			totalActions = 3;
			plannedActions = new EnemyAction[totalActions];

			if (strategy == 0) { // 2 attacks + shield
				plannedActions[0] = rand.nextBoolean() ? EnemyAction.ATTACK_1 : EnemyAction.ATTACK_2;
				plannedActions[1] = rand.nextBoolean() ? EnemyAction.ATTACK_1 : EnemyAction.ATTACK_2;
				plannedActions[2] = EnemyAction.SHIELD;
				System.out.println("Enemy strategy: 2 Attacks + Shield");
			} else { // 3 attacks
				for (int i = 0; i < 3; i++)
					plannedActions[i] = rand.nextBoolean() ? EnemyAction.ATTACK_1 : EnemyAction.ATTACK_2;
				System.out.println("Enemy strategy: 3 Attacks");
			}
		} else {
			totalActions = 2 + rand.nextInt(2); // 2 or 3 actions
			plannedActions = new EnemyAction[totalActions];

			// First action: mostly heal
			plannedActions[0] = rand.nextInt(100) < 80 ? EnemyAction.HEAL : EnemyAction.SHIELD;

			// Remaining actions: mix of attacks/heal/shield
			for (int i = 1; i < totalActions; i++) {
				int roll = rand.nextInt(100);
				if (hpPercent < 0.25f) {
					if (roll < 60)
						plannedActions[i] = EnemyAction.HEAL;
					else if (roll < 85)
						plannedActions[i] = EnemyAction.SHIELD;
					else
						plannedActions[i] = rand.nextBoolean() ? EnemyAction.ATTACK_1 : EnemyAction.ATTACK_2;
				} else {
					if (roll < 40)
						plannedActions[i] = EnemyAction.HEAL;
					else if (roll < 65)
						plannedActions[i] = EnemyAction.SHIELD;
					else
						plannedActions[i] = rand.nextBoolean() ? EnemyAction.ATTACK_1 : EnemyAction.ATTACK_2;
				}
			}

			System.out.println("Enemy strategy: Defensive (HP < 50%)");
		}

		// Start executing enemy actions
		executingActions = true;
		currentActionIndex = 0;
		nextActionTime = System.currentTimeMillis();
	}

	private void updateEnemyActions() {
		if (!executingActions)
			return;
		if (System.currentTimeMillis() < nextActionTime)
			return;

		if (currentActionIndex < totalActions) {
			performEnemyAction(plannedActions[currentActionIndex], currentActionIndex);
			currentActionIndex++;
			if (currentActionIndex < totalActions)
				nextActionTime = System.currentTimeMillis() + ACTION_DELAY;
		}

		if (currentActionIndex >= totalActions) {
			executingActions = false;
			enemyAttacked = true;
			startNextTurnWithDelay();
		}
	}

	
	//
	private void performEnemyAction(EnemyAction action, int actionNum) {
		Random rand = new Random();
		float hpPercent = (float) enemy.getCurrentHealth() / enemy.getMaxHealth();

		System.out.println("  Action " + (actionNum + 1) + ": " + action);

		
		
		// ====================================================================================================================================================
		// SET ATTACKS BY ATTEMPT
		// ====================================================================================================================================================
		
		
			if(levelCond.getLevelState(1) == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=30;
	        	else damageAddition+=10;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
		     }

	        else if(levelCond.getLevelState(2)  == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=20;
	        	else damageAddition+=20;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
	        }
	        
	        else if(levelCond.getLevelState(3) == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=70;
	        	else damageAddition+=30;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
	        }
	        

	        else if(levelCond.getLevelState(4)  == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=100;
	        	else damageAddition+=50;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
	        }
	        

	        else if(levelCond.getLevelState(5)  == 1) {
	        	if(levelAttempt.getLevelAttempt()==1) damageAddition+=200;
	        	else damageAddition+=100;
	        	
	        	System.out.println("Attempt:" + levelAttempt.getLevelAttempt() + " | Damamge: " + damageAddition);
	        }
		
		
		
		switch (action) {
		case ATTACK_1 -> {
			damage = 10 + rand.nextInt(16) + damageAddition;//10 + rand.nextInt(16);
			enemy.playAttackAnimation();
			Game.getInstance().addEffect(new AttackEffect(player.getX() + 64, player.getY() + 64,
					"/a_attackeffects/2-Enemy-Dark1.png", 16, 5f, 40, -50));
			player.takeDamage(damage, game);
			Game.getInstance().addDamageText(new DamageText(player.getX() + 64, player.getY() - 100 - (actionNum * 30),
					"-" + damage, Color.RED));
			System.out.println("Enemy attack dealt " + damage + " damage!");
		}
		case ATTACK_2 -> {
			 damage = 12 + rand.nextInt(14) + damageAddition;//12 + rand.nextInt(14);
			enemy.playAttackAnimation();
			Game.getInstance().addEffect(new AttackEffect(player.getX() + 64, player.getY() + 64,
					"/a_attackeffects/2-Enemy-Dark-Bolt.png", 11, 6f, 50, -90));
			player.takeDamage(damage, game);
			Game.getInstance().addDamageText(new DamageText(player.getX() + 64, player.getY() - 100 - (actionNum * 30),
					"-" + damage, Color.RED));
			System.out.println("Enemy attack dealt " + damage + " damage!");
		}
		case HEAL -> {
			int heal = (hpPercent < 0.3f ? 25 : 15) + rand.nextInt(16);
			Game.getInstance().addEffect(new AttackEffect(player.getX() + 64, player.getY() + 64,
					"/a_healingeffects/2-Enemy-Heal_Purple.png", 10, 3f, 880, 10));
			enemy.heal(heal);
			Game.getInstance().addDamageText(
					new DamageText(enemy.getX() + 64, enemy.getY() - 100 - (actionNum * 30), "+" + heal, Color.GREEN));
			System.out.println("    Healed " + heal + " HP!");
		}
		case SHIELD -> {
			int shield = 30;
			Game.getInstance().addEffect(new AttackEffect(player.getX() + 64, player.getY() + 64,
					"/a_shieldeffects/2-Enemy-Shield_Purple.png", 20, 0.8f, 900, 10));
			enemy.addShield(shield);
			Game.getInstance().addDamageText(new DamageText(enemy.getX() + 64, enemy.getY() - 120 - (actionNum * 30),
					"Shield +" + shield, Color.CYAN));
			System.out.println("    Gained " + shield + " shield!");
		}
		}
	}

	// =========================================================
	// TURN ROTATION
	// =========================================================
	private void nextTurn() {
		String finished = turnQueue.poll();
		turnQueue.add(finished);
		currentTurn = turnQueue.peek();
		playerAttacked = false;
		enemyAttacked = false;
		System.out.println("Now it's " + currentTurn + "'s turn!");

		if (currentTurn.equals("PLAYER")) {
			resetPlayerCost();
			if (game.getPileManager() != null)
				game.getPileManager().resetForNewTurn();
			player.resetTurnFlags();
			game.resetCardSelectionPrompt();
		}
	}

	public void endPlayerTurn() {
		if (currentTurn.equals("PLAYER") && !playerAttacked) {
			playerAttacked = true;
			waitingForPlayer = false;
			startNextTurnWithDelay();
			System.out.println("Player ended their turn.");
		}
	}

	public void startPlayerTurn() {
		playerCost = MAX_PLAYER_COST;
		playerAttacked = false;
		game.resetCardSelectionPrompt();
	}
}
