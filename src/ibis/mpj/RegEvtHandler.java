package ibis.mpj;

import ibis.ipl.Ibis;
import ibis.ipl.IbisIdentifier;
import ibis.ipl.RegistryEventHandler;

class RegEvtHandler implements RegistryEventHandler {

    IbisIdentifier myId;

    int nInstances;

    int myRank;

    private int joinCount;

    IbisIdentifier[] identifiers;

    public synchronized void joined(IbisIdentifier ident) {
        if (ident.equals(myId)) {
            myRank = joinCount;
        }
        identifiers[joinCount++] = ident;
        if (joinCount == nInstances) {
            notifyAll();
        }
    }

    public void left(IbisIdentifier ident) {
        // ignored
        System.out.println("A process left DREW WICKE " );
    }

    /**
     * DREW
     * I need to implement this so I know when a process dies!
     * @param corpse 
     */
    public void died(IbisIdentifier corpse) {
        // ignored
        System.out.println("A process died DREW WICKE " + corpse.name() + " " + corpse.poolName() + " " + corpse.tagAsString());
    }

    void waitForEveryone(Ibis ibis) {
        nInstances = ibis.registry().getPoolSize();
        identifiers = new IbisIdentifier[nInstances];
        myId = ibis.identifier();
        ibis.registry().enableEvents();
        synchronized (this) {
            while (joinCount < nInstances) {
                try {
                    wait();
                } catch (Exception e) {
                    // ignored
                }
            }
        }
    }

    public void electionResult(String name, IbisIdentifier winner) {
        // ignored
    }

    public void gotSignal(String signal, IbisIdentifier source) {
        // ignored
        System.out.println("Got sig: " + signal + " source " + source.tagAsString());
    }

    public void poolClosed() {
        // ignored
        System.out.println("pool closed DREW WICKE " );
    }

    public void poolTerminated(IbisIdentifier source) {
        // ignored
        System.out.println("A pool term DREW WICKE ");
    }
}
