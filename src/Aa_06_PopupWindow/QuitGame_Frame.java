package Aa_06_PopupWindow;

import javax.swing.*;
import java.awt.*;

public class QuitGame_Frame extends JWindow {

    public QuitGame_Frame(QuitGamePanel content) {

        setBackground(new Color(0, 0, 0, 150)); // Transparent dark overlay
        setAlwaysOnTop(true);

        setContentPane(content);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
