package org.cp.semaphores.core.threads;

/**
 * {@code CFSemaphore} stands for <b>CalculatingFractalPathSemaphore</b>.
 * It calculates the consecutive points which are further pushed to the stack
 * from which another thread pops them and use to
 *
 * @author kornicameister
 * @version 0.0.1
 * @since 0.0.1
 */
public class CFPSemaphore
        extends FSemaphore {
    @Override
    protected void runInternal() {

    }
}
