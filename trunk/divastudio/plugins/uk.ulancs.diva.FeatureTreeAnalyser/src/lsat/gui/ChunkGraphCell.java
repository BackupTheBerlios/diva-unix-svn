/*
 * ChunkGraphCell.java
 *
 * Created on 19 May 2006, 16:34
 *
 *
 */

package lsat.gui;

import java.awt.event.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.border.*;
import org.jgraph.graph.*;

/**
 * @version 1.0
 * @since 19 May 2006
 * @author Andrew Stone
 */

public class ChunkGraphCell extends DefaultGraphCell implements Comparable<ChunkGraphCell>
{
    private ChunkAnalysisStruct cas;
    private boolean isLeft;
    private Border highlightBorder;
    
    /**
     * Creates a new instance of ChunkGraphCell
     * @param cas 
     * @param isLeft 
     */
    public ChunkGraphCell(ChunkAnalysisStruct cas, boolean isLeft)
    {
        super(cas.getChunkNo());
        this.cas = cas;
        this.isLeft = isLeft;
        
        highlightBorder = BorderFactory.createLineBorder(Color.RED, 10);
        
        GraphConstants.setGradientColor(getAttributes(), cas.getColor());
        GraphConstants.setOpaque(getAttributes(), true);        
    }
    
    /**
     * 
     * @return 
     */
    public boolean isLeft()
    {
        return this.isLeft;
    }
    
    /**
     * 
     * @return 
     */
    public String getContent()
    {
        return cas.getContent();
    }
    
    public int getChunkNo()
    {
        return cas.getChunkNo();
    }
    
    public boolean hasChunkNo(int chunkNo)
    {
        return (cas.getChunkNo() == chunkNo);
    }

    public void highlight()
    {
        GraphConstants.setBorder(getAttributes(), highlightBorder);
    }
    
    public void unHighlight()
    {
        GraphConstants.setRemoveAttributes(getAttributes(), new Object[] {GraphConstants.BORDER});
    }

    public int compareTo(ChunkGraphCell o)
    {
        if(this.getChunkNo() < o.getChunkNo())
        {
            return -1;
        }
        else if(this.getChunkNo() == o.getChunkNo())
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
    
    public String getDocumentName()
    {
        return cas.getDocumentName();
    }
    
}
