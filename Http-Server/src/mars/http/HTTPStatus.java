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
public class HTTPStatus {
    public static final String HTTP_VERSION = "HTTP/1.1";
    public static final String SERVER_NAME ="Mars alpha 1.0.0";
    
    public static final String _204= HTTP_VERSION+" 204 No Content\r\n" +
                                    "Date: "+ServerUtils.getServerTime()+"\r\n" +
                                    "Server: "+SERVER_NAME+"\r\n" +
                                    "Content-type: text/html\r\n" +
                                    "Connection: keep-alive\r\n\r\n";
    
    public static final String _304= HTTP_VERSION+" 304 Not Modified\r\n" +
                                    "Date: "+ServerUtils.getServerTime()+"\r\n" +
                                    "Server: "+SERVER_NAME+"\r\n" +
                                    "Keep-Alive: keep-alive\r\n" +
                                    "Cache-Control: max-age=21600\r\n\r\n";
    
    public static final String _400=HTTP_VERSION+" 400 Bad Request\r\n\r\n";
    public static final String _401="";
    public static final String _402="";
    public static final String _403="";
    public static String _404;
                                    
    public static final String _405="";
    public static final String _406="";
    public static final String _407="";
    public static final String _408="";
    public static final String _409="";
    public static final String _410="";
    public static final String _411="";
    public static final String _412="";
    public static final String _413="";
    public static final String _414="";
    public static final String _415="";
    public static final String _416="";
    public static final String _417="";

    public static final String _500="";
    public static final String _501="";
    public static final String _502="";
    public static final String _503="";
    public static final String _504="";
    public static final String _505="";
    
    
    public static void init(){
        String body404 = "<html><head>\n" +
                            "<title>404 Not Found</title>\n" +
                            "</head><body>\n" +
                            "<h1>Not Found</h1>\n" +
                            "<p>The requested URL was not found on this server.</p>\n" +
                            "<hr>\n" +
                            "<address>"+SERVER_NAME+"</address>\n" +
                            "</body></html>";
        
        _404= HTTP_VERSION+" 404 Not Found\r\n"+
             "Date: "+ServerUtils.getServerTime()+"\r\n" +
             "Server: "+SERVER_NAME+"\r\n" +
             "Content-Length : "+body404.getBytes().length+"\r\n"+
             "Connection: Keep-Alive\r\n"+ 
             "Vary: Accept-Encoding\r\n" +
             "Content-Length: 246\r\n" +
             "Keep-Alive: timeout=15, max=100\r\n" +
             "Content-Type: text/html\r\n" +
             "\r\n"+body404;
        
        
             
    }
        
}
