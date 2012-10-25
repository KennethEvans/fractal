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
    public static final int SCHEME_RAINBOW = 0;
    public static final int SCHEME_LINEAR = 1;
    public static final int SCHEME_REVERSE_LINEAR = 2;
    public static final int SCHEME_BW = 3;
    public static final int SCHEME_GRAYSCALE = 4;
    public static final String[][] colorSchemeValues = {
        {"Rainbow", Integer.toString(SCHEME_RAINBOW)},
        {"Linear", Integer.toString(SCHEME_LINEAR)},
        {"Reverse Linear", Integer.toString(SCHEME_REVERSE_LINEAR)},
        {"Black & White", Integer.toString(SCHEME_BW)},
        {"Grayscale", Integer.toString(SCHEME_GRAYSCALE)},};
    public static final int N_COLOR_SCHEMES = colorSchemeValues.length;

    // Used for system selection
    // Change FractalBrowser.setSystem and FractalSystems when changes are
    // made
    public static final int IFS_MANDELBRODT = 0;
    public static final int IFS_DRAGON = 1;
    public static final String[][] systemValues = {
        {"Mandelbrodt", Integer.toString(IFS_MANDELBRODT)},
        {"Dragon", Integer.toString(IFS_DRAGON)},};
    public static final int N_SYSTEMS = systemValues.length;

    // Used for undo/redo
    public static final String REGION_PRESENTATION_NAME = "Region Change";
}
