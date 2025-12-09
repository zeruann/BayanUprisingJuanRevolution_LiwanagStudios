package Aa_04_GEnemyCard;

import javax.imageio.ImageIO;
import javax.swing.*;

import Aa_00_GlobalVariable.GameLevelAttempt;
import Aa_00_GlobalVariable.LevelConditional;
import Aa_01_BackgroundMusic.MusicPlayer;
import Aa_01_BackgroundMusic.SoundEffectsPlayer;
import Aa_03_GameMap.MapFrame;
import Aa_05_GGameInfo.StoryList;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import CG_01_Main.Game;
import CG_01_Main.GamePanel;
import CG_01_Main.GameWindow;
import main.PlatformerGame;

public class CardPanel extends JPanel implements Runnable {
    private final int WIDTH = 651;
    private final int HEIGHT = 412;
    private BufferedImage bgImage;
    private JFrame mainFrame;
    
    private StoryList story;
    private SoundEffectsPlayer getFX = new SoundEffectsPlayer();
    
    private CardButtons buttonsPanel;
    private Thread thread;
    private EnemySprite getSprite;
    private GamePanel gamePanel;

    LevelConditional levelCond = LevelConditional.GLOBAL;
    GameLevelAttempt levelAttempt = GameLevelAttempt.GLOBAL;
    
    // DEBUG
    private StringBuilder debugLog = new StringBuilder();
    
    public CardPanel() {
        this(null, null);
    }
    
    public CardPanel(JFrame mainFrame, String imagePath) {    	
        this.mainFrame = mainFrame;
        setOpaque(false);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);
        
        debug("=== CardPanel Initialization ===");
        debug("Level 1: " + levelCond.getLevelState(1));
        debug("Level 2: " + levelCond.getLevelState(2));
        debug("Level 3: " + levelCond.getLevelState(3));
        debug("Level 4: " + levelCond.getLevelState(4));
        debug("Level 5: " + levelCond.getLevelState(5));

        // Determine which level and load correct resources
        if (levelCond.getLevelState(1) == 1) {
            imagePath = "/Map_EnemyCard/Level 1_Card.png";
            debug("Level 1 detected");
        }
        else if(levelCond.getLevelState(2) == 1) {
            imagePath = "/Map_EnemyCard/Level 2_Card.png";
            debug("Level 2 detected");
        }
        else if(levelCond.getLevelState(3) == 1) {
            imagePath = "/Map_EnemyCard/Level 3_Card.png";
            debug("Level 3 detected");
        }
        else if(levelCond.getLevelState(4) == 1) {
            imagePath = "/Map_EnemyCard/Level 4_Card.png";
            debug("Level 4 detected");
        }
        else if(levelCond.getLevelState(5) == 1) {
            imagePath = "/Map_EnemyCard/Level 5_Card.png";
            debug("Level 5 detected");
        }
        
        // Load background image
        loadBackgroundImage(imagePath);
        
        // Load sprite animation
        loadSpriteAnimation();
        
        // Start animation thread
        thread = new Thread(this);
        thread.start();
        
        // Setup buttons
        setupButtons();
        buttonsPanel.setBounds(0, 0, 651, 412);
        add(buttonsPanel);
        
        printDebugLog();
    }

    // ============================================================
    // DEBUG METHOD
    // ============================================================
    private void debug(String msg) {
        String logMsg = "[CardPanel] " + msg;
        System.out.println(logMsg);
        debugLog.append(logMsg).append("\n");
    }

    private void printDebugLog() {
        System.out.println("\n" + debugLog.toString());
        try {
            java.nio.file.Files.write(
                java.nio.file.Paths.get(System.getProperty("user.home"), "CardPanel_debug.txt"),
                debugLog.toString().getBytes()
            );
            debug("Debug log written to: " + System.getProperty("user.home") + "/CardPanel_debug.txt");
        } catch (Exception e) {
            debug("Could not write debug file: " + e.getMessage());
        }
    }

    // ============================================================
    // LOAD BACKGROUND IMAGE
    // ============================================================
    private void loadBackgroundImage(String imagePath) {
        if (imagePath == null) {
            debug("✗ Background image path is null");
            return;
        }

        debug("Loading background: " + imagePath);
        
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                bgImage = ImageIO.read(is);
                if (bgImage != null) {
                    debug("✓ Background loaded from resources");
                } else {
                    debug("✗ Background read returned null");
                }
            } else {
                debug("✗ Resource stream is null for: " + imagePath);
                // Try alternative loading methods
                tryAlternativeLoad(imagePath);
            }
        } catch (IOException ex) {
            debug("✗ IOException loading background: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void tryAlternativeLoad(String imagePath) {
        try {
            InputStream is = ClassLoader.getSystemResourceAsStream(imagePath);
            if (is != null) {
                bgImage = ImageIO.read(is);
                debug("✓ Background loaded using ClassLoader");
            } else {
                debug("✗ ClassLoader also failed for: " + imagePath);
            }
        } catch (Exception e) {
            debug("✗ Alternative load failed: " + e.getMessage());
        }
    }

    // ============================================================
    // LOAD SPRITE ANIMATION
    // ============================================================
    private void loadSpriteAnimation() {
        String spriteSourceURL = null;
        
        if(levelCond.getLevelState(1) == 1) {
            spriteSourceURL = "/EnemySprite_Cards/Mouse_Idle.png";
        }
        else if(levelCond.getLevelState(2) == 1) {
            spriteSourceURL = "/EnemySprite_Cards/Pig_Idle.png";
        }
        else if(levelCond.getLevelState(3) == 1) {
            spriteSourceURL = "/EnemySprite_Cards/Rat_Idle.png";
        }
        else if(levelCond.getLevelState(4) == 1) {
            spriteSourceURL = "/EnemySprite_Cards/Boar_Idle.png";
        }
        else if(levelCond.getLevelState(5) == 1) {
            spriteSourceURL = "/EnemySprite_Cards/Croc_Idle.png";
        }
        
        if (spriteSourceURL == null) {
            debug("✗ Sprite source URL is null");
            return;
        }

        debug("Loading sprite: " + spriteSourceURL);
        
        try (InputStream is = getClass().getResourceAsStream(spriteSourceURL)) {
            if (is == null) {
                debug("✗ Sprite resource stream is null: " + spriteSourceURL);
                return;
            }
            
            BufferedImage sheet = ImageIO.read(is);
            if (sheet != null) {
                getSprite = new EnemySprite(sheet, 64, 64, 2, 27, 50);
                debug("✓ Sprite loaded successfully");
            } else {
                debug("✗ Sprite image read returned null");
            }
        } catch (Exception e) {
            debug("✗ Exception loading sprite: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================================================
    // SETUP BUTTONS
    // ============================================================
    private void setupButtons() {
        buttonsPanel = new CardButtons(
            e -> {
                debug("START button clicked");
                MusicPlayer.stop();

                // CLOSE POPUP WINDOW
                Window popup = SwingUtilities.getWindowAncestor(this);
                if (popup != null) popup.dispose();

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
                else if(levelCond.getLevelState(2) == 1) {
                    if(levelAttempt.getLevelAttempt()==0) levelAttempt.setLevelAttempt(1);
                    if(levelAttempt.getLevelAttempt()==1) levelAttempt.setLevelAttempt(2);
                    System.out.println("Attempt: " + levelAttempt.getLevelAttempt());
                }
                else if(levelCond.getLevelState(3) == 1) {
                    if(levelAttempt.getLevelAttempt()==0) levelAttempt.setLevelAttempt(1);
                    if(levelAttempt.getLevelAttempt()==1) levelAttempt.setLevelAttempt(2);
                    System.out.println("Attempt: " + levelAttempt.getLevelAttempt());
                }
                else if(levelCond.getLevelState(4) == 1) {
                    if(levelAttempt.getLevelAttempt()==0) levelAttempt.setLevelAttempt(1);
                    if(levelAttempt.getLevelAttempt()==1) levelAttempt.setLevelAttempt(2);
                    System.out.println("Attempt: " + levelAttempt.getLevelAttempt());
                }
                else if(levelCond.getLevelState(5) == 1) {
                    if(levelAttempt.getLevelAttempt()==0) levelAttempt.setLevelAttempt(1);
                    if(levelAttempt.getLevelAttempt()==1) levelAttempt.setLevelAttempt(2);
                    System.out.println("Attempt: " + levelAttempt.getLevelAttempt());
                }

                new PlatformerGame();
            },

            e -> {
                debug("BACK button clicked");
                if (mainFrame != null) {
                    mainFrame.getGlassPane().setVisible(false);
                }
                Window popup = SwingUtilities.getWindowAncestor(CardPanel.this);
                if (popup != null) popup.dispose();
            }
        );
    }

    // ============================================================
    // PAINT COMPONENT
    // ============================================================
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable high-quality rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        // Draw background image
        if (bgImage != null) {
            double imgW = bgImage.getWidth();
            double imgH = bgImage.getHeight();
            double scale = Math.max((double) w / imgW, (double) h / imgH);
            int newW = (int) Math.round(imgW * scale);
            int newH = (int) Math.round(imgH * scale);
            int x = (w - newW) / 2;
            int y = (h - newH) / 2;
            g2.drawImage(bgImage, x, y, newW, newH, this);
        } else {
            // Fallback gradient (image missing)
            GradientPaint gp = new GradientPaint(0, 0, new Color(40,40,40), 0, h, new Color(15,15,15));
            g2.setPaint(gp);
            g2.fillRect(0, 0, w, h);
        }

        super.paintComponent(g);  
        
        if (getSprite != null) {
            getSprite.draw(g2);
        }

        g2.dispose();
    }
    
    // ============================================================
    // ANIMATION THREAD
    // ============================================================
    @Override
    public void run() {
        while (true) {
            if (getSprite != null) {
                getSprite.update();
            }
            repaint();
            try { 
                Thread.sleep(16); 
            } catch (Exception e) {}
        }
    }
}