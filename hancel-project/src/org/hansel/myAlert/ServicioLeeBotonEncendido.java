package org.hansel.myAlert;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author mikesaurio
 * 
 */
public class ServicioLeeBotonEncendido extends Service {
	
	/**
	 * Declaraci—n de variables
	 */
	private String TAG = "ServicioGeolocalizacion";
	public static MainActivity login;
	public static double latitud_inicial = 19.0f;
	public static double longitud_inicial = -99.0f;
	ArrayList<String> pointsLat = new ArrayList<String>();
	ArrayList<String> pointsLon = new ArrayList<String>();
	private boolean isFirstTime = true;
	private Timer timer;
	public static boolean serviceIsIniciado = false;
	private BroadcastReceiver mReceiver;
	private static int countStart = -1;
	private Handler handler_time = new Handler();
	public static boolean countTimer = true;
	public static  boolean panicoActivado = false;
	public boolean isSendMesagge= false;
	private ResultReceiver resultReceiver;
  

	@Override
	public void onCreate() {
		Log.i(TAG, "CREADO");
		super.onCreate();
		timer = new Timer();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new MyReceiver();
		registerReceiver(mReceiver, filter);
	}
	
	
	@Override
	public void onStart(Intent intent, int startId) {
		//PAnic
		if(isFirstTime){
		
			isFirstTime=false;
			serviceIsIniciado= true;
		}
			try{
					resultReceiver = intent.getParcelableExtra("receiver");
					// revisamos si la pantalla esta prendida o apagada y contamos el numero de click al boton de apagado
					boolean screenOn = intent.getBooleanExtra("screen_state", false);
					// si damos m‡s de 4 click al boton de apagado se activa la alarma
					if (countStart >= 4) {
						Log.i(TAG, "mas de 4");
						countStart = -1;
						countTimer = true;
						// activamos el mensaje de auxilio
						isSendMesagge=true;
						getApplicationContext().startService(new Intent(getApplicationContext(),SendPanicService.class));
						Toast.makeText(getApplicationContext().getApplicationContext(), "Alerta enviada", Toast.LENGTH_SHORT).show();
						vibrar();
					} else {
						countStart += 1;
						Log.i(TAG,  countStart + "");
						// incrementamos los click en 1
	
						// contamos 5 segundos si no reiniciamos los contadores
						if (countTimer) {
							countTimer = false;
							handler_time.postDelayed(runnable, 5000);// 5 segundos de espera
						}
					}
			}catch(Exception e){
				Log.d(TAG, "vino null");
			}
				
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		serviceIsIniciado= false;
		// panic
		unregisterReceiver(mReceiver);
	}

	@Override
	public IBinder onBind(Intent intencion) {
		return null;
	}

	 /**
     * hilo que al pasar el tiempo reeinicia los valores
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //reiniciamos los contadores
            countStart = -1;
            countTimer = true;
        }
    };

	/**
	 * metodo para hacer que vibre el telefono
	 */
   public void vibrar() {
		Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(3000);        
	}
  

}