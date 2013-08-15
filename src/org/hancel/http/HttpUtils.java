package org.hancel.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.hancel.exceptions.NoInternetException;
import org.hansel.myAlert.Log.Log;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;

public class HttpUtils {
	private static String URL_BASE = "http://www.hanselapp.com/wp-3/sisweb.php?";
	 
	public static JSONObject requestHttp(String url, List<NameValuePair> params, String method)
	 {
	        return request(url,params,method);
	 }
    public static JSONObject request(String url, List<NameValuePair> params, String method){

      JSONObject response;
          try {
              response = execute(url, params, method);
              if(response!=null)
                  return response;
          } catch (IOException e) {
              Log.v("Error al obtener los datos de la URL");
              e.printStackTrace();
          } catch (JSONException e) {
              e.printStackTrace();
          }catch (Exception e) {
			e.printStackTrace();
		}

      return null;
  }
    private static JSONObject execute(String base, List<NameValuePair> params, String method) throws IOException, JSONException {

        URL url;
        HttpURLConnection urlConnection = null;

        if(method.equals("GET")){
            url = new URL(base+getQuery(params));
            urlConnection = (HttpURLConnection) url.openConnection();
        }
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Scanner s = new Scanner(in).useDelimiter("\\A");
            String parseString =  s.hasNext()?s.next():"";
            in.close();
            return new JSONObject(parseString);
        }
        finally {
            urlConnection.disconnect();

        }
    }
    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            if(pair.getValue()!=null)
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            else
                result.append("");
        }

        return result.toString();
    }
	
	public static JSONObject Login(String user, String password,String id_device) throws NoInternetException
	{
		 ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
		 values.add(new BasicNameValuePair("f","login"));
		 values.add(new BasicNameValuePair("strUsr",user));
		 values.add(new BasicNameValuePair("strPass",password));
		 values.add(new BasicNameValuePair("id_device",id_device));
		 
		 JSONObject result =  requestHttp(URL_BASE, values, "GET");
		 if(result==null)
		 {
			 throw new NoInternetException("Error en petición al server");
		 }
		 return result;
		
		 
	}
	public static JSONObject sendTrack(String idDevice, 
			String androidId,
			String idUsuario
			,String latitude
			,String longitude
			,String bateria) throws NoInternetException
	{

		 ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
		 values.add(new BasicNameValuePair("f","tracking"));
		 values.add(new BasicNameValuePair("androidId",androidId));
		 values.add(new BasicNameValuePair("idDevice",idDevice));
		
		 values.add(new BasicNameValuePair("idUsuario",idUsuario));
		 values.add(new BasicNameValuePair("latitude",latitude));
		 values.add(new BasicNameValuePair("longitude",longitude));
		 values.add(new BasicNameValuePair("bateria",bateria));
		 JSONObject result = requestHttp(URL_BASE, values, "GET");
		 if(result==null)
		 {
			 throw new NoInternetException("Error en petición al server");
		 }
		 return result;
		
		 
	}
	public static JSONObject sendPanic(String idDevice
			,String idUsuario
			,String latitude
			,String longitude
			,String bateria
			,String emailsIds
			,String ongList) throws NoInternetException
	{
		 ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
		 values.add(new BasicNameValuePair("f","panico"));
		 values.add(new BasicNameValuePair("idDevice",idDevice));
		 values.add(new BasicNameValuePair("idUsuario",idUsuario));
		 values.add(new BasicNameValuePair("latitude",latitude));
		 values.add(new BasicNameValuePair("longitude",longitude));
		 values.add(new BasicNameValuePair("bateria",bateria));
		 values.add(new BasicNameValuePair("emailsIds",emailsIds));
		 values.add(new BasicNameValuePair("ongsIds",ongList ));
		 
		
		 
		 JSONObject result =  requestHttp(URL_BASE, values, "GET");
		 if(result==null)
		 {
			 throw new NoInternetException("Error en petición al server");
		 }
		 return result;
		 
	}	
	public static JSONObject Register(String id_device, String usuario,
			String password,String email,String emailAmigo,
			String IMEI) throws NoInternetException
	{
		 ArrayList<NameValuePair> values = new ArrayList<NameValuePair>();
		 values.add(new BasicNameValuePair("f","registro"));
		 values.add(new BasicNameValuePair("idDevice",id_device));
		 values.add(new BasicNameValuePair("usuario",usuario));
		 values.add(new BasicNameValuePair("password",password));
		 values.add(new BasicNameValuePair("email",email));
		 values.add(new BasicNameValuePair("mailsAmigos",emailAmigo));
		 values.add(new BasicNameValuePair("imei",IMEI));
		 values.add(new BasicNameValuePair("verDroid", String.valueOf(Build.VERSION.SDK_INT)));
		 
		 JSONObject result =  requestHttp(URL_BASE, values, "GET");
		 if(result==null)
		 {
			 throw new NoInternetException("Error en petición al server");
		 }
		 return result;
		 
	}
}
