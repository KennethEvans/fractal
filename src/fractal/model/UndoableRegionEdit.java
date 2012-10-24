package fractal.model;

import java.awt.geom.Rectangle2D;

import javax.swing.undo.AbstractUndoableEdit;

import fractal.ui.IConstants;

/*
 * Created on Oct 16, 2012
 * By Kenneth Evans, Jr.
 */

public class UndoableRegionEdit extends AbstractUndoableEdit implements
    IConstants
{
    private static final long serialVersionUID = 1L;
    protected FractalModel fractalModel;
    protected Rectangle2D oldValue;
    protected Rectangle2D newValue;

    public UndoableRegionEdit(FractalModel fractalModel, Rectangle2D oldValue,
        Rectangle2D newValue) {
        this.fractalModel = fractalModel;
        this.newValue = newValue;
        this.oldValue = oldValue;
    }

    public String getPresentationName() {
        return REGION_PRESENTATION_NAME;
    }

    public void undo() {
        // // DEBUG
        // System.out.println("undo (Before): " + getPresentationName()
        // + " oldValue=" + oldValue);
        // System.out.println("undo (Before): " + getPresentationName()
        // + " newValue=" + newValue);
        // System.out.println("undo (Before): " + getPresentationName()
        // + " stateValue=" + fractalModel.getcRect());
        super.undo();
        fractalModel.setcRect((Rectangle2D)oldValue.clone());
        // // DEBUG
        // System.out.println("undo (After): " + getPresentationName()
        // + " stateValue=" + fractalModel.getcRect());
    }

    public void redo() {
        // // DEBUG
        // System.out.println("redo (Before): " + getPresentationName()
        // + " oldValue=" + oldValue);
        // System.out.println("redo (Before): " + getPresentationName()
        // + " newValue=" + newValue);
        // System.out.println("redo (Before): " + getPresentationName()
        // + " stateValue=" + fractalModel.getcRect());
        super.redo();
        fractalModel.setcRect((Rectangle2D)newValue.clone());
        // // DEBUG
        // System.out.println("redo (After): " + getPresentationName()
        // + " stateValue=" + fractalModel.getcRect());
    }

}
