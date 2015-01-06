/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.http;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Stavros Skourtis
 */
public class HTTPRequest {
    
    /**
     * Server folder root
     */
    public String root;
    
    /**
     * Absolute path to the root
     */
    public String absoluteRoot;
    
    /**
     * A String that has the protocol version
     */
    public String protocolVersion;
    
    /**
     * The url of the request
     */
    public String url;
    
    /**
     * The request method
     */
    public String method;
    
    /**
     * Query parameters if any
     */
    public String query;
    
    /**
     * IP of the client
     */
    public String ip;
    
    /**
     * Clients hostname
     */
    public String hostname;
    /**
     * The port of the client
     */
    public int port;
    /**
     * The file the client requested
     */
    public File urlFile;
    
    /**
     * A list of headers
     */
    private ArrayList<HTTPHeader> headers;
    /**
     * The request body as an array of bytes
     */
    public byte[] body;
    
    /**
     * creates the request
     * @param root server root folder
     * @param client Client's socket
     */
    public HTTPRequest(String root,Socket client){
        this.root = root;
        headers  = new ArrayList<HTTPHeader>();
        this.ip = client.getInetAddress().getHostAddress();
        this.hostname = client.getInetAddress().getHostName();
        this.port = client.getPort();
        absoluteRoot = new File(root).getAbsolutePath();
    }
    
    
    /**
     * Gets the value of header
     * @param name the header name we want
     * @return the value of the specified header filed, return null if it doesn't exist 
     */
    public String getHeaderField(String name){
        for(HTTPHeader header : headers)
            if(header.getName().equalsIgnoreCase(name))
                return header.getValue();
        return null;
    }
    
    /**
     * Gets a header object
     * @param name the name of the header we want
     * @return the header that matches the parameter string , null if it doesn't exist
     */
    public HTTPHeader getHeader(String name){
        for(HTTPHeader header : headers)
            if(header.getName().equalsIgnoreCase(name))
                return header;
        return null;
    }
    
    /**
     * Reads data from the socket request line / headers/ body( if exists)
     * @param in Socket Input Stream
     * @throws IOException 
     */
    public void create(DataInputStream in) throws IOException{
        int length = readRequestHeader(in);
        
        /*
            If body exists we read it
        */
        if(length >0)
            readRequestBody(in, length);
    }
    
    /**
     * read the header + request line
     * @param in Socket input Stream
     * @return body length , it can be 0 (no body)
     * @throws IOException 
     */
    private int readRequestHeader(DataInputStream in) throws IOException{  
        /*
         *  First we read the Request line 
         *  we read chars until we find \r \n 
         *  when we are done we split the String
         *  the first part is the http method
         *  the second part is the url
         *  the last part it the protocol version
         */
        char tempChar;
        String buffer = "";
        boolean cr =false,lf =false;
        
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
            
        String[] requestLine = buffer.split(" ");
        method = requestLine[0];
        url = requestLine[1];
        protocolVersion = requestLine[2];
        
        if(url.contains("?")){
            String[] t = url.split("\\?");
            query = t[1];
            urlFile = new File(root+t[0]);
        }else{
            query = "";
            urlFile = new File(root+url);
        }
        
       
        /*
            Below we read the headers
            we read every byte, cast it to char , add it to a string
            if a char is \r\n we add the string to the list and reset the buffer to empty string
            if a char is \r\n and the string is empty we know this is the empty line between the header and body
            we stop reading
            if this header contains the content-length header field we return it
            else we return 0
        */
        

        buffer ="";
        cr =false;
        lf =false;
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
            }
            
            if(buffer.equals("")){
                cont++;
            }else{
                headers.add(new HTTPHeader(buffer, urlFile));
            }
            
            
            cr = false;
            lf = false;
        }
        
        return bodyLength;
    }
    
    
    /**
     * Reads the body of the the request
     * @param in Socket Input Stream
     * @param bodyLength the length of the body
     * @throws IOException 
     */
    private void readRequestBody(DataInputStream in,int bodyLength) throws IOException{
        /*
            Here we read the request body,
            this will run only if content-length header exists
            we read exactly (var bodyLength) bytes
            
        */       
        
        body = new byte[bodyLength];
        
        int i=0;
        while( i <bodyLength){
            body[i] = in.readByte();
            i++;
        }
        
        if(method.equalsIgnoreCase("post"))
            query = ServerUtils.charBytesToString(body);
    }
    
    
    /**
     * 
     * @return returns a string that represents the request
     */
    public String getRequestString(){
        String request="";
        
        request+=method+" "+url+" "+protocolVersion+"\r\n";
        
        for(HTTPHeader header :headers){
            request += header.getName()+": "+header.getValue()+"\r\n";
        }
        
        return request;
    }
}
