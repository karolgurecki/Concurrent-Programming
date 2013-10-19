package org.cp.semaphores.fractal.rule;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.cp.semaphores.fractal.beans.FractalBean;

import java.util.List;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalRule {
    private List<FractalBean> predecessor;
    private List<FractalBean> successors;

    public FractalRule() {
        this.predecessor = Lists.newArrayList();
        this.successors = Lists.newArrayList();
    }

    public FractalRule setPredecessors(final FractalBean... predecessors) {
        this.predecessor = Lists.newArrayList(predecessors);
        return this;
    }

    public List<FractalBean> getPredecessor() {
        return predecessor;
    }

    public List<FractalBean> getSuccessors() {
        return successors;
    }

    public FractalRule setSuccessors(final FractalBean... successors) {
        this.successors = Lists.newArrayList(successors);
        return this;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("rule", this.getRuleAsString())
                      .toString();
    }

    private String getRuleAsString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("( ");
        for (final FractalBean bean : this.predecessor) {
            builder.append(bean.getSymbol());
        }
        builder.append(" -> ");
        for (final FractalBean bean : this.successors) {
            builder.append(bean.getSymbol());
        }
        builder.append(" )");

        return builder.toString();
    }

    public boolean isPredecessor(final FractalBean var) {
        return this.predecessor.contains(var);
    }
}
