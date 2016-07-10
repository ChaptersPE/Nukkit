package io.nukkit.event.server;

import io.nukkit.plugin.RegisteredServiceProvider;

/**
 * An event relating to a registered service. This is called in a {@link
 * org.bukkit.plugin.ServicesManager}
 */
public abstract class ServiceEvent extends ServerEvent {
    private final RegisteredServiceProvider<?> provider;

    public ServiceEvent(final RegisteredServiceProvider<?> provider) {
        this.provider = provider;
    }

    public RegisteredServiceProvider<?> getProvider() {
        return provider;
    }
}