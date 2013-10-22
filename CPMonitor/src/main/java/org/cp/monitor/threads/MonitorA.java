package org.cp.monitor.threads;

import org.apache.log4j.Logger;
import org.cp.monitor.resources.ResourceA;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class MonitorA extends MonitorRunnable {

    protected Logger logger = Logger.getLogger(MonitorA.class);

    ResourceA resourceA;

    @Override
    public void run() {
        while (roundNumber <= roundsAmount) {
            try {
                logger.info("Trying to acquire resource");
                resourceA = RESOURCE_A_RESOURCES_POOL.acquireResource();

                logger.info(String.format("Round %d: I acquire %s", roundNumber, resourceA));

                RESOURCE_A_RESOURCES_POOL.releaseResource(resourceA);
                logger.info("Released resource");
            } catch (Exception e) {
                logger.error(e);
            }
            roundNumber++;
        }
    }
}
