package PT_04_Levels;

import static PT_08_Utilz.HelpMethods.GetLevelData;
import static PT_08_Utilz.HelpMethods.GetPlayerSpawn;
import static PT_08_Utilz.HelpMethods.GetPortal;
import static PT_08_Utilz.HelpMethods.GetSpike;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import PT_05_Objects.Portal;
import PT_05_Objects.Spike;
import main.PlatformerGame;

public class Level {

	private BufferedImage img;
	private int[][] lvlData;
	private ArrayList<Portal> dimension;
	private ArrayList<Spike> spike;
	
	
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;

	public Level(BufferedImage img) {
		this.img = img;
		createLevelData();
		calcLvlOffsets();
		calcPlayerSpawn();
		createPortal();
		createSpike();
	}	

	private void createSpike() {
		spike = GetSpike(img);
		
	}

	private void createPortal() {
		dimension = GetPortal(img);		
	}

	private void calcPlayerSpawn() {
		playerSpawn = GetPlayerSpawn(img);
		
	}

	private void calcLvlOffsets() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - PlatformerGame.TILES_IN_WIDTH;
		maxLvlOffsetX = PlatformerGame.TILES_SIZE * maxTilesOffset;
	}
	
	private void createLevelData() {
		lvlData = GetLevelData(img);
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLevelData() {
		return lvlData;
	}

	public int getLvlOffset() {
		return maxLvlOffsetX;
	}

	public Point getPlayerSpawn() {
		return playerSpawn;
	}

	public ArrayList<Portal> getPortal() {
		return dimension;
	}
	
	public ArrayList<Spike> getSpike() {
		return spike;
	}
}
