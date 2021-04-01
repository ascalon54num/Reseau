package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private final static String URL_CONFIG = "./src/config/config.conf";
    private final static ConfigReader instance = new ConfigReader();

    private final Properties props;

    private ConfigReader() {
        this.props = new Properties();
        try (InputStream is = new FileInputStream(URL_CONFIG)) {
            props.load(is);
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ConfigReader getInstance() {
        return instance;
    }

    public static String getProp(String name) {
        return getInstance().props.getProperty(name);
    }
}
