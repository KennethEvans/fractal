package fractal.model;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.kenevans.imagemodel.utils.Utils;

import fractal.color.ColorScheme;
import fractal.color.ColorSchemes;

/*
 * Created on Oct 16, 2012
 * By Kenneth Evans, Jr.
 */

/**
 * Class to manage the fractalModel of the fractal.
 * 
 * @author Kenneth Evans, Jr.
 */
public class FractalModel implements Cloneable
{
    private static final int ITERS_DEFAULT = 570;
    private static final double RMAX_DEFAULT = 8;
    private Rectangle2D CRECT_DEFAULT = new Rectangle2D.Double(-2.08, -1.20,
        3.20, 2.40);
    private static final int IMAGE_WIDTH_DEFAULT = 800;
    private static final int IMAGE_HEIGHT_DEFAULT = 600;
    private static float H_DEFAULT = 0.0F;
    private static float S_DEFAULT = 1.0F;
    private static float B_DEFAULT = 1.0F;

    private static final int N_COLORS_DEFAULT = 1024;

    ColorScheme colorScheme = ColorSchemes.makeLinearScheme(N_COLORS_DEFAULT);
    FractalSystem system = FractalSystems.makeMandelbrodt();
    private Rectangle2D cRect = getcRectDefaultClone();
    private int iters = ITERS_DEFAULT;
    private double rMax = RMAX_DEFAULT;
    private int imageWidth = IMAGE_WIDTH_DEFAULT;
    private int imageHeight = IMAGE_HEIGHT_DEFAULT;
    private float hue = H_DEFAULT;
    private float saturation = S_DEFAULT;
    private float brightness = B_DEFAULT;

    // private int nColors = colorScheme.getNColors();

    /**
     * FractalModel constructor that uses the defaults
     */
    public FractalModel() {
        // Do nothing
    }

    /**
     * FractalModel constructor.
     * 
     * @param colorScheme
     * @param cRect
     * @param iters
     * @param rMax
     * @param imageWidth
     * @param imageHeight
     * @param hue
     * @param saturation
     * @param brightness
     */
    public FractalModel(ColorScheme colorScheme, Rectangle2D cRect, int iters,
        double rMax, int imageWidth, int imageHeight, float hue,
        float saturation, float brightness) {
        resetState(colorScheme, cRect, iters, rMax, imageWidth, imageHeight,
            hue, saturation, brightness);
    }

    /**
     * Changes the fields to the given values.
     * 
     * @param colorScheme
     * @param cRect
     * @param iters
     * @param rMax
     * @param imageWidth
     * @param imageHeight
     * @param hue
     * @param saturation
     * @param brightness
     */
    public void resetState(ColorScheme colorScheme, Rectangle2D cRect,
        int iters, double rMax, int imageWidth, int imageHeight, float hue,
        float saturation, float brightness) {
        this.colorScheme = colorScheme;
        this.cRect = cRect;
        this.iters = iters;
        this.rMax = rMax;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    /**
     * Changes the fields to the values in the given fractalModel.
     */
    public void resetState(FractalModel newModel) {
        colorScheme = newModel.getColorScheme();
        cRect = (Rectangle2D)newModel.getcRect().clone();
        iters = newModel.getIters();
        rMax = newModel.getrMax();
        imageWidth = newModel.getImageWidth();
        imageHeight = newModel.getImageHeight();
        hue = newModel.getHue();
        saturation = newModel.getSaturation();
        brightness = newModel.getBrightness();
    }

    /**
     * Gets info about this model.
     * 
     * @return
     */
    public String getInfo() {
        String ls = Utils.LS;
        String info = "Fractal Model" + ls;
        info += String.format("  region=(x=%g y=%g width=%g height=%g)",
            cRect.getMinX(), cRect.getMinY(), cRect.getWidth(),
            cRect.getHeight())
            + ls;
        info += "  colorScheme=" + colorScheme.getName() + ls;
        info += "  iters=" + iters + ls;
        info += "  rMax=" + rMax + ls;
        info += "  imageWidth=" + imageWidth + ls;
        info += "  imageHeight=" + imageHeight + ls;
        info += "  H=" + hue + ls;
        info += "  S=" + saturation + ls;
        info += "  V=" + brightness + ls;
        return info;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new FractalModel(colorScheme, (Rectangle2D)cRect.clone(), iters,
            rMax, imageWidth, imageHeight, hue, saturation, brightness);
    }

    @Override
    public String toString() {
        String info = "";
        info += "{";
        info += String.format("cRect=(x=%g y=%g width=%g height=%g)",
            cRect.getMinX(), cRect.getMinY(), cRect.getWidth(),
            cRect.getHeight());
        info += " iters=" + iters;
        info += " rMax=" + rMax;
        info += "  imageWidth=" + imageWidth;
        info += "  imageHeight=" + imageHeight;
        info += "  H=" + hue;
        info += "  S=" + saturation;
        info += "  V=" + brightness;
        info += "}";
        return info;
    }

    /**
     * Gets a clone of the default cRect.
     * 
     * @return
     */
    public Rectangle2D getcRectDefaultClone() {
        return (Rectangle2D)CRECT_DEFAULT.clone();
    }

    /**
     * Calculates a new BufferedImage using the current settings.
     * 
     * @return
     */
    public BufferedImage getImage() {
        // // DEBUG
        // System.out.println("draw: fm=" + this);
        Rectangle2D cRect = this.getcRect();
        int iters = this.getIters();
        double rMax = this.getrMax();
        int imageWidth = this.getImageWidth();
        int imageHeight = this.getImageHeight();
        BufferedImage image = new BufferedImage(imageWidth, imageHeight,
            BufferedImage.TYPE_INT_RGB);
        int curIter;
        int rgbColor, hsbColor;
        double cx, cy;
        double deltaX = cRect.getWidth() / (imageWidth - 1);
        double deltaY = cRect.getHeight() / (imageHeight - 1);
        double fraction;
        for(int row = 0; row < imageHeight; row++) {
            cy = cRect.getMinY() + deltaY * row;
            for(int col = 0; col < imageWidth; col++) {
                cx = cRect.getMinX() + deltaX * col;
                curIter = system.getIters(cx, cy, rMax, iters);
                fraction = (double)curIter / (iters - 1);
                rgbColor = curIter == 0 ? 0 : ColorScheme
                    .toColorInt(colorScheme.getStoredColor(fraction * .80));
                hsbColor = applyHSB(rgbColor);
                image.setRGB(col, row, hsbColor);
            }
        }
        return image;
    }

    /**
     * Gets the value of c at the given x and y values;
     * 
     * @param x
     * @param y
     * @return
     */
    public Point2D getCPoint(int x, int y) {
        double deltaX = cRect.getWidth() / (imageWidth - 1);
        double deltaY = cRect.getHeight() / (imageHeight - 1);
        double cx = cRect.getMinX() + deltaX * x;
        double cy = cRect.getMinY() + deltaY * y;
        return new Point2D.Double(cx, cy);
    }

    /**
     * Applies the current HSB values to the given color.
     * 
     * @param rgbColor
     * @return The new color
     */
    private int applyHSB(int rgbColor) {
        if(hue == H_DEFAULT && saturation == S_DEFAULT
            && brightness == B_DEFAULT) {
            return rgbColor;
        }
        // Get the RGB components
        Color color = new Color(rgbColor);
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        // Get the HSB components and convert them
        float[] hsbVals = Color.RGBtoHSB(red, green, blue, null);
        // Hue (Note plus)
        hsbVals[0] = hsbVals[0] + hue / 360.0F;
        // Saturation
        hsbVals[1] = hsbVals[1] * saturation;
        if(hsbVals[1] > 1.0) hsbVals[1] = 1.0F;
        // Brightness
        hsbVals[2] = hsbVals[2] * brightness;
        if(hsbVals[2] > 1.0) hsbVals[2] = 1.0F;
        int newColor = Color.HSBtoRGB(hsbVals[0], hsbVals[1], hsbVals[2]);
        return newColor;
    }

    // Getters and Setters

    /**
     * @return The value of colorScheme.
     */
    public ColorScheme getColorScheme() {
        return colorScheme;
    }

    /**
     * @param colorScheme The new value for colorScheme.
     */
    public void setColorScheme(ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    /**
     * @return The value of system.
     */
    public FractalSystem getSystem() {
        return system;
    }

    /**
     * @param system The new value for system.
     */
    public void setSystem(FractalSystem system) {
        this.system = system;
    }

    /**
     * @return The value of cRect.
     */
    public Rectangle2D getcRect() {
        return cRect;
    }

    /**
     * @param cRect The new value for cRect.
     */
    public void setcRect(Rectangle2D cRect) {
        this.cRect = cRect;
    }

    /**
     * @return The value of iters.
     */
    public int getIters() {
        return iters;
    }

    /**
     * @param iters The new value for iters.
     */
    public void setIters(int iters) {
        this.iters = iters;
    }

    /**
     * @return The value of rMax.
     */
    public double getrMax() {
        return rMax;
    }

    /**
     * @param rMax The new value for rMax.
     */
    public void setrMax(double rMax) {
        this.rMax = rMax;
    }

    /**
     * @return The value of imageWidth.
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * @param imageWidth The new value for imageWidth.
     */
    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    /**
     * @return The value of imageHeight.
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * @param imageHeight The new value for imageHeight.
     */
    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    /**
     * @return The value of hue.
     */
    public float getHue() {
        return hue;
    }

    /**
     * @param hue The new value for hue.
     */
    public void setHue(float hue) {
        this.hue = hue;
    }

    /**
     * @return The value of saturation.
     */
    public float getSaturation() {
        return saturation;
    }

    /**
     * @param saturation The new value for saturation.
     */
    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    /**
     * @return The value of brightness.
     */
    public float getBrightness() {
        return brightness;
    }

    /**
     * @param brightness The new value for brightness.
     */
    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

}
