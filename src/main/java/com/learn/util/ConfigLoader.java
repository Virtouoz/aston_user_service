package com.learn.util;

import com.learn.exception.ConfigLoaderException;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ConfigLoader {

    private ConfigLoader() {
        throw new AssertionError("Utility class cannot be instantiated");
    }

    public static Properties loadHibernateProperties() {
        Yaml yaml = new Yaml();
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream("hibernate.yml");
        if (inputStream == null) {
            throw new ConfigLoaderException("hibernate.yml not found in resources", new RuntimeException());
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> yamlMap = yaml.load(inputStream);
        Properties properties = new Properties();
        flattenMap(yamlMap, "", properties);
        return properties;
    }

    private static void flattenMap(Map<String, Object> map, String prefix, Properties properties) {
        map.forEach((key, value) -> {
            String newPrefix = prefix.isEmpty() ? key : prefix + "." + key;
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> subMap = (Map<String, Object>) value;
                flattenMap(subMap, newPrefix, properties);
            } else {
                properties.setProperty(newPrefix, value.toString());
            }
        });
    }
}