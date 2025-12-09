package Aa_00_GlobalVariable;

public class GameLevelAttempt {
	public static  final GameLevelAttempt GLOBAL = new GameLevelAttempt();
	
	private int levelAttempt =0;
	
	private GameLevelAttempt() {
		
	}; 
	
	public int getLevelAttempt() {
		return levelAttempt;
	}
	
	public void setLevelAttempt(int levelAttempt) {
		this.levelAttempt = levelAttempt;
	}
	
}
