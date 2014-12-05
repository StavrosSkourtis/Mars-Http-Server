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
    public static final String _204="HTTP/1.1 204 No Content\r\n" +
                                    "Date: "+ServerUtils.getServerTime()+"\r\n" +
                                    "Server: http server\r\n" +
                                    "Content-type: text/html\r\n" +
                                    "Connection: Closed\r\n\r\n";
    
    public static final String _400="HTTP/1.1 400 Bad Request\r\n\r\n";
    public static final String _401="";
    public static final String _402="";
    public static final String _403="";
    public static final String _404="HTTP/1.1 404 Not Found\r\n"+
                                    "Date: "+ServerUtils.getServerTime()+"\r\n" +
                                    "Server: http server made with java\r\n" +
                                    "Vary: Accept-Encoding\r\n" +
                                    "Content-Encoding: gzip\r\n" +
                                    "Content-Length: 246\r\n" +
                                    "Keep-Alive: timeout=15, max=99\r\n" +
                                    "Connection: Keep-Alive\r\n" +
                                    "Content-Type: text/html; charset=iso-8859-1\r\n\r\n\r\n";
                                    
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

}
