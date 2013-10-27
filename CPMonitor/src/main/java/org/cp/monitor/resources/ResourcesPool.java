package org.cp.monitor.resources;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class ResourcesPool<R extends Resource> {
    private final Lock lock = new ReentrantLock();
    private final Condition poolAvailable = lock.newCondition();
    private int num_resources;
    private final int max_num_resources;
    private final List<R> resourcePool = new ArrayList<>();
    private final Constructor<R> resourceConstructor;


    private R createResource(final int number) throws Exception {
        R resource;

        resource = resourceConstructor.newInstance(number);

        if (resource == null)
            throw new Exception(String.format("%s number %d can't be created",
                    resourceConstructor.getClass().getSimpleName(), number));

        return resource;
    }

    public ResourcesPool(Class<R> resourceClass, int num_resource_pools) throws Exception {
        this.resourceConstructor = resourceClass.getDeclaredConstructor(Integer.class);
        this.max_num_resources = num_resource_pools;
        this.num_resources = max_num_resources;

        for (int i = 0; i < max_num_resources; i++)
            resourcePool.add(createResource(i));
    }

    public R acquireResource() throws Exception {
        lock.lock();
        try {
            while (true) {
                while (num_resources <= 0)
                    poolAvailable.await();

                --num_resources;

                int i = 0;
                while (resourcePool.get(i).isLocked() && i <= resourcePool.size())
                    i++;

                //if (i != resourcePool.size()) {
                    resourcePool.get(i).setLocked(true);
                    return resourcePool.get(i);
                //}
            }
        } finally {
            lock.unlock();
        }
    }

    public void releaseResource(R resource) {
        lock.lock();
        try {
            // check to ensure release does not occur before acquire
            if (num_resources >= max_num_resources)
                return;

            int resourceIndex = resource.getNumber();
            ++num_resources;

            resourcePool.get(resourceIndex).setLocked(false);
            poolAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

}
