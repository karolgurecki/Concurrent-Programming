package org.cp.jamaica.client.impl;

import com.google.common.base.Objects;
import org.apache.log4j.Logger;
import org.cp.jamaica.client.Client;
import org.cp.jamaica.server.RServer;
import org.cp.monitor.resources.Resource;

import java.util.Arrays;

/**
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
abstract class AbstractClient
        implements Client {
    private static int COUNTER = 0;
    protected final RServer server;
    private final Logger LOGGER = Logger.getLogger(AbstractClient.this.getClass());
    protected String receiverCode;
    protected int     number = COUNTER++;
    private   boolean overMe = false;

    protected AbstractClient(final RServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this.server) {
                if (this.overMe) {
                    LOGGER.info("No resources left...closing..");
                    return;
                }
                final boolean accepted = this.server.call(this.receiverCode, this);
                if (accepted) {
                    LOGGER.info("I have been accepted");
//                    this.server.notifyAll();
                } else {
                    LOGGER.warn("Need to wait");
//                    try {
//                        this.server.wait();
//                    } catch (InterruptedException e) {
//                        AbstractClient.this.LOGGER.error("Error when waiting", e);
//                        return;
//                    }
                }
            }
        }
    }

    @Override
    public Client setReceiverCode(final String receiverCode) {
        this.receiverCode = receiverCode;
        return this;
    }

    @Override
    public synchronized void msg(final Resource... resources) {
        if (resources == null || resources.length == 0) {
            this.overMe = true;
        }
        LOGGER.info(Arrays.toString(resources));
        this.server.notifyAll();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(receiverCode, number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractClient that = (AbstractClient) o;

        return Objects.equal(this.receiverCode, that.receiverCode) &&
                Objects.equal(this.number, that.number);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                      .addValue(receiverCode)
                      .addValue(number)
                      .toString();
    }
}
