package CG_03_UI;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;

public class CardInfo {
	private String name;
	private String description;
	private int cost;
	private int damage;
	private boolean active;
	private BufferedImage originalImage;

	public CardInfo(String name, String description, int cost, int damage, int cardIndex) {
		this.name = name;
		this.description = description;
		this.cost = cost;
		this.damage = damage;
		this.active = true;

		loadImage(cardIndex);
	}

	private void loadImage(int cardIndex) {
		try {
			originalImage = ImageIO.read(getClass().getResource("/cards/Attack1.png"));

		} catch (IOException e) {
			System.err.println("Failed to load image for card: " + name + " (index: " + cardIndex + ")");
			e.printStackTrace();

		}
	}

	// Getters and setters
	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public int getCost() {
		return cost;
	}

	public int getDamage() {
		return damage;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}