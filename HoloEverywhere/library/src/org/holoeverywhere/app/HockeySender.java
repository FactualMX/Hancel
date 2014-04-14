package org.holoeverywhere.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.StrictMode;
import android.util.Log;
 
public  class HockeySender implements ReportSender {
 // public  String BASE_URL = "https://rink.hockeyapp.net/api/2/apps/";
  private static String CRASHES_PATH = "/crashes";
  public static final int HTTP_TIMEOUT = 30 * 1000;
  String url;
 
  public HockeySender(String envio) {
	   url = envio;
}

@Override
  public void send(CrashReportData report) throws ReportSenderException {
    try {
    	System.setProperty("http.keepAlive", "false");
		HttpClient httpclient = new DefaultHttpClient();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		final HttpParams par = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(par, HTTP_TIMEOUT);
		HttpConnectionParams.setSoTimeout(par, HTTP_TIMEOUT);
		ConnManagerParams.setTimeout(par, HTTP_TIMEOUT);
		HttpPost httppost;
		 BeanDatosLog bean=new BeanDatosLog();
		 
		 try{
		     Process process = Runtime.getRuntime().exec("logcat -d");
		     BufferedReader bufferedReader = new BufferedReader(
		     new InputStreamReader(process.getInputStream()));
		     StringBuilder log=new StringBuilder();
		     String line;
		     while ((line = bufferedReader.readLine()) != null) {
		       log.append(line);
		     }
		   //  Log.d("******sdfwds", log+"");
			
		 httppost = new HttpPost(url);
		 
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("modelo", new StringBody(bean.getMarcaCel()+""));
		entity.addPart("version", new StringBody(bean.getVersionAndroid() + ""));
		entity.addPart("tag",new StringBody(bean.getTagLog() + ""));
		entity.addPart("descripcion", new StringBody(log.toString() + ""));
	     Log.d("******sdfwds", "enviado");
		System.setProperty("http.keepAlive", "false");
		httppost.setEntity(entity);
		HttpResponse response = httpclient.execute(httppost);
   
    
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
    }catch (Exception e) {
		      e.printStackTrace();
		    } 
  }
/**
 * metodo que hace la conexion al servidor con una url especifica
 * @param url(String) ruta del web service
 * @return (String) resultado del service
 */
public static String doHttpConnection(String url) {
	HttpClient Client = new DefaultHttpClient();
	try {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		HttpGet httpget = new HttpGet(url);
		HttpResponse hhrpResponse = Client.execute(httpget);
		HttpEntity httpentiti = hhrpResponse.getEntity();
		//Log.d("RETURN HTTPCLIENT", EntityUtils.toString(httpentiti));
		return EntityUtils.toString(httpentiti);
	} catch (ParseException e) {
		Log.d("Error ParseEception", e.getMessage() + "");
		return null;
	} catch (IOException e) {
		Log.d("Error IOException", e.getMessage() + "");
		return null;
	}
}
 
}