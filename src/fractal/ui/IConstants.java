package fractal.ui;

/**
 * Provides constants for classes related to fractals.
 * 
 * @author Kenneth Evans, Jr.
 */
public interface IConstants
{
    public static final String LS = System.getProperty("line.separator");

    // Used for scheme selection
    // Change FractalBrowser.setColorScheme and ColorSchemes when changes are
    // made
    public static final int SCHEME_RAINBOW1 = 0;
    public static final int SCHEME_RAINBOW2 = 1;
    public static final int SCHEME_LINEAR = 2;
    public static final int SCHEME_REVERSE_LINEAR = 3;
    public static final int SCHEME_REDS = 4;
    public static final int SCHEME_GREENS = 5;
    public static final int SCHEME_BLUES = 6;
    public static final int SCHEME_SPRING = 7;
    public static final int SCHEME_SUMMER = 8;
    public static final int SCHEME_AUTUMN = 9;
    public static final int SCHEME_WINTER = 10;
    public static final int SCHEME_PASTEL = 11;
    public static final int SCHEME_BW = 12;
    public static final int SCHEME_GRAYSCALE = 13;
    public static final int SCHEME_REPEAT8 = 14;
    public static final String[][] colorSchemeValues = {
        {"Rainbow 1", Integer.toString(SCHEME_RAINBOW1)},
        {"Rainbow 2", Integer.toString(SCHEME_RAINBOW2)},
        {"Linear", Integer.toString(SCHEME_LINEAR)},
        {"Reverse Linear", Integer.toString(SCHEME_REVERSE_LINEAR)},
        {"Beds", Integer.toString(SCHEME_REDS)},
        {"Greens", Integer.toString(SCHEME_GREENS)},
        {"Blues", Integer.toString(SCHEME_BLUES)},
        {"Spring", Integer.toString(SCHEME_SPRING)},
        {"Summer", Integer.toString(SCHEME_SUMMER)},
        {"Autumn", Integer.toString(SCHEME_AUTUMN)},
        {"Winter", Integer.toString(SCHEME_WINTER)},
        {"Pastel", Integer.toString(SCHEME_PASTEL)},
        {"Black & White", Integer.toString(SCHEME_BW)},
        {"Grayscale", Integer.toString(SCHEME_GRAYSCALE)},
        {"Repeat 8", Integer.toString(SCHEME_REPEAT8)},};
    public static final int N_COLOR_SCHEMES = colorSchemeValues.length;

    // Used for system selection
    // Change FractalBrowser.setSystem and FractalSystems when changes are
    // made
    public static final int IFS_MANDELBROT = 0;
    public static final int IFS_DRAGON = 1;
    public static final int IFS_COS = 2;
    public static final int IFS_SIN = 3;
    public static final int IFS_COSH = 4;
    public static final int IFS_SINH = 5;
    public static final String[][] systemValues = {
        {"Mandelbrot", Integer.toString(IFS_MANDELBROT)},
        {"Dragon", Integer.toString(IFS_DRAGON)},
        {"Cosine", Integer.toString(IFS_COS)},
        {"Sine", Integer.toString(IFS_SIN)},
        {"Hyperbolic Cosine", Integer.toString(IFS_COSH)},
        {"Hyperbolic Sine", Integer.toString(IFS_SINH)},};
    public static final int N_SYSTEMS = systemValues.length;

    // Used for undo/redo
    public static final String REGION_PRESENTATION_NAME = "Region Change";
}
