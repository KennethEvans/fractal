package fractal.color;

import java.awt.Color;

/**
 * Creates color schemes
 * 
 * @author Kenneth Evans, Jr.
 */
public class ColorSchemes
{
    public static ColorScheme makeRainbowScheme(int nColors) {
        ColorScheme scheme = new ColorScheme(nColors) {
            public Color defineColor(int index, int nColors) {
                if(nColors <= 1) {
                    return Color.BLACK;
                }
                Color color = null;
                double nGroups = 5, nMembers = 45, nTotal = nGroups * nMembers;
                double high = 1.000, medium = .375;

                double h = (double)index / (double)nColors;
                double hx = h * nTotal;
                double deltax = (high - medium) / nMembers;
                double r, g, b;
                int gh = (int)Math.floor(hx / nMembers);
                int ih = (int)Math.floor(hx);
                switch(gh) {
                case 0:
                    r = medium;
                    g = medium + (ih - gh * nMembers) * deltax;
                    b = high;
                    break;
                case 1:
                    r = medium;
                    g = high;
                    b = high - (ih - gh * nMembers) * deltax;
                    break;
                case 2:
                    r = medium + (ih - gh * nMembers) * deltax;
                    g = high;
                    b = medium;
                    break;
                case 3:
                    r = high;
                    g = high - (ih - gh * nMembers) * deltax;
                    b = medium;
                    break;
                case 4:
                    r = high;
                    g = medium;
                    b = medium + (ih - gh * nMembers) * deltax;
                    break;
                default:
                    r = high;
                    g = medium;
                    b = high;
                    break;
                }
                int red = (int)(r * 255 + .5);
                if(red > 255) red = 255;
                int green = (int)(g * 255 + .5);
                if(green > 255) green = 255;
                int blue = (int)(b * 255 + .5);
                if(blue > 255) blue = 255;
                color = new Color(red, green, blue);
                return color;
            }

        };
        return scheme;
    }

    public static ColorScheme makeLinearScheme(int nColors) {
        ColorScheme scheme = new ColorScheme(nColors) {
            public Color defineColor(int index, int nColors) {
                if(nColors <= 1) {
                    return Color.BLACK;
                }
                int nRGBColors = 256 * 256 * 256;
                int rgb = index * (nRGBColors - 1) / (nColors - 1);
                if(rgb >= nRGBColors) {
                    rgb = nRGBColors - 1;
                }
                return new Color(rgb);
            }
        };
        return scheme;
    }

    public static ColorScheme makeReverseLinearScheme(int nColors) {
        ColorScheme scheme = new ColorScheme(nColors) {
            public Color defineColor(int index, int nColors) {
                if(nColors <= 1) {
                    return Color.BLACK;
                }
                int nRGBColors = 256 * 256 * 256;
                int rgb = nRGBColors - 1 - index * (nRGBColors - 1)
                    / (nColors - 1);
                if(rgb >= nRGBColors) {
                    rgb = nRGBColors - 1;
                }
                if(rgb < 0) {
                    rgb = 0;
                }
                return new Color(nRGBColors - rgb - 1);
            }
        };
        return scheme;
    }

    public static ColorScheme makeBWScheme(int nColors) {
        ColorScheme scheme = new ColorScheme(nColors) {
            public Color defineColor(int index, int nColors) {
                if(index % 2 == 0) {
                    return Color.BLACK;
                } else {
                    return Color.WHITE;
                }
            }
        };
        return scheme;
    }

    public static ColorScheme makeGrayscaleScheme(int nColors) {
        ColorScheme scheme = new ColorScheme(nColors) {
            public Color defineColor(int index, int nColors) {
                if(nColors <= 1) {
                    return Color.BLACK;
                }
                int gray = index * 255 / (nColors - 1);
                if(gray > 255) gray = 255;
                return new Color(gray, gray, gray);
            }
        };
        return scheme;
    }

}
