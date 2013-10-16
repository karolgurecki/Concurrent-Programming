package org.cp.semaphores.threads;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import org.cp.semaphores.fractal.beans.FractalBean;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class DFPSemaphore
        extends FSemaphore {
    private static final Logger LOGGER = Logger.getLogger(DFPSemaphore.class);

    @Override
    protected void runInternal() {
        LOGGER.info(String.format("Entering critical => %d", this.rounds.getRound()));
        {
            final FractalBean bean = this.resource.poll();
            LOGGER.debug(String.format("Polled value=%s", bean));
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
