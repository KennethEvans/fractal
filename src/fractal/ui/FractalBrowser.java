package fractal.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import net.kenevans.imagemodel.ImageModel;
import net.kenevans.imagemodel.PrintPreviewDialog;
import net.kenevans.imagemodel.ScrolledImagePanel;
import net.kenevans.imagemodel.utils.AboutBoxPanel;
import net.kenevans.imagemodel.utils.ImageUtils;
import net.kenevans.imagemodel.utils.Utils;
import fractal.color.ColorScheme;
import fractal.color.ColorSchemes;
import fractal.model.FractalModel;
import fractal.model.UndoableComponentEdit;
import fractal.model.UndoableRegionEdit;

public class FractalBrowser extends JFrame implements IConstants
{
    private static final long serialVersionUID = 1L;
    private static final boolean INITIALIZE_PATH = false; // For developing
    private static final boolean USE_STATUS_BAR = true;
    private static final String VERSION_STRING = "Fractal Browser 1.0.0";
    private static final String INITIAL_PATH = "c:/users/evans/Pictures";
    private static final String[] HOME_LOCATIONS = {"HOME", "HOME_PATH"};
    private static final String[] PICTURE_LOCATIONS = {"My Pictures",
        "Pictures"};
    private static String[] suffixes = {"jpg", "jpe", "jpeg", "gif", "tif",
        "tiff", "png", "bmp"};

    public static enum ControlPanelMode {
        REGION, COLORS, SIZE, ZOOM
    };

    private static final int N_COLORS = 1024;
    // private static final int ITERS_DEFAULT = 570;
    private static final double ZOOM_FACTOR = 1.5;

    private ControlPanelMode controlPanelMode = ControlPanelMode.REGION;
    private static final String TITLE = "Fractal Browser";
    private static final int WIDTH = 800 + 5;
    private static final int HEIGHT = 600 + 25;
    private PrintRequestAttributeSet printAttributes = new HashPrintRequestAttributeSet();
    private PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
    private PrintService printService = null;
    private boolean fitAlways = false;
    private boolean fitIfLarger = false;

    private UndoManager undoManager = new UndoManager();
    private FractalModel fm = new FractalModel();
    private static final int MAX_UNDO = 25;
    private boolean editInProgress = false;
    private boolean drawEnabled = true;
    private Rectangle2D regionLastValue = fm.getcRectDefaultClone();
    private String itersLastValue = Integer.toString(fm.getIters());
    private String rMaxLastValue = Double.toString(fm.getrMax());
    private String imageWidthLastValue = Integer.toString(fm.getImageWidth());
    private String imageHeightLastValue = Integer.toString(fm.getImageHeight());
    private String hueLastValue = Double.toString(fm.getHue());
    private String saturationLastValue = Double.toString(fm.getSaturation());
    private String brightnessLastValue = Double.toString(fm.getBrightness());

    private ColorScheme[] colorSchemes = new ColorScheme[N_COLOR_SCHEMES];
    private ColorScheme colorScheme;
    private int colorSchemeIndex = 1;

    private Container contentPane = this.getContentPane();
    private JToolBar toolBar = new JToolBar("FractalBrowser Tool Bar");
    private JPanel mainPanel = new JPanel();
    private JPanel controlPanel = new JPanel();
    private JPanel displayPanel = new JPanel();
    private ScrolledImagePanel imagePanel = null;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuFile = new JMenu();
    private JMenuItem menuFileOpen = new JMenuItem();
    private JMenuItem menuDirectoryOpen = new JMenuItem();
    private JMenuItem menuFileSaveAs = new JMenuItem();
    private JMenuItem menuFilePrint = new JMenuItem();
    private JMenuItem menuFilePrintPreview = new JMenuItem();
    private JMenuItem menuFilePageSetup = new JMenuItem();
    private JMenuItem menuFileExit = new JMenuItem();
    private JMenu menuEdit = new JMenu();
    private JMenuItem menuEditCopy = new JMenuItem();
    private JMenuItem menuEditPaste = new JMenuItem();
    private JMenuItem menuEditPastePrint = new JMenuItem();
    private JMenu menuInfo = new JMenu();
    private JMenuItem menuInfoSuffixes = new JMenuItem();
    private JCheckBoxMenuItem menuImageFit = new JCheckBoxMenuItem();
    private JMenu menuImage = new JMenu();
    private JMenuItem menuInfoImageInfo = new JMenuItem();
    private JMenuItem menuImageGamma = new JMenu();
    private JMenuItem menuGammaLighten = new JMenuItem();
    private JMenuItem menuGammaDarken = new JMenuItem();
    private JMenuItem menuGammaSpecify = new JMenuItem();
    private JMenuItem menuImageBlur = new JMenuItem();
    private JMenuItem menuImageSharpen = new JMenuItem();
    private JMenuItem menuImageGrayscale = new JMenuItem();
    private JMenu menuFlip = new JMenu();
    private JMenuItem menuFlipHorizontal = new JMenuItem();
    private JMenuItem menuFlipVertical = new JMenuItem();
    private JMenuItem menuFlipBoth = new JMenuItem();
    private JMenu menuRotate = new JMenu();
    private JMenuItem menuRotatePlus90 = new JMenuItem();
    private JMenuItem menuRotateMinus90 = new JMenuItem();
    private JMenuItem menuRotate180 = new JMenuItem();
    private JMenuItem menuRotateAny = new JMenuItem();
    // private JMenu menuCrop = new JMenu();
    private JMenuItem menuCropCrop = new JMenuItem();
    private JMenuItem menuImageRestore = new JMenuItem();
    private JMenu menuHelp = new JMenu();
    private JMenuItem menuHelpOverview = new JMenuItem();
    private JMenuItem menuHelpAbout = new JMenuItem();

    private JTextField itersText;
    private JTextField rMaxText;
    private JTextField imageWidthText;
    private JTextField imageHeightText;
    private JTextField hText;
    private JTextField sText;
    private JTextField bText;
    private JComboBox colorSchemeCombo;
    private JMenuItem menuEditUndo = new JMenuItem("Undo");
    private JMenuItem menuEditRedo = new JMenuItem("Redo");
    private JButton drawEnabledButton;

    private double initialRotateAngle = 0.0;
    private double initialGamma = ImageModel.GAMMA_LIGHTEN;
    private String currentDir = null;
    private ImageModel imageModel = new ImageModel();

    public FractalBrowser() {
        this(true);
    }

    public FractalBrowser(boolean initializeUI) {
        // Get the available names
        if(false) {
            String[] readerFormatNames = ImageIO.getReaderFormatNames();
            // There are 6 names without the JAI tools. Use our names (some will
            // fail)
            // unless there is an indication more are supported. This presumably
            // means
            // the JAI tools are installed.
            if(readerFormatNames != null && readerFormatNames.length > 6) {
                suffixes = readerFormatNames;
            }
        } else {
            // There are 4 suffices without the JAI tools. Use our names (some
            // will
            // fail)
            // unless there is an indication more are supported. This presumably
            // means
            // the JAI tools are installed.
            Set<String> readerSuffixes = ImageUtils.getReaderSuffixes();
            int size = readerSuffixes.size();
            if(size > 4) {
                suffixes = new String[size];
                Iterator<String> iter1 = readerSuffixes.iterator();
                int i = 0;
                while(iter1.hasNext()) {
                    String suffix = (String)iter1.next();
                    suffixes[i] = suffix;
                    i++;
                }
            }
        }

        // Return here if we are not to initialize the UI
        if(!initializeUI) return;

        // Initialize the UI
        uiInit();

        // Menus
        menuInit();

        // Make the image panel be focusable so ^C and ^V will operate on the
        // panel
        // after the mouse is clicked there. Also requires requestFocusInWindow
        // in
        // the mousePressed handler.
        setFocusable(true);

        // Set the initial directory (useful when debugging)
        if(INITIALIZE_PATH && INITIAL_PATH != null) {
            File file = new File(INITIAL_PATH);
            currentDir = INITIAL_PATH;
        } else {
            // TODO fix this for other platforms than Windows
            // Try to find the home directory
            String userDir = null;
            for(String dir : HOME_LOCATIONS) {
                userDir = System.getenv(dir);
                if(userDir != null) {
                    break;
                }
            }
            // Try to find the Pictures directory
            if(userDir != null) {
                File file;
                for(String dir : PICTURE_LOCATIONS) {
                    file = new File(userDir + File.separator + dir);
                    if(file.exists()) {
                        currentDir = file.getPath();
                        break;
                    }
                }
            }
        }

        // Cause it to initialize the color scheme
        setColorScheme(colorSchemeIndex);

        // Set the maximum number of edits
        undoManager.setLimit(MAX_UNDO);

        // Reset the undo/redo buttons
        resetUndoRedoButtons();

        // Draw the initial image
        draw();
    }

    private void uiInit() {
        this.setTitle(TITLE);

        // Display panel
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        imagePanel = new ScrolledImagePanel(imageModel, USE_STATUS_BAR);
        // Always enable crop
        imagePanel.setMode(ScrolledImagePanel.Mode.CROP);
        displayPanel.add(imagePanel);

        // Create the tool bar
        toolbarInit();

        // Main panel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(displayPanel, BorderLayout.CENTER);

        // Content pane
        // For the drag behavior to work correctly, the tool bar must be in a
        // container that uses the BorderLayout layout manager. The fm
        // that
        // the tool bar affects is generally in the center of the container. The
        // tool bar must be the only other fm in the container, and it
        // must
        // not be in the center.
        contentPane.setLayout(new BorderLayout());
        contentPane.add(toolBar, BorderLayout.NORTH);
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }

    private void toolbarInit() {
        // Settings
        toolBar.setFloatable(false);
        toolBar.setRollover(true);

        // Save As button
        JButton button = makeToolBarButton("/resources/saveas_edit.gif",
            "Save As", "Save As");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                saveAs();
            }
        });

        // Separator
        toolBar.addSeparator();

        // Copy button
        button = makeToolBarButton("/resources/copy_edit.gif", "Copy", "Copy");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                copy();
            }
        });

        // Paste button
        button = makeToolBarButton("/resources/paste_edit.gif", "Paste",
            "Paste");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                paste();
            }
        });

        // Print button
        button = makeToolBarButton("/resources/print_edit.gif", "Print",
            "Print");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                print();
            }
        });

        // Separator
        toolBar.addSeparator();

        // Scale button
        button = makeToolBarButton("/resources/scale.gif",
            "Toggle fit image to current area", "Fit Image");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                fitIfLarger = !fitIfLarger;
                fitImage();
            }
        });

        // Separator
        toolBar.addSeparator();

        // Zoom button
        button = makeToolBarButton("/resources/zoom.gif", "Zoom controls",
            "Zoom Controls");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                resetControlPanel(ControlPanelMode.ZOOM);
            }
        });

        // Colors button
        button = makeToolBarButton("/resources/colors.png", "Color controls",
            "Color Controls");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                resetControlPanel(ControlPanelMode.COLORS);
            }
        });

        // Page button
        button = makeToolBarButton("/resources/page.png", "Size controls",
            "Size Controls");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                resetControlPanel(ControlPanelMode.SIZE);
            }
        });

        // Region button
        button = makeToolBarButton("/resources/fractal.png", "Region controls",
            "Region Controls");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                resetControlPanel(ControlPanelMode.REGION);
            }
        });

        // Separator
        toolBar.addSeparator();

        // Control panel
        // controlPanel.setBackground(Color.RED);
        controlPanel.setLayout(new BorderLayout(5, 5));
        toolBar.add(controlPanel);

        // Make it default to REGION
        setControlPanelMode(ControlPanelMode.REGION);
    }

    /**
     * Sets the control panel according to mode.
     * 
     * @param mode The mode to set.
     */
    protected void resetControlPanel(ControlPanelMode mode) {
        // Clean out the old contents
        controlPanel.removeAll();

        // Make new contents
        if(mode == ControlPanelMode.REGION) {
            createRegionControlPanel();
        } else if(mode == ControlPanelMode.COLORS) {
            createColorsControlPanel();
        } else if(mode == ControlPanelMode.SIZE) {
            createSizeControlPanel();
        } else if(mode == ControlPanelMode.ZOOM) {
            createZoomControlPanel();
        }

        // Set the mode
        this.controlPanelMode = mode;
    }

    /**
     * Makes an the region control panel.
     */
    protected void createRegionControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        controlPanel.add(panel, BorderLayout.LINE_START);

        // Reset button
        JButton button = new JButton("Reset");
        panel.add(button);
        button.setToolTipText("Reset the region of interest");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                resetRegion();
            }
        });

        // Zoom out button
        button = new JButton("Expand");
        panel.add(button);
        button.setToolTipText("Expand the region of interest");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                resizeRegion(true);
            }
        });

        // Zoom in button
        button = new JButton("Reduce");
        panel.add(button);
        button.setToolTipText("Reduce the region of interest");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                resizeRegion(false);
            }
        });

        JLabel label = new JLabel("Iters: ");
        label.setToolTipText("The maximum number of iterations");
        panel.add(label);

        itersText = new JTextField(8);
        panel.add(itersText);
        itersText.setName("Iters");
        itersText.setText(Integer.toString(fm.getIters()));
        itersText.setToolTipText(label.getToolTipText());
        itersText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(itersText != null) {
                    String text = itersText.getText();
                    if(itersText.equals(itersLastValue)) {
                        return;
                    }
                    try {
                        fm.setIters(Integer.parseInt(text));
                        saveComponentState(itersText, itersLastValue, text);
                        draw();
                    } catch(NumberFormatException ex) {
                        Utils.excMsg("Error getting Iters", ex);
                    }
                    itersText.setText(Integer.toString(fm.getIters()));
                    itersLastValue = itersText.getText();
                }
            }
        });

        label = new JLabel("R Max: ");
        label.setToolTipText("The escape radius");
        panel.add(label);

        rMaxText = new JTextField(8);
        panel.add(rMaxText);
        rMaxText.setName("R Max");
        rMaxText.setText(String.format("%.3f", fm.getrMax()));
        rMaxText.setToolTipText(label.getToolTipText());
        rMaxText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(rMaxText != null) {
                    String text = rMaxText.getText();
                    if(rMaxText.equals(rMaxLastValue)) {
                        return;
                    }
                    try {
                        fm.setrMax(Integer.parseInt(text));
                        saveComponentState(rMaxText, rMaxLastValue, text);
                        draw();
                    } catch(NumberFormatException ex) {
                        Utils.excMsg("Error getting rMax", ex);
                    }
                    rMaxText.setText(Double.toString(fm.getrMax()));
                    rMaxLastValue = rMaxText.getText();
                }
            }
        });

        // Resize button
        button = new JButton("Resize");
        button.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK),
            "resetRegion");
        button.getActionMap().put("resetRegion", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                resize();
            }
        });
        panel.add(button);
        button
            .setToolTipText("Resize the region after dragging to set a selection");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                resize();
            }
        });

        controlPanel.repaint();
        contentPane.validate();
    }

    /**
     * Makes an the size control panel.
     */
    protected void createSizeControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        controlPanel.add(panel, BorderLayout.LINE_START);

        JLabel label = new JLabel("Image Width: ");
        label.setToolTipText("The width in pixels of the generated image");
        panel.add(label);

        imageWidthText = new JTextField(8);
        panel.add(imageWidthText);
        imageWidthText.setName("Image Width");
        imageWidthText.setText(Integer.toString(fm.getImageWidth()));
        imageWidthText.setToolTipText(label.getToolTipText());
        imageWidthText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(imageWidthText != null) {
                    String text = imageWidthText.getText();
                    if(imageWidthText.equals(imageWidthLastValue)) {
                        return;
                    }
                    try {
                        int newWidth = Integer.parseInt(text);
                        if(newWidth <= 0) {
                            Utils.errMsg("Invalid width");
                            return;
                        }
                        fm.setImageWidth(newWidth);
                        // Prompt to disable recalculation
                        int res = JOptionPane.showConfirmDialog(
                            FractalBrowser.this, "Defer recalculation?",
                            "Query", JOptionPane.YES_NO_OPTION);
                        if(res != JOptionPane.YES_OPTION) {
                            draw();
                        }
                    } catch(NumberFormatException ex) {
                        Utils.excMsg("Error getting image width", ex);
                    }
                    imageWidthText.setText(Integer.toString(fm.getImageWidth()));
                    imageWidthLastValue = imageWidthText.getText();
                }
            }
        });

        label = new JLabel("Image Height: ");
        label.setToolTipText("The width in pixels of the generated image");
        panel.add(label);

        imageHeightText = new JTextField(8);
        panel.add(imageHeightText);
        imageHeightText.setName("Image Height");
        imageHeightText.setText(Integer.toString(fm.getImageHeight()));
        imageHeightText.setToolTipText(label.getToolTipText());
        imageHeightText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(imageHeightText != null) {
                    String text = imageHeightText.getText();
                    if(imageHeightText.equals(imageHeightLastValue)) {
                        return;
                    }
                    try {
                        int newHeight = Integer.parseInt(text);
                        if(newHeight <= 0) {
                            Utils.errMsg("Invalid height");
                            return;
                        }
                        fm.setImageHeight(newHeight);
                        saveComponentState(imageHeightText,
                            imageHeightLastValue, text);
                        // Prompt to disable recalculation
                        int res = JOptionPane.showConfirmDialog(
                            FractalBrowser.this, "Defer recalculation?",
                            "Query", JOptionPane.YES_NO_OPTION);
                        if(res != JOptionPane.YES_OPTION) {
                            draw();
                        }
                    } catch(NumberFormatException ex) {
                        Utils.excMsg("Error getting image width", ex);
                    }
                    imageHeightText.setText(Integer.toString(fm
                        .getImageHeight()));
                    imageHeightLastValue = imageWidthText.getText();
                }
            }
        });

        drawEnabledButton = new JButton();
        drawEnabledButton.setText(drawEnabled ? "Disable Recalc"
            : "Enable Recalc");
        ;
        drawEnabledButton
            .setToolTipText("Set whethere drawing will happen after new "
                + "values are entered");
        panel.add(drawEnabledButton);
        drawEnabledButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                drawEnabled = !drawEnabled;
                drawEnabledButton.setText(drawEnabled ? "Disable Recalc"
                    : "Enable Recalc");
                if(drawEnabled) {
                    draw();
                }
            }
        });

        controlPanel.repaint();
        contentPane.validate();
    }

    /**
     * Creates the zoom control panel.
     */
    protected void createZoomControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        controlPanel.add(panel, BorderLayout.LINE_START);

        JButton button = new JButton();
        button.setText("Zoom In");
        button.setToolTipText("Zoom in on the image");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(imagePanel != null) {
                    imagePanel.zoomIn();
                }
            }
        });

        button = new JButton();
        button.setText("Zoom Out");
        button.setToolTipText("Zoom out on the image");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(imagePanel != null) {
                    imagePanel.zoomOut();
                }
            }
        });

        button = new JButton();
        button.setText("Zoom Reset");
        button.setToolTipText("Reset zoom on the image");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(imagePanel != null) {
                    imagePanel.zoomReset();
                }
            }
        });

        button = new JButton();
        button.setText("Zoom Fit");
        button.setToolTipText("Zoom the image to fit the window");
        panel.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(imagePanel != null) {
                    imagePanel.zoomFit();
                }
            }
        });

        controlPanel.repaint();
        contentPane.validate();
    }

    /**
     * Creates the colors control panel.
     */
    protected void createColorsControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        controlPanel.add(panel, BorderLayout.LINE_START);

        final JPopupMenu menu = new JPopupMenu();
        menu.add(menuImageGamma);

        String[] comboItems = new String[colorSchemeValues.length];
        for(int i = 0; i < comboItems.length; i++) {
            // // This doesn't work since schemes are not created until needed
            // comboItems[i] = colorSchemes[i].getName();
            comboItems[i] = colorSchemeValues[i][0];
        }
        colorSchemeCombo = new JComboBox(comboItems);
        colorSchemeCombo.setToolTipText("Color schemes");
        colorSchemeCombo.setSelectedIndex(colorSchemeIndex);
        panel.add(colorSchemeCombo);
        colorSchemeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                setColorScheme(colorSchemeCombo.getSelectedIndex());
                draw();
            }
        });

        JLabel label = new JLabel("H: ");
        label.setToolTipText("Hue, the angle in degrees on the color wheel");
        panel.add(label);

        hText = new JTextField(8);
        panel.add(hText);
        hText.setName("Hue");
        hText.setText(String.format("%.3f", fm.getHue()));
        hText.setToolTipText(label.getToolTipText());
        hText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(hText != null) {
                    String text = hText.getText();
                    if(hText.equals(hueLastValue)) {
                        return;
                    }
                    try {
                        fm.setHue(Float.parseFloat(text));
                        saveComponentState(hText, hueLastValue, text);
                        draw();
                    } catch(NumberFormatException ex) {
                        Utils.excMsg("Error getting hue", ex);
                    }
                    hText.setText(Float.toString(fm.getHue()));
                    hueLastValue = hText.getText();
                }
            }
        });

        label = new JLabel("S: ");
        label.setToolTipText("Saturation, 1 is no change, 0 is B&W");
        panel.add(label);

        sText = new JTextField(8);
        panel.add(sText);
        sText.setName("Saturation");
        sText.setText(String.format("%.3f", fm.getSaturation()));
        sText.setToolTipText(label.getToolTipText());
        sText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(sText != null) {
                    String text = sText.getText();
                    if(sText.equals(saturationLastValue)) {
                        return;
                    }
                    try {
                        fm.setSaturation(Float.parseFloat(text));
                        saveComponentState(sText, saturationLastValue, text);
                        draw();
                    } catch(NumberFormatException ex) {
                        Utils.excMsg("Error getting saturation", ex);
                    }
                    sText.setText(Float.toString(fm.getSaturation()));
                    saturationLastValue = sText.getText();
                }
            }
        });

        label = new JLabel("B: ");
        label.setToolTipText("Brightness, 1 is no change, 0 is black");
        panel.add(label);

        bText = new JTextField(8);
        panel.add(bText);
        bText.setName("Brightness");
        bText.setText(String.format("%.3f", fm.getBrightness()));
        bText.setToolTipText(label.getToolTipText());
        bText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(bText != null) {
                    String text = bText.getText();
                    if(bText.equals(brightnessLastValue)) {
                        return;
                    }
                    try {
                        fm.setBrightness(Float.parseFloat(text));
                        saveComponentState(bText, brightnessLastValue, text);
                        draw();
                    } catch(NumberFormatException ex) {
                        Utils.excMsg("Error getting brightness", ex);
                    }
                    bText.setText(Float.toString(fm.getBrightness()));
                    brightnessLastValue = bText.getText();
                }
            }
        });

        controlPanel.repaint();
        contentPane.validate();
    }

    /**
     * Makes a button for the tool bar.
     * 
     * @param imageName Path to the image.
     * @param toolTipText Text for the tool tip.
     * @param altText Button text when image not found.
     * @return The button.
     */
    protected JButton makeToolBarButton(String imageName, String toolTipText,
        String altText) {
        // Look for the image.
        URL imageURL = FractalBrowser.class.getResource(imageName);

        // Create and initialize the button.
        JButton button = new JButton();
        button.setToolTipText(toolTipText);

        if(imageURL != null) {
            button.setIcon(new ImageIcon(imageURL, altText));
        } else {
            button.setText(altText);
            Utils.errMsg("Resource not found: " + imageName);
        }

        toolBar.add(button);
        return button;
    }

    private void menuInit() {
        // Menu
        this.setJMenuBar(menuBar);

        // File
        menuFile.setText("File");
        menuBar.add(menuFile);

        // Directory Open
        menuDirectoryOpen.setText("Open Directory...");
        menuDirectoryOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openDirectory();
            }
        });
        menuFile.add(menuDirectoryOpen);

        // File Open
        menuFileOpen.setText("Open File...");
        menuFileOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                openFile();
            }
        });
        menuFile.add(menuFileOpen);

        // File Save as
        menuFileSaveAs.setText("Save As...");
        menuFileSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
            InputEvent.CTRL_MASK));
        menuFileSaveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                saveAs();
            }
        });
        menuFile.add(menuFileSaveAs);

        // File Print
        menuFilePrint.setText("Print...");
        menuFilePrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
            InputEvent.CTRL_MASK));
        menuFilePrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                print();
            }
        });
        menuFile.add(menuFilePrint);

        // File Print Preview
        menuFilePrintPreview.setText("Print Preview...");
        menuFilePrintPreview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                printPreview();
            }
        });
        menuFile.add(menuFilePrintPreview);

        // File Page Setup
        menuFilePageSetup.setText("Page Setup...");
        menuFilePageSetup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                pageSetup();
            }
        });
        menuFile.add(menuFilePageSetup);

        // File Exit
        menuFileExit.setText("Exit");
        menuFileExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                quit();
            }
        });
        menuFile.add(menuFileExit);

        // Edit
        menuEdit.setText("Edit");
        menuBar.add(menuEdit);

        menuEdit.add(menuEditUndo);
        menuEditUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
            InputEvent.CTRL_MASK));
        menuEditUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // DEBUG
                // System.out.println("*undoButton (Before): fm=" + fm);
                // System.out.println("   undoManager (Before)=" + undoManager);
                // System.out.println("   presentationName (Before)="
                // + undoManager.getPresentationName());
                editInProgress = true;
                String presentationName = undoManager.getPresentationName();
                try {
                    undoManager.undo();
                    if(presentationName.equals(REGION_PRESENTATION_NAME)) {
                        draw();
                    }
                    resetUndoRedoButtons();
                } catch(CannotUndoException ex) {
                    Utils.errMsg("Cannot undo");
                } finally {
                    editInProgress = false;
                }
                // // DEBUG
                // System.out.println("*undoButton (After): fm=" + fm);
                // System.out.println("   undoManager (After)=" + undoManager);
                // System.out.println("   presentationName (After)="
                // + undoManager.getPresentationName());
            }
        });

        menuEdit.add(menuEditRedo);
        menuEditRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
            InputEvent.CTRL_MASK));
        menuEditRedo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                // // DEBUG
                // System.out.println("*redoButton (Before): fm=" + fm);
                // System.out.println("   undoManager (Before)=" + undoManager);
                // System.out.println("   presentationName (Before)="
                // + undoManager.getPresentationName());
                String presentationName = undoManager.getPresentationName();
                editInProgress = true;
                try {
                    undoManager.redo();
                    if(presentationName.equals(REGION_PRESENTATION_NAME)) {
                        draw();
                    }
                    resetUndoRedoButtons();
                } catch(CannotRedoException ex) {
                    Utils.errMsg("Cannot redo");
                } finally {
                    editInProgress = false;
                }
                // // DEBUG
                // System.out.println("*redoButton (After): fm=" + fm);
                // System.out.println("   undoManager (After)=" + undoManager);
                // System.out.println("   presentationName (After)="
                // + undoManager.getPresentationName());
            }
        });

        // Edit Copy
        menuEditCopy.setText("Copy");
        menuEditCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
            InputEvent.CTRL_MASK));
        menuEditCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                copy();
            }
        });
        menuEdit.add(menuEditCopy);

        // Edit Paste
        menuEditPaste.setText("Paste");
        menuEditPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
            InputEvent.CTRL_MASK));
        menuEditPaste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                paste();
            }
        });
        menuEdit.add(menuEditPaste);

        // Edit Paste and Print
        menuEditPastePrint.setText("Paste and Print");
        menuEditPastePrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
            InputEvent.CTRL_MASK));
        menuEditPastePrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                paste();
                print();
            }
        });
        menuEdit.add(menuEditPastePrint);

        // Info
        menuInfo.setText("Info");
        menuBar.add(menuInfo);

        // Image Info
        menuInfoImageInfo.setText("Image Info");
        menuInfoImageInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                imageInfo();
            }
        });
        menuInfo.add(menuInfoImageInfo);

        // InfoView Suffixes
        menuInfoSuffixes.setText("Avaliable suffixes...");
        menuInfoSuffixes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                showSuffixes();
            }
        });
        menuInfo.add(menuInfoSuffixes);

        // Image
        menuImage.setText("Image");
        menuBar.add(menuImage);

        // Image Gamma
        menuImageGamma.setText("Gamma");
        menuImage.add(menuImageGamma);

        // Gamma Lighten
        menuGammaLighten.setText("Lighten");
        menuGammaLighten.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.gamma(ImageModel.GAMMA_LIGHTEN);
            }
        });
        menuImageGamma.add(menuGammaLighten);

        // Gamma Darken
        menuGammaDarken.setText("Darken");
        menuGammaDarken.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.gamma(ImageModel.GAMMA_DARKEN);
            }
        });
        menuImageGamma.add(menuGammaDarken);

        // Gamma Specify
        menuGammaSpecify.setText("Specify Gamma...");
        menuGammaSpecify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                String result = JOptionPane.showInputDialog("Enter gamma",
                    Double.toString(initialGamma));
                if(result != null) {
                    double gamma = Double.valueOf(result).doubleValue();
                    initialGamma = gamma;
                    imageModel.gamma(gamma);
                }
            }
        });
        menuImageGamma.add(menuGammaSpecify);

        // Image Blur
        menuImageBlur.setText("Blur");
        menuImageBlur.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.blur();
            }
        });
        menuImage.add(menuImageBlur);

        // Image Sharpen
        menuImageSharpen.setText("Sharpen");
        menuImageSharpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.sharpen();
            }
        });
        menuImage.add(menuImageSharpen);

        // Image Grayscale
        menuImageGrayscale.setText("Grayscale");
        menuImageGrayscale.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.grayscale();
            }
        });
        menuImage.add(menuImageGrayscale);

        // Flip
        menuFlip.setText("Flip");
        menuImage.add(menuFlip);

        // Flip Horizontal
        menuFlipHorizontal.setText("Horizontal");
        menuFlipHorizontal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.flip(true, false);
                imagePanel.revalidate();
            }
        });
        menuFlip.add(menuFlipHorizontal);

        // Flip Vertical
        menuFlipVertical.setText("Vertical");
        menuFlipVertical.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.flip(false, true);
                imagePanel.revalidate();
            }
        });
        menuFlip.add(menuFlipVertical);

        // Flip Both
        menuFlipBoth.setText("Both");
        menuFlipBoth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.flip(true, true);
                imagePanel.revalidate();
            }
        });
        menuFlip.add(menuFlipBoth);

        // Rotate
        menuRotate.setText("Rotate");
        menuImage.add(menuRotate);

        // Rotate 90
        menuRotatePlus90.setText("90 degrees CW");
        menuRotatePlus90.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.rotate(90);
                imagePanel.revalidate();
            }
        });
        menuRotate.add(menuRotatePlus90);

        // Rotate -90
        menuRotateMinus90.setText("90 degrees CCW");
        menuRotateMinus90.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.rotate(-90);
                imagePanel.revalidate();
            }
        });
        menuRotate.add(menuRotateMinus90);

        // Rotate 180
        menuRotate180.setText("180 degrees");
        menuRotate180.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                imageModel.rotate(180);
                // Do flip instead
                // imageModel.flip(true, true);
                imagePanel.revalidate();
            }
        });
        menuRotate.add(menuRotate180);

        // Rotate specify
        menuRotateAny.setText("Specify Angle...");
        menuRotateAny.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel == null) return;
                String result = JOptionPane.showInputDialog(
                    "Enter CW angle in degrees",
                    Double.toString(initialRotateAngle));
                if(result != null) {
                    double degrees = Double.valueOf(result).doubleValue();
                    initialRotateAngle = degrees;
                    ;
                    imageModel.rotate(degrees);
                    imagePanel.revalidate();
                }
            }
        });
        menuRotate.add(menuRotateAny);

        // Crop
        menuCropCrop.setText("Crop");
        menuCropCrop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imagePanel == null) return;
                imagePanel.crop();
            }
        });
        menuImage.add(menuCropCrop);

        // Image Fit
        menuImageFit.setText("Fit Image");
        menuImageFit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
            InputEvent.CTRL_MASK));
        menuImageFit.setState(fitIfLarger);
        menuImageFit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fitIfLarger = menuImageFit.isSelected();
                fitImage();
            }
        });
        menuImage.add(menuImageFit);

        // Image Restore
        menuImageRestore.setText("Restore");
        menuImageRestore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(imageModel != null) {
                    imageModel.restore();
                }
            }
        });
        menuImage.add(menuImageRestore);

        // Help
        menuHelp.setText("Help");
        menuBar.add(menuHelp);

        menuHelpOverview.setText("Overview");
        menuHelpOverview.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    help();
                } catch(Exception ex) {
                    overview();
                    Utils.excMsg("Could not find the HTML help file", ex);
                }
            }
        });
        menuHelp.add(menuHelpOverview);

        menuHelpAbout.setText("About");
        menuHelpAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JOptionPane.showMessageDialog(null, new AboutBoxPanel(
                    VERSION_STRING), "About", JOptionPane.PLAIN_MESSAGE);
            }
        });
        menuHelp.add(menuHelpAbout);
    }

    private void draw() {
        if(!drawEnabled) {
            return;
        }
        Cursor oldCursor = getCursor();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            BufferedImage image = fm.getImage();
            imageModel.replaceImage(image);
            if(imagePanel != null) {
                imagePanel.repaint();
                imagePanel.revalidate();
            }
            // Recalculate the fitImage state
            fitImage();
        } catch(Throwable t) {
            Utils.excMsg("Error drawing image", t);
        } finally {
            setCursor(oldCursor);
        }
    }

    public void resize() {
        if(imageModel == null) return;
        // saveComponentState();
        Rectangle2D cRect = fm.getcRect();
        int imageWidth = fm.getImageWidth();
        int imageHeight = fm.getImageHeight();
        Rectangle rectZoom = new Rectangle(imagePanel.getClipRectangle());
        // Scale the rectangle to the current zoom
        double zoom = imagePanel.getZoom();
        if(zoom != 0) {
            rectZoom.x /= zoom;
            rectZoom.y /= zoom;
            rectZoom.width /= zoom;
            rectZoom.height /= zoom;
        }
        Rectangle2D rect = rectZoom.getBounds2D();
        // DEBUG
        // System.out.println("resize: " + rect);
        if(rect.getWidth() <= 0 || rect.getHeight() <= 0) {
            return;
        }
        double x = cRect.getMinX() + cRect.getWidth() / (imageWidth - 1)
            * rect.getMinX();
        double width = cRect.getWidth() / (imageWidth - 1)
            * (rect.getMaxX() - rect.getMinX());
        double y = cRect.getMinY() + cRect.getHeight() / (imageHeight - 1)
            * rect.getMinY();
        double height = cRect.getHeight() / (imageHeight - 1)
            * (rect.getMaxY() - rect.getMinY());
        cRect.setRect(x, y, width, height);
        saveRegionState();
        draw();
    }

    public void resetRegion() {
        fm.setcRect(fm.getcRectDefaultClone());
        saveRegionState();
        draw();
    }

    public void resizeRegion(boolean out) {
        double scale;
        if(out) {
            scale = ZOOM_FACTOR;
        } else {
            scale = 1. / ZOOM_FACTOR;
        }
        Rectangle2D cRect = fm.getcRect();
        double x0 = cRect.getCenterX();
        double y0 = cRect.getCenterY();
        double width = cRect.getWidth() * scale;
        double height = cRect.getHeight() * scale;
        cRect.setRect(x0 - .5 * width, y0 - .5 * height, width, height);
        saveRegionState();
        draw();
    }

    /**
     * Implements opening a file.
     */
    private void openFile() {
        if(displayPanel == null) return;
        JFileChooser chooser = new JFileChooser();
        if(currentDir != null) {
            File file = new File(currentDir);
            if(file != null && file.exists()) {
                chooser.setCurrentDirectory(file);
            }
        }
        int result = chooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            // Save the selected path for next time
            File file = new File(currentDir);
            File parent = file.getParentFile();
            if(parent != null && parent.exists()) {
                chooser.setCurrentDirectory(parent);
            } else if(file != null && file.exists()) {
                chooser.setCurrentDirectory(file);
            }
            Cursor oldCursor = getCursor();
            try {
                file = chooser.getSelectedFile();
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                imageModel.readImage(file);
                fitImage();
                this.setTitle(file.getPath());
            } finally {
                setCursor(oldCursor);
            }
        }
    }

    /**
     * Implements opening a directory.
     */
    private void openDirectory() {
        if(displayPanel == null) return;
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(currentDir != null) {
            File file = new File(currentDir);
            // File parent = file.getParentFile();
            // if(parent != null && parent.exists()) {
            // chooser.setCurrentDirectory(parent);
            // } else if(file != null && file.exists()) {
            // chooser.setCurrentDirectory(file);
            // }
            if(file != null && file.exists()) {
                chooser.setCurrentDirectory(file);
            }
        }
        int result = chooser.showOpenDialog(this);
        if(result == JFileChooser.APPROVE_OPTION) {
            // Save the selected path for next time
            currentDir = chooser.getSelectedFile().getPath();
        }
    }

    /**
     * Implements print.
     */
    public void print() {
        if(displayPanel == null) return;
        if(imageModel == null) return;
        Cursor oldCursor = getCursor();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            PrinterJob job = PrinterJob.getPrinterJob();
            // Use the PrintService (printer) specified last time
            if(printService != null) job.setPrintService(printService);
            if(job.printDialog(printAttributes)) {
                job.setPrintable(imageModel);
                job.print(printAttributes);
                printService = job.getPrintService();
            }
        } catch(Exception ex) {
            Utils.excMsg("Printing failed:", ex);
        } finally {
            setCursor(oldCursor);
        }
    }

    /**
     * Implements print setup.
     */
    public void pageSetup() {
        Cursor oldCursor = getCursor();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            PrinterJob job = PrinterJob.getPrinterJob();
            pageFormat = job.pageDialog(printAttributes);
        } finally {
            setCursor(oldCursor);
        }
    }

    /**
     * Implements print preview.
     */
    public void printPreview() {
        if(displayPanel == null) return;
        if(imageModel == null) return;
        Cursor oldCursor = getCursor();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            PrintPreviewDialog dialog = new PrintPreviewDialog(imageModel,
                pageFormat, 1);
            dialog.setVisible(true);
        } finally {
            setCursor(oldCursor);
        }
    }

    /**
     * Saves the image to the clipboard.
     */
    public void copy() {
        if(displayPanel == null) return;
        if(imageModel == null) return;
        BufferedImage image = imageModel.getCurrentImage();
        if(image == null) {
            Utils.errMsg("No image");
            return;
        }
        Cursor oldCursor = getCursor();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            imageModel.copy();
        } finally {
            setCursor(oldCursor);
        }
    }

    /**
     * Pastes an image.
     */
    public void paste() {
        if(displayPanel == null) return;
        if(imageModel == null) return;
        Cursor oldCursor = getCursor();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            imageModel.paste();
            fitImage();
            this.setTitle("New Image");
        } finally {
            setCursor(oldCursor);
        }
    }

    /**
     * Saves the display frame to a file
     */
    public void saveAs() {
        if(displayPanel == null) return;
        if(imageModel == null) return;
        BufferedImage image = imageModel.getCurrentImage();
        if(image == null) {
            Utils.errMsg("No image");
            return;
        }
        Cursor oldCursor = getCursor();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            File file = ImageUtils.saveImageToFile(image, currentDir);
            if(file != null && file.exists()) {
                imageModel.setFile(file);
                // Set the currentDirectory based on what was saved
                File parent = file.getParentFile();
                if(parent != null && parent.exists()) {
                    currentDir = parent.getPath();
                } else {
                    currentDir = file.getPath();
                }
            }
        } finally {
            setCursor(oldCursor);
        }
    }

    /**
     * Saves the display frame to a file
     */
    public boolean save(File file) {
        boolean retVal = false;
        if(displayPanel == null) return false;
        if(imageModel == null) return false;
        BufferedImage image = imageModel.getCurrentImage();
        if(image == null) {
            Utils.errMsg("No image");
            return false;
        }
        Cursor oldCursor = getCursor();
        try {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            retVal = ImageUtils.saveImage(image, file);
        } finally {
            setCursor(oldCursor);
        }
        return retVal;
    }

    /**
     * Quits the application
     */
    private void quit() {
        System.exit(0);
    }

    private boolean help() throws IOException {
        JTextPane tp = new JTextPane();
        JScrollPane sp = new JScrollPane();
        sp.getViewport().add(tp);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(sp);
        frame.pack();
        frame.setLocationRelativeTo(this);
        frame.setSize(600, 600);
        ImageUtils.setIconImageFromResource(frame,
            "/resources/FractalBrowser.32x32.png");
        // Note that help.html does not work because *.html files
        // are filtered from building to bin. Could have changed the
        // Java Compiler | Building options.
        URL url = FractalBrowser.class.getResource("/resources/help.htm");
        tp.setPage(url);
        frame.setVisible(true);
        return true;
    }

    /**
     * Scales the image according to the fitAlways and fitIfLarger;
     */
    private void fitImage() {
        menuImageFit.setSelected(fitIfLarger);
        if(imagePanel == null) {
            return;
        }
        if(fitAlways) {
            imagePanel.zoomFit();
        } else if(fitIfLarger) {
            imagePanel.zoomFitIfLarger();
        } else {
            imagePanel.zoomReset();
        }
    }

    /**
     * Shows the image info.
     */
    private void imageInfo() {
        if(imageModel == null) return;
        String info = imageModel.getInfo();
        info += fm.getInfo() + LS;
        Utils.infoMsg(info);
    }

    /**
     * Shows available suffices in a message box.
     */
    private static void showSuffixes() {
        String info = ImageUtils.getSuffixInfo();
        Utils.infoMsg(info);
    }

    /**
     * Overview message.
     */
    private void overview() {
        String ls = LS;
        StringBuilder sb = new StringBuilder();
        sb.append("Fractal Browser allows you to manipulate a fractal image."
            + ls);
        sb.append("You can save the image as a new file and do Cut, Copy," + ls);
        sb.append("and Paste.  Some image manipulation is available from" + ls);
        sb.append("the Image menu.  These only affect the current image" + ls);
        sb.append("and are lost when the image is recalculated.  You can" + ls);
        sb.append("use them before saving." + ls + ls);
        sb.append("The image formats you can use to save will depend on" + ls);
        sb.append("whether Java Advanced Imaging (JAI and JAI ImageIO) are"
            + ls);
        sb.append("installed.  You can see the available formats under the "
            + ls);
        sb.append("Info menu." + ls);
        sb.append("" + ls);
        sb.append("Accelerator keys:" + ls);
        sb.append("  Undo: Ctrl-Z" + ls);
        sb.append("  Redo: Ctrl-Y" + ls);
        sb.append("  Copy: Ctrl-C" + ls);
        sb.append("  Paste: Ctrl-V" + ls);
        sb.append("  Paste and Print: Ctrl-B" + ls);
        sb.append("  Print: Ctrl-P" + ls);
        sb.append("  Always Fit: Ctrl-F" + ls);
        sb.append("  Save: Ctrl-S" + ls);
        sb.append("  Resize: Ctrl-R" + ls);
        sb.append("" + ls);
        sb.append("Mouse:" + ls);
        sb.append("  Drag: Set rectangle" + ls);
        sb.append("  Double click: Remove rectangle" + ls);
        sb.append("  Shift-click: Zoom In" + ls);
        sb.append("  Ctrl-click: Zoom Out" + ls);
        sb.append("  Alt-click: Zoom Reset" + ls);
        Utils.infoMsg(sb.toString());
    }

    /**
     * Creates a color scheme in the colorSchemeValues array if it has not been
     * created yet, sets that scheme, and redisplays the image. New schemes must
     * be added here and in IConstants.
     * 
     * @param index The index of the scheme in the colorSchemeValues array.
     */
    public void setColorScheme(int index) {
        if(index < 0 || index >= colorSchemes.length) {
            return;
        }
        if(colorSchemes[index] == null) {
            switch(index) {
            case SCHEME_RAINBOW:
                colorSchemes[index] = ColorSchemes.makeRainbowScheme(N_COLORS);
                break;
            case SCHEME_LINEAR:
                colorSchemes[index] = ColorSchemes.makeLinearScheme(N_COLORS);
                break;
            case SCHEME_REVERSE_LINEAR:
                colorSchemes[index] = ColorSchemes
                    .makeReverseLinearScheme(N_COLORS);
                break;
            case SCHEME_BW:
                colorSchemes[index] = ColorSchemes.makeBWScheme(N_COLORS);
                break;
            case SCHEME_GRAYSCALE:
                colorSchemes[index] = ColorSchemes
                    .makeGrayscaleScheme(N_COLORS);
                break;
            }
        }
        colorScheme = colorSchemes[index];
        colorSchemeIndex = index;
        if(colorSchemeCombo != null) {
            colorSchemeCombo.setSelectedIndex(index);
        }
        fm.setColorScheme(colorScheme);
    }

    /**
     * Saves the current fm of the given component for undo/redo.
     */
    /**
     * @param component
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    private void saveComponentState(Component component, String oldValue,
        String newValue) {
        // Don't save the fm while undo'ing or redo'ing
        if(editInProgress) {
            return;
        }
        undoManager.undoableEditHappened(new UndoableEditEvent(this,
            new UndoableComponentEdit(component, oldValue, newValue)));
        resetUndoRedoButtons();
    }

    /**
     * Saves the current fm of the region for undo/redo.
     */
    private void saveRegionState() {
        // // DEBUG
        // System.out.println("saveRegionState");
        // System.out.println("   undoManager (Before)=" + undoManager);
        // // Don't save the fm while undo'ing or redo'ing
        if(editInProgress) {
            return;
        }
        Rectangle2D newRect = (Rectangle2D)fm.getcRect().clone();
        undoManager.undoableEditHappened(new UndoableEditEvent(this,
            new UndoableRegionEdit(fm, regionLastValue, newRect)));
        regionLastValue = newRect;
        resetUndoRedoButtons();
        // // DEBUG
        // System.out.println("   undoManager (After)=" + undoManager);
    }

    private void resetUndoRedoButtons() {
        menuEditUndo.setEnabled(undoManager.canUndo());
        menuEditRedo.setEnabled(undoManager.canRedo());
        menuEditUndo.setText(undoManager.getUndoPresentationName());
        menuEditRedo.setText(undoManager.getRedoPresentationName());
    }

    // Getters and setters

    /**
     * @return The value of controlPanelMode.
     */
    public ControlPanelMode getControlPanelMode() {
        return controlPanelMode;
    }

    /**
     * @param controlPanelMode The new value for controlPanelMode.
     */
    public void setControlPanelMode(ControlPanelMode controlPanelMode) {
        resetControlPanel(controlPanelMode);
    }

    /**
     * @return The value of imagePanel.
     */
    public ScrolledImagePanel getImagePanel() {
        return imagePanel;
    }

    /**
     * @param imagePanel the imagePanel to set
     */
    public void setImagePanel(ScrolledImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    /**
     * @return The value of imageModel.
     */
    public ImageModel getImageModel() {
        return imageModel;
    }

    /**
     * @return The value of VERSION_STRING.
     */
    public static String getVersionString() {
        return VERSION_STRING;
    }

    /**
     * Main program.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Set window decorations
            JFrame.setDefaultLookAndFeelDecorated(true);

            // Set the native look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Make the job run in the AWT thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    FractalBrowser app = new FractalBrowser();
                    // Make it exit when the window manager close button is
                    // clicked
                    app.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    ImageUtils.setIconImageFromResource(app,
                        "/resources/FractalBrowser.32x32.png");
                    app.pack();
                    app.setVisible(true);
                    app.setLocationRelativeTo(null);
                }
            });
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

}
