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
            boolean connection;
            
            do{
                // clear the list so we can handle new a request
                request.clear();
               
                
                int length;
                
                // Read Header , if body length >0 we read the body too
                if( (length = readRequestHeader(in)) >0){
                    readRequestBody(in, length);
                }
                
                // adding records to the log file (log.txt)
                Logger.addRecord("Connected to "+client.getInetAddress()+" port:"+client.getPort()+" Requested : "+request.get(0));

                // it parses the request and send back the response
                HTTPRequestHandler requestProcess = new HTTPRequestHandler(request,body,out);
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
    
    
    public int readRequestHeader(DataInputStream in) throws IOException{
        
        /*
            Below we read the headers
            we read every byte, cast it to char , add it to a string
            if a char is \r\n we add the string to the list and reset the buffer to empty string
            if a char is \r\n and the string is empty we know this is the empty line between the header and body
            we stop reading
            if this header contains the content-length header field we return it
            else we return 0
        */
        
        char tempChar;
        String buffer;
        boolean cr =false,lf =false;
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
                
            //System.out.println(buffer);
            cr = false;
            lf = false;
        }
        
        return bodyLength;
    }
    
    public void readRequestBody(DataInputStream in,int bodyLength) throws IOException{
        
        /*
            Here we read the request body,
            this will run only if content-length header exists
            we read exactly (var bodyLength) bytes
            
        */
        char tempChar;
        String buffer;
        boolean cr =false,lf =false;
        int cont = 0;
        
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
            //System.out.println(buffer);
            cr = false;
            lf = false;
        }

        System.out.println("-----");
    }
}
