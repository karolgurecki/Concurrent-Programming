package org.cp.last.producent;

import org.apache.log4j.Logger;
import org.cp.last.products.Cigarette;
import org.cp.last.products.Matches;
import org.cp.last.products.Tobacco;
import org.cp.monitor.resources.Resource;
import org.cp.monitor.resources.ResourcesPool;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class AbstractProducer implements Runnable {

    protected Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());

    protected static ResourcesPool<Tobacco> tobaccoPoll;

    protected static ResourcesPool<Matches> matchesPool;

    protected static ResourcesPool<Cigarette> cigarettePool;

    protected Queue<Tobacco> myToccaco;

    protected Queue<Matches> myMatches;

    protected Queue<Cigarette> myCigarettes;

    protected ResourcesPool productPool;

    protected Class<Resource> product;

    protected static int howMuchToProduce;

    protected static int sleepAmount;

    protected int produced;
    protected Random RANDOM = new Random();

    public AbstractProducer(Class product, int warehouseCapacity, int howMuchToProduce, int sleepAmount) throws Exception {
        tobaccoPoll = new ResourcesPool<>(Tobacco.class, warehouseCapacity);
        matchesPool = new ResourcesPool<>(Matches.class, warehouseCapacity);
        cigarettePool = new ResourcesPool<>(Cigarette.class, warehouseCapacity);

        if (Tobacco.class.equals(product)) {
            productPool = tobaccoPoll;
        } else if (Matches.class.equals(product)) {
            productPool = matchesPool;
        } else {
            productPool = cigarettePool;
        }

        this.sleepAmount = sleepAmount;
        this.howMuchToProduce = howMuchToProduce;
        this.produced = 0;
        this.product = product;

        myCigarettes = new LinkedList<>();
        myMatches = new LinkedList<>();
        myToccaco = new LinkedList<>();
    }

    @Override
    public void run() {
        LOGGER.info(String.format("Producing a %s", product.getSimpleName()));
        Resource productTemp;
        try {
            while (produced < howMuchToProduce) {
                //productTemp = null;
                productTemp = this.product.newInstance();

                if (product != null) {
                    productPool.releaseResource(productTemp);
                }

                switch (RANDOM.nextInt(2)) {
                    case 0:
                        myToccaco.add(tobaccoPoll.acquireResource());
                        LOGGER.info(String.format("I acquired tobacco %s", myToccaco.peek()));
                        break;
                    case 1:
                        myMatches.add(matchesPool.acquireResource());
                        LOGGER.info(String.format("I acquired matches %s", myMatches.peek()));
                        break;
                    default:
                        myCigarettes.add(cigarettePool.acquireResource());
                        LOGGER.info(String.format("I acquired cigarette %s", myCigarettes.peek()));
                        break;

                }

                if (!myCigarettes.isEmpty() && !myToccaco.isEmpty() && !myMatches.isEmpty()) {
                    LOGGER.info(String.format("Start smoking with\n%s\n%s\n%s", myCigarettes.poll(), myToccaco.poll(), myMatches.poll()));
                    produced++;
                }


                LOGGER.info("Go to sleep");
                Thread.sleep(sleepAmount);

            }
        } catch (Exception e) {
            LOGGER.error(e);
            System.exit(-1);
        }
    }
}
