/* $Id: MPJObjectQueue.java 11523 2009-11-17 16:31:01Z ceriel $ */

/*
 * Created on 23.01.2005
 */
package ibis.mpj;

import java.lang.reflect.Array;

import java.util.Vector;

/**
 * The queue which holds all received objects, which were not expected.
 */
public class MPJObjectQueue {

    private Vector<MPJObject> queue;

    private boolean lock;

    protected MPJObjectQueue() {
        this.queue = new Vector<MPJObject>();
        this.lock = false;
    }

    protected int size() {
        return queue.size();
    }

    protected synchronized void addObject(MPJObject obj) {
        queue.add(obj);
    }

    protected synchronized MPJObject getObject(int contextId, int tag) {
        MPJObject obj = null;
        int i = 0;

        for (MPJObject o : queue) {
            if (((o.getTag() == tag) || ((tag == MPJ.ANY_TAG) && (o.getTag() >= 0)))
                    && (o.getContextId() == contextId)) {

                obj = o;
                break;
            }
            i++;
        }

        if (obj != null) {
            System.err.print("DREW IN MPJObjectQueue and removing obj " + obj);
            queue.remove(i);
        }

        return obj;
    }

    protected synchronized Status probe(int contextId, int tag)
            throws MPJException {
        MPJObject obj = null;
        Status status = null;

        // System.out.println("checking ObjectQueue.");

        for (MPJObject o : queue) {
            
            if (((o.getTag() == tag) || ((tag == MPJ.ANY_TAG) && (o.getTag() >= 0)))
                    && (o.getContextId() == contextId)) {
                obj = o;
                break;
            }
            
        }
        System.err.append("DREW MPJOBJECTQUEUE probe is " + obj);
        if (obj != null) {
            status = new Status();
            if (obj.getObjectData() != null) {
                status.setTag(obj.getTag());
                status.setCount(Array.getLength(obj.getObjectData()));
                status.setSize(status.getCount(null));
                return (status);
            }
        }
        return (status);
    }

    protected synchronized void lock() {
        this.lock = true;
    }

    protected boolean isLocked() {
        return (this.lock);
    }

    protected synchronized void release() {
        this.lock = false;
    }

}
