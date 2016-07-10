package io.nukkit.event.server;

import io.nukkit.command.CommandSender;
import io.nukkit.event.Cancellable;
import io.nukkit.event.Event;
import io.nukkit.event.HandlerList;
import org.apache.commons.lang3.Validate;

import java.util.List;

public class TabCompleteEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final CommandSender sender;
    private final String buffer;
    private List completions;
    private boolean cancelled;

    public TabCompleteEvent(CommandSender sender, String buffer, List completions) {
        Validate.notNull(sender, "sender");
        Validate.notNull(buffer, "buffer");
        Validate.notNull(completions, "completions");
        this.sender = sender;
        this.buffer = buffer;
        this.completions = completions;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public String getBuffer() {
        return this.buffer;
    }

    public List getCompletions() {
        return this.completions;
    }

    public void setCompletions(List completions) {
        Validate.notNull(completions);
        this.completions = completions;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
