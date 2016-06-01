/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;

import java.awt.Color;
import java.awt.Graphics2D;
import java.net.URL;

/**
 *
 * @author User
 */
class Luoti extends GameObject {
    public double xvel;
    public double yvel;
    public double oldX;
    public double oldY;
    public double decay;
    public double angle;
    public double size;
    public double damage;
    public boolean chaosMode;
    public boolean showTrails;
    public Hahmo sender;
    
    public URL hitSound;
    public URL bounceSound;
    
    public static final boolean doChaos = false;

    public Luoti(double x, double y, double a, double s, double dmg, double decay, boolean chaos) {
        super();
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.angle = a;
        this.xvel = cos2(a) * s;
        this.yvel = sin2(a) * s;
        this.decay = decay;
        this.size = 2;
        this.damage = dmg;
        this.chaosMode = chaos;
        this.showTrails = false;
        
        this.hitSound = Audio.sndHurt;
        this.bounceSound = Audio.sndChaosBounce;
    }

    @Override
    public void update() {
        this.x += this.xvel * GameHandler.frameTime;
        this.y += this.yvel * GameHandler.frameTime;
        
        this.xvel = this.xvel * Math.pow(this.decay, GameHandler.frameTime);
        this.yvel = this.yvel * Math.pow(this.decay, GameHandler.frameTime);
        this.size += 6 * GameHandler.frameTime;
        //Check for collisions with players
        for (GameObject o : GameHandler.updateList) {
            if (o.getClass() == Hahmo.class) {
                Hahmo e = (Hahmo) o;
                if (e.health > 0 && e.respawnTime <= 0 && getDistance(this.x, this.y, o.x, o.y) < (8)) {
                    e.health -= this.damage;
                    e.xVel += this.xvel * 0.1;
                    e.yVel += this.yvel * 0.1;
                    e.angleVel += (Math.random() - 0.5) * (Math.abs(this.xvel) + Math.abs(this.yvel));
                    Audio.playSound(this.hitSound);
                    LineFX.spawnLines(x, y, Color.RED, 100, 0, 360, 60, 2, 0.03);
                    
                    if (e.health <= 0) {
                        (this.sender).score(e);
                        new FlashText((int)e.x-40, (int)e.y+20, "KILLED BY " + (this.sender.name), 1, 4.0);
                    }
                    destroy();
                }
            }
        }
        
        if (Math.abs(this.xvel) < 40 && Math.abs(this.yvel) < 40) {destroy();}
        
        if (this.x < -50 || this.x > (PikseliPeli.width+50) || this.y < -50 || this.y > (PikseliPeli.height + 50)) {
            destroy();
        }
        
        if (this.showTrails) {
            if ((Math.random()*100.0) < 3) {
                LineFX.spawnLines(this.x, this.y, 20, getAngle(this.x, this.y,
                        this.oldX, this.oldY), 30,5.0, 1, 0.27);
            }
        }
        
        if (this.chaosMode) {
            
            if (this.x < PikseliPeli.borderMargin || this.y < PikseliPeli.borderMargin
                    || this.x > (PikseliPeli.width-PikseliPeli.borderMargin)
                    || this.y > (PikseliPeli.height-PikseliPeli.borderMargin)) {
                Audio.playSound(this.bounceSound);
            }
            if (this.x < PikseliPeli.borderMargin) {this.x = PikseliPeli.borderMargin;this.xvel = Math.abs(this.xvel);}
            if (this.y < PikseliPeli.borderMargin) {this.y = PikseliPeli.borderMargin;this.yvel = Math.abs(this.yvel);}
            if (this.x > (PikseliPeli.width-PikseliPeli.borderMargin)) {
                this.x = (PikseliPeli.width-PikseliPeli.borderMargin);this.xvel = Math.abs(this.xvel)*(-1);}
            if (this.y > (PikseliPeli.height-PikseliPeli.borderMargin)) {
                this.y = (PikseliPeli.height-PikseliPeli.borderMargin);this.yvel = Math.abs(this.yvel)*(-1);}
        }
        
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawLine((int) this.x, (int) this.y, (int) this.oldX, (int) this.oldY);
        //g.drawOval((int)this.x-(int)(this.size/2), (int)this.y-(int)(this.size/2), (int)this.size, (int)this.size);
        //g.drawRect((int)this.x, (int)this.y, 0, 0);
        this.oldX = this.x;
        this.oldY = this.y;
    }
    
}
