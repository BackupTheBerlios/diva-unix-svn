/*
 * LSATPreferences.java
 *
 * Created on 20 December 2005, 10:39
 *
 *
 */

package lsat.structures;


import java.util.*;
/**
 * @version 1.0
 * @since 20 December 2005
 * @author Andrew Stone
 */
public class LSATPreferences extends HashMap<String, String>
{
    public LSATPreferences(boolean load)
    {
        if(load)
        {
            loadDefaultPrefs();
        }
        else
        {
            setDefaultPrefs();
        }
    }
    
    private void setDefaultPrefs()
    {
        put("lsa-reducedrows", "10");
        put("lsa-boundarysize", "2");
//        put("lsa-regex", "^[0-9]+ ");
        put("lsa-regex", "\\@\\@");
//        put("lsa-regex", "\\n");
    }
    
    private void loadDefaultPrefs()
    {
        // TODO : implement the ability to load preferences here
    }
}
