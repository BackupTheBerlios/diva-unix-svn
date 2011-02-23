/*
 * LSAResults.java
 *
 * Created on 08 November 2005, 15:10
 *
 *
 */

package lsat.lsa;

import java.io.*;
import java.util.*;
import lsat.gui.ChunkAnalysisStruct;
import lsat.structures.*;
import java.text.*;
import no.uib.cipr.matrix.*;
import no.uib.cipr.matrix.io.*;
import no.uib.cipr.matrix.sparse.CompColMatrix;

/**
 * @version 1.0
 * @since 08 November 2005
 * @author Andrew Stone
 */
public class LSAResults implements Serializable
{
    private ArrayList<Term> termList;
    private Matrix f;
    private Matrix EtE;
    private DocumentBoundryList docBoundaryList;
    private DocumentClassCollection docClassCollection;
    
    public static final int WRTALLCHUNKS = -1;
            
    /**
     * Creates a new instance of LSAResults
     * @param termList 
     * @param f 
     * @param EtE 
     * @param docBoundaryList 
     */
    public LSAResults(ArrayList<Term> termList, Matrix f, Matrix EtE, 
                DocumentBoundryList docBoundaryList, DocumentClassCollection docClassCollection)
    {
        this.termList = termList;
        this.f = f;
        this.EtE = EtE;
        this.docBoundaryList = docBoundaryList;
        this.docClassCollection = docClassCollection;
    }
    
    public double getRowSumOfF(int row)
    {
        return this.f.rowSum(row);
    }
    
    public ArrayList<Integer> getNonZeroColumnsOfF(int row)
    {
        return this.f.getNonZeroColumns(row);
    }
    
    /**
     * 
     * @return 
     */
    public ArrayList<Term> getTermList()
    {
        return this.termList;
    }
    
    public long getNumberOfWordsInAllDocuments()
    {
        BreakIterator wordCounter = BreakIterator.getWordInstance();
        long wordCount = 0;
        
        for(DocumentBoundaryListPair p : docBoundaryList)
        {
            wordCounter.setText(p.getContent());
            while(wordCounter.next() != wordCounter.DONE)
            {
                wordCount++;
            }
        }
        
        return wordCount;
    }
    
    public String getChunkAsText(int chunk)
    {
        return this.docBoundaryList.getChunkText(chunk);
    }
    
    
    public void dumpChunksBelongingToDocumentsWithClass(int targetDocumentClass)
    {
        docBoundaryList.dumpChunksBelongingToDocumentsWithClass(targetDocumentClass);
    }
    
    
    
    public ArrayList<Integer> getContributors(int chunk, double threshold)
    {
        double[] values = getSimilarities(chunk);
        
        ArrayList<Integer> contributors = new ArrayList<Integer>();
        
        for(int i=0;i<values.length;i++)
        {
            if(values[i]>=threshold && i != chunk)
            {
                contributors.add(new Integer(i));
            }
        }

        return contributors;    
    }
    
    public ArrayList<ChunkAnalysisStruct> getContributors(int chunk, double threshold, int targetDocumentClass, boolean sorted)
    {
        double[] values = getSimilarities(chunk);
        
        ArrayList<ChunkAnalysisStruct> contributors = docBoundaryList.getAnalysisStructs(targetDocumentClass);
        ArrayList<ChunkAnalysisStruct> returnValue = new ArrayList<ChunkAnalysisStruct>();
        
        TreeMap<Double, ChunkAnalysisStruct> sortedMap = new TreeMap<Double, ChunkAnalysisStruct>();
        
        for(ChunkAnalysisStruct cas : contributors)
        {
            int index = cas.getChunkNo();

            if(values[index]>=threshold && index != chunk)
            {
                returnValue.add(cas);
                sortedMap.put(values[index], cas);                
            }
        }

        if(sorted)
        {
            returnValue = new ArrayList<ChunkAnalysisStruct>(sortedMap.values());
            Collections.reverse(returnValue);
        }
        
        return returnValue;
    }

    public ArrayList<ChunkAnalysisStruct> getContributorsFromAllClasses(int chunk, double threshold)
    {
        double[] values = getSimilarities(chunk);
        
        ArrayList<ChunkAnalysisStruct> contributors = docBoundaryList.getAnalysisStructs(DocumentBoundryList.ALLDOCUMENTS);
        ArrayList<ChunkAnalysisStruct> returnValue = new ArrayList<ChunkAnalysisStruct>();
        
        for(ChunkAnalysisStruct cas : contributors)
        {
            int index = cas.getChunkNo();

            if(values[index]>=threshold && index != chunk)
            {
                returnValue.add(cas);
            }
        }
        
        return returnValue;
    }
    
    public ArrayList<ChunkAnalysisStruct> getPoorlySourced(int sourceDocClass, int wrtDocClass, double threshold, int maxNumberOfRelatedChunks)
    {
        ArrayList<ChunkAnalysisStruct> returnValue = new ArrayList<ChunkAnalysisStruct>();
        ArrayList<ChunkAnalysisStruct> source = docBoundaryList.getAnalysisStructs(sourceDocClass);
        
        for(ChunkAnalysisStruct cas : source)
        {
            double[] similarities = getSimilarities(cas.getChunkNo());
            int count = 0;
            
            for(int i=0;i<similarities.length;i++)
            {
                int currentDocClass = docBoundaryList.getDocClassOfChunk(i);

                if( (currentDocClass == wrtDocClass || WRTALLCHUNKS == wrtDocClass) && 
                        currentDocClass != sourceDocClass && 
                        similarities[i] >= threshold)
                {
                        count++;
                }
            }
            
            if(count <= maxNumberOfRelatedChunks)
            {
                returnValue.add(cas);
            }
        }
        
        return returnValue;
        
    }
    
    
    public ArrayList<ChunkAnalysisStruct> getChunksBelongingToClass(int targetDocumentClass)
    {
        ArrayList<ChunkAnalysisStruct> returnValue = docBoundaryList.getAnalysisStructs(targetDocumentClass);
        return returnValue;
    }
    
    // private method used to return the similarities associated with a given chunk
    // essentially abstracts the L portion of the similarity matrix to a single 
    // vector 
    private double[] getSimilarities(int chunk)
    {
        double[] similarities = new double[EtE.numRows()];
        
        for(int i=0;i<similarities.length;i++)
        {
            similarities[i]=EtE.get(chunk,  i);
        }
        
        for(int i=chunk;i<similarities.length;i++)
        {
            similarities[i]=EtE.get(i,chunk);
        }
                
        return similarities;
    }
    
    private double[] getSimilaritiesFromImportedMatrix(int chunk, AbstractMatrix m)
    {
        // assuming square matrix here
        double[] similarities = new double[m.numRows()];
        
        for(int i=0;i<similarities.length;i++)
        {
            similarities[i]=m.get(chunk,  i);
        }
        
        for(int i=chunk;i<similarities.length;i++)
        {
            similarities[i]=m.get(i,chunk);
        }
                
        return similarities;
    }    
    
    
    /*public void printGraphingStats()
    {
        int[][] container = new int[4][0];
        int[] six = {6, 2,4,5,6,7,8,9,10,11,12,13,14,16,18,19,21,22,23,24,25,26,28,30,31,33,34,35,37,38,39,40,41,44,45,47,48,49,51,52,53,54,56,57,58};
        int[] seven = {7, 0,1,3,4,5,6,7,8,10,11,13,14,18,19,20,21,22,24,25,30,32,33,35,36,37,40,41,43,44,45,47,49,50,51,52,54,55};
        int[] eight = {8, 0,2,5,7,8,10,11,14,15,18,19,20,21,22,25,26,30,33,35,36,40,41,43,44,47,48,49,50,51,52,55};
        int[] nine = {9,2,3,5,6,8,12,20,21,23,26,27,28,31,35,41,51,56};
        
        container[0] = six;
        container[1] = seven;
        container[2] = eight;
        container[3] = nine;
                
        for(int i=0;i<container.length;i++)
        {
            int sourceChunk = container[i][0];
            int[] currentRow = container[i];
            
            System.out.println("sourceChunk = " + sourceChunk);
            
            for(double j=-1;j<=1;j+=0.01)
            {
                System.out.print(j + " ");
                ArrayList<Integer> results = this.getContributors(sourceChunk, j, 2);
                
                double count = 0;
                
                for(int k=1;k<currentRow.length;k++)
                {
                    int currentChunkFromManualList = currentRow[k];
                    for(int l = 0;l<results.size();l++)
                    {
                        int currentChunkFromLSA = results.get(l);
                        if((currentChunkFromManualList+25) == currentChunkFromLSA)
                        {
                            count++;
                        }
                    }
                 
                }

                double precision = count / results.size();
                double recall = count / (currentRow.length - 1);

                System.out.println(precision + " " +recall);
            }
                        
        }
    }*/

    public void printGraphingStatsFromReqSimilieData()
    {
        int[][] container = new int[4][0];
        
        // TODO : These offsets need updating
        int[] six = {66, 2,4,5,6,7,8,9,10,11,12,13,14,16,18,19,21,22,23,24,25,26,28,30,31,33,34,35,37,38,39,40,41,44,45,47,48,49,51,52,53,54,56,57,58};
        int[] seven = {67, 0,1,3,4,5,6,7,8,10,11,13,14,18,19,20,21,22,24,25,30,32,33,35,36,37,40,41,43,44,45,47,49,50,51,52,54,55};
        int[] eight = {68, 0,2,5,7,8,10,11,14,15,18,19,20,21,22,25,26,30,33,35,36,40,41,43,44,47,48,49,50,51,52,55};
        int[] nine = {69,2,3,5,6,8,12,20,21,23,26,27,28,31,35,41,51,56};
        
        container[0] = six;
        container[1] = seven;
        container[2] = eight;
        container[3] = nine;
        
        //load the matrix
        CompColMatrix input = null;
        try
        {
            MatrixVectorReader r = new MatrixVectorReader(new FileReader(new File("/home/stone/research/trunk/results/bentley vs ethno/firstset/RScomparison/BentleyRSmatrix.mat")));
            input = new CompColMatrix(r);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        
        if(input != null)
        {
            //lsat.lsa.Matrix.printMatrix(input);            
            for(int i=0;i<container.length;i++)
            {
                int sourceChunk = container[i][0];
                int[] currentRow = container[i];
                
                System.out.println("sourceChunk = " + sourceChunk);
                
                // j is threshold
                for(double threshold=-1;threshold<=1;threshold+=0.01)
                {
                    System.out.print(threshold + "\t\t\t\t");
    
                    double[] values = getSimilaritiesFromImportedMatrix(sourceChunk, input);
                    ArrayList<Integer> matching = new ArrayList<Integer>();
    
                    // specification chunks begin at 59, therefore only consider
                    // the first 58 chunks
                    for(int k=0;k<59;k++)
                    {
                        if(values[k]>=threshold && k != sourceChunk)
                        {
                            matching.add(k);
                        }
                    }
                    
                    double count = 0;
                    
                    for(int k=1;k<currentRow.length;k++)
                    {
                        int currentChunkFromManualList = currentRow[k];
                        for(int l = 0;l<matching.size();l++)
                        {
                            int currentChunkFromLSA = matching.get(l);
                            //System.out.println("comparing " + (currentChunkFromManualList) + " and " + currentChunkFromLSA);
                            if((currentChunkFromManualList) == currentChunkFromLSA)
                            {
                                count++;
                            }
                        }
                     
                    }
    
                    double precision = count / matching.size();
                    double recall = count / (currentRow.length - 1);
    
                    System.out.println(precision + "\t\t\t\t" +recall);
                }
                            
        }

            
            
        }
        
                
    }
    
}
