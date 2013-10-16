package org.cp.semaphores.threads;

import java.util.concurrent.Semaphore;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public interface SemaphoreRunnable
        extends Runnable {
    SemaphoreRunnable setSemaphore(final Semaphore semaphore);

    SemaphoreRunnable setRounds(final Integer rounds);
}
