package org.cp.semaphores.core.threads;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import org.cp.semaphores.core.SemaphoreRunnable;
import org.cp.semaphores.core.beans.FractalBean;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Semaphore;

/**
 * {@code FSemaphore} is the abstract foundation for the classes
 * that process the fractal calculation.
 *
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class FSemaphore
        implements SemaphoreRunnable {
    private static final Logger LOGGER = Logger.getLogger(FSemaphore.class);
    protected Semaphore          semaphore;
    protected Deque<FractalBean> deque;

    @Override
    public void initSemaphore(final int permits, final boolean fair) {
        this.semaphore = new Semaphore(permits, fair);
    }

    @Override
    public void initStack(final int size) {
        this.deque = new ArrayDeque<>();
    }

    @Override
    public final void run() {
        LOGGER.debug("acquireUninterruptibly()");
        this.semaphore.acquireUninterruptibly();
        try {
            LOGGER.debug("acquireUninterruptibly() - acquired");
            this.runInternal();
        } finally {
            this.semaphore.release();
            LOGGER.debug("acquireUninterruptibly() - released");
        }
    }

    protected abstract void runInternal();

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(semaphore)
                      .toString();
    }
}
