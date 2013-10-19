package org.cp.semaphores.fractal.beans;

import com.google.common.base.Objects;

/**
 * {@code FractalConstant} is an immutable {@link org.cp.semaphores.fractal.beans.FractalBean}
 *
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class FractalConstant
        extends AbstractFractalBean {

    public FractalConstant(final String var) {
        super(var);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(symbol)
                      .toString();
    }
}
