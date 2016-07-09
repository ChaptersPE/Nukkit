package io.nukkit.command;

public interface ProxiedCommandSender extends CommandSender {
    CommandSender getCaller();

    CommandSender getCallee();
}
