package PT_05_Objects;

import main.PlatformerGame;

public class Portal extends GameObject {
	private float hoverOffset;
	private int maxHoverOffset, hoverDir = 1;
	private boolean playerInPortal = false;
	
	public Portal(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true;

		initHitbox(32, 64);

		xDrawOffset = (int) (18 * PlatformerGame.SCALE);
		yDrawOffset = (int) (0 * PlatformerGame.SCALE);

		maxHoverOffset = (int) (10 * PlatformerGame.SCALE);
	}
	
	public void update() {
		updateAnimationTick();
		updateHover();
	}

	private void updateHover() {
		hoverOffset += (0.075f * PlatformerGame.SCALE * hoverDir);

		if (hoverOffset >= maxHoverOffset)
			hoverDir = -1;
		else if (hoverOffset < 0)
			hoverDir = 1;

		hitbox.y = y + hoverOffset;
	}
	
	public void onPlayerEnter() {
		if (!playerInPortal) {
			playerInPortal = true;
			// TODO: Add portal entry effect here
			// Examples: 
			// - Load next level
			// - Teleport player
			// - Play sound effect
			// - Trigger animation
			System.out.println("Player entered portal!");
		}
	}
	
	public void onPlayerExit() {
		playerInPortal = false;
	}
	
	public boolean isPlayerInPortal() {
		return playerInPortal;
	}
}