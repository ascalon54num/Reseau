import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceClient implements Runnable {

    private Socket connection;
    private String id;

    public ServiceClient(Socket laConnection, String mid)
    {
	    connection= laConnection;
	    id=mid;
	    System.out.format("Thread T__%s créé pour traiter la connection%n",id);  
    }

    private  void closeConnection(){
        try{
            if (connection != null)      
            {
                System.out.format("Fermeture connection pour %s%n", id); 
                connection.close();
            } 
        }
        catch (IOException e) {
            System.out.format("Terminaison pour %s%n", id);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        InputStream in = initInputStream();

        OutputStream out = initOutputStream();
        Scanner sc = new Scanner(in, StandardCharsets.UTF_8);
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
                            .getBytes(StandardCharsets.UTF_8);
            try {
                out.write(response, 0, response.length);
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        if (post.find()) {

        }
        sc.close();
        closeConnection();
    }

    private OutputStream initOutputStream() {
        OutputStream res = null;
        try {
            res=connection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private InputStream initInputStream() {
        InputStream res = null;
        try {
           res = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    
}
