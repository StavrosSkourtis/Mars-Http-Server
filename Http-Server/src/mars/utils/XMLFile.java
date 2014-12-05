package mars.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Stavros Skourtis
 */
public class XMLFile {
    private String xml;
    private ArrayList<XMLNode> nodes;
    boolean hasError = false;
    
    public XMLFile(){
        nodes = new ArrayList<XMLNode>();
    }
    public XMLFile(String xml){
        nodes = new ArrayList<XMLNode>();
        update(xml);
    }
    
    public void read(String filePath) throws IOException{
    	String text="",line;
    	BufferedReader reader = new BufferedReader(new FileReader(filePath));
    	
    	
    	while((line = reader.readLine())!=null){
    		text+=line;
    	}
    	
    	reader.close();
    	update(text);
    }
    
    public void write(String filePath) throws IOException{
    	BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
    	writer.write(this.toString());
    	writer.close();
    }
    
    public void update(String xml){
        nodes = new ArrayList<XMLNode>();
        while(xml.contains("<")||xml.contains(">")){ 
            
            try{
            	// get the signature;
                String singature = xml.substring(xml.indexOf("<")+1, xml.indexOf(">")); 
                // get the name and args   args[0] = name
                String args[] = singature.split("\\s+");                                
                // skip to the rest of the xml
                xml = xml.substring(xml.indexOf(">")+1);                                
                String innerText;
                if(args[args.length-1].equals("/"))
                    innerText="";                                                       
                	// the node has no inner text
                else{
                    String endOfNode = "</"+args[0]+">";
                    // get the inner text
                    innerText=xml.substring(0, xml.indexOf(endOfNode));
                    // skip to the rest of the xml
                    xml =xml.substring(xml.indexOf(endOfNode)+endOfNode.length());     
                }
                
                // create the node
                nodes.add(new XMLNode(args, innerText));                                  
            
            }catch(IndexOutOfBoundsException e){
            	hasError = true;
                xml = xml.substring(xml.indexOf(">")+1);
            }
            
        } 
    }
    
    public void extend(String xml){
       
        while(xml.contains("<")||xml.contains(">")){ 
            
            try{
            	// get the signature;
                String singature = xml.substring(xml.indexOf("<")+1, xml.indexOf(">")); 
                // get the name and args   args[0] = name
                String args[] = singature.split("\\s+");
                // skip to the rest of the xml
                xml = xml.substring(xml.indexOf(">")+1);                                
                String innerText;
                if(args[args.length-1].equals("/"))
                	 // the node has no inner text
                    innerText="";                                                      
                else{
                    String endOfNode = "</"+args[0]+">";
                    // get the inner text
                    innerText=xml.substring(0, xml.indexOf(endOfNode));                 
                    // skip to the rest of the xml
                    xml =xml.substring(xml.indexOf(endOfNode)+endOfNode.length());      
                }
                // create the node
                nodes.add(new XMLNode(args, innerText));                                  
            
            }catch(IndexOutOfBoundsException e){
            	hasError = true;
                xml = xml.substring(xml.indexOf(">")+1);
            }
            
        } 
    }
    
    public ArrayList<XMLNode> getChildrenByName(String name){
        ArrayList<XMLNode> matchingNodes = new ArrayList<XMLNode>();
        for(int i=0;i<nodes.size();i++)
            if(nodes.get(i).getName().equals(name))
                matchingNodes.add(nodes.get(i));
        return matchingNodes;
    }
    
    public void addNode(String args[],String innerText){
        for(int i=0;i<args.length;i++){
            args[i] = args[i].replace('\'', '"');
        }
        nodes.add(new XMLNode(args, innerText));
    }
    
    public void addNode(XMLNode node){
    	nodes.add(node);
    }
    
    public String toString(){
        String text = "";
        for(int i=0;i<nodes.size();i++)
            text+=nodes.get(i)+"\n";
        
        return text;
    }
}
