package mars.launch;

import mars.http.HTTPServer;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import mars.cli.CommandLineInterface;
import mars.gui.GuiLauncher;
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
            Initialise config file ....
        */
        Config.init();
        
        /*
            Initialise the Logger .... 
        */
        Logger.init();
        
        
        
        /*  Old code for single server capabilities
        HTTPServer server = new HTTPServer("www",81,false);
        server.start();
        */
        
        /*
            Website initialization
        */
        for(Website site : Config.WEBSITES){
            if(site.isOnline())
                site.start();
        }
       
        
        /*
            Check if graphical enviroment exists
            if true (a linux server without gui) then we start the command line interface
            if false we start the graphical user interface
        */
        if (GraphicsEnvironment.isHeadless()) {
            CommandLineInterface.run();
        } else {
            new GuiLauncher();
        }
        
    }
}
