package io.nukkit.event.server;

import io.nukkit.event.HandlerList;
import io.nukkit.plugin.Plugin;

/**
 * Called when a plugin is disabled.
 */
public class PluginDisableEvent extends PluginEvent {
    private static final HandlerList handlers = new HandlerList();

    public PluginDisableEvent(final Plugin plugin) {
        super(plugin);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
