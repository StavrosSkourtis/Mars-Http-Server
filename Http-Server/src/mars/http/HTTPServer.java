package mars.http;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stavros Skourtis 
 */
public class HTTPServer extends Thread{
    
    public static ArrayList<String> requests;
    
    private ServerSocket serverSocket;
    private boolean run;
    private String root;
    private boolean ssl;
    
    public HTTPServer(String root,int portNumber,boolean ssl) throws IOException{
        this.root = root;
        this.ssl = ssl;
        run = true;
        // create the server's socket
        serverSocket = new ServerSocket(portNumber);
        serverSocket.setSoTimeout(15000);
    }
        
    @Override
    public void run(){
        while(run){
            
                try{
                    // Listen for new requests and accept them
                    Socket client = serverSocket.accept();

                    // Start a new thread for every request and go back go listening
                    new ServerClientThread(client,root).start();
                }catch(IOException e){

                }
            
        }  
    }
    
    public void dispose() throws IOException{
        serverSocket.close();
        run = false;
    }
      
}
