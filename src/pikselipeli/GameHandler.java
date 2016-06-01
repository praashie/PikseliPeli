/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author User
 */
class GameHandler {
    public static ArrayList<GameObject> updateList;
    public static ArrayList<GameObject> addList;
    public static ArrayList<GameObject> removeList;
    public static double frameTime;
    public static long lastFrame;
    public static BufferedImage buffer;
    public static Graphics2D bg;

    public static void init() {
        updateList = new ArrayList<GameObject>();
        removeList = new ArrayList<GameObject>();
        addList = new ArrayList<GameObject>();
        lastFrame = System.currentTimeMillis();
        buffer = new BufferedImage(PikseliPeli.width, PikseliPeli.height,
                BufferedImage.TYPE_INT_RGB);
        bg = buffer.createGraphics();
        PixelText.init();
        updateAll(null);
    }
    
    public static boolean ObjectExists(GameObject o) {
        if (updateList.contains(o)) {return true;}
        else {return false;}
    }
    public static void resetTime() {
        lastFrame = System.currentTimeMillis();
    }

    public static void update(KeyHandler keyboard) {
        //frameTime = (double) (System.currentTimeMillis() - lastFrame) / 1000.0;
        //Add newborns
        for (GameObject o : addList) {
            updateList.add(o);
        }
        addList.clear();
        //Do necessary updates
        for (GameObject o : updateList) {
            o.update();
        }
        //Remove garbage
        for (GameObject o : removeList) {
            updateList.remove(o);
        }
        removeList.clear();
        //resetTime();
    }

    public static void updateAll(KeyHandler keyboard) {
        updateAll(keyboard, 1);
    }
    public static void updateAll(KeyHandler keyboard, int updates) {
        bg.setBackground(Color.WHITE);
        bg.clearRect(0, 0, PikseliPeli.width, PikseliPeli.height);
        
        for (int i = 1;i<=updates;i++) {
            frameTime = (double) (System.currentTimeMillis() - lastFrame) / 1000.0;
            frameTime = Math.min(frameTime, 0.25); //Limit speed during lag spikes
            resetTime();
            update(keyboard);
            LineFX.update();
            
        }
        //bg.drawImage(dump, 0, 0, null);
        for (GameObject o : updateList) {
            o.clearInput();
        }
        
    }

    public static void drawAll() {
        if (updateList != null){
            for (GameObject o : updateList) {
                o.draw(bg);
            }
        }
        LineFX.draw(bg);
    }
    
}
