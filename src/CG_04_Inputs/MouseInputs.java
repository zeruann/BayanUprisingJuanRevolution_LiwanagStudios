// ============================================
// OPTION 1: Fix MouseInputs.java (RECOMMENDED)
// ============================================
package CG_04_Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import CG_01_Main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

	private GamePanel gamePanel;
	private CardClickHandler cardClickHandler;

	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		this.cardClickHandler = new CardClickHandler(gamePanel);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gamePanel.getGame().isCardsActive() || gamePanel.getGame().getCardManager().isAnimating()) {
			gamePanel.setHoveredCardIndex(-1);
			gamePanel.hoverCardIndex = -1; // Add this field to GamePanel
			return;
		}

		int hoverIndex = gamePanel.getCardClickHandler().getHoveredCardIndex(e.getX(), e.getY());
		if (hoverIndex != gamePanel.hoverCardIndex) {
			if (hoverIndex != -1)
				gamePanel.getSoundEffectsPlayer().playSound("/soundfx/01_Hover.wav");
				gamePanel.hoverCardIndex = hoverIndex;
		}
		gamePanel.setHoveredCardIndex(hoverIndex);
		gamePanel.repaint(); // ADD THIS - triggers hover repaint
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	public CardClickHandler getCardClickHandler() {
		return cardClickHandler;
	}
}
