
package learningGame.music;


// Own packages 
import learningGame.LearningGame;
import learningGame.log.Log2;


// Java packages
import java.io.File;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


/* 
 * TODO: comments
 */
public class PlayMusic {
    final protected static String workingDir = LearningGame.workingDir + "music\\";
    private static Hashtable<String, Clip> clipTable = new Hashtable<String, Clip>();
    
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Constructor
     * ----------------------------------------------------------------------------------------------------------------
     */
    @Deprecated
    private PlayMusic() { }
    
    /* ----------------------------------------------------------------------------------------------------------------
     * Private fuctions
     * ----------------------------------------------------------------------------------------------------------------
     */
    /* 
     * Creates a single playable clip.
     * 
     * @param fileName the location of the music file. Must be a wav file format.
     * @return the created clip.
     */
    public static Clip createClip(String fileName) {
        Clip clip = null;
        
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
        } catch (UnsupportedAudioFileException e) {
            Log2.write(e);
            
        } catch (IOException e) {
            Log2.write(e);
            
        } catch (LineUnavailableException e) {
            Log2.write(e);
        }
        
        clipTable.put(fileName, clip);
        
        return clip;
    }
    
    /* 
     * Plays a music file.
     * If there was no clip of the file, create a clip.
     * 
     * @param fileName the location of the music file. Must be a wav file format.
     */
    public static void play(String fileName) {
        if (!clipTable.contains(fileName)) {
            createClip(fileName);
        }
        
        clipTable.get(fileName).start();
    }
    
    /* 
     * Adjust the volume of the given clip.
     * 
     * @param clip determines which clip should be affected by the volume change.
     * @param gain determines the volume that the clip should be played.
     *     It must hold that 0.0 <= gain <= 1.0.
     * 
     * TODO not working correctly when the music is already playing.
     */
    public static void setVolume(Clip clip, float gain) {
        if (gain > 1.0F) gain = 1.0F;
        if (gain < 0.0F) gain = 0.0F;
        
        if (gain < 0.0F) {
            Math.log10(gain * 0.9F + 1F);
            
        } else {
            Math.log10(gain * 10);
        }
        
        boolean isRunning = clip.isRunning();
        
        if (isRunning) clip.stop();
        FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        
        float max = control.getMaximum();
        float min = control.getMinimum();
        System.out.println(max);
        System.out.println(min);
        
        control.setValue(min + (max-min) * gain);
        if (isRunning) clip.start();
    }
    
    /* 
     * Stops all clips from playing.
     */
    public static void stopAllMusic() {
        for (Enumeration<Clip> e = clipTable.elements(); e.hasMoreElements();) {
            e.nextElement().stop();
        }
    }
    
    
    
}