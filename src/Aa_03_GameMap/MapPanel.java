package Aa_03_GameMap;

import javax.swing.*;

import Aa_00_GlobalVariable.GameInfo_Condition;
import Aa_00_GlobalVariable.LevelConditional;
import Aa_01_BackgroundMusic.MusicPlayer;
import Aa_01_BackgroundMusic.SoundEffectsPlayer;
import Aa_05_GGameInfo.*;
import Aa_06_PopupWindow.GameResultFrame;
import Aa_06_PopupWindow.GameResultPanel;
import Aa_06_PopupWindow.QuitGamePanel;
import Aa_06_PopupWindow.QuitGame_Frame;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
public class MapPanel extends JPanel {

	//private JFrame mainFrame;
    private BufferedImage background;
    private GameInfo_Buttons buttonsPanel; // call CardButtons
    //private String imageURL = "/GameMap/GameMap101.png";
    
    
    private String imageURL = "/GameMap/GameMap_11.png";
    private GameInfo_Condition getButtonCondition;
  
    private MusicPlayer getMusic = new  MusicPlayer();
    
    //LOAD IMAGE TO METHOD
    
    public void setBackgroundImage(String path) {
        try {
        	
            imageURL = path;
            background = ImageIO.read(getClass().getResource(imageURL));
            repaint(); 
        } catch (Exception e) {
            System.err.println("Could not load background.");
        }
    }
   
    LevelConditional levelCond = LevelConditional.GLOBAL;
	GameInfo_Condition setButtonCondition = GameInfo_Condition.GLOBAL;
			
    String locked;
    String normal;
    String hover;
    boolean unlocked = false;

    //CONDITION: 
    //1=CurrentlLevel, 2 = LevelComplete, 0 = LevelLocked

    int state = levelCond.getLevelState(1);

    
    private void addLevelIcon(int levelNumber, int levelState, int x, int y) {

        String locked = "", normal = "", hover = "";
        boolean unlocked = false;

        // Assign icons based on levelNumber
        switch(levelNumber) {
            case 1:
                locked = "/GameMap/Level1_Locked.png";
                normal = "/GameMap/Level1_Normal.png";
                hover  = "/GameMap/Level1_Hover.png";
                break;
            case 2:
                locked = "/GameMap/Level2_Locked.png";
                normal = "/GameMap/Level2_Normal.png";
                hover  = "/GameMap/Level2_Hover.png";
                break;
            case 3:
                locked = "/GameMap/Level3_Locked.png";
                normal = "/GameMap/Level3_Normal.png";
                hover  = "/GameMap/Level3_Hover.png";
                break;
            case 4:
                locked = "/GameMap/Level4_Locked.png";
                normal = "/GameMap/Level4_Normal.png";
                hover  = "/GameMap/Level4_Hover.png";
                break;
            case 5:
                locked = "/GameMap/Level5_Locked.png";
                normal = "/GameMap/Level5_Normal.png";
                hover  = "/GameMap/Level5_Hover.png";
                break;
            case 6:
                locked = "/GameMap/Level6_Locked.png";
                normal = "/GameMap/Level6_Normal.png";
                hover  = "/GameMap/Level6_Hover.png";
                break;
            default:
                locked = normal = hover = "/GameMap/default_locked.png";
        }

        // Decide unlock state and adjust icons based on levelState
        switch(levelState) {
            case 1: // Available
                unlocked = true;
                break;
            case 2: // DEAD - COMPLETED
                locked = normal = hover = "/GameMap/Level" + levelNumber + "_Dead.png";
                unlocked = false;
                break;
            case 0: // Locked
            default:
                unlocked = false;
                break;
        }

        MapLevel_Icons icon = new MapLevel_Icons(locked, normal, hover, unlocked);
        icon.setBounds(x, y, 230, 230);
        add(icon);

        if (unlocked) {
            icon.enableFloating();
        }
    }


    // ==========================================
    // PANEL AND ADD TO THE FRAME
    // ==========================================
  
    public MapPanel() {
    	
    	//imageURL = "/GameMap/GameMap101.png";
    	getMusic.playMusic("/BackgroundMusic/02_Map and MainMenu.wav", true);
    	
    	
	    	//if(levelCond.getLevelState(1) == 2) {

    	    		//display panel
	    	    //JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapPanel.this);
    	        //mainFrame.getGlassPane().setVisible(true);
    	     
    	      //  GameResultPanel panel = new GameResultPanel(null, null);
    	      //  new GameResultFrame(panel);
    	        
	   /// }
		
 
  
        // MAP BACKGROUND
        try {
            background = ImageIO.read(getClass().getResource(imageURL));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load background image.");
        }

        //SET FRAME SIZE
        setPreferredSize(new Dimension(1280, 800)); // 1280, 800
        setLayout(null);

        
       // ================================================================
       // GAME LEVEL ICONS
       // ================================================================  
        
        addLevelIcon(1, levelCond.getLevelState(5), 480, 10);  // LEVEL 6
        addLevelIcon(5, levelCond.getLevelState(2), 658, 270); // LEVEL 2
        addLevelIcon(3, levelCond.getLevelState(3), 370, 215); // LEVEL 3
        addLevelIcon(4, levelCond.getLevelState(4), 665, 123); // LEVEL 4
        addLevelIcon(6, levelCond.getLevelState(1), 340, 400); // LEVEL 1
        

        // ================================================================
        // CALL BUTTONS PANEL FOR GAME INFO
        // ================================================================  

        buttonsPanel = new GameInfo_Buttons(
        	    e -> {  
        	    	
        	    	//get button condition -> display images for storyline
        	    		GameInfo_Condition.GLOBAL.setInfoButtonState(1);
	
        	    	//display panel
        	        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapPanel.this);
        	        mainFrame.getGlassPane().setVisible(true);
 
        	        GameStoryPanel panel = new GameStoryPanel(mainFrame, null);
        	        new GameFrame(panel);
        	        
        	    },

        	    e -> {
        	    	//get button condition -> display images for characters
        	    	GameInfo_Condition.GLOBAL.setInfoButtonState(2);
	
        	    	//display panel
        	        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapPanel.this);
        	        mainFrame.getGlassPane().setVisible(true);
 
        	        GameStoryPanel panel = new GameStoryPanel(mainFrame, null);
        	        new GameFrame(panel);
        	    }, 
        	    
        	    e -> {
        	    	//get button condition -> display images for card info
        	    	GameInfo_Condition.GLOBAL.setInfoButtonState(3);
	
        	    	//display panel
        	        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapPanel.this);
        	        mainFrame.getGlassPane().setVisible(true);
 
        	        GameStoryPanel panel = new GameStoryPanel(mainFrame, null);
        	        new GameFrame(panel);
        	    },
        	    
         	    
        	    e -> {
        	    	
        	    	JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapPanel.this);
        	        mainFrame.getGlassPane().setVisible(true);
 
        	        QuitGamePanel panel = new QuitGamePanel(mainFrame);
        	        new QuitGame_Frame(panel);
        	        
        	    } 
        	);	
        

        buttonsPanel.setBounds(1100, 20, 140, 260);
        buttonsPanel.setBackground(new Color(0, 0, 0, 50));  // semi-transparent black
        buttonsPanel.setOpaque(true);
	    add(buttonsPanel);
	    //add(new StoryPanel());
	    
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
    
}

