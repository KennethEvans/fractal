package fractal.model;

import java.awt.geom.Rectangle2D;

/*
 * Created on Oct 16, 2012
 * By Kenneth Evans, Jr.
 */

/**
 * Class to manage the state of the fractal.
 * 
 * @author Kenneth Evans, Jr.
 */
public class State implements Cloneable
{
    private Rectangle2D cRect;
    private int iters;
    private double rMax;
    private int imageWidth;
    private int imageHeight;

    /**
     * State constructor.
     * 
     * @param cRect
     * @param iters
     * @param rMax
     * @param imageWidth
     * @param imageHeight
     */
    public State(Rectangle2D cRect, int iters, double rMax, int imageWidth,
        int imageHeight) {
        resetState(cRect, iters, rMax, imageWidth, imageHeight);
    }

    /**
     * Changes the fields to the given values.
     * 
     * @param cRect
     * @param iters
     * @param rMax
     * @param imageWidth
     * @param imageHeight
     */
    public void resetState(Rectangle2D cRect, int iters, double rMax,
        int imageWidth, int imageHeight) {
        this.cRect = cRect;
        this.iters = iters;
        this.rMax = rMax;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    /**
     * Changes the fields to the values in the given state.
     */
    public void resetState(State newState) {
        cRect = (Rectangle2D)newState.getcRect().clone();
        iters = newState.getIters();
        rMax = newState.getrMax();
        imageWidth = newState.getImageWidth();
        imageHeight = newState.getImageHeight();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new State((Rectangle2D)cRect.clone(), iters, rMax, imageWidth,
            imageHeight);
    }

    @Override
    public String toString() {
        String info = "";
        info += "{";
        info += String.format("cRect=(x=%g y=%g width=%g height=%g)",
            cRect.getMinX(), cRect.getMinY(), cRect.getWidth(),
            cRect.getHeight());
        info += " iters=" + iters;
        info += " rMax=" + rMax;
        info += "  imageWidth=" + imageWidth;
        info += "  imageHeight=" + imageHeight;
        info += "}";
        return info;
    }

    /**
     * @return The value of cRect.
     */
    public Rectangle2D getcRect() {
        return cRect;
    }

    /**
     * @param cRect The new value for cRect.
     */
    public void setcRect(Rectangle2D cRect) {
        this.cRect = cRect;
    }

    /**
     * @return The value of iters.
     */
    public int getIters() {
        return iters;
    }

    /**
     * @param iters The new value for iters.
     */
    public void setIters(int iters) {
        this.iters = iters;
    }

    /**
     * @return The value of rMax.
     */
    public double getrMax() {
        return rMax;
    }

    /**
     * @param rMax The new value for rMax.
     */
    public void setrMax(double rMax) {
        this.rMax = rMax;
    }

    /**
     * @return The value of imageHeight.
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @param imageHeight The new value for imageHeight.
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * @return The value of imageWidth.
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @param imageWidth The new value for imageWidth.
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

}
