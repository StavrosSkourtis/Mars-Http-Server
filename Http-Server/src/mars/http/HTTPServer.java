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
    public static String[] defaultPages;
   
    private final ServerSocket serverSocket;
    
    public HTTPServer(int portNumber) throws IOException{
        // create the server's socket
        HTTPStatus.init();
        serverSocket = new ServerSocket(portNumber);
    }
        
    @Override
    public void run(){
        while(true){
            try{
                // Listen for new requests and accept them
                Socket client = serverSocket.accept();
                
                // Start a new thread for every request and go back go listening
                new ServerClientThread(client).start();
            }catch(IOException e){
                
            }
        }  
    }
}
