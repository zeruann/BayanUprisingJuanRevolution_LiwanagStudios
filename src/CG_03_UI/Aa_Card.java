package CG_03_UI;

import java.awt.*;
import java.awt.image.BufferedImage;

import CG_01_Main.Game;

public abstract class Aa_Card {
    public int x;
    private int y;
    protected int width;
    protected int height;
    protected String name;
    protected BufferedImage image; // add this

    public Aa_Card(String name, BufferedImage image, int x, int y, int width, int height) {
        this.name = name;
        this.image = image;
        this.x = x;
        this.setY(y);
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        if (image != null)
            g.drawImage(image, x, getY(), width, height, null);
        else {
            g.setColor(Color.WHITE);
            g.fillRect(x, getY(), width, height);
            g.setColor(Color.BLACK);
            g.drawRect(x, getY(), width, height);
            g.drawString(name, x + 5, getY() + 20);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, getY(), width, height);
    }

    public abstract void play(Game game);

    public void setPosition(int x, int y) {
        this.x = x;
        this.setY(y);
    }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
}
