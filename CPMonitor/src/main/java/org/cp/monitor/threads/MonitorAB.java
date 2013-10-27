package org.cp.monitor.threads;

import org.apache.log4j.Logger;
import org.cp.monitor.resources.ResourceA;
import org.cp.monitor.resources.ResourceB;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class MonitorAB extends MonitorRunnable {

    protected Logger logger=Logger.getLogger(MonitorAB.class);

    ResourceA resourceA;

    ResourceB resourceB;

    public MonitorAB(final Integer number) {
        super(number);
    }

    @Override
    public void run() {
        while (roundNumber <= roundsAmount) {
            try {
                logger.info(String.format("%d - Trying to acquire resource",number));

                resourceA = RESOURCE_A_RESOURCES_POOL.acquireResource();
                resourceB = RESOURCE_B_RESOURCES_POOL.acquireResource();

                logger.info(String.format("%d - Round %d: I acquire %s and %s",number, roundNumber, resourceA, resourceB));

                RESOURCE_A_RESOURCES_POOL.releaseResource(resourceA);
                RESOURCE_B_RESOURCES_POOL.releaseResource(resourceB);

                logger.info(String.format("%d - Released resource. Go to sleep",number));

                Thread.sleep(sleepTime);
            } catch (Exception e) {
                logger.error(e);
            }
            roundNumber++;
        }
    }
}
