package org.cp.semaphores.core;

import java.util.concurrent.Semaphore;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FSemaphore
        extends Semaphore {
    public FSemaphore(final int permits) {
        super(permits, true);
    }
}
