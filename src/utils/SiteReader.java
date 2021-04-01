package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SiteReader {

    private final static String SITES_DIRECTORY = ConfigReader.getProp("hosts_dir");
    public static final String CONTENT_HTML = "text/html";

    public static byte[] get(String dns, String path) {
        if (path.equalsIgnoreCase("/")) {
            if (dns.equalsIgnoreCase(ConfigReader.getProp("address"))) {
                return getDefaultHtml();
            } else {
                return getRessource(dns, "index.html");
            }
        } else {
            return getRessource(dns, path);
        }
    }

    private static byte[] getRessource(String dns, String path) {
        try {
            File directory = new File(SITES_DIRECTORY);
            File[] list = directory.listFiles();
            for (File file : list) {
                if (dns.contains(file.getName())) {
                    path = path.replace("/", "\\");
                    return Files.readAllBytes(Paths.get(file.getPath() + "\\" + path));
                }
            }
        } catch (Exception ignored) {
        }
        return null; // TODO: Gerer Erreur 404
    }

    public static byte[] getDefaultHtml() {
        try {
            return Files.readAllBytes(Paths.get(SITES_DIRECTORY + "\\index.html"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getContentType(String path) {
        if (path.length() > 1) {
            String extension = getExtensionFromPath(path);
            return switch (extension) {
                case "html", "css" -> "text/" + extension;
                case "js" -> "application/javascript";
                case "png", "jpeg", "ico", "gif", "svg" -> "image/" + extension;
                case "jpg" -> "image/jpeg";
                default -> "";
            };
        } else {
            return CONTENT_HTML;
        }
    }

    private static String getExtensionFromPath(String path) {
        String[] split = path.split("/");
        String file = split[split.length - 1];
        split = file.split("\\.");
        return split[split.length - 1];
    }
}
