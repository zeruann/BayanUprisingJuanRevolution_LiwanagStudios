package Aa_05_GGameInfo;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import Aa_00_GlobalVariable.GameInfo_Condition;

public class DisplayAnimation {
	 private BufferedImage spriteSheet;
	    private int frameWidth, frameHeight;
	    private int totalFrames;
	    private int currentFrame = 0;

	    private int x, y;          // SPRITE POSTION  
	    private int speed = 15;    // ANIMATION SPEED
	    private int counter = 0;   // FOR TIMING
	    public double scale = 3.3; // SPRITESHEET SCALING - NO BLUR
	    
	    private int gameInfoMode = GameInfo_Condition.GLOBAL.getInfoButtonState(); //GET GAME INFO MODE
	    
	    
	    public DisplayAnimation(BufferedImage spriteSheet, int frameWidth, int frameHeight, int totalFrames, int x, int y) {
	        this.spriteSheet = spriteSheet;
	        this.frameWidth = frameWidth;
	        this.frameHeight = frameHeight;
	        this.totalFrames = totalFrames;
	        this.x = x;
	        this.y = y;
	    }

	    public void update() {
	        counter++;
	        if (counter >= speed) {
	            currentFrame = (currentFrame + 1) % totalFrames;
	            counter = 0;
	        }
	    }

	    //DRAW SPRITESHEET ANIMATON
	    public void draw(Graphics g, int offsetX, int offsetY) {
	        Graphics2D g2 = (Graphics2D) g;

	        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

	        if(gameInfoMode == 2) scale = 3;
	        if(gameInfoMode == 3) scale = 3.7;
	        
	        int drawW = (int)(frameWidth * scale);
	        int drawH = (int)(frameHeight * scale);

	        int sx = currentFrame * frameWidth;

	        g2.drawImage(
	                spriteSheet,
	                offsetX, offsetY, offsetX + drawW, offsetY + drawH,
	                sx, 0, sx + frameWidth, frameHeight,
	                null
	        );
	    }

	    // SETTERS
	    public void setPosition(int x, int y) {
	        this.x = x; this.y = y;
	    }

	    public void setSpeed(int speed) {
	        this.speed = speed;
	    }
}
