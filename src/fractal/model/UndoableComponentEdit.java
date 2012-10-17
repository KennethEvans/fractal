package fractal.model;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.undo.AbstractUndoableEdit;

/*
 * Created on Oct 16, 2012
 * By Kenneth Evans, Jr.
 */

public class UndoableComponentEdit extends AbstractUndoableEdit
{
    private static final long serialVersionUID = 1L;
    protected Component component;
    protected String oldValue;
    protected String newValue;

    public UndoableComponentEdit(Component component, String oldValue, String newValue) {
        this.component = component;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public String getPresentationName() {
        return component.getName() + " Change";
    }

    public void undo() {
        // DEBUG
        System.out.println("undo (Before): " + getPresentationName()
            + " newValue=" + newValue);
        super.undo();
        if(component instanceof JTextField) {
            ((JTextField)component).setText(oldValue);
            postEnterEvent();
        }
        // DEBUG
        System.out.println("undo (After): " + getPresentationName()
            + " oldValue=" + oldValue);
    }

    public void redo() {
        // DEBUG
        System.out.println("redo (Before): " + getPresentationName()
            + " newValue=" + newValue);
        super.redo();
        if(component instanceof JTextField) {
            ((JTextField)component).setText(newValue);
            postEnterEvent();
        }
        // DEBUG
        System.out.println("redo (After): " + getPresentationName()
            + " newValue=" + newValue);
    }

    /**
     * Sets the focus to the state and sends a VK_ENTER event to cause it to
     * execute its actionPerformed method.
     */
    public void postEnterEvent() {
        if(component == null) {
            return;
        }
        component.requestFocus();
        KeyEvent ev = new KeyEvent(component, KeyEvent.KEY_PRESSED,
            System.currentTimeMillis(), 0, KeyEvent.VK_ENTER,
            KeyEvent.CHAR_UNDEFINED);
        component.dispatchEvent(ev);
    }
}
