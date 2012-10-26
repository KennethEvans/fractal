package fractal.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.kenevans.imagemodel.utils.Utils;

/**
 * CustomRegionDialog is a dialog to see the right, left, top, and bottom of the
 * region. If it is successful, getValue() will return the new cRect, otherwise
 * false.
 * 
 * @author Kenneth Evans, Jr.
 */
public class CustomRegionDialog extends JDialog implements IConstants
{
    private static final long serialVersionUID = 1L;
    Rectangle2D cRect;
    Rectangle2D cRectNew;
    Rectangle2D cRect1 = new Rectangle2D.Double(-2.08, -1.20, 3.20, 2.40);
    Rectangle2D cRect2 = new Rectangle2D.Double(-7.00, -8.00, 12.00, 16.00);

    JTextField leftText;
    JTextField rightText;
    JTextField topText;
    JTextField bottomText;

    /**
     * Constructor
     */
    public CustomRegionDialog(Component parent, Rectangle2D cRect) {
        super();
        this.cRect = cRect;
        init();
        reset(cRect);
        // Locate it on the screen
        this.setLocationRelativeTo(parent);
    }

    /**
     * This method initializes this dialog
     * 
     * @return void
     */
    private void init() {
        this.setTitle("Custom Region");
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridBagLayout());

        GridBagConstraints gbcDefault = new GridBagConstraints();
        gbcDefault.insets = new Insets(2, 2, 2, 2);
        gbcDefault.weightx = 100;
        gbcDefault.anchor = GridBagConstraints.WEST;
        gbcDefault.fill = GridBagConstraints.NONE;
        // gbcDefault.fill = GridBagConstraints.HORIZONTAL;
        GridBagConstraints gbc = null;

        // Left

        JLabel label = new JLabel("Left:");
        label.setToolTipText("The left side of the region.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(label, gbc);

        leftText = new JTextField(30);
        leftText.setToolTipText(label.getText());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPane.add(leftText, gbc);

        // Right
        label = new JLabel("Right:");
        label.setToolTipText("The right side of the region.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(label, gbc);

        rightText = new JTextField(30);
        rightText.setToolTipText(label.getText());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPane.add(rightText, gbc);

        // Top
        label = new JLabel("Top:");
        label.setToolTipText("The top side of the region.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(label, gbc);

        topText = new JTextField(30);
        topText.setToolTipText(label.getText());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(topText, gbc);

        // Bottom
        label = new JLabel("Bottom:");
        label.setToolTipText("The bottom side of the region.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(label, gbc);

        bottomText = new JTextField(30);
        bottomText.setToolTipText(label.getText());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPane.add(bottomText, gbc);

        // Presets panel
        JPanel presetsPanel = new JPanel();
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridy = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(presetsPanel, gbc);

        JButton button = new JButton();
        button.setText("Preset 1");
        button.setToolTipText("Set to Preset 1 (Default).");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                reset(cRect1);
            }
        });
        presetsPanel.add(button);

        button = new JButton();
        button.setText("Preset 2");
        button.setToolTipText("Set to Preset 2 (Trignometric).");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                reset(cRect2);
            }
        });
        presetsPanel.add(button);

        // Button panel
        JPanel buttonPanel = new JPanel();
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridy = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(buttonPanel, gbc);

        button = new JButton();
        button.setText("OK");
        button.setToolTipText("Apply the changes.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                boolean result = apply();
                if(result) {
                    CustomRegionDialog.this.setVisible(false);
                }
            }
        });
        buttonPanel.add(button);

        button = new JButton();
        button.setText("Reset");
        button.setToolTipText("Reset to the original values.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                reset(cRect);
            }
        });
        buttonPanel.add(button);

        button = new JButton();
        button.setText("Cancel");
        button.setToolTipText("Close the dialog and do nothing.");
        button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                CustomRegionDialog.this.setVisible(false);
            }
        });
        buttonPanel.add(button);

        pack();
    }

    /**
     * Resets the internal state from the input cRect. Can also be used to
     * initialize the dialog.
     */
    public void reset(Rectangle2D cRect) {
        if(leftText != null) {
            leftText.setText(Double.toString(cRect.getX()));
        }
        if(rightText != null) {
            rightText.setText(Double.toString(cRect.getX() + cRect.getWidth()));
        }
        if(topText != null) {
            topText.setText(Double.toString(cRect.getY()));
        }
        if(bottomText != null) {
            bottomText
                .setText(Double.toString(cRect.getY() + cRect.getHeight()));
        }
    }

    /**
     * Collects the values of the components, and if they are valid, then calls
     * the saveFile method in the viewer.
     * 
     * @return True on success to close the dialog or false otherwise to leave
     *         the dialog up.
     */
    public boolean apply() {
        cRectNew = (Rectangle2D)cRect.clone();
        String text;

        double left = 0;
        text = leftText.getText();
        try {
            left = Double.parseDouble(text);
        } catch(NumberFormatException ex) {
            Utils.errMsg("Invalid value for left");
            cRectNew = null;
            return false;
        }

        double right = 0;
        text = rightText.getText();
        try {
            right = Double.parseDouble(text);
        } catch(NumberFormatException ex) {
            Utils.errMsg("Invalid value for right");
            cRectNew = null;
            return false;
        }

        double top = 0;
        text = topText.getText();
        try {
            top = Double.parseDouble(text);
        } catch(NumberFormatException ex) {
            Utils.errMsg("Invalid value for top");
            cRectNew = null;
            return false;
        }

        double bottom = 0;
        text = bottomText.getText();
        try {
            bottom = Double.parseDouble(text);
        } catch(NumberFormatException ex) {
            Utils.errMsg("Invalid value for bottom");
            cRectNew = null;
            return false;
        }

        if(left == right) {
            Utils.errMsg("Width is 0");
            return false;
        }
        if(left > right) {
            Utils.errMsg("Left is larger than Right");
            return false;
        }
        double width = right - left;

        if(top == bottom) {
            Utils.errMsg("Height is 0");
            return false;
        }
        if(top > bottom) {
            Utils.errMsg("Top is larger than Bottom");
            return false;
        }
        double height = bottom - top;

        cRectNew = new Rectangle2D.Double(left, top, width, height);
        return true;
    }

    /**
     * @return The value of cRectNew, which is null unless apply is successful.
     */
    public Rectangle2D getValue() {
        return cRectNew;
    }

}
