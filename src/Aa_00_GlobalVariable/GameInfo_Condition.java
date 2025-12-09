package Aa_00_GlobalVariable;

public class GameInfo_Condition {

    // GLOBAL instance
    public static final GameInfo_Condition GLOBAL = new GameInfo_Condition();

    // Store info panel condition (1=Storyline, 2=Cards Info, 3=Characters)
    private int infoButtonState = 0;

    private GameInfo_Condition() {}

    public int getInfoButtonState() {
        return infoButtonState;
    }

    public void setInfoButtonState(int state) {
        this.infoButtonState = state;
    }
}
