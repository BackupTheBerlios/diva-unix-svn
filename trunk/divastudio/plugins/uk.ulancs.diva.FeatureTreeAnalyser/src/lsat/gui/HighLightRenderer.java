/*
 * HighLightRenderer.java
 *
 * Created on 29 May 2006, 14:00
 *
 *
 */

package lsat.gui;

/**
 * @version 1.0
 * @since 29 May 2006
 * @author Andrew Stone
 */
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;

public class HighLightRenderer extends JLabel implements TableCellRenderer
{
    Border unselectedBorder = null;
    Border selectedBorder = null;
    boolean isBordered = true;

    public HighLightRenderer(boolean isBordered) 
    {
        this.isBordered = isBordered;
        setOpaque(true); //MUST do this for background to show up.
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column)
    {
        ChunkViewTableModel theModel = (ChunkViewTableModel)(table.getModel());
        
        if(theModel.isHighlighted(row) == true)
        {
            this.setBackground(Color.PINK);
        }
        else
        {
            this.setBackground(Color.WHITE);
        }
        
        setText(value.toString());
        
        if (isBordered) 
        {
            if (isSelected) 
            {
                if (selectedBorder == null) 
                {
                    selectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
                                              table.getSelectionBackground());
                }
                setBorder(selectedBorder);
            } 
            else 
            {
                if (unselectedBorder == null) 
                {
                    unselectedBorder = BorderFactory.createMatteBorder(2,5,2,5,
                                              table.getBackground());
                }
                setBorder(unselectedBorder);
            }
        }
        
        
        return this;
    }
}
