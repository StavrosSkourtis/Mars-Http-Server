package mars.http;

import mars.http.HTTPStatus;
import mars.http.HTTPServer;
import java.io.*;
import java.util.ArrayList;
import static mars.http.HTTPStatus.*;
import mars.utils.Config;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stavros Skourtis
 */
public class HTTPRequestHandler {
    
    private final ArrayList<String> request;
    private final ArrayList<String> body;
    private final DataOutputStream outStream;
    
    
    /*
        Request Line
    */
    
    private String method;
    private String url;
    private File urlFile;
    private String protocolVersion;
    
    /*
        Header fields 
    */
    private String host;
    private String connection;
    private String date;
    private String accept;
    private String authorization;
    private String cookie;
    private String range;
    private String userAgent;
    private String transferEncoding;
    
    /*
        The following fields are conditional
        value 10 = not used;
        value 0 = false;
        value 1 = true;
    */
    private byte ifMatch = 10;
    private byte ifModifiedSince = 10;
    private byte ifNoneMatch = 10;
    private byte ifRange =10;
    private byte ifUnModifiedSince = 10;
    
    /*
        Constructor
    */
    public HTTPRequestHandler(ArrayList<String> request,ArrayList<String> body,DataOutputStream outStream){
        this.request = request;
        this.body = body;
        this.outStream = outStream;
    }
    
    public boolean process() throws IOException{
        parseHeaders();
        getResponse();
        
        return !connection.equalsIgnoreCase("close");
    }
    
    /*
        Parse Header
    */
    public void parseHeaders(){
        
        /*
            First we parse the request line
        */
              
        String line[] = request.get(0).split(" ");
        
        method = line[0];
        url = line[1];
        urlFile = new File("www"+url);
        protocolVersion = line[2];
        
        
        /*
            Then we loop throught the header fields and store the appropriate value to the variables
        */
        for (int i=1;i<request.size();i++) {
            String[] fields = request.get(i).split(":");
            
            if(fields[0].equalsIgnoreCase("connection")){
                connection = fields[1].contains("keep-alive") ? "keep-alive" : "close";
            }if(fields[0].equalsIgnoreCase("host")){
                host = fields[1].trim();
            }else if(fields[0].equalsIgnoreCase("date")){
                date = fields[1].trim();
            }else if(fields[0].equalsIgnoreCase("accept")){
                accept = fields[1].trim();
            }else if(fields[0].equalsIgnoreCase("authorization")){
                authorization = fields[1].trim();
            }else if(fields[0].equalsIgnoreCase("cookie")){
                cookie = fields[1].trim();
            }else if(fields[0].equalsIgnoreCase("if-match")){
               
            }else if(fields[0].equalsIgnoreCase("if-modified-since")){
                if(ServerUtils.compareDate( fields[1].trim() , urlFile) < 0)
                    ifModifiedSince = 1;
                else 
                    ifModifiedSince = 0;
            }else if(fields[0].equalsIgnoreCase("if-none-match")){
                
            }else if(fields[0].equalsIgnoreCase("if-range")){
                if(ServerUtils.compareDate( fields[1].trim() , urlFile) < 0)
                    ifRange = 1;
                else 
                    ifRange = 0;
            }else if(fields[0].equalsIgnoreCase("if-unmodified-since")){
                if(ServerUtils.compareDate( fields[1].trim() , urlFile) >0)
                    ifUnModifiedSince = 1;
                else 
                    ifUnModifiedSince = 0;
            }else if(fields[0].equalsIgnoreCase("range")){
                range = fields[1].trim();
            }else if(fields[0].equalsIgnoreCase("user-agent")){
                userAgent = fields[1].trim();
            }else if(fields[0].equalsIgnoreCase("transfer-encoding")){
                transferEncoding = fields[1].trim();
            }
            
        }
               
    }
    
    public void getResponse() throws IOException{
        /*
            If protocol is not HTTP/1.1 or HTTP/1.0 is is not suported
        */
        if(! (protocolVersion.equals("HTTP/1.1") || protocolVersion.equals("HTTP/1.0") )){
            outStream.writeBytes(_505);
            return;
        }
        
        /*
            The request MUST contain a host header field
        */
        if(host==null){
            outStream.writeBytes(_400);
            return;
        }
        
        /*
            If true the project is modified
        */
        if (ifUnModifiedSince == 0 && urlFile.exists()){ 
            outStream.writeBytes(_412);
            return;
        }
        
        /*
            If true the project is not modified , so 304 status code is send back
        */
        if( ifModifiedSince == 0 && urlFile.exists()){
            outStream.writeBytes(_304);
            return;
        }
        
        
        /* 
                Check http method and return repsonse
                if the  requested Method is not valid
                a   400    response   is  sent   back
                                                            */  
        if(method.equalsIgnoreCase("GET")){
            if(Config.GET)
                get();
            else
                outStream.writeBytes(_405);
        }else if(method.equalsIgnoreCase("POST")){
            if(Config.POST)
                post();
            else
                outStream.writeBytes(_405);
        }else if(method.equalsIgnoreCase("HEAD")){
            if(Config.HEAD)
                head();
            else
                outStream.writeBytes(_405);
        }else if(method.equalsIgnoreCase("PUT")){
            if(Config.PUT)
                put();
            else
                outStream.writeBytes(_405);
        }else if(method.equalsIgnoreCase("DELETE")){
            if(Config.DELETE)
                delete();
            else
                outStream.writeBytes(_405);
        }else if(method.equalsIgnoreCase("CONNECT")){
            if(Config.CONNECT)
                connect();
            else
                outStream.writeBytes(_405);
        }else if(method.equalsIgnoreCase("TRACE")){
            if(Config.TRACE)
                trace();
            else
                outStream.writeBytes(_405);
        }else if(method.equalsIgnoreCase("OPTIONS") && Config.OPTIONS){
            if(Config.OPTIONS)
                options();
            else
                outStream.writeBytes(_405);
        }else{
            outStream.writeBytes(_400);
        };
        
    }
    
    private void get() throws IOException{
        String response = "";
        File f = new File("www"+url);
        if(f.exists()){
            if(f.isDirectory()){
                for (String defaultPage : HTTPServer.defaultPages) {
                    if (url.endsWith("/")) {
                        f = new File("www"+url + defaultPage);
                    } else {
                        f = new File("www"+url+"/" + defaultPage);
                    }
                    if(f.exists()){
                        byte[] entity = ServerUtils.getBinaryFile(f);
                        response+=HTTP_VERSION+" 200 OK\r\n";
                        response+="Date: "+ServerUtils.getServerTime()+"\r\n";
                        response+="Connection: "+connection+"\r\n";
                        response+="Server: "+SERVER_NAME+"\r\n" ;
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
                response+=HTTP_VERSION+" 200 OK\r\n";
                response+="Connection: "+connection+"\r\n";
                response+="Server: "+SERVER_NAME+"\r\n" ;
                response+="Content-Length: "+entity.length+"\r\n";
                response+="Last-Modified:"+f.lastModified()+"\r\n";
                response+="\r\n";
                outStream.writeBytes(response);
                outStream.write(entity);
                
                return;
            }
        }
        outStream.writeBytes(_404);
    }
    
    private void post(){
        
    }
    
    private void head(){
        
    }
    
    private void put() throws IOException{
        File f = new File("www"+url);
        
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
        
        
        for(int i=0;i<body.size();i++){
            System.out.println(body.get(i));
            writer.write(body.get(i));
            writer.newLine();
        }
        writer.close();
        outStream.writeChars(_204);
    }
    
    private void delete() throws IOException{
        File f = new File("www"+url);
        if(f.exists() && f.isDirectory()){
            for (String defaultPage : HTTPServer.defaultPages) {
                if (url.endsWith("/")) {
                    f = new File("www"+url + defaultPage);
                }else {
                    f = new File("www"+url+"/" + defaultPage);
                }
                
                if(f.exists()){
                    if(f.delete()){
                    outStream.writeBytes(_204);
                    return;
                    }else{
                        System.out.println("not deleted");
                    }
                }
                
            }
        }else if(f.exists()){
            if(f.delete()){
                outStream.writeBytes(_204);
                return;
            }else{
                System.out.println("not deleted");
                    }
            return;
        }
        
        outStream.writeBytes(_404);
    }
    
    private void connect() throws IOException{
        outStream.writeBytes(HTTP_VERSION+" 200 Connection established\r\n" +
                             "Date: "+ServerUtils.getServerTime()+"\r\n"+
                             "Server: "+SERVER_NAME+"\r\n");
    }
    
    private void trace()throws IOException{
        String body ="";
        for(int i=0;i<request.size();i++)
            body+=request.get(i)+"\r\n";
        
        String response = "";
        response+=  HTTP_VERSION+" 200 OK\r\n" +
                    "Date: "+ServerUtils.getServerTime()+"\r\n"+
                    "Server: "+SERVER_NAME+"\r\n"  +
                    "Connection: "+connection+"\r\n" +
                    "Content-Type: message/http\r\n" +
                    "Content-Length: "+body.getBytes().length+"\r\n"+
                    "\r\n"+
                    body;
        outStream.writeBytes(response);
    }
    
    private void options() throws IOException{
        outStream.writeBytes(   HTTP_VERSION+" 200 OK\r\n" +
                                "Date: "+ServerUtils.getServerTime()+"\r\n" +
                                "Server: "+SERVER_NAME+"\r\n" +
                                "Allow: GET,HEAD,POST,DELETE,CONNECT,OPTIONS,TRACE\r\n" +
                                "Content-Type: httpd/unix-directory\r\n");
    }
    
    
}
