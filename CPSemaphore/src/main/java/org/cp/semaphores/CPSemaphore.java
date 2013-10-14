package org.cp.semaphores;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class CPSemaphore {
    private static final Logger LOGGER = Logger.getLogger(CPSemaphore.class);

    public static void main(final String[] args) throws Exception {
        LOGGER.info(String.format("%s is starting...", CPSemaphore.class.getSimpleName()));
        {
            final CPSemaphore semaphore = new CPSemaphore();
            semaphore.initFromCmd(Arrays.asList(args));
        }
        LOGGER.info(String.format("%s has finished...", CPSemaphore.class.getSimpleName()));
    }

    private void initFromCmd(final List<String> args) {
        if (args.contains("-p")) {
            final int pIndex = args.indexOf("-p");
            if (pIndex + 1 <= (args.size() - 1)) {
                final String pathToProperties = args.get(pIndex + 1);
                final Properties properties = new Properties();
                try {
                    properties.load(new FileInputStream(new File(pathToProperties)));
                    LOGGER.debug(properties);
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
}