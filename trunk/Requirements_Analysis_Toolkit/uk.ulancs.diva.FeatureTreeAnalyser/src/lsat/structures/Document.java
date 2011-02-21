/*
 * Document.java
 *
 * Created on April 4, 2005, 4:56 PM
 */

package lsat.structures;

/**
 *
 * @author stone
 */

import java.util.*;
import java.text.BreakIterator;
import java.io.*;
import lsat.lsa.*;
import java.net.*;
import java.util.regex.*;
import lsat.lsa.Chunk;

public class Document implements Serializable
{
    private String name;
    private String content;
    private DocumentClass docClass;
    private float version;
    private ArrayList<Chunk> chunkBoundaries;
    
    
    /**
     * 
     * @param name 
     * @param content 
     * @param docClass 
     * @param version 
     */
    public Document(String name, String content, DocumentClass docClass, float version)
    {
        this.name = name;
        this.content = content;
        this.docClass = docClass;
        this.version = version;
        
        chunkBoundaries = new ArrayList<Chunk>();
    }

    /**
     * 
     * @return 
     */
    public String getContent()
    {
        return this.content;
    }
    
    /**
     * 
     * @return 
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * 
     * @return 
     */
    public DocumentClass getDocumentClass()
    {
        return this.docClass;
    }
    
    /**
     * 
     * @return 
     */
    public float getVersion()
    {
        return this.version;
    }
    
}
