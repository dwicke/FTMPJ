package ibis.mpj;

class Waiter {
    
    private static boolean inRegion;
    
    private static int waiters = 0;
    
    private static Object lock = new Object();
    
    static void enterRegion() {
        synchronized(lock) {
            while (inRegion) {
                try {
                    lock.wait();
                } catch(Throwable e) {
                    // ignored
                }
            }
            inRegion = true;
        }
    }

    static void exitRegion() {
        synchronized(lock) {
            if (! inRegion) {
                throw new Error("Something wrong!");
            }
            inRegion = false;
            lock.notifyAll();
        }
    }
    
    static void doNotifyAndExitRegion() {
        synchronized(lock) {
            if (! inRegion) {
                throw new Error("Something wrong, you can only notify from within a region");
            }
            inRegion = false;
            lock.notifyAll();
        }
    }
    
    static void doWait() {
        synchronized(lock) {
            if (! inRegion) {
                throw new Error("Something wrong, you can only wait from within a region");
            }
            inRegion = false;
            lock.notifyAll();
            try {
                waiters++;
                lock.wait();
                waiters--;
            } catch (InterruptedException e) {
                // ignored
            }
            enterRegion();
        }
    }
}
