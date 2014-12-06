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
    
    public ServerClientThread(Socket client){
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
                HTTPRequest request = new HTTPRequest("www");
                request.create(in);
                
               
                // adding records to the log file (log.txt)
                Logger.addRecord("Connected to "+client.getInetAddress()+" port:"+client.getPort()+" Requested : "+request.method+" "+request.url);

                // it parses the request and send back the response
                HTTPRequestHandler requestProcess = new HTTPRequestHandler(request,out);
                // returns true if keep alive , false if close or not exists
                connection = requestProcess.process();
                
            }while(connection);
            
            // close i/o and socket
            in.close();
            out.close();
            client.close();
        }catch(IOException e){      
        }
    }
    
   
}
