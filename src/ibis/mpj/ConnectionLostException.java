/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ibis.mpj;

/**
 * This represents a Connection that has been lost
 * So then the person sending the data will be notified
 * @author drew
 */
public class ConnectionLostException extends MPJException {

    public ConnectionLostException() {
    }

    public ConnectionLostException(String s) {
        super(s);
    }

    public ConnectionLostException(String s, Throwable e) {
        super(s, e);
    }

    
}
