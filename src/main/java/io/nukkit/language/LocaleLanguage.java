package io.nukkit.language;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class LocaleLanguage {

    private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Splitter splitter = Splitter.on('=').limit(2);
    private static LocaleLanguage language = new LocaleLanguage(Locale.ENGLISH);
    private final Map<String, String> formats = new HashMap<>();

    public LocaleLanguage(Locale locale) {
        try {
            InputStream stream = LocaleLanguage.class.getResourceAsStream("/lang/" + locale.toString() + ".lang");

            if (stream == null) {
                stream = LocaleLanguage.class.getResourceAsStream("/lang/" + locale.getLanguage() + ".lang");
            }

            if (stream != null) {
                for (String line : IOUtils.readLines(stream, StandardCharsets.UTF_8)) {
                    if (!line.isEmpty() && line.charAt(0) != '#') {
                        String[] kv = Iterables.toArray(splitter.split(line), String.class);
                        if (kv != null && kv.length == 2) {
                            String key = kv[0];
                            String value = kv[1];
                            value = pattern.matcher(value).replaceAll("%$1s");
                            this.formats.put(key, value);
                        }
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }

    static LocaleLanguage getLanguage() {
        return language;
    }

    public synchronized String get(String key) {
        return this.getFormat(key);
    }

    public synchronized String get(String key, Object... args) {
        String format = this.getFormat(key);

        try {
            return String.format(format, args);
        } catch (IllegalFormatException var5) {
            return "Format error: " + format;
        }
    }

    private String getFormat(String key) {
        String format = this.formats.get(key);
        return format == null ? key : format;
    }

    public synchronized boolean contains(String key) {
        return this.formats.containsKey(key);
    }
}
