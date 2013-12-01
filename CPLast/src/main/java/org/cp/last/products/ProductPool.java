package org.cp.last.products;

import org.apache.log4j.Logger;
import org.cp.monitor.resources.Resource;

import java.lang.reflect.Constructor;
import java.util.ArrayDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by <a href="mailto:karolgurecki@gmail.com">Karol Górecki</a> on 28.11.13.
 */
public class ProductPool<P extends Resource> {

    protected Lock lock = new ReentrantLock();
    protected final Condition poolAvailable = lock.newCondition();
    protected int max_num_products;
    protected final ArrayDeque<P> productPool = new ArrayDeque<>();
    protected Constructor productConstructor;
    protected int num_products;

    protected Logger logger = Logger.getLogger(this.getClass());

    public ProductPool(Class productClass, int num_product_pools) throws Exception {
        super();
        this.productConstructor = productClass.getConstructor();
        this.max_num_products = num_product_pools;
        this.num_products = 0;
    }

    public P acquireProduct() throws Exception {
        lock.lock();
        try {
            logger.debug(String.format("Number of available %s is %d", productConstructor.getDeclaringClass().getSimpleName(), num_products));
            while (num_products <= 0) {
                poolAvailable.await();
            }

            --num_products;

            P r = productPool.getFirst();
            productPool.removeFirst();
            r.setLocked(true);
            return r;

        } finally {
            lock.unlock();
        }
    }

    public void produceProduct(P product) {
        lock.lock();
        try {
            // check to ensure release does not occur before acquire
            if (num_products >= max_num_products) {
                return;
            }

            //int productIndex = product.getNumber();
            ++num_products;

            product.setLocked(false);
            productPool.addLast(product);

            poolAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return String.format("ResourcesPool{max_num_products= %d,productConstructor= %s, num_products= %d}",
                max_num_products, productConstructor, num_products);
    }

    public boolean isWarehouseFull() {
        return num_products == max_num_products;
    }


}
