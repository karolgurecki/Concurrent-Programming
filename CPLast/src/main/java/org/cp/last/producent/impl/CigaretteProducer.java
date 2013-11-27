package org.cp.last.producent.impl;

import org.cp.last.producent.AbstractProducer;
import org.cp.last.products.Cigarette;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class CigaretteProducer extends AbstractProducer {

    public CigaretteProducer(Integer warehouseCapacity, Integer howMuchToProduce, Integer sleepAmount) throws Exception {
        super(Cigarette.class, warehouseCapacity, howMuchToProduce, sleepAmount);
    }


}
