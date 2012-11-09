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

    public static FractalSystem makeCubic() {
        FractalSystem sys = new FractalSystem("Cubic") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp1 = -3 * zx * zy * zy + zx * zx * zx + cx;
                zy = -zy * zy * zy + 3 * zx * zx * zy + cy;
                z.setLocation(tmp1, zy);
            }
        };
        return sys;
    }

    public static FractalSystem makeQuartic() {
        FractalSystem sys = new FractalSystem("Quartic") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp1 = zy * zy * zy * zy - 6 * zx * zx * zy * zy + zx * zx * zx
                    * zx + cx;
                zy = -4 * zx * zy * zy * zy + 4 * zx * zx * zx * zy + cy;
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

    public static FractalSystem makeDragon2() {
        // This seems to be very similar to Mandelbrot but reversed in x
        FractalSystem sys = new FractalSystem("Dragon 2") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp2 = (1 - zx) * (1 - zx) - zy * zy;
                tmp1 = cx * tmp2 + 2 * cy * (1 - zx) * zy;
                zy = cy * tmp2 - 2 * cx * (1 - zx) * zy;
                zx = tmp1;
                z.setLocation(tmp1, zy);
            }
        };
        return sys;
    }

    public static FractalSystem makeDragon3() {
        // Always 0
        FractalSystem sys = new FractalSystem("Dragon 2") {
            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                tmp2 = zx * zx - zy * zy;
                tmp1 = cx * tmp2 - 2 * cy * zx * zy;
                zy = cy * tmp2 + 2 * cx * zx * zy;
                zx = tmp1;
                z.setLocation(tmp1, zy);
            }
        };
        return sys;
    }

    public static FractalSystem makeMandelBox1() {
        FractalSystem sys = new FractalSystem("Mandelbox 2.0") {
            protected final double scale = 2.0;

            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                if(zx > 1) {
                    zx = 2 - zx;
                } else if(zx < -1) {
                    zx = -2 - zx;
                }
                if(zy > 1) {
                    zy = 2 - zy;
                } else if(zy < -1) {
                    zy = -2 - zy;
                }
                // Magnitude squared
                tmp1 = z.getX() * z.getX() + z.getY() * z.getY();
                if(tmp1 < .25) {
                    zx *= 4;
                    zy *= 4;
                } else if(tmp1 < 1) {
                    zx /= tmp1;
                    zy /= tmp1;
                }
                zx = scale * zx + cx;
                zy = scale * zy + cy;
                z.setLocation(zx, zy);
            }
        };
        return sys;
    }

    public static FractalSystem makeMandelBox2() {
        FractalSystem sys = new FractalSystem("Mandelbox -1.5") {
            protected final double scale = -1.5;

            @Override
            public void nextZ(Point2D z, double cx, double cy) {
                zx = z.getX();
                zy = z.getY();
                if(zx > 1) {
                    zx = 2 - zx;
                } else if(zx < -1) {
                    zx = -2 - zx;
                }
                if(zy > 1) {
                    zy = 2 - zy;
                } else if(zy < -1) {
                    zy = -2 - zy;
                }
                // Magnitude squared
                tmp1 = z.getX() * z.getX() + z.getY() * z.getY();
                if(tmp1 < .25) {
                    zx *= 4;
                    zy *= 4;
                } else if(tmp1 < 1) {
                    zx /= tmp1;
                    zy /= tmp1;
                }
                zx = scale * zx + cx;
                zy = scale * zy + cy;
                z.setLocation(zx, zy);
            }
        };
        return sys;
    }

}
