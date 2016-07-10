package io.nukkit.event.server;

import io.nukkit.plugin.Plugin;

/**
 * Used for plugin enable and disable events
 */
public abstract class PluginEvent extends ServerEvent {
    private final Plugin plugin;

    public PluginEvent(final Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the plugin involved in this event
     *
     * @return Plugin for this event
     */
    public Plugin getPlugin() {
        return plugin;
    }
}
