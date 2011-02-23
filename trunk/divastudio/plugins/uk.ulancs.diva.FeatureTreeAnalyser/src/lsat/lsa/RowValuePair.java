/*
 * RowValuePair.java
 *
 * Created on 08 November 2005, 12:20
 *
 *
 */

package lsat.lsa;

/**
 * @version 1.0
 * @since 08 November 2005
 * @author Andrew Stone
 */
public class RowValuePair
{
    
    private double value;
    private int row;
    
    /**
     * Creates a new instance of RowValuePair
     * @param row 
     * @param value 
     */
    public RowValuePair(int row, double value)
    {
        this.row = row;
        this.value = value;
    }
    
    /**
     * 
     * @return 
     */
    public int getRow()
    {
        return this.row;
    }
    
    /**
     * 
     * @return 
     */
    public double getValue()
    {
        return this.value;
    }
}
