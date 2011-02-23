/*
 * DocumentFrameTableModel.java
 *
 * Created on 20 December 2005, 11:52
 *
 *
 */

package lsat.gui;

import javax.swing.table.*;
import lsat.structures.*;
import java.util.*;
import lsat.log.*;

/**
 * @version 1.0
 * @since 20 December 2005
 * @author Andrew Stone
 */
public class DocumentFrameTableModel extends AbstractTableModel
{
    private ArrayList <Document> documentCollection;
    private Log log = LogFactory.getLog();
    
    /** Creates a new instance of DocumentFrameTableModel */
    public DocumentFrameTableModel(ArrayList <Document> documentCollection)
    {
        this.documentCollection = documentCollection;
    }
    
    public String getColumnName(int column)
    {
        String result = "";
        switch(column)
        {
            case 0:
                result = "Class";
                break;
            case 1:
                result = "Version";
                break;
            case 2:
                result = "Name";
                break;
            default:
                log.log(Log.ERROR, "Name for illegal column requested from DocumentFrameTableModel");
                result = null;
                break;
        }
        return result;
    }
    
    public Object getValueAt(int row, int column)
    {
        Document d = documentCollection.get(row);
        Object result;
        
        switch(column)
        {
            case 0:
                result = d.getDocumentClass().getDescription();
                break;
            case 1:
                result = d.getVersion();
                break;
            case 2:
                result = d.getName();
                break;
            default:
                log.log(Log.ERROR, "Illegal column requested from DocumentFrameTableModel");
                result = null;
                break;
        }
        
        return result;
    }
    
    public int getColumnCount()
    {
        return 3;
    }
    
    public int getRowCount()
    {
        return documentCollection.size();
    }
}
