/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.utils;

/**
 *
 * @author Phenom
 */
public class Connection {
    private String ip;
    private int port;
    private String requestline;
    private String responseCode;

    public Connection(String ip, int port, String requestline, String responseCode) {
        this.ip = ip;
        this.port = port;
        this.requestline = requestline;
        this.responseCode = responseCode;
    }
    
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRequestline() {
        return requestline;
    }

    public void setRequestline(String requestline) {
        this.requestline = requestline;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
