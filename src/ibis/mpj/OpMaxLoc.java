/* $Id: OpMaxLoc.java 11524 2009-11-18 08:22:01Z ceriel $ */

/*
 * Created on 13.02.2005
 */
package ibis.mpj;

/**
 * Implementation of the operation: maximum local (MPJ.MAXLOC). Only available
 * for these datatypes: MPJ.SHORT2, MPJ.INT2, MPJ.LONG2, MPJ.FLOAT2,
 * MPJ.DOUBLE2.
 */
public class OpMaxLoc extends Op {
    OpMaxLoc(boolean commute) {
        super(commute);
    }

    public void call(Object invec, int inoffset, Object inoutvec,
            int outoffset, int count, Datatype datatype) throws MPJException {
        if (datatype == MPJ.SHORT2) {
            short[] shortInvec = (short[]) invec;
            short[] shortInoutvec = (short[]) inoutvec;
            if (shortInvec.length != shortInoutvec.length) {
                return;
            }

            int dispIndexIn = inoffset;
            int dispIndexOut = outoffset;

            for (int i = 0; i < count; i++) {
                short o1 = shortInvec[dispIndexIn];
                short o2 = shortInoutvec[dispIndexOut];

                if (o1 > o2) {
                    shortInoutvec[dispIndexOut] = o1;
                    shortInoutvec[dispIndexOut + 1] = shortInvec[dispIndexIn + 1];
                } else if (o1 == o2) {
                    if (shortInvec[dispIndexIn + 1] < shortInoutvec[dispIndexOut + 1]) {
                        shortInoutvec[dispIndexOut + 1] = shortInvec[dispIndexIn + 1];
                    }
                }
                dispIndexIn += 2;
                dispIndexOut += 2;
            }
            return;
        }
        if (datatype == MPJ.INT2) {
            int[] intInvec = (int[]) invec;
            int[] intInoutvec = (int[]) inoutvec;
            if (intInvec.length != intInoutvec.length) {
                return;
            }

            int dispIndexIn = inoffset;
            int dispIndexOut = outoffset;

            for (int i = 0; i < count; i++) {
                int o1 = intInvec[dispIndexIn];
                int o2 = intInoutvec[dispIndexOut];

                if (o1 > o2) {
                    intInoutvec[dispIndexOut] = o1;
                    intInoutvec[dispIndexOut + 1] = intInvec[dispIndexIn + 1];
                } else if (o1 == o2) {
                    if (intInvec[dispIndexIn + 1] < intInoutvec[dispIndexOut + 1]) {
                        intInoutvec[dispIndexOut + 1] = intInvec[dispIndexIn + 1];
                    }
                }
                dispIndexIn += 2;
                dispIndexOut += 2;
            }
            return;
        }
        if (datatype == MPJ.LONG2) {
            long[] longInvec = (long[]) invec;
            long[] longInoutvec = (long[]) inoutvec;
            if (longInvec.length != longInoutvec.length) {
                return;
            }

            int dispIndexIn = inoffset;
            int dispIndexOut = outoffset;

            for (int i = 0; i < count; i++) {
                long o1 = longInvec[dispIndexIn];
                long o2 = longInoutvec[dispIndexOut];

                if (o1 > o2) {
                    longInoutvec[dispIndexOut] = o1;
                    longInoutvec[dispIndexOut + 1] = longInvec[dispIndexIn + 1];
                } else if (o1 == o2) {
                    if (longInvec[dispIndexIn + 1] < longInoutvec[dispIndexOut + 1]) {
                        longInoutvec[dispIndexOut + 1] = longInvec[dispIndexIn + 1];
                    }
                }
                dispIndexIn += 2;
                dispIndexOut += 2;
            }
            return;
        }
        if (datatype == MPJ.FLOAT2) {
            float[] floatInvec = (float[]) invec;
            float[] floatInoutvec = (float[]) inoutvec;
            if (floatInvec.length != floatInoutvec.length) {
                return;
            }

            int dispIndexIn = inoffset;
            int dispIndexOut = outoffset;

            for (int i = 0; i < count; i++) {
                float o1 = floatInvec[dispIndexIn];
                float o2 = floatInoutvec[dispIndexOut];

                if (o1 > o2) {
                    floatInoutvec[dispIndexOut] = o1;
                    floatInoutvec[dispIndexOut + 1] = floatInvec[dispIndexIn + 1];
                } else if (o1 == o2) {
                    if (floatInvec[dispIndexIn + 1] < floatInoutvec[dispIndexOut + 1]) {
                        floatInoutvec[dispIndexOut + 1] = floatInvec[dispIndexIn + 1];
                    }
                }
                dispIndexIn += 2;
                dispIndexOut += 2;
            }
            return;
        }
        if (datatype == MPJ.DOUBLE2) {
            double[] doubleInvec = (double[]) invec;
            double[] doubleInoutvec = (double[]) inoutvec;
            if (doubleInvec.length != doubleInoutvec.length) {
                return;
            }

            int dispIndexIn = inoffset;
            int dispIndexOut = outoffset;

            for (int i = 0; i < count; i++) {
                double o1 = doubleInvec[dispIndexIn];
                double o2 = doubleInoutvec[dispIndexOut];

                if (o1 > o2) {
                    doubleInoutvec[dispIndexOut] = o1;
                    doubleInoutvec[dispIndexOut + 1] = doubleInvec[dispIndexIn + 1];
                } else if (o1 == o2) {
                    if (doubleInvec[dispIndexIn + 1] < doubleInoutvec[dispIndexOut + 1]) {
                        doubleInoutvec[dispIndexOut + 1] = doubleInvec[dispIndexIn + 1];
                    }
                }
                dispIndexIn += 2;
                dispIndexOut += 2;
            }
            return;
        }

        throw new MPJException("Operation does not support this Datatype");
    }
}
