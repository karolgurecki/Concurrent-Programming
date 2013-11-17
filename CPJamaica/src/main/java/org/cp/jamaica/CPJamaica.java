package org.cp.jamaica;

import org.apache.log4j.Logger;
import org.cp.jamaica.client.impl.ClientA;
import org.cp.jamaica.client.impl.ClientAB;
import org.cp.jamaica.client.impl.ClientB;
import org.cp.jamaica.server.RServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class CPJamaica {
    private static final Logger LOGGER = Logger.getLogger(CPJamaica.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info(String.format("%s is starting...", CPJamaica.class.getSimpleName()));
        {
            final CPJamaica s = new CPJamaica();
            s.run();
        }
        LOGGER.info(String.format("%s has finished...", CPJamaica.class.getSimpleName()));
    }

    private void run() throws InterruptedException {
        final ExecutorService executorService = Executors.newCachedThreadPool();
        final RServer rServer = new RServer();
        executorService.submit(rServer);
        for (int i = 0 ; i < 2 ; i++) {
            executorService.submit(new ClientA(rServer));
            executorService.submit(new ClientB(rServer));
            executorService.submit(new ClientAB(rServer));
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }
}
