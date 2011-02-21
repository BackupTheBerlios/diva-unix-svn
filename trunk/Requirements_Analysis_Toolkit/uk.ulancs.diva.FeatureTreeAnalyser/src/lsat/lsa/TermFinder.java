/*
 * TermFinder.java
 *
 * Created on April 5, 2005, 4:12 PM
 */

package lsat.lsa;

/**
 *
 * @author stone
 */

import java.io.*;

public class TermFinder extends lsat.lsa.PreProcessor
{
    
    
    /**
     * Creates a new instance of TermFinder
     * @param text 
     * @param pathToVerbStemList 
     * @param pathToStopWordList 
     */
    public TermFinder(String text, String pathToVerbStemList, String pathToStopWordList)
    {
        // TODO : Change this to dynamically read text file content from the JAR
        //InputStream stop = MyApp.class.getResourceAsStream( "stopworldlist.txt" );
        //InputStream  = MyApp.class.getResourceAsStream( "stopworldlist.txt" );
        //super(new StringReader(text.toLowerCase()), pathToVerbStemList, pathToStopWordList);
        super(new StringReader(text.toLowerCase()), pathToVerbStemList, pathToStopWordList);
    }
    
    /**
     * 
     * @throws java.io.IOException 
     * @return 
     */
    public String nextTerm() throws IOException
    {
        return this.yylex();
    }
    
    /**
     * 
     * @param input 
     */
    public void reset(String input)
    {
        this.yyreset(new StringReader(input.toLowerCase()));
    }
    
}
