package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;

/**
 * @author Perry Berman
 * @since 0.1.0
 */
public class ReadyEvent extends DLEvent {

    /**
     * @param loader The current instance of DiscLoader
     */
    public ReadyEvent(DiscLoader loader) {
        super(loader);
    }

}
