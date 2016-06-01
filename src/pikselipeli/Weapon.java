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
public abstract class Weapon {
    
    public static int maxAmmo;
    public double aimWeight = 1.0;
    public double moveWeight = 1.0;
    public int ammoLeft;
    public int chaosAmmo;
    
    public abstract void pullTrigger(Hahmo user);
    public abstract void draw(Hahmo user, Graphics2D g);
    public abstract void update(Hahmo user);
    
    public boolean hasAmmo() {
        return (this.ammoLeft > 0);
    }
    public void addAmmo(int a) {
        this.ammoLeft = Math.min(this.ammoLeft + a, maxAmmo);
    }
    public void addChaosAmmo(int a) {
        this.chaosAmmo = Math.max(0, this.chaosAmmo);
        this.chaosAmmo += a;
    }
    public void useAmmo(int a) {
        this.ammoLeft -= a;
        this.chaosAmmo -= a;
    }
    public void useAmmo(int a, int c) {
        this.ammoLeft -= a;
        this.chaosAmmo -= c;
    }
    
    public static double getAngle(double x1, double y1, double x2, double y2) {
        return Math.atan2(y2 - y1, x2 - x1) * 180.0 / Math.PI;
    }

    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(Math.abs(x2 - x1), 2) + Math.pow(Math.abs(y1 - y2), 2));
    }

    public static double cos2(double a) {
        return Math.cos(Math.toRadians(a));
    }

    public static double sin2(double a) {
        return Math.sin(Math.toRadians(a));
    }
    
    public static double wrapAngle(double a) {
        if (a > 0) {
            a = a - Math.floor(a / 360.0)*360.0;
        } else {
            a = a + Math.floor(a / (-360.0) + 1)*360.0;
        }
        return a;
    }
    
    public static class Shotgun extends Weapon {
        
        {maxAmmo = 18;}
        public int shotCounter;
        
        public Shotgun() {
            this.ammoLeft = 0;
            this.shotCounter = 0;
        }
        public void pullTrigger(Hahmo user) {
            double decay = 0.04;
            if (this.chaosAmmo > 0) {decay = 0.6;}
            if (hasAmmo()) {
                double oS = -10.0;
                if (this.shotCounter % 3 == 0) {
                    if (this.chaosAmmo > 0) {Audio.playSound(Audio.sndChaosShot);}
                    for (int i=1;i<9;i++) {
                        Luoti l = new Luoti(user.x + cos2(user.angle+oS) * 12, user.y + sin2(user.angle+oS) * 12,
                            user.angle+oS, 900.0 + Math.random()*400.0 - 200.0, 9, decay, true);
                        l.showTrails = true;
                        if (!(this.chaosAmmo > 0)) {l.bounceSound = Audio.sndShotgunClick1;}
                        l.sender = user;
                        oS = oS + 20.0/9.0;
                    }

                    user.xVel += cos2(user.angle + 180) * 80;
                    user.yVel += sin2(user.angle + 180) * 80;

                    LineFX.spawnLines(user.x + cos2(user.angle) * 20,
                            user.y + sin2(user.angle) * 20, 180, user.angle+70, 30, 150, 5, 0.003);
                    LineFX.spawnLines(user.x + cos2(user.angle) * 20,
                            user.y + sin2(user.angle) * 20, 180, user.angle-70, 30, 150, 5, 0.003);
                    LineFX.spawnLines(user.x + cos2(user.angle) * 20,
                            user.y + sin2(user.angle) * 20, 430, user.angle, 60, 250, 5, 0.003);
                    
                    Audio.playSound(Audio.sndShotgun);

                } else if (this.shotCounter % 3 == 1) {
                    Audio.playSound(Audio.sndShotgunClick1);
                } else {
                    Audio.playSound(Audio.sndShotgunClick2);
                    useAmmo(1);}

                this.shotCounter += 1;
            }
        }
        
        @Override
        public void draw(Hahmo user, Graphics2D g) {}
        public void update(Hahmo user) {}
    }
    
    public static class Pistol extends Weapon {
        {maxAmmo = 1;}
        
        public Pistol() {
            this.ammoLeft = 1;
        }
        public void pullTrigger(Hahmo user) {
            
            Luoti l = new Luoti(user.x + cos2(user.angle) * 12, user.y + sin2(user.angle) * 12,
                    user.angle, 500.0, 17, 0.93, this.chaosAmmo > 0);
            l.sender = user;
            this.chaosAmmo -= 1;
            
            Audio.playSound(Audio.sndShoot);
            LineFX.spawnLines(user.x + cos2(user.angle) * 20,
                user.y + sin2(user.angle) * 20, 300, user.angle, 20, 150, 2, 0.01);
            user.xVel += cos2(user.angle + 180) * 30;
            user.yVel += sin2(user.angle + 180) * 30;
            
        }
        
        public void draw(Hahmo user, Graphics2D g) {}
        public void update(Hahmo user) {}
        
    }
    
    public static class Rifle extends Weapon {
        {maxAmmo = 4;}
        public boolean isZoomed;
        
        public Rifle() {
            this.isZoomed = false;
        }
        public void pullTrigger(Hahmo user) {
            /*if (!isZoomed) {
                Audio.playSound(Audio.sndRifleZoom);
                this.aimWeight = 0.1;
                this.moveWeight = 0.0;
                isZoomed = true;
            } else {*/
                if (this.ammoLeft > 0) {
                    Luoti l = new Luoti(user.x + cos2(user.angle) * 12, user.y + sin2(user.angle) * 12,
                    user.angle, 900.0, 84, 0.7, this.chaosAmmo > 0);
                    l.sender = user;
                    
                    Audio.playSound(Audio.sndRifle);
                    if (this.chaosAmmo > 0) {
                        Audio.playSound(Audio.sndChaosShot);
                    }
                    
                    user.xVel += cos2(user.angle + 180) * 160;
                    user.yVel += sin2(user.angle + 180) * 160;
                    
                    useAmmo(1, 10);
                    this.isZoomed = false;
                    this.aimWeight = 1.0;
                    this.moveWeight = 1.0;
                }
            //}
        }
        
        public void update(Hahmo user) {}
        public void draw(Hahmo user, Graphics2D g) {
            /*if (isZoomed) {
                
                int[] lX = {(int)(user.x + cos2(user.angle)*10),
                            (int)(user.x + cos2(user.angle)*1000), 
                            (int)(user.x + cos2(user.oldAngle)*1000)};
                int[] lY = {(int)(user.y + sin2(user.angle)*10), 
                            (int)(user.y + sin2(user.angle)*1000),
                            (int)(user.y + sin2(user.oldAngle)*1000)};
                g.setColor(new Color(255, 126, 126));
                g.fillPolygon(lX, lY, 3);
                g.setColor(new Color(255, 26, 26));
                g.drawPolygon(lX, lY, 3);
           */     
                g.setColor(new Color(196, 196, 196));
                g.drawLine((int)(user.x + cos2(user.angle)*16),
                            (int)(user.y + sin2(user.angle)*16), 
                            (int)(user.x + cos2(user.angle)*1000), 
                            (int)(user.y + sin2(user.oldAngle)*1000));
            //}
        }
    }
    
    public static class Laser extends Weapon {
        
        public boolean isFiring;
        public int x1;
        public int y1;
        public int x2;
        public int y2;
        
        {maxAmmo = 10000;}
        
        public Laser() {
            isFiring = false;
        }
        
        public void pullTrigger(Hahmo user) {
            isFiring = !isFiring;
            if (ammoLeft <= 0) {isFiring = false;}
        }
        
        public void update(Hahmo user) {
            
            if (isFiring) {
                
                x1 = (int) (user.x + cos2(user.angle) * 12);
                y1 = (int) (user.y + sin2(user.angle) * 12);
                
                double maxDist = 9999;
                double hDist = 0.0;
                Hahmo mDObj = null;
                
                for (GameObject o: GameHandler.updateList) {
                    
                    if (o.getClass() == Hahmo.class && o != user && ((Hahmo) o).health > 0) {
                        Hahmo h = (Hahmo) o;
                        double aDiff = wrapAngle(Math.abs(wrapAngle(user.angle)+360.0 - getAngle(
                                user.x, user.y, h.x, h.y)));
                        //double aDiff = wrapAngle(user.angle) - getAngle(
                        //        user.x, user.y, h.x, h.y);
                        double dist = getDistance(user.x, user.y, h.x, h.y);
                        
                        if ( Math.abs(sin2(aDiff) * dist) < 8 && aDiff < 90) {
                            if (dist < maxDist) {
                                hDist = cos2(aDiff)*dist;
                                maxDist = dist;
                                mDObj = h;
                            }
                        }
                        
                    }
                    
                    if (mDObj != null) {
                        
                        mDObj.health -= 10.0*GameHandler.frameTime;
                        x2 = (int)(user.x + cos2(user.angle) * hDist);
                        y2 = (int)(user.y + sin2(user.angle) * hDist);
                        
                        if (mDObj.health <= 0) {
                            (user).score(mDObj);
                            new FlashText((int)mDObj.x-40, (int)mDObj.y+20, "KILLED BY " + (user.name), 1, 4.0);
                        }
                    } else {
                        x2 = (int)(user.x + cos2(user.angle) * 9999);
                        y2 = (int)(user.y + sin2(user.angle) * 9999);
                    }
                }
                useAmmo((int) (GameHandler.frameTime * 100) );
                if (ammoLeft <= 0) {isFiring = false;}
            }
            
        }
        public void draw(Hahmo user, Graphics2D g) {
            if (isFiring) {
                g.setColor(new Color(160, 0, 0));
                g.drawLine(x1, y1, x2, y2);
            }
        }
        
    }
    
    
}
