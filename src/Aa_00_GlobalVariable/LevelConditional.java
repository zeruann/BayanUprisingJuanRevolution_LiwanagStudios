package Aa_00_GlobalVariable;

public class LevelConditional{

	public static final LevelConditional GLOBAL = new LevelConditional();
	
	// 0 = inactive, 1 = active, 2 = completed
    private int[] levelState = new int[6];

    public LevelConditional() {

        levelState[0] = 1; // Level 1 active
        for (int i = 1; i < 6; i++) {
            levelState[i] = 0; // inactive
        }
        
    }

    // GETTER
    public int getLevelState(int levelIndex) {
        if (levelIndex < 1 || levelIndex > 6) return -1; 
        return levelState[levelIndex - 1];
    }

    // SETTER
    public void setLevelState(int levelIndex, int state) {
        if (levelIndex < 1 || levelIndex > 6) return;
        if (state < 0 || state > 2) return;
        levelState[levelIndex - 1] = state;
    }

    // Utility helpers
    public boolean isActive(int levelIndex) {
        return getLevelState(levelIndex) == 1;
    }

    public boolean isCompleted(int levelIndex) {
        return getLevelState(levelIndex) == 2;
    }

    public boolean isInactive(int levelIndex) {
        return getLevelState(levelIndex) == 0;
    }
    
}

