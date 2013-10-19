package org.cp.semaphores.fractal.beans;

import com.google.common.base.Objects;

/**
 * {@code FractalVariable} is an mutable {@link org.cp.semaphores.fractal.beans.FractalBean}
 *
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalVariable
        extends FractalConstant {

    public FractalVariable(final String var) {
        super(var);
    }

    public FractalVariable setSymbol(final String symbol) {
        this.symbol = new String(symbol.getBytes());
        return this;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(symbol)
                      .toString();
    }
}
