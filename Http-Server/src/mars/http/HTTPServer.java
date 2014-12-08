package mars.http;

import java.net.*;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Properties;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.print.attribute.standard.ReferenceUriSchemesSupported;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stavros Skourtis 
 */
public class HTTPServer extends Thread{
    
    public static ArrayList<String> requests;
    
    private ServerSocket serverSocket;
    private boolean run;
    private String root;
    private boolean ssl;
    
    public HTTPServer(String root,int portNumber,boolean ssl,String sslFile,String sslPass) throws IOException{
        this.root = root;
        this.ssl = ssl;
        run = true;
        // create the server's socket
        if(ssl){
            try{
                String password = sslPass;
                String keystore = sslFile;

                /* Create keystore */
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(new FileInputStream(keystore), password.toCharArray());

                /* Get factory for the given keystore */
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(keyStore);
                SSLContext ctx = SSLContext.getInstance("SSL");
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(keyStore, password.toCharArray());
                // ...
                ctx.init(kmf.getKeyManagers(), null, null);
                SSLServerSocketFactory factory = ctx.getServerSocketFactory();

                serverSocket = (SSLServerSocket) factory.createServerSocket(portNumber);
            }catch(Exception e){e.printStackTrace();}
        }else{
            serverSocket = new ServerSocket(portNumber);
            serverSocket.setSoTimeout(15000);
        }
    }
        
    @Override
    public void run(){
        while(run){
            
                try{
                    Socket client; 
                    // Listen for new requests and accept them
                    if(ssl)
                        client = (SSLSocket) serverSocket.accept();
                    else
                        client = serverSocket.accept();
                        
                    new ClientThread(client,root).start();
                }catch(IOException e){
                   // e.printStackTrace();
                }
            
        }  
    }
    
    public void dispose() throws IOException{
        serverSocket.close();
        run = false;
    }
      
}
