package lsat.lsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;
import lsat.structures.*;
import lsat.gui.ChunkAnalysisStruct;
import lsat.log.*;

public class DocumentBoundaryListPair implements Serializable
{
    private Document d;
    private ArrayList<Chunk> boundaryList;
    private Log log = LogFactory.getLog();
    private TreeSet<Integer> chunkColumns;
    
    /**
     * 
     * @param d 
     */
    public DocumentBoundaryListPair(Document d)
    {
        this.d=d;
        boundaryList = new ArrayList<Chunk>();
        chunkColumns = new TreeSet<Integer>();
    }
    
    public String getFileName()
    {
        return d.getName();
    }
    
    /**
     * 
     * @param c 
     */
    public void addBoundary(Chunk c)
    {
        if(boundaryList.add(c))
        {
            chunkColumns.add(c.getColumn());
        }
    }
    
    /**
     * 
     * @return 
     */
    public String getContent()
    {
        return this.d.getContent();
    }
    
    /**
     * 
     * @return 
     */
    public int getNumberOfBoundaries()
    {
        return boundaryList.size();
    }
    
    /**
     * 
     * @return         
     */
    public ArrayList<Chunk> getBoundaries()
    {
        return this.boundaryList;
    }
    
    public String getBoundaryContent(int column)
    {
        String result = null;
        for(int i=0;i<boundaryList.size();i++)
        {
            Chunk currentBoundary = boundaryList.get(i);
            if (currentBoundary.getColumn() == column)
            {
                result = getContent().substring(currentBoundary.getStart(), 
                                                currentBoundary.getFinish());
                break;
            }
        }        
        
        return result;
    }
    
    //checked
    public void dumpChunks()
    {
//        log.log(Log.INFO, d.getContent());
        
        //System.out.println("Document name : " + d.getName() + "\n");
        for(Chunk c : boundaryList)
        {
            //System.out.println("Chunk number " + c.getColumn() + "\n");
            //System.out.println(d.getContent().substring(c.getStart(), c.getFinish()));
            //System.out.println("-----");
            String content = d.getContent().substring(c.getStart(), c.getFinish());
            content = content.replaceAll("[\\n\\r\\f]"," ");
            System.out.println(c.getColumn() + "\t" + content);
        }
        
    }
    
    //checked
    public boolean documentClassIDMatches(int id)
    {
        return (d.getDocumentClass().getId() == id);
    }
    
    //checked
    public int getDocumentClass()
    {
        return d.getDocumentClass().getId();
    }
    
    public boolean containsChunk(int chunk)
    {
        return chunkColumns.contains(chunk);
    }
    
    public ChunkAnalysisStruct getChunkAsAnalysisStruct(int chunk)
    {
        ChunkAnalysisStruct returnValue = null;
        
        if(chunkColumns.contains(chunk))
        {
            for(Chunk c : this.getBoundaries())
            {
                // we've found the right chunk to return
                if(c.getColumn() == chunk)
                {
                    returnValue = new ChunkAnalysisStruct(chunk, this.getBoundaryContent(chunk), this.getDocumentClass(), this.getFileName());
                }
            }
        }
        
        return returnValue;
    }
    
}
