import utils.SiteReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class ServiceClient implements Runnable {

    private final Socket connection;
    private final String id;

    public ServiceClient(Socket laConnection, String mid) {
        connection = laConnection;
        id = mid;
        System.out.format("Thread T__%s créé pour traiter la connection%n", id);
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            OutputStream out = connection.getOutputStream();
            try {
                String path = in.readLine().split(" ")[1];
                String dns = in.readLine().split(" ")[1];
                out.write("HTTP/1.0 200 OK\r\n".getBytes());
                String contentType = SiteReader.getContentType(path);
                if (!contentType.equals("")) {
                    out.write(("Content-Type: " + contentType + "\r\n").getBytes());
                }
                out.write("\r\n".getBytes());
                out.write(SiteReader.get(dns, path));

                closeFlux(in, out);

            } catch (Exception e) {
                System.out.println("ERREUR 404 à retourner");
                out.write("HTTP/1.0 404 Not Found\r\n".getBytes());
                closeFlux(in, out);
            }
        } catch (Exception ignored) {
        }
    }

    private void closeFlux(BufferedReader in, OutputStream out) throws IOException {
        // on ferme les flux.
        System.err.println("Connexion avec le client terminée");
        out.flush();
        out.close();
        in.close();
        connection.close();
    }
}
