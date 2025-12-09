package Aa_03_GameMap;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;

import javax.swing.*;

import Aa_04_GEnemyCard.DimOverlay;

public class MapFrame extends JFrame{
	
    public MapFrame() {
    	
    	DimOverlay overlay = new DimOverlay();
    	overlay.setVisible(false);
    	setGlassPane(overlay);
    	
    	
    	 add(new MapPanel());//panel

        setTitle("Bayan Uprising: Juan Revolution");
        //setSize(1390, 735);
        setSize(1280, 800);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
      
        
        // =====================================================
        // DIM OVERLAY
        // =====================================================
        JPanel dim = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 100));   // semi-transparent black
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // =====================================================
        // BLOCK MOUSE CLICKS
        // =====================================================
        dim.addMouseListener(new MouseAdapter() {});
        dim.addMouseMotionListener(new MouseAdapter() {});

        dim.setOpaque(false);
        dim.setBounds(0, 0, MapFrame.this.getWidth(), MapFrame.this.getHeight());
        dim.setVisible(true);

        dim.setOpaque(false);
        dim.setVisible(false);   // hidden by default

        setGlassPane(dim);
        
      
        
        
    }
    
    //public static void main(String[] args) {
       /// SwingUtilities.invokeLater(() -> { // always good for Swing
      //      new MapFrame().setVisible(true);
       // });
   // }
}