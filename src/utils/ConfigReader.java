package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lecteur du fichier de configuration (singleton)
 *
 * @author user
 */
public class ConfigReader {
    /**
     * Chemin d'acc�s du fichier
     */
    private final static String URL_CONFIG = "/config/config.conf";
    private final static ConfigReader instance = new ConfigReader();

    /**
     * Propri�t�s lues dans le fichier de config
     */
    private final Properties props;

    private ConfigReader() {
        this.props = new Properties();
        try (InputStream is = getClass().getResourceAsStream(URL_CONFIG)) {
            props.load(is);
        } catch (
                IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Fonction retournant l'instance du singleton
     *
     * @return ConfigReader
     */
    public static ConfigReader getInstance() {
        return instance;
    }

    /**
     * Fonction pour r�cup�rer une propri�t� sp�cifique du fichier de configuration
     *
     * @param name
     * @return String
     */
    public static String getProp(String name) {
        return getInstance().props.getProperty(name);
    }
}
