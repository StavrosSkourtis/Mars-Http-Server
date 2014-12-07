/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Phenom
 */
public class Logger {
    public static final String LOG_FILE = "log.txt";
    public static ArrayList<Connection> connections = new ArrayList<Connection>();
    static BufferedWriter writer;
    
    public static void init(){
        
    }
    
    public static void addRecord(String ip,int port,String requestLine,String responseCode){
        connections.add(new Connection(ip, port, requestLine, responseCode));
        try{
            BufferedReader reader = new BufferedReader(new FileReader("log.txt"));
            ArrayList<String> lines = new ArrayList<String>();
                
            String line;
            while( (line = reader.readLine()) != null){
                    lines.add(line);
            }
                
            reader.close();
                
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));
                for(String l: lines){
                    writer.write(l);
                    writer.newLine();
                }
            writer.write("Connected to"+ip+":"+port+" requested:"+requestLine+" responded with "+responseCode);
            writer.newLine();
            writer.close();
        }catch(IOException e){}
    }
    
    public static void addRecord(String text){
        
        try{
            BufferedReader reader = new BufferedReader(new FileReader("log.txt"));
            ArrayList<String> lines = new ArrayList<String>();
                
            String line;
            while( (line = reader.readLine()) != null){
                    lines.add(line);
            }
                
            reader.close();
                
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));
                for(String l: lines){
                    writer.write(l);
                    writer.newLine();
                }
            writer.write(text);
            writer.newLine();
            writer.close();
        }catch(IOException e){}
    }
    
    public static void dispose(){
        
    }
   
    
}
