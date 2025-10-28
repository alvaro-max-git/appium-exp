package org.nttdata.config;

import java.util.Optional;
import io.github.cdimascio.dotenv.Dotenv;

public class Env {

    private final static Dotenv DOTENV = Dotenv.configure().
        ignoreIfMalformed().
        ignoreIfMissing().
        load();

    private Env () {}


    public static String get(String key, String defaultValue) {
        String system_value = System.getenv(key);

        if (system_value != null && !system_value.isBlank())
            return system_value;

        String dotenv_value = DOTENV.get(key);

        return ((dotenv_value != null) && (!dotenv_value.isBlank())) ? dotenv_value : defaultValue;
    }

    public static String getRequired (String key) {
        return
            Optional.ofNullable(get(key, null)).
                filter(v -> !v.isBlank())
                .orElseThrow(() -> new IllegalStateException("Missing required env value: " + key));
    }

}
