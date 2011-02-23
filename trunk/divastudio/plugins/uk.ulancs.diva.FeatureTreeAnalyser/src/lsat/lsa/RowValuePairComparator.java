/*
 * RowValuePairComparator.java
 *
 * Created on 08 November 2005, 12:30
 *
 *
 */

package lsat.lsa;
import java.util.*;

/**
 * @version 1.0
 * @since 08 November 2005
 * @author Andrew Stone
 */
public class RowValuePairComparator implements Comparator<RowValuePair>
{
    
    /**
     * 
     * @param first 
     * @param second 
     * @return 
     */
    public int compare(RowValuePair first, RowValuePair second)
    {
        if(first.getValue() < second.getValue())
        {
            return -1;
        }
        else if(first.getValue() == second.getValue())
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    
}
