package CG_02_System;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class BackgroundManager {

    private BufferedImage background;


    public BackgroundManager(String resourcePath) {
        loadBackground(resourcePath);
    }

    private void loadBackground(String resourcePath) {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Background resource not found: " + resourcePath);
                return;
            }
            background = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public void draw(Graphics g) {
        if (background != null) {
            g.drawImage(background, 0, 0, 1280, 800, null); // adjust size if needed
        }
    }

    public BufferedImage getBackground() {
        return background;
    }

    public void setBackground(BufferedImage background) {
        this.background = background;
    }
}
