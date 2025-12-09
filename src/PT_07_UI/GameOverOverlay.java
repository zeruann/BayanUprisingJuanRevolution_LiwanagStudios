package PT_07_UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import PT_02_GameStates.Playing;
import main.PlatformerGame;

public class GameOverOverlay {

	private Playing playing;

	public GameOverOverlay(Playing playing) {
		this.playing = playing;
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, PlatformerGame.GAME_WIDTH, PlatformerGame.GAME_HEIGHT);

		g.setColor(Color.white);
		g.drawString("Game Over", PlatformerGame.GAME_WIDTH / 2, 150);
		g.drawString("Press esc to enter Main Menu!", PlatformerGame.GAME_WIDTH / 2, 300);

	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			playing.resetAll();
		}
	}
}
