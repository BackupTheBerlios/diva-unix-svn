/*
 * ChunkAnalysisStruct.java
 *
 * Created on 17 May 2006, 13:39
 *
 *
 */

package lsat.gui;

import java.awt.Color;
import java.io.File;

/**
 * @version 1.0
 * @since 17 May 2006
 * @author Andrew Stone
 */
public class ChunkAnalysisStruct
{
    private int chunkNo;
    private String content;
    private Color c;
    private boolean highlighted;
    private String documentName;
    
    /**
     * Creates a new instance of ChunkAnalysisStruct
     * @param chunkNo 
     * @param content 
     * @param classID 
     */
    public ChunkAnalysisStruct(int chunkNo, String content, int classID, String documentName)
    {
        this.chunkNo = chunkNo;
        this.content = content;
        this.c = ColorTable.getColourForID(classID);
        this.highlighted = false;
        this.documentName = documentName;
    }
    
    /**
     * 
     * @return 
     */
    public int getChunkNo()
    {
        return chunkNo;
    }
    
    /**
     * 
     * @return 
     */
    public String getContent()
    {
        return content;
    }
    
    /**
     * 
     * @return 
     */
    public Color getColor()
    {
        return c;
    }
    
    /**
     * 
     * @return 
     */
    public boolean isHighlighted()
    {
        return this.highlighted;
    }
    
    /**
     * 
     * @param highlighted 
     */
    public void setHighlighted(boolean highlighted)
    {
        this.highlighted = highlighted;
    }
    
    public String getDocumentName()
    {
        File f = new File(this.documentName);
        return f.getName();
    }
}
