/*
 * LSATPreferencesFactory.java
 *
 * Created on 20 December 2005, 10:41
 *
 *
 */

package lsat.structures;

/**
 * @version 1.0
 * @since 20 December 2005
 * @author Andrew Stone
 */
public abstract class LSATPreferencesFactory
{
    private static LSATPreferences preferences;
    
    public static synchronized LSATPreferences getPrefs(boolean load)
    {
        if(preferences == null)
        {
            preferences = new LSATPreferences(load);
        }

        return preferences;
    }
    

}
