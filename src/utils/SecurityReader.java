package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Lecteur des propri�t�s de s�curit� du serveur
 * @author user
 *
 */
public class SecurityReader {
	
	/**
	 * Chemin du fichier contenant les propri�t�s
	 */
    private static String path;
    /**
     * Instance du singleton
     */
    private  static SecurityReader instance;
    /**
     * Propri�t�s lues dans le fichier
     */
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
    
    /**
     * Fonction retournant l'instance du singleton
     * @return SecurityReader
     */
    public static SecurityReader getInstance() {
        if ( instance == null){
           instance = new SecurityReader();
        }
        return instance;
    }

    /**
     * Fonction pour r�cup�rer une propri�t� sp�cifique du fichier de configuration
     * @param name
     * @return String
     */
    public static String getProp(String name) {
        return getInstance().props.getProperty(name);
    }

    public static void setPath(String path) {
        SecurityReader.path = path+"\\.htpasswd";
    }
}
