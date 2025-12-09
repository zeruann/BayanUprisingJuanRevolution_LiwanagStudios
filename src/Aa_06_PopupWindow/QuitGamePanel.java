package Aa_06_PopupWindow;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class QuitGamePanel extends JPanel implements Runnable {

    public final int WIDTH = 423;
    public final int HEIGHT = 456;

    private JFrame mainFrame;
    private BufferedImage bgImage;
    private QuitGameButton quitButtons;

    private String windowIMG = "/GameInfo/QuitWindow.png";

    public QuitGamePanel() {
        this(null);
    }

    public QuitGamePanel(JFrame mainFrame) {
        this.mainFrame = mainFrame;

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setOpaque(false);
        setLayout(null);

        // Load background image
        loadBackground(windowIMG);

        // Create quit buttons
        quitButtons = new QuitGameButton(
                e -> System.exit(0), // Cancel
                e -> closePanel(),   // Outside click
                e -> closePanel()    // Confirm quit
        );

        quitButtons.setBounds(0, 0, WIDTH, HEIGHT);
        quitButtons.setOpaque(false);

        add(quitButtons);
    }

    private void loadBackground(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                bgImage = ImageIO.read(is);
            } else {
                System.err.println("Background not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("Background failed: " + path);
        }
    }

    private void closePanel() {
        // Close only the popup window
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) {
            w.dispose();
        }

        // If using a glass pane, hide it
        if (mainFrame != null) {
            mainFrame.getGlassPane().setVisible(false);
        }
    }

    @Override
    public void run() { }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, WIDTH, HEIGHT, null);
        }
    }
}
