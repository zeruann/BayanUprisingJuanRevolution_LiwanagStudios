package Aa_06_PopupWindow;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import Aa_00_GlobalVariable.GameLevelAttempt;
import Aa_00_GlobalVariable.GameResult_WinLose;
import Aa_00_GlobalVariable.LevelConditional;

import Aa_03_GameMap.MapFrame;
import CG_01_Main.Game;
import CG_01_Main.GameWindow;

public class GameResultPanel extends JPanel implements Runnable {

	public int WIDTH = 435;
	public int HEIGHT = 570;

	private BufferedImage bgImage;
	private JFrame mainFrame;
	private GameResultButton resultButton;
	private Game game;
	private String imagePath;

	// =========================================================================
	// GOBAL VARIABLES
	// =========================================================================
	LevelConditional levelCond = LevelConditional.GLOBAL;
	GameResult_WinLose result = GameResult_WinLose.GLOBAL;
	GameLevelAttempt attempt = GameLevelAttempt.GLOBAL;

	private PopUp_Sprite sprite; // <–– IMPORTANT: stored here
	private Thread thread;

	// =========================================================================
	// CONSTRUCTOR
	// =========================================================================
	public GameResultPanel(JFrame mainFrame, String imagePath) {

	    this.mainFrame = mainFrame;
	    this.game = game; // use existing game
	    this.imagePath = imagePath;

	    setOpaque(false);
	    setPreferredSize(new Dimension(WIDTH, HEIGHT));
	    setLayout(null);

	    // ---------------------------------------------------------
	    // PICK BACKGROUND IMAGE
	    // ---------------------------------------------------------
	    if (result.getGameResult() == 2) {
	        this.imagePath = "/BattleResult/WIN_JuanPopup.png";
	    } else {
	        if (levelCond.getLevelState(1) == 1)
	            this.imagePath = "/BattleResult/CrocProfile.png";
	    }

	    loadBackground();
	    loadSpriteAnimation();

	    // Start animation thread
	    thread = new Thread(this);
	    thread.start();

	    // ---------------------------------------------------------
	    // RESULT BUTTONS
	    // ---------------------------------------------------------
	    resultButton = new GameResultButton(e -> {
	        attempt.setLevelAttempt(attempt.getLevelAttempt() + 1);
	        
//	        if (result.getGameResult() == 1) {
//	            // WIN - Only update the level that was actually completed
//	            int completedLevel = getCompletedLevel();
//	            if (completedLevel > 0) {
//	                levelCond.setLevelState(completedLevel, 2); // mark as finished
//	                if (completedLevel < 5) {
//	                    levelCond.setLevelState(completedLevel + 1, 1); // unlock next
//	                }
//	                System.out.println("Level " + completedLevel + ": VICTORY!");
//	            }

	            // CLOSE POPUP
	            Window popup = SwingUtilities.getWindowAncestor(this);
	            if (popup != null)
	                popup.dispose();

	            // CLOSE ALL FRAMES
	            Frame[] frames = Frame.getFrames();
	            for (Frame f : frames) {
	                if (f.isVisible()) {
	                    f.dispose();
	                }
	            }
//
//	            // CREATE ONLY ONE MapFrame (AFTER ALL CLEANUP)
//	            MapFrame map = new MapFrame();
//	            map.setVisible(true);
//	            return;
//	        }

	        // LOSE - Just restart current level
	        // CLOSE POPUP
//	        Window popup = SwingUtilities.getWindowAncestor(this);
//	        if (popup != null)
//	            popup.dispose();
//
//	        // CLOSE CURRENT JFRAME
//	        Frame[] frames = Frame.getFrames();
//	        for (Frame f : frames) {
//	            if (f.isVisible()) {
//	                f.dispose();
//	            }
//	        }
//
//	        new Game();

	    }, e -> {
//	        // FOR QUIT - CALL MAP FRAME
//	        Window popup = SwingUtilities.getWindowAncestor(this);
//	        if (popup != null)
//	            popup.dispose();
//
//	        // CLOSE MAP JFRAME
//	        Frame[] frames = Frame.getFrames();
//	        for (Frame f : frames) {
//	            if (f.isVisible()) {
//	                f.dispose();
//	            }
//	        }
//	        MapFrame map = new MapFrame();
//	        map.setVisible(true);
	    });

	    resultButton.setBounds(0, 0, WIDTH, HEIGHT);
	    resultButton.setOpaque(false);
	    add(resultButton);
	}

	// =========================================================================
	// HELPER METHOD TO GET THE COMPLETED LEVEL
	// =========================================================================
	private int getCompletedLevel() {
	    // This method should determine which level was just completed
	    // You need to pass this information from the Game class
	    
	    // For now, return the first active level (this might need adjustment)
	    for (int i = 1; i <= 5; i++) {
	        if (levelCond.getLevelState(i) == 1) {
	            return i;
	        }
	    }
	    return -1; // No level found
	}
	// =========================================================================
	// LOAD BACKGROUND
	// =========================================================================
	private void loadBackground() {
		try (InputStream is = getClass().getResourceAsStream(imagePath)) {
			if (is != null) {
				bgImage = ImageIO.read(is);
				System.out.println("Loaded BG: " + imagePath);
			} else {
				System.err.println("Could not load BG: " + imagePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// =========================================================================
	// LOAD SPRITE ANIMATION
	// =========================================================================
	private void loadSpriteAnimation() {

		String spritePath = "";
		if (result.getGameResult() == 1) {
			if (levelCond.getLevelState(1) == 1)
				spritePath = "/BattleResult/02_mouse-hurt.png";
			if (levelCond.getLevelState(2) == 1)
				spritePath = "/BattleResult/03_pig-hurt.png";
			if (levelCond.getLevelState(3) == 1)
				spritePath = "/BattleResult/04_rat-hurt.png";
			if (levelCond.getLevelState(4) == 1)
				spritePath = "/BattleResult/05_Boarman-hurt.png";
			if (levelCond.getLevelState(5) == 1)
				spritePath = "/BattleResult/06_Croc-hurt.png";

		} else {
			spritePath = "/BattleResult/01_Juan-Hurt.png";
		}

		try (InputStream is = getClass().getResourceAsStream(spritePath)) {

			if (is == null) {
				System.err.println("SPRITE NOT FOUND: " + spritePath);
				return;
			}

			BufferedImage sheet = ImageIO.read(is);
			sprite = new PopUp_Sprite(sheet, 64, 64, 6, 85, 195);
			// (frameWidth & frameHeight = 64, totalFrames = 2, position = 160,300)

			System.out.println("LOADED SPRITE: " + spritePath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// =========================================================================
	// PAINT EVERYTHING
	// =========================================================================
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (bgImage != null) {
			g.drawImage(bgImage, 0, 0, WIDTH, HEIGHT, null);
		}

		if (sprite != null) {
			sprite.draw(g);
		}
	}

	// =========================================================================
	// ANIMATION LOOP
	// =========================================================================
	@Override
	public void run() {
		while (true) {

			if (sprite != null)
				sprite.update();

			repaint();

			try {
				Thread.sleep(16); // 60 FPS
			} catch (Exception e) {
			}
		}
	}
}
