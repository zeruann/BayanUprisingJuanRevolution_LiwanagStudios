package CG_03_UI;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Aa_00_GlobalVariable.LevelConditional;
import CG_01_Main.Game;
import CG_02_System.TurnManager;
import CG_06_Entities.Enemy;
import CG_06_Entities.Player;

public class HUDRenderer {

	// *** ADD LEVEL CONDITIONAL ***
	LevelConditional levelCond = LevelConditional.GLOBAL;

	/*** === CONSTANTS === ***/
	private static final int BAR_WIDTH = 400;
	private static final int BAR_HEIGHT = 20;
	private static final int OUTLINE_THICKNESS = 2;
	private static final int OUTLINE_THICKNESS_NAME = 3;

	private static final int PLAYER_NAME_X = 20;
	private static final int PLAYER_NAME_Y = 60;

	private static final int ENEMY_NAME_Y = 60;
	private static final int SCREEN_WIDTH = 1280;

	private static final int SHIELD_ICON_SIZE = 32;
	private static final int SHIELD_ICON_Y_OFFSET = 110;

	/*** === FIELDS === ***/
	private Player player;
	private Enemy enemy;
	private Font baseFont;
	private BufferedImage shieldIcon;
	private BufferedImage manaIcon;

	private BufferedImage playerProfile;
	private BufferedImage mouseProfile;
	private BufferedImage pigProfile;
	private BufferedImage ratProfile;
	private BufferedImage boarProfile;
	private BufferedImage crocProfile;
	
	// *** ADD CURRENT ENEMY PROFILE ***
	private BufferedImage currentEnemyProfile;
	private String currentEnemyName;
	
	private static final int PROFILE_SIZE = 150;
	private static final int PROFILE_PADDING = 10;
	
	private boolean visible = false;

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	//CONSTRUCTOR 
	public HUDRenderer(Font font, Player player, Enemy enemy) {
		this.baseFont = font;
		this.player = player;
		this.enemy = enemy;

		try {
			playerProfile = ImageIO.read(getClass().getResourceAsStream("/profile/Juan.png"));
			mouseProfile = ImageIO.read(getClass().getResourceAsStream("/profile/Mouse.png"));
			pigProfile = ImageIO.read(getClass().getResourceAsStream("/profile/Pig.png"));
			ratProfile = ImageIO.read(getClass().getResourceAsStream("/profile/Rat.png"));
			boarProfile = ImageIO.read(getClass().getResourceAsStream("/profile/Boar.png"));
			crocProfile = ImageIO.read(getClass().getResourceAsStream("/profile/Croc.png"));
			
			shieldIcon = ImageIO.read(getClass().getResourceAsStream("/ui/1_shield-icon.png"));
			manaIcon = ImageIO.read(getClass().getResourceAsStream("/ui/1-Cost-icon.png"));
			
			// *** LOAD INITIAL ENEMY PROFILE - ONLY ONCE ***
			loadEnemyProfile();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// LOAD ENEMY PROFILE BASED ON LEVEL - CALLED ONLY ONCE OR ON RESET 
	private void loadEnemyProfile() {
		if (levelCond.getLevelState(1) == 1) {
			currentEnemyProfile = mouseProfile;
			currentEnemyName = "Mang Gagantso";
		} else if (levelCond.getLevelState(2) == 1) {
			currentEnemyProfile = pigProfile;
			currentEnemyName = "Mayor Baboy Santos";
		} else if (levelCond.getLevelState(3) == 1) {
			currentEnemyProfile = ratProfile;
			currentEnemyName = "Gov. Squeaky";
		} else if (levelCond.getLevelState(4) == 1) {
			currentEnemyProfile = boarProfile;
			currentEnemyName = "Director Baboyrambo";
		} else if (levelCond.getLevelState(5) == 1) {
			currentEnemyProfile = crocProfile;
			currentEnemyName = "Kroko Dalisay III";
		} else {
			// Default fallback
			currentEnemyProfile = pigProfile;
			currentEnemyName = "Unknown Enemy";
		}
	}

	//MAIN DRAW
	public void draw(Graphics2D g2d) {
		if (!visible) return;
		g2d.setFont(baseFont);

		drawPlayerHUD(g2d);
		drawEnemyHUD(g2d);
	}

	//PLAYER HUD
	private void drawPlayerHUD(Graphics2D g2d) {
		int profileX = PLAYER_NAME_X;
		int profileY = PLAYER_NAME_Y - 40;

		g2d.drawImage(playerProfile, profileX, profileY, PROFILE_SIZE, PROFILE_SIZE, null);

		int nameX = profileX + PROFILE_SIZE + PROFILE_PADDING;
		int nameY = PLAYER_NAME_Y;

		TextRenderer.drawOutlinedText(g2d, "Juan", nameX, nameY, Color.WHITE, Color.BLACK, OUTLINE_THICKNESS_NAME);

		int barX = nameX;
		int barY = nameY + 20;

		drawHealthBar(g2d, barX, barY, player.getCurrentHealth(), player.getMaxHealth(), true);
		drawHPLabels(g2d, barX, barY, player.getCurrentHealth(), player.getMaxHealth());

		int costY = barY + BAR_HEIGHT + 40;
		drawCostText(g2d, barX, costY);

		int shieldY = costY + 25;
		drawShieldIcon(g2d, player.getShield(), barX, shieldY);
	}

	private void drawCostText(Graphics2D g2d, int x, int y) {
		TurnManager turnManager = Game.getInstance().getTurnManager();
		if (!turnManager.getCurrentTurn().equals("PLAYER"))
			return;

		
	//===================================================================
		int currentCost = turnManager.getPlayerCost();
		int maxCost = 4;

		if(levelCond.getLevelState(1) == 1) {
			maxCost = 4;
        	System.out.println("Total Cost:" + maxCost);
	     }

        else if(levelCond.getLevelState(2)  == 1) {
        	maxCost = 6;
        	System.out.println("Total Cost:" + maxCost);
        }
        
        else if(levelCond.getLevelState(3) == 1) {
        	maxCost = 8;
        	System.out.println("Total Cost:" + maxCost);
        }
        

        else if(levelCond.getLevelState(4)  == 1) {
        	maxCost = 9;
        	System.out.println("Total Cost:" + maxCost);
        }
        

        else if(levelCond.getLevelState(5)  == 1) {
        	maxCost = 10;
        	System.out.println("Total Cost:" + maxCost);
        }
		
		
		
		
		Font original = g2d.getFont();
		Font costFont = original.deriveFont(40f);
		g2d.setFont(costFont);

		int iconSize = 45;
		int iconX = x;
		int iconY = y - 30;
		if (manaIcon != null) {
			g2d.drawImage(manaIcon, iconX, iconY, iconSize, iconSize, null);
		}

		String costText = currentCost + "/" + maxCost;
		TextRenderer.drawOutlinedText(g2d, costText, 
			x + iconSize + 8, y,
			Color.YELLOW, Color.BLACK, OUTLINE_THICKNESS);

		g2d.setFont(original);
	}

	/*** ===================================================== 
	 * ENEMY HUD - NO LONGER RELOADS PROFILE EVERY FRAME
	 * ===================================================== */
	private void drawEnemyHUD(Graphics2D g2d) {
		// *** REMOVED: loadEnemyProfile() call - profile stays constant during battle ***

		int profileX = SCREEN_WIDTH - PROFILE_SIZE - 20;
		int profileY = ENEMY_NAME_Y - 40;

		// *** USE CURRENT ENEMY PROFILE ***
		g2d.drawImage(currentEnemyProfile, profileX, profileY, PROFILE_SIZE, PROFILE_SIZE, null);

		// *** USE CURRENT ENEMY NAME ***
		FontMetrics fm = g2d.getFontMetrics();
		int nameWidth = fm.stringWidth(currentEnemyName);

		int nameX = profileX - PROFILE_PADDING - nameWidth;
		int nameY = ENEMY_NAME_Y;

		TextRenderer.drawOutlinedText(g2d, currentEnemyName, nameX, nameY, Color.WHITE, Color.BLACK, OUTLINE_THICKNESS_NAME);

		// *** HEALTH BAR ***
		int barX = profileX - PROFILE_PADDING - BAR_WIDTH;
		int barY = nameY + 20;

		drawHealthBar(g2d, barX, barY, enemy.getCurrentHealth(), enemy.getMaxHealth(), false);
		drawHPLabels(g2d, barX, barY, enemy.getCurrentHealth(), enemy.getMaxHealth());

		// *** ADD ENEMY SHIELD DISPLAY ***
		int enemyShieldY = barY + BAR_HEIGHT + 10;
		drawEnemyShieldIcon(g2d, enemy.getShield(), barX, enemyShieldY);
	}

	/*** ===================================================== 
	 * PLAYER SHIELD ICON (LEFT SIDE)
	 * ===================================================== */
	private void drawShieldIcon(Graphics2D g2d, int shield, int baseX, int baseY) {
		if (shield <= 0)
			return;

		int iconX = baseX + 10;
		int iconY = baseY;

		g2d.drawImage(shieldIcon, iconX, iconY, SHIELD_ICON_SIZE, SHIELD_ICON_SIZE, null);

		String shieldText = "+" + shield;

		Font original = g2d.getFont();
		Font bigFont = original.deriveFont(40f);
		g2d.setFont(bigFont);

		TextRenderer.drawOutlinedText(g2d, shieldText, iconX + 40, iconY + 24, Color.CYAN, Color.BLACK,
				OUTLINE_THICKNESS);

		g2d.setFont(original);
	}

	/*** ===================================================== 
	 * ENEMY SHIELD ICON (RIGHT SIDE - MIRRORED LAYOUT)
	 * ===================================================== */
	private void drawEnemyShieldIcon(Graphics2D g2d, int shield, int baseX, int baseY) {
		if (shield <= 0)
			return;

		// Position shield on the RIGHT side of the health bar
		int iconX = baseX + BAR_WIDTH - SHIELD_ICON_SIZE - 10;
		int iconY = baseY;

		g2d.drawImage(shieldIcon, iconX, iconY, SHIELD_ICON_SIZE, SHIELD_ICON_SIZE, null);

		String shieldText = "+" + shield;

		Font original = g2d.getFont();
		Font bigFont = original.deriveFont(40f);
		g2d.setFont(bigFont);

		// Calculate text width to position it LEFT of the icon
		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(shieldText);

		TextRenderer.drawOutlinedText(g2d, shieldText, 
			iconX - textWidth - 8, iconY + 24, 
			Color.CYAN, Color.BLACK, OUTLINE_THICKNESS);

		g2d.setFont(original);
	}

	/*** ===================================================== 
	 * GENERIC HEALTH BAR DRAWER
	 * ===================================================== */
	private void drawHealthBar(Graphics2D g2d, int x, int y, int currentHP, int maxHP, boolean reverseFill) {
		double hpPercent = currentHP / (double) maxHP;
		int fillWidth = (int) (hpPercent * BAR_WIDTH);

		g2d.setColor(Color.RED);
		g2d.fillRect(x, y, BAR_WIDTH, BAR_HEIGHT);

		g2d.setColor(Color.GREEN);

		if (reverseFill) {
			g2d.fillRect(x + (BAR_WIDTH - fillWidth), y, fillWidth, BAR_HEIGHT);
		} else {
			g2d.fillRect(x, y, fillWidth, BAR_HEIGHT);
		}

		g2d.setColor(Color.BLACK);
		g2d.drawRect(x, y, BAR_WIDTH, BAR_HEIGHT);
	}

	/*** ===================================================== 
	 * HP LABELS: "HP:" + "current / max"
	 * ===================================================== */
	private void drawHPLabels(Graphics2D g2d, int barX, int barY, int currentHP, int maxHP) {
		Font original = g2d.getFont();
		Font bigFont = original.deriveFont(25f);
		g2d.setFont(bigFont);

		String hpLabel = "HP:";
		TextRenderer.drawOutlinedText(g2d, hpLabel, barX, barY - 2, Color.WHITE, Color.BLACK, OUTLINE_THICKNESS);

		String hpText = currentHP + " / " + maxHP;

		FontMetrics fm = g2d.getFontMetrics();
		int textWidth = fm.stringWidth(hpText);

		TextRenderer.drawOutlinedText(g2d, hpText, barX + BAR_WIDTH - textWidth, barY - 2, Color.WHITE, Color.BLACK,
				OUTLINE_THICKNESS);

		g2d.setFont(original);
	}
	
	// *** PUBLIC METHOD TO MANUALLY REFRESH PROFILE (call when starting new level) ***
	public void refreshEnemyProfile() {
		loadEnemyProfile();
	}
}