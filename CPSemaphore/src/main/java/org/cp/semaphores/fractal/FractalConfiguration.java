package org.cp.semaphores.fractal;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import org.cp.semaphores.fractal.beans.FractalBean;
import org.cp.semaphores.fractal.beans.FractalConstant;
import org.cp.semaphores.fractal.rule.FractalRule;

import java.util.List;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalConfiguration {
    private List<FractalConstant> constants;
    private FractalConstant       axiom;
    private List<FractalRule>     rules;

    protected FractalConfiguration() {
        this.constants = Lists.newArrayList();
        this.rules = Lists.newArrayList();
    }

    public static FractalConfiguration newFractalConfiguration(final String axiom, final String[] constants, final String[] rules) {
        final FractalConfiguration fractalConfiguration = new FractalConfiguration();

        fractalConfiguration.axiom = new FractalConstant(axiom);
        for (final String rawConstant : constants) {
            fractalConfiguration.constants.add(new FractalConstant(rawConstant));
        }
        for (String rawRule : rules) {
            rawRule = rawRule.replaceAll("\\(|\\)", "");
            final String[] unparsedRules = rawRule.split(">");
            fractalConfiguration.rules.add(
                    new FractalRule()
                            .setPredecessors(FractalConfiguration.getBeanFromString(unparsedRules[0]))
                            .setSuccessors(FractalConfiguration.getBeanFromString(unparsedRules[1]))
            );
        }

        return fractalConfiguration;
    }

    private static FractalBean[] getBeanFromString(final String raw) {
        final FractalBean[] fractalBeans = new FractalBean[raw.length()];
        final char[] charArray = raw.toCharArray();
        for (int i = 0 ; i < charArray.length ; i++) {
            final Character character = charArray[i];
            fractalBeans[i] = new FractalConstant(String.valueOf(character));
        }
        return fractalBeans;
    }

    public List<FractalConstant> getConstants() {
        return constants;
    }

    public FractalConstant getAxiom() {
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
