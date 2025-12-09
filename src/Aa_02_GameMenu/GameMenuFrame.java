package Aa_02_GameMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMenuFrame extends JFrame {
    
	private Point dragStart;

    public GameMenuFrame(GameMenuPanel content) {
        // Frame setup
        setUndecorated(true);                       
        setBackground(new Color(0,0,0,0));          // make the whole frame transparent
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use our custom panel as content pane
        setContentPane(content);

        pack();                                     // sizes to panel's preferred size
        setLocationRelativeTo(null);                 // center on screen

        // Make draggable: use frame offset relative to mouse
     
        /*addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                dragStart = e.getPoint();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point loc = getLocation();
                setLocation(loc.x + e.getX() - dragStart.x, loc.y + e.getY() - dragStart.y);
            }
        });
         */
        
        setVisible(true);
    }


}
