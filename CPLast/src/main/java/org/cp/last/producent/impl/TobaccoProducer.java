package org.cp.last.producent.impl;

import org.cp.last.producent.AbstractProducer;
import org.cp.last.products.Tobacco;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class TobaccoProducer extends AbstractProducer {

    public TobaccoProducer(Integer sleepAmount) throws Exception {
        super(Tobacco.class, sleepAmount);
    }
}
