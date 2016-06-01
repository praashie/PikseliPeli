/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author User
 */
class Hahmo extends GameObject {
    public static double prespawnDuration = 3.5;
    
    public double health;
    public short score;
    public Weapon defaultWeapon;
    public Weapon usedWeapon;
    
    public short lifekills;
    public int lifeShots;
    public short killspree;
    public String name;
    public double respawnTime;
    public double scoreFlash;
    public double xVel;
    public double yVel;
    public double angle;
    public double oldAngle;
    public double angleVel;
    public boolean isMoved;
    public boolean turnCW;
    public boolean isStopped;
    public boolean isStarted;
    public boolean isDead;
    public boolean shouldMove;
    public boolean isDull;
    public double hitTimer;

    public Hahmo(double x, double y, String n) {
        super();
        this.health = 100;
        this.defaultWeapon = new Weapon.Pistol();
        this.usedWeapon = defaultWeapon;
        this.name = n;
        this.score = 0;
        this.lifekills = 0;
        this.lifeShots = 0;
        this.x = x;
        this.y = y;
        this.angle = Math.random() * 360.0;
        this.oldAngle = this.angle;
        this.isMoved = false;
        this.isStopped = true;
        this.turnCW = true;
        this.hitTimer = 0;
    }

    public void move(double a, double b) {
        this.angleVel += a * GameHandler.frameTime;
        this.xVel += cos2(this.angle) * b * GameHandler.frameTime;
        this.yVel += sin2(this.angle) * b * GameHandler.frameTime;
    }

    public void move2() {
        this.shouldMove = true;
    }

    public void doMove() {
        this.isMoved = true;
        this.isStopped = false;
        
        
        if (!this.isStarted) {
            this.hitTimer = 0;
            this.isStarted = true;
        } else if (this.hitTimer > 0) {
            if (this.turnCW) {
                move(2600.0*this.usedWeapon.aimWeight, 800*this.usedWeapon.moveWeight);
            } else {
                move((-2600.0)*this.usedWeapon.aimWeight, 800*this.usedWeapon.moveWeight);
            }
        }
    }

    public void score(Hahmo victim) {
        
        if (this != victim) {
            this.score += 1;
            scoreFlash += 1.3;
            this.lifekills += 1;
            new FlashText((int)this.x+17, (int)this.y-33, "+1", 2, 2.0);
            
            if (lifekills % 3 == 0) {
                new FlashText((int)this.x - 50, (int)this.y-63,
                        this.name + " has " + this.lifekills + " kills in a row!",
                        3, 4.0);
                new FlashText((int)this.x - 20, (int)this.y+63,
                        "+1 Spree bonus",
                        2, 4.0);
                this.score += 1;
            }
        
        } else {
            this.score -= 1;
            scoreFlash += 1.3;
            new FlashText((int)this.x-27, (int)this.y-33, "SUICIDE!! -1", 2, 2.0);
        }
        
    }
    @Override
    public void update() {
        if (respawnTime <= prespawnDuration) {
            if (this.shouldMove) {
                doMove();
            }
            this.x += this.xVel * GameHandler.frameTime;
            this.y += this.yVel * GameHandler.frameTime;
            this.angle += this.angleVel * GameHandler.frameTime;

            this.xVel *= Math.pow(0.12, GameHandler.frameTime);
            this.yVel *= Math.pow(0.12, GameHandler.frameTime);
            
            this.angleVel *= Math.pow(0.00005, GameHandler.frameTime);
            
            this.hitTimer += GameHandler.frameTime;
            
            if (this.hitTimer > 30.0) {
                this.isDull = true;
            }
            
            if (!this.isMoved && !this.isStopped) {
                this.turnCW = !this.turnCW;
                this.isDull = false;
                if ((this.hitTimer) < 0.11 && this.respawnTime <= 0) {
                    
                    if (!this.usedWeapon.hasAmmo()) {
                        this.defaultWeapon.chaosAmmo += this.usedWeapon.chaosAmmo;
                        this.usedWeapon = this.defaultWeapon;
                    }
                    this.defaultWeapon.addAmmo(999);
                    this.usedWeapon.pullTrigger(this);

                    //this.turnCW = !this.turnCW;
                }
                this.isStopped = true;
                
            } else if (!this.isMoved) {
                this.isStarted = false;
            }
            
            this.usedWeapon.update(this);
            
            if (this.x < (PikseliPeli.borderMargin+8)) {
                this.x = PikseliPeli.borderMargin+8;
                this.xVel = Math.abs(this.xVel)*1;
            }
            if (this.x > (PikseliPeli.width - PikseliPeli.borderMargin-8)) {
                this.x = PikseliPeli.width - PikseliPeli.borderMargin-8;
                this.xVel = Math.abs(this.xVel)*(-1);
            }
            if (this.y < (PikseliPeli.borderMargin+8)) {
                this.y = PikseliPeli.borderMargin+8;
                this.yVel = Math.abs(this.yVel)*1;
            }
            if (this.y > (PikseliPeli.height - PikseliPeli.borderMargin-8)) {
                this.y = PikseliPeli.height - PikseliPeli.borderMargin-8;
                this.yVel = Math.abs(this.yVel)*(-1);
            }
            
            this.isMoved = false;
            
            if (scoreFlash > 0) {scoreFlash -= GameHandler.frameTime;
            } else {scoreFlash = 0;}
            
            if (health <= 0) {
                Audio.playSound(Audio.sndDeath);
                LineFX.spawnLines(x, y, 200, 0, 360, 150, 18, 0.001);
                respawnTime = 10;
                this.lifekills = 0;
                this.lifeShots = 0;
                this.usedWeapon = defaultWeapon;
                this.x = Math.random() * (PikseliPeli.width-PikseliPeli.borderMargin*2) + PikseliPeli.borderMargin;
                this.y = Math.random() * (PikseliPeli.height-PikseliPeli.borderMargin*2) + PikseliPeli.borderMargin;
            }
        }
        if (respawnTime > 0) {
            respawnTime -= GameHandler.frameTime;
        }
        if (respawnTime <= prespawnDuration && health <= 0) {
            health = 100;
            this.xVel = 0;
            this.yVel = 0;
            Audio.playSound(Audio.sndSpawn);
        }
    }

    public void clearInput() {
        this.shouldMove = false;
    }

    @Override
    public void draw(Graphics2D g) {
        if (respawnTime <= prespawnDuration && (Math.max(0, (int) (respawnTime * 1000)) % 100) < 50) {
            g.setColor(Color.BLACK);
            //Body
            g.drawOval((int) this.x - 8, (int) this.y - 8, 16, 16);
            g.drawLine((int) (this.x + cos2(this.angle) * 8), (int) (this.y + sin2(this.angle) * 8), (int) (this.x + cos2(this.angle) * 16), (int) (this.y + sin2(this.angle) * 16));
            
            //Health bar
            if (this.health > 60 || (System.currentTimeMillis() % 100) < 50) {
                g.drawRect((int) this.x - 18, (int) this.y - 26, 35, 7);
            }
            g.fillRect((int) this.x - 16, (int) this.y - 24, (int) (health / 100.0 * 32.0), 4);
            //Name
            PixelText.draw((int) this.x, (int) this.y - 48, this.name, g, 2, true);
            //Turning indicator
            if (this.turnCW) {
                g.drawRect((int)(this.x + cos2(this.angle+40) * 17 - 1),
                        (int)(this.y + sin2(this.angle+40) * 17 - 1), 1, 1);
            } else {
                g.drawRect((int)(this.x + cos2(this.angle-40) * 17 - 1),
                        (int)(this.y + sin2(this.angle-40) * 17 - 1), 1, 1);
            }
            
            //Score
            if (scoreFlash <= 0 || (((int)(scoreFlash*1000))%100)<50) {
                PixelText.draw((int) this.x, (int) this.y - 35, "PTS:" + this.score, g, 1, true);
                
            }
            
            //Weapon
            this.usedWeapon.draw(this, g);
            
            this.oldAngle = this.angle;
        }
    }
    
}
