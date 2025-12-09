package CG_07_Effects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class AttackEffect {

    // =========================================================
    // Position & Rendering
    // =========================================================
    private float x, y;                    // Position where effect is drawn
    private float scale = 2.0f;            // Size multiplier of the effect
    private int xOffset = 0;               // Manual X offset adjustment
    private int yOffset = 0;               // Manual Y offset adjustment

    // =========================================================
    // Animation Data
    // =========================================================
    private BufferedImage[] frames;        // Individual sprite frames
    private int aniIndex = 0;              // Current frame index
    private int aniTick = 0;               // Frame timer
    private int aniSpeed = 20;             // Speed of animation (lower = faster)
    private boolean finished = false;      // Marks if animation is done

    // =========================================================
    // Constructors
    // =========================================================


    public AttackEffect(float x, float y, String spritePath, int frameCount) {
        this(x, y, spritePath, frameCount, 2.0f, 0, 0);
    }


    public AttackEffect(float x, float y, String spritePath, int frameCount,
                        float scale, int xOffset, int yOffset) {

        this.x = x;
        this.y = y;
        this.scale = scale;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        loadFrames(spritePath, frameCount);
    }

    // =========================================================
    // Load Animation Frames
    // =========================================================

    private void loadFrames(String path, int frameCount) {
        try (InputStream is = getClass().getResourceAsStream(path)) {

            BufferedImage spriteSheet = ImageIO.read(is);
            int frameWidth = spriteSheet.getWidth() / frameCount;
            int frameHeight = spriteSheet.getHeight();

            frames = new BufferedImage[frameCount];

            for (int i = 0; i < frameCount; i++) {
                frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // Update Animation
    // =========================================================

    public void update() {
        if (finished)
            return;

        aniTick++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            // Stop at the last frame instead of looping
            if (aniIndex >= frames.length) {
                aniIndex = frames.length - 1;
                finished = true;
            }
        }
    }

    // =========================================================
    // Render Animation
    // =========================================================

    public void render(Graphics g) {
        if (!finished) {

            int width = (int) (frames[aniIndex].getWidth() * scale);
            int height = (int) (frames[aniIndex].getHeight() * scale);

            // Center the animation and apply manual offsets
            g.drawImage(
                frames[aniIndex],
                (int) (x - width / 2 + xOffset),
                (int) (y - height / 2 + yOffset),
                width,
                height,
                null
            );
        }
    }

    // =========================================================
    // Status
    // =========================================================

    public boolean isFinished() {
        return finished;
    }
}
