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

public class ServerClientThread extends Thread{
    
    private Socket client;    
    private ArrayList<String> request;
    private ArrayList<String> body;
    
    public ServerClientThread(Socket client){
        this.client = client; 
        request = new ArrayList<>();
    }
    
                
    public void run(){
        try{
            // get socket i/o
            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out =  new DataOutputStream(client.getOutputStream());
            
            System.out.println("Connected "+client.getInetAddress()+":"+client.getPort());
            
            request.clear();
            
            
            char tempChar;
            String buffer;
            boolean cr =false,lf =false,hasbody = false;
            int cont = 0,bodyLength=0;
            while( cont <1){
                buffer="";
                while(!(cr && lf)){
                    
                    tempChar = (char)in.readByte();
                    
                    if(tempChar=='\r'){
                        cr = true;
                        lf = false;
                    }else if(tempChar=='\n'){
                        lf = true;
                    }else{
                        buffer +=tempChar;
                        
                    }
                }
                String temp = buffer.toLowerCase();
                if(temp.contains("content-length:")){
                    hasbody = true;
                    String t[] = buffer.split(":");
                    bodyLength = Integer.parseInt(t[1].substring(t[1].lastIndexOf(" ")+1));
                }if(buffer.equals("")){
                    cont++;
                    if(cont==1){
                        request.add(buffer);
                    }
                }else{
                    request.add(buffer);
                }
                
                System.out.println(buffer);
                cr = false;
                lf = false;
            }
              
            if(hasbody){
                body = new ArrayList<>();
                int i=0;
                while( i <bodyLength){
                    buffer="";
                    while(!(cr && lf) && i<bodyLength){
                        tempChar = (char)in.readByte();
                        i++;
                        
                        if(tempChar=='\r'){
                            cr = true;
                            lf = false;
                        }else if(tempChar=='\n'){
                            lf = true;
                        }else{
                            buffer +=tempChar;

                        }
                    }
                    body.add(buffer);                   
                    System.out.println(buffer);
                    cr = false;
                    lf = false;
                }
            }        
            System.out.println("-----");
             
            // it parses the request and send back the response
            new HTTPRequest(request,body,out);
            
            // close i/o and socket
            in.close();
            out.close();
            client.close();
        }catch(IOException e){      
        }
    }
}
