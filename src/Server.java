import utils.ConfigReader;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	/**
	 * Main du serveur
	 * @param args
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
    	//Recupération du port dans le fichier de config
        int port = Integer.parseInt(ConfigReader.getProp("port"));
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("[Serveur] Démarrage du serveur sur " + ConfigReader.getProp("address") + ":" + port + ".\r\n");
            //Création d'un thread de traitement par connection
            while (true) {
                System.out.println("[Serveur] Attente d'une connexion...\r\n");
                Socket client = server.accept();
                String clientIp = client.getInetAddress().toString();
                int clientPort = client.getPort();
                System.out.format("[Serveur] : Arrivée Client IP %s sur %d%n", clientIp, clientPort);
                System.out.format("[Serveur ]: Creation du thread T_%d%n", clientPort);
                new Thread(new ServiceClient(client, "T_" + clientPort)).start();
            }
        }
    }
}
