package Aa_04_GEnemyCard;

import javax.swing.*;
import java.awt.*;

public class CardFrame extends JFrame {

    public CardFrame(CardPanel content) {
        System.out.println("[CardFrame] Constructor called");
        
        try {
            System.out.println("[CardFrame] Setting default close operation");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            System.out.println("[CardFrame] Setting undecorated");
            setUndecorated(true);
            
            System.out.println("[CardFrame] Setting transparent background");
            setBackground(new Color(0, 0, 0, 0));
            
            System.out.println("[CardFrame] Setting always on top");
            setAlwaysOnTop(true);
            
            System.out.println("[CardFrame] Setting content pane");
            setContentPane(content);
            
            System.out.println("[CardFrame] Packing");
            pack();
            
            System.out.println("[CardFrame] Setting location relative to null (center)");
            setLocationRelativeTo(null);
            
            System.out.println("[CardFrame] Setting visible");
            setVisible(true);
            
            System.out.println("[CardFrame] ✓ CardFrame setup complete");
            
        } catch (Exception e) {
            System.err.println("[CardFrame] ✗ Exception during setup: " + e.getMessage());
            e.printStackTrace();
        }
    }
}