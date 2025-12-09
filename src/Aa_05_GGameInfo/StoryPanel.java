package Aa_05_GGameInfo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StoryPanel extends JPanel {
    
    private StoryList story;
    private JLabel imageLabel;
    
    public StoryPanel() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 500));

        // Create story list
        story = new StoryList();
        story.addImage("/GameInfo/StoryPanel1.png");
        story.addImage("/GameInfo/StoryPanel2.png");
        story.addImage("/GameInfo/StoryPanel3.png");

        
        // Image display
        
        imageLabel = new JLabel();
        imageLabel.setBounds(50, 50, 700, 350);
        updateImage();
        add(imageLabel);

        // NEXT button
        JButton nextBtn = new JButton("Next");
        nextBtn.setBounds(600, 420, 100, 40);
        nextBtn.addActionListener(e -> {
            updateImage(story.nextImage());
        });
        add(nextBtn);

        // PREVIOUS button
        JButton prevBtn = new JButton("Previous");
        prevBtn.setBounds(100, 420, 100, 40);
        prevBtn.addActionListener(e -> {
            updateImage(story.prevImage());
        });
        add(prevBtn);
        

        // CLOSE button
        JButton closeBtn = new JButton("Close");
        closeBtn.setBounds(350, 420, 100, 40);
        closeBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) w.dispose();
        });
        add(closeBtn);
    }

    private void updateImage() {
     //   updateImage(story.getCurrentImage());
    }

    private void updateImage(String path) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        imageLabel.setIcon(icon);
    }
}
