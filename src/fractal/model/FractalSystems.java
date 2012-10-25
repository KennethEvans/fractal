package fractal.model;

/**
 * Creates fractal systems.
 * 
 * @author Kenneth Evans, Jr.
 */
public class FractalSystems
{
    public static FractalSystem makeMandelbrodt() {
        FractalSystem sys = new FractalSystem("Mandelbrodt") {
            @Override
            public int getIters(double cx, double cy, double rMax, int iters) {
                double zx = 0;
                double zy = 0;
                double tmp;
                while(zx * zx + zy * zy < rMax && iters > 0) {
                    tmp = zx * zx - zy * zy + cx;
                    zy = 2.0 * zx * zy + cy;
                    zx = tmp;
                    iters--;
                }
                return iters;
            }
        };
        return sys;
    }

    public static FractalSystem makeDragon() {
        FractalSystem sys = new FractalSystem("Dragon") {
            @Override
            public int getIters(double cx, double cy, double rMax, int iters) {
                // // Attempt to use the inputs as the starting values with a
                // // constant c
                // double zy = y;
                // double cx = -.7;
                // double cy = -.2;
                // double tmp, tmp1;
                // while(zx * zx + zy * zy < rMax && iters > 0) {
                // tmp1 = zy * zy - zx * zx + 1;
                // tmp = cx * tmp1 + 2 * cy * zx * zy;
                // zy = cy * tmp1 - 2 * cx * zx * zy;
                // zx = tmp;
                // iters--;
                // }
                // return iters;

                double zx = cx;
                double zy = cy;
                double tmp, tmp1;
                while(zx * zx + zy * zy < rMax && iters > 0) {
                    tmp1 = zy * zy - zx * zx + 1;
                    tmp = cx * tmp1 + 2 * cy * zx * zy;
                    zy = cy * tmp1 - 2 * cx * zx * zy;
                    zx = tmp;
                    iters--;
                }
                return iters;
            }
        };
        return sys;
    }

}
