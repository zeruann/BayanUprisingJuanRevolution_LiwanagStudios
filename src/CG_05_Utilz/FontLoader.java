package CG_05_Utilz;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class FontLoader {

    /**
     * Loads a TrueType font from resources and returns it at the specified size.
     * If loading fails, returns a fallback Arial font.
     */
    public static Font loadFont(String path, float size) {
        //System.out.println("Font resource URL: " + FontLoader.class.getResource(path));

        try (InputStream is = FontLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("FAILED to load font: " + path);
                return new Font("Arial", Font.BOLD, (int) size);
            }

            Font base = Font.createFont(Font.TRUETYPE_FONT, is);
            Font derived = base.deriveFont(size);

            // Register the base font
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(base);

            //System.out.println("Loaded custom font: " + base.getFontName());
            return derived;

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.BOLD, (int) size);
        }
    }
}
