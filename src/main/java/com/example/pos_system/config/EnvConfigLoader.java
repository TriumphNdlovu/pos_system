package com.example.pos_system.config;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class EnvConfigLoader {

    private final Environment environment;

    public EnvConfigLoader(Environment environment) {
        this.environment = environment;
    }

    @PostConstruct
    public void init() {
        String dbUrl = environment.getProperty("DB_URL");
        String dbUser = environment.getProperty("DB_USER");
        String dbPassword = environment.getProperty("DB_PASSWORD");

        System.setProperty("db.url", dbUrl);
        System.setProperty("db.user", dbUser);
        System.setProperty("db.password", dbPassword);
    }
}
