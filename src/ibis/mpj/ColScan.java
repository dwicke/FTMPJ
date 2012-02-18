/* $Id: ColScan.java 11526 2009-11-18 09:37:09Z ceriel $ */

/*
 * Created on 11.03.2005
 */
package ibis.mpj;

import java.lang.reflect.Array;

/**
 * Implementation of the collective operation: scan.
 */
public class ColScan {

    private Object sendbuf = null;

    private int sendoffset = 0;

    private Object recvbuf = null;

    private int recvoffset = 0;

    private int count = 0;

    private Datatype datatype = null;

    private Op op = null;

    private Intracomm comm = null;

    private int tag = 0;

    public ColScan(Object sendbuf, int sendoffset, Object recvbuf,
            int recvoffset, int count, Datatype datatype, Op op,
            Intracomm comm, int tag) throws MPJException {

        this.sendbuf = sendbuf;
        this.sendoffset = sendoffset;
        this.recvbuf = recvbuf;
        this.recvoffset = recvoffset;
        this.count = count;
        this.datatype = datatype;
        this.op = op;
        this.comm = comm;
        this.tag = tag;
    }

    protected void call() throws MPJException {
        int size = this.comm.size();
        int rank = this.comm.rank();

        // Object origin = null;

        if (rank == 0) {
            System.arraycopy(this.sendbuf, this.sendoffset, this.recvbuf,
                    this.recvoffset, this.count * datatype.extent());
        }

        else {

            if (!op.isCommute()) {
                Class<?> componentType = sendbuf.getClass().getComponentType();
                Object tempBuf = Array.newInstance(componentType, Array.getLength(this.recvbuf));

                this.comm.localcopy1type(this.sendbuf, this.sendoffset,
                        this.recvbuf, this.recvoffset, this.count, datatype);

                this.comm.recv(tempBuf, recvoffset, count, datatype, rank - 1,
                        tag);

                op.call(tempBuf, recvoffset, recvbuf, recvoffset, count,
                        datatype);
            } else {
                this.comm.recv(recvbuf, recvoffset, count, datatype, rank - 1,
                        tag);

                op.call(sendbuf, sendoffset, recvbuf, recvoffset, count,
                        datatype);

            }

        }
        if (rank < (size - 1)) {
            this.comm.send(recvbuf, recvoffset, count, datatype, rank + 1, tag);
        }

    }
}
