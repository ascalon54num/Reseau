import utils.SiteReader;

import java.io.*;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;

public class ServiceClient implements Runnable {

    /**
     * Connection socket de l'utilisateur ayant fait une requ�te
     */
    private final Socket connection;
    /**
     * Id du thread
     */
    private final String id;

    /**
     * Constructeur de la cible du thread
     *
     * @param laConnection
     * @param mid
     */
    public ServiceClient(Socket laConnection, String mid) {
        connection = laConnection;
        id = mid;
        System.out.format("Thread T__%s cr�� pour traiter la connection%n", id);
    }

    @Override
    /**
     * Fonction de traitement de la cible du thread
     */
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            OutputStream out = connection.getOutputStream();
            try {
                //R�cup�ration du path de la requ�te
                String path = in.readLine().split(" ")[1];
                //R�cup�ration du nom de domaine de la requete
                String dns = in.readLine().split(" ")[1];
                String line = null;
                Boolean stop = false;
                String auth = null;
                BufferedReader buffer = new BufferedReader(in);
                while (buffer.ready() && Boolean.FALSE.equals(stop)) {
                    line = buffer.readLine();
                    if (line.contains("Authorization")) {
                        auth = line.split(" ")[2];
                        stop = true;
                    }
                }
                //R�cup�ration contenu de la r�ponse
                byte[] content = SiteReader.get(dns, path, auth);
                //R�cup�ration du code de retour
                String codeRep = SiteReader.getCodeReponse();
                //Ecriture ent�te du code de retour
                out.write(("HTTP/1.0 " + codeRep + "\r\n").getBytes());
                String contentType = SiteReader.getContentType(path);
                //Usage de gzip
                if (SiteReader.canGzipThisRessource(path)) {
                    ByteArrayOutputStream obj = new ByteArrayOutputStream();
                    GZIPOutputStream gzip = new GZIPOutputStream(obj);
                    gzip.write(content);
                    gzip.flush();
                    gzip.close();
                    content = obj.toByteArray();
                    obj.flush();
                    obj.close();
                    out.write("Content-Encoding: gzip\r\n".getBytes());
                }

                if (!contentType.equals("")) {
                    //Ecriture de l'ent�te du type de contenu
                    out.write(("Content-Type: " + contentType + "\r\n").getBytes());
                }
                //Ecriture du contenu de la r�ponse
                if (codeRep.equals("401 Unauthorized")) {
                    out.write(content);
                }
                out.write("\r\n".getBytes());

                if (codeRep.equals("200 OK")) {
                    out.write(content);
                }

                closeFlux(in, out);

            } catch (Exception e) {
                //En cas d'exception
                out.write("HTTP/1.0 404 NOT FOUND\r\n".getBytes());
                closeFlux(in, out);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Fonction qui ferme la connexion
     *
     * @param in
     * @param out
     * @throws IOException
     */
    private void closeFlux(BufferedReader in, OutputStream out) throws IOException {
        // on ferme les flux.
        System.err.println("Connexion avec le client termin�e");
        out.flush();
        out.close();
        in.close();
        connection.close();
    }
}
