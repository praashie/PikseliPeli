/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;

/**
 *
 * @author User
 */
public class PowerupSpawner extends GameObject {
    
    public double spawnTimer;
    public double timeBase;
    public double timeVariance;
    public Powerup.bonus bonusType;
    public Class<? extends Weapon> weapon;
    public String bonusName;
    public Powerup child;
    
    public PowerupSpawner(double tB, double tV, Powerup.bonus t, String n) {
        super();
        
        this.timeBase = tB;
        this.timeVariance = tV;
        this.spawnTimer = timeBase + timeVariance;
        this.bonusType = t;
        this.bonusName = n;
        this.child = null;
    }
    
    public PowerupSpawner(double tB, double tV, Class<? extends Weapon> w, String n) {
        super();
        
        this.timeBase = tB;
        this.timeVariance = tV;
        this.spawnTimer = timeBase + timeVariance;
        this.bonusType = Powerup.bonus.weapon;
        this.weapon = w;
        this.bonusName = n;
        this.child = null;
    }
    
    public void update() {
        
        this.spawnTimer -= GameHandler.frameTime;
        
        if (this.spawnTimer <= 0.0) {
            double sX = Math.random() * (PikseliPeli.width - 2*PikseliPeli.spawnMargin)
                    + PikseliPeli.spawnMargin;
            double sY = Math.random() * (PikseliPeli.height - 2*PikseliPeli.spawnMargin)
                    + PikseliPeli.spawnMargin;
            if (!GameHandler.ObjectExists(this.child)) {
                if (this.bonusType != Powerup.bonus.weapon) {
                    this.child = new Powerup(sX, sY, this.bonusType, this.bonusName);
                } else {
                    this.child = new Powerup(sX, sY, this.weapon, this.bonusName);
                }
            }
            
            this.spawnTimer = this.timeBase + (Math.random()-0.5)*this.timeVariance;
        }
    }
}
