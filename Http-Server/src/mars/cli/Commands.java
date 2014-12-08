/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.cli;

import java.io.IOException;
import mars.http.ServerUtils;
import mars.utils.Config;
import mars.utils.Connection;
import mars.utils.Logger;
import mars.utils.Website;

/**
 *
 * @author Phenom
 */
public class Commands {
    
    /*
        Exit command.
        closes application
    */
    static void exit(String [] args){
        CommandLineInterface.exit = true;
    }
    
    /*
        List commamand ,lists connectins 
    */
    static void list(String [] args){
        if(args.length >=2 && args[1].equals("connections")){
            System.out.println("IP                       Port                        Request                 Response");
            for(Connection con : Logger.connections)
                System.out.println(String.format("%-10s", con.getIp())+
                                   String.format("%-10s", con.getPort() )+
                                   String.format("%-10s", con.getRequestline() )+
                                   String.format("%-10s", con.getResponseCode()) );
        }else if(args.length==1){
            
        }
    }
    
    /*
        site , proccesses site
    */
    static void site(String [] args) throws IOException{
        if(args.length==1){
            System.out.println("Websites");
            for(Website site:Config.WEBSITES){
                System.out.println("\n"+site.getId()+"::"+site.getPort());
                System.out.println("Path   : "+site.getPath());
                System.out.println("Port   : "+site.getPort());
                System.out.println("Online : "+site.isOnline());
                System.out.println("SSL    : "+site.isSsl());
            }
        }else if(args.length==2 && args[1].equals("-new")){
            String path,sslFile="",sslPass="";
            int port;
            boolean ssl,online;
            
            System.out.print("Site path :");
            path = CommandLineInterface.readString();
            
            String temp=null;
            do{
                if(temp!=null)
                    System.out.println("Please enter a valid port number.");
                System.out.print("Site port :");
                temp =CommandLineInterface.readString();
            }while( !(ServerUtils.isNumeric(temp) && !temp.equals("") && Integer.parseInt(temp)<65536 && Integer.parseInt(temp)>=0 ));
            port = Integer.parseInt(temp);
            
            System.out.println("Enable ssl? (yes/no) :");
            ssl = CommandLineInterface.readString().equalsIgnoreCase("yes");
            if(ssl){
                System.out.println("Enter ssl keystore file: ");
                sslFile = CommandLineInterface.readString();
                    
                System.out.println("Enter ssl pass  : ");
                sslPass = CommandLineInterface.readString();
            }
            
            System.out.println("Set online? (yes/no) :");
            online = CommandLineInterface.readString().equalsIgnoreCase("yes");
            
            Config.WEBSITES.add(new Website(port, path, ssl, online,sslFile,sslPass));
            Config.saveChanges();
            
            System.out.println("Site added successfuly!\n");
            
        }else if(args.length==3 && args[1].equals("-edit")){
            if(ServerUtils.isNumeric(args[2])){
                int index = Integer.parseInt(args[2]);
                
                if(index<Config.WEBSITES.size()){
                    
                    System.out.println("Enter path (leave blank to not modify) : ");
                    
                    String path = CommandLineInterface.readString();
                    
                    if(!path.equals(""))
                        Config.WEBSITES.get(index).setPath(path);
                    
                    System.out.println("Enter port (Enter -1 to not modify) : ");
                    int port;
                    String temp=null;
                    do{
                        if(temp!=null)
                            System.out.println("Please enter a valid port number.");
                        System.out.print("Site port :");
                        temp =CommandLineInterface.readString();
                    }while(! (ServerUtils.isNumeric(temp) && !temp.equals("") &&((temp.equals("-1")) || (Integer.parseInt(temp)<65536 && Integer.parseInt(temp)>=0 )) ) );
                    port = Integer.parseInt(temp);
                    
                    if(port!=-1){
                        Config.WEBSITES.get(index).setPort(port);
                    }
                    
                    System.out.println("Enter ssl keystore file (leave blank to not modify) : ");
                    
                    String sslFile = CommandLineInterface.readString();
                    
                    if(!path.equals("")){
                        Config.WEBSITES.get(index).setSslFile(sslFile);
                        System.out.println("Enter ssl pass (leave blank to not modify) : ");
                    
                        String sslPass = CommandLineInterface.readString();

                        if(!path.equals(""))
                            Config.WEBSITES.get(index).setSslPass(sslPass);
                    }
                    
                    Config.WEBSITES.get(index).resume();
                    Config.saveChanges();
                    System.out.println("Site changed successfuly\n");
                    
                }else
                    System.out.println("Please enter a valid index.\n");
            }else
                System.out.println("Please enter a valid index.\n");
        }else if(args.length==3 && args[1].equals("-delete")){
            
            if(ServerUtils.isNumeric(args[2])){
                int index = Integer.parseInt(args[2]);
                
                if(index<Config.WEBSITES.size()){
                    System.out.println("Are you sure you want to delete site "+index+" ?  (Y/N)");
                    
                    boolean delete = CommandLineInterface.readString().equalsIgnoreCase("Y");
                    
                    if(delete){
                        Config.WEBSITES.remove(index);
                        Config.saveChanges();
                        System.out.println("site deleted\n\n");
                    }
                }else
                    System.out.println("Please enter a valid index.\n");
            }else
                System.out.println("Please enter a valid index.\n");
            
        }else if(args.length==3 && args[1].equals("-stop")){
            
            if(ServerUtils.isNumeric(args[2])){
                int index = Integer.parseInt(args[2]);
                
                if(index<Config.WEBSITES.size()){
                    System.out.println("Are you sure you want to stop site "+index+" ?  (Y/N)");
                    
                    boolean delete = CommandLineInterface.readString().equalsIgnoreCase("Y");
                    
                    if(delete){
                        Config.WEBSITES.get(index).setOnline(false);
                        Config.WEBSITES.get(index).pause();
                        Config.saveChanges();
                        System.out.println("site stoped\n\n");
                    }
                }else
                    System.out.println("Please enter a valid index.\n");
            }else
                System.out.println("Please enter a valid index.\n");
            
        }else if(args.length==3 && args[1].equals("-start")){
            if(ServerUtils.isNumeric(args[2])){
                int index = Integer.parseInt(args[2]);
                
                if(index<Config.WEBSITES.size()){
                    System.out.println("Are you sure you want to start site "+index+" ?  (Y/N)");
                    
                    boolean delete = CommandLineInterface.readString().equalsIgnoreCase("Y");
                    
                    if(delete){
                        Config.WEBSITES.get(index).setOnline(true);
                        Config.WEBSITES.get(index).resume();
                        Config.saveChanges();
                        System.out.println("site started\n\n");
                    }
                }else
                    System.out.println("Please enter a valid index.\n");
            }else
                System.out.println("Please enter a valid index.\n");
        }
    }
    
    /*
        default file
    */
    static void defaultfile(String [] args){
        if(args.length==1){
            System.out.println("Default files");
            for(String page:Config.DEFAULT_PAGES)
                System.out.println(page);
        }else if(args.length==2){
            
            Config.DEFAULT_PAGES.add(args[1]);
            
            try{
                Config.saveChanges();
            }catch(IOException e){
                Logger.addRecord(e.getMessage());
            }
            System.out.println("Page name added to defaults");
        }else if(args.length==3 && args[1].equals("remove")){
            for(int i=0;i<Config.DEFAULT_PAGES.size();i++)
                if(args[2].equals(Config.DEFAULT_PAGES.get(i))){
                    Config.DEFAULT_PAGES.remove(i);
                    System.out.println("deleted at index "+i);
                }
            
            try{
                Config.saveChanges();
            }catch(IOException e){
                Logger.addRecord(e.getMessage());
            }
        }else{
            
        }
    }
    
    /*
        PHP Settings
    */
    static void php(String [] args){
        if(args.length==1){
            System.out.println("Path    :"+Config.PHP_PATH);
            System.out.println("Enabled :"+Config.PHP_ENABLED);
        }else if(args.length==2 && args[1].equals("enable")){
            Config.PHP_ENABLED = true;
            
            try{
            Config.saveChanges();
            }catch(IOException e){
                Logger.addRecord(e.getMessage());
            }
            System.out.println("php enabled");
        }else if(args.length==2 && args[1].equals("disable")){
            Config.PHP_ENABLED = false;
            
            try{
            Config.saveChanges();
            }catch(IOException e){
                Logger.addRecord(e.getMessage());
            }
            System.out.println("php disabled");
        }else if(args.length==3 && args[1].equals("-p")){
            Config.PHP_PATH = args[2];
            
            try{
                Config.saveChanges();
            }catch(IOException e){
                Logger.addRecord(e.getMessage());
            }
            System.out.println("php path updated");
        }else{
            System.out.println("To view php settings : php");
            System.out.println("To enable php : php enable");
            System.out.println("To disable php : php disable");
            System.out.println("To change php path : php -p (PATH)");
        }
    }
    
    /*
        Blacklist
    */
    static void blacklist(String [] args){
        if(args.length==1){
            System.out.println("Blacklisted IPs");
            for(String ip:Config.BLACK_LIST)
                System.out.println(ip);
        }else if(args.length==2){
            
            String ipParts[] = args[1].split("\\.");
            
           
            if(ipParts.length==4)
                if(ServerUtils.isNumeric(ipParts[0]) && Integer.parseInt(ipParts[0])>=0 && Integer.parseInt(ipParts[0])<=255 &&
                   ServerUtils.isNumeric(ipParts[1]) && Integer.parseInt(ipParts[1])>=0 && Integer.parseInt(ipParts[1])<=255 &&
                   ServerUtils.isNumeric(ipParts[2]) && Integer.parseInt(ipParts[2])>=0 && Integer.parseInt(ipParts[2])<=255 &&
                   ServerUtils.isNumeric(ipParts[3]) && Integer.parseInt(ipParts[3])>=0 && Integer.parseInt(ipParts[3])<=255){

                    Config.BLACK_LIST.add(args[1]);
                    try{
                        Config.saveChanges();
                    }catch(IOException e){
                        Logger.addRecord(e.getMessage());
                    }
                    System.out.println("IP successfuly added to the black list");
                }else{
                    System.out.println("IP not valid");
                }
            else
                System.out.println("IP not valid");
            
        }else if(args.length==3 && args[1].equals("remove")){
            for(int i=0;i<Config.BLACK_LIST.size();i++)
                if(args[2].equals(Config.BLACK_LIST.get(i)))
                    Config.BLACK_LIST.remove(i);
            System.out.println("removed successfuly");
            try{
                Config.saveChanges();
            }catch(IOException e){
                Logger.addRecord(e.getMessage());
            }
        }else{
            
        }
    }
    
    /*
        Methods
    */
    static void methods(String [] args){
        if(args.length==1){
            System.out.println("GET     "+(Config.GET?"allowed":"disabled"));
            System.out.println("POST    "+(Config.POST?"allowed":"disabled"));
            System.out.println("HEAD    "+(Config.HEAD?"allowed":"disabled"));
            System.out.println("CONNECT "+(Config.CONNECT?"allowed":"disabled"));
            System.out.println("OPTIONS "+(Config.OPTIONS?"allowed":"disabled"));
            System.out.println("DELETE  "+(Config.DELETE?"allowed":"disabled"));
            System.out.println("PUT     "+(Config.PUT?"allowed":"disabled"));
            System.out.println("TRACE   "+(Config.TRACE?"allowed":"disabled"));
        }else if(args.length==3 && args[1].equals("enable")){
            if(args[2].equalsIgnoreCase("GET"))
                Config.GET = true;
            else if(args[2].equalsIgnoreCase("POST"))
                Config.POST = true;
            else if(args[2].equalsIgnoreCase("HEAD"))
                Config.HEAD = true;
            else if(args[2].equalsIgnoreCase("TRACE"))
                Config.TRACE = true;
            else if(args[2].equalsIgnoreCase("OPTIONS"))
                Config.OPTIONS = true;
            else if(args[2].equalsIgnoreCase("PUT"))
                Config.PUT = true;
            else if(args[2].equalsIgnoreCase("DELETE"))
                Config.DELETE = true;
            else if(args[2].equalsIgnoreCase("CONNECT"))
                Config.CONNECT = true;
            else{
                System.out.println("Methods doesnt exist");
                return;
            }
            System.out.println(args[2]+" enabled");
            try{
                Config.saveChanges();
            }catch(IOException e){
                Logger.addRecord(e.getMessage());
            }
        }else if(args.length==3 && args[1].equals("disable")){
            if(args[2].equalsIgnoreCase("GET"))
                Config.GET = false;
            else if(args[2].equalsIgnoreCase("POST"))
                Config.POST = false;
            else if(args[2].equalsIgnoreCase("HEAD"))
                Config.HEAD = false;
            else if(args[2].equalsIgnoreCase("TRACE"))
                Config.TRACE = false;
            else if(args[2].equalsIgnoreCase("OPTIONS"))
                Config.OPTIONS = false;
            else if(args[2].equalsIgnoreCase("PUT"))
                Config.PUT = false;
            else if(args[2].equalsIgnoreCase("DELETE"))
                Config.DELETE = false;
            else if(args[2].equalsIgnoreCase("CONNECT"))
                Config.CONNECT = false;
            else{
                System.out.println("Methods doesnt exist");
                return;
            }
            System.out.println(args[2]+" disabled");
            try{
                Config.saveChanges();
            }catch(IOException e){
                Logger.addRecord(e.getMessage());
            }
        }
    }
    
    static void status(String [] args){
        
        if(args.length==1){
            
            int runningWebsites=0;
            String [] lines = new String[Config.WEBSITES.size()];
            
            for(int i=0;i<lines.length;i++){
                lines[i] = "";
                lines[i]+= Config.WEBSITES.get(i).getId()+"::"+Config.WEBSITES.get(i).getPort();
                if(Config.WEBSITES.get(i).isOnline()){
                    lines[i]+="\t\tonline";
                    runningWebsites++;
                }
            }
            
            System.out.println(runningWebsites+" running websites ,   PHP:"+Config.PHP_ENABLED);
            System.out.println("\nSite\t\tRunning");
            
            for(int i=0;i<lines.length;i++)
                System.out.println(lines[i]);
            System.out.println();
        }
    }

    static void time(String[] args) {
        System.out.println(ServerUtils.getServerTime()+"\n");
    }
    
}
