package mars.launch;

import mars.http.HTTPServer;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import mars.cli.CommandLineInterface;
import mars.gui.GuiLauncher;
import mars.utils.Config;
import mars.utils.Logger;
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
    public static ArrayList<Website> websites;
    public static void main(String args[]) throws IOException{
        /*
            Opening config file
        */
        XMLFile config = new XMLFile();
        config.read("config.xml");
        
        
        /*
            Reading default pages from config
        */
        ArrayList<XMLNode> defaultPages = config.getChildrenByName("defaults").get(0).getChildrenByName("page");
        String pages[] = new String[defaultPages.size()];
        for(int i=0;i<pages.length;i++)
            pages[i] = defaultPages.get(i).getAttribute("value");
         
        HTTPServer.defaultPages = pages;
        /*
            Read which http methods are allowed from config file and update the Config class variable
        */
        ArrayList<XMLNode> methods = config.getChildrenByName("httpmethods").get(0).getChildrenByName("method");
        
        for(XMLNode node:methods){
            if(node.getAttribute("name").equals("get"))
                Config.GET = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("post"))
                Config.POST = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("head"))
                Config.HEAD = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("trace"))
                Config.TRACE = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("options"))
                Config.OPTIONS = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("delete"))
                Config.DELETE = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("put"))
                Config.PUT = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("connect"))
                Config.CONNECT = node.getAttribute("allow").equals("true");
        }
        
        HTTPServer server = new HTTPServer(81);
        server.start();
        
        /*
            initialise the Logger .... 
        */
        Logger.init();
        
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
