package CG_01_Main;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.*;

public class GameWindow {
	private JFrame jframe;
	
	public GameWindow(GamePanel gamePanel) {
		jframe = new JFrame();
		 
		//jframe.setSize(400, 400);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
		jframe.pack(); //pack FIRST to calculate final size
		jframe.setMinimumSize(new Dimension(1280, 800));
		//jframe.setMinimumSize(new Dimension(1369, 735));

		jframe.setLocationRelativeTo(null); // center window
		jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
				
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				
				
			}
		});
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
