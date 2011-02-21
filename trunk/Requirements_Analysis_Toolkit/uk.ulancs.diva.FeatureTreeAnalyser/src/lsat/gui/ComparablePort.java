/*
 * ComparablePort.java
 *
 * Created on 20 July 2006, 14:17
 *
 *
 */

package lsat.gui;

import org.jgraph.graph.*;

/**
 * @version 1.0
 * @since 20 July 2006
 * @author Andrew Stone
 */
public class ComparablePort extends DefaultPort implements Comparable<Port>
{
    public int compareTo(Port o)
    {
        int selfHash = this.hashCode();

        int foreignHash = o.hashCode();
        
        if(selfHash < foreignHash)
        {
            return -1;
        }
        else if(selfHash == foreignHash)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
}
