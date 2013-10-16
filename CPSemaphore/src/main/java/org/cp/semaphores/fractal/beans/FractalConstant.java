package org.cp.semaphores.fractal.beans;

import com.google.common.base.Objects;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalConstant
        extends AbstractFractalBean {

    public FractalConstant(final String axiom) {
        this.symbol = axiom;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(symbol)
                      .toString();
    }
}
