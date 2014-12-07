package mars.launch;

import mars.http.HTTPServer;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import mars.cli.CommandLineInterface;
import mars.gui.GuiLauncher;
import mars.http.HTTPStatus;
import mars.http.ServerUtils;
import mars.utils.Config;
import mars.utils.Logger;
import mars.utils.Website;
import mars.utils.XMLFile;
import mars.utils.XMLNode;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Stavros Skourtis
 */
public class Launcher {
    
    public static void main(String args[]) throws IOException{
        
        /*
            Initialise the Logger .... 
        */
        Logger.init();
        
        
        Logger.addRecord("\r\n"+HTTPStatus.SERVER_NAME+"\r\n"+ServerUtils.getServerTime()+"\r\n-------------------------------------------------");
        Logger.addRecord("Loading config...");
        /*
            Initialise config file ....
        */
        Config.init();
        Logger.addRecord("Config loaded");
        
        
        /*  Old code for single server capabilities
        HTTPServer server = new HTTPServer("www",81,false);
        server.start();
        */
        
        
        Logger.addRecord("Starting sites..");
        /*
            Website initialization
        */
        for(Website site : Config.WEBSITES){
            if(site.isOnline())
                site.start();
        }
        Logger.addRecord("Sites started");       
        
        /*
            Check if graphical enviroment exists
            if true (a linux server without gui) then we start the command line interface
            if false we start the graphical user interface
        */
        Logger.addRecord("Checking graphical enviroment...");
        if (true){//GraphicsEnvironment.isHeadless() || (args.length >=2) && args[1].equals("-cli")) {
            Logger.addRecord("Command line started");
            CommandLineInterface.run();
        } else {
            Logger.addRecord("starting gui..");
            new GuiLauncher();
            Logger.addRecord("gui ready");
        }
        
    }
}
