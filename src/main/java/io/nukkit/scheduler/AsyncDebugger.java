package io.nukkit.scheduler;

import io.nukkit.plugin.Plugin;

class AsyncDebugger {
    private AsyncDebugger next = null;
    private final int expiry;
    private final Plugin plugin;
    private final Class clazz;

    AsyncDebugger(int expiry, Plugin plugin, Class clazz) {
        this.expiry = expiry;
        this.plugin = plugin;
        this.clazz = clazz;
    }

    final AsyncDebugger getNextHead(int time) {
        AsyncDebugger current;
        AsyncDebugger next;
        for (current = this; time > current.expiry; current = next) {
            next = current.next;
            if (current.next == null) {
                break;
            }
        }

        return current;
    }

    final AsyncDebugger setNext(AsyncDebugger next) {
        return this.next = next;
    }

    StringBuilder debugTo(StringBuilder string) {
        for (AsyncDebugger next = this; next != null; next = next.next) {
            string.append(next.plugin.getDescription().getName()).append(':').append(next.clazz.getName()).append('@').append(next.expiry).append(',');
        }

        return string;
    }
}
