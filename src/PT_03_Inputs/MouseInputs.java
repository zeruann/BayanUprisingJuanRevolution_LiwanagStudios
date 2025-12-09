package PT_03_Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

	private GamePanel gamePanel;

	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
			gamePanel.getGame().getPlaying().mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
			gamePanel.getGame().getPlaying().mouseMoved(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
			gamePanel.getGame().getPlaying().mouseClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
			gamePanel.getGame().getPlaying().mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
			gamePanel.getGame().getPlaying().mouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

}