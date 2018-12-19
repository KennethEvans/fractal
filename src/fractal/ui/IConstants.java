package fractal.ui;

/**
 * Provides constants for classes related to fractals.
 * 
 * @author Kenneth Evans, Jr.
 */
public interface IConstants
{
    // For testing
    public static final String iccFileName = "C:/Windows/System32/spool/drivers/color/xRite-2012-09-28-6500-2.2-090.icc";

    public static final String LS = System.getProperty("line.separator");

    // Used for scheme selection
    // Change FractalBrowser.setColorScheme and ColorSchemes when changes are
    // made
    public static final int SCHEME_RAINBOW1 = 0;
    public static final int SCHEME_RAINBOW2 = 1;
    public static final int SCHEME_REPEAT8 = 2;
    public static final int SCHEME_LINEAR = 3;
    public static final int SCHEME_REVERSE_LINEAR = 4;
    public static final int SCHEME_REDS = 5;
    public static final int SCHEME_GREENS = 6;
    public static final int SCHEME_BLUES = 7;
    public static final int SCHEME_SPRING = 8;
    public static final int SCHEME_SUMMER = 9;
    public static final int SCHEME_AUTUMN = 10;
    public static final int SCHEME_WINTER = 11;
    public static final int SCHEME_PASTEL = 12;
    public static final int SCHEME_BW = 13;
    public static final int SCHEME_GRAYSCALE = 14;
    public static final String[][] colorSchemeValues = {
        {"Rainbow 1", Integer.toString(SCHEME_RAINBOW1)},
        {"Rainbow 2", Integer.toString(SCHEME_RAINBOW2)},
        {"Repeat 8", Integer.toString(SCHEME_REPEAT8)},
        {"Linear", Integer.toString(SCHEME_LINEAR)},
        {"Reverse Linear", Integer.toString(SCHEME_REVERSE_LINEAR)},
        {"Reds", Integer.toString(SCHEME_REDS)},
        {"Greens", Integer.toString(SCHEME_GREENS)},
        {"Blues", Integer.toString(SCHEME_BLUES)},
        {"Spring", Integer.toString(SCHEME_SPRING)},
        {"Summer", Integer.toString(SCHEME_SUMMER)},
        {"Autumn", Integer.toString(SCHEME_AUTUMN)},
        {"Winter", Integer.toString(SCHEME_WINTER)},
        {"Pastel", Integer.toString(SCHEME_PASTEL)},
        {"Black & White", Integer.toString(SCHEME_BW)},
        {"Grayscale", Integer.toString(SCHEME_GRAYSCALE)},};
    public static final int N_COLOR_SCHEMES = colorSchemeValues.length;

    // Used for system selection
    // Change FractalBrowser.setSystem and FractalSystems when changes are
    // made
    public static final int IFS_MANDELBROT = 0;
    public static final int IFS_CUBIC = 1;
    public static final int IFS_QUARTIC = 2;
    public static final int IFS_POLY1 = 3;
    public static final int IFS_COS = 4;
    public static final int IFS_SIN = 5;
    public static final int IFS_COSH = 6;
    public static final int IFS_SINH = 7;
    public static final int IFS_MANDELBOX1 = 8;
    public static final int IFS_MANDELBOX2 = 9;
    public static final String[][] systemValues = {
        {"Mandelbrot", Integer.toString(IFS_MANDELBROT)},
        {"Cubic", Integer.toString(IFS_CUBIC)},
        {"Quartic", Integer.toString(IFS_QUARTIC)},
        {"Poly", Integer.toString(IFS_POLY1)},
        {"Cosine", Integer.toString(IFS_COS)},
        {"Sine", Integer.toString(IFS_SIN)},
        {"Hyperbolic Cosine", Integer.toString(IFS_COSH)},
        {"Hyperbolic Sine", Integer.toString(IFS_SINH)},
        {"Mandelbox 2.0", Integer.toString(IFS_MANDELBOX1)},
        {"Mandelbox -1.5", Integer.toString(IFS_MANDELBOX2)},};
    public static final int N_SYSTEMS = systemValues.length;

    // Used for undo/redo
    public static final String REGION_PRESENTATION_NAME = "Region Change";
}
