/*
 * DocumentBoundryList.java
 *
 * Created on 06 December 2005, 12:02
 *
 *
 */

package lsat.lsa;

import java.io.Serializable;
import java.util.ArrayList;
import lsat.structures.*;
import lsat.gui.ChunkAnalysisStruct;

/**
 * @version 1.0
 * @since 06 December 2005
 * @author Andrew Stone
 */
public class DocumentBoundryList extends ArrayList<DocumentBoundaryListPair> implements Serializable
{
    public static final int ALLDOCUMENTS = -1;
    
    public String getChunkText(int chunk)
    {
        String text = null;
        
        for(int i=0;i<this.size();i++)
        {
            text = this.get(i).getBoundaryContent(chunk);
            if(text != null)
                break;
        }
        
        return text;
    }
    
    //checked
    public void dumpChunksBelongingToDocumentsWithClass(int classID)
    {
        for(DocumentBoundaryListPair n : this)
        {
            if(n.documentClassIDMatches(classID))
            {
                n.dumpChunks();
            }
        }
    }
    
    //checked
    public int getNumberOfBoundaries()
    {
        int total = 0;

        for(DocumentBoundaryListPair n : this)
        {
            total += n.getNumberOfBoundaries();
        }

        return total;        
    }
    
    public ChunkAnalysisStruct getAnalysisStructOfChunk(int chunk)
    {
        ChunkAnalysisStruct cas = null;
        
        for(DocumentBoundaryListPair n : this)
        {
            cas = n.getChunkAsAnalysisStruct(chunk);
            if(cas != null)
                break;
        }
        
        return cas;
    }
    
    //checked
    public ArrayList<ChunkAnalysisStruct> getAnalysisStructs(int targetDocumentClass)
    {
        ArrayList<ChunkAnalysisStruct> returnStructs = new ArrayList<ChunkAnalysisStruct>();
        
        for(DocumentBoundaryListPair n : this)
        {
            if(n.documentClassIDMatches(targetDocumentClass) || targetDocumentClass == this.ALLDOCUMENTS)
            {
                for(Chunk c : n.getBoundaries())
                {
                    String content = n.getBoundaryContent(c.getColumn());
                    int column = c.getColumn();
                    int docClass = targetDocumentClass;
                    
                    if(this.ALLDOCUMENTS == targetDocumentClass)
                    {
                        docClass = n.getDocumentClass();
                    }
                    
                    ChunkAnalysisStruct cas = new ChunkAnalysisStruct(c.getColumn(),content, docClass, n.getFileName());
                    
                    returnStructs.add(cas);
                }
            }
        }
        
        return returnStructs;
    }
    
    public int getDocClassOfChunk(int chunk)
    {
        int docClass = -1;
        for(DocumentBoundaryListPair dbpl : this)
        {
            if(dbpl.containsChunk(chunk))
            {
                docClass = dbpl.getDocumentClass();
            }
        }
        
        return docClass;
    }
    
    
}
