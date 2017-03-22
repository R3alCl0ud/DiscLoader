package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class ReadyEvent extends DLEvent {

    /**
     * @param loader
     */
    public ReadyEvent(DiscLoader loader) {
        super(loader);
    }

}
