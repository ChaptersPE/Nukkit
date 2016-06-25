package io.nukkit.util;


import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractOutputStreamAppender;
import org.apache.logging.log4j.core.appender.rolling.*;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.util.Booleans;
import org.apache.logging.log4j.core.util.Integers;
import org.apache.logging.log4j.message.MessageFormatMessage;

import java.io.Serializable;
import java.util.HashMap;

/**
 * author: MagicDroidX
 * Nukkit Project
 */

@Plugin(
        name = "FileLogger",
        category = "Core",
        elementType = "appender",
        printObject = true
)
public final class FileLogAppender extends AbstractOutputStreamAppender<RollingFileManager> {
    private final String fileName;
    private final String filePattern;
    private final Advertiser advertiser;
    private Object advertisement;

    private FileLogAppender(String name, Layout<? extends Serializable> layout, Filter filter, RollingFileManager manager, String fileName, String filePattern, boolean ignoreExceptions, boolean immediateFlush, int bufferSize, Advertiser advertiser) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
        if (advertiser != null) {
            HashMap configuration = new HashMap(layout.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }

        this.fileName = fileName;
        this.filePattern = filePattern;
        this.advertiser = advertiser;
    }

    @PluginFactory
    public static FileLogAppender createAppender(@PluginAttribute("fileName") String fileName, @PluginAttribute("filePattern") String filePattern, @PluginAttribute("append") String append, @PluginAttribute("name") String name, @PluginAttribute("immediateFlush") String immediateFlush, @PluginAttribute("bufferSize") String bufferSizeStr, @PluginElement("Policy") TriggeringPolicy policy, @PluginElement("Strategy") RolloverStrategy strategy, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") String ignore, @PluginAttribute("advertise") String advertise, @PluginAttribute("advertiseURI") String advertiseURI, @PluginConfiguration Configuration config) {
        boolean isAppend = Booleans.parseBoolean(append, true);
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        boolean isAdvertise = Boolean.parseBoolean(advertise);
        int bufferSize = Integers.parseInt(bufferSizeStr, 262144);
        if (name == null) {
            LOGGER.error("No name provided for FileAppender");
            return null;
        } else if (fileName == null) {
            LOGGER.error("No filename was provided for FileAppender with name " + name);
            return null;
        } else if (filePattern == null) {
            LOGGER.error("No filename pattern provided for FileAppender with name " + name);
            return null;
        } else if (policy == null) {
            LOGGER.error("A TriggeringPolicy must be provided");
            return null;
        } else {
            if (strategy == null) {
                strategy = DefaultRolloverStrategy.createStrategy(null, null, null, String.valueOf(-1), config);
            }

            if (layout == null) {
                layout = PatternLayout.createDefaultLayout();
            }

            RollingRandomAccessFileManager manager = RollingRandomAccessFileManager.getRollingRandomAccessFileManager(fileName, filePattern, isAppend, isFlush, bufferSize, policy, strategy, advertiseURI, (Layout) layout);
            return manager == null ? null : new FileLogAppender(name, (Layout) layout, filter, manager, fileName, filePattern, ignoreExceptions, isFlush, bufferSize, isAdvertise ? config.getAdvertiser() : null);
        }
    }

    public void stop() {
        super.stop();
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }

    }

    public void append(LogEvent event) {
        event = new Log4jLogEvent(
                event.getLoggerName(),
                event.getMarker(),
                event.getLoggerFqcn(),
                event.getLevel(),
                new MessageFormatMessage(ChatColor.clean(event.getMessage().getFormattedMessage())),
                event.getThrown(),
                event.getContextMap(),
                event.getContextStack(),
                event.getThreadName(),
                event.getSource(),
                event.getTimeMillis());
        RollingRandomAccessFileManager manager = (RollingRandomAccessFileManager) this.getManager();
        manager.checkRollover(event);
        manager.setEndOfBatch(event.isEndOfBatch());
        super.append(event);
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFilePattern() {
        return this.filePattern;
    }

    public int getBufferSize() {
        return this.getManager().getBufferSize();
    }

}
