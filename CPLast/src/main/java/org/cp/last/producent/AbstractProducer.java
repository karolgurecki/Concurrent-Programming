package org.cp.last.producent;

import org.apache.log4j.Logger;
import org.cp.last.products.Cigarette;
import org.cp.last.products.Matches;
import org.cp.last.products.ProductPool;
import org.cp.last.products.Tobacco;
import org.cp.monitor.resources.Resource;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class AbstractProducer implements Runnable {

    protected Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());

    public static ProductPool<Tobacco> tobaccoPoll;

    public static ProductPool<Matches> matchesPool;

    public static ProductPool<Cigarette> cigarettePool;

    protected Queue<Tobacco> myTobacco;

    protected Queue<Matches> myMatches;

    protected Queue<Cigarette> myCigarettes;

    protected Class product;

    protected static int sleepAmount;

    public static int stock;

    protected int produced;

    public AbstractProducer(Class _product, int _sleepAmount) throws Exception {

        sleepAmount = _sleepAmount;
        this.produced = 0;
        product = _product;

        myCigarettes = new LinkedList<>();
        myMatches = new LinkedList<>();
        myTobacco = new LinkedList<>();

        if (product != null) {

            if (Tobacco.class.equals(product)) {
                myTobacco.add(new Tobacco());
            } else if (Matches.class.equals(product)) {
                myMatches.add(new Matches());
            } else {
                myCigarettes.add(new Cigarette());
            }
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                produce();
                acquire();
                LOGGER.debug(String.format("myTobacco.size %d, myCigarettes.size %d, myMatches.size %d",
                        myTobacco.size(), myCigarettes.size(), myMatches.size()));
                fold();
                LOGGER.info("Go to sleep");
                Thread.sleep(sleepAmount);
            }
        } catch (Exception e) {
            LOGGER.error(exceptionToString(e));
            System.exit(-1);
        }
    }

    private String exceptionToString(Exception e) {
        StringBuilder builder = new StringBuilder();
        builder.append(e.getMessage());

        for (StackTraceElement element : e.getStackTrace()) {
            builder.append("\n");
            builder.append(element);
        }

        return builder.toString();
    }

    private void produce() throws IllegalAccessException, InstantiationException {
        Resource productTemp;

        LOGGER.info(String.format("Producing a %s", product.getSimpleName()));
        for (int i = 0; i < stock; i++) {
            if (product != null) {
                if (Tobacco.class.equals(product)) {
                    if (!tobaccoPoll.isWarehouseFull()) {
                        productTemp = (Resource) this.product.newInstance();
                        tobaccoPoll.produceProduct((Tobacco) productTemp);
                    }
                } else if (Matches.class.equals(product)) {
                    if (!matchesPool.isWarehouseFull()) {
                        productTemp = (Resource) this.product.newInstance();
                        matchesPool.produceProduct((Matches) productTemp);
                    }
                } else {
                    if (!cigarettePool.isWarehouseFull()) {
                        productTemp = (Resource) this.product.newInstance();
                        cigarettePool.produceProduct((Cigarette) productTemp);
                    }
                }
            }
        }
        LOGGER.info(String.format("Produced %s", product.getSimpleName()));
    }

    private void acquire() throws Exception {
        if (!Tobacco.class.equals(product)) {
            LOGGER.info("Trying to acquire a tobacco");
            myTobacco.add(tobaccoPoll.acquireProduct());
            LOGGER.info(String.format("I acquired %s", myTobacco.peek()));
        }
        if (!Matches.class.equals(product)) {
            LOGGER.info("Trying to acquire a matches");
            myMatches.add(matchesPool.acquireProduct());
            LOGGER.info(String.format("I acquired %s", myMatches.peek()));
        }
        if (!Cigarette.class.equals(product)) {
            LOGGER.info("Trying to acquire a cigarette");
            myCigarettes.add(cigarettePool.acquireProduct());
            LOGGER.info(String.format("I acquired %s", myCigarettes.peek()));
        }
    }

    private void fold() {
        if (!myCigarettes.isEmpty() && !myTobacco.isEmpty() && !myMatches.isEmpty()) {
            LOGGER.info(String.format("Start smoking %d with\n%s\n%s\n%s", produced++, myCigarettes.poll(), myTobacco.poll(), myMatches.poll()));

            if (product != null) {
                if (Tobacco.class.equals(product)) {
                    myTobacco.add(new Tobacco());
                } else if (Matches.class.equals(product)) {
                    myMatches.add(new Matches());
                } else {
                    myCigarettes.add(new Cigarette());
                }
            }
        }
    }
}
