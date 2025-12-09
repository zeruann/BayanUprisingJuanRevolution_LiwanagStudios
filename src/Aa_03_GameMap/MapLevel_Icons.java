package Aa_03_GameMap;

import javax.swing.*;

import Aa_00_GlobalVariable.LevelConditional;
import Aa_01_BackgroundMusic.SoundEffectsPlayer;
import Aa_04_GEnemyCard.*;
import Aa_05_GGameInfo.GameFrame;
import Aa_05_GGameInfo.GameStoryPanel;

import java.awt.*;
import java.awt.event.*;

public class MapLevel_Icons extends JButton {
	
    //GLOBAL VARIABLE: LEVEL CONDITIONAL ==============================
	LevelConditional levelCond = LevelConditional.GLOBAL;
    
	
    private boolean unlocked;

    private Image lockedImg;
    private Image normalImg;
    private Image hoverImg;

    // PRE-SCALED (cached) images
    private Image scaledLocked;
    private Image scaledNormal;
    private Image scaledHover;

    private SoundEffectsPlayer music = new SoundEffectsPlayer();
   
    
    // animation
    private int originalY;
    private int offset = 0;
    private int direction = 1;
    private final int speed = 1;
    private final int range = 7;
    private Timer animationTimer;

    private boolean isHovered = false;

    public MapLevel_Icons(String lockedPath, String normalPath, String hoverPath, boolean unlocked) {

        this.unlocked = unlocked;
        
        lockedImg = new ImageIcon(getClass().getResource(lockedPath)).getImage();
        normalImg = new ImageIcon(getClass().getResource(normalPath)).getImage();
        hoverImg  = new ImageIcon(getClass().getResource(hoverPath)).getImage();

        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ------------------------------------------
        // HOVER + CLICK LISTENERS
        // ------------------------------------------
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!MapLevel_Icons.this.unlocked) return;

                isHovered = true;
                music.playSound("/SoundEffects/02_Click_Hover.wav");

                setIcon(new ImageIcon(scaledHover));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                updateScaledIcon();
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!unlocked) return;
                music.playSound("/SoundEffects/02_Click_Hover.wav");
                System.out.println("Button Clicked!");

            	 //CALL DIM OVERLAY WWEHEEHEHHEEHE

                if (levelCond.getLevelState(1) == 1) {
                    // turn ON dim
                	JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapLevel_Icons.this);
                	mainFrame.getGlassPane().setVisible(true);
                    
                	CardPanel panel = new CardPanel(mainFrame, null);
                    new CardFrame(panel); // show popup
                    

                }

                else if(levelCond.getLevelState(2) == 1) {
                	
                	  JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapLevel_Icons.this);
					  mainFrame.getGlassPane().setVisible(true);
	                  
					  CardPanel panel = new CardPanel(mainFrame, null);
	              	  new CardFrame(panel);
                }
              
                else if(levelCond.getLevelState(3) == 1) {
              	  	  JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapLevel_Icons.this);
					  mainFrame.getGlassPane().setVisible(true);
	                  
					  CardPanel panel = new CardPanel(mainFrame, null);
	              	  new CardFrame(panel);
                  }
                
                else if(levelCond.getLevelState(4) == 1) {
              	  	  JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapLevel_Icons.this);
					  mainFrame.getGlassPane().setVisible(true);
	                  
					  CardPanel panel = new CardPanel(mainFrame, null);
	              	  new CardFrame(panel);
                  }
                
                else if(levelCond.getLevelState(5) == 1) {
              	  	  JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(MapLevel_Icons.this);
					  mainFrame.getGlassPane().setVisible(true);
	                  
					  CardPanel panel = new CardPanel(mainFrame, null);
	              	  new CardFrame(panel);
                  }
            }
            
        });
    }

    // -------------------------------------------------------
    // FLOATING ANIMATION (SMOOTH, NO LAG)
    // -------------------------------------------------------
    public void enableFloating() {
        if (!unlocked) return;

        originalY = getY();

        animationTimer = new Timer(16, e -> {

            offset += direction * speed;

            if (offset > range || offset < -range) {
                direction *= -1;
            }

         
            setLocation(getX(), originalY + offset);

        
            if (isHovered) {
                setIcon(new ImageIcon(scaledHover));
            }

        });

        animationTimer.start();
    }

    
    // -------------------------------------------------------
    // ICON SCALING (ONLY DONE ONCE, NO LAG)
    // -------------------------------------------------------
    private void updateScaledIcon() {

        if (scaledLocked == null || scaledNormal == null || scaledHover == null) {
            return; // still initializing
        }

        if (isHovered && unlocked) {
            setIcon(new ImageIcon(scaledHover));
            return;
        }

        Image base = unlocked ? scaledNormal : scaledLocked;
        setIcon(new ImageIcon(base));
    }

    // PRE-SCALE IMAGES WHEN SIZE CHANGES
    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);

        if (w > 0 && h > 0) {
            scaledLocked = lockedImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            scaledNormal = normalImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            scaledHover  = hoverImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
        }

        updateScaledIcon();
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
        updateScaledIcon();
    }
}
