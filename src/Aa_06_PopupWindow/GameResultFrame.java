package Aa_06_PopupWindow;

import java.awt.Color;

import javax.swing.JWindow;

import Aa_05_GGameInfo.GameStoryPanel;

public class GameResultFrame extends JWindow{

    public GameResultFrame(GameResultPanel content) {

        setBackground(new Color(0,0,0,0)); // transparent
        setAlwaysOnTop(true);

        setContentPane(content);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
