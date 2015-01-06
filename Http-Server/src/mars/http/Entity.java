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
    /**
     * Entity Tag
     */
    private String etag;
    /**
     * When this was created
     */
    private long created;
    
    /**
     * The file that is represented by this entity
     */
    private File file;
    
    public Entity(File file){
        this.file = file;
        generateEtag();
    }
    
    
    /**
     * Generates a new eTag
     */
    public void generateEtag(){
        created = file.lastModified();
        etag = created+file.getName().substring(0, file.getName().indexOf("."));
    }
    
    /**
     * 
     * @return the entity's eTag
     */
    public String getEtag(){
        return etag;
    }
    
    /**
     * 
     * @return the file
     */
    public File getFile(){
        return file;
    }
    
    /**
     * Check if this entity is valid
     * An entity is valid if the file it represent has not been modified
     * @return true is the it is valid else false
     */
    public boolean isValid(){
        if(created != file.lastModified())
            return false;
        return true;        
    }
    
    
}
