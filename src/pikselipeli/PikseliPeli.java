package pikselipeli;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.ArrayList;
import java.net.*;
import java.io.*;

public class PikseliPeli {

    public static int width = 800;
    public static int height = 600;
    public static boolean FullScreen = false;
    
    public static int spawnMargin = 120;
    public static int borderMargin = 100;
    
    static JFrame frame;
    static Screen screen;
    static KeyHandler keyboard;
    
    static boolean isPaused = false;
    
    public static void init() {
        frame = new JFrame("PikseliPeli");
        
        //frame.setPreferredSize(new Dimension(width+6, height+40));
        frame.setResizable(false);
        
        if (FullScreen) {
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.setUndecorated(true);
        }
        
        screen = new Screen();
        screen.setPreferredSize(new Dimension(width, height));
        //frame.setBackground(Color.WHITE);
        (frame.getContentPane()).add(screen);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        keyboard = new KeyHandler();
        frame.addKeyListener(keyboard);
        
        frame.pack();
        
        frame.setVisible(true);
        
    }
    
    public static void main(String[] args) {
        
        if (args.length > 0) {
            if (args[0].equals("-update")) {
                System.exit(0);
            }
        }
        
        init();
        
        Audio.loadSoundFX();
        Audio.setMasterVolume(-10f);
        
        GameHandler.init();
        
        PixelText.loadFont("media/font.bft");
        
        int maxPlayers = 10;
        
        ArrayList<Hahmo> players = new ArrayList<Hahmo>();
        ArrayList<Integer> keys = new ArrayList<Integer>();
        ArrayList<String> names = new ArrayList<String>();
        
        double px, py;

        
        new PowerupSpawner(4.0, 10.0, Powerup.bonus.chaosmode, "BOUNCY BULLETS");
        //new PowerupSpawner(6.0, 0, Weapon.Rifle.class, "RIFLE");
        new PowerupSpawner(6.0, 20.0, Weapon.Shotgun.class, "SHOTGUN");
        
        //new PowerupSpawner(1.0, 1.0, Weapon.Laser.class, "LASER");
        while (!keyboard.keyUp(KeyEvent.VK_ESCAPE)) {
            
            //Tarkista, onko uusi nappi painettu
            //Lisää pelaaja sen mukaan
            for (int i = 0; i <= KeyEvent.KEY_LAST;i++) {
                boolean isIncluded = false;
                for (int o = 0; o < players.size();o++) {
                    if (keys.get(o) == i) {isIncluded = true;break;}
                }
                if (!isIncluded && keyboard.keyHit(i)
                        && players.size() < maxPlayers
                        && i != KeyEvent.VK_PAUSE
                        && i != KeyEvent.VK_ESCAPE) {
                    keys.add(i);
                    names.add(KeyEvent.getKeyText(i));
                    
                    px = Math.random() * (width - spawnMargin*2) + spawnMargin;
                    py = Math.random() * (height - spawnMargin*2) + spawnMargin;
                    
                    Hahmo h = new Hahmo(px, py, names.get(players.size()));
                    LineFX.spawnLines(px, py, 600, 0, 360, 300, 15, 0.03);
                    Audio.playSound(Audio.sndSpawn);
                    new FlashText((int)px, (int)py - 35, 
                            KeyEvent.getKeyText(i) +" has joined the \'game\'!",
                            3, 4.0);
                    players.add(h);
                    h.respawnTime = 2;
                    
                    break;
                }
            }
            
            //Tauko
            if (keyboard.keyUp(KeyEvent.VK_PAUSE)) {
                isPaused = !isPaused;
                GameHandler.resetTime();
                if (isPaused) {PixelText.draw(width/2, height/2-100, "PAUSED", GameHandler.bg, 20, true);}
            }

            //Päivitä kaikki
            if(!isPaused && players.size() >= 1) {
                for (int i = 0; i < players.size(); i+=1) {
                    
                    if (keyboard.keyHeld(keys.get(i))) {players.get(i).move2();}
                    
                    //Poistu pelistä, jos pidetään yli 30 sek.
                    if (false && players.get(i).isDull) {
                        players.get(i).destroy();
                        players.remove(i);
                        keys.remove(i);
                        names.remove(i);
                    }
                }
                GameHandler.updateAll(keyboard, 1);
            } else {}
            
            GameHandler.drawAll();
            
            GameHandler.bg.setColor(Color.BLACK);
            
            PixelText.draw(width/2, 20, "PIKSELIPELI", GameHandler.bg, 10);
            GameHandler.bg.setColor(Color.LIGHT_GRAY);
            PixelText.draw(width/2 - 
                    PixelText.textWidth("PIKSELIPELI", 10)/2
                    - PixelText.textWidth("Se tietty ", 1),
                    60, "Se tietty", GameHandler.bg, 1, false);
            //Pistetaulukko
            GameHandler.bg.setColor(Color.BLACK);
            PixelText.draw(5, 30, "SCORES: ", GameHandler.bg, 3, false);
            GameHandler.bg.drawRect(borderMargin, borderMargin, width-borderMargin*2-1, height-borderMargin*2-1);
            
            for (int i = 0; i < players.size(); i+=1) {
                    PixelText.draw(15+80*(i/8), 80+(i%8)*60, players.get(i).name + ": "
                            + players.get(i).score, GameHandler.bg, 3, false);
                    
            }
            
            PixelText.draw(width-PixelText.textWidth("Praash 2013+", 1)-20, height-20, "Praash 2013+", GameHandler.bg, 1, false);
            
            keyboard.refresh();
            
            screen.repaint();
            
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException ex) {
                //Logger.getLogger(PikseliPeli.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        frame.dispose();
    }
    
}

class Screen extends JPanel {
    
    public void paintComponent(Graphics g) {
        g = (Graphics2D) g;
        g.clearRect(0, 0, PikseliPeli.width, PikseliPeli.height);
        g.drawImage(GameHandler.buffer, 0, 0, null);
    }
    
}
