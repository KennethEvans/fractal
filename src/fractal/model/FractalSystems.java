package fractal.model;

import java.awt.geom.Point2D;

/**
 * Creates fractal systems.
 * 
 * @author Kenneth Evans, Jr.
 */
public class FractalSystems
{
    public static FractalSystem makeMandelbrot() {
        FractalSystem sys = new FractalSystem("Mandelbrot") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp1 = zx * zx - zy * zy + cx;
                zy = 2.0 * zx * zy + cy;
                z.setLocation(tmp1, zy);
            }
        };
        return sys;
    }

    public static FractalSystem makeCosine() {
        FractalSystem sys = new FractalSystem("Cosine") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp1 = Math.cos(zx) * Math.cosh(zy) + cx;
                zy = -Math.sin(zx) * Math.sinh(zy) + cy;
                z.setLocation(tmp1, zy);
            }
        };
        return sys;
    }

    public static FractalSystem makeHyperbolicCosine() {
        FractalSystem sys = new FractalSystem("Hyperbolic Cosine") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp1 = Math.cosh(zx) * Math.cos(zy) + cx;
                zy = Math.sinh(zx) * Math.sin(zy) + cy;
                z.setLocation(tmp1, zy);
            }
        };
        return sys;
    }

    public static FractalSystem makeSine() {
        FractalSystem sys = new FractalSystem("Sine") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp1 = Math.sin(zx) * Math.cosh(zy) + cx;
                zy = Math.cos(zx) * Math.sinh(zy) + cy;
                z.setLocation(tmp1, zy);
            }
        };
        return sys;
    }

    public static FractalSystem makeHyperbolicSine() {
        FractalSystem sys = new FractalSystem("Hyperbolic Sine") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp1 = Math.sinh(zx) * Math.cos(zy) + cx;
                zy = Math.cosh(zx) * Math.sin(zy) + cy;
                z.setLocation(tmp1, zy);
            }
        };
        return sys;
    }

    public static FractalSystem makeDragon() {
        FractalSystem sys = new FractalSystem("Dragon") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp2 = zy * zy - zx * zx + 1;
                tmp1 = cx * tmp2 + 2 * cy * zx * zy;
                zy = cy * tmp2 - 2 * cx * zx * zy;
                zx = tmp1;
                z.setLocation(tmp1, zy);
            }
        };
        return sys;
    }

}
