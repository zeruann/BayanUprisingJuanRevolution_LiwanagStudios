package Aa_03_GameMap;

import javax.swing.*;

import Aa_01_BackgroundMusic.SoundEffectsPlayer;

import java.awt.*;
import java.awt.event.*;

public class GameInfo_Buttons extends JPanel  {
    private JButton storyButton;
    private JButton cardButton;
    private JButton characterButton;
    private JButton quitButton;


    private SoundEffectsPlayer getFX = new SoundEffectsPlayer();
    
	public GameInfo_Buttons(ActionListener storyAction, ActionListener showCard, ActionListener showChar, ActionListener showQuit){
		
	  setLayout(null);
      setOpaque(false); // transparent panel
    
      // =======================================================        
      // Create and position buttons 
      // Game Info Buttons
      // =======================================================   
      
      storyButton = createImageButton(
              "/GameInfo/StoryIcon_Normal.png",
              "/GameInfo/StoryIcon_Hover.png",
              20, 20,   // x and y position
              107, 47,   //width and height
              storyAction
      );
      

      cardButton = createImageButton(
              "/GameInfo/CharIcon_Normal.png",
              "/GameInfo/CharIcon_Hover.png",
              20, 80,   // x and y position
              107, 47,  //width and height //100 × 42
              showCard
      );
      
      
      characterButton = createImageButton(
              "/GameInfo/CardIcon_Normal.png",
              "/GameInfo/CardIcon_Hover.png",
              20, 140,   // x and y position
              107, 47,  //width and height
              showChar
      );
      
      quitButton = createImageButton(
              "/GameInfo/Quit.png",
              "/GameInfo/Quit-Hover.png",
              20, 200,   // x and y position
              107, 47,  //width and height
              showQuit
      );
      
      
      add(cardButton);
      add(characterButton);
      add(storyButton);
      add(quitButton);
      
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