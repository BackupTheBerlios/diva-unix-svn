/*
 * TermAlphabeticComparator.java
 *
 * Created on 14 December 2005, 16:59
 *
 *
 */

package lsat.lsa;

/**
 * @version 1.0
 * @since 14 December 2005
 * @author Andrew Stone
 */
public class TermAlphabeticComparator implements java.util.Comparator<Term>
{    
    public int compare(Term first, Term second)    
    {
        return first.getTerm().compareTo(second.getTerm());
    }
    
}
