package org.cp.last;

import org.apache.log4j.Logger;
import org.cp.last.producent.AbstractProducer;
import org.cp.last.producent.impl.CigaretteProducer;
import org.cp.last.producent.impl.MatchesProducer;
import org.cp.last.producent.impl.TobaccoProducer;

import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Nappa
 * Version: 0.01
 * Since: 0.01
 */
public class Launcher {

    private static Logger LOGGER = Logger.getLogger(Launcher.class);

    private static List<AbstractProducer> producers = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        init(Arrays.asList(args));

        run();

    }

    private static void run() throws InterruptedException {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        for (final Runnable runnable : producers) {
            LOGGER.info(String.format(" Bootstrapped with thread=%s", executorService.submit(runnable)));
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }

    private static void init(List<String> args) throws Exception {
        if (args.contains("-p")) {
            int pIndex = args.indexOf("-p");

            if (pIndex + 1 < args.size()) {
                final String pathToProperties = args.get(pIndex + 1);
                final Properties properties = new Properties();

                try {
                    properties.load(new FileReader(pathToProperties));
                    LOGGER.info("Reading configuration");

                    int cigaretteProducerNumber = Integer.parseInt(properties.getProperty("cigarette.producer.amount"));
                    int matchesProducerNumber = Integer.parseInt(properties.getProperty("matches.producer.amount"));
                    int tobaccoProducerNumber = Integer.parseInt(properties.getProperty("tobacco.producer.amount"));
                    int warehouseCapacity = Integer.parseInt(properties.getProperty("warehouse.capacity"));
                    int amountToDo = Integer.parseInt(properties.getProperty("rounds.amount"));
                    int sleetAmount = Integer.parseInt(properties.getProperty("sleep.time"));

                    createThreads(TobaccoProducer.class.getName(), tobaccoProducerNumber, warehouseCapacity, amountToDo, sleetAmount);
                    createThreads(MatchesProducer.class.getName(), matchesProducerNumber, warehouseCapacity, amountToDo, sleetAmount);
                    createThreads(CigaretteProducer.class.getName(), cigaretteProducerNumber, warehouseCapacity, amountToDo, sleetAmount);

                    LOGGER.info("Program configured");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else {
                throw new RuntimeException("Failed to read properties file, missing path to properties file");
            }
        } else {
            throw new RuntimeException("Failed to read properties file, missing cmd argument -p");
        }

    }

    private static void createThreads(String strClazz, int numberOfThreads, int warehouseCapacity, int howMuchToProduce, int sleepAmount) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, ClassNotFoundException {

        Class clazz = Class.forName(strClazz);

        Constructor constructor = clazz.getConstructor(Integer.class, Integer.class, Integer.class);

        for (int i = 0; i < numberOfThreads; i++) {
            producers.add((AbstractProducer) constructor.newInstance(warehouseCapacity, howMuchToProduce, sleepAmount));
        }
    }
}
