/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.http;

import java.io.File;
import mars.utils.Config;

/**
 *
 * @author Phenom
 */
public class HTTPHeader {
    
    /**
     *     A http header looks like this
     *     connection: keep-alive
     *     to the left there is the name of the header
     *     and to the right is the value
     *     some headers like if-modified-since make the http method conditional
     *     we store the result of that condition in the condition variable
     *     if the header is not conditional
     *     the condition variable should be ignored
     */
    private String name;
    private String value;
    private boolean condition = false;
    
    
    public HTTPHeader(String line,File urlFile){
        /*
            We just split the line on the ':'
            and put the parts in the name and value fields
        */       
        name = line.substring(0, line.indexOf(":")).trim();
        value =  line.substring(line.indexOf(":")+1).trim();
        
        // Check if value contains an etag
        boolean etag  = value.charAt(0)=='"' && value.charAt(value.length()-1)=='"' ;
        String etags[] = null;
        if(etag & value.contains(",")){
            etags=value.split(" ");
            for(int i=0;i<etags.length-1;i++){
                etags[i] = etags[i].substring(1,etags[i].length()-2);
            }
            etags[etags.length-1] = etags[etags.length-1].substring(1,etags[etags.length-1].length()-1);
        }else if(etag){
            etags = new String[1];
            etags[0] = value.substring(1, value.length()-1);
        }
        
        /*
            If the header is conditional we check the condition and update the condition variable
        */
        if(etag){
            if(name.equalsIgnoreCase("if-match")){
                for (String etag1 : etags) {
                    Entity temp = Config.getEntity(etag1);
                    if(temp!=null && !temp.isValid()){
                        condition = true;
                        break;
                    }
                }
            }else if(name.equalsIgnoreCase("if-modified-since")){
                for (String etag1 : etags) {
                    Entity temp = Config.getEntity(etag1);
                    if(temp!=null && !temp.isValid()){
                        condition = true;
                        break;
                    }
                }
            }else if(name.equalsIgnoreCase("if-none-match")){
                for (String etag1 : etags) {
                    Entity temp = Config.getEntity(etag1);
                    if(temp!=null && temp.isValid()){
                        condition = true;    
                        break;
                    }
                }
            }else if(name.equalsIgnoreCase("if-range")){
                for (String etag1 : etags) {
                    Entity temp = Config.getEntity(etag1);
                    if(temp!=null && !temp.isValid()){
                        condition = true;
                        break;
                    }
                }
            }else if(name.equalsIgnoreCase("if-unmodified-since")){
                for (String etag1 : etags) {
                    Entity temp = Config.getEntity(etag1);
                    if(temp!=null && temp.isValid()){
                        condition = true;
                        break;
                    }
                }
            }
        }else{
            if(name.equalsIgnoreCase("if-modified-since")){
                if(ServerUtils.compareDate( value , urlFile) == 1 && urlFile.exists())
                    condition = true;
            }else if(name.equalsIgnoreCase("if-range")){
                if(ServerUtils.compareDate( value , urlFile) == 1 && urlFile.exists())
                    condition = true;
            }else if(name.equalsIgnoreCase("if-unmodified-since")){
                if(ServerUtils.compareDate( value , urlFile) == -1 && urlFile.exists())
                    condition = true;
            }
        }
    }
    
    /*
        set() and get() methods
    */
    public boolean getCondition(){
        return condition;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }   
    
    public void setValue(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
