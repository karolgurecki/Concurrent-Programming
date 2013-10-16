package org.cp.semaphores.fractal.path;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.cp.semaphores.fractal.beans.FractalVariable;

import java.util.Iterator;
import java.util.List;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalPath
        implements Iterable<FractalPath.FStep> {
    private final List<FractalVariable> rawPath;
    private       List<FStep>           path;

    public FractalPath(final FractalVariable... variables) {
        this.path = Lists.newArrayList(this.analyzePath(variables));
        this.rawPath = Lists.newArrayList(variables);
    }

    public List<FractalVariable> getRawPath() {
        return rawPath;
    }

    public List<FStep> getPath() {
        return path;
    }

    private List<FStep> analyzePath(final FractalVariable[] variables) {
        final List<FStep> fSteps = Lists.newArrayList();
        for (final FractalVariable fv : variables) {
            fSteps.add(FStep.toFStep(fv));
        }
        return fSteps;
    }

    @Override
    public Iterator<FStep> iterator() {
        return this.path.iterator();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(rawPath)
                      .addValue(path)
                      .toString();
    }

    public enum FStep {
        LINE_WITH_LEAF("0"),
        LINE("1"),
        LEFT_TURN("["),
        RIGHT_TURN("]");
        private final String symbol;

        FStep(final String symbol) {
            this.symbol = symbol;
        }

        public static FStep toFStep(final FractalVariable fv) {
            for (final FStep fStep : FStep.values()) {
                if (fStep.getSymbol().equals(fv.getSymbol())) {
                    return fStep;
                }
            }
            return null;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
