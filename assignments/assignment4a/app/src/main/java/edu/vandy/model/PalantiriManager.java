package edu.vandy.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

/**
 * Defines a mechanism that mediates concurrent access to a fixed
 * number of available Palantiri.  This class uses a "fair" Semaphore
 * and a ConcurrentHashMap, and synchronized statements to mediate
 * concurrent access to the Palantiri.  This class implements a
 * variant of the "Pooling" pattern
 * (kircher-schwanninger.de/michael/publications/Pooling.pdf).
 */
public class PalantiriManager {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final static String TAG = 
        PalantiriManager.class.getSimpleName();

    /**
     * A FairSemaphore that limits concurrent access to the fixed
     * number of available palantiri managed by the PalantiriManager.
     */
    private final Semaphore mAvailablePalantiri;

    /**
     * A map that associates the @a Palantiri key to the @a boolean
     * values that keep track of whether the key is available.
     */
    private final ConcurrentHashMap<Palantir, Boolean> mPalantiriMap;

    /**
     * Constructor creates a PalantiriManager for the List of @a
     * palantiri passed as a parameter and initializes the fields.
     */
    public PalantiriManager(List<Palantir> palantiri) {
        // Create a new ConcurrentHashMap, iterate through the List of
        // Palantiri and initialize each key in the HashMap with
        // "true" to indicate it's available, and initialize the
        // Semaphore to use a "fair" implementation that mediates
        // concurrent access to the given Palantiri.
        // TODO -- you fill in here.
        mPalantiriMap = new ConcurrentHashMap<>(palantiri.size());
        palantiri.forEach(p -> mPalantiriMap.put(p, true));

        mAvailablePalantiri = new Semaphore(palantiri.size(), true);
    }

    /**
     * Get a Palantir from the PalantiriManager, blocking until one is
     * available.
     */
    public Palantir acquire() {
        // Acquire the Semaphore uninterruptibly and then keep
        // iterating through the ConcurrentHashMap to find the first
        // key in the HashMap whose value is "true" (which indicates
        // it's available for use) and atomically replace the value of
        // this key with "false" to indicate the Palantir isn't
        // available and then return that palantir to the client.
        // There should be *no* synchronized statements in this
        // method.
        // TODO -- you fill in here.
        mAvailablePalantiri.acquireUninterruptibly();
        Set<Map.Entry<Palantir, Boolean>> entries = mPalantiriMap.entrySet();
        for (Map.Entry<Palantir, Boolean> entry:entries){
            if(entry.getValue()){
                entry.setValue(false);
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Returns the designated @code palantir to the PalantiriManager
     * so that it's available for other Threads to use.  An invalid @a
     * palantir is ignored.
     */
    public void release(final Palantir palantir) {
        // Put the "true" value back into ConcurrentHashMap for the
        // palantir key and release the Semaphore if all works
        // properly.  There should be *no* synchronized statements in
        // this method.
        // TODO -- you fill in here.
        mPalantiriMap.put(palantir, true);

        mAvailablePalantiri.release();
    }

    /*
     * The following method is just intended for use by the regression
     * tests, not by applications.
     */

    /**
     * Returns the number of available permits on the semaphore.
     */
    public int availablePermits() {
        return mAvailablePalantiri.availablePermits();
    }
}
