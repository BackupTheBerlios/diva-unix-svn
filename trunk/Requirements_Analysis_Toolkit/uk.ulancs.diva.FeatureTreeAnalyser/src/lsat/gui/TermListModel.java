/*
 * TermListModel.java
 *
 * Created on 24 May 2006, 14:00
 *
 *
 */

package lsat.gui;

import javax.swing.*;
import lsat.lsa.*;
import java.util.*;
import java.io.*;
import lsat.log.*;


/**
 * @version 1.0
 * @since 24 May 2006
 * @author Andrew Stone
 */
public class TermListModel extends DefaultListModel
{
    private ArrayList<Term> termList;
    private LSAResults theResults;
    private Log log = LogFactory.getLog();
    private HashMap<String, Long> frequencyList;
    
    public TermListModel(LSAResults theResults)
    {
        this.termList = theResults.getTermList();
        this.theResults = theResults;
        
        sortByFrequency();
        loadFrequencyList();
    }
    
    public void sortByFrequency()
    {
        this.clear();
        
        // elements are already in frequency order
        for(Term t : termList)
        {
            addElement(t);
        }
    }
    
    private void loadFrequencyList()
    {
        frequencyList = new HashMap<String, Long>();
        File freqList= null;
        BufferedReader br = null;
        
        try
        {
            // TODO : This method works in both source file and JAR version for loading from text
            // TODO : Make sure other loading methods use this
            InputStreamReader isr = new InputStreamReader(getClass().getResourceAsStream("BncSampWr.wrd.fql"));
            br = new BufferedReader(isr);
        }
        catch(Exception e)
        {
            log.log(Log.ERROR, "Unable to find BncSampWr.wrd.fql");
            System.out.println(e.getMessage());
            return;
        }   
        
        if(br!=null)
        {
            String readContent = null;
            StringTokenizer tokenizer = null;
            do
            {
                try
                {
                    readContent = br.readLine();
                    
                    if(readContent != null)
                    {
                        tokenizer = new StringTokenizer(readContent);
                        String word = tokenizer.nextToken();
                        String count = tokenizer.nextToken();
                        frequencyList.put(word, new Long(count));
                    }
                }
                catch(IOException e)
                {
                    // TODO : add log line
                }
            }while(readContent != null);
            try
            {
                br.close();
            }
            catch(IOException e)
            {
                // TODO : Add log line
            }
        }
    }
    
    public void sortByLL()
    {
        // c is the number of words in the corpus (corpus one)
        long c= frequencyList.get("TOTAL");
        
        // d is the number of words in the documents (corpus two)
        long d = theResults.getNumberOfWordsInAllDocuments();
        
        // list to hold log likelihood values
        ArrayList<lsat.lsa.RowValuePair> ll = new ArrayList<lsat.lsa.RowValuePair>();
        
        long a, b;
        double e1, e2, term1, term2, g2, accumulated;
        for(Term t : termList)
        {
            if(frequencyList.get(t.getTerm()) == null)
            {
                ll.add(new RowValuePair(t.getRow(), Double.MAX_VALUE));
            }
            else
            {
                a = frequencyList.get(t.getTerm());                            
                //b = (long)theResults.getF().rowSum(t.getRow());
                b = (long)theResults.getRowSumOfF(t.getRow());
                

                e1 = c * (a+b)/(c+d);
                e2 = d * (a+b)/(c+d);

                term1 = (a==0) || (e1==0) ? 0 : (a*Math.log(a/e1));
                term2 = (b==0) || (e2==0) ? 0 : (b*Math.log(b/e2));

                g2 = 2 * (term1 + term2);
                ll.add(new RowValuePair(t.getRow(), g2));
            }
        }
        
        // sort the list
        Collections.sort(ll, new RowValuePairComparator());
        Collections.reverse(ll);
        
        this.clear();
        
        for(RowValuePair p : ll)
        {
            Term t = termList.get(p.getRow());
            if(p.getValue() == Double.MAX_VALUE)
            {
                t.setInCorpus(false);
            }
            addElement(t);
        }     
    }
    
    public void highlightChunks(Term t, ChunkViewTableModel m)
    {
        //un-highlight all the old highlighted cells
        m.unHighlightAll();
        
        int row = t.getRow();
        
        ArrayList<Integer> toHighlight = theResults.getNonZeroColumnsOfF(row);
        
        for(Integer i : toHighlight)
        {
            m.highlightRow(i);    
        }
        
        m.fireTableDataChanged();
    }
    
}
