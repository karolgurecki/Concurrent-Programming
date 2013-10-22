package org.cp.monitor.threads;

import org.cp.monitor.core.CPLock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public interface MonitorRunnable extends Runnable {

    List<CPLock> lockA = new ArrayList<>();

    List<CPLock> lockB = new ArrayList<>();
}
