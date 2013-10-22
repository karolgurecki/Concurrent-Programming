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

    @Override
    public void run() {
        while (roundNumber <= roundsAmount) {
            try {
                logger.info("Trying to acquire resource");

                resourceA = RESOURCE_A_RESOURCES_POOL.acquireResource();
                resourceB = RESOURCE_B_RESOURCES_POOL.acquireResource();

                logger.info(String.format("Round %d: I acquire %s and %s", roundNumber, resourceA, resourceB));

                RESOURCE_A_RESOURCES_POOL.releaseResource(resourceA);
                RESOURCE_B_RESOURCES_POOL.releaseResource(resourceB);

                logger.info("Released resource");
            } catch (Exception e) {
                logger.error(e);
            }
            roundNumber++;
        }
    }
}
