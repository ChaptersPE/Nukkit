package io.nukkit.util;


import io.nukkit.Nukkit;
import jline.console.ConsoleReader;
import jline.console.CursorBuffer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Attribute;

import java.io.IOException;
import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;


/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ConsoleWriter extends Thread {

    private static final Map<ChatColor, String> replacements = new EnumMap<>(ChatColor.class);

    static {
        replacements.put(ChatColor.BLACK, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        replacements.put(ChatColor.DARK_BLUE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        replacements.put(ChatColor.DARK_GREEN, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        replacements.put(ChatColor.DARK_AQUA, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        replacements.put(ChatColor.DARK_RED, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        replacements.put(ChatColor.DARK_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        replacements.put(ChatColor.GOLD, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        replacements.put(ChatColor.GRAY, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        replacements.put(ChatColor.DARK_GRAY, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
        replacements.put(ChatColor.BLUE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
        replacements.put(ChatColor.GREEN, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
        replacements.put(ChatColor.AQUA, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
        replacements.put(ChatColor.RED, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
        replacements.put(ChatColor.LIGHT_PURPLE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
        replacements.put(ChatColor.YELLOW, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
        replacements.put(ChatColor.WHITE, Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
        replacements.put(ChatColor.MAGIC, Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
        replacements.put(ChatColor.BOLD, Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString());
        replacements.put(ChatColor.STRIKETHROUGH, Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
        replacements.put(ChatColor.UNDERLINE, Ansi.ansi().a(Attribute.UNDERLINE).toString());
        replacements.put(ChatColor.ITALIC, Ansi.ansi().a(Attribute.ITALIC).toString());
        replacements.put(ChatColor.RESET, Ansi.ansi().a(Attribute.RESET).toString());
    }

    private final ConsoleReader reader;
    private CursorBuffer buffer;
    private final OutputStream output;
    private final ChatColor[] colors = ChatColor.values();

    public ConsoleWriter(OutputStream output, ConsoleReader reader) {
        this.output = output;
        this.reader = reader;
    }

    public void run() {
        while (true) {
            String message = ConsoleLogAppender.getNextLogEvent("ConsoleLogger");
            if (message != null) {
                if (reader.getTerminal().isAnsiSupported()) {
                    for (ChatColor color : colors) {
                        if (replacements.containsKey(color)) {
                            message = message.replaceAll("(?i)" + color.toString(), replacements.get(color));
                        } else {
                            message = message.replaceAll("(?i)" + color.toString(), "");
                        }
                    }
                } else {
                    message = ChatColor.clean(message);
                }

                try {
                    if (Nukkit.useJline) {
                        this.reader.print(Ansi.ansi().eraseLine(Ansi.Erase.ALL).toString() + '\r');
                        this.reader.flush();
                        this.output.write(message.getBytes());
                        this.output.flush();

                        try {
                            this.reader.drawLine();
                        } catch (Throwable var3) {
                            this.reader.getCursorBuffer().clear();
                        }

                        this.reader.flush();
                        /*this.buffer = this.reader.getCursorBuffer().copy();
                        this.reader.getOutput().write("\u001b[1G\u001b[K");
                        this.reader.flush();

                        this.output.write(message.getBytes());
                        this.output.flush();

                        this.reader.resetPromptLine(this.reader.getPrompt(), this.buffer.toString(), this.buffer.cursor);*/
                    } else {
                        this.output.write(message.getBytes());
                        this.output.flush();
                    }
                } catch (IOException var4) {
                    LogManager.getLogger(ConsoleWriter.class.getName()).log(Level.FATAL, (String) null, var4);
                }
            }
        }
    }
}
