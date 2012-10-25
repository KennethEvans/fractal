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
            public double[] nextZ(double zx, double zy, double cx, double cy) {
                tmp1 = zx * zx - zy * zy + cx;
                zy = 2.0 * zx * zy + cy;
                return new double[] {tmp1, zy};
            }
        };
        return sys;
    }

    public static FractalSystem makeDragon() {
        FractalSystem sys = new FractalSystem("Dragon") {
            @Override
            public double[] nextZ(double zx, double zy, double cx, double cy) {
                tmp2 = zy * zy - zx * zx + 1;
                tmp1 = cx * tmp2 + 2 * cy * zx * zy;
                zy = cy * tmp2 - 2 * cx * zx * zy;
                zx = tmp1;
                return new double[] {tmp1, zy};
            }
        };
        return sys;
    }

}
