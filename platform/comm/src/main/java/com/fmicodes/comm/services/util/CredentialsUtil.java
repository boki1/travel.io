package com.fmicodes.comm.services.util;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class CredentialsUtil {

    private static final String CREDENTIALS_FILE_PATH = "src/main/resources/credentials.yaml";
    private static final String RAPID_API_KEY_YAML_VALUE = "rapidApiKey";
    private static final String MAPS_API_KEY_YAML_VALUE = "mapsApiKey";
    private static final String UNSPLASH_API_KEY_YAML_VALUE = "unsplashApiKey";

    public static String getRapidAPIKey() {
        return getAPIKey(RAPID_API_KEY_YAML_VALUE);
    }

    public static String getMapsAPIKey() {
        return getAPIKey(MAPS_API_KEY_YAML_VALUE);
    }

    public static String getUnsplashApiKey() {
        return getAPIKey(UNSPLASH_API_KEY_YAML_VALUE);
    }

    private static String getAPIKey(String key) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(CREDENTIALS_FILE_PATH);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);
        return (String) data.get(key);
    }
}
