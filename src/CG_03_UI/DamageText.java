package CG_03_UI;

import java.awt.*;
import java.awt.font.GlyphVector;

public class DamageText {

    private int x, y;           // screen position
    private String text;        // text to show
    private Color color;        // text color
    private long startTime;     // when it was created
    private long duration = 1000; // ms before disappearing
    private int riseDistance = 50; // how much it moves up
    private boolean expired = false;

    public DamageText(int x, int y, String text, Color color) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return expired;
    }

    public void draw(Graphics2D g2d, Font font) {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed >= duration) {
            expired = true;
            return;
        }

        // Alpha (fade out)
        float alpha = 1.0f - (float) elapsed / duration; // 1 → 0
        alpha = Math.max(0, Math.min(alpha, 1));

        // Float upward
        int riseY = y - (int) (riseDistance * ((float) elapsed / duration));

        // Set font and antialiasing
        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create GlyphVector for outline + fill
        GlyphVector gv = font.createGlyphVector(g2d.getFontRenderContext(), text);

        // Outline (fade with alpha)
        Color outlineColor = new Color(0, 0, 0, (int)(alpha * 255));
        g2d.setColor(outlineColor);
        g2d.setStroke(new BasicStroke(3));
        g2d.draw(gv.getOutline(x, riseY));

        // Fill (fade with alpha)
        Color fillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255));
        g2d.setColor(fillColor);
        g2d.fill(gv.getOutline(x, riseY));
    }
}
