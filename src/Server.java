import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Server{
	public static void main(String[] args) throws IOException {
		int port = 80;
		
		try (ServerSocket server = new ServerSocket(port);){
			System.out.println("[Serveur] Démarrage du serveur sur 127.0.0.1:"+port+".\r\n");
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