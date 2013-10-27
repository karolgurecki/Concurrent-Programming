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

    public MonitorA(final Integer number) {
        super(number);
    }

    @Override
    public void run() {
        while (roundNumber <= roundsAmount) {
            try {
                logger.info(String.format("%d - Trying to acquire resource",number));
                resourceA = RESOURCE_A_RESOURCES_POOL.acquireResource();

                logger.info(String.format("%d - Round %d: I acquire %s",number, roundNumber, resourceA));

                RESOURCE_A_RESOURCES_POOL.releaseResource(resourceA);

                logger.info(String.format("%d - Released resource. Go to sleep",number));

                Thread.sleep(sleepTime);
            } catch (Exception e) {
                logger.error(e);
            }
            roundNumber++;
        }
    }
}
