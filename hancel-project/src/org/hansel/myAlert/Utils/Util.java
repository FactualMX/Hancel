package org.hansel.myAlert.Utils;
/*This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
Created by Javier Mejia @zenyagami
zenyagami@gmail.com
	*/
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import junit.framework.Assert;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;

import org.hansel.myAlert.ContactInfo;
import org.hansel.myAlert.LocationManagement;
import org.hansel.myAlert.MainActivity;
import org.hansel.myAlert.ReminderService;
import org.hansel.myAlert.StopScheduleActivity;
import org.hansel.myAlert.dataBase.TrackDAO;
import org.hansel.myAlert.dataBase.UsuarioDAO;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.Editor;

import android.location.Address;
import android.location.Geocoder;
import android.os.BatteryManager;
import android.os.Build;
import android.telephony.TelephonyManager;

public class Util {
	public static final String PREF_SESSION="Sesion";
	public static final String PREF_GET_LOGIN_OK ="login_ok";
	public static final String PREF_GET_SERVICE_OK ="running_service";
	public static final String PREF_GET_LAST_TRACK_OK ="last_track";
	
	public static final int RQS_PANIC =10;
	public static final String PHONE_NO_SIM ="SinRed";
	//URL de rrastreo, se debe mejorar el método post
	public static final String URL_REGISTRO = "http://www.hanselapp.com/xmlrpc/registro.php?";
	public static final String URL_LOGIN = "http://www.hanselapp.com/xmlrpc/login.php?";
	public static final String URL_PANICO = "http://www.hanselapp.com/xmlrpc/panico.php?";
	public static final String URL_TRACKING = "http://www.hanselapp.com/xmlrpc/rastreo.php?";
	
	public static final int REGISTRO_PASO_1 = 1;
	public static final int REGISTRO_PASO_2 = 2;
	public static final int REGISTRO_PASO_3 = 3;
	public static final int REGISTRO_PASO_4 = 4;
	
	 @SuppressLint("DefaultLocale")
	public static List<String>  orderHashMap(final HashMap<String, ContactInfo> con)
	 {
      		 List<String> keys = new ArrayList<String>();
       keys.addAll(con.keySet());
       
       Collections.sort(keys, new Comparator<Object>() {
			@SuppressLint("DefaultLocale")
			public int compare(Object o1, Object o2) {
           	ContactInfo v1 = (ContactInfo)con.get(o1);
           	ContactInfo v2 = (ContactInfo)con.get(o2);
               if (v1 == null) {
                   return (v2 == null) ? 0 : 1;
               }
               return ((Comparable<String>) stripAccents(v1.getDisplayName()).toUpperCase()).compareTo(stripAccents(v2.getDisplayName()).toUpperCase());
           }
       });
       return keys;
		 
	 }
	@SuppressLint("NewApi")
	public static String stripAccents(String s) {
		 if(isGB())
		 {
			 s = Normalizer.normalize(s, Normalizer.Form.NFD);
			 s = s.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
			 
		 }
		 return s;
	 }
	 public static boolean isGB() {
	        // Can use static final constants like ICS, declared in later versions
	        // of the OS since they are inlined at compile time. This is guaranteed behavior.
	        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	    }
	 public static boolean isICS() {
	        // Can use static final constants like ICS, declared in later versions
	        // of the OS since they are inlined at compile time. This is guaranteed behavior.
	        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	    }
	 public static String geoCodeMyLocation(double latitude, double longitude,Context activity) {
	        Geocoder geocoder = new Geocoder(activity, Locale.ENGLISH);
	        try {
	            List<Address> addresses = geocoder.getFromLocation(latitude,
	                    longitude, 1);

	            if (addresses != null) {
	                Address returnedAddress = addresses.get(0);
	                StringBuilder strReturnedAddress = new StringBuilder(
	                        "Dirección:\n");
	                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
	                    strReturnedAddress
	                            .append(returnedAddress.getAddressLine(i)).append(
	                                    "\n");
	                }
	                return strReturnedAddress.toString();
	            } else {
	                return "No se puede localizar";
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return "Sin localización Determinada";
	        }
	    }
	 public static Vector<String> splitVector(String original, String separator) {
	        Vector<String> nodes = new Vector<String>();
	        // Parse nodes into vector
	        int index = original.indexOf(separator);
	        while(index >=0 ) {
	            //nodes.addElement( original.substring(0, index).toString().concat(separator) );
	            nodes.addElement( original.substring(0, index).toString() );
	            original = original.substring(index+separator.length());
	            index = original.indexOf(separator);
	        }
	        // Get the last node
	        nodes.addElement( original );
	        return nodes;
	    }
	 public static void setLoginOkInPreferences(Context  context,boolean status)
	 {
		
		 SharedPreferences preferencias=    PreferenceManager.wrap(context, Util.PREF_SESSION, Context.MODE_PRIVATE);
		    Editor editor=preferencias.edit();    				   
		    	editor.putBoolean(Util.PREF_GET_LOGIN_OK,status);				   
		        editor.commit();
		    
	 }
	 public static String getRingtone(Context context)
	    {
	    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	        return prefs.getString("pref_ringtone_pref","");
	    }
	 public static int getPanicDelay(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	        String res =  prefs.getString("pref_panic_delay","3");
	        int minutos;
	        try
			{
				minutos = Integer.valueOf(res); 
			}catch(Exception ex)
			{
				minutos=3;
			}
	        return minutos;
	        
	 }
	 public static int getTrackingMinutes(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	        String res =  prefs.getString("pref_key_intervalo","5");
	        int minutos;
	        try
			{
				minutos = Integer.valueOf(res); 
			}catch(Exception ex)
			{
				minutos=5;
			}
	        return minutos;
	 }
	  @SuppressLint("InlinedApi")
	public static PendingIntent getPendingAlarmPanicButton(Context context)
	  {
		  Intent intent = new Intent(context
				  , MainActivity.class);
		  intent.putExtra("panico", true);
		  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		  if(Util.isICS())
		  {
			  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK ); 
		  }
		  PendingIntent pendingIntent = PendingIntent.getActivity(context, RQS_PANIC
				  , intent, PendingIntent.FLAG_CANCEL_CURRENT);
		  return pendingIntent;
	  }
	 public static boolean isMyServiceRunning(Context context) {
		    ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
		        if (LocationManagement.class.getName().equals(service.service.getClassName())) {
		            return true;
		        }
		    }
		    return false;
		}
	 public static String getTelephoneNumber(Context context)
	 {
		 TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String phoneNumber = telephonyManager.getSubscriberId ();
			return  phoneNumber==null? PHONE_NO_SIM:phoneNumber;
	 }
	 public static String getIMEI(Context context)
	 {
		 TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			String IMEI = telephonyManager.getDeviceId();
			return IMEI!=null? IMEI:"IMEI_NULL";
	 }
	 public static boolean getRunningService(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context, Util.PREF_SESSION, Context.MODE_PRIVATE);
	     return prefs.getBoolean(Util.PREF_GET_SERVICE_OK,false);
	 }
	 public static void setRunningService(Context context,boolean value)
	 {
		 
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_SESSION,Context.MODE_PRIVATE);
		 Editor ed = prefs.edit();
		 ed.putBoolean(Util.PREF_GET_SERVICE_OK, value);
		 ed.commit();
	 }
	 public static int getLastTrack(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_SESSION,Context.MODE_PRIVATE);
	     return prefs.getInt(Util.PREF_GET_LAST_TRACK_OK,0);
	 }
	 public static void setLastTrack(Context context,int value)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_SESSION,Context.MODE_PRIVATE);
		 Editor ed = prefs.edit();
		 ed.putInt(Util.PREF_GET_LAST_TRACK_OK, value);
		 ed.commit();
	 }
	 
	 public static int getBatteryLevel(Context context)
	 {
		 IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			Intent batteryStatus = context.registerReceiver(null, ifilter);
			int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			return status;
	 }
	 public static void inicarServicio(Context context)
	 {
		
			context.startService(getIntentForService(context));
	 }
	 private static Intent getIntentForService(Context context)
	 {
		 int trackId=  Util.getLastTrackId(context);
		 int minutos = Util.getTrackingMinutes(context);
		 
		 UsuarioDAO usuarioDao = new UsuarioDAO(context);
		 usuarioDao.open();
		
			//obtenemos ultimo track y lo insertamos
		
		 Intent i = new Intent(context
					,LocationManagement.class);
		 i.putExtra("track", trackId);
			i.putExtra("minutos", minutos);
			i.putExtra("userName", usuarioDao.getUser());
			usuarioDao.close();
			return i;
	 }
	 
	 public static String getSimpleDateFormatTrack(Calendar cal)
	 {
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd-MMM-yyyy hh:mm aa",Locale.getDefault());
			return sdf.format(cal.getTime());
	 }

	   public static int getDrawable(Context context, String name)
	    {
	        Assert.assertNotNull(context);
	        Assert.assertNotNull(name);
	        return context.getResources().getIdentifier(name,
	                null, context.getPackageName());
	    }
	   //obtenemos el inicio del servicio
	   public static PendingIntent getServicePendingIntent(Context context)
	   {
		   return PendingIntent.getService(context, 0, getIntentForService(context), 0);
	   }
	   public static void insertNewTrackId(Context context,int id)
	   {
		   TrackDAO track = new TrackDAO(context);
			  track.open();
			 track.InsertaNewId(null,id);
			  track.close();
	   }
	   public static int getLastTrackId(Context context)
	   {
		   TrackDAO track;
			 track = new TrackDAO(context.getApplicationContext());
			  track.open();
			int trackID=(int) track.Insertar("algo");
			track.close();
			return trackID;
			
	   }
	   public static PendingIntent getReminderPendingIntennt(Context context)
	   {
		   Intent i = new Intent(context,ReminderService.class);
		   PendingIntent pi =  PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		   return pi;
		   
	   }
	   public static PendingIntent getStopSchedulePendingIntentWithExtra(Context context)
	   {
		   Intent i = new Intent(context,StopScheduleActivity.class);
		   PendingIntent pi =  PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
		   return pi;
		   
	   }
	   public static String getStringFromRaw(Context c,int file) {
		   try {
			   Resources r = c.getResources();
		        InputStream is = r.openRawResource(file);
		        String statesText = convertStreamToString(is);
		        is.close();
		        return statesText;
		} catch (Exception e) {
		}
		   return "";
	        	        
	}
	   private static String convertStreamToString(InputStream is) throws IOException {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    int i = is.read();
		    while (i != -1) {
		        baos.write(i);
		        i = is.read();
		    }
		    return baos.toString();
		}
	   
	   
}
