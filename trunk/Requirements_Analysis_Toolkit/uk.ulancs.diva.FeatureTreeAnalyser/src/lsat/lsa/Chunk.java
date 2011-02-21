/*
 * Chunk.java
 *
 * Created on April 12, 2005, 3:23 PM
 */

package lsat.lsa;

import java.io.Serializable;
import lsat.structures.*;

/**
 *
 * @author stone
 */
public class Chunk implements Serializable
{
    private int start;
    private int finish;
    private int column;
    
    public static int nextColumn = 0;
    
    /**
     * 
     * @param start 
     * @param finish 
     */
    public Chunk(int start, int finish)
    {
        this.start = start;
        this.finish = finish;
        
        this.column = nextColumn;
        nextColumn++;
    }
    
    /**
     * 
     * @param start 
     */
    public void setStart(int start)
    {
        this.start = start;
    }
    
    /**
     * 
     * @param finish 
     */
    public void setFinish(int finish)
    {
        this.finish = finish;
    }
    
    /**
     * 
     * @return 
     */
    public int getStart()
    {
        return this.start;
    }
    
    /**
     * 
     * @return 
     */
    public int getFinish()
    {
        return this.finish;
    }
    
    /**
     * 
     * @return 
     */
    public int getColumn()
    {
        return this.column;
    }
}
