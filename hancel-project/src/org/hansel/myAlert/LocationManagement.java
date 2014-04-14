package org.hansel.myAlert;
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

import org.hancel.http.HttpUtils;
import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;

public class LocationManagement  extends 

Service implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	 private boolean mLocationTracking = true;
	// private String fileName=Environment.getExternalStorageDirectory()+"/hansel.txt";
	    private boolean mDisableTracking = false;
	    private LocationManager mLocationManager;
	    private static final int TWO_MINUTES = 1000 * 60 * 5;
	    protected Location mLocation;
	    private final Handler handler = new Handler();
	    private int minutos=5;
	    private TelephonyManager tMgr;
	    private int TrackId;
	  //  File file = new File(this.fileName);
	    private LocationRequest mLocationRequest;
	    private LocationClient mLocationClient;
	 public LocationManagement()
	 {
			// if file doesnt exists, then create it
			/*file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
	 }

    public void setMinutos(int tiempo)
    {
    	this.minutos=tiempo;
    }
    private final Runnable getData = new Runnable() {
        public void run() {
            getDataFrame();
        }
    };
	 
    private void getDataFrame() {
    	Log.v("Inicia Handler de Rastreo");
        final boolean gpsEnabled = mLocationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(mLocationTracking && !mDisableTracking) {
            if (!gpsEnabled && isGPSToggleable()) {
                enableGPS();
            }
            mLocationTracking = true;
        }
       // escribe("Obteniendo datos");
        
        //mLocationManager.removeUpdates(this);
        //enviamos URL al WS URL debemos mejorar el WS
    	
		/*String url = Util.URL_TRACKING+"idAndroid="+TrackId+"&id=0&idCel="+CelId+"&latitude="+Latitud
				+"&longitude="+longitud+"&nivbat="+Util.getBatteryLevel(getApplicationContext())+"&fechas=&horas="+
				"&usn="+mUser;
		Log.v("Enviamos URL de rastreo: "+url); */
        new conexionWS().execute();
        handler.postDelayed(getData,1000*60*minutos); 
        
        /*
        * ID android
        * telefono
        * lat
        * long
        * bat
        * 
        */
    }
    private void getLocation()
    {
    	if (mLocationClient!=null && mLocationClient.isConnected() && isBetterLocation(mLocationClient.getLastLocation(), mLocation))
        {
        	mLocation = mLocationClient.getLastLocation();
        }else
        {
        	mLocation = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        
        if(mLocation!=null)
        {
        //	escribe(mBestProvider+  "     http://maps.google.com/?q="+mLocation.getLatitude()+","
 	     //   +mLocation.getLongitude());
 	        Log.v(""+  "     http://maps.google.com/?q="+mLocation.getLatitude()+","
 	        +mLocation.getLongitude());
        }
    	else
    	{
    		try
    		{
    		Log.v("error tomando el GPS vamos a hacerlo por celdas ");
			String posicionCeldas = tMgr.getCellLocation().toString().replace("[", "").replace("]", "");
			System.out.println(posicionCeldas);
			//09-14 12:14:37.599: I/System.out(1980): ....> [8,2703,8,19.4311,-99.16801]
			try{
			if(!posicionCeldas.equals("")){
			//	Vector<String> celdas = Util.splitVector(posicionCeldas, ",");
				//Latitud = Double.valueOf(celdas.get(3));
				//longitud = Double.valueOf( celdas.get(4));
			//	escribe("Celdas: http://maps.google.com/?q="+Latitud+","+longitud);
				
			}
			}catch (ArrayIndexOutOfBoundsException a) {
				// TODO: handle exception
				System.out.println("No se encontro tampoco por las celdas -> " + a.getMessage());
			}catch (Exception e) {
				Log.v("Error usando Celdas:"+e.getMessage());
			}
    		}catch(Exception ex)
    		{
    			Log.v("Error obteniendo celdas:"+ex.getMessage());
    		}
		}
    }
    public void stopGPS() {
    	Log.v("Detenmos Rastreo");
        //mLocationManager.removeUpdates(this);
   
    }
    public void startGPS(String provider) {
    	//escribe("inicia rastreo StartGps: " + provider);
    	Log.v("Buscando provider"+provider);
       // mLocationManager.requestLocationUpdates(provider, NORMAL_INTERVAL,NORMAL_DISTANCE , this);
    }

    public void disableTracking() {
    	// If the client is connected
        if (mLocationClient.isConnected()) {
            mLocationClient.removeLocationUpdates(this);
        }
        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mLocationClient.disconnect();
    	
    	
        mDisableTracking = true;
        handler.removeCallbacks(getData);
        stopGPS();
    }
    private boolean isGPSToggleable() {
    	
        PackageManager pacman = getApplication().getPackageManager();
        PackageInfo pacInfo = null;
        Log.v("Intenamos ver si se puede activar GPS");
        try {
            pacInfo = pacman.getPackageInfo("com.android.settings",
                    PackageManager.GET_RECEIVERS);
        } catch (NameNotFoundException e) {
            return false;
        }
        if (pacInfo != null) {
            for (ActivityInfo actInfo : pacInfo.receivers) {
                if (actInfo.name
                        .equals("com.android.settings.widget.SettingsAppWidgetProvider")
                        && actInfo.exported) {
                    return true;
                }
            }
        }
        return false;
    }
    private void enableGPS() {
    	Log.v("Intentamos Activar el GPS(no funciona en todas las versiones) ");
        String provider = Settings.Secure.getString(getApplication().getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) {
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            getApplication().sendBroadcast(poke);
        }
    }
    protected boolean isBetterLocation(Location location,
            Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }
        return false;
    }
    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }
    
    public void StartTracking()
    {
    	handler.post(getData);
    }

	@Override
	public void onLocationChanged(Location location) {
		Log.v("On Loc Change");
		
		if(location!=null)
		{
	        String geoCodedLocation;
	        geoCodedLocation = Util.geoCodeMyLocation(location.getLatitude(),
	                location.getLongitude(),getApplicationContext());

	        if (isBetterLocation(location, mLocation) && Geocoder.isPresent()) {
	            try {
	            
	            	//enviamos al servidor el mensaje de reastreo
	            	Log.v("Cambio de Localizacion: "+geoCodedLocation);
	            } catch (IllegalArgumentException e) {
	            }
	            mLocation = location;
	        }
		}
	}
	
	/*private  void  escribe( String contenido) {
		try {
			 Calendar c = Calendar.getInstance(); 
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append(c.getTime()+" "+contenido);
			bw.newLine();
			bw.close();
 
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		 mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		 showNotification();
		/* CelId = Util.getIMEI(getApplicationContext());
		 if(CelId.length()>=5 )
			{
			 CelId = CelId.substring(0, 5);
			}*/
		minutos = intent.getIntExtra("minutos", 5);
		long UpdateInterval =  ( minutos *60)*1000 -(4000) ; //inicia actualizacion 20 segundos antes de obtener nuestros datos
		long fastUpdate  = UpdateInterval - (60*1000); // actualiza el fast a la mitad de tiempo
		
		TrackId = intent.getIntExtra("track", 0);
		//new google PLay service api
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
		mLocationRequest.setInterval(UpdateInterval);
		mLocationRequest.setFastestInterval(fastUpdate);
		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();
		handler.post(getData);		
		//indicador si el servicio esta iniciado,nos servirá si se reinicia el dispositivo
		//o algún otro metodo de inicio
		 return(START_STICKY);
    }
	@Override
	public void onDestroy()
	{
		disableTracking();
		stopForeground(true);
	}
	
	 private void showNotification()
	 {
		 Intent i=new Intent(this, MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    i.addCategory(Intent.CATEGORY_LAUNCHER);
			
			PendingIntent pi=PendingIntent.getActivity(this, 0,
			                         i, 0);
			
		 NotificationCompat.Builder mBuilder =
				    new NotificationCompat.Builder(this)
				    .setSmallIcon(R.drawable.ic_launcher)
				    .setContentTitle(getResources().getString(R.string.app_name))
				    .setContentText("Hansel Running")
				    .setContentIntent(pi)
				    ;
		
			Notification notif = mBuilder.build();
			notif.flags = Notification.FLAG_NO_CLEAR;   
		startForeground(1337, notif);
	 }
	 
	 private class conexionWS extends AsyncTask<Void, Void, Void>
	 {

		@Override
		protected Void doInBackground(Void... params) {
		
			try {
				getLocation();
				HttpUtils.sendTrack(PreferenciasHancel.getDeviceId(getApplicationContext())
						, String.valueOf( TrackId)
						, String.valueOf(PreferenciasHancel.getUserId(getApplicationContext()))
						, String.valueOf(mLocation.getLatitude())
						, String.valueOf(mLocation.getLongitude()) 
						, String.valueOf(Util.getBatteryLevel(getApplicationContext())));
			} catch (Exception e) {
			}
			return null;
		}
		 
	 }

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		if(mLocationClient!=null)
		{
			mLocationClient.connect();
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(mLocationRequest,  this);
	}

	@Override
	public void onDisconnected() {
		if(mLocationClient!=null)
		{
			mLocationClient.connect();
		}
	}
	
	 
	
}

