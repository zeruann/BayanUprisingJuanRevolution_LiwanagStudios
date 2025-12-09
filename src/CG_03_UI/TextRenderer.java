package CG_03_UI;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.Shape;

public class TextRenderer {

    public static void drawOutlinedText(Graphics2D g2d, String text, int x, int y, Color fill, Color outline, float stroke) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Font boldFont = g2d.getFont().deriveFont(Font.BOLD);
        g2d.setFont(boldFont);

        FontRenderContext frc = g2d.getFontRenderContext();
        Shape shape = boldFont.createGlyphVector(frc, text).getOutline(x, y);

        g2d.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(outline);
        g2d.draw(shape);

        g2d.setColor(fill);
        g2d.fill(shape);
    }
}
