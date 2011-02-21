/*
 * Log.java
 *
 * Created on 01 November 2005, 16:17
 *
 *
 */

package lsat.log;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import javax.swing.JTextArea;

/**
 * @version 1.0
 * @since 01 November 2005
 * @author Andrew Stone
 */
public class Log implements Serializable
{
    public static final int INFO = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;
    public static final int TERMINAL = 3;
    
    private JTextArea outputArea;

    public void setOutputTextArea(JTextArea outputArea)
    {
        this.outputArea = outputArea;
    }
    
    public synchronized void log(int status, String message)
    {
        DateFormat dateFormatter= DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        DateFormat timeFormatter = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.getDefault());
        
        Date now = new Date();
        
        String date = new String(dateFormatter.format(now));
        String time = new String(timeFormatter.format(now));

        String output = "[" + time.toString() + " " + date.toString() + "] ";

        switch(status)
        {
            case 0:
                output = output.concat("INFO: " + message);
                break;
            case 1:
                output = output.concat("WARNING: " + message);
                break;
            case 2:
                output = output.concat("ERROR: " + message);
                break;
            case 3:
                output = output.concat("TERMINAL: " + message);
                break;
        }
        
        output = output.concat("\n");
        
        if(outputArea != null)        
        {
            outputArea.append(output);
            outputArea.setCaretPosition(outputArea.getText().length());
        }
        
        System.out.print(output);            
        
    }
}
