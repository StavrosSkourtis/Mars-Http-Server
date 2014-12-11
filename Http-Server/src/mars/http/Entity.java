/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.http;

import java.io.File;

/**
 *
 * @author Phenom
 */
public class Entity {
    private String etag;
    private long created;
    private File file;
    
    public Entity(File file){
        this.file = file;
        generateEtag();
    }
    
    public void generateEtag(){
        created = file.lastModified();
        etag = created+file.getName().substring(0, file.getName().indexOf("."));
    }
    
    public String getEtag(){
        return etag;
    }
    
    public File getFile(){
        return file;
    }
    
    public boolean isValid(){
        if(created != file.lastModified())
            return false;
        return true;        
    }
    
    
}
