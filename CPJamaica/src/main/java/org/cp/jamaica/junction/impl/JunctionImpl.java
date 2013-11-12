package org.cp.jamaica.junction.impl;

import org.cp.jamaica.junction.Junction;
import org.cp.jamaica.task.ResourceProcessor;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class JunctionImpl
        implements Junction {
    private Deque<ResourceProcessor<?>> buffer = new ConcurrentLinkedDeque<>();

    @Override
    public boolean accept(final ResourceProcessor<?> runnable) {
        return false;
    }

}
