package io.nukkit.util;

/**
 * author: MagicDroidX
 * Nukkit Project
 */

import io.nukkit.Nukkit;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Plugin(name = "ConsoleLogger", category = "Core", elementType = "appender", printObject = true)
public class ConsoleLogAppender extends AbstractAppender {
    private static final int MAX_CAPACITY = 250;
    private static final Map<String, BlockingQueue<String>> QUEUES;
    private static final ReadWriteLock QUEUE_LOCK;

    static {
        QUEUES = new HashMap<>();
        QUEUE_LOCK = new ReentrantReadWriteLock();
    }

    private final BlockingQueue<String> queue;

    public ConsoleLogAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final boolean ignoreExceptions, final BlockingQueue<String> queue) {
        super(name, filter, (Layout) layout, ignoreExceptions);
        this.queue = queue;
    }

    @PluginFactory
    public static ConsoleLogAppender createAppender(@PluginAttribute("name") final String name, @PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filters") final Filter filter, @PluginAttribute("target") String target) {
        final boolean ignoreExceptions = Boolean.parseBoolean(ignore);
        if (name == null) {
            ConsoleLogAppender.LOGGER.error("No name provided for ConsoleLogAppender");
            return null;
        }
        if (target == null) {
            target = name;
        }
        ConsoleLogAppender.QUEUE_LOCK.writeLock().lock();
        BlockingQueue<String> queue = ConsoleLogAppender.QUEUES.get(target);
        if (queue == null) {
            queue = new LinkedBlockingQueue<>();
            ConsoleLogAppender.QUEUES.put(target, queue);
        }
        ConsoleLogAppender.QUEUE_LOCK.writeLock().unlock();
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, true, !Nukkit.useConsole, null, null);
        }
        return new ConsoleLogAppender(name, filter, layout, ignoreExceptions, queue);
    }

    public static String getNextLogEvent(final String queueName) {
        ConsoleLogAppender.QUEUE_LOCK.readLock().lock();
        final BlockingQueue<String> queue = ConsoleLogAppender.QUEUES.get(queueName);
        ConsoleLogAppender.QUEUE_LOCK.readLock().unlock();
        if (queue != null) {
            try {
                return queue.take();
            } catch (InterruptedException ex) {
                //ignore
            }
        }
        return null;
    }

    public void append(final LogEvent event) {
        if (this.queue.size() >= 250) {
            this.queue.clear();
        }
        this.queue.add(this.getLayout().toSerializable(event).toString());
    }
}
