package Aa_05_GGameInfo;

import javax.imageio.ImageIO;
import javax.swing.*;

import Aa_00_GlobalVariable.GameInfo_Condition;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;

public class GameStoryPanel extends JPanel implements Runnable {

    public int WIDTH = 790;
    public int HEIGHT = 480;
    public int clicked;
    
    private JLabel imageLabel;
    private JFrame mainFrame;
    private BufferedImage bgImage;
   
    private StoryList story;
    private GameStoryButtons buttons;

    private JPanel animationPanel;
    private ArrayList<JPanel> hitPanels = new ArrayList<>();

    private Thread thread;
    private ArrayList<DisplayAnimation> enemyAnimations = new ArrayList<>();
    private int gameInfoMode = GameInfo_Condition.GLOBAL.getInfoButtonState();

    // DEBUG
    private StringBuilder debugLog = new StringBuilder();

    public GameStoryPanel() {
        this(null, null);
    }

    public GameStoryPanel(JFrame mainFrame, String bgPath) {
        this.mainFrame = mainFrame;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setOpaque(false);
        setLayout(null);

        debug("=== GameStoryPanel Initialization ===");
        debug("Game Info Mode: " + gameInfoMode);
        debug("Background Path: " + bgPath);

        loadBackground(bgPath);
        setupStory();
        loadAllSpriteAnimations();
        setupImageLabel();
        setupButtons();
        add(buttons);

        showAnimationOverlay();
    
        setComponentZOrder(animationPanel, 0);
        setComponentZOrder(buttons, 0);
        startThread();

        printDebugLog();
    }

    // ============================================================
    // DEBUG METHOD
    // ============================================================
    private void debug(String msg) {
        String logMsg = "[DEBUG] " + msg;
        System.out.println(logMsg);
        debugLog.append(logMsg).append("\n");
    }

    private void printDebugLog() {
        System.out.println("\n" + debugLog.toString());
        // Also try to write to a file for exe debugging
        try {
            java.nio.file.Files.write(
                java.nio.file.Paths.get(System.getProperty("user.home"), "GameStoryPanel_debug.txt"),
                debugLog.toString().getBytes()
            );
            debug("Debug log written to: " + System.getProperty("user.home") + "/GameStoryPanel_debug.txt");
        } catch (Exception e) {
            debug("Could not write debug file: " + e.getMessage());
        }
    }

    // ============================================================
    private void loadBackground(String path) {
        if (path == null) {
            debug("Background path is null, skipping");
            return;
        }
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                bgImage = ImageIO.read(is);
                debug("✓ Background loaded: " + path);
            } else {
                debug("✗ Background InputStream is null: " + path);
                // Try alternative loading method
                tryAlternativeLoad(path);
            }
        } catch (Exception e) {
            debug("✗ Background failed: " + path + " | Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void tryAlternativeLoad(String path) {
        try {
            InputStream is = ClassLoader.getSystemResourceAsStream(path);
            if (is != null) {
                bgImage = ImageIO.read(is);
                debug("✓ Background loaded (alternative method): " + path);
            } else {
                debug("✗ Alternative load failed for: " + path);
            }
        } catch (Exception e) {
            debug("✗ Alternative load exception: " + e.getMessage());
        }
    }

    // ============================================================
    private void showAnimationOverlay() {
        debug("showAnimationOverlay() called");
        hideAnimationOverlay();

        animationPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int x = 18, y = 0;
                for (DisplayAnimation anim : enemyAnimations) {
                    anim.draw(g, x, y);
                    x += 140;
                }
            }
        };
        
        int HEIGHT = 178;
        int yPosition = 130;
        if(gameInfoMode==3) {yPosition = 127; HEIGHT = 215;}
        	
        animationPanel.setBounds(0, yPosition, WIDTH, HEIGHT);
        animationPanel.setOpaque(false);
        add(animationPanel);

        createHitBoxes();
        revalidate();
        repaint();
        debug("Animation overlay shown");
    }

    private void createHitBoxes() {
        debug("Creating " + enemyAnimations.size() + " hitboxes");
        hitPanels.clear();
        int x = 60, y = 150;

        for (int i = 0; i < enemyAnimations.size(); i++) {
            JPanel hit = new JPanel();
            hit.setBackground(new Color(0, 0, 0, 20));
            hit.setOpaque(true);
            hit.setBounds(x, y, 100, 190);

            final int index = i;
            hit.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    onSpriteClicked(index);
                }
            });

            add(hit);
            hitPanels.add(hit);
            x += 140;
        }
        debug("✓ Hitboxes created");
    }

    private void hideAnimationOverlay() {
        if (animationPanel != null) {
            remove(animationPanel);
            animationPanel = null;
        }
        for (JPanel hit : hitPanels) remove(hit);
        hitPanels.clear();
        revalidate();
        repaint();
        debug("Animation overlay hidden");
    }

    private void onSpriteClicked(int index) {
        debug("Clicked sprite: " + index);
    }

    // ============================================================
    private void loadAllSpriteAnimations() {
        debug("loadAllSpriteAnimations() called, gameInfoMode=" + gameInfoMode);
        
        enemyAnimations.clear();
        String[] paths = null;
        
        if(gameInfoMode==1) {
            paths = new String[] {};
        } else if(gameInfoMode==2) {
            paths = new String[] {
                "/CharacterSprites_Cards/Juan_Idle.png"
            };
        } else if(gameInfoMode==3) {
            paths = new String[] {};
        }

        if (paths == null) {
            debug("✗ No paths configured for gameInfoMode " + gameInfoMode);
            return;
        }

        debug("Loading " + paths.length + " sprite animations");
   
        for (String path : paths) {
            try (InputStream is = getClass().getResourceAsStream(path)) {

                if (is == null) {
                    debug("✗ Missing sprite (null InputStream): " + path);
                    continue;
                }

                BufferedImage sheet = ImageIO.read(is);
                if (sheet == null) {
                    debug("✗ Failed to read image: " + path);
                    continue;
                }

                int spriteFrames = 2;
                enemyAnimations.add(new DisplayAnimation(sheet, 64, 64, spriteFrames, 0, 0));
                debug("✓ Loaded sprite: " + path);

            } catch (Exception e) {
                debug("✗ Exception loading " + path + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        debug("Sprite loading complete. Total loaded: " + enemyAnimations.size());
    }

    // ============================================================
    private void setupStory() {
        debug("setupStory() called, gameInfoMode=" + gameInfoMode);
        
        story = new StoryList();

        if (gameInfoMode == 1) {
            story.addImage("/GameInfo/StoryPanel1.png");
            story.addImage("/GameInfo/StoryPanel2.png");
            story.addImage("/GameInfo/StoryPanel3.png");
            debug("Story mode 1 setup");

        } else if (gameInfoMode == 2) {
            story.addImage("/GameInfo/CharPanel1.png");
            story.addImage("/GameInfo/CharPanel2.png");
            debug("Story mode 2 setup");

        } else if (gameInfoMode == 3) {
            story.addImage("/GameInfo/CardPanel1.png");
            story.addImage("/GameInfo/CardPanel3.png");
            story.addImage("/GameInfo/CardPanel2.png");
            story.addImage("/GameInfo/CardPanel4.png");
            story.addImage("/GameInfo/CardPanel5.png");
            story.addImage("/GameInfo/CardPanel6.png");
            debug("Story mode 3 setup");
        }
    }

    // ============================================================
    private void setupImageLabel() {
        debug("setupImageLabel() called");
        
        imageLabel = new JLabel();
        imageLabel.setBounds(0, 0, WIDTH, HEIGHT);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(imageLabel);
        updateImage(story.getCurrent());
        
        debug("✓ Image label setup complete");
    }

    // ============================================================
    private void setupButtons() {
        debug("setupButtons() called");
        
        buttons = new GameStoryButtons(
                e -> {
                    debug("NEXT button clicked");
                    if (gameInfoMode == 2) hideAnimationOverlay();
                    if (gameInfoMode == 3) {
                        if(clicked<5)clicked++;
                        hideAnimationOverlay();
                    }
                    updateImage(story.nextImage());
                },

                e -> {
                    debug("PREVIOUS button clicked");
                    if (gameInfoMode == 2) {
                        showAnimationOverlay();
                        setComponentZOrder(animationPanel, 0);
                        setComponentZOrder(buttons, 1);
                    }
                    if(gameInfoMode == 3) {
                        if (clicked>1) {
                            hideAnimationOverlay();
                            clicked--;  
                        } else {
                            showAnimationOverlay();
                            setComponentZOrder(animationPanel, 0);
                            setComponentZOrder(buttons, 1);
                        }
                    }
                    updateImage(story.prevImage());
                },

                e -> {
                    debug("CLOSE button clicked");
                    closePanel();
                }
        );

        buttons.setBounds(0, 0, WIDTH, HEIGHT);
        buttons.setOpaque(false);
        debug("✓ Buttons setup complete");
    }

    // ============================================================
    private void closePanel() {
        debug("closePanel() called");
        if (mainFrame != null)
            mainFrame.getGlassPane().setVisible(false);

        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
    }

    // ============================================================
    private void updateImage(String path) {
        if (path == null) {
            debug("✗ updateImage() called with null path");
            return;
        }

        debug("Loading image: " + path);
        
        try (InputStream is = getClass().getResourceAsStream(path)) {

            if (is == null) {
                debug("✗ Image InputStream is null: " + path);
                return;
            }

            BufferedImage img = ImageIO.read(is);
            if (img == null) {
                debug("✗ Failed to read image: " + path);
                return;
            }

            Image scaled = img.getScaledInstance(WIDTH - 40, HEIGHT - 80, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
            debug("✓ Image loaded: " + path);

        } catch (Exception e) {
            debug("✗ Failed to load image: " + path + " | Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ============================================================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null)
            g.drawImage(bgImage, 0, 0, WIDTH, HEIGHT, this);
    }

    // ============================================================
    private void startThread() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            for (DisplayAnimation anim : enemyAnimations)
                anim.update();

            if (animationPanel != null)
                animationPanel.repaint();

            try {
                Thread.sleep(16);
            } catch (Exception ignored) {}
        }
    }
}