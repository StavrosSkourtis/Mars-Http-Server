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
import mars.http.ServerUtils;

/**
 *
 * @author Phenom
 */
public class CommandLineInterface {
    static boolean exit = false;

    
    public static void run(){
        System.out.println(HTTPStatus.SERVER_NAME);
        System.out.println(ServerUtils.getServerTime());
        System.out.println();
        try{
            while(!exit){
                System.out.print("mars > ");
                String command = readString();
                execute(command);
            }
        }catch(Exception e){e.printStackTrace();}
        
        System.exit(0);
    }
    
    public static void execute(String command) throws IOException{
        command = command.trim();
        if(command.length()==0)
            return;
        
        String args[] = command.split(" ");
        
        if(args[0].equals("exit")){
            Commands.exit(args);
        }else if(args[0].equals("list")){
            Commands.list(args);
        }else if(args[0].equals("site")){
            Commands.site(args);
        }else if(args[0].equals("blacklist")){
            Commands.blacklist(args);
        }else if(args[0].equals("php")){
            Commands.php(args);
        }else if(args[0].equals("defaultpage")){
            Commands.defaultfile(args);
        }else if(args[0].equals("methods")){
            Commands.methods(args);
        }else if(args[0].equals("status")){
            Commands.status(args);
        }else if(args[0].equals("time")){
            Commands.time(args);
        }else{
            System.out.println("command not found");
        }
        System.out.println();
    }
    
    
    public static String readString() throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
        return reader.readLine();
    }
}
