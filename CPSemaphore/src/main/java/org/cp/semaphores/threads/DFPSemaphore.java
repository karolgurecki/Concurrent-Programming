package org.cp.semaphores.threads;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import org.cp.semaphores.FractalTree;
import org.cp.semaphores.fractal.path.FractalPath;

import java.util.Deque;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class DFPSemaphore
        extends FSemaphore {
    private static final Logger LOGGER = Logger.getLogger(DFPSemaphore.class);
    private final FractalTree frame;

    public DFPSemaphore(final Deque<FractalPath> resource, final FractalTree fractalTree) {
        super(resource);
        this.frame = fractalTree;
    }

    @Override
    protected void runInternal() {
        LOGGER.info(String.format("Entering critical => %d", this.rounds.getRound()));
        while (this.resource.size() > 1) {
            final FractalPath path = this.resource.pollLast();
            LOGGER.debug(String.format("Polled value=%s", path));
            this.frame.updateTree(path);
        }
        LOGGER.info(String.format("Leaving critical => %d", this.rounds.getRound()));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(semaphore)
                      .addValue(resource)
                      .toString();
    }
}
