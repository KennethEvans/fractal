package fractal.color;

import java.awt.Color;

/**
 * ColorScheme Provides an abstract class to provide an array of colors.
 * 
 * @author Kenneth Evans, Jr.
 */
public abstract class ColorScheme
{
    protected final static int NCOLORS = 256;
    protected int nColors = NCOLORS;
    protected Color[] colors;
    protected String name = "Color Scheme";

    /**
     * ColorScheme default constructor (256 colors).
     */
    public ColorScheme() {
    }

    /**
     * ColorScheme default constructor (256 colors).
     * 
     * @param name
     * @param nColors
     */
    public ColorScheme(String name, int nColors) {
        this.name = name;
        this.nColors = nColors;
    }

    /**
     * ColorScheme constructor.
     * 
     * @param nColors The number of colors in the scheme.
     */
    public ColorScheme(int nColors) {
        this.nColors = nColors;
    }

    /**
     * Defines the Color array.
     */
    public Color[] defineColors() {
        if(colors != null) return colors;

        colors = new Color[nColors];
        for(int i = 0; i < nColors; i++) {
            colors[i] = defineColor(i, nColors);
        }
        return colors;
    }

    /**
     * Gets a color corresponding to a given number of colors.
     * 
     * @param index Index of the color [0, nColors - 1].
     * @param nColors The total number of colors.
     * @return The Color corresponding to the index.
     */
    abstract public Color defineColor(int index, int nColors);

    /**
     * Calculates the integer value of the Color.
     * 
     * @param color
     * @return 256*256*red + 256*green + blue.
     */
    public static int toColorInt(Color color) {
        int colorInt = 65536 * color.getRed() + 256 * color.getGreen()
            + color.getBlue();
        return colorInt;
    }

    /**
     * Calculates the string value of the Color.
     * 
     * @param color
     * @return "rrr,ggg,bbb".
     */
    public static String toColorString(Color color) {
        String string = color.getRed() + "," + color.getGreen() + ","
            + color.getBlue();
        return string;
    }

    /**
     * Calculates the color corresponding to a fraction of the default number of
     * colors (256).
     * 
     * @param fract A fraction in the range [0,1] inclusive.
     * @return The Color with index closest to the fraction times the maximun
     *         color index (255).
     */
    public Color getColor(double fract) {
        return getColor(fract, NCOLORS);
    }

    /**
     * Returns the color corresponding to a fraction of the number of colors
     * from the stored color array. Calculates the array if it has not
     * previously been calculated.
     * 
     * @param fract A fraction in the range [0,1] inclusive.
     * @param nColors The total number of colors.
     * @return The Color with index closest to the fraction times the maximun
     *         color index (nColors - 1).
     */
    public Color getStoredColor(double fract) {
        if(colors == null) defineColors();
        if(colors == null) return null;
        int index = (int)Math.round((nColors - 1) * fract);
        if(index < 0) index = 0;
        if(index > nColors) index = nColors;
        return colors[index];
    }

    /**
     * Calculates the color corresponding to a fraction of the number of colors.
     * 
     * @param fract A fraction in the range [0,1] inclusive.
     * @param nColors The total number of colors.
     * @return The Color with index closest to the fraction times the maximun
     *         color index (nColors - 1).
     */
    public Color getColor(double fract, int nColors) {
        int index = (int)Math.round((nColors - 1) * fract);
        if(index < 0) index = 0;
        if(index > nColors) index = nColors;
        return defineColor(index, nColors);
    }

    /**
     * @return The colors array.
     */
    public Color[] getColors() {
        return colors;
    }

    /**
     * @return Returns the default number of colors in the color array (256).
     */
    public static int getNColorsDefault() {
        return NCOLORS;
    }

    /**
     * @return Returns the number of colors in the color array.
     */
    public int getNColors() {
        return nColors;
    }

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
