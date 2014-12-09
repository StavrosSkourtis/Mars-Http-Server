/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import mars.http.Entity;

/**
 *
 * @author Stavros Skourtis
 */
public class Config {
    private static XMLFile config;
    public static boolean GET;
    public static boolean POST;
    public static boolean HEAD;
    public static boolean TRACE;
    public static boolean OPTIONS;
    public static boolean PUT;
    public static boolean DELETE;
    public static boolean CONNECT;
    
    public static boolean PHP_ENABLED;
    public static String PHP_PATH;
    
    public static ArrayList<String> DEFAULT_PAGES;
    
    public static ArrayList<String> BLACK_LIST;
    
    public static ArrayList<Website> WEBSITES;
    
    public static ArrayList<Entity> entities;
    
    /*
        Loads settings from config.xml file
    */
    public static void init() throws IOException{
        if(config==null){
            config = new XMLFile();
            config.read("config.xml");
        }
        if(entities==null)
            entities = new ArrayList<>();
        
        
        /*
            Loading default pages from config
        */
        ArrayList<XMLNode> defaultPages = config.getChildrenByName("defaults").get(0).getChildrenByName("page");
        DEFAULT_PAGES = new ArrayList<String>();
        for(XMLNode page : defaultPages){
            DEFAULT_PAGES.add(page.getAttribute("value") );
        }
        
        /*
            Loading which http methods are allowed from config file and update the Config class variable
        */
        ArrayList<XMLNode> methods = config.getChildrenByName("httpmethods").get(0).getChildrenByName("method");
        
        for(XMLNode node:methods){
            if(node.getAttribute("name").equals("get"))
                GET = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("post"))
                POST = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("head"))
                HEAD = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("trace"))
                TRACE = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("options"))
                OPTIONS = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("delete"))
                DELETE = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("put"))
                PUT = node.getAttribute("allow").equals("true");
            else if(node.getAttribute("name").equals("connect"))
                CONNECT = node.getAttribute("allow").equals("true");
        }
        
        
        /*
            Loading php settings
        */
        PHP_PATH = config.getChildrenByName("php").get(0).getAttribute("path").replace('&',' ');
        PHP_ENABLED = config.getChildrenByName("php").get(0).getAttribute("allow").equals("true");
        
        
        /*
            Loading black list
        */
        BLACK_LIST = new ArrayList<String>();
        ArrayList<XMLNode> ips = config.getChildrenByName("blacklist").get(0).getChildrenByName("ip");
        
        for(XMLNode ip : ips){
            BLACK_LIST.add(ip.getAttribute("address"));
        }
        
        
        /*
            Load website settings from config
        */
        if(WEBSITES!=null){
            for(Website site: WEBSITES)
                site.pause();
        }
        
        WEBSITES = new ArrayList<Website>();
        ArrayList<XMLNode> siteNodes = config.getChildrenByName("websites").get(0).getChildrenByName("site");
        
        for(XMLNode node : siteNodes){            
            String path = node.getAttribute("path");
            boolean online = node.getAttribute("online").equals("true");
            boolean ssl = node.getAttribute("ssl").equals("true");
            int port = Integer.parseInt(node.getAttribute("port"));
            String sslFile = node.getAttribute("sslfile");
            String sslPass = node.getAttribute("sslpass");
            WEBSITES.add(new Website(port, path, ssl, online,sslFile,sslPass));
        }
    }
    
    public static void saveChanges()throws IOException{
        
        /*
            Save php settings 
        */
        config.getChildrenByName("php").get(0).setAttribute("path", PHP_PATH.replace(' ', '&'));
        config.getChildrenByName("php").get(0).setAttribute("allow", PHP_ENABLED?"true":"false");
        
        
        /*
            Saving default pages
        */
        ArrayList<XMLNode> methods = config.getChildrenByName("httpmethods").get(0).getChildrenByName("method");
        
        for(XMLNode node:methods){
            if(node.getAttribute("name").equals("get"))
                node.setAttribute("allow", GET?"true":"false");
            else if(node.getAttribute("name").equals("post"))
                node.setAttribute("allow", POST?"true":"false");
            else if(node.getAttribute("name").equals("head"))
                node.setAttribute("allow", HEAD?"true":"false");
            else if(node.getAttribute("name").equals("trace"))
                node.setAttribute("allow", TRACE?"true":"false");
            else if(node.getAttribute("name").equals("options"))
                node.setAttribute("allow", OPTIONS?"true":"false");
            else if(node.getAttribute("name").equals("delete"))
                node.setAttribute("allow", DELETE?"true":"false");
            else if(node.getAttribute("name").equals("put"))
                node.setAttribute("allow", PUT?"true":"false");
            else if(node.getAttribute("name").equals("connect"))
                node.setAttribute("allow", CONNECT?"true":"false");
        }
        
        
        /*
            Save black list
        */
        XMLNode blacklist = config.getChildrenByName("blacklist").get(0);
        blacklist.removeAllChildNodes();
        
        for(String ip : BLACK_LIST){
            XMLNode temp = new XMLNode("ip");
            temp.addAttribute("address", ip);
            
            blacklist.addChildNode(temp);
        }
        
        /*
            Save default pages
        */
        XMLNode defaultPages = config.getChildrenByName("defaults").get(0);
        defaultPages.removeAllChildNodes();
        
        for(String page : DEFAULT_PAGES){
            XMLNode temp = new XMLNode("page");
            temp.addAttribute("value", page);
            
            defaultPages.addChildNode(temp);
        }
        
        
        /*
            Save website settings
        */
        XMLNode websites = config.getChildrenByName("websites").get(0);
        websites.removeAllChildNodes();
        for(Website site : WEBSITES){            
            XMLNode temp = new XMLNode("site");
            
            temp.addAttribute("path", site.getPath());
            temp.addAttribute("port", String.valueOf(site.getPort()));
            temp.addAttribute("online", site.isOnline()?"true":"false");
            temp.addAttribute("ssl", site.isSsl()?"true":"false");
            temp.addAttribute("sslfile", site.getSslFile());
            temp.addAttribute("sslpass", site.getSslPass());
            websites.addChildNode(temp);
        }
        
        
        /*
            write changes to file
        */
        config.write("config.xml");
        
    }
    
    /*
        Etag utils
    */
    
    public static Entity getEntity(String etag){
        for(int i=0;i<entities.size();i++)
            if (entities.get(i).getEtag().equals(etag)) {
                return entities.get(i);
            }
        return null;
    }
    
    public static Entity getEntity(File file){
        for(int i=0;i<entities.size();i++)
            if (entities.get(i).getFile().equals(file) ) {
                return entities.get(i);
            }
        return null;
    }
    
    public static void createEntity(File file){
        entities.add(new Entity(file));
    }
}