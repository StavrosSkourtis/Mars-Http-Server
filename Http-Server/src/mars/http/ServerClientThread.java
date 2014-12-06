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
import java.util.ArrayList;
import mars.utils.Logger;

public class ServerClientThread extends Thread{
    
    private final Socket client; 
    private String root;
    
    public ServerClientThread(Socket client,String root){
        this.root = root;
        this.client = client; 
    }
    
                
    public void run(){
        try{
            // get socket i/o
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out =  new DataOutputStream(client.getOutputStream());
            boolean connection;
            
            do{
                // clear the list so we can handle new a request
               
                
                int length;
                
                //Create a new HTTPRequest (an object that represents it , not an actual http request) and pass it the socket input stream;
                HTTPRequest request = new HTTPRequest(root);
                request.create(in);
                              
                

                // it parses the request and send back the response
                HTTPRequestHandler requestProcess = new HTTPRequestHandler(request,out);
                // returns true if keep alive , false if close or not exists
                connection = requestProcess.process(client.getInetAddress().toString(),client.getPort());
                
            }while(connection);
            
            // close i/o and socket
            in.close();
            out.close();
            client.close();
        }catch(IOException e){      
        }
    }
    
   
}
