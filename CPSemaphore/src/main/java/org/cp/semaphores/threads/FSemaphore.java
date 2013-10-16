package org.cp.semaphores.threads;

import com.google.common.base.Objects;
import org.cp.semaphores.fractal.path.FractalPath;

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
    protected final Deque<FractalPath> resource;
    protected       Semaphore          semaphore;
    protected       RoundsFlag         rounds;

    protected FSemaphore(final Deque<FractalPath> resource) {
        this.resource = resource;
    }

    @Override
    public final SemaphoreRunnable setSemaphore(final Semaphore semaphore) {
        this.semaphore = semaphore;
        return this;
    }

    @Override
    public SemaphoreRunnable setRounds(final Integer rounds) {
        this.rounds = new RoundsFlag(rounds);
        return this;
    }

    @Override
    public final void run() {
        while (this.rounds.hasNext()) {
            try {
                Thread.sleep(100);
                this.semaphore.acquire();
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            try {
                this.rounds.next();
                synchronized (this.resource) {
                    this.runInternal();
                }
            } finally {
                this.semaphore.release();
            }
        }
    }

    protected abstract void runInternal();

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(semaphore)
                      .addValue(resource)
                      .addValue(rounds)
                      .toString();
    }
}
