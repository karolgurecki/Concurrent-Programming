package org.cp.semaphores.threads;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import org.cp.semaphores.fractal.FractalConfiguration;
import org.cp.semaphores.fractal.beans.FractalBean;
import org.cp.semaphores.fractal.beans.FractalConstant;
import org.cp.semaphores.fractal.beans.FractalVariable;
import org.cp.semaphores.fractal.rule.FractalRule;

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
    private boolean initialGeneration = true;

    public CFPSemaphore(final FractalConfiguration fractalConfiguration) {
        this.axiom = fractalConfiguration.getAxiom();
        this.constants = fractalConfiguration.getConstants();
        this.rules = fractalConfiguration.getRules();
    }

    @Override
    protected void runInternal() {
        LOGGER.info(String.format("Entering critical => %d", this.rounds.getRound()));
        {
            if (this.isInitial()) {
                this.resource.add(new FractalVariable(this.axiom.getSymbol()));
                this.initialGeneration = false;
                LOGGER.debug(String.format("Initial generation, current queue=%d", this.resource.size()));
            }
            for (final FractalVariable variable : this.resource) {
                for (final FractalRule rule : this.rules) {
                    for (final FractalBean predecessor : rule.getPredecessor()) {
                        if (predecessor.equals(variable)) {
                            for (final FractalBean successor : rule.getSuccessors()) {
                                final FractalVariable nextElement = new FractalVariable(successor.getSymbol());
                                LOGGER.debug(String
                                        .format("For predecessor=[ %s ] generated nextElement= [ %s ]", predecessor.getSymbol(), nextElement.getSymbol()));
                                this.resource.offer(nextElement);
                            }
                        }
                    }
                }
            }
        }
        LOGGER.info(String.format("Leaving critical => %d", this.rounds.getRound()));
    }

    private boolean isInitial() {
        return this.resource.isEmpty() || this.initialGeneration;
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
