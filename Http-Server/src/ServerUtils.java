
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    
    public static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getDefault().getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }
    
    public static byte[] getBinaryFile(File file){
        byte[] data = new byte[(int)file.length()];
        try{
            DataInputStream reader = new DataInputStream(new FileInputStream(file));
            for(int i=0;i<data.length;i++)
                data[i] = reader.readByte();
        }catch(Exception e){}
        return data;
    }
}
