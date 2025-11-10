package com.example.interviewsimulator.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class StartupValidator implements ApplicationRunner {

    private final Environment env;

    public StartupValidator(Environment env) {
        this.env = env;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String clientId = env.getProperty("GOOGLE_CLIENT_ID");
        String clientSecret = env.getProperty("GOOGLE_CLIENT_SECRET");

        if (clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
            String message = "Missing required OAuth environment variables: GOOGLE_CLIENT_ID and GOOGLE_CLIENT_SECRET.\n"
                    + "Set them in your environment (Render service env vars, or local .env loaded into the process).\n"
                    + "See README-ENHANCED.md for instructions.";
            System.err.println("[StartupValidator] " + message);
            // Fail fast so developers/CI see a clear error instead of sending malformed requests to Google
            throw new IllegalStateException(message);
        }
    }
}
