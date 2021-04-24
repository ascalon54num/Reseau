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
                String line = null;
                Boolean stop = false;
                String auth = null;
                BufferedReader buffer = new BufferedReader(in);
                while(buffer.ready() && Boolean.FALSE.equals(stop)){
                    line= buffer.readLine();
                    if (line.contains("Authorization")){
                        auth = line.split(" ")[2];
                        stop =true;
                    }
                }
                byte[] content = SiteReader.get(dns, path, auth);
                String codeRep =SiteReader.getCodeReponse();
                out.write(("HTTP/1.0 "+codeRep+"\r\n").getBytes());
                String contentType = SiteReader.getContentType(path);
                if (!contentType.equals("")) {
                    out.write(("Content-Type: " + contentType + "\r\n").getBytes());
                }
                if (codeRep.equals("401 Unauthorized")){
                    out.write(content);
                }
                out.write("\r\n".getBytes());
                if( codeRep.equals("200 OK")){
                    out.write(content);
                }

                closeFlux(in, out);

            } catch (Exception e) {
                out.write("HTTP/1.0 404 NOT FOUND\r\n".getBytes());
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
