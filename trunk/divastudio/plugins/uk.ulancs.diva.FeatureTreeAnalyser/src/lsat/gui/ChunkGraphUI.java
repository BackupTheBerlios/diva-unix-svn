/*
 * ChunkGraphUI.java
 *
 * Created on 22 May 2006, 14:08
 *
 *
 */

package lsat.gui;

import javax.swing.JTextArea;
import org.jgraph.plaf.basic.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import org.jgraph.graph.*;
import org.jgraph.JGraph;

/**
 * @version 1.0
 * @since 22 May 2006
 * @author Andrew Stone
 */
public class ChunkGraphUI extends BasicGraphUI
{
    private JTextArea first;
    private JTextArea second;
    
    /**
     * 
     * @param first 
     * @param second 
     */
    public ChunkGraphUI(JTextArea first, JTextArea second)
    {
        super();
        this.first = first;
        this.second = second;
        
    }
    /**
     * 
     * @return 
     */
    protected MouseListener createMouseListener()
    {
        return new ChunkMouseListener();
    }
    
    public class ChunkMouseListener extends MouseAdapter implements MouseMotionListener, Serializable
    {
        /* The cell under the mousepointer. */
        protected CellView cell;

        /* The object that handles mouse operations. */
        protected Object handler;

        protected transient Cursor previousCursor = null;

        /**
         * Invoked when a mouse button has been pressed on a component.
         * @param e 
         */
        public void mousePressed(MouseEvent e) 
        {
                handler = null;
                if (!e.isConsumed() && graph.isEnabled()) 
                {
                        graph.requestFocus();
                        int s = graph.getTolerance();
                        Rectangle2D r = graph.fromScreen(new Rectangle2D.Double(e
                                        .getX()
                                        - s, e.getY() - s, 2 * s, 2 * s));
                        lastFocus = focus;
                        focus = (focus != null && focus.intersects(graph, r)) ? focus
                                        : null;
                        cell = graph.getNextSelectableViewAt(focus, e.getX(), e.getY());
                        if (focus == null)
                                focus = cell;
                        completeEditing();
                        boolean isForceMarquee = isForceMarqueeEvent(e);
                        if (!isForceMarquee) 
                        {
                                if (e.getClickCount() == graph.getEditClickCount()
                                                && focus != null && focus.isLeaf()
                                                && focus.getParentView() == null
                                                && graph.isCellEditable(focus.getCell())
                                                && handleEditTrigger(cell.getCell(), e)) 
                                {
                                        e.consume();
                                        cell = null;
                                } 
                                else if (!isToggleSelectionEvent(e)) 
                                {
                                        if (handle != null) 
                                        {
                                                handle.mousePressed(e);
                                                handler = handle;
                                        }
                                        // Immediate Selection
                                        if (!e.isConsumed() && cell != null
                                                        && !graph.isCellSelected(cell.getCell())) 
                                        {
                                                selectCellForEvent(cell.getCell(), e);
                                                focus = cell;
                                                if (handle != null) 
                                                {
                                                        handle.mousePressed(e);
                                                        handler = handle;
                                                }
                                                e.consume();
                                                cell = null;
                                        }
                                }
                        }
                        // Marquee Selection
                        if (!e.isConsumed() && marquee != null && (!isToggleSelectionEvent(e) || focus == null || isForceMarquee)) 
                        {
                                marquee.mousePressed(e);
                                handler = marquee;
                        }
                }
        }

        /**
         * Handles edit trigger by starting the edit and return true if the
         * editing has already started.
         * 
         * @param cell
         *            the cell being edited
         * @param e
         *            the mouse event triggering the edit
         * @return <code>true</code> if the editing has already started
         */
        protected boolean handleEditTrigger(Object cell, MouseEvent e) 
        {
                graph.scrollCellToVisible(cell);
                if (cell != null)
                        startEditing(cell, e);
                return graph.isEditing();
        }

        public void mouseDragged(MouseEvent e) 
        {
                autoscroll(graph, e.getPoint());
                if (graph.isEnabled()) 
                {
                        if (handler != null && handler == marquee)
                                marquee.mouseDragged(e);
                        else if (handler == null && !isEditing(graph) && focus != null) 
                        {
                                if (!graph.isCellSelected(focus.getCell())) 
                                {
                                        selectCellForEvent(focus.getCell(), e);
                                        cell = null;
                                }
                                if (handle != null)
                                        handle.mousePressed(e);
                                handler = handle;
                        }
                        if (handle != null && handler == handle)
                                handle.mouseDragged(e);
                }
        }

        /**
         * Invoked when the mouse pointer has been moved on a component (with no
         * buttons down).
         */
        public void mouseMoved(MouseEvent e) 
        {
                if (previousCursor == null) 
                {
                        previousCursor = graph.getCursor();
                }
                if (graph != null && graph.isEnabled()) 
                {
                        if (marquee != null)
                                marquee.mouseMoved(e);
                        if (handle != null)
                                handle.mouseMoved(e);
                        if (!e.isConsumed() && previousCursor != null) 
                        {
                                Cursor currentCursor = graph.getCursor();
                                if (currentCursor != previousCursor) 
                                {
                                        graph.setCursor(previousCursor);
                                }
                                previousCursor = null;
                        }
                }
        }

        // Event may be null when called to cancel the current operation.
        public void mouseReleased(MouseEvent e) 
        {
            // my edit to fill the content 
            if(focus != null && focus.getCell() != null)
            {
                ChunkGraphCell currentCell = null;
                try
                {
                    currentCell = ((ChunkGraphCell)(focus.getCell()));
                }
                catch(java.lang.ClassCastException castingException)
                {
                    // TODO : add a log line here
                }
                if(currentCell != null)
                {
                    JTextArea out = null;
                    if(currentCell.isLeft())
                    {
                        out = first;
                    }
                    else
                    {
                       out = second;
                    }

                    out.setText("");
                    out.setText(currentCell.getDocumentName() + "\n" + currentCell.getContent());
                    out.setCaretPosition(0);                
                }
            }
            
                try 
                {
                        if (e != null && !e.isConsumed() && graph != null
                                        && graph.isEnabled()) 
                        {
                                if (handler == marquee && marquee != null)
                                        marquee.mouseReleased(e);
                                else if (handler == handle && handle != null)
                                        handle.mouseReleased(e);
                                if (isDescendant(cell, focus) && e.getModifiers() != 0) 
                                {
                                        // Do not switch to parent if Special Selection
                                        cell = focus;
                                }
                                if (!e.isConsumed() && cell != null) 
                                {
                                        Object tmp = cell.getCell();
                                        boolean wasSelected = graph.isCellSelected(tmp);
                                        // if (!wasSelected || e.getModifiers() != 0)
                                        selectCellForEvent(tmp, e);
                                        focus = cell;
                                        postProcessSelection(e, tmp, wasSelected);
                                }
                        }
                } 
                finally 
                {
                        handler = null;
                        cell = null;
                }
        }

        /**
         * Invoked after a cell has been selected in the mouseReleased method.
         * This can be used to do something interesting if the cell was already
         * selected, in which case this implementation selects the parent.
         * Override if you want different behaviour, such as start editing.
         */
        protected void postProcessSelection(MouseEvent e, Object cell, boolean wasSelected) 
        {
                if (wasSelected && graph.isCellSelected(cell) && e.getModifiers() != 0) 
                {
                        Object parent = cell;
                        Object nextParent = null;
                        while (((nextParent = graphModel.getParent(parent)) != null)
                                        && graphLayoutCache.isVisible(nextParent))
                                parent = nextParent;
                        selectCellForEvent(parent, e);
                        lastFocus = focus;
                        focus = graphLayoutCache.getMapping(parent, false);
                }
        }

        protected boolean isDescendant(CellView parentView, CellView childView) 
        {
                if (parentView == null || childView == null) 
                {
                        return false;
                }

                Object parent = parentView.getCell();
                Object child = childView.getCell();
                Object ancestor = child;

                do 
                {
                    if (ancestor == parent)
                        return true;
                } while ((ancestor = graphModel.getParent(ancestor)) != null);

                return false;
        }
    }
}
