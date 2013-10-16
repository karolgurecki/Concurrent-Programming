package org.cp.semaphores.threads;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.cp.semaphores.fractal.FractalConfiguration;
import org.cp.semaphores.fractal.beans.FractalBean;
import org.cp.semaphores.fractal.beans.FractalConstant;
import org.cp.semaphores.fractal.beans.FractalVariable;
import org.cp.semaphores.fractal.path.FractalPath;
import org.cp.semaphores.fractal.rule.FractalRule;

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
    private final FractalConstant       axiom;
    private final List<FractalConstant> constants;
    private final List<FractalRule>     rules;


    public CFPSemaphore(final Deque<FractalPath> resource, final FractalConfiguration fractalConfiguration) {
        super(resource);
        this.axiom = fractalConfiguration.getAxiom();
        this.constants = fractalConfiguration.getConstants();
        this.rules = fractalConfiguration.getRules();
    }

    @Override
    protected void runInternal() {
        LOGGER.info(String.format("Entering critical => %d", this.rounds.getRound()));

        final FractalPath peek = this.resource.peekLast();
        final List<FractalVariable> nextRawPath = Lists.newArrayList();

        for (final FractalVariable path : peek.getRawPath()) {

            for (final FractalRule rule : this.rules) {
                for (final FractalBean predecessor : rule.getPredecessor()) {
                    if (predecessor.equals(path)) {
                        final List<FractalBean> successors = rule.getSuccessors();

                        for (final FractalBean successor : successors) {
                            nextRawPath.add(new FractalVariable(successor.getSymbol()));
                        }

                    }
                }
            }

        }

        final FractalPath path = new FractalPath(nextRawPath.toArray(new FractalVariable[nextRawPath.size()]));
        if (this.resource.offerFirst(path)) {
            LOGGER.debug(String.format("From %s to %s", peek.getRawPath(), path.getRawPath()));
        }

        LOGGER.info(String.format("Leaving critical => %d", this.rounds.getRound()));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(axiom)
                      .addValue(constants)
                      .addValue(rules)
                      .addValue(semaphore)
                      .addValue(resource)
                      .addValue(rounds)
                      .toString();
    }
}
