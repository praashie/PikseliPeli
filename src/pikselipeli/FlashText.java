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
public class FlashText extends GameObject {
    
    public double fadeTime;
    public double velY;
    public String text;
    public int size;
    
    public FlashText(int x, int y, String str, int s, double fadeTime) {
        super();
        this.x = x;
        this.y = y;
        this.text = str;
        this.size = s;
        this.velY = -12.0;
        this.fadeTime = fadeTime;
        
        if ((x + str.length()*s*4) > PikseliPeli.width ) {
            this.x = PikseliPeli.width - (str.length()*s*3);
        }
        if (x < 0) {this.x = 0;}
    }
    
    @Override
    public void update() {
        this.fadeTime -= GameHandler.frameTime;
        this.y += velY * GameHandler.frameTime;
        if (this.fadeTime <= 0) {destroy();}
    }
    
    @Override
    public void draw(Graphics2D g) {
        /*
        if ( ((int)(fadeTime*1000)%100)<50 ) {
            PixelText.draw((int)(this.x), (int)this.y, this.text, g, this.size);
        }
        */
        int c = (int)Math.max(0, Math.min(255, (
                ((int)(fadeTime*1000)%100)*2.5+255*(1-Math.max(0.0, Math.min(1.0, 
                    fadeTime)))
                )));
        g.setColor(new Color(c, c, c));
        PixelText.draw((int)(this.x), (int)this.y, this.text, g, this.size);
    }
    
}
