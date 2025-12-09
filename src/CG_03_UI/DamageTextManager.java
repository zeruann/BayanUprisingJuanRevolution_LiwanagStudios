package CG_03_UI;

import java.awt.Graphics2D;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Iterator;

public class DamageTextManager {

    private ArrayList<DamageText> damageTexts;
    private Font font;

    public DamageTextManager(Font font) {
        this.damageTexts = new ArrayList<>();
        this.font = font;
    }

    // Add a new damage text
    public void addDamageText(DamageText text) {
        damageTexts.add(text);
    }

    // Update: just remove expired ones (no dt.update() needed)
    public void update() {
        Iterator<DamageText> iterator = damageTexts.iterator();
        while (iterator.hasNext()) {
            DamageText dt = iterator.next();
            if (dt.isExpired()) {
                iterator.remove();
            }
        }
    }

    // Draw all damage texts
    public void draw(Graphics2D g2d) {
        for (DamageText dt : damageTexts) {
            dt.draw(g2d, font);
        }
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
