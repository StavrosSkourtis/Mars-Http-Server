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

public class ClientThread extends Thread{
    
    private String root;
    private DataInputStream in;
    private DataOutputStream out;
    private String ip;
    private int port;
    public ClientThread(InputStream in,OutputStream out,String root,String ip,int port){
        this.root = root;
        this.in = new DataInputStream(in);
        this.out = new DataOutputStream(out);
        this.ip = ip;
        this.port = port;
    }
    
                
    public void run(){
        try{
            // get socket i/o
            
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
                connection = requestProcess.process(ip,port);
                
            }while(connection);
            
            // close i/o and socket
            in.close();
            out.close();
                
        }catch(IOException e){   
            e.printStackTrace();
        }
    }
    
   
}
