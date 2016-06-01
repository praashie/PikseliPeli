/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pikselipeli;

import java.awt.Graphics2D;
import java.awt.Image;

/**
 *
 * @author User
 */
class GameObject {
    public double x;
    public double y;
    public Image sprite;

    public GameObject() {
        this.addtoList();
    }

    private final void addtoList() {
        GameHandler.addList.add(this);
    }

    public final void destroy() {
        GameHandler.removeList.add(this);
    }

    public void update() {
    }

    public void clearInput() {
    }

    public void draw(Graphics2D g) {
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
    
}
