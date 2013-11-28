package org.cp.last.producent.impl;

import org.cp.last.producent.AbstractProducer;
import org.cp.last.products.Matches;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class MatchesProducer extends AbstractProducer {

    public MatchesProducer(Integer sleepAmount) throws Exception {
        super(Matches.class, sleepAmount);
    }
}
