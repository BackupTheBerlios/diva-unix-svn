/*
 * Copyright (C) 2003-2005 Bjorn-Ove Heimsund
 * 
 * This file is part of MTJ.
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package no.uib.cipr.matrix;

import no.uib.cipr.matrix.LAPACKkernel.JobSVD;

/**
 * Computes singular value decompositions
 */
public class SVD {

    /**
     * Work array
     */
    private final double[] work;

    /**
     * Work array
     */
    private final int[] iwork;

    /**
     * Matrix dimension
     */
    private final int m, n;

    /**
     * Compute the singular vectors fully?
     */
    private final boolean vectors;

    /**
     * Job to do
     */
    private final JobSVD job;

    /**
     * The singular values
     */
    private final double[] S;

    /**
     * Singular vectors
     */
    private final DenseMatrix U, Vt;

    /**
     * Creates an empty SVD which will compute all singular values and vectors
     * 
     * @param m
     *            Number of rows
     * @param n
     *            Number of columns
     */
    public SVD(int m, int n) {
        this(m, n, true);
    }

    /**
     * Creates an empty SVD
     * 
     * @param m
     *            Number of rows
     * @param n
     *            Number of columns
     * @param vectors
     *            True to compute the singular vectors, false for just the
     *            singular values
     */
    public SVD(int m, int n, boolean vectors) {
        this.m = m;
        this.n = n;
        this.vectors = vectors;

        // Allocate space for the decomposition
        S = new double[Math.min(m, n)];
        if (vectors) {
            U = new DenseMatrix(m, m);
            Vt = new DenseMatrix(n, n);
        } else
            U = Vt = null;

        job = vectors ? JobSVD.All : JobSVD.None;

        // Find workspace requirements
        iwork = new int[8 * Math.min(m, n)];

        // Query optimal workspace
        double[] worksize = new double[1];
        int info = Interface.lapack().gesdd(job, m, n, new double[0],
                new double[0], new double[0], new double[0], worksize, -1,
                iwork);
//        int info = 0;

        // Allocate workspace
        int lwork = -1;
        if (info != 0) {
            if (vectors)
                lwork = 3
                        * Math.min(m, n)
                        * Math.min(m, n)
                        + Math.max(Math.max(m, n), 4 * Math.min(m, n)
                                * Math.min(m, n) + 4 * Math.min(m, n));
            else
                lwork = 3
                        * Math.min(m, n)
                        * Math.min(m, n)
                        + Math.max(Math.max(m, n), 5 * Math.min(m, n)
                                * Math.min(m, n) + 4 * Math.min(m, n));
        } else
            lwork = (int) worksize[0];

        lwork = Math.max(lwork, 1);
        work = new double[lwork];
    }

    /**
     * Convenience method for computing a full SVD
     * 
     * @param A
     *            Matrix to decompose, not modified
     * @return Newly allocated factorization
     * @throws NotConvergedException
     */
    public static SVD factorize(Matrix A) throws NotConvergedException {
        return new SVD(A.numRows(), A.numColumns()).factor(new DenseMatrix(A));
    }

    /**
     * Computes an SVD
     * 
     * @param A
     *            Matrix to decompose. Size must conform, and it will be
     *            overwritten on return. Pass a copy to avoid this
     * @return The current decomposition
     * @throws NotConvergedException
     */
    public SVD factor(DenseMatrix A) throws NotConvergedException {
        if (A.numRows() != m)
            throw new IllegalArgumentException("A.numRows() != m");
        else if (A.numColumns() != n)
            throw new IllegalArgumentException("A.numColumns() != n");

        int info = Interface.lapack().gesdd(job, m, n, A.getData(), S,
                vectors ? U.getData() : new double[0],
                vectors ? Vt.getData() : new double[0], work, work.length,
                iwork);

        if (info > 0)
            throw new NotConvergedException(
                    NotConvergedException.Reason.Iterations);
        else if (info < 0)
            throw new IllegalArgumentException();

        return this;
    }

    /**
     * True if singular vectors are stored
     */
    public boolean hasSingularVectors() {
        return U != null;
    }

    /**
     * Returns the left singular vectors, column-wise. Not available for partial
     * decompositions
     * 
     * @return Matrix of size m*m
     */
    public DenseMatrix getU() {
        return U;
    }

    /**
     * Returns the right singular vectors, row-wise. Not available for partial
     * decompositions
     * 
     * @return Matrix of size n*n
     */
    public DenseMatrix getVt() {
        return Vt;
    }

    /**
     * Returns the singular values
     * 
     * @return Array of size min(m,n)
     */
    public double[] getS() {
        return S;
    }

}
