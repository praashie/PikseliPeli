/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
/**
 *
 * @author User
 */
public class LineFX {
    
    private static ArrayList<Particle> updateList;
    private static ArrayList<Particle> removeList;
    
    private static class Particle {
        
        public double x;
        public double y;
        public double oldX;
        public double oldY;
        public double velX;
        public double velY;
        public double decay;
        public Color color;
        
        public Particle(double x, double y, double a, double speed, double decay) {
            
            this.x = x;
            this.y = y;
            this.oldX = x;
            this.oldY = y;
            this.velX = LineFX.cos2(a) * speed;
            this.velY = LineFX.sin2(a) * speed;
            this.decay = decay;
            this.color = Color.BLACK;
        }
        
        public Particle(double x, double y, Color c, double a, double speed, double decay) {
            
            this.x = x;
            this.y = y;
            this.oldX = x;
            this.oldY = y;
            this.velX = LineFX.cos2(a) * speed;
            this.velY = LineFX.sin2(a) * speed;
            this.decay = decay;
            this.color = c;
        }
        
        public void update() {
            
            //this.x += ( (this.velX * GameHandler.frameTime) +
            //        (this.velX * Math.pow(this.decay, GameHandler.frameTime)) ) / 2;
            //this.y += ( (this.velY * GameHandler.frameTime) +
            //        (this.velY * Math.pow(this.decay, GameHandler.frameTime)) ) / 2;
            this.x += this.velX * GameHandler.frameTime;
            this.y += this.velY * GameHandler.frameTime;
            
            this.velX = this.velX * Math.pow(this.decay, GameHandler.frameTime);
            this.velY = this.velY * Math.pow(this.decay, GameHandler.frameTime);
            //this.velX = this.velX * this.decay;
            //this.velY = this.velY * this.decay;
            
            if (Math.abs(this.velX) < 15 && Math.abs(this.velY) < 15) {
                LineFX.removeList.add(this);
            }
        }
        
        public void draw(Graphics2D g) {
            g.setColor(color);
            g.drawLine((int)this.oldX, (int)this.oldY, (int)this.x, (int)this.y);
            this.oldX = this.x;
            this.oldY = this.y;
        }
    }
    
    public static void spawnLines(double x, double y, Color c, double spd, double a, double s,
            double ss, double amount, double decay) {
        
        if (updateList == null) {
            updateList = new ArrayList<Particle>();
            removeList = new ArrayList<Particle>();
        }
        
        for (int i = 1; i<= amount; i++) {
            LineFX.Particle p = new LineFX.Particle(x, y, c, 
                    a + s * (Math.random() - 0.5), spd + (Math.random() - 0.5)*ss*2,
                    decay);
            updateList.add(p);
        }
    }
    public static void spawnLines(double x, double y, double spd, double a, double s,
            double ss, double amount, double decay) {
        spawnLines(x, y, Color.BLACK, spd, a, s, ss, amount, decay);
        
    }
    
    public static void update() {
        
        if (updateList == null) {
            updateList = new ArrayList<Particle>();
            removeList = new ArrayList<Particle>();
        }
        
        for (Particle p: updateList) {
            p.update();
        }
        for (Particle p: removeList) {
            updateList.remove(p);
        }
        removeList.clear();
    }
    
    public static void draw(Graphics2D g) {
        if (updateList != null){
            for (Particle p: updateList) {
                p.draw(g);
            }
        }
    }
    
    public static double cos2(double a) {
        return Math.cos(Math.toRadians(a));
    }

    public static double sin2(double a) {
        return Math.sin(Math.toRadians(a));
    }
}
