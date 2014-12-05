/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.launch;

import java.io.IOException;
import mars.http.HTTPServer;

/**
 *
 * @author Phenom
 */
public class Website {
    private String path;
    private int port;
    private String name;
    private HTTPServer server;
           
    public Website(String path,int port) throws IOException{
        this.port = port;
        this.path = path;
        
        server = new HTTPServer(port);
        server.start();
    }
}
