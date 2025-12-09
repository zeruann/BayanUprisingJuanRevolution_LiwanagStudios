package CG_01_Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import CG_02_System.SoundEffectsPlayer;
import CG_03_UI.CardInfo;
import CG_04_Inputs.CardClickHandler;
import CG_04_Inputs.KeyboardInputs;
import CG_04_Inputs.MouseInputs;
import CG_05_Utilz.FontLoader;

//Inheritance
public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private Game game;
	private SoundEffectsPlayer getSoundFx; // sound
	private int lastHoveredCardIndex = -1; // track last hover
	private CardClickHandler cardClickHandler;
	
	public float hoverProgress = 0f; // 0 = no hover, 1 = full hover
	private float hoverSpeed = 0.15f; // how fast the animation progresses

	public int hoverCardIndex = -1;   // Make existing field public

	public CardClickHandler getCardClickHandler() {
		return cardClickHandler;
	}
	

	public void setGetSoundFx(SoundEffectsPlayer getSoundFx) {
		this.getSoundFx = getSoundFx;
	}


	public SoundEffectsPlayer getSoundEffectsPlayer() {
		return getSoundFx;
	}

	// Fixed GamePanel constructor
	public GamePanel(Game game) {
	    this.game = game;
	    this.setFocusable(true);
	    this.requestFocus();
	    this.getSoundFx = new SoundEffectsPlayer();
	    getSoundFx.playBackgroundMusic("/music/01_InGameBG_Funny_Bit_.wav", false);
	    
	    // IMPORTANT: Only create MouseInputs, which creates CardClickHandler internally
	    mouseInputs = new MouseInputs(this);
	    
	    // Get the CardClickHandler from MouseInputs instead of creating a new one
	    cardClickHandler = mouseInputs.getCardClickHandler();
	    
	    addKeyListener(new KeyboardInputs(this));
	    addMouseListener(mouseInputs);
	    addMouseMotionListener(mouseInputs);

	    setPanelSize();
	}

	private void setPanelSize() {
		Dimension size = new Dimension(1280, 800);
		setMinimumSize(size);
		setPreferredSize(size);
		setMaximumSize(size);

	}

	public void updateGame() {

	}

	private int hoveredCardIndex = -1;

	public void setHoveredCardIndex(int index) {
		this.hoveredCardIndex = index;
	}

	public int getHoveredCardIndex() {
		return hoveredCardIndex;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// fill background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());

		// center area
		int gameWidth = 1280;
		int gameHeight = 800;
		int x = (getWidth() - gameWidth) / 2;
		int y = (getHeight() - gameHeight) / 2;
		Graphics g2 = g.create(x, y, gameWidth, gameHeight);

		// render game
		game.render(g2);
		
		// Animate hover progress
		if (hoverCardIndex != -1) {
		    hoverProgress += hoverSpeed;
		    if (hoverProgress > 1f) hoverProgress = 1f;
		} else {
		    hoverProgress -= hoverSpeed;
		    if (hoverProgress < 0f) hoverProgress = 0f;
		}

		if (hoverProgress > 0f && hoverCardIndex != -1) {
		    CardInfo info = getCardClickHandler().getCardInfos()[hoverCardIndex];
		    Point mousePos = getMousePosition();
		    if (mousePos != null) {

		    	String desc = info.getDescription();
		    	String cost = "Cost: " + info.getCost();
		    	String damage = "Damage: " + info.getDamage(); // NEW

		    	Font font = FontLoader.loadFont("/fonts/ByteBounce.ttf", 24f);
		    	g.setFont(font);
		    	FontMetrics fm = g.getFontMetrics();

		    	int boxWidth = 250;
		    	int padding = 10;

		    	// Wrap description
		    	String[] lines = wrapText(g, desc, boxWidth - padding * 2);

		    	// Auto height (add 3 lines: description + cost + damage)
		    	int lineHeight = fm.getHeight();
		    	int boxHeight = (lines.length + 3) * lineHeight + padding * 2;

		    	// Position
		    	int tooltipX = mousePos.x - boxWidth / 2;
		    	int tooltipY = mousePos.y - boxHeight - 20;

		    	// Keep on screen
		    	if (tooltipX < 0) tooltipX = 0;
		    	if (tooltipX + boxWidth > getWidth()) tooltipX = getWidth() - boxWidth;
		    	if (tooltipY < 0) tooltipY = 0;

		    	// Draw box
		    	g.setColor(new Color(0, 0, 0, 200));
		    	g.fillRoundRect(tooltipX, tooltipY, boxWidth, boxHeight, 15, 15);
		    	g.setColor(Color.WHITE);
		    	g.drawRoundRect(tooltipX, tooltipY, boxWidth, boxHeight, 15, 15);

		    	// Draw description lines
		    	int yText = tooltipY + padding + lineHeight;
		    	int center = tooltipX + boxWidth / 2;

		    	for (String l : lines) {
		    	    g.drawString(l, center - fm.stringWidth(l) / 2, yText);
		    	    yText += lineHeight;
		    	}

		    	// Draw cost
		    	yText += 5;
		    	g.setColor(Color.YELLOW);
		    	g.drawString(cost, center - fm.stringWidth(cost) / 2, yText);

		    	// Draw damage below cost
		    	yText += lineHeight;
		    	g.setColor(Color.RED);
		    	g.drawString(damage, center - fm.stringWidth(damage) / 2, yText);

		    }
		}

		g2.dispose();
	}

	public Game getGame() {
		return game;
	}
	
	private String[] wrapText(Graphics g, String text, int maxWidth) {
	    FontMetrics fm = g.getFontMetrics();
	    java.util.List<String> lines = new java.util.ArrayList<>();

	    String[] words = text.split(" ");
	    String line = "";

	    for (String word : words) {
	        String testLine = line.isEmpty() ? word : line + " " + word;
	        if (fm.stringWidth(testLine) > maxWidth) {
	            lines.add(line);
	            line = word;
	        } else {
	            line = testLine;
	        }
	    }
	    if (!line.isEmpty()) {
	        lines.add(line);
	    }

	    return lines.toArray(new String[0]);
	}


}
