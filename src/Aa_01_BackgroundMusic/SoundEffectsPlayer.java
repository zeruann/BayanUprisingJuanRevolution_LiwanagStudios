package Aa_01_BackgroundMusic;

import javax.sound.sampled.*;
import java.io.BufferedInputStream; // <-- New Import
import java.io.IOException;
import java.io.InputStream;

public class SoundEffectsPlayer {
	
    public void playSound(String path) {
        try {
         
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.err.println("Sound effect file not found: " + path);
                return;
            }
            
        
            InputStream bufferedIn = new BufferedInputStream(is);

          
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
