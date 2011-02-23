package lsat.lsa;


import lsat.gui.DocumentFrameTableModel;
import lsat.structures.*; 
import no.uib.cipr.matrix.*;
import java.util.*;
import java.text.*;
import java.io.*;
import lsat.log.*;
import no.uib.cipr.matrix.nni.LAPACK;
import java.util.regex.*;


/**
 * This class performs Latent Semantic Analysis on an ArrayList of Documents. The 
 * terms found, sentence boundaries detected, freuency matrix and the document 
 * similarity matrix are returned in an LSAResults object.
 */
public class LSAModule implements Serializable
{
    private ArrayList<Term> termList;
    private Matrix f, EtE;
    private DocumentBoundryList docBoundaryList;
    private Log log = LogFactory.getLog();
    private LSATPreferences prefs = LSATPreferencesFactory.getPrefs(false);
    private TermFinder tf;
    
    
    public static final int LINEWISE = -1;
    public static final int PARAWISE = -2;
    public static final int CUSTOMREGEX = -3;
        
    // add ability to do paragraph wise
    private void computeSentenceBoundaries(DocumentBoundaryListPair dbpl, int boundarysize, String regex)
    {
    	
    	
        if(boundarysize > 0) // do it in (sentence * boundarysize) chunks
        {
            log.log(Log.INFO, boundarysize + " boundaries per chunk");
            BreakIterator bi = BreakIterator.getSentenceInstance();
            bi.setText(dbpl.getContent());

            int start = bi.first();
            for (int end = bi.next(boundarysize); end != BreakIterator.DONE; start = end, end = bi.next(boundarysize)) 
            {
                dbpl.addBoundary(new Chunk(start, end));
            }
        }
        // TODO : The linewise stuff just doesn't work, however the custom regex
        // block with \n\n does, so consider making linewise just give the custom
        // regex block the string "\n\n"
        else // else do it linewise
        {
            Pattern p = null;
            Pattern wordCharacterMatcher = Pattern.compile("\\w+");
            if(LINEWISE == boundarysize)
            {
                log.log(Log.INFO, "One line per chunk");
                p = Pattern.compile("\n");
            }
            else if(PARAWISE == boundarysize)
            {
                String results[] = dbpl.getContent().split("\n\n");
                
                int start = 0;
                int end = 0;
                
                for(String s : results)
                {
                    end+=s.length();
                    end--;
                    if(start < end)
                    {
                        dbpl.addBoundary(new Chunk(start, end));
                    }
                    start = end+2;
                }
            }
            else if(CUSTOMREGEX == boundarysize && regex != null)
            {
                log.log(Log.INFO, "Custom regex : " + regex);
                // by default we enable multiline mode - as a hangover from my
                // inability to use regexes with anything other than grep
                p = Pattern.compile(regex, Pattern.MULTILINE);
                Matcher m = p.matcher(dbpl.getContent());

                int start = 0;
                int end = 0;

                while(m.find())
                {
                    end = m.end();
                    String substring = dbpl.getContent().substring(start, end);
                    Matcher nonWhiteMatcher = wordCharacterMatcher.matcher(substring);
                    if(start < end && nonWhiteMatcher.find())
                    {
                        dbpl.addBoundary(new Chunk(start, end));
                    }
                    start = end;
                }
            }
            else
            {
                // TODO : report an error here
            }
        }
    }
    
    private void findTerms(ArrayList<DocumentBoundaryListPair> docBoundaryList)
    {
        termList = new ArrayList<Term>();
   
        //int count = 0;
        for(int i=0;i<docBoundaryList.size();i++)
        {
            tf.reset(docBoundaryList.get(i).getContent());
            TermAlphabeticComparator alphaComparator = new TermAlphabeticComparator();
            Term searchElement = new Term();

            String term = null;
            
            do
            {        
                try
                {
                    // get next term
                    term = tf.nextTerm();
                }
                catch(IOException e)
                {
                    String error = "IOException thrown while reading in memory content String \n" +
                                   "This should never be thrown - the content is being read from an in-memory StringBuffer \n" +
                                   "If this is thrown you may have faulty RAM or SMP problems";
                    log.log(Log.ERROR, error);
                }


                if(term != null)
                {
                    searchElement.setTerm(term);
                    int result = Collections.binarySearch(termList, searchElement, 
                                                                alphaComparator);
                    if(result < 0)
                    {
                        termList.add((-result)-1, new Term(term));
                    }
                }
        }while(term != null);
    }
}
            
    private void populateFmatrix(ArrayList<DocumentBoundaryListPair> docBoundaryList)
    {
        // compute number of columns by iterating through all docBoundaryListpairs
        // and adding up the number of boundaries that each one contains
        int numberOfColumns = 0;
        
        
        for(int i=0;i<docBoundaryList.size();i++)
        {
            numberOfColumns += docBoundaryList.get(i).getNumberOfBoundaries();
        }
        
        
        // create the f matrix
        f = new lsat.lsa.Matrix(termList.size(), numberOfColumns);
        
        log.log(Log.INFO, "Matrix is " + f.numRows() + " by " + f.numColumns());
        
        TermAlphabeticComparator alphaComparator = new TermAlphabeticComparator();
        Term searchElement = new Term();

        // iterate through documents
        for(int i=0;i<docBoundaryList.size();i++)
        {
            DocumentBoundaryListPair currentDoc = docBoundaryList.get(i);
            ArrayList<Chunk> boundaries = currentDoc.getBoundaries();
            log.log(Log.INFO, "Working on document " + currentDoc.getFileName());
            

            // iterate through boundaries (i.e. columns)
            for(int j=0;j<boundaries.size();j++)
            {
                Chunk currentBoundary = boundaries.get(j);
                log.log(Log.INFO, "Working on boundary " + j);
                String token = null;
                assert currentBoundary.getStart() != -1;
                assert currentBoundary.getFinish() != -1;
                
                String boundaryText = currentDoc.getContent().substring(currentBoundary.getStart(), 
                                                                        currentBoundary.getFinish());
                this.tf.reset(boundaryText);

                do
                {
                    try
                    {
                        // get next term
                        token = tf.nextTerm();
                   }
                    catch(IOException e)
                    {
                        String logMessage = "IOException thrown while reading in memory content String" + 
                                "This should never be thrown - the content is being read from an in-memory StringBuffer" + 
                                "If this is thrown you may have faulty RAM or SMP problems";
                        log.log(Log.ERROR, logMessage);
                    }
                    
                    if(token != null)
                    {
                        searchElement.setTerm(token);
                        int result = Collections.binarySearch(termList, 
                                                searchElement, alphaComparator);
                        if(result >= 0)
                        {
                            int row = termList.get(result).getRow();
                            assert currentBoundary.getColumn() < f.numColumns() : "index asked for = " + row + "," + currentBoundary.getColumn();
                            f.increment(row, currentBoundary.getColumn());
                        }
                    }
                } while (token != null);
            }
        }
            
        // unsure why this is necessary
        Collections.sort(termList, new TermRowComparator());
    }
    // investigate if this is broken
    private void reorderFmatrix()
    {
        ArrayList<RowValuePair> sumsList = new ArrayList<RowValuePair>();
        
        // loop to sum each row and stick the values in an arraylist
        for(int i=0;i<f.numRows();i++)
        {
            RowValuePair rvp = new RowValuePair(i,  f.rowSum(i));
            sumsList.add(rvp);
        }
        
        // sort the row value pairs by value        
        Collections.sort(sumsList, new RowValuePairComparator());
        
        // we need them descending, so invert the list
        Collections.reverse(sumsList);
        
        //build a new matrix to hold the ordered data
        Matrix newFMatrix = new Matrix(f.numRows(), f.numColumns());
        
        for(int i=0;i<sumsList.size();i++)
        {
            int oldRow = sumsList.get(i).getRow();
            newFMatrix.setRow(i, f.getRow(oldRow));
            assert oldRow < f.numRows() : "row asked for is " + oldRow ;
            termList.get(oldRow).setRow(i);
        }
        
        // reorder termlist
        Collections.sort(termList, new TermRowComparator());
        
        // set the new matrix as the new f matrix
        f = newFMatrix;
    }
    
    private void applyLocalWeight()
    {
        // currently blankwise
        // TODO : generate an algorithm here
        log.log(Log.WARNING, "No local weighting algorithm specified");
    }
    
    private void applyGlobalWeight()
    {
        // adds one to every value in the f matrix
        // as we're going to be taking log 10 of these values
        // adding one prevents NaNs appearing in the matrix
        for(int i=0;i<f.numRows();i++)
        {
            for(int j=0;j<f.numColumns();j++)
            {
                double value = f.get(i,j);
                value++;
                f.set(i,j, Math.log10(value));
            }
        }
    }
    
    private void computeDocumentSimilarityMatrix(int reducedRows)
    {
        // Something to hold the results of the SVD in
        SVD results = null;
        
        // this gets really heavy, do a gc() to free up RAM before we start
        System.gc();
                                            
        /*
         * finally, after all this work, compute the bastard SVD
         * quite possibly the most expensive part of the  process
         * and yet, the creators of the maths library insist that it 
         * runs inside a try{...} block
         *
         * clever eh?
         */
        log.log(Log.INFO, "Computing SVD");
        log.log(Log.INFO, "Matrix is " + f.numRows() + " by " + f.numColumns());
        
        try
        {
            //LAPACK.gesdd();
            results = SVD.factorize(f);
        }
        catch(NotConvergedException e)
        { 
            log.log(Log.ERROR, "Matrix does not converge - aborting");
        }
        log.log(Log.INFO, "Finished computing SVD");
        
        log.log(Log.INFO, "Reconstructing sigma matrix");
        // reconstruct sigma matrix from singular row vector
        no.uib.cipr.matrix.Matrix Sigma = new no.uib.cipr.matrix.DenseMatrix(f.numRows(), f.numColumns());
        for(int i=0;i<results.getS().length;i++)
        {
            Sigma.set(i,i, results.getS()[i]);
        }

        log.log(Log.INFO, "Multiplying Sigma by Vt");
        // multiply sigma by Vt to get the column space matrix A
        //no.uib.cipr.matrix.Matrix A = new DenseMatrix(Sigma.numRows(), f.numColumns());
        no.uib.cipr.matrix.Matrix A = new no.uib.cipr.matrix.DenseMatrix(Sigma.numRows(), f.numColumns());
        Sigma.mult(results.getVt(), A);
        
        
        log.log(Log.INFO, "Creating B matrix");
        
        if (reducedRows > f.numColumns()) {
        	reducedRows = f.numColumns();
        }
        
        // lose the extraneous rows of A to create the matrix B
        Matrix B = new Matrix(reducedRows, f.numColumns());
        for(int i=0;i<B.numRows();i++)
        {
        	
            for(int j=0;j<B.numColumns();j++)
            {
                B.set(i,j, A.get(i,j));
            }
        }
        
        log.log(Log.INFO, "Column length normalising matrix B to create matrix E");
        // the matrix E is the matrix B with length normalised columns
        //no.uib.cipr.matrix.Matrix E = computeColumnLengthNormalisedMatrix(B);
        B.columnLengthNormaliseMatrix();
        
        // we need to compute EtE, so create a matrix to hold Et
        // and set it to be the transpose of E
        log.log(Log.INFO, "Creating transpose of E");
        Matrix Et = new Matrix(B.numColumns(), B.numRows());
        B.transpose(Et);
        
        log.log(Log.INFO, "Computing EtE");
        // hooray this is it! We multiply Et by E
        // and set this.EtE to the result
        this.EtE = new Matrix(Et.numRows(), B.numColumns());
        Et.mult(B,  EtE);
        
        // EtE is symmetric about the diagonal
        // that makes it look rather confusing, so we're going to set the upper
        // region equal to 0
        // this makes it easier to read
        log.log(Log.INFO, "Erasing U portion of EtE");        
        for(int i=0;i<EtE.numColumns();i++)
        {
            for(int j=0;j<i;j++)
            {
                EtE.set(j,i, 0);
            }
        }
        
        for(MatrixEntry me : EtE)
        {
//            System.out.println(me.get());
        }
    }
    
    // null some variables
    private void clear()
    {
        System.gc();
        termList = null;
        f = null;
        Term.nextRow = 0;
        Chunk.nextColumn = 0;
        docBoundaryList = null;
    }
    
    /**
     * Convenience method to produce the document similarity matrix, the ultimate goal of LSA
     * @param reducedRows The number of dimensions that the original matrix should be collapsed into
     * @param documentCollection A collection of documents in an ArrayList on which to perform the LSA operations
     * @param boundarySize The size of a boundary in sentences i.e. approx 10 sentence would compute boundaries that are paragraph size
     * @return The document similiarity matrix (EtE), a square lower matrix with identity values along the diagonal
     */
    public LSAResults performLSA(ArrayList<lsat.structures.Document> documentCollection, DocumentClassCollection docClassCollection, String regex, int boundarySize, int reducedRows, String workspacePath)
    {
//        int boundarySize = -1, reducedRows = 0;
//        try
//        {
//            boundarySize = Integer.valueOf(this.prefs.get("lsa-boundarysize"));
//            reducedRows = Integer.valueOf(this.prefs.get("lsa-reducedrows"));
//        }
//        catch(NumberFormatException e)
//        {
//            log.log(Log.ERROR, "Unable to retrieve preferences object");
//        }
        
        // clear the old variables from the last computation
        log.log(Log.INFO, "Clearing");
        this.clear();
        
        // create the document and bounary list pair container
        this.docBoundaryList = new DocumentBoundryList();
        
        // create the termfinder
        File stopwordlist = null;
        File verbstemlist = null;
        try
        {
        	
            stopwordlist = new File(workspacePath + "stopwordlist.txt");
            verbstemlist = new File(workspacePath + "verbstemlist.txt");
            
        }
        catch(Exception e)
        {
            log.log(Log.ERROR, "Unable to find stopwordlist.txt or verbstemlist.txt");
        }
        
        this.tf = new TermFinder("", verbstemlist.getAbsolutePath(), stopwordlist.getAbsolutePath());
        
        // compute sentence boundaries
        log.log(Log.INFO, "Computing Sentence Boundaries");
        for(int i=0;i<documentCollection.size();i++)
        {
            docBoundaryList.add(new DocumentBoundaryListPair(documentCollection.get(i)));
            this.computeSentenceBoundaries(docBoundaryList.get(i), boundarySize, regex);
        }
        
        log.log(Log.INFO, "Finding Terms");
        findTerms(this.docBoundaryList);

        log.log(Log.INFO, "Populating f Matrix");
        populateFmatrix(this.docBoundaryList);

        log.log(Log.INFO, "Reordering f Matrix");
        reorderFmatrix();

        log.log(Log.INFO, "Applying Local Weight");
        applyLocalWeight();

        log.log(Log.INFO, "Applying Global Weight");
        applyGlobalWeight();

        log.log(Log.INFO, "Computing document similarity matrix");
        computeDocumentSimilarityMatrix(reducedRows);
        
        // this is quite heavy stuff - request a GC to clear some memory
        System.gc();
        
        log.log(Log.INFO, "Constructing results object");
        LSAResults theResults = new LSAResults(termList, f, EtE, docBoundaryList, docClassCollection);
       
        log.log(Log.INFO, "Finished LSA");
        return theResults;
    }
   
    
    /**
     * Method to test computation of EtE. For debug use only.
     * @return The document similarity matrix for the test matrix
     */
    public Matrix testEtEComputation()
    {
       
        System.out.println("Clearing");
        this.clear();
        
        f = new Matrix(5,6);
        
        f.set(0,0,1);
        f.set(0,2,1);
        f.set(1,1,1);
        f.set(2,0,1);
        f.set(2,1,10);
        f.set(3,0,1);
        f.set(3,3,15);
        f.set(3,4,1);
        f.set(4,3,19);
        f.set(4,5,7);

        
        this.docBoundaryList = new DocumentBoundryList();
        System.out.println("Old F matrix");
        Matrix.printMatrix(f);
        this.reorderFmatrix();
        System.out.println("New F matrix");
        Matrix.printMatrix(f);
        
        System.out.println("Compute EtE");
        computeDocumentSimilarityMatrix(2);
        
        Matrix.printMatrix(f);
        
        return EtE;
    }
    
    /**
     * Test stub for the LSAModule class
     * @param args The command line arguments
     */
    public static void main(String args[])
    {
        LSAModule lsam = new LSAModule();
        Matrix.printMatrix(lsam.testEtEComputation());
    }
  
}
