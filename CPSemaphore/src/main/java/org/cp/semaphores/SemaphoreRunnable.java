package org.cp.semaphores;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public interface SemaphoreRunnable
        extends Runnable {
    void initSemaphore(final int permits, final boolean fair);

    void initStack(final int size);
}
