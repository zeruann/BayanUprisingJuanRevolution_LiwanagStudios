package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import PT_03_Inputs.KeyboardInputs;
import PT_03_Inputs.MouseInputs;

import static main.PlatformerGame.GAME_HEIGHT;
import static main.PlatformerGame.GAME_WIDTH;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private PlatformerGame game;

	public GamePanel(PlatformerGame game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	private void setPanelSize() {
		Dimension size = new Dimension(1200, 800);
		setPreferredSize(size);
	}

	public void updateGame() {

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	public PlatformerGame getGame() {
		return game;
	}

}