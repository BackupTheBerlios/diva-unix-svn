/*
 * Project.java
 *
 * Created on April 4, 2005, 4:56 PM
 */

package lsat.structures;

/**
 *
 * @author stone
 */

import java.util.ArrayList;
import lsat.lsa.LSAModule;
import java.io.*;
import lsat.lsa.*;


public class Project implements Serializable 
{
    private String projectName;
    private ArrayList <Document> documentCollection;
    private LSAModule lsaModule;
    private DocumentClassCollection docClassCollection;

    public Project(String name)
    {
        this.projectName = name;
        this.documentCollection = new ArrayList<Document>();
        docClassCollection = new DocumentClassCollection();
        this.lsaModule = new LSAModule();
    }
    
    private String readFileContent(File in)
    {
        BufferedReader reader;
        StringBuffer content = new StringBuffer();
        char[] tempBuffer = new char[1024];

        try
        {
            reader = new BufferedReader(new FileReader(in));
            
            String line;
            while ((line = reader.readLine()) != null) {
            	content.append(line);
        		content.append(System.getProperty("line.separator"));
            }
            
    		content.append(System.getProperty("line.separator"));
//            while(reader.ready())
//            {
//                reader.read(tempBuffer);
//                content.append(tempBuffer);
//            }
            
        }
        catch(FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        
        return content.toString();
    }
    
    public void addDocumentClass(String description)
    {
        this.docClassCollection.addNewDocumentClass(description);
    }
    
    public DocumentClass getDocumenClass(int id)
    {
        return this.docClassCollection.getDocumentClass(id);
    }
    
    public void addNewDocument(File in, float version, String name, DocumentClass docClass)
    {
        Document d = new Document(name, this.readFileContent(in), docClass, version);
        this.documentCollection.add(d);
    }
    
    public ArrayList <Document> getDocumentCollection()
    {
        return this.documentCollection;
    }
    
    public int documentClassCollectionSize()
    {
        return this.docClassCollection.size();
    }
    
    public DocumentClassCollection getDocumentClassCollection()
    {
        return this.docClassCollection;
    }
    
    public LSAModule getLSAM() {
    	return this.lsaModule;
    }
    
    public String getProjectName()
    {
        return this.projectName;
    }
}
