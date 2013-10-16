package org.cp.semaphores.threads;

import com.google.common.base.Objects;

import java.util.Iterator;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class RoundsFlag
        implements Iterator<Integer> {
    private final int threshold;
    private       int currentCount;

    public RoundsFlag(final int threshold) {
        this.currentCount = 0;
        this.threshold = threshold;
    }

    @Override
    public boolean hasNext() {
        return this.currentCount != this.threshold;
    }

    @Override
    public Integer next() {
        return ++this.currentCount;
    }

    @Override
    public void remove() {
        --this.currentCount;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(threshold)
                      .addValue(currentCount)
                      .toString();
    }

    public int getRound() {
        return this.currentCount;
    }
}
