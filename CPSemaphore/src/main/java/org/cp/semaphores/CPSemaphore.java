package org.cp.semaphores;

import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.cp.semaphores.core.FMutex;
import org.cp.semaphores.fractal.FractalConfiguration;
import org.cp.semaphores.fractal.beans.FractalVariable;
import org.cp.semaphores.threads.CFPSemaphore;
import org.cp.semaphores.threads.DFPSemaphore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class CPSemaphore {
    private static final Logger LOGGER = Logger.getLogger(CPSemaphore.class);
    private FractalConfiguration fractalConfiguration;
    private Set<Runnable>        threadPool;

    public static void main(final String[] args) throws Exception {
        LOGGER.info(String.format("%s is starting...", CPSemaphore.class.getSimpleName()));
        {
            final CPSemaphore semaphore = new CPSemaphore();
            semaphore.initFromCmd(Arrays.asList(args));
            semaphore.run();
        }
        LOGGER.info(String.format("%s has finished...", CPSemaphore.class.getSimpleName()));
    }

    private void run() {
        final ThreadGroup threadGroup = new ThreadGroup(this.getClass().getName());
        for (final Runnable runnable : this.threadPool) {
            final Thread thread = new Thread(threadGroup, runnable);
            thread.setName(runnable.getClass().getName());
            thread.start();

            LOGGER.info(String.format("Bootstrapped with thread=%s", thread));
        }
    }

    private void initFromCmd(final List<String> args) {
        if (args.contains("-p")) {
            final int pIndex = args.indexOf("-p");
            if (pIndex + 1 <= (args.size() - 1)) {
                final String pathToProperties = args.get(pIndex + 1);
                final Properties properties = new Properties();
                try {
                    properties.load(new FileInputStream(new File(pathToProperties)));
                    {
                        this.fractalConfiguration = this.initFractalConfiguration(properties);
                        this.threadPool = this.initThreadPool(properties);
                    }
                    LOGGER.debug(String
                            .format("Ready with:\n\t-> fractalConfiguration=%s\n\t->threadPool=%s", this.fractalConfiguration, this.threadPool));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("Failed to read properties file, missing path to properties file");
            }
        } else {
            throw new RuntimeException("Failed to read properties file, missing cmd argument -p");
        }
    }

    private Set<Runnable> initThreadPool(final Properties properties) {
        final Set<Runnable> runnables = Sets.newHashSet();
        final Queue<FractalVariable> arrayLifoQueue = Collections.asLifoQueue(new ArrayDeque<FractalVariable>());
        final Semaphore semaphore = new FMutex();
        final Integer rounds = Integer.valueOf(properties.getProperty("fractal.iterations"));

        runnables.add(new CFPSemaphore(this.fractalConfiguration).setSemaphore(semaphore)
                                                                 .setSharedResource(arrayLifoQueue)
                                                                 .setRounds(rounds));
        runnables.add(new DFPSemaphore().setSemaphore(semaphore)
                                        .setSharedResource(arrayLifoQueue)
                                        .setRounds(rounds));

        return runnables;
    }

    private FractalConfiguration initFractalConfiguration(final Properties properties) {
        return FractalConfiguration.newFractalConfiguration(
                properties.getProperty("fractal.axiom").trim(),
                properties.getProperty("fractal.constants").trim().split(","),
                properties.getProperty("fractal.rules").trim().split(",")
        );
    }
}