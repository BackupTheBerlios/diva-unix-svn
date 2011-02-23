/*
 * ChunkViewTableModel.java
 *
 * Created on 17 May 2006, 13:37
 *
 *
 */

package lsat.gui;

import javax.swing.table.AbstractTableModel;
import java.util.*;
import lsat.log.*;

/**
 * @version 1.0
 * @since 17 May 2006
 * @author Andrew Stone
 */
public class ChunkViewTableModel extends AbstractTableModel
{
    private ArrayList<ChunkAnalysisStruct> dataModel;
    private Log log = LogFactory.getLog();
    
    /** Creates a new instance of ChunkViewTableModel */
    public ChunkViewTableModel(ArrayList<ChunkAnalysisStruct> dataModel)
    {
        this.dataModel = dataModel;
    }
    
    public String getColumnName(int column)
    {
        String result = "";
        switch(column)
        {
            case 0:
                result = "No";
                break;
            case 1:
                result = "Class";
                break;
            case 2:
                result = "Text";
                break;
            default:
                log.log(Log.ERROR, "Name for illegal column requested from DocumentFrameTableModel");
                result = null;
                break;
        }
        return result;        
    }
            
    public int getRowCount()
    {
        return dataModel.size();
    }

    public int getColumnCount()
    {
        return 3;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        ChunkAnalysisStruct currentStruct = dataModel.get(rowIndex);
        Object returnObject = null;
        
        switch(columnIndex)
        {
            case 0:
                returnObject = new HighLightStruct(currentStruct.getChunkNo());
                break;
            case 1:
                returnObject = currentStruct.getColor();
                break;
            case 2:
                returnObject = currentStruct.getContent();
                break;
            default:
        }
        
        return returnObject;
    }
    
    public Class getColumnClass(int c) 
    {
        if(c==0)
        {
            return lsat.gui.HighLightStruct.class;
        }
        else if(c==1)
        {
            return java.awt.Color.class;
        }
        else
        {
            return java.lang.String.class;
        }
    }    
    
    public void highlightRow(int row)
    {
        if(row < dataModel.size())
        {
            dataModel.get(row).setHighlighted(true);
        }
        
    }
    
    public boolean isHighlighted(int row)
    {
        if(dataModel.get(row).isHighlighted())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void unHighlightAll()
    {
        for(ChunkAnalysisStruct n : dataModel)
        {
            n.setHighlighted(false);
        }
    }
}
