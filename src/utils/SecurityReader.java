package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SecurityReader {

    private static String path;
    private  static SecurityReader instance;

    private final Properties props;

    private SecurityReader() {
        this.props = new Properties();
        try (InputStream is = new FileInputStream(path)) {
            props.load(is);
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
    }

    public static SecurityReader getInstance() {
        if ( instance == null){
           instance = new SecurityReader();
        }
        return instance;
    }

    public static String getProp(String name) {
        return getInstance().props.getProperty(name);
    }

    public static void setPath(String path) {
        SecurityReader.path = path+"\\.htpasswd";
    }
}
