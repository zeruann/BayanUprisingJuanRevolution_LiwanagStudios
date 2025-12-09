package Aa_03_GameMap;

import javax.swing.*;
import java.awt.*;

public class Animation extends JLabel {

    private int originalY;
    private int offset = 0;
    private int direction = 1;
    private int speed = 1;
    private int range = 10;

    public Animation(String imagePath, int x, int y, int width, int height) {

        ImageIcon raw = new ImageIcon(getClass().getResource(imagePath));
        Image scaled = raw.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(scaled));

        setBounds(x, y, width, height);
        originalY = y;

        Timer timer = new Timer(16, e -> animate());
        timer.start();
    }

    private void animate() {
        offset += direction * speed;

        if (offset > range || offset < -range)
            direction *= -1;

        setLocation(getX(), originalY + offset);
    }
}
