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
import java.util.ArrayList;
import java.util.Calendar;

import org.hancel.http.HttpUtils;
import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;
import org.hansel.myAlert.dataBase.ContactoDAO;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class SendPanicService extends Service implements GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener {
	private LocationClient mLocationClient;
	private ContactoDAO contactoDao;
	private ejecutaPanico mTask;
	
	///
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(getApplicationContext()
				.getApplicationContext(), this, this);
		mLocationClient.connect();

	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public Location getLocation() {

		Location loc = null;
		if (mLocationClient != null && mLocationClient.isConnected()) {
			loc = mLocationClient.getLastLocation();
		}
		if (loc == null) {
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		}
		return loc;
	}

	public class ejecutaPanico extends AsyncTask<Void, Void, Void> {
		Contacts con = new Contacts(getApplicationContext());

		@Override
		protected void onCancelled() {
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.v("Iniciando Panico");
			Location loc = getLocation();

			double Lat = 0;
			double Long = 0;
			String mapa = "";
			// String direccion="";
			if (loc != null) {
				Lat = loc.getLatitude();
				Long = loc.getLongitude();
				mapa = " Loc. Aprox: " + "http://maps.google.com/?q=" + Lat+ "," + Long + "\n";

			}
			// String direccion = Util.geoCodeMyLocation(Lat, Long,
			// getActivity());
			ArrayList<ContactInfo> users = getSqliteContacts();
			String emailList = "";
			SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String mensaje = preferencias.getString("pref_key_custom_msg","Ayuda");

			for (int i = 0; i < users.size(); i++) { // por cada contacto
				ContactInfo ci = users.get(i);
				ArrayList<String> numeros = ci.getPhoneNumbers();

				for (int j = 0; j < numeros.size(); j++) { // por cada "número seleccionado"
					try {
						Log.v("Mensaje a: " + numeros.get(j));
						Log.v(mensaje + mapa + " bateria: "+getNivelBateria()+"%");

						enviarSMS(numeros.get(j), mensaje + mapa+ " bateria: "+getNivelBateria()+"%");
						// enviarSMS(numeros.get(j), direccion);
					} catch (Exception ex) {
						Log.v("Ocurrio un Error al enviar SMS :" + numeros	+ " Excepcion:" + ex.getMessage());
					}
				}
				try {
					emailList += getContactById(ci.getPhotoId());
				} catch (Exception e) {
					e.printStackTrace();
					Log.v("Error al obtener la lista de email: "+ e.getMessage());
				}
			}
			
			try {
				Log.v("Inicia envio de panico: ");
				String ongList = PreferenciasHancel.getSelectedOng(getApplicationContext());
				if (ongList != null) {
					try {
						ongList = ongList.endsWith(",") ? ongList.substring(0,ongList.length() - 1) : ongList;
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
				try {
					emailList = emailList.endsWith(",") ? emailList.substring(0, emailList.length() - 1) : emailList;
				} catch (Exception e) {
					// TODO: handle exception
				}
				HttpUtils.sendPanic(PreferenciasHancel.getDeviceId(getApplicationContext()),
								String.valueOf(PreferenciasHancel.getUserId(getApplicationContext())),
								String.valueOf(loc.getLatitude()),
								String.valueOf(loc.getLongitude()),
								String.valueOf(getNivelBateria()),
								emailList, ongList);
			} catch (Exception ex) {
				Log.v("Error al mandar el track: " + ex.getMessage());
			}
			return null;
		}

		private String getContactById(String photoId) {
			StringBuilder lista = new StringBuilder();
			Cursor cur1 = getApplicationContext().getContentResolver().query(
					ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
					new String[] { photoId }, null);
			while (cur1.moveToNext()) {
				String email = cur1
						.getString(cur1
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
				if (email != null && email.length() != 0) {
					lista.append(email);
					lista.append(",");
				}
			}
			cur1.close();
			return lista.toString();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// iniciamos rastreo
			// mostramos el fragmento de programación de rastreo como "detener"
			/*
			 * Rastreo rastreo = (Rastreo)
			 * getApplicationContext().getSupportFragmentManager
			 * ().findFragmentById(R.id.fragmentRastreo); if(rastreo!=null) {
			 * rastreo.panicButtonPressed(); }
			 */
			// cancelamos alarma
			cancelAlarms();
			if (!Util.isMyServiceRunning(getApplicationContext())) {
				Util.inicarServicio(getApplicationContext());
			}
			// mostramos la fecha de la ultima vez que se corrio el pánico y
			// guardamos la nueva fecha
			String currentDateandTime = Util.getSimpleDateFormatTrack(Calendar
					.getInstance());
			PreferenciasHancel.setLastPanicAlert(getApplicationContext(),
					currentDateandTime);
			stopSelf();
		}
	}

	private void cancelAlarms() {
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(Util
				.getReminderPendingIntennt(getApplicationContext()));
		alarmManager
				.cancel(Util
						.getStopSchedulePendingIntentWithExtra(getApplicationContext()));

	}

	/**
	 * envia un SMS si es muy largo envia varios
	 * 
	 * @param telefono
	 *            (telefono al que se le enviara el SMS)
	 * @param mensaje
	 *            (mensaje de panico)
	 */
	public void enviarSMS(String telefono, String mensaje) {
		SmsManager sms = SmsManager.getDefault();
		try {
			sms.sendTextMessage(telefono, null, mensaje, null, null);
			Log.v("Mensaje enviado a " + telefono);
		} catch (Exception e) {
			try {
				ArrayList<String> parts = sms.divideMessage(mensaje);
				sms.sendMultipartTextMessage(telefono, null, parts, null, null);
				Log.v("Mensaje enviado en partes " + telefono);
			} catch (Exception i) {
				Log.v("Error al mandar , falla al envio de SMS");
				e.printStackTrace();
			}
		}

	}

	private ArrayList<ContactInfo> getSqliteContacts() {
		ArrayList<ContactInfo> userInfo = new ArrayList<ContactInfo>();
		contactoDao = new ContactoDAO(getApplicationContext());
		contactoDao.open();
		Cursor c = contactoDao.getList();
		if (c != null) {
			while (c.moveToNext()) {
				int id = c.getInt(1);
				String phones = c.getString(2);
				String photoId = c.getString(3);
				ContactInfo ci = new ContactInfo("", String.valueOf(id));
				ci.setContactNumbers(phones);
				ci.setPhotoId(photoId);
				userInfo.add(ci);
			}
		}
		try {
			c.close();
		} catch (Exception ex) {
		}
		contactoDao.close();
		return userInfo;
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle arg0) {
		if (mTask == null) {
			mTask = new ejecutaPanico();
			mTask.execute();
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/**
	 * revisa el nivel de bateria del celular
	 * 
	 * @return int ( nivel de bateria)
	 */
	private int getNivelBateria() {
		// Log.v("*******entre bateria");
		Intent i = new ContextWrapper(this).registerReceiver(null,
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		// Log.v("*******nivel"+i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1));
		return   i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	}
	
	
	
    
}
