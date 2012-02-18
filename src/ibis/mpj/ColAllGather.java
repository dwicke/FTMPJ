/* $Id: ColAllGather.java 11517 2009-11-17 11:10:42Z ceriel $ */

/*
 * Created on 05.04.2005
 */
package ibis.mpj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the collective operation: allgather.
 */
public class ColAllGather {
    static Logger logger = LoggerFactory.getLogger(ColAllGather.class.getName());

    private Object sendbuf = null;

    private int sendoffset = 0;

    private int sendcount = 0;

    private Datatype sendtype = null;

    private Object recvbuf = null;

    private int recvoffset = 0;

    private int recvcount = 0;

    private Datatype recvtype = null;

    private Intracomm comm = null;

    private int tag = 0;

    private int recvextent = 0;

    public ColAllGather(Object sendbuf, int sendoffset, int sendcount,
            Datatype sendtype, Object recvbuf, int recvoffset, int recvcount,
            Datatype recvtype, Intracomm comm, int tag) {
        this.sendbuf = sendbuf;
        this.sendoffset = sendoffset;
        this.sendcount = sendcount;
        this.sendtype = sendtype;
        this.recvbuf = recvbuf;
        this.recvoffset = recvoffset;
        this.recvcount = recvcount;
        this.recvtype = recvtype;
        this.comm = comm;
        this.tag = tag;
        this.recvextent = recvtype.extent();

    }

    // double ring algorithm (taken from CCJ)
    protected void call() throws MPJException {
        int rank = this.comm.rank();
        int size = this.comm.size();

        int leftRank = getPrevRank(1, rank, size);
        int rightRank = getNextRank(1, rank, size);
        // int nextRank = 0;
        // int prevRank = 0;
        // int myPosition = 0;
        // int nextPosition = 0;
        // int prevPosition = 0;

        int rounds = (size - 1) / 2;
        boolean add_half_round = (((size - 1) % 2) != 0);

        this.comm.localcopy2types(sendbuf, sendoffset, sendcount, sendtype,
                recvbuf, recvoffset + rank * recvcount * recvextent,
                recvcount, recvtype);

        try {
            for (int i = 0; i < rounds; i++) {

                int nextWritePosition = this.recvoffset
                        + (getNextRank(i, rank, size) * (recvcount * recvextent));// +
                                                                        // displs[nextRank];
                int prevWritePosition = this.recvoffset
                        + (getPrevRank(i, rank, size) * (recvcount * recvextent));// +
                                                                        // displs[prevRank];

                int nextReadPosition = this.recvoffset
                        + (getNextRank(i + 1, rank, size) * (recvcount * recvextent));// +
                                                                        // displs[nextRank];
                int prevReadPosition = this.recvoffset
                        + (getPrevRank(i + 1, rank, size) * (recvcount * recvextent));// +
                                                                        // displs[prevRank];

                Request req1 = this.comm.irecv(recvbuf, nextReadPosition, this.recvcount,
                        this.recvtype, rightRank, this.tag);
                Request req2 = this.comm.irecv(recvbuf, prevReadPosition, this.recvcount,
                        this.recvtype, leftRank, this.tag);
                this.comm.send(recvbuf, nextWritePosition, this.recvcount,
                        this.recvtype, leftRank, this.tag);
                this.comm.send(recvbuf, prevWritePosition, this.recvcount,
                        this.recvtype, rightRank, this.tag);
                req1.Wait();
                req2.Wait();

            }
            if (add_half_round) {

                // just one send and one receive
                int prevRank = getPrevRank(rounds + 1, rank, size);
                int nextRank = getNextRank(rounds, rank, size);

                int nextPosition = this.recvoffset
                        + (nextRank * (recvcount * recvextent));// +
                                                                        // displs[nextRank];
                int prevPosition = this.recvoffset
                        + (prevRank * (recvcount * recvextent));// +
                                                                        // displs[prevRank];

                if (logger.isDebugEnabled()) {
                    logger.debug("prevRank: " + prevRank + "; prevPos: "
                            + prevPosition + "; rightRank: " + rightRank);
                    logger.debug("nextRank: " + nextRank + "; nextPos: "
                            + nextPosition + ";  leftRank: " + leftRank);
                }

                Request req = this.comm.irecv(recvbuf, prevPosition, this.recvcount,
                        this.recvtype, rightRank, this.tag);
                this.comm.send(recvbuf, nextPosition, this.recvcount,
                        this.recvtype, leftRank, this.tag);
                req.Wait();

            }
        } catch (Exception e) {
            throw new MPJException(e.toString());
        }

    }

    private int getPrevRank(int round, int myRank, int size) {
        return ((myRank + round) % size);

    }

    private int getNextRank(int round, int myRank, int size) {
        return ((myRank + size - round) % size);
    }
}
