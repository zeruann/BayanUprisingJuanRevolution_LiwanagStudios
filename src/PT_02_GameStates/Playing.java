package PT_02_GameStates;

import static PT_08_Utilz.Constants.Environment.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import CG_03_UI.TextRenderer;
import PT_01_Entities.Player;
import PT_04_Levels.LevelManager;
import PT_05_Objects.ObjectManager;
import PT_07_UI.GameOverOverlay;
import PT_08_Utilz.LoadSave;
import main.PlatformerGame;

public class Playing implements Statemethods {
	private Player player;
	private PlatformerGame game;
	private LevelManager levelManager;
	private GameOverOverlay gameOverOverlay;
	private ObjectManager objectManager;
	private Font font;
	private int xLvlOffset;
	private int leftBorder = (int) (0.5 * PlatformerGame.GAME_WIDTH);
	private int rightBorder = (int) (0.5 * PlatformerGame.GAME_WIDTH);
	private int maxLvlOffsetX;

	private BufferedImage backgroundImg, bigCloud, smallCloud;
	private int[] smallCloudsPos;
	private Random rnd = new Random();

	private boolean gameOver;
	
	public Playing(PlatformerGame game) {
		this.game = game;
		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudsPos = new int[8];
		for (int i = 0; i < smallCloudsPos.length; i++)
			smallCloudsPos[i] = (int) (90 * PlatformerGame.SCALE) + rnd.nextInt((int) (100 * PlatformerGame.SCALE));

		calcLvlOffset();
		loadStartLevel();
	}

	public void loadNextLevel() {
		resetAll();
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void loadStartLevel() {
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		objectManager = new ObjectManager(this);

		player = new Player(100, 100, 64, 64, this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		gameOverOverlay = new GameOverOverlay(this);
	}

	public void update() {
		if (!gameOver) {
			levelManager.update();
			objectManager.update(player);
			player.update();
			checkCloseToBorder();
		}
	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;
	}

	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, PlatformerGame.GAME_WIDTH, PlatformerGame.GAME_HEIGHT, null);

		drawClouds(g);

		levelManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);
		
		
		if (gameOver)
			gameOverOverlay.draw(g);
	}

	private void drawClouds(Graphics g) {
		for (int i = 0; i < 3; i++)
			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * PlatformerGame.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);

		for (int i = 0; i < smallCloudsPos.length; i++)
			g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
	}

	public void resetAll() {
		gameOver = false;
		player.resetAll();
		objectManager.resetAllObjects();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}


	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver)
			gameOverOverlay.keyPressed(e);
		else
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			case KeyEvent.VK_F:
				// Activate portal if player is near one
				objectManager.activatePortal();
				break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			}

	}

	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	
	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public PlatformerGame getGame() {
		return game;
	}
	
	public LevelManager getLevelManager() {
		return levelManager;
	}

}