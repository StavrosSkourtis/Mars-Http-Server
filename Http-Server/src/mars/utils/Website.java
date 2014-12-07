/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.utils;

import java.io.IOException;
import mars.http.HTTPServer;

/**
 *
 * @author Phenom
 */
public class Website {
    public static int ID=0;
    private int id;
    private int port;
    private String path;
    private boolean ssl;
    private boolean online;
    private HTTPServer server;
    private boolean running = false;
    private boolean neverStarted = true;
    
    public Website(int port, String path, boolean ssl, boolean online) throws IOException{
        this.port = port;
        this.path = path;
        this.ssl = ssl;
        this.online = online;
        server = new HTTPServer(path,port,ssl);
        
        id = ID++;
    }
    
    public void start() throws IOException{
        server.start();
        neverStarted = false;
    }
    
    public void pause() throws IOException{
        server.dispose();
    }
    
    public void resume() throws IOException{
        if(neverStarted){
            start();
            return;
        }
        server.dispose();
        server = new HTTPServer(path, port, ssl);
        server.start();
    }
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    
}
