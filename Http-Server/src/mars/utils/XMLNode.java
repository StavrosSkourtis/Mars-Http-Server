package mars.utils;

import java.util.ArrayList;

/**
 *
 * @author Stavros Skourtis
 */
public class XMLNode {
    private ArrayList<XMLNode> children;
    private ArrayList<Attribute> attributes;
    private String name;
    private String innerText;
    
    public XMLNode(String tagName){
    	 children = new ArrayList<XMLNode>();
         attributes = new ArrayList<Attribute>();
         innerText="";
         this.name = tagName;
    }
    
    public XMLNode(String[] args,String innerText){
        if(args.length==0)
            throw new IllegalArgumentException("node zero arguments");
        
        children = new ArrayList<XMLNode>();
        attributes = new ArrayList<Attribute>();
        
        name = args[0];
        StringBuffer pureInnerText = new StringBuffer(innerText);
        
        while(pureInnerText.toString().contains("<") || pureInnerText.toString().contains(">")){
            pureInnerText.delete(pureInnerText.indexOf("<"), pureInnerText.indexOf(">")+1);
        }
        
        this.innerText = pureInnerText.toString();
       
        
        for(int i=1;i<args.length;i++)
            if(!args[i].equals("/"))
                addAttribute(args[i]);
        
        update(innerText);
    }
    
    private void update(String xml){
        
        while( xml.contains("<") || xml.contains(">")  ){                                         
            String singature = xml.substring(xml.indexOf("<")+1, xml.indexOf(">")); // get the signature;
            String args[] = singature.split("\\s+");                                // get the name and args   args[0] = name
            xml = xml.substring(xml.indexOf(">")+1);                                // skip to the rest of the xml
            String innerText;
            if(args[args.length-1].equals("/"))
                innerText="";                                                       // the node has no inner text because it ends quickly
            else{
                String endOfNode = "</"+args[0]+">";
                innerText=xml.substring(0, xml.indexOf(endOfNode));                 // get the inner text
                xml =xml.substring(xml.indexOf(endOfNode)+endOfNode.length());      // skip to the rest of the xml
            }
            
            children.add(new XMLNode(args, innerText));                                   // create the node
        }
        
    }
    
    
    public  void addAttribute(String name,String value){
        attributes.add(new Attribute(name, value));
    }
    private void addAttribute(String unsplitedtext){
        attributes.add(new Attribute(unsplitedtext));
    }
    
    public void setAttribute(String name , String value){
        for(int i=0;i<attributes.size();i++)
            if(attributes.get(i).getName().equals(name))
                attributes.get(i).setValue(value);
    }
    
    public void addChildNode(String args[],String innerText){
    	for(int i=0;i<args.length;i++){
            args[i] = args[i].replace('\'', '"');
        }
        children.add(new XMLNode(args, innerText));
    }
    
    public void addChildNode(XMLNode child){
    	children.add(child);
    }
    
    public String getName(){
        return name;
    }
    
    public String getInnerText(){
        return innerText;
    }
    
    public void setInnerText(String innerText){
    	this.innerText = innerText;
    }
    
    public String getAttribute(String name){
        for(int i=0;i<attributes.size();i++)
            if(attributes.get(i).getName().equals(name))
                return attributes.get(i).getValue();
        
        return null;
    }
    
    public ArrayList<XMLNode> getChildrenByName(String name){
        ArrayList<XMLNode> matchingNodes = new ArrayList<XMLNode>();
        for(int i=0;i<children.size();i++)
            if(children.get(i).getName().equals(name))
                matchingNodes.add(children.get(i));
        return matchingNodes;
    }
    
    
    public void remove(int index){
        children.remove(index);
    }
    
    
    public String toString(){
        String text= "<"+name+" ";
        
        for(int i=0;i<attributes.size();i++){
        	text+= attributes.get(i).name+"=\""+attributes.get(i).value+"\" ";
        }
        
        
        text+=">";
        text+=innerText;
        	
        for(int i=0;i<children.size();i++){
        	if(i==0)
        		text+="\r\n";
        	text+=children.get(i).toString()+"\r\n";
        }
        	
        text+="</"+name+">";
        
           
        return text;
    }
    
    public class Attribute {
        private String name;
        private String value;
        
        public Attribute(String name,String value){
            this.name = name;
            this.value = value;
        }
        
        public Attribute(String unsplitedtext){   
            name = unsplitedtext.substring(0,unsplitedtext.indexOf("="));
            value = unsplitedtext.substring(unsplitedtext.indexOf("\"")+1, unsplitedtext.length()-1); 
        }
        
        public String getName(){
            return name;
        }
        
        public String getValue(){
            return value;
        }
        
        public void setValue(String value){
            this.value = value;
        }
    }

}
