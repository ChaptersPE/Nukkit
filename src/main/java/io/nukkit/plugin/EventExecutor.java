package io.nukkit.plugin;

import io.nukkit.event.Event;
import io.nukkit.event.EventException;
import io.nukkit.event.Listener;

/**
 * Interface which defines the class for event call backs to plugins
 */
public interface EventExecutor {
    void execute(Listener listener, Event event) throws EventException;
}
