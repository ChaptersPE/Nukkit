package io.nukkit.plugin;

import io.nukkit.Nukkit;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.message.Message;


/**
 * The PluginLogger class is a modified {@link Logger} that prepends all
 * logging calls with the name of the plugin doing the logging. The API for
 * PluginLogger is exactly the same as {@link Logger}.
 *
 * @see Logger
 */
public class TestPluginLogger extends Logger {
    private String pluginName;
    private org.apache.logging.log4j.Logger parent;

    /**
     * Creates a new PluginLogger that extracts the name from a plugin.
     *
     * @param context A reference to the plugin
     */
    public TestPluginLogger(String name) {
        super(new LoggerContext(name), name, null);
        pluginName = "[" + name + "] ";
        parent = Nukkit.getServer().getLogger();
        setLevel(Level.ALL);
    }

    @Override
    public void logMessage(String fqcn, Level level, Marker marker, Message message, Throwable t) {
        parent.log(level, marker, pluginName + message.getFormattedMessage(), t);
    }
}
