/*
 * Term.java
 *
 * Created on April 5, 2005, 4:13 PM
 */

package lsat.lsa;

import java.io.Serializable;
import lsat.structures.*;

/**
 *
 * @author stone
 */
public class Term implements Serializable
{
    private String term;
    private int row;
    private boolean inCorpus;
    
    public static int nextRow = 0;
    
    /**
     * 
     * @param term 
     */
    
    public Term()
    {
        row = -1;
        term = null;
        inCorpus = true;
    }
    
    public Term(String term)
    {
        this.term = term;
        this.row = nextRow;
        nextRow++;
        inCorpus = true;
    }
    
    /**
     * 
     * @return 
     */
    public String getTerm()
    {
        return this.term;
        
    }
    
    public void setTerm(String term)
    {
        this.term = term;
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
     * @param row 
     */
    public void setRow(int row)
    {
        this.row = row;
    }
    
    public void setInCorpus(boolean inCorpus)
    {
        this.inCorpus = inCorpus;
    }
    
    public boolean isInCorpus()
    {
        return this.inCorpus;
    }
    
    public String toString()
    {
        if(inCorpus)
        {
           return this.term; 
        }
        else
        {
            return "+ " + this.term;
        }
    }
    
}
