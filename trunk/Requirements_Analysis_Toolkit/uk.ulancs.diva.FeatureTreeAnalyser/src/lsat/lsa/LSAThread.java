/*
 * LSAThread.java
 *
 * Created on 20 December 2005, 10:38
 *
 *
 */

package lsat.lsa;

import lsat.gui.LSATApplication;
import lsat.structures.*;
import lsat.lsa.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * @version 1.0
 * @since 20 December 2005
 * @author Andrew Stone
 */
public class LSAThread extends Thread
{
    private LSAModule theModule;
    
    private ArrayList <Document> documentCollection;
    private DocumentClassCollection docClassCollection;
    private LSATApplication parent;
    private String regex;
    
    
    public LSAThread(ArrayList <Document> documentCollection, 
                        DocumentClassCollection docClassCollection, String regex,
                        LSATApplication parent)
    {
        theModule = new LSAModule();
        this.documentCollection = documentCollection;
        this.docClassCollection = docClassCollection;
        this.parent = parent;
        this.regex = regex;
    }
            
    public void run()
    {
        LSAResults theResults = theModule.performLSA(documentCollection, docClassCollection, regex, 2, 10, "");
        parent.showLSAResults(theResults);
    }
}
