/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.jar.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;
/**
 *
 * @author User
 */
public class PixelText {
    
    public static String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,:;!?()*+-/\\[]'\"_";
    public static int[] characterSize = {3,3,3,3,3,3,3,3,1,2,3,3,5,4,4,3,4,3,3,
            3,4,3,5,3,3,3,3,1,3,3,3,3,3,3,3,3,1,1,1,1,1,2,2,2,3,3,3,3,3,2,2,1,3,3,3};
    public static int[] characterPos;
    
    public static boolean[][][] character;
    public static boolean isLoaded = false;
    
    public static BufferedImage fontImg = null;
    
    public static void init() {
        
        //try {
        //    fontImg = ImageIO.read(PixelText.class.getResource("media/font.png"));
        //} catch (IOException e) {}   

        characterPos = new int[characterSize.length];
        
        int pos = 0;
        for (int i=0;i<characterSize.length;i++) {
            characterPos[i] = pos;
            pos += characterSize[i] + 1;
        }
    }

    public static void draw(int x, int y, String str, Graphics2D g, int scale) {
        draw(x, y, str, g, scale, true);
    }
    
    public static int textWidth(String str, int scale) {
        int charIndex;
        
        int off = 0;
            for (int i = 0;i < str.length();i++) {
                charIndex = characterSet.indexOf(str.charAt(i));
                if (charIndex != -1) {
                    off += (characterSize[charIndex] + 1)*scale;
                } else {off += scale*4;}
            }
        return off;
    }
    
    public static void draw_old(int x, int y, String str, Graphics2D g, int scale, boolean center) {
        int charIndex;
        
        int w;
        
        str = str.toUpperCase();
        
        if (center) {
            int off = 0;
            for (int i = 0;i < str.length();i++) {
                charIndex = characterSet.indexOf(str.charAt(i));
                if (charIndex != -1) {
                    off += (characterSize[charIndex] + 1)*scale;
                } else {off += scale*4;}
            }
            x -= off/2;
        }
        
        int drawx = x;
        int drawy = y;
        
        for (int i = 0;i < str.length();i++) {
            
            charIndex = characterSet.indexOf(str.charAt(i));
            
            if (str.charAt(i) == '\n') {
                drawx = x;
                drawy += 8*scale;
            } else if (charIndex != -1) {
                w = (characterPos[charIndex+1]-characterPos[charIndex]);
                g.drawImage(fontImg, drawx, drawy,
                        drawx+w*scale, drawy + 6*scale,
                        characterPos[charIndex], 0,
                        characterPos[charIndex]+w, 6, null);
                drawx += w*scale;
            } else {drawx += 4*scale;}
            
        }
    }
    
    public static void draw(int x, int y, String str, Graphics2D g, int scale, boolean center) {
    if (isLoaded) {
        
        int charIndex;
        char chr;
        
        int w;
        
        str = str.toUpperCase();
        
        if (center) {
            x -= textWidth(str, scale)/2;
        }
        
        int drawx = x;
        int drawy = y;
        
        for (int i = 0;i < str.length();i++) {
            
            chr = str.charAt(i);
            charIndex = characterSet.indexOf(chr);
            
            if (charIndex == -1 && chr != ' ') {
                chr = '?';
                charIndex = characterSet.indexOf(chr);
            }
            
            if (chr == '\n') {
                drawx = x;
                drawy += 8*scale;
            } else if (charIndex != -1) {
                w = characterSize[charIndex];
                
                for (int cx = 0;cx < w;cx++) {
                    for (int cy = 0;cy < 8;cy++) {
                        
                        if (character[charIndex][cx][cy]) {
                            g.fillRect(drawx+cx*scale, drawy+cy*scale, scale, scale);
                        }
                        
                    }
                }
                
                drawx += (w+1)*scale;
            } else {
                drawx += 4*scale;
            }
            
        }
        
    }
    }
    
    public static void loadFont(String path) {
        try {
            InputStream ioStream = (PikseliPeli.class.getResourceAsStream(path));
            //FileInputStream ioStream = new FileInputStream(path);
            //Font file starts with characters BFONT
            String fPrefix = "" + (char)ioStream.read();
            fPrefix += (char)ioStream.read();
            fPrefix += (char)ioStream.read();
            fPrefix += (char)ioStream.read();
            fPrefix += (char)ioStream.read();
            
            if (fPrefix.equals("BFONT")) {
                //Read the character set
                
                //Get length
                int numCharacters = ioStream.read();
                characterSet = "";
                characterSize = new int[numCharacters];
                
                String[] dump = {"", "", "", "", "", "", "", ""};
                
                for (int i = 0;i < numCharacters;i++) {
                    characterSet = characterSet + (char)ioStream.read();
                }
                //System.out.println(characterSet);
                //Height of a stripe is always 8.
                character = new boolean[numCharacters][][];
                
                for (int i = 0;i < numCharacters;i++) {
                    int charWidth = ioStream.read();
                    character[i] = new boolean[charWidth][8];
                    characterSize[i] = charWidth;
                    for (int o = 0;o < charWidth;o++) {
                        int binInt = ioStream.read();
                        for (int b = 0;b < 8;b++) {
                            if ( (binInt & ((int)Math.pow(2, 7-b))) > 0 ) {
                                character[i][o][b] = true;
                                dump[b] = dump[b] + "X";
                            } else {
                                character[i][o][b] = false;
                                dump[b] = dump[b] + " ";
                            }
                        }
                    }
                    for (int k = 0;k < 8;k++) {
                        dump[k] += " ";
                    }
                }
                
                for (int x = 0;x < 8;x++) {
                    //System.out.println(dump[x]);
                }
                isLoaded = true;
            } else {
                System.out.println("Font loading failed!" + fPrefix);
            }
            ioStream.close();
            
        } catch (Exception ex) {
            Logger.getLogger(PixelText.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void saveFont(String path) {
        try {
            
            FileOutputStream outStream = new FileOutputStream(path);
            
            outStream.write((int)'B');
            outStream.write((int)'F');
            outStream.write((int)'O');
            outStream.write((int)'N');
            outStream.write((int)'T');
            
            String[] dump = {"", "", "", "", "", ""};
            
            outStream.write(characterSet.length());
            //Character set
            for (int i = 0;i < characterSet.length();i++) {
                outStream.write((int)characterSet.charAt(i));
            }
            
            for (int o = 0;o < characterSet.length();o++) {
                outStream.write(characterSize[o]);
                
                for (int x = 0;x < characterSize[o];x++) {
                    int outBits = 0;
                    for (int y = 0;y < 6;y++) {
                        outBits = outBits * 2;
                        int alpha = (fontImg.getRGB(characterPos[o]+x, y))&0x000000FF;
                        if ( alpha == 0 ) {
                            outBits = outBits + 1;
                            dump[y] = dump[y] + "X";
                        } else {dump[y] = dump[y] + " ";}
                        //System.out.println(""+(characterPos[o]+x) + ", " + y);
                        
                    }
                    outBits = outBits * 2;
                    outStream.write(outBits);
                    //dump[] = dump[] + " ";
                }
            }
            
            outStream.close();
            for (int i = 0;i < 6;i++) {
                System.out.println(dump[i]);
            }
            
        } catch (Exception ex){
            Logger.getLogger(PixelText.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        init();
        
        for (int y = 0;y < fontImg.getHeight();y++) {
            for (int x = 0;x < fontImg.getWidth();x++) {
                if ( ((fontImg.getRGB(x, y))&0x000000FF) == 0 ) {
                    System.out.print("X");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
        
        saveFont("font.bft");
        loadFont("font.bft");
    }
}
