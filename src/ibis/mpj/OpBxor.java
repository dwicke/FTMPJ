/* $Id: OpBxor.java 11519 2009-11-17 16:11:18Z ceriel $ */

/*
 * Created on 12.02.2005
 */
package ibis.mpj;

/**
 * Implementation of the operation: binary xor (MPJ.BXOR). Only available for
 * these datatypes: MPJ.BYTE, MPJ.SHORT, MPJ.BOOLEAN, MPJ.INT and MPJ.LONG.
 */
public class OpBxor extends Op {
    OpBxor(boolean commute) {
        super(commute);
    }

    public void call(Object invec, int inoffset, Object inoutvec,
            int outoffset, int count, Datatype datatype) throws MPJException {
        if (datatype == MPJ.BYTE) {
            byte[] byteInvec = (byte[]) invec;
            byte[] byteInoutvec = (byte[]) inoutvec;
            if (byteInvec.length != byteInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                byteInoutvec[i + outoffset] ^= byteInvec[i+inoffset];
            }
            return;
        }
        if (datatype == MPJ.SHORT) {
            short[] shortInvec = (short[]) invec;
            short[] shortInoutvec = (short[]) inoutvec;
            if (shortInvec.length != shortInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                shortInoutvec[i + outoffset] ^= shortInvec[i+inoffset];
            }
            return;
        } 
        if (datatype == MPJ.BOOLEAN) {
            boolean[] booleanInvec = (boolean[]) invec;
            boolean[] booleanInoutvec = (boolean[]) inoutvec;
            if (booleanInvec.length != booleanInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                booleanInoutvec[i + outoffset] ^= booleanInvec[i+inoffset];
            }
            return;
        }
        if (datatype == MPJ.INT) {
            int[] intInvec = (int[]) invec;
            int[] intInoutvec = (int[]) inoutvec;
            if (intInvec.length != intInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                intInoutvec[i + outoffset] ^= intInvec[i+inoffset];
            }
            return;
        }
        if (datatype == MPJ.LONG) {
            long[] longInvec = (long[]) invec;
            long[] longInoutvec = (long[]) inoutvec;
            if (longInvec.length != longInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                longInoutvec[i + outoffset] ^= longInvec[i+inoffset];
            }
            return;
        }

        throw new MPJException("Operation does not support this Datatype");

    }
}
