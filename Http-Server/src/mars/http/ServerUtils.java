package mars.http;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
            d1 = stringToLong(date1);
        else
            d1 = Long.parseLong(date1);
        
        long d2 = file.lastModified();
        
        if(d1==d2)
            return 0;
        else if(d1<d2)
            return -1;     
        return 1;
    }
    
    
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
}
