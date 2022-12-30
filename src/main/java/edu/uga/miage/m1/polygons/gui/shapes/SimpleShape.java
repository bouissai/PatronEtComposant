package edu.uga.miage.m1.polygons.gui.shapes;

import edu.uga.miage.m1.polygons.gui.persistence.Visitable;

import java.awt.*;


/**
 * This interface defines the <tt>SimpleShape</tt> extension. This extension
 * is used to draw shapes.
 *
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public abstract class SimpleShape implements Visitable {

    private int mX;

    private int mY;

    protected SimpleShape(int x, int y) {
        setXY(x, y);
    }

    /**
     * Method to draw the shape of the extension.
     *
     * @param g2 The graphics object used for painting.
     */
    public abstract void draw(Graphics2D g2);

    public abstract String getType();

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public void setXY(int x, int y) {
        this.mX = x;
        this.mY = y;
    }
}
