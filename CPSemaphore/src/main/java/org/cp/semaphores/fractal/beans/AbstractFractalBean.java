package org.cp.semaphores.fractal.beans;

import com.google.common.base.Objects;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class AbstractFractalBean
        implements FractalBean {
    protected String symbol;

    protected AbstractFractalBean(final String symbol) {
        this.symbol = new String(symbol.getBytes());
    }

    @Override
    public String getSymbol() {
        return this.symbol;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(symbol)
                      .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        AbstractFractalBean that = (AbstractFractalBean) o;

        return Objects.equal(this.symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(symbol);
    }
}
