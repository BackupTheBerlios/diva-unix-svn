/*
 * MatrixWrapper.java
 *
 * Created on April 5, 2005, 2:50 PM
 */

package lsat.lsa;

import java.io.Serializable;
import lsat.structures.*;
import java.util.*;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.sparse.CompColMatrix;
import no.uib.cipr.matrix.sparse.SparseVector;
import no.uib.cipr.matrix.DenseVector;

/**
 *
 * @author stone
 */

//public class Matrix extends mt.DenseMatrix implements mt.Matrix
public class Matrix extends no.uib.cipr.matrix.sparse.FlexCompRowMatrix implements no.uib.cipr.matrix.Matrix
{
    
    /**
     * Creates a new instance of Matrix
     * @param rows 
     * @param columns 
     */
    public Matrix(int rows, int columns)
    {
        super(rows, columns);
    }
    
    
    public void columnLengthNormaliseMatrix()
    {
        // right, now we need to add up all the columns to produce a row vector
        // here's the column vector to hold the result
        double sum = 0, value = 0;

        // loop through all elements in the matrix
        for(int i=0;i<numColumns();i++)
        {
            // new column therefore sum is 0
            sum = 0;
            
            // go to each row in the column, add the squre of the values to sum
            for(int j=0;j<numRows();j++)
            {
                value = this.get(j,i);
                value *= value;
                sum += value;
            }
            
            // root sum
            sum = Math.sqrt(sum);

            // divide each row in the column by sum
            for(int j=0;j<numRows();j++)
            {
                value = this.get(j,i);
                value /= sum;
                set(j,i,value);
            }
        }
    }
    
    public void swapRow(int a, int b)
    {
        if(a<this.numRows() && b<this.numRows())
        {
            no.uib.cipr.matrix.sparse.SparseVector temp = this.getRow(a);
            this.setRow(a, this.getRow(b));
            this.setRow(b, temp);
        }
        else
        {
            String error = "Requested swap indices are " + a + "," + b + ", size of matrix is " + this.numRows();
            ArrayIndexOutOfBoundsException e = new ArrayIndexOutOfBoundsException(error);
            throw e;
        }
        
    }
    
    public double rowSum(int row)
    {
        double result = 0;
        
        if(row < this.numRows())
        {
            double[] rowData = this.getRow(row).getData();
            
            for(int i=0;i<rowData.length;i++)
            {
                result+=rowData[i];
            }
        }
        else
        {
            String error = "Requested index is " + row + ", size of matrix is " + this.numRows();
            ArrayIndexOutOfBoundsException e = new ArrayIndexOutOfBoundsException(error);
            throw e;
        }
        
        return result;
    }
    
    public void increment(int row, int column)
    {
        double currentValue = this.get(row, column);
        currentValue++;
        this.set(row, column, currentValue);
    }
    
    public ArrayList<Integer> getNonZeroColumns(int row)
    {
        ArrayList<Integer> results = new ArrayList<Integer>();
        for(int i=0;i<this.numColumns();i++)
        {
            if(get(row, i) != 0)
            {
                results.add(i);
            }
        }
        return results;
    }
    
    /**
     *  Prints the matrix t to the console
     * @param t The matrix to be printed
     */
    public static void printMatrix(no.uib.cipr.matrix.Matrix t)
    {
        for(int i=0;i<t.numRows();i++)
        {
            for(int j=0;j<t.numColumns();j++)
            {
                System.out.print(t.get(i,j) + "\t\t");
            }
            System.out.println("");
        }
    }    
    
    
    public static void main(String args[])
    {
        Matrix f ;
        f = new Matrix(5,6);
        
        f.set(0,0,10);
        f.set(0,2,1);
        f.set(1,1,20);
        f.set(2,0,1);
        f.set(2,1,1);
        f.set(3,0,1);
        f.set(3,3,1);
        f.set(3,4,1);
        f.set(4,3,1);
        f.set(4,5,1);
        
        System.out.println("Pre swap");
        Matrix.printMatrix(f);
        
        f.swapRow(0,1);
        
        System.out.println("Post swap");
        Matrix.printMatrix(f);
        
        System.out.println(f.rowSum(1));
    }
}
