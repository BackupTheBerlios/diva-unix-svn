/*
 * ChunkPair.java
 *
 * Created on 15 November 2005, 12:18
 *
 *
 */

package lsat.lsa;

/**
 * @version 1.0
 * @since 15 November 2005
 * @author Andrew Stone
 */
public class ChunkPair
{
    private int first;
    private int second;
    
    /**
     * Creates a new instance of ChunkPair 
     */
    public ChunkPair(int first, int second)
    {
        this.first = first;
        this.second = second;
    }
    
    public int getFirst()
    {
        return this.first;
    }
    
    public int getSecond()
    {
        return this.second;
    }
}
