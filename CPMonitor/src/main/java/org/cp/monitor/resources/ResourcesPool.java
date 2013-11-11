package org.cp.monitor.resources;

import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class ResourcesPool<R extends Resource> {
    private final Lock      lock          = new ReentrantLock();
    private final Condition poolAvailable = lock.newCondition();
    private final int max_num_resources;
    private final ArrayDeque<R> resourcePool = new ArrayDeque<>();
    private final Constructor<R> resourceConstructor;
    private       int            num_resources;


    public ResourcesPool(Class<R> resourceClass, int num_resource_pools) throws Exception {
        this.resourceConstructor = resourceClass.getDeclaredConstructor(Integer.class);
        this.max_num_resources = num_resource_pools;
        this.num_resources = max_num_resources;

        for (int i = 0 ; i < max_num_resources ; i++) {
            resourcePool.add(createResource(i));
        }
    }

    private R createResource(final int number) throws Exception {
        R resource;

        resource = resourceConstructor.newInstance(number);

        if (resource == null) {
            throw new Exception(String.format("%s number %d can't be created",
                    resourceConstructor.getClass().getSimpleName(), number));
        }

        return resource;
    }

    public R acquireResource() throws Exception {
        lock.lock();
        try {
            while (num_resources <= 0) {
                poolAvailable.await();
            }

            --num_resources;

            R r = resourcePool.getFirst();

            r.setLocked(true);
            return r;

        } finally {
            lock.unlock();
        }
    }

    public void releaseResource(R resource) {
        lock.lock();
        try {
            // check to ensure release does not occur before acquire
            if (num_resources >= max_num_resources) {
                return;
            }

            int resourceIndex = resource.getNumber();
            ++num_resources;

            resource.setLocked(false);
            resourcePool.addLast(resource);

            poolAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

}