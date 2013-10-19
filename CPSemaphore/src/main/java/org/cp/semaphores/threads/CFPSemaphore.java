package org.cp.semaphores.threads;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.cp.semaphores.fractal.FractalConfiguration;
import org.cp.semaphores.fractal.beans.FractalBean;
import org.cp.semaphores.fractal.beans.FractalConstant;
import org.cp.semaphores.fractal.path.FractalPath;
import org.cp.semaphores.fractal.rule.FractalRule;

import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * {@code CFSemaphore} stands for <b>CalculatingFractalPathSemaphore</b>.
 * It calculates the consecutive points which are further pushed to the stack
 * from which another thread pops them and use to
 *
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class CFPSemaphore
        extends FSemaphore {
    private static final Logger LOGGER = Logger.getLogger(CFPSemaphore.class);
    private final FractalConfiguration cfg;


    public CFPSemaphore(final Deque<FractalPath> resource, final FractalConfiguration fractalConfiguration) {
        super(resource);
        this.cfg = fractalConfiguration;
    }

    @Override
    protected void runInternal() {
        LOGGER.info(String.format("Entering critical => %d", this.rounds.getRound()));

        final FractalPath peek = this.resource.peekFirst();
        final List<FractalBean> nextRawPath = Lists.newArrayList();

        for (final FractalBean peekElement : peek.getRawPath()) {

            // placeholder for the elements of the new path

            if (this.cfg.isVariable(peekElement)) {

                // loop through all rules and check predecessors
                for (final FractalRule rule : this.cfg.getRules()) {

                    if (rule.isPredecessor(peekElement)) {
                        nextRawPath.addAll(this.copyBean(rule.getSuccessors()));
                        break;
                    }
                }

            } else if (this.cfg.isConstant(peekElement)) {
                nextRawPath.add(new FractalConstant(peekElement.getSymbol()));
            }

        }

        final FractalPath path = new FractalPath(nextRawPath.toArray(new FractalBean[nextRawPath.size()]));
        if (this.resource.offerFirst(path)) {
            LOGGER.debug(String.format("From %s to %s", peek.getRawPath(), path.getRawPath()));
        }

        LOGGER.info(String.format("Leaving critical => %d", this.rounds.getRound()));
    }

    private Collection<? extends FractalBean> copyBean(final List<FractalBean> beans) {
        List<FractalBean> fractalBeans = Lists.newArrayList();
        for (final FractalBean bean : beans) {
            fractalBeans.add(this.cfg.getBeanFromString(bean.getSymbol())[0]);
        }
        return fractalBeans;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(cfg)
                      .addValue(semaphore)
                      .addValue(resource)
                      .addValue(rounds)
                      .toString();
    }
}
