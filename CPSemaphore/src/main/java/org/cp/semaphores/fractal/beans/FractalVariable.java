package org.cp.semaphores.fractal.beans;

import com.google.common.base.Objects;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalVariable
        extends FractalConstant {

    public FractalVariable(final String symbol) {
        super(symbol);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(symbol)
                      .toString();
    }
}
