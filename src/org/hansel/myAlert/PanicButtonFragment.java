package org.hansel.myAlert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.PreferenciasHancel;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PanicButtonFragment extends Fragment 
{
	private TextView txtLastPanic;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//habilitamos wifi para tratar de tener una mejor localización
	
		View v = inflater.inflate(R.layout.fragment_panic, container,false);
		Button btnPanico = (Button)v.findViewById(R.id.btnPanico);
		txtLastPanic =(TextView)v.findViewById(R.id.txtUltimaAlerta);
		
		btnPanico.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().startService(new Intent(getActivity(),SendPanicService.class));
				Toast.makeText(getActivity().getApplicationContext(), "Alerta enviada"
						, Toast.LENGTH_SHORT).show();
			}
		});
		//programar rastreo
		return v;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		txtLastPanic.setText(PreferenciasHancel.getLastPanicAlert(getActivity().getApplicationContext()));
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try
		{
			ActivaRadios();
		}catch(Exception ex)
		{
			Log.v("Error al activar los radios!!!!");
			ex.printStackTrace();
		}
		Bundle datos = getArguments();
		if(datos !=null)
		{
			if(datos.getBoolean("panico"))
			{
				//getActivity().moveTaskToBack(true);
			}
		}
	}
	private void ActivaRadios()
	{
		Log.v("Intentamos activar Datos");
		try {
			setMobileDataEnabled(getActivity().getApplicationContext(), true);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		Log.v("Activamos WIFI");
		//WifiManager wifiManager = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		//wifiManager.setWifiEnabled(true);
		
	}
	@SuppressWarnings("rawtypes")
	private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, InvocationTargetException {
		   final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
		   final Class conmanClass = Class.forName(conman.getClass().getName());
		   final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
		   iConnectivityManagerField.setAccessible(true);
		   final Object iConnectivityManager = iConnectivityManagerField.get(conman);
		   final Class iConnectivityManagerClass =  Class.forName(iConnectivityManager.getClass().getName());
		   @SuppressWarnings("unchecked")
		final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
		   setMobileDataEnabledMethod.setAccessible(true);

		   setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		}
	@Override
	public void onPause() {
		super.onPause();
	}
	
	/*public class ejecutaPanico extends AsyncTask<Void, Void, Void>
	{
		Contacts con = new Contacts(getActivity());

		@Override
		protected void onCancelled() {
		}
		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					//iniciamos rastreo
					//mostramos el fragmento de programación de rastreo como "detener"
					Rastreo rastreo = (Rastreo) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentRastreo);
					if(rastreo!=null)
					{
						rastreo.panicButtonPressed();
					}
					//cancelamos alarma
					if(!Util.isMyServiceRunning(getActivity().getApplicationContext()))
					{
						Util.inicarServicio(getActivity());
						
					}
					//mostramos la fecha de la ultima vez que se corrio el pánico y guardamos la nueva fecha
					String currentDateandTime = Util.getSimpleDateFormatTrack(Calendar.getInstance());
					txtLastPanic.setText(currentDateandTime);
					PreferenciasHancel.setLastPanicAlert(getActivity().getApplicationContext(), currentDateandTime);
					
		}}  */

	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
