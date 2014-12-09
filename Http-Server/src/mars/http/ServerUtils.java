package mars.http;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;
import mars.utils.Config;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Phenom
 */
public class ServerUtils {
    
    /**
     * 
     * @param date1 first date
     * @param date2 second date
     * @return 0 if equal, >0 if date1 is older , <0 if date1 is more recent than date2
     */
    public static int compareDate(String date1 , File file){
        long d1;
        if(!isNumeric(date1))
            // we add 1 sec to d1 because the date1 string doesnt include seconds
            d1 = stringToLong(date1)+1000;
        else
            d1 = Long.parseLong(date1);
        
        long d2 = file.lastModified();
        
        if(d1==d2)
            return 0;
        else if(d1<d2)
            return -1;     
        return 1;
    }
    
    
    /**
     *  Converts a byte array to String
     *  to call this you must be sure the 
     *  array contains chars
     * @param bytes
     * @return 
     */
    public static String charBytesToString(byte[] bytes){
        String buffer = "";
        for(int i=0;i<bytes[i];i++)
            buffer += (char)bytes[i];
        return buffer;
    }
    
    /**
     * 
     * @param s the string to check if it is numeric
     * @return true if numeric else false
     */
    public static boolean isNumeric(String s){
        for(int i=0;i<s.length();i++)
            if(s.charAt(i) < '0' || s.charAt(i) > '9')
                return false;
        return true;
    }
        
    /**
     * 
     * @return the current time in correct format as a String , format : EEE, dd MMM yyyy HH:mm:ss z" 
     */
    public static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault().getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
    
    /**
     * 
     * @return the current time in milliseconds 
     */
    public static long getServerTimeMillis(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }
    
    /**
     * 
     * @param date the date as String
     * @return the given dare in milliseconds
     */
    public static long stringToLong(String date){
        try{
            SimpleDateFormat f = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            Date d  = f.parse(date);
            return d.getTime();
        }catch(ParseException e){}
        return 0;
    }
    
    /** converts the given milliseconds in to a date */
    public static String longToDate(long millis) {
        Date date=new Date(millis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault().getTimeZone("GMT"));
        return dateFormat.format(date);
    }
    
    /**
     * Reads a file
     * 
     * @param file the path of the file
     * @return returns content as a String
     */
    public static String getFile(File file){
        String buffer="";
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while( (line=br.readLine())!=null ){
                buffer += line;
            }   
        }catch(FileNotFoundException fileNotFound){
        }catch(IOException e){}
        return buffer;
    }
    
    /**
     * reads a file
     * @param file the path of the file
     * @return returns the file as an array of bytes
     */
    public static byte[] getBinaryFile(File file){
        byte[] data = new byte[(int)file.length()];
        try{
            DataInputStream reader = new DataInputStream(new FileInputStream(file));
            for(int i=0;i<data.length;i++)
                data[i] = reader.readByte();
            
            reader.close();
        }catch(Exception e){}
        return data;
    }
    
    /**
     * reads a file
     * @param file the path of the file
     * @param start the starting index
     * @param end the last index
     * @return returns the file as an array of bytes
     */
    public static byte[] getBinaryFile(File file,int start,int end){
        byte[] data = new byte[(int)file.length()];
        try{
            DataInputStream reader = new DataInputStream(new FileInputStream(file));
            for(int i=0;i<data.length;i++){
                // We only keep the bytes that were requested
                if(i>=start && i<=end)
                    data[i] = reader.readByte();
                else
                    reader.readByte();
            }
            
            reader.close();
        }catch(Exception e){}
        return data;
    }
    
    /**
     * @param script the script File
     * @return the output as a list of Strings
     */
    public static ArrayList<String> runPHP(String query,String method ,File urlFile){
        // If method equals POST we run the other method, this one prosseces only GET
        if(method.equalsIgnoreCase("post"))
            return runPHPPost(query,urlFile);
        
        // We will store here the output
        ArrayList<String> output = new ArrayList<>();
        try{  
            // this will hold the arguments
            String args[];
            
            //we fill the args array 
            if(!query.equals("")){
                String params[];
                if(query.contains("&"))
                    params = query.split("\\&");
                else{
                    params = new String[1];
                    params[0] = query;
                }
                args = new String[params.length+2];

                args[0] = Config.PHP_PATH;
                args[1] = urlFile.getAbsolutePath();

                for(int i=2;i<args.length;i++){
                    args[i] = params[i-2];
                }
            }else{
                args = new String[2];

                args[0] = Config.PHP_PATH;
                args[1] = urlFile.getAbsolutePath();
            }
            
            // start the php process
            Process p = Runtime.getRuntime().exec(args);
            
            // get php's output stream
            DataInputStream in = new DataInputStream(p.getInputStream());
            
            // we store all the lines
            String line;
            while((line=in.readLine())!=null){
                output.add(line);
            }
            
        }catch(Exception e){e.printStackTrace();}
        //return the output
        return output;
    }
    
    
    private static ArrayList<String> runPHPPost(String query,File urlFile){
        /*
            Why did i even bother with php???
         */
        return null;
    }
    
    
    public static String getContentType(String type){ 
        
        /*
            Image type
        */
        if(type.equals("png")){
            return "image/png";
        }else if(type.equals("jpeg")){
            return "image/jpeg";
        }else if(type.equals("jpg")){
            return "image/jpg";
        }else if(type.equals("gif")){
            return "image/gif";
        }else if(type.equals("tif")){
            return "image/tiff";
        }else if(type.equals("jif")){
            return "image/jif";
        }else if(type.equals("jfif")){
            return "image/jfif";
        }else if(type.equals("fpx")){
            return "image/fpx";
        }else if(type.equals("jp2")){
            return "image/jp2";
        }else if(type.equals("jpx")){
            return "image/jpx";
        }else if(type.equals("j2k")){
            return "image/jk2";
        }else if(type.equals("j2c")){
            return "image/j2c";
        }else if(type.equals("pcd")){
            return "image/pcd";
        }
        
        /*
            Text type
        */
        
        else if(type.equals("html")){
            return "text/html";
        }else if(type.equals("htm")){
            return "text/html";
        }else if(type.equals("js")){
            return "text/javascript";
        }else if(type.equals("css")){
            return "text/css";
        }else if(type.equals("java")){
            return "text/java";
        }else if(type.equals("pdf")){
            return "application/pdf";
        }
        
        /*
            Audio
        */
        else if(type.equals("mp3")){
            return "audio/mp3";
        }
        
        
        return "text/plain";
    }
    
}   
