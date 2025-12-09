package CG_04_Inputs;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Aa_00_GlobalVariable.GameLevelAttempt;
import Aa_00_GlobalVariable.LevelConditional;
import Aa_03_GameMap.MapFrame;
import CG_01_Main.Game;
import CG_01_Main.GamePanel;


public class KeyboardInputs implements KeyListener{
	private Game game;
	private GamePanel gamePanel;
	
    //GLOBAL VARIABLE: LEVEL CONDITIONAL
    LevelConditional levelCond = LevelConditional.GLOBAL;
    GameLevelAttempt levelAttempt = GameLevelAttempt.GLOBAL;
    
	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		 this.game = gamePanel.getGame(); // <-- add this
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	} 
	
	@Override
	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();
		switch(key) {
			case KeyEvent.VK_R: 
			 // CLOSE MAP JFRAME
	        Frame[] frames2 = Frame.getFrames();
	        for (Frame f : frames2) {
	            if (f.isVisible()) {
	                f.dispose();
	            }
	        }	
	        
	        new Game(); 
			break;
			case  KeyEvent.VK_ENTER: 
				
				// CLOSE MAP JFRAME
				setAttempt();
				setLevel() ;		
    	        Frame[] frames = Frame.getFrames();
    	        for (Frame f : frames) {
    	            if (f.isVisible()) {
    	                f.dispose();
    	            }
    	        }
				MapFrame map = new MapFrame(); // Call Game Map
				map.setVisible(true);
				break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}


	// 1 == current level // 2 == complete
	public void setLevel() {
	 	if(levelCond.getLevelState(1) == 1) {
	 		levelCond.setLevelState(1, 2); // level1 finished
	 		levelCond.setLevelState(2, 1); // unlock level2
	 		System.out.println("Level 2: VICTORY! " + levelCond.getLevelState(2));
        }

        else if(levelCond.getLevelState(2)  == 1) {
        	levelCond.setLevelState(2, 2); // level1 finished
        	levelCond.setLevelState(3, 1); // unlock level3
        	System.out.println("Level 2: VICTORY! " + levelCond.getLevelState(2));
        }
        

        else if(levelCond.getLevelState(3) == 1) {
        	levelCond.setLevelState(3, 2); // level1 finished
        	levelCond.setLevelState(4, 1); // unlock level4
        	System.out.println("Level 3: VICTORY!" + levelCond.getLevelState(3));
        }
        

        else if(levelCond.getLevelState(4)  == 1) {
        	levelCond.setLevelState(4, 2); // level1 finished
        	levelCond.setLevelState(5, 1); // unlock level2
        	System.out.println("Level 4: VICTORY!" + levelCond.getLevelState(4));
        }
        

        else if(levelCond.getLevelState(5)  == 1) {
        	levelCond.setLevelState(5, 2); // level1 finished
        	levelCond.setLevelState(5, 2); // unlock level2
        	System.out.println("Level 5: VICTORY!" + levelCond.getLevelState(5));
        }
	
	
	
	}
	
	
	public void setAttempt() {
		// Retry option || RETRY GAME
		
	    if (game.getBattleUI().isShowingRetry()) {
	       // game.restartBattle(); 
	    	
	        // CLOSE MAP JFRAME
	        Frame[] frames = Frame.getFrames();
	        for (Frame f : frames) {
	            if (f.isVisible()) {
	                f.dispose();
	            }
	        }
	        
	        
	        // SET LEVEL ATTEMPT
	        if(levelCond.getLevelState(1) == 1 ) {
	        	if(levelAttempt.getLevelAttempt()==0) levelAttempt.setLevelAttempt(1);
	        	if(levelAttempt.getLevelAttempt()==1) levelAttempt.setLevelAttempt(2);
	        	
	        	System.out.println("Attempt: " + levelAttempt.getLevelAttempt());
	        }
	        

	        else if(levelCond.getLevelState(2)  == 1) {
	        	if(levelAttempt.getLevelAttempt()==0) levelAttempt.setLevelAttempt(1);
	        	if(levelAttempt.getLevelAttempt()==1) levelAttempt.setLevelAttempt(2);
	        	
	        	System.out.println("Attempt: " + levelAttempt.getLevelAttempt());
	        }
	        

	        else if(levelCond.getLevelState(3) == 1) {
	        	if(levelAttempt.getLevelAttempt()==0) levelAttempt.setLevelAttempt(1);
	        	if(levelAttempt.getLevelAttempt()==1) levelAttempt.setLevelAttempt(2);
	        	
	        	System.out.println("Attempt: " + levelAttempt.getLevelAttempt());
	        }
	        

	        else if(levelCond.getLevelState(4)  == 1) {
	        	if(levelAttempt.getLevelAttempt()==0) levelAttempt.setLevelAttempt(1);
	        	if(levelAttempt.getLevelAttempt()==1) levelAttempt.setLevelAttempt(2);
	        	
	        	System.out.println("Attempt: " + levelAttempt.getLevelAttempt());
	        }

	        else if(levelCond.getLevelState(5)  == 1) {
	        	if(levelAttempt.getLevelAttempt()==0) levelAttempt.setLevelAttempt(1);
	        	if(levelAttempt.getLevelAttempt()==1) levelAttempt.setLevelAttempt(2);
	        	
	        	System.out.println("Attempt: " + levelAttempt.getLevelAttempt());
	        }
	        

	}
				   
}

	
}