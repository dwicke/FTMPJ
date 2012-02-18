/* $Id: ColReduce.java 11526 2009-11-18 09:37:09Z ceriel $ */

/*
 * Created on 18.02.2005
 */
package ibis.mpj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;

/**
 * Implementation of the collective operation: reduce
 */
public class ColReduce {
    static Logger logger = LoggerFactory.getLogger(ColReduce.class.getName());

    private Object sendbuf = null;

    private int sendOffset = 0;

    private Object recvbuf = null;

    private int recvOffset = 0;

    private int count = 0;

    private Datatype datatype = null;

    private Intracomm comm = null;

    private Op op = null;

    private int root = 0;

    private int tag = 0;

    public ColReduce(Object sendBuf, int sendOffset, Object recvBuf,
            int recvOffset, int count, Datatype datatype, Op op, int root,
            Intracomm comm, int tag) {
        this.sendbuf = sendBuf;
        this.sendOffset = sendOffset;
        this.recvbuf = recvBuf;
        this.recvOffset = recvOffset;
        this.count = count;
        this.datatype = datatype;
        this.comm = comm;
        this.op = op;
        this.root = root;
        this.tag = tag;

    }

    protected void call() throws MPJException {
        if (root < 0 || root >= this.comm.size()) {
            throw new MPJException("root rank " + root + " is invalid.");
        }

        if (this.op.commute) {
            executeBin();
        } else {
            executeFlat();
        }
    }

    // binomial tree algorithm (taken from CCJ)
    private void executeBin() throws MPJException {
        int mask, relrank, peer;
        int rank = this.comm.rank();
        int size = this.comm.size();

        if (logger.isDebugEnabled()) {
            logger.debug(rank + ": Reduce started. Root = " + root
                    + " groupsize = " + size + " counter<< = " + this.tag);
        }

        Class<?> componentType = this.recvbuf.getClass().getComponentType();
        Object recv = Array.newInstance(componentType, Array.getLength(this.recvbuf));

        this.comm.localcopy1type(this.sendbuf, this.sendOffset, this.recvbuf,
                this.recvOffset, this.count, this.datatype);

        mask = 1;

        relrank = (rank - root + size) % size;

        while (mask < size) {
            if ((mask & relrank) == 0) { // receive and reduce
                peer = (relrank | mask);

                if (peer < size) {
                    peer = (peer + root) % size;

                    if (logger.isDebugEnabled()) {
                        logger.debug(rank + ": Reduce receive from " + peer);
                    }

                    this.comm.recv(recv, this.recvOffset, this.count,
                            this.datatype, peer, this.tag);

                    op.call(recv, recvOffset, recvbuf, recvOffset, count,
                            datatype);
                }
            } else { // send and terminate
                peer = ((relrank & (~mask)) + root) % size;

                if (logger.isDebugEnabled()) {
                    logger.debug(rank + ": Reduce send to " + peer);
                }

                this.comm.send(this.recvbuf, this.recvOffset, this.count,
                        this.datatype, peer, this.tag);
                break;
            }

            mask <<= 1;
        }

        if (logger.isDebugEnabled()) {
            logger.debug(rank + ": reduce done.");
        }

    }

    private void executeFlat() throws MPJException {
        if (this.comm.rank() == root) {
            Class<?> componentType = sendbuf.getClass().getComponentType();
            Object recv = Array.newInstance(componentType, Array.getLength(this.recvbuf));
            System.arraycopy(this.sendbuf, this.sendOffset, this.recvbuf,
                    this.recvOffset, this.count);

            for (int i = 0; i < this.comm.size(); i++) {
                if (i != root) {
                    this.comm.recv(recv, recvOffset, count, datatype, i,
                            this.tag);

                    op.call(recv, recvOffset, recvbuf, recvOffset, count,
                            datatype);

                }
            }
        } else {
            this.comm.send(sendbuf, sendOffset, count, datatype, root, this.tag);
        }
    }
}
