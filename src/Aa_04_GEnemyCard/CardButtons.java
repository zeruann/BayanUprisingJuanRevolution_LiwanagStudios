package Aa_04_GEnemyCard;

import javax.imageio.ImageIO;
import javax.swing.*;

import Aa_01_BackgroundMusic.SoundEffectsPlayer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class CardButtons extends JPanel {

    private JButton playGame_EnemyCard;
    private JButton close_EnemyCard;
    
    private JButton storyButton;
    private JButton cardButton;
    private JButton characterButton;
    
    private SoundEffectsPlayer getFX = new SoundEffectsPlayer();
    
    public CardButtons(ActionListener playAction, ActionListener exitAction) {
        setLayout(null);
        setOpaque(false); // transparent panel
      
        // =======================================================        
        // Create and position buttons 
        // Card Buttons for Game Level
        // =======================================================   
        
        playGame_EnemyCard = createImageButton(
                "/Map_EnemyCard/PlayButton_Platformer.png",
                "/Map_EnemyCard/PlayButton_Platformer-Hover.png",
                320, 315,   // x and y position
                291, 59,  //width and height
                playAction
        ); 
        
        close_EnemyCard = createImageButton(
            	"/Map_EnemyCard/CloseButton_Platformer.png",
            	"/Map_EnemyCard/CloseButton_Platformer-Hover.png",
            	590, 30,  // x and y position
                35, 33,  //width and height
                exitAction
          );
          
        // =======================================================        
        // Create and position buttons 
        // Game Info Buttons
        // =======================================================   
        
        storyButton = createImageButton(
                "/GameInfo/StoryIcon_Normal.png",
                "/GameInfo/StoryIcon_Hover.png",
                320, 315,   // x and y position
                214, 89,  //width and height
                playAction
        );
        

        cardButton = createImageButton(
                "/GameInfo/CharIcon_Normal.png",
                "/GameInfo/CharIcon_Hover.png",
                320, 315,   // x and y position
                214, 89,  //width and height
                playAction
        );
        
        
        characterButton = createImageButton(
                "/GameInfo/CardIcon_Normal.png",
                "/GameInfo/CardIcon_Hover.png",
                320, 315,   // x and y position
                214, 89, //width and height
                playAction
        );
        
        add(playGame_EnemyCard);
        add(close_EnemyCard);
    }
    

    private JButton createImageButton(String normalPath, String hoverPath, int x, int y, int width, int height, ActionListener action) {
        
        // Load images safely with error handling
        BufferedImage normalImg = loadImage(normalPath);
        BufferedImage hoverImg = loadImage(hoverPath);
        
        // Check if images loaded successfully
        if (normalImg == null) {
            System.err.println("[CardButtons] Failed to load: " + normalPath);
            // Create a fallback button
            JButton button = new JButton("Button");
            button.setBounds(x, y, width, height);
            return button;
        }
        
        if (hoverImg == null) {
            System.err.println("[CardButtons] Failed to load: " + hoverPath);
            hoverImg = normalImg; // Use normal image as fallback
        }

        // Scale images
        Image scaledNormal = normalImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Image scaledHover = hoverImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        JButton button = new JButton(new ImageIcon(scaledNormal));
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
                button.setIcon(new ImageIcon(scaledHover));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(new ImageIcon(scaledNormal));
            }
        });
        
        // Button click effect
        button.addActionListener(e -> {
            getFX.playSound("/SoundEffects/02_Click_Hover.wav");
        }); 

        button.addActionListener(action);
        return button;
    }
    
    // ============================================================
    // Safe image loading method
    // ============================================================
    private BufferedImage loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("[CardButtons] InputStream is null for: " + path);
                return null;
            }
            
            BufferedImage img = ImageIO.read(is);
            if (img == null) {
                System.err.println("[CardButtons] ImageIO.read returned null for: " + path);
                return null;
            }
            
            System.out.println("[CardButtons] ✓ Loaded: " + path);
            return img;
            
        } catch (Exception e) {
            System.err.println("[CardButtons] Exception loading " + path + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}