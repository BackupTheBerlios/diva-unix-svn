/*
 * LogFactory.java
 *
 * Created on 01 November 2005, 16:17
 *
 *
 */

package lsat.log;

/**
 * @version 1.0
 * @since 01 November 2005
 * @author Andrew Stone
 */
public abstract class LogFactory
{
    private static Log logger;
    
    public static synchronized Log getLog()
    {
        if(logger == null)
        {
            logger = new Log();
        }
        
        return logger;
    }
}
