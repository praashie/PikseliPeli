/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author User
 */
public class Audio {
    
    public static URL sndShoot, sndHurt, sndPowerup, sndDeath, sndSpawn,
            sndShotgun, sndShotgunClick1, sndShotgunClick2, sndRifleZoom,
            sndRifle, sndPowerupSpawn, sndPowerupReady, sndChaosShot, sndChaosBounce;
    public static float masterVolume = 0.0f;
    
    public static void setMasterVolume(float v) {
        masterVolume = v;
    }
    public static URL loadSound(String path) {
        URL c = null;
        try {
            //c = AudioSystem.getClip();
            //c = AudioSystem.getAudioInputStream(
            //        PikseliPeli.class.getResource(path));
            c = PikseliPeli.class.getResource(path);
            //c.open(sStream);
        } catch (Exception ex) {
            Logger.getLogger(Audio.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (c != null) {return c;}
        else {return null;}
    }
    public static void playSound(URL s) {
        playSound(s, 0.0f);
    }
    
    public static void playSound(URL s, float volume) {
        try {
            //s.setFramePosition(0);
            //s.start();
            Clip c = AudioSystem.getClip();
            AudioInputStream sc = AudioSystem.getAudioInputStream(
                    s);
            c.open(sc);
            
            if (c.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                ((FloatControl)(c.getControl(FloatControl.Type.MASTER_GAIN)))
                        .setValue(volume+masterVolume);
            }
            
            c.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public static void loadSoundFX() {
        sndShoot = loadSound("media/shoot.wav");
        sndHurt = loadSound("media/hurt.wav");
        sndPowerup = loadSound("media/powerup.wav");
        sndPowerupSpawn = loadSound("media/powerup_spawn.wav");
        sndPowerupReady = loadSound("media/powerup_ready.wav");
        sndDeath = loadSound("media/death.wav");
        sndSpawn = loadSound("media/respawn.wav");
        sndShotgun = loadSound("media/shotgun.wav");
        sndShotgunClick1 = loadSound("media/click1.wav");
        sndShotgunClick2 = loadSound("media/click2.wav");
        sndRifleZoom = loadSound("media/rifle_focus.wav");
        sndRifle = loadSound("media/rifle.wav");
        sndChaosShot = loadSound("media/shoot_chaos.wav");
        sndChaosBounce = loadSound("media/chaos_bounce.wav");
    }
    
}
