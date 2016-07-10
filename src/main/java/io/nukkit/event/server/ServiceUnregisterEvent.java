package io.nukkit.event.server;

import io.nukkit.event.HandlerList;
import io.nukkit.plugin.RegisteredServiceProvider;

/**
 * This event is called when a service is unregistered.
 * <p>
 * Warning: The order in which register and unregister events are called
 * should not be relied upon.
 */
public class ServiceUnregisterEvent extends ServiceEvent {
    private static final HandlerList handlers = new HandlerList();

    public ServiceUnregisterEvent(RegisteredServiceProvider<?> serviceProvider) {
        super(serviceProvider);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
