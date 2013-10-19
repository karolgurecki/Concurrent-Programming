package org.cp.semaphores.fractal;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.cp.semaphores.fractal.beans.FractalBean;
import org.cp.semaphores.fractal.beans.FractalConstant;
import org.cp.semaphores.fractal.beans.FractalVariable;
import org.cp.semaphores.fractal.path.FractalPath;
import org.cp.semaphores.fractal.rule.FractalRule;

import java.util.List;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalConfiguration {
    private final List<FractalVariable> variables;
    private final List<FractalConstant> constants;
    private final List<FractalRule>     rules;
    private       FractalPath           axiom;

    protected FractalConfiguration() {
        this.constants = Lists.newArrayList();
        this.variables = Lists.newArrayList();
        this.rules = Lists.newArrayList();
    }

    public static FractalConfiguration newFractalConfiguration(
            final String axiom,
            final String[] variables,
            final String[] constants,
            final String[] rules
    ) {
        final FractalConfiguration fractalConfiguration = new FractalConfiguration();


        for (final String rawConstant : constants) {
            fractalConfiguration.constants.add(new FractalConstant(rawConstant));
        }

        for (final String rawVariable : variables) {
            fractalConfiguration.variables.add(new FractalVariable(rawVariable));
        }

        fractalConfiguration.axiom = fractalConfiguration.getPathFromString(axiom);

        for (String rawRule : rules) {
            rawRule = rawRule.replaceAll("\\(|\\)", "");
            final String[] predecessorToSuccessor = rawRule.split(">");
            fractalConfiguration.rules.add(
                    new FractalRule()
                            .setPredecessors(fractalConfiguration.getBeanFromString(predecessorToSuccessor[0]))
                            .setSuccessors(fractalConfiguration.getBeanFromString(predecessorToSuccessor[1]))
            );
        }
        return fractalConfiguration;
    }

    public FractalPath getPathFromString(final String var) {
        return new FractalPath(this.getBeanFromString(var));
    }

    public FractalBean[] getBeanFromString(final String var) {
        final FractalBean[] fractalBeans = new FractalBean[var.length()];
        for (int i = 0 ; i < var.length() ; i++) {
            final String examinedCharacter = String.valueOf(var.charAt(i));
            if (this.isConstant(examinedCharacter)) {
                fractalBeans[i] = new FractalConstant(examinedCharacter);
            } else if (this.isVariable(examinedCharacter)) {
                fractalBeans[i] = new FractalVariable(examinedCharacter);
            }
        }
        return fractalBeans;
    }

    public boolean isVariable(final String var) {
        return !FluentIterable
                .from(this.variables)
                .filter(new Predicate<FractalVariable>() {
                    @Override
                    public boolean apply(final FractalVariable input) {
                        return input.getSymbol().equalsIgnoreCase(var);
                    }
                })
                .toList()
                .isEmpty();
    }

    public boolean isConstant(final String var) {
        return !FluentIterable
                .from(this.constants)
                .filter(new Predicate<FractalConstant>() {
                    @Override
                    public boolean apply(final FractalConstant input) {
                        return input.getSymbol().equalsIgnoreCase(var);
                    }
                })
                .toList()
                .isEmpty();
    }

    public boolean isConstant(final FractalBean bean) {
        return this.isConstant(bean.getSymbol());
    }

    public boolean isVariable(final FractalBean bean) {
        return this.isVariable(bean.getSymbol());
    }

    public List<FractalConstant> getConstants() {
        return constants;
    }

    public FractalPath getAxiom() {
        return axiom;
    }

    public List<FractalRule> getRules() {
        return rules;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .add("constants", constants)
                      .add("axiom", axiom)
                      .add("rules", rules)
                      .toString();
    }
}
