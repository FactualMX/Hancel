package org.hansel.myAlert.Http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {
	public static final String URL_WS ="http://192.168.119.93:8080/";
	public static String SendPosts(String httpURL, String data) throws IOException   {
        URL url = new URL(httpURL);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        
        // If cookie exists, then send cookie
       /* if (_cookie != "") {
            connection.setRequestProperty("Cookie", _cookie);
            connection.connect();
        }*/
        
        // If Post Data not empty, then send POST Data
        if (data != "") {
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(data);
            out.flush();
            out.close();
        }
        
       /// Save Cookie
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        //_cookies.clear();
     /*   if (_cookie == "") {
            for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++) {
                if (headerName.equalsIgnoreCase("Set-Cookie")) {    
                    String cookie = connection.getHeaderField(i);
                    _cookie += cookie.substring(0,cookie.indexOf(";")) + "; ";
                }
            }
        }*/
        
        // Get HTML from Server
        String getData = "";
        String decodedString;
        while ((decodedString = in.readLine()) != null) {
            getData += decodedString + "\n";
        }
        in.close();
        
        return getData;
    }

}
