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
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		ServerSocket server = new ServerSocket(80);

		System.out.println("Démarrage du serveur sur 127.0.0.1:80.\r\nAttente d'une connexion...");
		while (true) {
			Socket client = server.accept();

			System.out.println("Un client s'est connecté.");
			InputStream in = client.getInputStream();

			OutputStream out = client.getOutputStream();
			Scanner sc = new Scanner(in, "UTF-8");
			// translate bytes of request to string
			String data = sc.useDelimiter("\\r\\n\\r\\n").next();

			Matcher get = Pattern.compile("^GET").matcher(data);
			Matcher post = Pattern.compile("^POST").matcher(data);

			if (get.find()) {
				 Matcher match = Pattern.compile("//(.*)/").matcher(data);
				 match.find();
				 MatchResult mr = match.toMatchResult();
				 try {
					System.out.println("Requête Get sur "+ mr.group(1));
				 } catch (Exception e) {
					 System.out.println("Requête option");
				 }
				byte[] response = ("HTTP/1.1 200 Ok\r\n"
						+ "Content-Type:text/html; charset=utf-8\r\n"
						+ "\r\n\r\n"
						+ "<!DOCTYPE html>" 
						+ "<html>" 
						+ "<p>Hello World<p>" 
						+ "</html>\r\n")
								.getBytes("UTF-8");
				out.write(response, 0, response.length);
			}

			if (post.find()) {

			}
			sc.close();
			client.close();
		}
		
	}
}