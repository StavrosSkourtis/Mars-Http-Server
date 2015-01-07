/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mars.http;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Phenom
 */
public class HTTPResponse {

    private String statusCode;
    private final ArrayList<String> headers;
    private byte[] body;

    public HTTPResponse() {
        headers = new ArrayList<String>();
    }

    /**
     * set the response line
     *
     * @param statusCode the response line
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     *
     * @return the response line
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Adds a header to the response
     *
     * @param name Header name
     * @param value Header value
     */
    public void addHeader(String name, String value) {
        headers.add(name + ": " + value);
    }

    /**
     * Adds the body of the response
     *
     * @param body the body as an byte array
     */
    public void addBody(byte[] body) {
        this.body = body.clone();
    }

    /**
     * Sends the Response
     *
     * @param out The Output Stream of the Socket that the response will use the
     * send itself
     * @throws IOException
     */
    public void send(DataOutputStream out) throws IOException {

        out.writeBytes(statusCode + "\r\n");

        for (String header : headers) {
            out.writeBytes(header + "\r\n");
        }
        out.writeBytes("\r\n");

        if (body != null) {
            out.write(body);
        }
    }

    /**
     * Only used for tunneling
     *
     * @param in inputStream
     * @throws IOException 
     */
    public void read(DataInputStream in) throws IOException {
        /*
         *  First we read the Request line 
         *  we read chars until we find \r \n 
         *  when we are done we split the String
         *  the first part is the http method
         *  the second part is the url
         *  the last part it the protocol version
         */
        char tempChar;
        String buffer = "";
        boolean cr = false, lf = false;

        while (!(cr && lf)) {

            tempChar = (char) in.readByte();

            if (tempChar == '\r') {
                cr = true;
                lf = false;
            } else if (tempChar == '\n') {
                lf = true;
            } else {
                buffer += tempChar;
            }
        }

        statusCode = buffer;

        /*
         Below we read the headers
         we read every byte, cast it to char , add it to a string
         if a char is \r\n we add the string to the list and reset the buffer to empty string
         if a char is \r\n and the string is empty we know this is the empty line between the header and body
         we stop reading
         if this header contains the content-length header field we return it
         else we return 0
         */
        buffer = "";
        cr = false;
        lf = false;
        int cont = 0, bodyLength = 0;
        while (cont < 1) {
            buffer = "";
            while (!(cr && lf)) {

                tempChar = (char) in.readByte();

                if (tempChar == '\r') {
                    cr = true;
                    lf = false;
                } else if (tempChar == '\n') {
                    lf = true;
                } else {
                    buffer += tempChar;
                }
            }
            String temp = buffer.toLowerCase();
            if (temp.contains("content-length:")) {
                String t[] = buffer.split(":");
                bodyLength = Integer.parseInt(t[1].substring(t[1].lastIndexOf(" ") + 1));
            }

            if (buffer.equals("")) {
                cont++;
            } else {
                headers.add(buffer);
            }

            cr = false;
            lf = false;
        }

        if (bodyLength > 0) {
            body = new byte[bodyLength];
            
            for (int i=0;i < bodyLength;i++) {
                body[i] = in.readByte();
            }
        }
    }
}
