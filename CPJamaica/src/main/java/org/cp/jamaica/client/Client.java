package org.cp.jamaica.client;

import org.cp.monitor.resources.Resource;

/**
 * {@code Client} calls the server for <b>service</b>
 *
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public interface Client
        extends Runnable {

    void msg(Resource... resources);

    String getReceiverCode();

    Client setReceiverCode(final String receiverCode);

    Resource[] getResources();
}
