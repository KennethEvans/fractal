package fractal.model;

/*
 * Created on Oct 24, 2012
 * By Kenneth Evans, Jr.
 */

public abstract class FractalSystem
{
    protected String name = "Fractal System";

    public FractalSystem(String name) {
        this.name = name;
    }

    /**
     * Iterates over the recursion algorithm starting at iters and decreasing to
     * zero. Returns the iteration number for which the modulus squared of z
     * exceeds rMax or the iteration number becomes 0.
     * 
     * @param x Value of x.
     * @param y Value of y.
     * @param rMax Escape value for the modulus squared of z.
     * @param iters The maximum number of iterations.
     * @return The iteration number.
     */
    public abstract int getIters(double x, double y, double rMax, int iters);

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
