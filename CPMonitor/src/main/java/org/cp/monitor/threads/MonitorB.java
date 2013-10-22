package org.cp.monitor.threads;

import org.apache.log4j.Logger;
import org.cp.monitor.resources.ResourceB;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class MonitorB extends MonitorRunnable  {

    protected Logger logger=Logger.getLogger(MonitorB.class);

    ResourceB resourceB;

    public MonitorB(final Integer number) {
        super(number);
    }

    @Override
    public void run() {
        while (roundNumber <= roundsAmount) {
            try {
                logger.info(String.format("%d - Trying to acquire resource",number));

                resourceB = RESOURCE_B_RESOURCES_POOL.acquireResource();

                logger.info(String.format("%d - Round %d: I acquire %s",number,roundNumber,resourceB));

                RESOURCE_B_RESOURCES_POOL.releaseResource(resourceB);

                logger.info(String.format("%d - Released resource",number));

            } catch (Exception e) {
                logger.error(e);
            }
            roundNumber++;
        }

    }
}
