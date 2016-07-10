package io.nukkit.plugin;

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
public class PluginLogger extends Logger {
    private String pluginName;
    private org.apache.logging.log4j.Logger parent;

    /**
     * Creates a new PluginLogger that extracts the name from a plugin.
     *
     * @param context A reference to the plugin
     */
    public PluginLogger(Plugin context) {
        super(new LoggerContext(context.getClass().getCanonicalName()), context.getClass().getCanonicalName(), context.getServer().getLogger().getMessageFactory());
        String prefix = context.getDescription().getPrefix();
        pluginName = prefix != null ? "[" + prefix + "] " : "[" + context.getDescription().getName() + "] ";
        parent = context.getServer().getLogger();
        setLevel(Level.ALL);
    }

    @Override
    public void logMessage(String fqcn, org.apache.logging.log4j.Level level, Marker marker, Message message, Throwable t) {
        parent.log(level, marker, pluginName + message.getFormattedMessage(), t);
    }
}
