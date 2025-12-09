package PT_05_Objects;

import static PT_08_Utilz.Constants.ObjectConstants.*;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Aa_00_GlobalVariable.LevelConditional;
import CG_01_Main.Game;
import PT_01_Entities.Player;
import PT_02_GameStates.Playing;
import PT_04_Levels.Level;
import PT_08_Utilz.LoadSave;

public class ObjectManager {

	private Playing playing;
	private BufferedImage[] portals;
	private BufferedImage spikeImg;
	private ArrayList<Portal> portalsArray;
	private ArrayList<Spike> spikesArray;
	
	
	LevelConditional levelCond = LevelConditional.GLOBAL;
	
	
	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();
		portalsArray = new ArrayList<>();
		spikesArray = new ArrayList<>();
	}
	
	public void update(Player player) {
		updatePortals();
		checkObjectTouched(player.getHitbox(), player);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawPortals(g, xLvlOffset);
		drawSpikes(g, xLvlOffset);
	}

	private void updatePortals() {
		for (Portal p : portalsArray)
			if (p.isActive())
				p.update();
	}

	public void checkObjectTouched(Rectangle2D.Float hitbox, Player player) {
		// Check portal collision
		for (Portal p : portalsArray) {
			if (p.isActive()) {
				if (hitbox.intersects(p.getHitbox())) {
					// Player is touching the portal
					if (!p.isPlayerInPortal()) {
						p.onPlayerEnter();
					}
				} else {
					// Player left the portal area
					if (p.isPlayerInPortal()) {
						p.onPlayerExit();
					}
				}
			}
		}
		
		// Check spike collision
		for (Spike spike : spikesArray) {
			if (spike.isActive()) {
				if (hitbox.intersects(spike.getHitbox())) {
					// Player touched spike - respawn at spawn point
					handleSpikeCollision(player);
					break; // Only trigger once per frame
				}
			}
		}
	}

	private void handleSpikeCollision(Player player) {
		System.out.println("Player hit spike! Respawning...");
		
		// TODO: Add effects before respawn
		// playing.getGame().getAudioPlayer().playEffect(SPIKE_HIT_SOUND);
		// player.playHurtAnimation();
		// Add brief delay or flash effect
		
		// Reset player to spawn point
		player.resetAll();
		player.setSpawn(playing.getLevelManager().getCurrentLevel().getPlayerSpawn());
		
		// Alternative: Reduce health instead of instant respawn
		// player.changeHealth(-10);
		// if (player.getCurrentHealth() <= 0) {
		//     playing.setGameOver(true);
		// }
	}

	public void activatePortal() {
		// Check if player is near any portal
		for (Portal p : portalsArray) {
			if (p.isActive() && p.isPlayerInPortal()) {
				handlePortalEntry();
				break; // Only activate one portal at a time
			}
		}
	}

	private void handlePortalEntry() {
		// Portal entry logic - triggers level transition
		System.out.println("Portal activated! Loading next level...");
		
		// TODO: Add transition effects here (fade out, sound, etc.)
		// playing.getGame().getAudioPlayer().playEffect(PORTAL_SOUND);
		// playing.startLevelTransition(); // Add fade/transition effect
		
		// Load the next level
		//playing.loadNextLevel();
		
        // CLOSE MAP JFRAME
        Frame[] frames = Frame.getFrames();
        for (Frame f : frames) {
            if (f.isVisible()) {
                f.dispose();
            }
        }
		new Game();
		
		
		// Alternative: If you want to load a completely new frame/window:
		// JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(playing.getGame().getGamePanel());
		// currentFrame.dispose(); // Close current frame
		// new GameWindow(new Game()); // Create new game window
		
		// Or load a specific level:
		// playing.getLevelManager().setLevel(NEXT_LEVEL_INDEX);
		// playing.resetAll();
	}

	public void loadObjects(Level newLevel) {
		portalsArray = newLevel.getPortal();
		spikesArray = newLevel.getSpike();
	}

	private void loadImgs() {
		// Portal
		BufferedImage portal = LoadSave.GetSpriteAtlas(LoadSave.PORTAL_SPRITE);
		portals = new BufferedImage[6];
		for (int i = 0; i < portals.length; i++)
			portals[i] = portal.getSubimage(32 * i, 0, 32, 32);
		
		// Spike
		spikeImg = LoadSave.GetSpriteAtlas(LoadSave.SPIKE);
	}

	private void drawPortals(Graphics g, int xLvlOffset) {
		for (Portal gc : portalsArray)
			if (gc.isActive()) {
				if (gc.getObjType() == PORTAL)
					g.drawImage(portals[gc.getAniIndex()],
						(int)(gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
						(int)(gc.getHitbox().y - gc.getyDrawOffset()),
						PORTAL_WIDTH, PORTAL_HEIGHT, null);
			}
	}

	private void drawSpikes(Graphics g, int xLvlOffset) {
		for (Spike spike : spikesArray)
			if (spike.isActive()) {
				g.drawImage(spikeImg,
					(int)(spike.getHitbox().x - spike.getxDrawOffset() - xLvlOffset),
					(int)((spike.getHitbox().y - spike.getyDrawOffset() + 100)),
					SPIKE_WIDTH, SPIKE_HEIGHT, null);
			}
	}

	public void resetAllObjects() {
		for (Portal p : portalsArray)
			p.reset();
		
		// Spikes don't need resetting, they're always active
		for (Spike spike : spikesArray)
			spike.setActive(true);
	}
}