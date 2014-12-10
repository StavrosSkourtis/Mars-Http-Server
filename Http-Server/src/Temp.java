
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import mars.http.HTTPStatus;
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
public class Temp {
    public static void main(String args[]) throws IOException{
        //add all cgi enviromental variables to an array list 
        ArrayList<String> envList = new ArrayList<>();
        //envList.add("DOCUMENT_ROOT=H:\\Programming Source Code\\Mars-");
        //envList.add("HTTP_COOKIE=");
        //envList.add("HTTP_HOST=");
        //envList.add("HTTPS=");
        //envList.add("PATH=");      
        //envList.add("REMOTE_ADDR=");
        //envList.add("REMOTE_HOST=");
        //envList.add("REMOTE_PORT=");
        //envList.add("REMOTE_USER=");     
        //envList.add("SERVER_ADMIN=");
        //envList.add("SERVER_SOFTWARE=");
        
        envList.add("REQUEST_METHOD=POST");
        envList.add("SCRIPT_FILENAME=H:\\Programming Source Code\\Mars-Http-Server-\\Http-Server\\www\\test.php");
        envList.add("SCRIPT_NAME=test.php");
        envList.add("CONTENT_TYPE=application/x-www-form-urlencoded");
        envList.add("CONTENT_LENGTH=8");
        envList.add("QUERY_STRING=");
        envList.add("REQUEST_URI=/temp.php");
        envList.add("SERVER_NAME=Mars 1.0.0");
        envList.add("GATEWAY_INTERFACE=CGI/1.1");
        envList.add("SERVER_PORT=81");
        envList.add("REDIRECT_STATUS=CGI");         
            
        //Convert the list to an array
        String[] env = new String[envList.size()];
        envList.toArray(env);
            
        // start the php process
        Process p = Runtime.getRuntime().exec(new String[]{"C:\\Program Files (x86)\\PHP\\v5.6\\php-cgi.exe"},env);
        
        // get php's i/o stream
        DataInputStream in = new DataInputStream(p.getInputStream());
        DataOutputStream out = new DataOutputStream(p.getOutputStream());
        
        out.writeBytes("a=2&v=12");
        out.close();
        
        // we print all the lines
        String line;
        while((line=in.readLine())!=null){
             System.out.println(line);
        }
        
        in.close();
        
    }
}
