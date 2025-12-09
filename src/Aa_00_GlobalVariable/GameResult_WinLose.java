package Aa_00_GlobalVariable;

public class GameResult_WinLose {
	
	
    // GLOBAL instance
    public static final GameResult_WinLose GLOBAL = new GameResult_WinLose();
    
    private int gameResult = 0;
    
    private GameResult_WinLose(){};
    
    public int getGameResult() {
    	return gameResult;
    }
    
    public void setGameResult(int result) {
    	this.gameResult = result;
    }
    
}
