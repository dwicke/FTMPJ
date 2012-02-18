/* $Id: ConnectionTable.java 8456 2008-05-30 12:24:07Z ceriel $ */

/*
 * Created on 19.02.2005
 */
package ibis.mpj;

import ibis.ipl.IbisIdentifier;

import java.util.HashMap;

/**
 * Collection of all MPJ connections.
 */
public class ConnectionTable {
    private final HashMap<IbisIdentifier, Connection> conTable;

    public ConnectionTable() {
        this.conTable = new HashMap<IbisIdentifier, Connection>();

    }

    protected void addConnection(IbisIdentifier id, Connection con) {
        conTable.put(id, con);
    }
    
    protected Connection getUntestedConnection(IbisIdentifier id) throws MPJException
    {
        if (conTable.containsKey(id)) {
            return conTable.get(id);
        }
        throw new MPJException("" + id + " not found in ConnectionTable.");
    }
    
    protected boolean isConnected(int id)
    {
        return conTable.get(MPJ.COMM_WORLD.group.getId(id)).isConnectionEstablished();
    }

    protected Connection getConnection(IbisIdentifier id) throws MPJException {
        // DREW added-  && conTable.get(id).isConnectionEstablished() == true
        // didn't work since apparently somewhere a conections is needed
        // without being connected I found that it is in MPJ.init that this
        // happens so I changed MPJ.init so that it can deal with the exception
        /*
        if (conTable.containsKey(id) && conTable.get(id).isConnectionEstablished() == true) {
            return conTable.get(id);
        }else if (conTable.containsKey(id) && conTable.get(id).isConnectionEstablished() == false)
        {
            throw new ConnectionLostException("" + id + " not connected"); 
        }
        
        throw new MPJException("" + id + " not found in ConnectionTable.");
         * 
         */
        
        if (conTable.containsKey(id)) {
            return conTable.get(id);
        }
        throw new MPJException("" + id + " not found in ConnectionTable.");
    }
}
