/* $Id: Request.java 11542 2009-11-19 15:36:50Z ceriel $ */

/*
 * Created on 21.01.2005
 */
package ibis.mpj;

/**
 * Request class for basic point-to-point communication.
 */
public class Request {

    protected transient Thread ibisMPJCommThread = null;

    protected transient IbisMPJComm ibisMPJComm = null;

    private Status status = null;

    public Request() {
        // nothing here
    }

    public void setIbisMPJComm(IbisMPJComm ibisMPJComm) {
        this.ibisMPJComm = ibisMPJComm;
    }

    /**
     * Blocks until the operation identified by the request is complete. After
     * the call returns, the request object becomes inactive.
     * 
     * @return status object
     * @throws MPJException
     */
    public Status Wait() throws MPJException {

        if (ibisMPJCommThread == null) {
            return (null);
        }

        try {
            // System.out.println("waiting");
            this.ibisMPJCommThread.join();
            this.ibisMPJCommThread = null;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        status = this.ibisMPJComm.getStatus();

        return (status);

    }

    /**
     * Returns a status object if the operation identified by the request is
     * complete, or a null reference otherwise. After the call, if the operation
     * is complete (ie, if the return value of test is non-null), the request
     * object becomes an inactive request.
     * 
     * @return status object
     * @throws MPJException
     */
    public Status test() throws MPJException {
        if (ibisMPJComm == null) {

            return (null);

        }
        // Status status;
        if (this.ibisMPJComm.isFinished()) {
            if (ibisMPJCommThread != null) {
                try {
                    this.ibisMPJCommThread.join();
                } catch (InterruptedException e) {
                    // ignored
                }
                this.ibisMPJCommThread = null;
            }

            return (this.ibisMPJComm.getStatus());
        }
        return (null);
    }

    public boolean isVoid() throws MPJException {
        return this.ibisMPJCommThread == null;
    }

    /**
     * Blocks until one of the operations associated with the active requests in
     * the array has completed. The corresponding element of arrayOfRequests
     * becomes inactive.
     * 
     * @param arrayOfRequests
     *            array of requests
     * @return status object
     * @throws MPJException
     */
    public static Status waitAny(Request[] arrayOfRequests) throws MPJException {
        if ((arrayOfRequests == null) || (arrayOfRequests.length == 0)) {
            return (null);
        }
        boolean hasActiveRequests = false;
        // First, check if there are active requests.
        for (int i = 0; i < arrayOfRequests.length; i++) {
            if (arrayOfRequests[i] != null && ! arrayOfRequests[i].isVoid()) {
                hasActiveRequests = true;
                break;
            }
        }
        if (! hasActiveRequests) {
            return null;
        }

        Status status = null;

        Waiter.enterRegion();
        for (;;) {
            status = testAny(arrayOfRequests);
            if (status != null) {
                Waiter.exitRegion();
                return status;
            }
            Waiter.doWait();
        }
    }

    /**
     * Tests for completion of either one or none of the operations associated
     * with active requests. If some request completed, the index in
     * arrayOfRequests of that request can be obtained from the status object.
     * If no request completed, testAny returns a null reference.
     * 
     * @param arrayOfRequests
     *            array of requests
     * @return status object or null reference
     * @throws MPJException
     */
    public static Status testAny(Request[] arrayOfRequests) throws MPJException {
        if ((arrayOfRequests == null) || (arrayOfRequests.length == 0)) {
            return (null);
        }

        Status status = null;
        for (int i = 0; i < arrayOfRequests.length; i++) {
            if (arrayOfRequests[i] != null && ! arrayOfRequests[i].isVoid()) {
                status = arrayOfRequests[i].test();
                if (status != null) {
                    status.setIndex(i);
                    break;
                }
            }
        }
        return (status);

    }

    /**
     * Blocks until all of the operations associated with the active requests in
     * the array have completed. The result array will be the same size as
     * arrayOfRequests. On exit, requests become inactive. If the input value of
     * arrayOfRequests contains any inactive requests, corresponding elements of
     * the result array will contain null status references.
     * 
     * @param arrayOfRequests
     *            array of requests
     * @return array of status objects, or a null reference
     * @throws MPJException
     */
    public static Status[] waitAll(Request[] arrayOfRequests)
            throws MPJException {

        if ((arrayOfRequests == null) || (arrayOfRequests.length == 0)) {
            return null;
        }
        Status[] status = new Status[arrayOfRequests.length];
        boolean hasActiveRequests = false;
        // First, check if there are active requests.
        for (int i = 0; i < arrayOfRequests.length; i++) {
            if (arrayOfRequests[i] != null && ! arrayOfRequests[i].isVoid()) {
                hasActiveRequests = true;
                break;
            }
        }
        if (! hasActiveRequests) {
            return status;
        }

        Waiter.enterRegion();
        // System.out.println("waitAll entered region");
        int count = 1;
        while (count != 0) {
            count = 0;
            for (int i = 0; i < arrayOfRequests.length; i++) {
                if (arrayOfRequests[i].isVoid()) {
                    continue;
                }
                if (status[i] == null) {
                    status[i] = arrayOfRequests[i].test();
                }
                if (status[i] == null) {
                    count++;
                }
            }
            if (count != 0) {
                // System.out.println("waitAll entering doWait");
                Waiter.doWait();
            }
        }
        Waiter.exitRegion();
        // System.out.println("waitAll exited region");
        return (status);

    }

    /**
     * Tests for completion of all of the operations associated with active
     * requests. If all operations have completed, the exit values of the
     * argument array and the result array are as for waitAll. If any operation
     * has not completed, the result value is null and no element of the
     * argument array is modified.
     * 
     * @param arrayOfRequests
     *            array of requests
     * @return array of status objects, or a null reference
     * @throws MPJException
     */
    public static Status[] testAll(Request[] arrayOfRequests)
            throws MPJException {
        if ((arrayOfRequests == null) || (arrayOfRequests.length == 0)) {
            return (null);
        }

        Status[] status = new Status[arrayOfRequests.length];
        boolean check = true;
        for (int i = 0; i < arrayOfRequests.length; i++) {
            if (arrayOfRequests[i] != null && ! arrayOfRequests[i].isVoid()) {
                if (! arrayOfRequests[i].ibisMPJComm.isFinished()) {
                    check = false;
                    break;
                }
            }
        }

        if (check) {
            for (int i = 0; i < arrayOfRequests.length; i++) {
                if (arrayOfRequests[i] != null && ! arrayOfRequests[i].isVoid()) {
                    status[i] = arrayOfRequests[i].test();
                }
            }
            return status;
        }
        
        return null;
    }

    /**
     * Blocks until at least one of the operations associated with the active
     * requests in the array has completed. The correspondig element in
     * arrayOfRequests becomes inactive. If arrayOfRequests list contains no
     * active requests, waitSome immediately returns a null reference.
     * 
     * @param arrayOfRequests
     *            array of requests
     * @return array of status objects, or a null reference
     * @throws MPJException
     */
    public static Status[] waitSome(Request[] arrayOfRequests)
            throws MPJException {

        // System.out.println("waitsome");
        if ((arrayOfRequests == null) || (arrayOfRequests.length == 0)) {
            return (null);
        }

        Status[] status = new Status[arrayOfRequests.length];
        Status[] newStatus = null;
        
        int count2 = 0;
        Waiter.enterRegion();
        while (count2 == 0) {
            int count = 0;
            for (int i = 0; i < status.length; i++) {
                if (arrayOfRequests[i] != null && !arrayOfRequests[i].isVoid()) {
                    status[i] = arrayOfRequests[i].test();
                    if (status[i] != null) {
                        status[i].setIndex(i);
                        count2++;
                    }
                    count++;
                }
            }
            if (count == 0) {
                Waiter.exitRegion();
                return null;
            }
            if (count2 == 0) {
                Waiter.doWait();
            }
        }
        Waiter.exitRegion();
        
        newStatus = new Status[count2];

        count2 = 0;
        for (int i = 0; i < status.length; i++) {
            if (status[i] != null) {
                newStatus[count2] = status[i];
                count2++;
            }
        }

        return newStatus;
    }

    /**
     * Behaves like waitSome, except that it returns immediately. If no
     * operation has completed, testSome returns an array of length zero and
     * elements of arrayOfRequests are unchanged. Otherwise, arguments and
     * return value are as for waitSome.
     * 
     * @param arrayOfRequests
     *            array of requests
     * @return array of status objects, or a null reference
     * @throws MPJException
     */
    public static Status[] testSome(Request[] arrayOfRequests)
            throws MPJException {
        if ((arrayOfRequests == null) || (arrayOfRequests.length == 0)) {
            return (null);
        }

        Status[] status = new Status[arrayOfRequests.length];

        int count = 0;
        for (int i = 0; i < arrayOfRequests.length; i++) {
            if (arrayOfRequests[i] != null && ! arrayOfRequests[i].isVoid()) {
                status[count] = arrayOfRequests[i].test();
                if (status[count] != null) {
                    status[count].setIndex(i);
                    count++;
                }
            }
        }

        Status[] newStatus = new Status[count];
        for (int i = 0; i < count; i++) {
            newStatus[i] = status[i];
        }

        return (newStatus);
    }
}
