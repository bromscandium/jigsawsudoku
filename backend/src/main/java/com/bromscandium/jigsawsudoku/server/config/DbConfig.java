package com.bromscandium.jigsawsudoku.server.config;

import java.io.IOException;
import java.util.Properties;

public class DbConfig {
    private static final Properties props = new Properties();

    static {
        try {
            props.load(DbConfig.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            throw new RuntimeException("Error of application.properties", e);
        }
    }

    public static String getUrl() {
        return props.getProperty("spring.datasource.url");
    }

    public static String getUser() {
        return props.getProperty("spring.datasource.username");
    }

    public static String getPassword() {
        return props.getProperty("spring.datasource.password");
    }
}
