package Aa_01_BackgroundMusic;

import javax.sound.sampled.*;
import java.io.BufferedInputStream; // <-- New Import
import java.io.IOException;
import java.io.InputStream;

public class MusicPlayer {

    private static Clip clip;

    public static synchronized void playMusic(String path, boolean loop) {

        if (clip != null && clip.isRunning()) {
            System.out.println("Music already playing. Ignored.");
            return;
        }

        try {
            stop();

            AudioInputStream audio = AudioSystem.getAudioInputStream(
                MusicPlayer.class.getResource(path)
            );

            clip = AudioSystem.getClip();
            clip.open(audio);

            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            System.out.println("Now playing: " + path);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static synchronized void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            System.out.println("Music stopped.");
        }
    }
    
    public static boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

}
