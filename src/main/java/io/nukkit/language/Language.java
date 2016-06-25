package io.nukkit.language;

import java.util.Locale;

public class Language {
    private static LocaleLanguage FALLBACK = LocaleLanguage.getLanguage();
    private static LocaleLanguage LANGUAGE = new LocaleLanguage(Locale.getDefault());

    public static String get(String key) {
        if (LANGUAGE.contains(key)) {
            return LANGUAGE.get(key);
        }

        return FALLBACK.get(key);
    }

    public static String get(String key, Object... args) {
        if (LANGUAGE.contains(key)) {
            return LANGUAGE.get(key, args);
        }

        return FALLBACK.get(key, args);
    }
}
