package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class GameWindow {
	private JFrame frame;
	public GameWindow(GamePanel gamePanel) {
		frame = new JFrame("Bayan Uprising: Juan Revolution");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(gamePanel);
		frame.setResizable(false);
		frame.pack();//auto fits the size of game window for game panel
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.addWindowFocusListener(new WindowFocusListener()  {

			@Override
			public void windowGainedFocus(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
				
			}
			
		});
	}
}
