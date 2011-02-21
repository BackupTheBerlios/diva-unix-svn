/*
 * HighLightStruct.java
 *
 * Created on 29 May 2006, 14:16
 *
 *
 */

package lsat.gui;

/**
 * @version 1.0
 * @since 29 May 2006
 * @author Andrew Stone
 */
public class HighLightStruct
{
    private int n;
    /** Creates a new instance of HighLightStruct */
    public HighLightStruct(int n)
    {
        this.n = n;
    }
    
    public String toString()
    {
        return String.valueOf(n);
    }
}
