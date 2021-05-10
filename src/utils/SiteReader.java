package utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Classe gérant l'analyse de la requête et renvoi le code de réponse et le contenu à renvoyer
 * @author user
 *
 */
public class SiteReader {
	/**
	 * Chemin du dossier de stockage des sites
	 */
    private static final String SITES_DIRECTORY = ConfigReader.getProp("hosts_dir");
    /**
     * Type de contenu de réponse
     */
    public static final String CONTENT_HTML = "text/html";
    /**
     * Code de la réponse
     */
    private static String codeReponse;
    
    /**
     * Fonction pour le traitement des requêtes get
     * @param dns
     * @param path
     * @param auth
     * @return
     */
    public static byte[] get(String dns, String path, String auth) {
        System.out.println("PATH = " + path);
        if (path.equalsIgnoreCase("/")) {
            if (dns.equalsIgnoreCase(ConfigReader.getProp("address"))) {
                return getDefaultHtml(auth);
            } else {
                return getRessource(dns, "index.html", auth);
            }
        } else {
            return getRessource(dns, path, auth);
        }
    }

    /**
     * Fonction de récupération des ressources demandées par la requête
     * @param dns
     * @param path
     * @param auth
     * @return byte[]
     */
    private static byte[] getRessource(String dns, String path, String auth) {
        try {
            File directory = new File(SITES_DIRECTORY);
            File[] list = directory.listFiles();
            for (File file : list) {
                if (dns.contains(file.getName())) {
                    path = path.replace("/", "\\");
                    return checkSecurity(file, path, auth);
                } else {
                    String[] pathElements = path.split("/");
                    if (pathElements.length > 1) {
                        String firstPathElement = pathElements[1];
                        if (firstPathElement.equals(file.getName())) {
                            if (pathElements.length > 2) {
                                return checkSecurity(file, path.replace("/" + firstPathElement, ""), auth);
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        codeReponse = "404 NOT FOUND";
        return null;
    }

    private static byte[] checkSecurity(File file, String path, String auth) throws IOException {
        codeReponse = "200 OK";
        byte[] res = Files.readAllBytes(Paths.get(file.getPath() + "\\" + path));
        Boolean protege = Arrays.stream(file.list()).anyMatch(".htpasswd"::contains);
        Boolean sendDatas = true;
        if (Boolean.TRUE.equals(file.isDirectory() && protege) && auth == null) {
            codeReponse = "401 Unauthorized";
            res = ("WWW-Authenticate: Basic realm=\" Access to " + file.getPath() + "\\" + path + "\"\r\n").getBytes();
        } else if (Boolean.TRUE.equals(file.isDirectory() && protege) && auth != null) {
            sendDatas = checkAuth(file.getPath(), auth);
        }
        if (Boolean.FALSE.equals(sendDatas)) {
            codeReponse = "403 Forbidden";
            res = "".getBytes();
        }
        return res;
    }
    
    /**
     * Fonction de vérification de l'authentification
     * @param path
     * @param auth
     * @return Boolean
     * @throws UnsupportedEncodingException
     */
    private static Boolean checkAuth(String path, String auth) throws UnsupportedEncodingException {
        boolean res = false;
        SecurityReader.setPath(path);
        byte[] decodedValue = Base64.getDecoder().decode(auth);
        String[] credentials = (new String(decodedValue, StandardCharsets.UTF_8.toString())).split(":");
        String hash = SecurityReader.getProp(credentials[0]);
        if (hash != null) {
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");

                md.update(credentials[1].getBytes());
                byte[] digest = md.digest();
                StringBuilder hashString = new StringBuilder();
                for (int i = 0; i < digest.length; ++i) {
                    String hex = Integer.toHexString(digest[i]);
                    if (hex.length() == 1) {
                        hashString.append('0');
                        hashString.append(hex.charAt(hex.length() - 1));
                    } else {
                        hashString.append(hex.substring(hex.length() - 2));
                    }
                }
                res = hash.equals(hashString.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return res;
    }
    
    /**
     * Fonction retournant le contenu et code de réponse pour une requête get sur localhost (127.0.0.1)
     * @param auth
     * @return byte[]
     */
    public static byte[] getDefaultHtml(String auth) {
        try {
            File directory = new File(SITES_DIRECTORY);
            return checkSecurity(directory, "\\index.html", auth);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Fonction retournant le type de contenu
     * @param path
     * @return
     */
    public static String getContentType(String path) {
        if (path.length() > 1) {
            String extension = getExtensionFromPath(path);
            switch (extension) {
                case "html":
                case "css":
                    return "text/" + extension;
                case "js":
                    return "application/javascript";
                case "png":
                case "jpeg":
                case "ico":
                case "gif":
                case "svg":
                    return "image/" + extension;
                case "jpg":
                    return "image/jpeg";
                default:
                    return "";
            }
        } else {
            return CONTENT_HTML;
        }
    }

    /**
     * Fonction indiquant si on peut utiliser gzip sur la réponse à la requête
     * @param path
     * @return boolean
     */
    public static boolean canGzipThisRessource(String path) {
        if (path.length() > 1) {
            String extension = getExtensionFromPath(path);
            return extension.equals("css") || extension.equals("js");
        }
        return false;
    }

    private static String getExtensionFromPath(String path) {
        String[] split = path.split("/");
        String file = split[split.length - 1];
        split = file.split("\\.");
        return split[split.length - 1];
    }

    public static String getCodeReponse() {
        return codeReponse;
    }
}
