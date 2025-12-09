package CG_06_Entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Aa_00_GlobalVariable.LevelConditional;
import CG_05_Utilz.Constants.EnemyConstants;

public class Enemy extends Entity {

    // GLOBAL VARIABLE: LEVEL CONDITIONAL
    LevelConditional levelCond = LevelConditional.GLOBAL;
    
    //==========================================================
    // calculateScale
    //==========================================================
    private int panelWidth;
    private int panelHeight;
    private float scale = 1.0f;
    
    // Add this to constructor or create a setter
    public void setPanelSize(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
        calculateScale();
    }
    
    private void calculateScale() {
        // Scale based on panel dimensions
        // Adjust these multipliers based on your needs
        float scaleX = panelWidth / 1920f; // Assuming 1920x1080 base
        float scaleY = panelHeight / 1080f;
        scale = Math.min(scaleX, scaleY); // Use minimum to maintain aspect ratio
    }

    
    
    // =========================================================
    // SPRITES
    // =========================================================
    private BufferedImage[] idleFrames;
    private BufferedImage[] attackFrames;
    private BufferedImage[] hurtFrames;
    private BufferedImage[] walkFrames;

    private String idleFramesURL = "/characters/03_pig-idle-combat.png";
    private String attackFramesURL = "/characters/03_pig-thrust.png";
    private String hurtFramesURL = "/characters/03_pig-hurt.png";
    private String walkFramesURL = "/characters/03_pig-walk.png";

    // =========================================================
    // POSITION + ATTACK MOVEMENT
    // =========================================================
    private float originalX, originalY;
    private float attackOffsetX = -100;
    private float attackOffsetY = 0;
    private boolean isAttacking = false;

    // Reference to target player
    private Player target;

    // =========================================================
    // ANIMATION
    // =========================================================
    private int aniTick = 0, aniIndex = 0;
    private int aniSpeed = 30;
    private int enemyAction = EnemyConstants.IDLE;

    // =========================================================
    // ENEMY STATS
    // =========================================================
    private int currentHealth = 250;
    private int maxHealth = 250;
    private int shield = 0;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public Enemy(float x, float y) {
        super(x, y);
        originalX = x;
        originalY = y;
        loadAnimations();
    }

    public void setTarget(Player player) {
        this.target = player;
    }

    // =========================================================
    // UPDATE + RENDER
    // =========================================================
    public void update() {
        updateAnimationTick();

        if (enemyAction == EnemyConstants.ATTACK && target != null) {
            isAttacking = true;
            x = target.getX() + attackOffsetX;
            y = target.getY() + attackOffsetY;
        } else {
            isAttacking = false;
            x = originalX;
            y = originalY;
        }
    }

    public void render(Graphics g) {
        BufferedImage frame = getCurrentFrame();
        if (frame != null) {
            int width = (int) (64 * 5 * scale);
            int height = (int) (64 * 5 * scale);
            g.drawImage(frame, (int) x, (int) y, width, height, null);
        }
    }

    // =========================================================
    // ANIMATION LOGIC
    // =========================================================
    private void updateAnimationTick() {

        aniTick++;

        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;

            int frames = EnemyConstants.GetSpriteAmount(enemyAction);

            if (aniIndex >= frames) {

                // DEAD → hold last frame
                if (enemyAction == EnemyConstants.HURT && isDead()) {
                    aniIndex = frames - 1;
                    enemyAction = EnemyConstants.DEATH;
                    return;
                }

                aniIndex = 0;

                if (enemyAction == EnemyConstants.ATTACK || enemyAction == EnemyConstants.HURT) {
                    enemyAction = EnemyConstants.IDLE;
                }
            }
        }
    }

    private BufferedImage getCurrentFrame() {
        switch (enemyAction) {
        case EnemyConstants.ATTACK:
            return attackFrames != null && attackFrames.length > 0 ? attackFrames[aniIndex % attackFrames.length] : null;
        case EnemyConstants.HURT:
            return hurtFrames != null && hurtFrames.length > 0 ? hurtFrames[aniIndex % hurtFrames.length] : null;
        case EnemyConstants.DEATH:
            return hurtFrames != null && hurtFrames.length > 0 ? hurtFrames[hurtFrames.length - 1] : null;
        default:
            return idleFrames != null && idleFrames.length > 0 ? idleFrames[aniIndex % idleFrames.length] : null;
        }
    }

    // =========================================================
    // ACTIONS
    // =========================================================
    public void playAttackAnimation() {
        enemyAction = EnemyConstants.ATTACK;
        aniIndex = 0;
    }

    public void playHurtAnimation() {
        enemyAction = EnemyConstants.HURT;
        aniIndex = 0;
        aniTick = 0;
        aniSpeed = 30;
    }

    // =========================================================
    // HEALTH SYSTEM
    // =========================================================
    public void takeDamage(int damage) {

        if (shield > 0) {
            int blocked = Math.min(shield, damage);
            damage -= blocked;
            shield -= blocked;
        }

        currentHealth = Math.max(0, currentHealth - damage);

        if (isDead()) {
            playHurtAnimation();
        }
    }

    // New setters (required by TutorialManager and other systems)
    public void setMaxHealth(int hp) {
        if (hp < 1) hp = 1;
        maxHealth = hp;
        if (this.currentHealth > this.maxHealth) {
            this.currentHealth = this.maxHealth;
        }
    }


    public void setCurrentHealth(int hp) {
        if (hp < 0) hp = 0;
        currentHealth = Math.min(hp, this.maxHealth);
    }

    public void heal(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    public void addShield(int amount) {
        shield += amount;
    }
    
    
    // SET HEALTH FOR EACH LEVEL
    public void setHealthForLevel() {
        int level = getCurrentLevel();
        
        switch (level) {
            case 1 -> {
                maxHealth = 400;
                currentHealth = 400;
            }
            case 2 -> {
                maxHealth = 600;
                currentHealth = 600;
            }
            case 3 -> {
                maxHealth = 800;
                currentHealth = 800;
            }
            case 4 -> {
                maxHealth = 1500;
                currentHealth = 1500;
            }
            case 5 -> {
                maxHealth = 2000;
                currentHealth = 2000;
            }
            default -> {
                maxHealth = 2500;
                currentHealth = 2500;
            }
        }
        
        System.out.println("Enemy HP set to: " + maxHealth + " for Level " + level);
    }

    // Helper method to get current level
    private int getCurrentLevel() {
        for (int i = 1; i <= 5; i++) {
            if (levelCond.getLevelState(i) == 1) {
                return i;
            }
        }
        return 1;
    }
    
    
    

    // =========================================================
    // RESET (FOR RETRY BUTTON)
    // =========================================================
    public void resetForBattle() {
        currentHealth = maxHealth;
        enemyAction = EnemyConstants.IDLE;
        aniIndex = 0;
        x = originalX;
        y = originalY;
    }

    // =========================================================
    // GETTERS & SETTERS
    // =========================================================
    public boolean isDead() {
        return currentHealth <= 0;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getShield() {
        return shield;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int getX() {
        return (int) x;
    }

    @Override
    public int getY() {
        return (int) y;
    }

    // =========================================================
    // SPRITE LOADING
    // =========================================================

    private void loadAnimations() {

        try {
            if (levelCond.getLevelState(1) == 1) {

                idleFramesURL = "/characters/02_mouse-idle.png";
                attackFramesURL = "/characters/02_mouse-thrust.png";
                hurtFramesURL = "/characters/02_mouse-hurt.png";
                walkFramesURL = "/characters/02_mouse-walk.png";

            } else if (levelCond.getLevelState(2) == 1) {
                idleFramesURL = "/characters/03_pig-idle-combat.png";
                attackFramesURL = "/characters/03_pig-thrust.png";
                hurtFramesURL = "/characters/03_pig-hurt.png";
                walkFramesURL = "/characters/03_pig-walk.png";
            } else if (levelCond.getLevelState(3) == 1) {
                idleFramesURL = "/characters/04_rat-idle.png";
                attackFramesURL = "/characters/04_rat-thrust.png";
                hurtFramesURL = "/characters/04_rat-hurt.png";
                walkFramesURL = "/characters/04_rat-walk.png";
            } else if (levelCond.getLevelState(4) == 1) {
                idleFramesURL = "/characters/05_Boarman-combat.png";
                attackFramesURL = "/characters/05_Boarman-thrust.png";
                hurtFramesURL = "/characters/05_Boarman-hurt.png";
                walkFramesURL = "/characters/05_Boarman-walk.png";
            } else if (levelCond.getLevelState(5) == 1) {
                idleFramesURL = "/characters/06_Croc-combat.png";
                attackFramesURL = "/characters/06_Croc-thrust.png";
                hurtFramesURL = "/characters/06_Croc-hurt.png";
                walkFramesURL = "/characters/06_Croc-walk.png";
            }

            idleFrames = loadAnimationRow(idleFramesURL, 2, 1);
            attackFrames = loadAnimationRow(attackFramesURL, 8, 1);
            hurtFrames = loadAnimationRow(hurtFramesURL, 6, 0);
            walkFrames = loadAnimationRow(walkFramesURL, 9, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage[] loadAnimationRow(String path, int cols, int row) {
        try {
            java.io.InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.err.println("Enemy.loadAnimationRow: missing sprite sheet: " + path);
                return new BufferedImage[0];
            }
            BufferedImage sheet = ImageIO.read(is);
            if (sheet == null) {
                System.err.println("Enemy.loadAnimationRow: couldn't read image: " + path);
                return new BufferedImage[0];
            }
            BufferedImage[] frames = new BufferedImage[cols];

            for (int i = 0; i < cols; i++) {
                frames[i] = sheet.getSubimage(i * 64, row * 64, 64, 64);
            }

            return frames;

        } catch (Exception e) {
            e.printStackTrace();
            return new BufferedImage[0];
        }
    }

    public int getWidth() {
        return 100; //  sprite width
    }

    public int getHeight() {
        return 120; // sprite height
    }
    
}
