package org.cp.jamaica.junction;

import org.cp.jamaica.task.ResourceProcessor;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public interface Junction {
    boolean accept(final ResourceProcessor<?> runnable);
}
