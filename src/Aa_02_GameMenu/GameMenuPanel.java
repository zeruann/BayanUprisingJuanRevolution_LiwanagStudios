package Aa_02_GameMenu;

import javax.imageio.ImageIO;
import javax.swing.*;

import Aa_01_BackgroundMusic.MusicPlayer;
import Aa_01_BackgroundMusic.SoundEffectsPlayer;
import Aa_03_GameMap.MapFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public class GameMenuPanel extends JPanel {
    private final int WIDTH = 945;
    private final int HEIGHT =575;
    private BufferedImage bgImage;
    
    //BG Music and SoundFX
    private MusicPlayer getMusic = new  MusicPlayer();
    private SoundEffectsPlayer getFX = new SoundEffectsPlayer();
    
    
    private GameMenuButtons buttonsPanel; // call GameMenuButtons

    //constructor
    public GameMenuPanel() {
        this(null);
    }
    
    public MusicPlayer getGetMusic() {
		return getMusic;
	}

	public GameMenuPanel(String imagePath) {
    	
    	//play music
    	getMusic.playMusic("/BackgroundMusic/02_Map and MainMenu.wav", true);
        setOpaque(false); // for tranaprent frame
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        
    
        if (imagePath != null) {
            try {
              
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is != null) {
                    bgImage = ImageIO.read(is);
                } else {
                    bgImage = ImageIO.read(new java.io.File(imagePath));
                }
            } catch (IOException ex) {
                System.err.println("Background image load failed: " + ex.getMessage());
                bgImage = null;
            }
        }
        
        
        // =====================================================================================
        // Create button panel and add to this panel (GameMenuPanel        
        
        buttonsPanel = new GameMenuButtons(
            e -> {
            	
            	MusicPlayer.stop(); // stop current music
            	//getMusic.playMusic("/BackgroundMusic/02_Map and MainMenu.wav", true);
            	((JFrame) SwingUtilities.getWindowAncestor(this)).dispose(); // close menu
            	MapFrame map = new MapFrame(); // Call Game Map
            	map.setVisible(true);
            },
            
            e -> {
            	System.exit(0); 
            }
        );
        
        buttonsPanel.setBounds(0, 0, 945, 575);
        add(buttonsPanel);

    }


    @Override
    protected void paintComponent(Graphics g) {
        // Paint rounded rectangle
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable high-quality rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();


        // Draw and Scale image
        if (bgImage != null) {
            // scale preserving aspect ratio (cover)
            double imgW = bgImage.getWidth();
            double imgH = bgImage.getHeight();

            double scale = Math.max((double) w / imgW, (double) h / imgH);
            int newW = (int) Math.round(imgW * scale);
            int newH = (int) Math.round(imgH * scale);
            int x = (w - newW) / 2;
            int y = (h - newH) / 2;

            g2.drawImage(bgImage, x, y, newW, newH, this);
        } else {
            // Fallback gradient background (if image missing)
            GradientPaint gp = new GradientPaint(0, 0, new Color(40,40,40), 0, h, new Color(15,15,15));
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
        }

       
        super.paintComponent(g);  
    }
    
    
}
