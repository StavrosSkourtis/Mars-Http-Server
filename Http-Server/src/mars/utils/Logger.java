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
    static BufferedWriter writer;
    
    public static void init(){
        try{
            File file = new File(LOG_FILE);
            if(file.exists()){
                BufferedReader reader = new BufferedReader(new FileReader(file));
                ArrayList<String> lines = new ArrayList<String>();
                
                String line;
                while( (line = reader.readLine()) != null){
                    lines.add(line);
                }
                
                reader.close();
                
                writer = new BufferedWriter(new FileWriter(file));
                
                for(String l: lines){
                    writer.write(l);
                    writer.newLine();
                }
                
            }else{
                writer = new BufferedWriter(new FileWriter(file));
            }
        }catch(IOException e){}
    }
    
    public static void addRecord(String record){
        try{
            writer.write(record);
            writer.newLine();
        }catch(IOException e){}
    }
    
    public static void dispose(){
        try{
            writer.close();
        }catch(IOException e){}
    }
   
    
}
