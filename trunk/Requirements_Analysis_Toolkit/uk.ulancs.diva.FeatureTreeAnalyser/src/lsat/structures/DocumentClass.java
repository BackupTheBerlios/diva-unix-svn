/*
 * DocumentClass.java
 *
 * Created on 13 December 2005, 17:09
 *
 *
 */

package lsat.structures;

import java.io.Serializable;

/**
 * @version 1.0
 * @since 13 December 2005
 * @author Andrew Stone
 */
public class DocumentClass implements Serializable
{
    private int id;
    private String description;
    
    public DocumentClass(int id, String description)
    {
        this.id = id;
        this.description =  description;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public String getDescription()
    {
        return this.description;
    }

}
