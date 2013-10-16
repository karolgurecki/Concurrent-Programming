package org.cp.semaphores.core;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FMutex
        extends FSemaphore {
    public FMutex() {
        super(1);
    }
}
