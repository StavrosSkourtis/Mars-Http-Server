package mars.http;

import mars.utils.Config;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Phenom
 */
public class HTTPStatus {
    public static final String HTTP_VERSION = "HTTP/1.1";
    public static final String SERVER_NAME ="Mars alpha 1.0.0";    
    
    /**
     * @return 204 NO CONTENT Response
     */
    public static HTTPResponse code204(){
        HTTPResponse response = new HTTPResponse();
                
        response.setStatusCode(HTTP_VERSION+" 204 No Content");
        response.addHeader("Date", ServerUtils.getServerTime());
        response.addHeader("Server", SERVER_NAME);
        response.addHeader("Connection","keep-alive");
        response.addHeader("Content-Type","text/html");
        
        
        return response;
    }
    
    /**
     * @return 304 NOT MODIFIED Response
     */
    public static HTTPResponse code304(){
        HTTPResponse response = new HTTPResponse();
        
        response.setStatusCode(HTTP_VERSION+" 304 Not Modified");
        response.addHeader("Date", ServerUtils.getServerTime());
        response.addHeader("Server", SERVER_NAME);
        response.addHeader("Connection","keep-alive");
        response.addHeader("Vary","Accept-Encoding");
        
        return response;
    }
    
    /**
     * @return 400 BAD REQUEST Response
     */
    public static HTTPResponse code400(){
        HTTPResponse response = new HTTPResponse();
        
        response.setStatusCode(HTTP_VERSION+" 400 Bad Request");
        response.addHeader("Date", ServerUtils.getServerTime());
        response.addHeader("Server", SERVER_NAME);
        response.addHeader("Connection","keep-alive");
        
        return response;
    }
    
    /**
     * @return 404 NOT FOUND Response 
     */
    public static HTTPResponse code404(){
        HTTPResponse response = new HTTPResponse();
        
        String body404 = "<html><head>\n" +
                            "<title>404 Not Found</title>\n" +
                            "</head><body>\n" +
                            "<h1>404 Not Found</h1>\n" +
                            "<p>The requested URL was not found on this server.</p>\n" +
                            "<hr>\n" +
                            "<p>"+SERVER_NAME+"</p>\n" +
                            "</body></html>";
        
        response.setStatusCode(HTTP_VERSION+" 404 Not Found");
        response.addHeader("Date", ServerUtils.getServerTime());
        response.addHeader("Server", SERVER_NAME);
        response.addHeader("Connection","keep-alive");
        response.addHeader("Content-Length",String.valueOf( body404.getBytes().length) );
        response.addHeader("Content-Type","text/html");
        
        response.addBody(body404.getBytes());
        
        return response;
    }
    
    /**
     * @return 405 METHOD NOT ALLOWED Response
     */
    public static HTTPResponse code405(){
        HTTPResponse response = new HTTPResponse();
        
        String allow= "";
        
        if(Config.GET)
            allow+="GET";
        else if(Config.POST)
            allow+=",POST";
        else if(Config.HEAD)
            allow+=",HEAD";
        else if(Config.TRACE)
            allow+=",TRACE";
        else if(Config.OPTIONS)
            allow+=",OPTIONS";
        else if(Config.DELETE)
            allow+=",DELETE";
        else if(Config.PUT)
            allow+=",PUT";
        else if(Config.CONNECT)
            allow+=",CONNECT";
        
        response.setStatusCode(HTTP_VERSION+" 405 Method Not Allowed");
        response.addHeader("Date", ServerUtils.getServerTime());
        response.addHeader("Server", SERVER_NAME);
        response.addHeader("allow", allow);
        response.addHeader("Connection","keep-alive");
        response.addHeader("Content-Type","text/html");
                
        return response;
    }
    
    /**
     * @return 406 Not Acceptable Response
     */
    public static HTTPResponse code406(){
        HTTPResponse response = new HTTPResponse();
        
        response.setStatusCode(HTTP_VERSION+" 406 Not Acceptable");
        response.addHeader("Date", ServerUtils.getServerTime());
        response.addHeader("Server", SERVER_NAME);
        response.addHeader("Connection","keep-alive");

                
        return response;
    }
    
    /**
     * @return 412 PRECONDITION FAILED Response 
     */
    public static HTTPResponse code412(){
        HTTPResponse response = new HTTPResponse();
        
        response.setStatusCode(HTTP_VERSION+" 412 Precondition Failed");
        response.addHeader("Date", ServerUtils.getServerTime());
        response.addHeader("Server", SERVER_NAME);
        response.addHeader("Connection","keep-alive");        
        
        return response;
    }
    
    /**
     * @return 505 HTTP VERSION NOT SUPORTED Response
     */
    public static HTTPResponse code505(){
        HTTPResponse response = new HTTPResponse();
        
        response.setStatusCode(HTTP_VERSION+" 505 HTTP Version Not Supported");
        response.addHeader("Date", ServerUtils.getServerTime());
        response.addHeader("Server", SERVER_NAME);
        response.addHeader("Connection","keep-alive");        
        
        return response;
    }
        
}
