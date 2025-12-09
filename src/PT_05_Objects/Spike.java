package PT_05_Objects;

import main.PlatformerGame;

public class Spike extends GameObject{

	public Spike(int x, int y, int objType) {
		super(x, y, objType);
		
		initHitbox(64, 32);
		xDrawOffset = 0;
		yDrawOffset = (int)(PlatformerGame.SCALE * 32);
		hitbox.y += yDrawOffset;
		
	}

}