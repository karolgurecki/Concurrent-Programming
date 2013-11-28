package org.cp.last.producent;

import org.apache.log4j.Logger;
import org.cp.last.products.Cigarette;
import org.cp.last.products.Matches;
import org.cp.last.products.ProductPool;
import org.cp.last.products.Tobacco;
import org.cp.monitor.resources.Resource;

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
    protected Random RANDOM = new Random();

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

        Resource productTemp;
        try {
            while (true) {
                LOGGER.info(String.format("Producing a %s", product.getSimpleName()));
                //productTemp = null;
                for (int i = 0; i < stock; i++) {
                    productTemp = (Resource) this.product.newInstance();

                    if (product != null) {
                        if (Tobacco.class.equals(product)) {
                            tobaccoPoll.produceProduct((Tobacco) productTemp);
                        } else if (Matches.class.equals(product)) {
                            matchesPool.produceProduct((Matches) productTemp);
                        } else {
                            cigarettePool.produceProduct((Cigarette) productTemp);
                        }
                        LOGGER.info(String.format("Produced %s", productTemp.getClass().getSimpleName()));
                    }
                }
                boolean acquired = false;
                while (!acquired) {
                    switch (RANDOM.nextInt(3)) {
                        case 0:
                            if (!Tobacco.class.equals(product)) {
                                LOGGER.info("Trying to acquire a tobacco");
                                myTobacco.add(tobaccoPoll.acquireProduct());
                                LOGGER.info(String.format("I acquired %s", myTobacco.peek()));
                                acquired = true;
                            }
                            break;
                        case 1:
                            if (!Matches.class.equals(product)) {
                                LOGGER.info("Trying to acquire a matches");
                                myMatches.add(matchesPool.acquireProduct());
                                LOGGER.info(String.format("I acquired %s", myMatches.peek()));
                                acquired = true;
                            }
                            break;
                        default:
                            if (!Cigarette.class.equals(product)) {
                                LOGGER.info("Trying to acquire a cigarette");
                                myCigarettes.add(cigarettePool.acquireProduct());
                                LOGGER.info(String.format("I acquired %s", myCigarettes.peek()));
                                acquired = true;
                            }
                            break;
                    }
                }

                LOGGER.debug(String.format("myTobacco.size %d, myCigarettes.size %d, myMatches.size %d",
                        myTobacco.size(), myCigarettes.size(), myMatches.size()));

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
}
