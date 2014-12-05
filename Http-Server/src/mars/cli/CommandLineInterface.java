/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import mars.http.HTTPStatus;

/**
 *
 * @author Phenom
 */
public class CommandLineInterface {
    static boolean exit = false;

    
    public static void run(){
        System.out.println(HTTPStatus.SERVER_NAME);
        System.out.println();
        System.out.println("created by Stavros Skourtis");
        System.out.println();
        try{
            while(!exit){
                System.out.print("> ");
                String command = readString();
                execute(command);
            }
        }catch(Exception e){e.printStackTrace();}
        
        System.exit(0);
    }
    
    public static void execute(String command){
        command = command.trim();
        if(command.length()==0)
            return;
        
        String args[] = command.split(" ");
        
        if(args[0].equals("exit")){
            Commands.exit(args);
        }else{
            System.out.println("command not found");
        }
    }
    
    
    private static String readString() throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        return reader.readLine();
    }
}
