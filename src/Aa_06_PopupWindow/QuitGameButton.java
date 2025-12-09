package Aa_06_PopupWindow;

import javax.swing.*;

import Aa_01_BackgroundMusic.SoundEffectsPlayer;

import java.awt.*;
import java.awt.event.*;

public class QuitGameButton extends JPanel {

    private JButton stay_Game;
    private JButton quit_Game;
    private JButton close_Window;
    
    
    private SoundEffectsPlayer getFX = new SoundEffectsPlayer();
    
    public QuitGameButton(ActionListener stayGame, ActionListener QuitGame, ActionListener closePopUp) {
        setLayout(null);
        setOpaque(false); // transparent panel
      
        // =======================================================        
        // Create and position buttons 
        // Card Buttons for Game Level
        // =======================================================   
        
       quit_Game = createImageButton(
        		"/GameInfo/QuitGame.png",
        		

        		"/GameInfo/QuitGame_Hover.png",
                58, 338,   // x and y position
                146, 56,   //width and height  / 222x84
                stayGame
        ); 
        
       stay_Game = createImageButton(
        		"/GameInfo/Stay.png",
        		"/GameInfo/Stay_Hover.png",
            	220, 338,  // x and y position
            	146, 56,  //width and height
                QuitGame
          );
          
        // =======================================================        
        // Create and position buttons 
        // Game Info Buttons
        // =======================================================   
        
        close_Window = createImageButton(
                "/GameInfo/CloseQuit.png",
                "/GameInfo/CloseQuit_Hover.png",
                358, 30,   // x and y position
                32, 32,  //width and height
                closePopUp
        );

        
        add(stay_Game);
        add(quit_Game);
        add(close_Window);

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
