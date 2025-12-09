package CG_02_System;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class SoundEffectsPlayer {

    private Clip backgroundClip;

    // Play a sound effect once
    public void playSound(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.err.println("Sound effect file not found: " + path);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(is);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Play background music in a loop
    public void playBackgroundMusic(String path, boolean loop) {
        try {
            // Stop current clip if playing
            if (backgroundClip != null && backgroundClip.isRunning()) {
                backgroundClip.stop();
                backgroundClip.close();
            }

            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                System.err.println("Background music file not found: " + path);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(is);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);

            if (loop) {
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                backgroundClip.start();
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Stop background music
    public void stopBackgroundMusic() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }
}
