package edu.vandy.model;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class define a counting semaphore with "fair" semantics.  Grad
 * students must implement this class using a Java ReentrantLock and
 * ConditionObject, whereas undergrad students must implement this
 * class using Java built-in monitor object features.
 */
public class SimpleSemaphore {
    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Make sure that this field will ensure
    // its values aren't cached by multiple threads..
    private volatile int permits;
    /**
     * Grad students define a ReentrantLock to protect critical
     * sections.
     */
    // TODO - you fill in here
    private ReentrantLock lock;
    
    /**
     * Grad students define a Condition that's used to wait while the
     * number of permits is 0.
     */
    // TODO - you fill in here
    private Condition empty;


    /**
     * Constructor initialize the fields.
     */
    public SimpleSemaphore (int permits) {
        // TODO -- you fill in here. Grad students make sure the
        // ReentrantLock has "fair" semantics.
        this.permits = permits;
        lock = new ReentrantLock(true);
        empty = lock.newCondition();
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire()
        throws InterruptedException {
        // TODO -- you fill in here.
        lock.lockInterruptibly();
        try {
            while (permits == 0){
             empty.await();
            }
            permits--;
        }finally {
            lock.unlock();
        }
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot
     * be interrupted.  If an interrupt occurs while this method is
     * running make sure to set the interrupt state when the thread
     * returns from this method.
     */
    public void acquireUninterruptibly() {
        // TODO -- you fill in here.
        boolean isInterrupted = false;
        lock.lock();
        try{
            while (permits == 0){
                empty.await();
            }
            permits--;
        } catch (InterruptedException e) {
            isInterrupted = true;
            //e.printStackTrace();
        } finally {
            lock.unlock();
            if(isInterrupted){
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Return one permit to the semaphore.
     */
    public void release() {
        // TODO -- you fill in here.
        lock.lock();
        try {
            if(++permits > 0){
                empty.signal();
            }
        }finally {
            lock.unlock();
        }
    }

    /**
     * Returns the current number of permits.
     */
    public int availablePermits() {
        // TODO -- you fill in here.
        return permits;
    }
}
