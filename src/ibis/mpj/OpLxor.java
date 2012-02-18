/* $Id: OpLxor.java 11519 2009-11-17 16:11:18Z ceriel $ */

/*
 * Created on 12.02.2005
 */
package ibis.mpj;

/**
 * Implementation of the operation: logical xor (MPJ.LXOR). Only available for
 * these datatypes: MPJ.SHORT, MPJ.BOOLEAN, MPJ.INT and MPJ.LONG.
 */
public class OpLxor extends Op {
    OpLxor(boolean commute) {
        super(commute);
    }

    public void call(Object invec, int inoffset, Object inoutvec,
            int outoffset, int count, Datatype datatype) throws MPJException {

        if (datatype == MPJ.SHORT) {
            short[] shortInvec = (short[]) invec;
            short[] shortInoutvec = (short[]) inoutvec;
            if (shortInvec.length != shortInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                short o1 = shortInvec[i+inoffset];
                short o2 = shortInoutvec[i+outoffset];
                if ((o1 == 0 && o2 == 0) || (o1 != 0 && o2 != 0)) {
                    shortInoutvec[i+outoffset] = 0;
                } else {
                    shortInoutvec[i+outoffset] = 1;
                }
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
                boolean o1 = booleanInvec[i+inoffset];
                boolean o2 = booleanInoutvec[i+outoffset];
                booleanInoutvec[i+outoffset] = o1 ^ o2;
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
                int o1 = intInvec[i+inoffset];
                int o2 = intInoutvec[i+outoffset];
                if ((o1 == 0 && o2 == 0) || (o1 != 0 && o2 != 0)) {
                    intInoutvec[i+outoffset] = 0;
                } else {
                    intInoutvec[i+outoffset] = 1;
                }
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
                long o1 = longInvec[i+inoffset];
                long o2 = longInoutvec[i+outoffset];
                if ((o1 == 0 && o2 == 0) || (o1 != 0 && o2 != 0)) {
                    longInoutvec[i+outoffset] = 0;
                } else {
                    longInoutvec[i+outoffset] = 1;
                }
            }
            return;
        }

        throw new MPJException("Operation does not support this Datatype");
    }

}
