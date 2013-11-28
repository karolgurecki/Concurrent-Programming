package org.cp.last.products;

import org.cp.monitor.resources.Resource;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class Matches extends Resource {
    private static int totalNumber = 0;

    public Matches() {
        super(++totalNumber);
    }
}