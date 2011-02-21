/*
 * ColorTable.java
 *
 * Created on 17 May 2006, 13:42
 *
 *
 */

package lsat.gui;


import java.awt.Color;
import java.util.HashMap;

/**
 * @version 1.0
 * @since 17 May 2006
 * @author Andrew Stone
 */
public class ColorTable
{
    private static HashMap<Integer,Color> colorTable = new HashMap<Integer,Color>();

    /**
     * 
     * @param id 
     * @return 
     */
    public static Color getColourForID(int id)
    {
        if(colorTable.size() < 1)
        {
            colorTable.put(0, Color.ORANGE);
            colorTable.put(1, Color.RED);
            colorTable.put(2, Color.MAGENTA);
            colorTable.put(3, Color.PINK);
            colorTable.put(4, Color.YELLOW);
            colorTable.put(5, Color.MAGENTA);
            colorTable.put(6, Color.GREEN);
            colorTable.put(7, Color.BLUE);
            colorTable.put(8, Color.CYAN);
            colorTable.put(9, Color.LIGHT_GRAY);
        }
        return colorTable.get(id%colorTable.size());
    }
    
}
