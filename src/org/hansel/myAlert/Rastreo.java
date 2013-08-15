package org.hansel.myAlert;




import java.util.Calendar;
import java.util.Date;

import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;
import org.hansel.myAlert.dataBase.TrackDAO;
import org.hansel.myAlert.dataBase.UsuarioDAO;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.hancel.adapters.customUi.TrackDialog;
import org.hancel.customclass.TrackDate;

public class Rastreo extends Fragment implements OnClickListener{


	private boolean corriendo=false;
	private int minutos;
	private static final String START_TRACK = "Programar Rastreo";
	private static final String STOP_TRACK = "Detener";
	private static final int RQS_1 = 12;
	private Button btnTracking;
	private UsuarioDAO usuarioDao;
//	private TimePickerDialog timePickerDialog;
	private  AlarmManager alarmManager;
	private TrackDAO track;
	private final int REQUEST_CODE = 0;
	private View currentTrackInfo;
	private TextView txtCurrentTrackInfo;
	private TrackDate trackDate;
	//private boolean okButtonPressed;
	
	private Button btnCancelCurrentTrack;
	private Button btnModifyCurrentTrack;
	
	public boolean isTracking()
	{
		return corriendo;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  alarmManager = (AlarmManager)getActivity().getSystemService(Activity.ALARM_SERVICE);
		  track = new TrackDAO(getActivity().getApplicationContext());
		  track.open();
	}
	
	private void showCurrentTrackInfo(boolean showTrackInfo)
	{
		currentTrackInfo.setVisibility(showTrackInfo ? View.VISIBLE : View.GONE );
		btnTracking.setVisibility(showTrackInfo? View.GONE : View.VISIBLE);
	}
	public void panicButtonPressed()
	{
		//se presiona botón de panico, cancelamos "servicio" si aun no esta corriendo y cancelamos la alarma
		showCurrentTrackInfo(false);
		//cancelamos la alarma antes de ejecutar el servicio por el botón de pánico
		//si esta corriendo es que corrió por causa de la alarma
		if(!Util.isMyServiceRunning(getActivity()))
		{
			alarmManager.cancel(Util.getServicePendingIntent(getActivity()));
		}
		//corriendo siempre será "true" por que al presionar el botón de pánico
		// se inizializa el servicio
		corriendo=true;
		setupButtonText();
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==REQUEST_CODE && resultCode== Activity.RESULT_OK && data!=null)
		{
			
			/*
			//cancelamos la alarma anterios en caso que se modificque
			alarmManager.cancel(Util.getServicePendingIntent(getActivity()));
			//obtenemos data para poder iniciar el rastreo:
			trackDate = (TrackDate) data.getExtras().get(TrackDialog.TRACK_EXTRA);
			//mostramos Botones De info
			setAlarm(trackDate.getEndTimeTrack());
			showCurrentTrackInfo(true);
			txtCurrentTrackInfo.setText(Util.getSimpleDateFormatTrack(trackDate.getStartDateTrack()) );
			//alarma para iniciar el servicio de "recordatorio"
			 alarmManager.set(AlarmManager.RTC_WAKEUP, trackDate.getStartTimeTrack().getTimeInMillis(), Util.getServicePendingIntent(getActivity()));
			 */
			cancelAlarms();
			PreferenciasHancel.setReminderCount(getActivity(), 0);
			trackDate = (TrackDate) data.getExtras().get(TrackDialog.TRACK_EXTRA);
			 alarmManager.set(AlarmManager.RTC_WAKEUP, trackDate.getStartTimeTrack().getTimeInMillis(), Util.getReminderPendingIntennt(getActivity()));
			 Log.v("Finalizando en: "+ new Date(trackDate.getEndTimeTrack().getTimeInMillis()) );
			 alarmManager.set(AlarmManager.RTC_WAKEUP, trackDate.getEndTimeTrack().getTimeInMillis(), Util.getStopSchedulePendingIntentWithExtra(getActivity()));
			//guardamos inicio de alarma
			 PreferenciasHancel.setAlarmStartDate(getActivity(), trackDate.getEndTimeTrack().getTimeInMillis());
			 showCurrentTrackInfo(true);
			 txtCurrentTrackInfo.setText(Util.getSimpleDateFormatTrack(trackDate.getStartTimeTrack()) );
			 
		}else
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_rastreo, container,false);
		btnTracking = (Button)v.findViewById(R.id.IniciaTrackId);
		usuarioDao=new UsuarioDAO(getActivity().getApplicationContext());
		usuarioDao.open();
		//convertimos a entero el valor
		minutos = Util.getTrackingMinutes(getActivity().getApplicationContext());
		//minutos =4;
		//Layouts para ocultar o mostrar dependiendo de la alerta
		currentTrackInfo = v.findViewById(R.id.layoutCurrentTrack);
		txtCurrentTrackInfo = (TextView)v.findViewById(R.id.txtUltimaAlerta);
		
		btnCancelCurrentTrack = (Button)v.findViewById(R.id.btnCancelCurrentTrack);
		btnModifyCurrentTrack = (Button)v.findViewById(R.id.btnModifyCurrentTrack);
		btnCancelCurrentTrack.setOnClickListener(this);
		btnModifyCurrentTrack.setOnClickListener(this);
		showCurrentTrackInfo(false);
		
	 Log.v("Buscando el tiempo por defecto para actualizar: "+minutos);
	if(savedInstanceState!=null)
	{
		corriendo = savedInstanceState.getBoolean("run");
		if(corriendo)
		{
			btnTracking.setText(STOP_TRACK);
		}else
		{
			btnTracking.setText(START_TRACK);
		}
	}
	
	btnTracking.setOnClickListener(this); 
			 
	return v;
	}
	  
	  private PendingIntent getPendingAlarm()
	  {
		  Intent intent = new Intent(getActivity().getApplicationContext()
				  , AlarmReceiver.class);
		  PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), RQS_1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		  return pendingIntent;
	  }
	  
	@Override
	public void onResume() {
		super.onResume();
		long time = PreferenciasHancel.getAlarmStartDate(getActivity());
		if(time!=0)
		{
			Calendar currentTime = Calendar.getInstance();
			Calendar alarmTime = Calendar.getInstance();
			alarmTime.setTimeInMillis(time);
			//comparamos la fecha para saber si ya esta corriendo la alarma
			if(alarmTime.compareTo(currentTime)!=-1) //-1 si es antes aun podemos modificar la alarma
			{
				showCurrentTrackInfo(true);
			}
			txtCurrentTrackInfo.setText(Util.getSimpleDateFormatTrack(alarmTime) );
		}
		
		corriendo =Util.isMyServiceRunning(getActivity().getApplicationContext()); 
		setupButtonText();
		
	}
	
	private void setupButtonText() {
		if(corriendo)
		{
			showCurrentTrackInfo(false);
			btnTracking.setText(STOP_TRACK);
			
		}else
		{
			btnTracking.setText(START_TRACK);
		}
	}
	protected void createPasswordDialog(final Button btnPanico) {
	    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());                 
	    alert.setTitle("Contraseña para cancelar...");  
	    alert.setMessage("Contraseña:");                

	     // Set an EditText view to get user input   
	     final EditText input = new EditText(getActivity());
	     input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
	     alert.setView(input);

	        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
	        public void onClick(DialogInterface dialog, int whichButton) {  
	            String value = input.getText().toString();
	            //encriptamos el password y lo comparamos con el guardado
	          /*  String hash = SimpleCrypto.md5(SimpleCrypto.MD5_KEY);
				if(hash.length()>0)
				{
					String encrypted= SimpleCrypto.encrypt(value, hash);
					if(encrypted.length()>0)
					{
						value = encrypted;
					}
				}*/
	            boolean isOK = usuarioDao.getPassword(value.trim());
	            if(isOK && btnPanico.getId()== R.id.IniciaTrackId ) //buscar en la BD la contraseña
	            {
		        	Log.v("Detener Rastreo");
		        	alarmManager.cancel(getPendingAlarm());
		        	btnPanico.setText(START_TRACK);
		        	Util.setRunningService(getActivity().getApplicationContext(), false);
					getActivity().stopService(new Intent(getActivity().getApplicationContext()
							,LocationManagement.class));
					alarmManager.cancel(Util.getPendingAlarmPanicButton(getActivity().getApplicationContext()));
					corriendo=false;
		            Toast.makeText(getActivity(), "Rastreo Detenido", Toast.LENGTH_SHORT).show();
		            PreferenciasHancel.setAlarmStartDate(getActivity(), 0);
	            return;                  
	           }else if(isOK && btnPanico.getId() == R.id.btnCancelCurrentTrack )
	           {
	       			//cancelamos alarma para iniciar servicio
	       			//alarmManager.cancel(Util.getServicePendingIntent (getActivity()));
	       			cancelAlarms();
	       			Toast.makeText(getActivity(), "Programación de Rastreo Cancelado", Toast.LENGTH_SHORT).show();
	       			showCurrentTrackInfo(false);
	       			 PreferenciasHancel.setAlarmStartDate(getActivity(), 0);
	           }else if(isOK && btnPanico.getId()==R.id.btnModifyCurrentTrack)
	           {
	        	   startActivityForResult(new Intent(getActivity(), TrackDialog.class),REQUEST_CODE );
	           }
	            else
	           {
	        	   Toast.makeText(getActivity(), "Contraseña Incorrecta", 
	        			   Toast.LENGTH_SHORT).show();
	        	   return;
	           }
	        }
	        });  

	        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int which) {
	                return;   
	            }
	        });
	       alert.show();
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(outState!=null)
		{
			outState.putBoolean("run", corriendo);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			if(track!=null)
			{
				track.close();
			}
		} catch (Exception e) {
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCancelCurrentTrack:
			//cancelamos alarma para iniciar servicio
			//alarmManager.cancel(Util.getServicePendingIntent (getActivity()));
		
			/*cancelAlarms();
			Toast.makeText(getActivity(), "Programación de Rastreo Cancelado", Toast.LENGTH_SHORT).show();
			showCurrentTrackInfo(false);
			 PreferenciasHancel.setAlarmStartDate(getActivity(), 0);*/
			//break;
		case R.id.btnModifyCurrentTrack:
			createPasswordDialog((Button) v);	
			//startActivityForResult(new Intent(getActivity(), TrackDialog.class),REQUEST_CODE );
			break;
		case R.id.IniciaTrackId:
			if(!corriendo)
			{
				startActivityForResult(new Intent(getActivity(), TrackDialog.class),REQUEST_CODE );
			}else
			{
				//si detenemos rastreo iniciamos un dialogo pidiendo password:
				createPasswordDialog(btnTracking);
			}
			break;
		default:
			break;
		}
	}
	private void cancelAlarms() {
		alarmManager.cancel(Util.getReminderPendingIntennt(getActivity()));
		alarmManager.cancel(Util.getStopSchedulePendingIntentWithExtra(getActivity()));
		
	}
		
}
