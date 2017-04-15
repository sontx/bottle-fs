package com.blogspot.sontx.bottle.fs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigUtils {
    private static final String RESOURCE_FILE_NAME = "bottle-fs-config.properties";
    private static Properties properties = null;

    public synchronized static String getValue(String key) {
        if (properties == null)
            loadConfig();
        if (properties == null)
            return null;
        return properties.getProperty(key);
    }

    private static void loadConfig() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        properties = new Properties();
        try (InputStream in = classLoader.getResourceAsStream(RESOURCE_FILE_NAME)) {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            properties = null;
        }
    }

    private ConfigUtils() {
    }
}
