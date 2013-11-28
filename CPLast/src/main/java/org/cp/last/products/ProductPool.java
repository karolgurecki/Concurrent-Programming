package org.cp.last.products;

import org.cp.monitor.resources.Resource;
import org.cp.monitor.resources.ResourcesPool;

/**
 * Created by <a href="mailto:karolgurecki@gmail.com">Karol Górecki</a> on 28.11.13.
 */
public class ProductPool<R extends Resource> extends ResourcesPool {

    public ProductPool(Class resourceClass, int num_resource_pools) throws Exception {
        super();
        this.resourceConstructor = resourceClass.getConstructor();
        this.max_num_resources = num_resource_pools;
        this.num_resources = 0;
    }
}
