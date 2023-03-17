package com.fmicodes.comm.services.util;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class CredentialsUtil {
    private static String credentialsFilePath = "src/main/resources/credentials.yaml";
    private static String rapidApiKeyInYaml = "rapidApiKey";

    public static String getRapidAPIKey() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(credentialsFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(inputStream);
        return (String) data.get(rapidApiKeyInYaml);
    }
}
