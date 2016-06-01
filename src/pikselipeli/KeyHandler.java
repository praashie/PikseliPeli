/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

//NÄPPÄIMISTÖN KÄSITTELIJÄ

//KeyHit bugaa! :C
class KeyHandler implements KeyListener {
    private ArrayList<Integer> keyPressQueue = new ArrayList<Integer>();
    private ArrayList<Integer> keyReleaseQueue = new ArrayList<Integer>();
    private ArrayList<Integer> keyPress = new ArrayList<Integer>();
    private ArrayList<Integer> keyRelease = new ArrayList<Integer>();
    private ArrayList<Integer> keyHold = new ArrayList<Integer>();

    //-------------------
    //Tapahtumien kirjaus
    @Override
    public void keyPressed(KeyEvent e) {
        keyPressQueue.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyReleaseQueue.add(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    //------------------

    public boolean keyHeld(int k) {
        return keyHold.contains(k);
    }

    public boolean keyHit(int k) {
        return keyPress.contains(k);
    }

    public boolean keyUp(int k) {
        return keyRelease.contains(k);
    }

    //Päivitä puskuri
    public void refresh() {
        keyPress.clear();
        keyRelease.clear();
        
        try {
            for (int k : keyPressQueue) {
                if (!keyHold.contains(k)) {
                    keyHold.add(k);
                    keyPress.add(k);
                }
            }
            for (int k : keyReleaseQueue) {
                if (!keyRelease.contains(k) && keyHold.contains(k)) {
                    keyRelease.add(k);
                }
                Integer o = k;
                if (keyHold.contains(k)) {
                    keyHold.remove(o);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        keyPressQueue.clear();
        keyReleaseQueue.clear();
    }
    
}
