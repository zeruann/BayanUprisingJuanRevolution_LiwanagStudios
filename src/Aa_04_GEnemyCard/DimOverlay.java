package Aa_04_GEnemyCard;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

public class DimOverlay extends JComponent {

    public DimOverlay() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw translucent black over the entire window
        g.setColor(new Color(0, 0, 0, 100)); 
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}