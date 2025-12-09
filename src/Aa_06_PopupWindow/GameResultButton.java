package Aa_06_PopupWindow;

import javax.swing.*;

import Aa_00_GlobalVariable.GameResult_WinLose;

import java.awt.*;
import java.awt.event.*;
import Aa_01_BackgroundMusic.SoundEffectsPlayer;

public class GameResultButton extends JPanel {
	
    private JButton RetryGame;
    private JButton backToMap;   
    
    GameResult_WinLose result = GameResult_WinLose.GLOBAL;
    
    private SoundEffectsPlayer getFX = new SoundEffectsPlayer();
    
   
    public GameResultButton(ActionListener retryGame, ActionListener goToMap) {
        setLayout(null);
        setOpaque(false); // transparent panel
      
        // =======================================================        
        // Create and position buttons 
        // Card Buttons for Game Level
        // =======================================================   
        //result.setGameResult(2);
       
        
        if(result.getGameResult()==1) {
        	//WIN
        	
        		RetryGame = createImageButton(
                    "/Map_EnemyCard/PlayButton_Platformer.png",
                    "/Map_EnemyCard/PlayButton_Platformer-Hover.png",
                    1, 1,   // x and y position
                    1, 1,  //width and height
                    retryGame
            ); 
            
            backToMap = createImageButton(
                	"/BattleResult/WinBTN_Normal.png",
                	"/BattleResult/WinBTN-Hover.png",
                	95, 485,  // x and y position
                	245, 54 ,  //width and height
                    goToMap
              );
            
            add(backToMap);
        }
        
        else {
        		//LOSE
            RetryGame = createImageButton(
            		"/BattleResult/Retry.png",
                	"/BattleResult/Retry_Hover.png",
                	70, 485,   // x and y position
                    142, 54,  //width and height
                    retryGame
            ); 
            
            backToMap = createImageButton(
            		"/BattleResult/ReturnToMap.png",
                	"/BattleResult/ReturnToMap_Hover.png",
                	225, 485,  // x and y position
                	142, 54,  //width and height
                    goToMap
              );

            add(RetryGame);
            add(backToMap);
        }

    }
    

    private JButton createImageButton(String normalPath, String hoverPath, int x, int y, int width, int height, ActionListener action) {
        // Load and scale images
        ImageIcon normalIcon = new ImageIcon(getClass().getResource(normalPath));
        ImageIcon hoverIcon = new ImageIcon(getClass().getResource(hoverPath));

        // image scale
        Image normalImg = normalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Image hoverImg = hoverIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        JButton button = new JButton(new ImageIcon(normalImg));
        button.setBounds(x, y, width, height);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	getFX.playSound("/SoundEffects/01_Hover.wav");
                button.setIcon(new ImageIcon(hoverImg));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(new ImageIcon(normalImg));
            }
        });
        
        //button click effect
        button.addActionListener(e->{
        	getFX.playSound("/SoundEffects/02_Click_Hover.wav");
        }); 

        button.addActionListener(action);
        return button;
    }

}
