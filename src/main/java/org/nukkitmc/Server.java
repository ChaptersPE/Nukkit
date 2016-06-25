package org.nukkitmc;

import jline.UnsupportedTerminal;
import jline.console.ConsoleReader;
import joptsimple.OptionSet;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nukkitmc.util.ConsoleHandler;
import org.nukkitmc.util.ConsoleWriter;
import org.nukkitmc.util.LoggerOutputStream;
import org.nukkitmc.util.ServerShutdownThread;

import java.io.IOException;
import java.io.PrintStream;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class Server implements Runnable {

    public static final Logger LOGGER = LogManager.getLogger();

    public OptionSet options;

    public ConsoleReader reader;

    private boolean isRunning = true;
    private boolean isStopped;

    public int port = -1;

    public Server(OptionSet options) {
        this.options = options;

        if (System.console() == null && System.getProperty(jline.TerminalFactory.JLINE_TERMINAL) == null) {
            System.setProperty(jline.TerminalFactory.JLINE_TERMINAL, UnsupportedTerminal.class.getName());

            Nukkit.useJline = false;
        }

        if (options.has("port")) {
            int port = (Integer) options.valueOf("port");
            if (port > 0) {
                this.setPort(port);
            }
        }

        try {
            this.reader = new ConsoleReader(System.in, System.out);
            this.reader.setExpandEvents(false);
        } catch (Throwable e) {
            try {
                System.setProperty(jline.TerminalFactory.JLINE_TERMINAL, UnsupportedTerminal.class.getName());
                System.setProperty("user.language", "en");
                Nukkit.useJline = false;

                this.reader = new ConsoleReader(System.in, System.out);
                this.reader.setExpandEvents(false);
            } catch (IOException ex) {
                LOGGER.warn((String) null, ex);
            }
        }

        Runtime.getRuntime().addShutdownHook(new ServerShutdownThread(this));

        (new ConsoleHandler(this)).start();

        (new ConsoleWriter(System.out, this.reader)).start();

        Logger rootLogger = LogManager.getRootLogger();
        System.setOut(new PrintStream(new LoggerOutputStream(rootLogger, Level.INFO), true));
        System.setErr(new PrintStream(new LoggerOutputStream(rootLogger, Level.WARN), true));

        (new Thread(this, "Server Thread")).start();
    }

    @Override
    public void run() {

    }

    public void stop() {

    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public boolean isStopped() {
        return this.isStopped;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
