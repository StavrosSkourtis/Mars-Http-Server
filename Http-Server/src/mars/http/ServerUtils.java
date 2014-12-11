package mars.http;


import java.io.BufferedReader;
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
     * @param file the file we must compare the last modified date with the @date1
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
        for(int i=0;i<bytes.length;i++)
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
     * @param request the HTTP Request
     * @param urlFile the file we must read
     * @return the output as a list of Strings
     */
    public static ArrayList<String> runPHP(HTTPRequest request,File urlFile){
        // We will store here the output
        ArrayList<String> output = new ArrayList<String>();
        try{  
            
            //add all cgi enviromental variables to an array list 
            ArrayList<String> envList = new ArrayList<>();
            //
            //envList.add("HTTP_HOST=");
            //envList.add("HTTPS=");
            //envList.add("PATH=");         
            //envList.add("SERVER_ADMIN=");
            //envList.add("SERVER_SOFTWARE=");
            if(request.method.equalsIgnoreCase("post")){
                envList.add("QUERY_STRING=");
                if(request.getHeader("CONTENT-TYPE")!=null) envList.add("CONTENT_TYPE="+request.getHeaderField("CONTENT-TYPE"));
                envList.add("CONTENT_LENGTH="+request.query.length());
            }else{
                envList.add("QUERY_STRING="+request.query);
            }
            if(request.getHeader("referer")!=null) envList.add("HTTP_REFERER="+request.getHeaderField("referer"));
            if(request.getHeader("user-agent")!=null) envList.add( "HTTP_USER_AGENT="+request.getHeaderField("user-agent"));
            if(request.getHeader("cookie")!=null) envList.add( "HTTP_COOKIE="+request.getHeaderField("cookie"));
            envList.add("REQUEST_URI="+request.url);
            envList.add("GATEWAY_INTERFACE=CGI/1.1");
            envList.add("REMOTE_ADDR="+request.ip);
            envList.add("REMOTE_PORT="+request.port);
            envList.add("REMOTE_HOST="+request.hostname);
            envList.add("REQUEST_METHOD="+request.method);
            envList.add("DOCUMENT_ROOT="+request.absoluteRoot);
            envList.add("SCRIPT_FILENAME="+urlFile.getAbsolutePath());
            envList.add("SCRIPT_NAME="+urlFile.getName());
            envList.add("SERVER_PORT=81");
            envList.add("SERVER_SOFTWARE="+HTTPStatus.SERVER_NAME);
            envList.add("REDIRECT_STATUS=CGI");        
            
            //Convert the list to an array
            String[] env = new String[envList.size()];
            envList.toArray(env);
            
            // start the php process
            Process p = Runtime.getRuntime().exec(new String[]{Config.PHP_PATH},env);
            
            // get php's output stream
            DataInputStream sdtOut = new DataInputStream(p.getInputStream());
            DataOutputStream sdtIn = new DataOutputStream(p.getOutputStream());
            
            if(request.method.equalsIgnoreCase("post")){
                sdtIn.writeBytes(request.query);
                sdtIn.close();
            }
            // we store all the lines
            String line;
            while((line=sdtOut.readLine())!=null){
                output.add(line);
            }
            
            sdtOut.close();
        }catch(Exception e){e.printStackTrace();}
        //return the output
        return output;
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
    
    public static String getGeneralContentType(String type){
        
        if(type.equals("html") || type.equals("htm") || type.equals("js") || type.equals("css") ){
            return "text";
        }else if( type.equals("png") || type.equals("gif") || type.equals("jpg") || type.equals("jpeg") ||
            type.equals("tif") || type.equals("jif") || type.equals("jfif") || type.equals("fpx")  ||
            type.equals("jp2") || type.equals("jpx") || type.equals("j2k") || type.equals("j2c")  ||
            type.equals("pcd")){
            return "image";
        }else if( type.equals("pdf")){
            return "application";
        }
        
        return "text";
    }
}   
