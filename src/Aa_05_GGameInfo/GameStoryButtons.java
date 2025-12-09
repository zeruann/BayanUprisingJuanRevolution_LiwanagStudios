package Aa_05_GGameInfo;

import javax.swing.*;

import Aa_01_BackgroundMusic.SoundEffectsPlayer;

import java.awt.*;
import java.awt.event.*;

public class GameStoryButtons extends JPanel{
	 	private JButton NextButton;
	    private JButton PrevButton;
	    private JButton CloseButton; 
	   
	    
	    private SoundEffectsPlayer getFX = new SoundEffectsPlayer();
	    
	    public GameStoryButtons(ActionListener next, ActionListener prev, ActionListener close) {
	        setLayout(null);
	        setOpaque(false); // transparent panel
	      
	        // =======================================================        
	        // Create and position buttons 
	        // Card Buttons for Game Level
	        // =======================================================   
	        
	        //PANEL WIDTH 756
	        NextButton = createImageButton(
	                "/GameInfo/Next.png",
	                "/GameInfo/Next -Hover.png",
	                607, 353,  // x and y position
	            	129, 52,   //width and height
	                next
	        ); 
	        
	        PrevButton = createImageButton(
	        		"/GameInfo/Prev.png",
	                "/GameInfo/Prev - Hover.png",
	                465, 353,  // x and y position
	                129, 52,    //width and height
	                prev
	          );
	          
	        CloseButton = createImageButton(
	        		"/GameInfo/CloseButton.png",
	                "/GameInfo/CloseButton_Hover.png",
	                697, 75,   // x and y position
	                38, 38,   //width and height
	                close
	        );
	       
	        add(NextButton);
	        add(PrevButton);
	        add(CloseButton);   
	        
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
