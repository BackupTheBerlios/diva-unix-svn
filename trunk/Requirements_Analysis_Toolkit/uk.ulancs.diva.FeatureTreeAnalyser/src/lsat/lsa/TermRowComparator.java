/*
 * TermRowComparator.java
 *
 * Created on 09 November 2005, 16:17
 *
 *
 */

package lsat.lsa;

/**
 * @version 1.0
 * @since 09 November 2005
 * @author Andrew Stone
 */
public class TermRowComparator implements java.util.Comparator<Term>
{
    /**
     * 
     * @param first 
     * @param second 
     * @return 
     */
    public int compare(Term first, Term second)    
    {
        if(first.getRow() < second.getRow())
        {
            return -1;
        }
        else if(first.getRow() == second.getRow())
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
}
