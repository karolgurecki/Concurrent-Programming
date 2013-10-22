package org.cp.monitor.threads;

import org.cp.monitor.resources.ResourceA;
import org.cp.monitor.resources.ResourceB;
import org.cp.monitor.resources.ResourcesPool;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public abstract class MonitorRunnable implements Runnable {



    public static ResourcesPool<ResourceA> RESOURCE_A_RESOURCES_POOL;

    public static ResourcesPool<ResourceB> RESOURCE_B_RESOURCES_POOL;

    public static int roundsAmount;

    protected int roundNumber=1;

    public MonitorRunnable(){

    }
}
