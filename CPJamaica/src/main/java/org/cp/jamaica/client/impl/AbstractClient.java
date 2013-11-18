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
    public static final int TIMEOUT = 100;
    private static      int COUNTER = 0;
    protected final RServer server;
    private final Logger LOGGER = Logger.getLogger(AbstractClient.this.getClass());
    protected String receiverCode;
    protected int number = COUNTER++;
    private Resource[] resources;

    protected AbstractClient(final RServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    if (this.server.call(this.receiverCode, this)) {
                        this.doWait();
                        this.process();
                    } else {
                        Thread.sleep(10);
                        this.notify();
                    }
                } catch (Exception exception) {
                    LOGGER.error(String.format("%s terminated by the exception", this), exception);
                    this.resources = null;
                    return;
                }
            }
        }
    }

    private synchronized void process() throws InterruptedException {
        LOGGER.trace(String.format("%s is processing resources = %s", this, Arrays.toString(this.resources)));
        Thread.sleep(TIMEOUT);
        this.notifyAll();
    }

    private synchronized void doWait() throws InterruptedException {
        if (this.resources == null) {
            LOGGER.trace(String.format("%s is waiting for rendezvous", this));
            this.wait();
        }
    }

    @Override
    public synchronized void msg(final Resource... resources) {
        LOGGER.info(String.format("%s received resources=%s", this, Arrays.toString(resources)));
        this.resources = resources;
        this.notifyAll();
    }

    @Override
    public String getReceiverCode() {
        return this.receiverCode;
    }

    @Override
    public Client setReceiverCode(final String receiverCode) {
        this.receiverCode = receiverCode;
        return this;
    }

    @Override public Resource[] getResources() {
        final Resource[] resources1 = this.resources;
        this.resources = null;
        return resources1;
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
