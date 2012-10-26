package fractal.model;

import java.awt.geom.Point2D;

/*
 * Created on Oct 24, 2012
 * By Kenneth Evans, Jr.
 */

public abstract class FractalSystem
{
    protected String name = "Fractal System";
    // Fixed storage to avoid memory allocations
    protected double zx;
    protected double zy;
    protected double tmp1;
    protected double tmp2;

    public FractalSystem(String name) {
        this.name = name;
    }

    /**
     * Gets the next value of z.
     * 
     * @param z On input contains the current z, on output the next z.
     * @param cx
     * @param cy
     */
    public abstract void nextZ(Point2D z, double cx, double cy);

    /**
     * @return The value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The new value for name.
     */
    public void setName(String name) {
        this.name = name;
    }

}
