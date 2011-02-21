/*
 * DocumentClassCollection.java
 *
 * Created on 13 December 2005, 17:10
 *
 *
 */

package lsat.structures;

import java.io.Serializable;
import java.util.*;
import lsat.log.*;

/**
 * @version 1.0
 * @since 13 December 2005
 * @author Andrew Stone
 */
public class DocumentClassCollection extends ArrayList<DocumentClass> implements Serializable
{
    private Log log = LogFactory.getLog();
    
    public String getDescription(int id)
    {
        String returnValue = "";
        for(int i=0;i<this.size();i++)
        {
            if(this.get(i).getId() == id)
            {
                returnValue = this.get(i).getDescription();
            }
        }
        
        if(returnValue == "")
        {
            log.log(Log.ERROR, "Description for id " + id + " not found");
        }
        
        return returnValue;
    }
    
    public DocumentClass getDocumentClass(int id)
    {
        DocumentClass returnValue = null;
        for(int i=0;i<this.size();i++)
        {
            if(this.get(i).getId() == id)
            {
                returnValue = this.get(i);
            }
        }
        
        if(returnValue == null)
        {
            log.log(Log.ERROR, "DocumentClass for id " + id + " not found");
        }
        
        return returnValue;

        
    }
    
    public int addNewDocumentClass(String description)
    {
        int nextId = this.size() + 1;
        add(new DocumentClass( nextId, description));
        return nextId;
    }
}
