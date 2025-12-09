package Aa_05_GGameInfo;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JWindow {

    public GameFrame(GameStoryPanel content) {

        setBackground(new Color(0,0,0,0)); // transparent
        setAlwaysOnTop(true);

        setContentPane(content);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
