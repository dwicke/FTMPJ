/* $Id: OpMax.java 11519 2009-11-17 16:11:18Z ceriel $ */

/*
 * Created on 12.02.2005
 */
package ibis.mpj;

/**
 * Implementation of the operation: maximum (MPJ.MAX). Only available for these
 * datatypes: MPJ.BYTE, MPJ.CHAR, MPJ.SHORT, MPJ.BOOLEAN, MPJ.INT, MPJ.LONG,
 * MPJ.FLOAT, MPJ.DOUBLE.
 */
public class OpMax extends Op {
    OpMax(boolean commute) {
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
                byte o1 = byteInvec[i + inoffset];
                byte o2 = byteInoutvec[i + outoffset];
                if (o1 > o2) {
                     byteInoutvec[i + outoffset] = o1;
                }
            }
            return;
        }
        if (datatype == MPJ.CHAR) {
            char[] charInvec = (char[]) invec;
            char[] charInoutvec = (char[]) inoutvec;
            if (charInvec.length != charInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                char o1 = charInvec[i + inoffset];
                char o2 = charInoutvec[i + outoffset];
                if (o1 > o2) {
                     charInoutvec[i + outoffset] = o1;
                }
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
                short o1 = shortInvec[i + inoffset];
                short o2 = shortInoutvec[i + outoffset];
                if (o1 > o2) {
                     shortInoutvec[i + outoffset] = o1;
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
                boolean o1 = booleanInvec[i + inoffset];
                if (o1) {
                     booleanInoutvec[i + outoffset] = o1;
                }
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
                int o1 = intInvec[i + inoffset];
                int o2 = intInoutvec[i + outoffset];
                if (o1 > o2) {
                     intInoutvec[i + outoffset] = o1;
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
                long o1 = longInvec[i + inoffset];
                long o2 = longInoutvec[i + outoffset];
                if (o1 > o2) {
                     longInoutvec[i + outoffset] = o1;
                }
            }
            return;
        }
        if (datatype == MPJ.FLOAT) {
            float[] floatInvec = (float[]) invec;
            float[] floatInoutvec = (float[]) inoutvec;
            if (floatInvec.length != floatInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                float o1 = floatInvec[i + inoffset];
                float o2 = floatInoutvec[i + outoffset];
                if (o1 > o2) {
                     floatInoutvec[i + outoffset] = o1;
                }
            }
            return;
        }
        if (datatype == MPJ.DOUBLE) {
            double[] doubleInvec = (double[]) invec;
            double[] doubleInoutvec = (double[]) inoutvec;
            if (doubleInvec.length != doubleInoutvec.length) {
                return;
            }

            for (int i = 0; i < count; i++) {
                double o1 = doubleInvec[i + inoffset];
                double o2 = doubleInoutvec[i + outoffset];
                if (o1 > o2) {
                     doubleInoutvec[i + outoffset] = o1;
                }
            }
            return;
        }

        throw new MPJException("Operation does not support this Datatype");
    }
}
