package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesUtil
{
    private static Properties props = new Properties();

    static {
        try {
            props.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}
