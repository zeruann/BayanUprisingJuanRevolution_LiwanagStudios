package Aa_02_GameMenu;

import javax.swing.*;

import Aa_01_BackgroundMusic.SoundEffectsPlayer;

import java.awt.*;
import java.awt.event.*;

public class GameMenuButtons extends JPanel {

    private JButton playButton;
    private JButton exitButton;
    private JButton closeButton;
    private JButton playGame_EnemyCard;
    private JButton close_EnemyCard;
    private SoundEffectsPlayer getFX = new SoundEffectsPlayer();
    
    public GameMenuButtons(ActionListener playAction, ActionListener exitAction) {
        setLayout(null);
        setOpaque(false); // transparent panel
      
        
        // Create and position buttons
        playButton = createImageButton(
            "/GameMenu_Assets/PlayButton.png",
            "/GameMenu_Assets/PlayButton-Hover.png",
            320, 410,   // x and y position
            305, 65, //width and height
            playAction
        );

        exitButton = createImageButton(
        	"/GameMenu_Assets/BTN_Exit.png",
            "/GameMenu_Assets/BTN_Exit_Hover.png",
        	705, 420,  // x and y position
            146, 69,  //width and height
            exitAction
        );
        
        closeButton = createImageButton(
        	"/GameMenu_Assets/CloseButton_Platformer.png",
        	"/GameMenu_Assets/CloseButton_Platformer-Hover.png",
            867, 11,
            32, 32,
            exitAction
        );

        
        add(closeButton);
        add(playButton);
        //add(exitButton);

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
