/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Powerup extends GameObject {
    public static enum bonus {
        weapon, health, chaosmode
    }
    public bonus type;
    public Class<? extends Weapon> weapon;
    public double flashTime;
    public String name;
    
    public Powerup(double x, double y, bonus t, String n) {
        super();
        this.x = x;
        this.y = y;
        this.type = t;
        this.flashTime = 5.0;
        Audio.playSound(Audio.sndPowerupSpawn);
        this.name = n;
    }
    
    public Powerup(double x, double y, Class<? extends Weapon> w, String n) {
        super();
        this.x = x;
        this.y = y;
        this.type = bonus.weapon;
        this.weapon = w;
        this.flashTime = 5.0;
        Audio.playSound(Audio.sndPowerupSpawn);
        this.name = n;
    }
    
    public void update() {
        
        if (this.flashTime > 0.0) {
            this.flashTime -= GameHandler.frameTime;
        } else if (this.flashTime != -10){
            Audio.playSound(Audio.sndPowerupReady);
            this.flashTime = -10;
        } else {
            for (GameObject o: GameHandler.updateList) {
                if (o.getClass() == Hahmo.class) {

                    if (getDistance(this.x, this.y, o.x, o.y) < 20) {
                        Hahmo h = (Hahmo) o;
                        
                        if (this.type == bonus.weapon) {
                            if ((h.usedWeapon).getClass() != weapon) {
                                try {
                                    h.usedWeapon = (weapon.getConstructor()).newInstance();
                                } catch (Exception ex) {
                                    Logger.getLogger(Powerup.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                                }
                                
                            }
                            h.usedWeapon.addAmmo(h.usedWeapon.maxAmmo);
                        } else if (this.type == bonus.chaosmode) {
                            h.usedWeapon.addChaosAmmo(15);
                        } else if (this.type == bonus.health) {
                            h.health += 30;
                        }
                        Audio.playSound(Audio.sndPowerup);
                        destroy();
                        break;
                    }

                }
            }
        }
    }
    
    public void draw(Graphics2D g) {
        
        if (this.flashTime <= 0.0 || (((int)(this.flashTime*1000))%100)<50) {
            g.setColor(Color.BLACK);
            g.drawRect((int)this.x-8, (int)this.y-8, 16, 16);
            PixelText.draw((int)this.x, (int)this.y-25, this.name, GameHandler.bg, 1);
        }
        
    }
}
