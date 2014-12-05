package mars.http;

import mars.http.HTTPStatus;
import mars.http.HTTPServer;
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
public class HTTPRequest {
    
    private final ArrayList<String> request;
    private final ArrayList<String> body;
    private final DataOutputStream outStream;
    
    public HTTPRequest(ArrayList<String> request,ArrayList<String> body,DataOutputStream outStream) throws IOException{
        this.request = request;
        this.body = body;
        this.outStream = outStream;
        getResponse();
    }
    
    public void getResponse() throws IOException{
        String line[] = request.get(0).split(" ");
        
        /* 
                Check http method and return repsonse
                if the  requested Method is not valid
                a   400    response   is  sent   back
                                                            */  
        if(line[0].equalsIgnoreCase("GET")){
            get(line[1]);
        }else if(line[0].equalsIgnoreCase("POST")){
            post(line[1]);
        }else if(line[0].equalsIgnoreCase("HEAD")){
            head(line[1]);
        }else if(line[0].equalsIgnoreCase("PUT")){
            put(line[1]);
        }else if(line[0].equalsIgnoreCase("DELETE")){
            delete(line[1]);
        }else if(line[0].equalsIgnoreCase("CONNECT")){
            connect(line[1]);
        }else if(line[0].equalsIgnoreCase("TRACE")){
            trace(line[1]);
        }else if(line[0].equalsIgnoreCase("OPTIONS")){
            options(line[1]);
        }else{
            outStream.writeBytes(HTTPStatus._400);
        };
    }
    
    private void get(String path) throws IOException{
        String response = "";
        File f = new File("www"+path);
        if(f.exists()){
            if(f.isDirectory()){
                for (String defaultPage : HTTPServer.defaultPages) {
                    if (path.endsWith("/")) {
                        f = new File("www"+path + defaultPage);
                    } else {
                        f = new File("www"+path+"/" + defaultPage);
                    }
                    if(f.exists()){
                        byte[] entity = ServerUtils.getBinaryFile(f);
                        response+="HTTP/1.1 200 OK\r\n";
                        response+="Date: "+ServerUtils.getServerTime()+"\r\n";
                        response+="Connection: close\r\n";
                        response+="Content-Length: "+entity.length+"\r\n";
                        response+="Last-Modified:"+f.lastModified()+"\r\n";
                        response+="\r\n";
                        outStream.writeBytes(response);
                        outStream.write(entity);
                        return;
                    }
                    
                }
            }else{
                byte[] entity = ServerUtils.getBinaryFile(f);
                response+="HTTP/1.1 200 OK\r\n";
                response+="Connection: close\r\n";
                response+="Content-Length: "+entity.length+"\r\n";
                response+="Last-Modified:"+f.lastModified()+"\r\n";
                response+="\r\n";
                outStream.writeBytes(response);
                outStream.write(entity);
                
                return;
            }
        }
        outStream.writeBytes(HTTPStatus._404);
    }
    
    private void post(String path){
        
    }
    
    private void head(String path){
        
    }
    
    private void put(String path) throws IOException{
        File f = new File("www"+path);
        
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        
        
        for(int i=0;i<body.size();i++){
            System.out.println(body.get(i));
            writer.write(body.get(i));
            writer.newLine();
        }
        writer.close();
        outStream.writeChars(HTTPStatus._204);
    }
    
    private void delete(String path) throws IOException{
        File f = new File("www"+path);
        if(f.exists() && f.isDirectory()){
            for (String defaultPage : HTTPServer.defaultPages) {
                if (path.endsWith("/")) {
                    f = new File("www"+path + defaultPage);
                }else {
                    f = new File("www"+path+"/" + defaultPage);
                }
                
                if(f.exists()){
                    if(f.delete()){
                    outStream.writeBytes(HTTPStatus._204);
                    return;
                    }else{
                        System.out.println("not deleted");
                    }
                }
                
            }
        }else if(f.exists()){
            if(f.delete()){
                outStream.writeBytes(HTTPStatus._204);
                return;
            }else{
                System.out.println("not deleted");
                    }
            return;
        }
        
        outStream.writeBytes(HTTPStatus._404);
    }
    
    private void connect(String path) throws IOException{
        outStream.writeBytes("HTTP/1.1 200 Connection established\r\n" +
                             "Date: "+ServerUtils.getServerTime()+"\r\n"+
                             "Server: http server\r\n\r\n");
    }
    
    private void trace(String path)throws IOException{
        String body ="";
        for(int i=0;i<request.size();i++)
            body+=request.get(i)+"\r\n";
        
        String response = "";
        response+=  "HTTP/1.1 200 OK\r\n" +
                    "Date: "+ServerUtils.getServerTime()+"\r\n"+
                    "Server: http server\r\n" +
                    "Connection: close\r\n" +
                    "Content-Type: message/http\r\n" +
                    "Content-Length: "+body.getBytes().length+"\r\n"+
                    "\r\n"+
                    body;
        outStream.writeBytes(response);
    }
    
    private void options(String path) throws IOException{
        outStream.writeBytes(   "HTTP/1.1 200 OK\r\n" +
                                "Date: "+ServerUtils.getServerTime()+"\r\n" +
                                "Server: http server\r\n" +
                                "Allow: GET,HEAD,POST,DELETE,CONNECT,OPTIONS,TRACE\r\n" +
                                "Content-Type: httpd/unix-directory\r\n");
    }
}
