package org.cp.last.products;

import org.cp.monitor.resources.Resource;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class Cigarette extends Resource {
    private static int totalNumber = 0;

    public Cigarette() {
        super(totalNumber);

        totalNumber++;
    }

    public Cigarette(Integer number) {
        super(totalNumber);

        totalNumber++;
    }
}
