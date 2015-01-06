/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Phenom
 */
public class HTTPResponse {
    private String statusCode;
    private final ArrayList<String> headers;
    private byte[] body;
    
    public HTTPResponse(){
       headers = new ArrayList<String>();
    }

    /**
     * set the response line
     * @param statusCode the response line
     */
    public void setStatusCode(String statusCode){
        this.statusCode = statusCode;
    }
    
    /**
     * 
     * @return the response line
     */
    public String getStatusCode(){
        return statusCode;
    }
    
    
    /**
     * Adds a header to the response
     * @param name Header name
     * @param value Header value
     */
    public void addHeader(String name,String value){
        headers.add(name+": "+value);
    }
    
    /**
     * Adds the body of the response
     * @param body the body as an byte array
     */
    public void addBody(byte[] body){
        this.body = body.clone();
    }
    
    
    /**
     * Sends the Response
     * @param out The Output Stream of the Socket that the response will use the send itself
     * @throws IOException 
     */
    public void send(DataOutputStream out) throws IOException{
        
        out.writeBytes(statusCode+"\r\n");
        
        for(String header : headers){
            out.writeBytes(header+"\r\n");
        }
        out.writeBytes("\r\n");
        
        if(body!=null)
            out.write(body);
    }
}
