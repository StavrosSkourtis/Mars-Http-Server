package mars.http;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Phenom
 */
import java.net.*;
import java.io.*;
import mars.utils.Config;
import mars.utils.Logger;

public class ClientThread extends Thread {

    /**
     * root folder of the server
     */
    private String root;

    /**
     * The clients socket
     */
    private Socket client;

    /**
     * Creates a thread that will handle the client
     *
     * @param client the clients socket
     * @param root the root folder
     */
    public ClientThread(Socket client, String root) {
        this.root = root;
        this.client = client;
    }

    /**
     * Starts this Thread
     */
    @Override
    public void run() {

        try {
            /*
             If the clints ip is black listed 
             we will close the connection
             */
            for (String ip : Config.BLACK_LIST) {
                if (client.getInetAddress().getHostAddress().equals(ip)) {
                    client.close();
                    return;
                }

            }

            // get socket input and output streams
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            Socket tunnelSocket = null;

            boolean connection = true;
            boolean httpTunnel = false;

            do {
                if (httpTunnel) {
                    HTTPRequestHandler.tunnel(client, tunnelSocket);
                } else {

                    //Create a new HTTPRequest (an object that represents it , not an actual http request) and pass it the socket input stream;
                    HTTPRequest request = new HTTPRequest(root, client);
                    request.create(in);
                    

                    // it parses the request and send back the response
                    HTTPRequestHandler requestProcess = new HTTPRequestHandler(request, out);

                    // create a response
                    requestProcess.process();

                    // returns true if keep alive , false if close or not exists
                    connection = request.getHeader("connection") != null && request.getHeaderField("connection").equalsIgnoreCase("keep-alive");

                    if (request.method.equalsIgnoreCase("connect")) {
                        httpTunnel = true;
                        connection = true;
                        try {
                            String url[] = request.url.split("\\:");
                            tunnelSocket = new Socket(url[0], Integer.parseInt(url[1]));
                            System.out.println("Everything's fine");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            } while (connection);

            // close i/o and socket
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            Logger.addRecord(e.getMessage());
        }
    }

}
