package org.cp.semaphores.fractal.path;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.cp.semaphores.fractal.beans.FractalBean;
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
    private final List<FractalBean> rawPath;
    private       List<FStep>       path;

    public FractalPath(final FractalBean... variables) {
        this.path = Lists.newArrayList(this.analyzePath(variables));
        this.rawPath = Lists.newArrayList(variables);
    }

    public static FractalPath combine(final FractalPath target, final FractalPath source) {
        target.path.addAll(source.path);
        return target;
    }

    public List<FractalBean> getRawPath() {
        return rawPath;
    }

    public List<FStep> getPath() {
        return path;
    }

    private List<FStep> analyzePath(final FractalBean[] variables) {
        final List<FStep> fSteps = Lists.newArrayList();
        for (final FractalBean fv : variables) {
            fSteps.add(FStep.toFStep(fv));
        }
        return fSteps;
    }

    @Override
    public Iterator<FStep> iterator() {
        return this.path.iterator();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FractalPath)) {
            return false;
        }

        final FractalPath path1 = (FractalPath) o;

        return !(path != null ? !path.equals(path1.path) : path1.path != null);
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(this.renderPath(path))
                      .toString();
    }

    private String renderPath(final List<FStep> path) {
        final StringBuilder builder = new StringBuilder();
        for (FStep fStep : path) {
            builder.append(fStep.getSymbol());
        }
        return builder.toString();
    }

    public FractalPath append(final FractalVariable var) {
        this.rawPath.add(var);
        this.path.add(FStep.toFStep(var));
        return this;
    }

    public FractalPath update(final FractalPath fractalPath) {
        this.rawPath.addAll(fractalPath.rawPath);
        this.path.addAll(fractalPath.path);
        return this;
    }

    public boolean isEmpty() {
        return this.path.isEmpty() && this.rawPath.isEmpty();
    }

    public int size() {
        return this.path.size();
    }

    public enum FStep {
        LINE_WITH_LEAF("W"),
        LINE("L"),
        LEFT_TURN("+"),
        RIGHT_TURN("-"),
        FORWARD("F"),
        BACKWARD("B");
        private final String symbol;

        FStep(final String symbol) {
            this.symbol = symbol;
        }

        public static FStep toFStep(final FractalBean fv) {
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
