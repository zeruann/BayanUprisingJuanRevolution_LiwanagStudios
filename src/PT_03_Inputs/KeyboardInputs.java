package PT_03_Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import main.GamePanel;

public class KeyboardInputs implements KeyListener {

	private GamePanel gamePanel;

	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
			gamePanel.getGame().getPlaying().keyReleased(e);		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		gamePanel.getGame().getPlaying().keyPressed(e);	
	}
}