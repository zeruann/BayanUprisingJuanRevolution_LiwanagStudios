package CG_06_Entities;

import static CG_05_Utilz.Constants.PlayerConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import CG_01_Main.Game;
import CG_03_UI.DamageText;

import java.io.InputStream;
import java.io.IOException;

public class Player extends Entity {
	
	

	// =========================================================
	// SPRITES
	// =========================================================
	private BufferedImage[] idleFrames;
	private BufferedImage[] attackFrames;
	private BufferedImage[] hurtFrames;
	private BufferedImage[] walkFrames;

	// =========================================================
	// POSITION + ATTACK MOVEMENT
	// =========================================================
	private float originalX, originalY;
	private boolean isAttacking = false;

	// =========================================================
	// ANIMATION
	// =========================================================
	private int aniTick = 0;
	private int aniIndex = 0;
	private int aniSpeed = 30;
	private int playerAction = IDLE; // 0 = IDLE, 1 = ATTACK, 2 = HURT

	// =========================================================
	// MOVEMENT FLAGS
	// =========================================================
	private boolean left, up, right, down;
	private boolean moving = false;
	private boolean attacking = false;
	private boolean hurt = false;

	// =========================================================
	// PLAYER STATS
	// =========================================================
	private int currentHealth = 100;
	private int maxHealth = 100;
	private int shield = 0;
	
	//SET HEALTH EACH LEVEL
	public void setHealthForLevel(int level) {
	    switch (level) {
	        case 1 -> {
	            maxHealth = 200;
	            currentHealth = 200;
	        }
	        case 2 -> {
	            maxHealth = 300;
	            currentHealth = 300;
	        }
	        case 3 -> {
	            maxHealth = 400;
	            currentHealth = 400;
	        }
	        case 4 -> {
	        	maxHealth = 500;
	            currentHealth = 500;
	        }
	        case 5 -> {
	        	maxHealth = 600;
	            currentHealth = 600;
	        }
	        default -> {
	        	maxHealth = 700;
	            currentHealth = 700;
	        }
	    }
	    
	    System.out.println("Player HP set to: " + maxHealth + " for Level " + level);
	}

	// =========================================================
	// CONSTRUCTOR
	// =========================================================
	public Player(float x, float y) {
		super(x, y);
		originalX = x;
		originalY = y;
		loadAnimations();
	}

	// =========================================================
	// UPDATE + RENDER
	// =========================================================
	public void update() {
		updateAnimationTick();
		updateAnimationState();
	}

	public void render(Graphics g) {
		BufferedImage frame = getCurrentFrame();
		g.drawImage(frame, (int) x, (int) y, 64 * 3, 64 * 3, null);
	}

	// =========================================================
	// ANIMATION LOGIC
	// =========================================================
	private void updateAnimationTick() {
	    aniTick++;
	    if (aniTick >= aniSpeed) {
	        aniTick = 0;
	        aniIndex++;

	        int frames = getFramesAmount(playerAction);

	        if (aniIndex >= frames) {
	            // DEAD → hold last frame
	            if (playerAction == HURT && isDead()) {
	                aniIndex = frames - 1;
	                return;
	            }

	            aniIndex = 0; // loop safely

	            if (playerAction == ATTACK || playerAction == HURT) {
	                playerAction = IDLE;
	                attacking = false;
	                hurt = false;
	                isAttacking = false;
	            }
	        }
	    }
	}


	private void updateAnimationState() {
		int previous = playerAction;

		if (hurt)
			playerAction = HURT;
		else if (attacking)
			playerAction = ATTACK;
		else if (moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;

		if (previous != playerAction && !isDead())
			resetAnimation();
	}

	private void resetAnimation() {
		aniTick = 0;
		aniIndex = 0;
	}

	private BufferedImage getCurrentFrame() {
	    BufferedImage[] framesArray;
	    switch (playerAction) {
	        case ATTACK:
	            framesArray = attackFrames;
	            break;
	        case HURT:
	            framesArray = hurtFrames;
	            break;
	        default:
	            framesArray = idleFrames;
	            break;
	    }

	    if (aniIndex >= framesArray.length) {
	        aniIndex = 0; // loop safely
	    }

	    return framesArray[aniIndex];
	}


	private int getFramesAmount(int action) {
		switch (action) {
		case ATTACK:
			return attackFrames.length;
		case HURT:
			return hurtFrames.length;
		default:
			return idleFrames.length;
		}
	}

	// =========================================================
	// COMBAT SYSTEM
	// =========================================================
	public void takeDamage(int damage, Game game) {
		int absorbed = Math.min(shield, damage);
		shield -= absorbed;
		damage -= absorbed;

		if (absorbed > 0) {
			game.addDamageText(new DamageText(getX() + 64, getY() - 40, "-" + absorbed + " absorbed", Color.CYAN));
		}

		currentHealth = Math.max(0, currentHealth - damage);

		playHurtAnimation();
	}

	public void heal(int amount) {
		currentHealth = Math.min(maxHealth, currentHealth + amount);
	}

	public void addShield(int amount) {
		shield += amount;
	}

	public int absorbDamage(int damage) {
		if (shield >= damage) {
			shield -= damage;
			return 0;
		}
		int remaining = damage - shield;
		shield = 0;
		return remaining;
	}

	public boolean isDead() {
		return currentHealth <= 0;
	}

	// =========================================================
	// ANIMATION TRIGGERS
	// =========================================================
	public void playAttackAnimation() {
		playerAction = ATTACK;
		aniIndex = 0;
		aniTick = 0;
		attacking = true;
		isAttacking = true;
	}

	public void playHurtAnimation() {
		playerAction = HURT;
		aniIndex = 0;
		aniTick = 0;
		hurt = true;
	}

	public void resetForBattle() {
		currentHealth = maxHealth;
		shield = 0;
		playerAction = IDLE;
		aniIndex = 0;
		x = originalX;
		y = originalY;
		attacking = false;
		hurt = false;
		isAttacking = false;
		left = right = up = down = false;
	}

	// =========================================================
	// SPRITE LOADING
	// =========================================================
	private void loadAnimations() {
		idleFrames = loadAnimationRow("/characters/01_Juan-idle.png", 2, 3);
		attackFrames = loadAnimationRow("/characters/01_Juan-Attack.png", 7, 3);
		hurtFrames = loadAnimationRow("/characters/01_Juan-Hurt.png", 6, 0);
		walkFrames = loadAnimationRow("/characters/01_Juan-walk.png", 9, 3);
	}

	private BufferedImage[] loadAnimationRow(String path, int cols, int row) {
		try (InputStream is = getClass().getResourceAsStream(path)) {
			BufferedImage sheet = ImageIO.read(is);
			BufferedImage[] frames = new BufferedImage[cols];

			for (int i = 0; i < cols; i++) {
				frames[i] = sheet.getSubimage(i * 64, row * 64, 64, 64);
			}

			return frames;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// =========================================================
	// MOVEMENT UTILITY
	// =========================================================
	public void resetDirectionBooleans() {
		left = right = up = down = false;
	}

	public void resetHealth() {
		this.currentHealth = this.maxHealth;
	}

	// =========================================================
	// GETTERS & SETTERS
	// =========================================================
	// inside Player class
	private boolean shieldAppliedThisTurn = false;

	public boolean isShieldAppliedThisTurn() {
	    return shieldAppliedThisTurn;
	}

	public void setShieldAppliedThisTurn(boolean applied) {
	    this.shieldAppliedThisTurn = applied;
	}

	// Call this at the start of each turn
	public void resetTurnFlags() {
	    shieldAppliedThisTurn = false;
	}

	
	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public int getX() {
		return (int) x;
	}

	@Override
	public int getY() {
		return (int) y;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getShield() {
		return shield;
	}

	// Movement flags (if needed externally)
	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	public void setMaxHealth(int hp) {
	    maxHealth = hp;
	}

	public void setCurrentHealth(int hp) {
	    currentHealth = Math.min(hp, maxHealth);
	}
}
