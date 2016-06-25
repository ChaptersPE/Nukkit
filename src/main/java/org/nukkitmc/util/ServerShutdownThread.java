package org.nukkitmc.util;

import org.nukkitmc.Server;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ServerShutdownThread extends Thread {

    private final Server server;

    public ServerShutdownThread(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            this.server.stop();
        } finally {
            try {
                this.server.reader.getTerminal().restore();
            } catch (Exception ignored) {
                
            }
        }
    }
}
