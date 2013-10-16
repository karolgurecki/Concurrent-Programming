package org.cp.semaphores.threads;

import com.google.common.base.Objects;
import org.cp.semaphores.fractal.beans.FractalVariable;

import java.util.Queue;
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
    protected Semaphore              semaphore;
    protected Queue<FractalVariable> resource;
    protected RoundsFlag             rounds;

    @Override
    public final SemaphoreRunnable setSemaphore(final Semaphore semaphore) {
        this.semaphore = semaphore;
        return this;
    }

    @Override
    public final SemaphoreRunnable setSharedResource(final Queue<FractalVariable> resource) {
        this.resource = resource;
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
                this.semaphore.acquire();
                this.rounds.next();
                this.runInternal();
                this.semaphore.release();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
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
