/*
 * Copyright (C) 2003-2005 Bjorn-Ove Heimsund * 
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

import no.uib.cipr.matrix.BLASkernel.Diag;
import no.uib.cipr.matrix.BLASkernel.Side;
import no.uib.cipr.matrix.BLASkernel.Transpose;
import no.uib.cipr.matrix.BLASkernel.UpLo;
import no.uib.cipr.matrix.Matrix.Norm;

import org.netlib.lapack.Dgbcon;
import org.netlib.lapack.Dgbsv;
import org.netlib.lapack.Dgbtrf;
import org.netlib.lapack.Dgbtrs;
import org.netlib.lapack.Dgecon;
import org.netlib.lapack.Dgeev;
import org.netlib.lapack.Dgelqf;
import org.netlib.lapack.Dgels;
import org.netlib.lapack.Dgelsd;
import org.netlib.lapack.Dgelss;
import org.netlib.lapack.Dgeqlf;
import org.netlib.lapack.Dgeqrf;
import org.netlib.lapack.Dgerqf;
import org.netlib.lapack.Dgesdd;
import org.netlib.lapack.Dgesv;
import org.netlib.lapack.Dgesvd;
import org.netlib.lapack.Dgetrf;
import org.netlib.lapack.Dgetrs;
import org.netlib.lapack.Dgtcon;
import org.netlib.lapack.Dgtsv;
import org.netlib.lapack.Dgttrf;
import org.netlib.lapack.Dgttrs;
import org.netlib.lapack.Dlamch;
import org.netlib.lapack.Dorglq;
import org.netlib.lapack.Dorgql;
import org.netlib.lapack.Dorgqr;
import org.netlib.lapack.Dorgrq;
import org.netlib.lapack.Dormlq;
import org.netlib.lapack.Dormql;
import org.netlib.lapack.Dormqr;
import org.netlib.lapack.Dormrq;
import org.netlib.lapack.Dpbcon;
import org.netlib.lapack.Dpbsv;
import org.netlib.lapack.Dpbtrf;
import org.netlib.lapack.Dpbtrs;
import org.netlib.lapack.Dpocon;
import org.netlib.lapack.Dposv;
import org.netlib.lapack.Dpotrf;
import org.netlib.lapack.Dpotrs;
import org.netlib.lapack.Dppcon;
import org.netlib.lapack.Dppsv;
import org.netlib.lapack.Dpptrf;
import org.netlib.lapack.Dpptrs;
import org.netlib.lapack.Dptsv;
import org.netlib.lapack.Dsbev;
import org.netlib.lapack.Dsbevd;
import org.netlib.lapack.Dspev;
import org.netlib.lapack.Dspevd;
import org.netlib.lapack.Dspsv;
import org.netlib.lapack.Dstev;
import org.netlib.lapack.Dstevr;
import org.netlib.lapack.Dsyev;
import org.netlib.lapack.Dsyevr;
import org.netlib.lapack.Dsysv;
import org.netlib.lapack.Dtbtrs;
import org.netlib.lapack.Dtptrs;
import org.netlib.lapack.Dtrtrs;
import org.netlib.lapack.Ilaenv;
import org.netlib.util.doubleW;
import org.netlib.util.intW;

/**
 * LAPACK kernel with a JLAPACK back-end
 */
class JLAPACK_LAPACKkernel implements LAPACKkernel {

    public double lamch(String cmach) {
        return Dlamch.dlamch(cmach);
    }

    public int laenv(int ispec, String name, String opts, int n1, int n2,
            int n3, int n4) {
        return Ilaenv.ilaenv(ispec, name, opts, n1, n2, n3, n4);
    }

    public int gesvd(JobSVD jobu, JobSVD jobvt, int m, int n, double[] A,
            double[] s, double[] U, double[] Vt, double[] work, int lwork) {
        intW info = new intW(0);
        Dgesvd.dgesvd(jobSVD(jobu), jobSVD(jobvt), m, n, A, 0, ld(m), s, 0, U,
                0, ld(m), Vt, 0, ld(n), work, 0, lwork, info);
        return info.val;
    }

    public int gesdd(JobSVD jobz, int m, int n, double[] A, double[] s,
            double[] U, double[] Vt, double[] work, int lwork, int[] iwork) {
        intW info = new intW(0);
        Dgesdd.dgesdd(jobSVD(jobz), m, n, A, 0, ld(m), s, 0, U, 0, ld(m), Vt,
                0, ld(n), work, 0, lwork, iwork, 0, info);
        return info.val;
    }

    public int gelss(int m, int n, int nrhs, double[] A, double[] B,
            double[] s, double rcond, int[] rank, double[] work, int lwork) {
        intW info = new intW(0);
        intW rankW = new intW(rank[0]);
        Dgelss.dgelss(m, n, nrhs, A, 0, ld(m), B, 0, ld(m, n), s, 0, rcond,
                rankW, work, 0, lwork, info);
        rank[0] = rankW.val;
        return info.val;
    }

    public int gelsd(int m, int n, int nrhs, double[] A, double[] B,
            double[] s, double rcond, int[] rank, double[] work, int lwork,
            int[] iwork) {
        intW info = new intW(0);
        doubleW rcondW = new doubleW(rcond);
        intW rankW = new intW(rank[0]);
        Dgelsd.dgelsd(m, n, nrhs, A, 0, ld(m), B, 0, ld(m, n), s, 0, rcondW,
                rankW, work, 0, lwork, iwork, 0, info);
        rank[0] = rankW.val;
        return info.val;
    }

    public int geev(JobEig jobvl, JobEig jobvr, int n, double[] A, double[] wr,
            double[] wi, double[] Vl, double[] Vr, double[] work, int lwork) {
        intW info = new intW(0);
        Dgeev.dgeev(jobEig(jobvl), jobEig(jobvr), n, A, 0, ld(n), wr, 0, wi, 0,
                Vl, 0, ld(n), Vr, 0, ld(n), work, 0, lwork, info);
        return info.val;
    }

    public int syev(JobEig jobz, UpLo uplo, int n, double[] A, double[] w,
            double[] work, int lwork) {
        intW info = new intW(0);
        Dsyev.dsyev(jobEig(jobz), uplo(uplo), n, A, 0, ld(n), w, 0, work, 0,
                lwork, info);
        return info.val;
    }

    public int spev(JobEig jobz, UpLo uplo, int n, double[] Ap, double[] w,
            double[] Z, double[] work) {
        intW info = new intW(0);
        Dspev.dspev(jobEig(jobz), uplo(uplo), n, Ap, 0, w, 0, Z, 0, ld(n),
                work, 0, info);
        return info.val;
    }

    public int sbev(JobEig jobz, UpLo uplo, int n, int kd, double[] Ab,
            double[] w, double[] Z, double[] work) {
        intW info = new intW(0);
        Dsbev.dsbev(jobEig(jobz), uplo(uplo), n, kd, Ab, 0, ld(kd + 1), w, 0,
                Z, 0, ld(n), work, 0, info);
        return info.val;
    }

    public int stev(JobEig jobz, int n, double[] d, double[] e, double[] Z,
            double[] work) {
        intW info = new intW(0);
        Dstev.dstev(jobEig(jobz), n, d, 0, e, 0, Z, 0, ld(n), work, 0, info);
        return info.val;
    }

    public int gels(Transpose trans, int m, int n, int nrhs, double[] A,
            double[] B, double[] work, int lwork) {
        intW info = new intW(0);
        Dgels.dgels(trans(trans), m, n, nrhs, A, 0, ld(m), B, 0, ld(m, n),
                work, 0, lwork, info);
        return info.val;
    }

    public int gesv(int n, int nrhs, double[] A, int[] ipiv, double[] B) {
        intW info = new intW(0);
        Dgesv.dgesv(n, nrhs, A, 0, ld(n), ipiv, 0, B, 0, ld(n), info);
        return info.val;
    }

    public int gbsv(int n, int kl, int ku, int nrhs, double[] Ab, int[] ipiv,
            double[] B) {
        intW info = new intW(0);
        Dgbsv.dgbsv(n, kl, ku, nrhs, Ab, 0, ld(2 * kl + ku + 1), ipiv, 0, B, 0,
                ld(n), info);
        return info.val;
    }

    public int gtsv(int n, int nrhs, double[] dl, double[] d, double[] du,
            double[] B) {
        intW info = new intW(0);
        Dgtsv.dgtsv(n, nrhs, dl, 0, d, 0, du, 0, B, 0, ld(n), info);
        return info.val;
    }

    public int posv(UpLo uplo, int n, int nrhs, double[] A, double[] B) {
        intW info = new intW(0);
        Dposv.dposv(uplo(uplo), n, nrhs, A, 0, ld(n), B, 0, ld(n), info);
        return info.val;
    }

    public int ppsv(UpLo uplo, int n, int nrhs, double[] Ap, double[] B) {
        intW info = new intW(0);
        Dppsv.dppsv(uplo(uplo), n, nrhs, Ap, 0, B, 0, ld(n), info);
        return info.val;
    }

    public int pptrf(UpLo uplo, int n, double[] Ap) {
        intW info = new intW(0);
        Dpptrf.dpptrf(uplo(uplo), n, Ap, 0, info);
        return info.val;
    }

    public int pptrs(UpLo uplo, int n, int nrhs, double[] Ap, double[] B) {
        intW info = new intW(0);
        Dpptrs.dpptrs(uplo(uplo), n, nrhs, Ap, 0, B, 0, ld(n), info);
        return info.val;
    }

    public int ppcon(UpLo uplo, int n, double[] Ap, double anorm,
            double[] rcond, double[] work, int[] iwork) {
        intW info = new intW(0);
        doubleW rcondW = new doubleW(rcond[0]);
        Dppcon.dppcon(uplo(uplo), n, Ap, 0, anorm, rcondW, work, 0, iwork, 0,
                info);
        rcond[0] = rcondW.val;
        return info.val;
    }

    public int pbsv(UpLo uplo, int n, int kd, int nrhs, double[] Ab, double[] B) {
        intW info = new intW(0);
        Dpbsv.dpbsv(uplo(uplo), n, kd, nrhs, Ab, 0, ld(kd + 1), B, 0, ld(n),
                info);
        return info.val;
    }

    public int pbtrf(UpLo uplo, int n, int kd, double[] Ab) {
        intW info = new intW(0);
        Dpbtrf.dpbtrf(uplo(uplo), n, kd, Ab, 0, ld(kd + 1), info);
        return info.val;
    }

    public int pbtrs(UpLo uplo, int n, int kd, int nrhs, double[] Ab, double[] B) {
        intW info = new intW(0);
        Dpbtrs.dpbtrs(uplo(uplo), n, kd, nrhs, Ab, 0, ld(kd + 1), B, 0, ld(n),
                info);
        return info.val;
    }

    public int pbcon(UpLo uplo, int n, int kd, double[] Ab, double anorm,
            double[] rcond, double[] work, int[] iwork) {
        intW info = new intW(0);
        doubleW rcondW = new doubleW(rcond[0]);
        Dpbcon.dpbcon(uplo(uplo), n, kd, Ab, 0, ld(kd + 1), anorm, rcondW,
                work, 0, iwork, 0, info);
        rcond[0] = rcondW.val;
        return info.val;
    }

    public int ptsv(int n, int nrhs, double[] d, double[] e, double[] B) {
        intW info = new intW(0);
        Dptsv.dptsv(n, nrhs, d, 0, e, 0, B, 0, ld(n), info);
        return info.val;
    }

    public int sysv(UpLo uplo, int n, int nrhs, double[] A, int[] ipiv,
            double[] B, double[] work, int lwork) {
        intW info = new intW(0);
        Dsysv.dsysv(uplo(uplo), n, nrhs, A, 0, ld(n), ipiv, 0, B, 0, ld(n),
                work, 0, lwork, info);
        return info.val;
    }

    public int spsv(UpLo uplo, int n, int nrhs, double[] Ap, int[] ipiv,
            double[] B) {
        intW info = new intW(0);
        Dspsv.dspsv(uplo(uplo), n, nrhs, Ap, 0, ipiv, 0, B, 0, ld(n), info);
        return info.val;
    }

    public int geqrf(int m, int n, double[] A, double[] tau, double[] work,
            int lwork) {
        intW info = new intW(0);
        Dgeqrf.dgeqrf(m, n, A, 0, ld(m), tau, 0, work, 0, lwork, info);
        return info.val;
    }

    public int ormqr(Side side, Transpose trans, int m, int n, int k,
            double[] A, double[] tau, double[] C, double[] work, int lwork) {
        intW info = new intW(0);
        Dormqr.dormqr(side(side), trans(trans), m, n, k, A, 0,
                side == Side.Left ? ld(m) : ld(n), tau, 0, C, 0, ld(m), work,
                0, lwork, info);
        return info.val;
    }

    public int orgqr(int m, int n, int k, double[] A, double[] tau,
            double[] work, int lwork) {
        intW info = new intW(0);
        Dorgqr.dorgqr(m, n, k, A, 0, ld(m), tau, 0, work, 0, lwork, info);
        return info.val;
    }

    public int geqlf(int m, int n, double[] A, double[] tau, double[] work,
            int lwork) {
        intW info = new intW(0);
        Dgeqlf.dgeqlf(m, n, A, 0, ld(m), tau, 0, work, 0, lwork, info);
        return info.val;
    }

    public int ormql(Side side, Transpose trans, int m, int n, int k,
            double[] A, double[] tau, double[] C, double[] work, int lwork) {
        intW info = new intW(0);
        Dormql.dormql(side(side), trans(trans), m, n, k, A, 0,
                side == Side.Left ? ld(m) : ld(n), tau, 0, C, 0, ld(m), work,
                0, lwork, info);
        return info.val;
    }

    public int orgql(int m, int n, int k, double[] A, double[] tau,
            double[] work, int lwork) {
        intW info = new intW(0);
        Dorgql.dorgql(m, n, k, A, 0, ld(m), tau, 0, work, 0, lwork, info);
        return info.val;
    }

    public int gerqf(int m, int n, double[] A, double[] tau, double[] work,
            int lwork) {
        intW info = new intW(0);
        Dgerqf.dgerqf(m, n, A, 0, ld(m), tau, 0, work, 0, lwork, info);
        return info.val;
    }

    public int ormrq(Side side, Transpose trans, int m, int n, int k,
            double[] A, double[] tau, double[] C, double[] work, int lwork) {
        intW info = new intW(0);
        Dormrq.dormrq(side(side), trans(trans), m, n, k, A, 0, ld(k), tau, 0,
                C, 0, ld(m), work, 0, lwork, info);
        return info.val;
    }

    public int orgrq(int m, int n, int k, double[] A, double[] tau,
            double[] work, int lwork) {
        intW info = new intW(0);
        Dorgrq.dorgrq(m, n, k, A, 0, ld(m), tau, 0, work, 0, lwork, info);
        return info.val;
    }

    public int gelqf(int m, int n, double[] A, double[] tau, double[] work,
            int lwork) {
        intW info = new intW(0);
        Dgelqf.dgelqf(m, n, A, 0, ld(m), tau, 0, work, 0, lwork, info);
        return info.val;
    }

    public int ormlq(Side side, Transpose trans, int m, int n, int k,
            double[] A, double[] tau, double[] C, double[] work, int lwork) {
        intW info = new intW(0);
        Dormlq.dormlq(side(side), trans(trans), m, n, k, A, 0, ld(k), tau, 0,
                C, 0, ld(m), work, 0, lwork, info);
        return info.val;
    }

    public int orglq(int m, int n, int k, double[] A, double[] tau,
            double[] work, int lwork) {
        intW info = new intW(0);
        Dorglq.dorglq(m, n, k, A, 0, ld(m), tau, 0, work, 0, lwork, info);
        return info.val;
    }

    public int gbtrf(int m, int n, int kl, int ku, double[] Ab, int[] ipiv) {
        intW info = new intW(0);
        Dgbtrf.dgbtrf(m, n, kl, ku, Ab, 0, 2 * kl + ku + 1, ipiv, 0, info);
        return info.val;
    }

    public int gbtrs(Transpose trans, int n, int kl, int ku, int nrhs,
            double[] Ab, int[] ipiv, double[] B) {
        intW info = new intW(0);
        Dgbtrs.dgbtrs(trans(trans), n, kl, ku, nrhs, Ab, 0, 2 * kl + ku + 1,
                ipiv, 0, B, 0, ld(n), info);
        return info.val;
    }

    public int getrf(int m, int n, double[] A, int[] ipiv) {
        intW info = new intW(0);
        Dgetrf.dgetrf(m, n, A, 0, ld(m), ipiv, 0, info);
        return info.val;
    }

    public int getrs(Transpose trans, int n, int nrhs, double[] A, int[] ipiv,
            double[] B) {
        intW info = new intW(0);
        Dgetrs.dgetrs(trans(trans), n, nrhs, A, 0, ld(n), ipiv, 0, B, 0, ld(n),
                info);
        return info.val;
    }

    public int gecon(Norm norm, int n, double[] A, double anorm,
            double[] rcond, double[] work, int[] iwork) {
        intW info = new intW(0);
        doubleW rcondW = new doubleW(rcond[0]);
        Dgecon.dgecon(norm(norm), n, A, 0, ld(n), anorm, rcondW, work, 0,
                iwork, 0, info);
        rcond[0] = rcondW.val;
        return info.val;
    }

    public int gbcon(Norm norm, int n, int kl, int ku, double[] Ab, int[] ipiv,
            double anorm, double[] rcond, double[] work, int[] iwork) {
        intW info = new intW(0);
        doubleW rcondW = new doubleW(rcond[0]);
        Dgbcon.dgbcon(norm(norm), n, kl, ku, Ab, 0, ld(2 * kl + ku + 1), ipiv,
                0, anorm, rcondW, work, 0, iwork, 0, info);
        rcond[0] = rcondW.val;
        return info.val;
    }

    public int gttrf(int n, double[] dl, double[] d, double[] du, double[] du2,
            int[] ipiv) {
        intW info = new intW(0);
        Dgttrf.dgttrf(n, dl, 0, d, 0, du, 0, du2, 0, ipiv, 0, info);
        return info.val;
    }

    public int gttrs(Transpose trans, int n, int nrhs, double[] dl, double[] d,
            double[] du, double[] du2, int[] ipiv, double[] B) {
        intW info = new intW(0);
        Dgttrs.dgttrs(trans(trans), n, nrhs, dl, 0, d, 0, du, 0, du2, 0, ipiv,
                0, B, 0, ld(n), info);
        return info.val;
    }

    public int gtcon(Norm norm, int n, double[] dl, double[] d, double[] du,
            double[] du2, int[] ipiv, double anorm, double[] rcond,
            double[] work, int[] iwork) {
        intW info = new intW(0);
        doubleW rcondW = new doubleW(rcond[0]);
        Dgtcon.dgtcon(norm(norm), n, dl, 0, d, 0, du, 0, du2, 0, ipiv, 0,
                anorm, rcondW, work, 0, iwork, 0, info);
        rcond[0] = rcondW.val;
        return info.val;
    }

    public int trtrs(UpLo uplo, Transpose trans, Diag diag, int n, int nrhs,
            double[] A, int lda, double[] B) {
        intW info = new intW(0);
        Dtrtrs.dtrtrs(uplo(uplo), trans(trans), diag(diag), n, nrhs, A, 0, lda,
                B, 0, ld(n), info);
        return info.val;
    }

    public int tptrs(UpLo uplo, Transpose trans, Diag diag, int n, int nrhs,
            double[] Ap, double[] B) {
        intW info = new intW(0);
        Dtptrs.dtptrs(uplo(uplo), trans(trans), diag(diag), n, nrhs, Ap, 0, B,
                0, ld(n), info);
        return info.val;
    }

    public int tbtrs(UpLo uplo, Transpose trans, Diag diag, int n, int kd,
            int nrhs, double[] Ab, double[] B) {
        intW info = new intW(0);
        Dtbtrs.dtbtrs(uplo(uplo), trans(trans), diag(diag), n, kd, nrhs, Ab, 0,
                ld(kd + 1), B, 0, ld(n), info);
        return info.val;
    }

    public int potrf(UpLo uplo, int n, double[] A) {
        intW info = new intW(0);
        Dpotrf.dpotrf(uplo(uplo), n, A, 0, ld(n), info);
        return info.val;
    }

    public int potrs(UpLo uplo, int n, int nrhs, double[] A, double[] B) {
        intW info = new intW(0);
        Dpotrs.dpotrs(uplo(uplo), n, nrhs, A, 0, ld(n), B, 0, ld(n), info);
        return info.val;
    }

    public int pocon(UpLo uplo, int n, double[] A, double anorm,
            double[] rcond, double[] work, int[] iwork) {
        intW info = new intW(0);
        doubleW rcondW = new doubleW(rcond[0]);
        Dpocon.dpocon(uplo(uplo), n, A, 0, ld(n), anorm, rcondW, work, 0,
                iwork, 0, info);
        rcond[0] = rcondW.val;
        return info.val;
    }

    private int ld(int n) {
        return Math.max(1, n);
    }

    private int ld(int m, int n) {
        return Math.max(1, Math.max(m, n));
    }

    private String trans(Transpose trans) {
        if (trans == Transpose.NoTranspose)
            return "N";
        else
            return "T";
    }

    private String side(Side side) {
        if (side == Side.Left)
            return "L";
        else
            return "R";
    }

    private String uplo(UpLo uplo) {
        if (uplo == UpLo.Lower)
            return "L";
        else
            return "U";
    }

    private String diag(Diag diag) {
        if (diag == Diag.NonUnit)
            return "N";
        else
            return "U";
    }

    private String jobEig(JobEig job) {
        if (job == JobEig.Eigenvalues)
            return "N";
        else
            return "V";
    }

    private String jobEigRange(JobEigRange job) {
        if (job == JobEigRange.All)
            return "A";
        else if (job == JobEigRange.Interval)
            return "V";
        else
            return "I";
    }

    private String jobSVD(JobSVD job) {
        if (job == JobSVD.All)
            return "A";
        else if (job == JobSVD.Part)
            return "S";
        else if (job == JobSVD.Overwrite)
            return "O";
        else
            return "N";
    }

    private String norm(Norm norm) {
        if (norm == Norm.One)
            return "1";
        else if (norm == Norm.Infinity)
            return "I";
        else
            throw new IllegalArgumentException(
                    "Norm must be the 1 or the Infinity norm");
    }

    public int sbevd(JobEig jobz, UpLo uplo, int n, int kd, double[] Ab,
            double[] w, double[] Z, double[] work, int lwork, int[] iwork,
            int liwork) {
        intW info = new intW(0);
        Dsbevd.dsbevd(jobEig(jobz), uplo(uplo), n, kd, Ab, 0, ld(kd + 1), w, 0,
                Z, 0, ld(n), work, 0, lwork, iwork, 0, liwork, info);
        return info.val;
    }

    public int spevd(JobEig jobz, UpLo uplo, int n, double[] Ap, double[] w,
            double[] Z, double[] work, int lwork, int[] iwork, int liwork) {
        intW info = new intW(0);
        Dspevd.dspevd(jobEig(jobz), uplo(uplo), n, Ap, 0, w, 0, Z, 0, ld(n),
                work, 0, lwork, iwork, 0, liwork, info);
        return info.val;
    }

    public int stevr(JobEig jobz, JobEigRange range, int n, double[] d,
            double[] e, double vl, double vu, int il, int iu, double abstol,
            int[] m, double[] w, double[] Z, int[] isuppz, double[] work,
            int lwork, int[] iwork, int liwork) {
        intW info = new intW(0);
        intW mW = new intW(0);
        Dstevr.dstevr(jobEig(jobz), jobEigRange(range), n, d, 0, e, 0, vl, vu,
                il, iu, abstol, mW, w, 0, Z, 0, ld(n), isuppz, 0, work, 0,
                lwork, iwork, 0, liwork, info);
        m[0] = mW.val;
        return info.val;
    }

    public int syevr(JobEig jobz, JobEigRange range, UpLo uplo, int n,
            double[] A, double vl, double vu, int il, int iu, double abstol,
            int[] m, double[] w, double[] Z, int[] isuppz, double[] work,
            int lwork, int[] iwork, int liwork) {
        intW info = new intW(0);
        intW mW = new intW(0);
        Dsyevr.dsyevr(jobEig(jobz), jobEigRange(range), uplo(uplo), n, A, 0,
                ld(n), vl, vu, il, iu, abstol, mW, w, 0, Z, 0, ld(n), isuppz,
                0, work, 0, lwork, iwork, 0, liwork, info);
        m[0] = mW.val;
        return info.val;
    }

}
