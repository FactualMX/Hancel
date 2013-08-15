package org.hansel.myAlert;

	import java.io.IOException;
import java.util.Calendar;

	import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.Util;
import org.hansel.myAlert.dataBase.UsuarioDAO;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;


	public class AlarmFragment extends DialogFragment   {
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			try {
				if(mMediaPlayer.isPlaying())
						mMediaPlayer.stop();
			} catch (Exception e) {
				// TODO: handle exception
			}
			mVibrator.cancel();
			handler.removeCallbacks(stop);
		}
		private  AlarmManager alarmManager;
		private UsuarioDAO usuarioDao;
		 private MediaPlayer mMediaPlayer;
		 private Vibrator mVibrator;
		 //handler para quitar la vibración en caso que no se apague la alarma
		 
		 private Handler handler = new Handler();
		 private Runnable stop= new Runnable() {
			@Override
			public void run() {
				if(mVibrator!=null)
				{
					mVibrator.cancel();
				}
				if(mMediaPlayer!=null)
				{
					mMediaPlayer.stop();
				}
			}
		};
		 
		private void Vibra()
		{
			int dot = 200;      // Length of a Morse Code "dot" in milliseconds
	    	int dash = 500;     // Length of a Morse Code "dash" in milliseconds
	    	int short_gap = 200;    // Length of Gap Between dots/dashes
	    	int medium_gap = 500;   // Length of Gap Between Letters
	    	int long_gap = 1000;    // Length of Gap Between Words
	    	long[] pattern = {
	    	    0,  // Start immediately
	    	    dot, short_gap, dot, short_gap, dot,    // s
	    	    medium_gap,
	    	    dash, short_gap, dash, short_gap, dash, // o
	    	    medium_gap,
	    	    dot, short_gap, dot, short_gap, dot,    // s
	    	    long_gap
	    	};
	    	mVibrator.vibrate(pattern,0);
		}
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			 getActivity().getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON | LayoutParams.FLAG_DISMISS_KEYGUARD);
			 mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
			 alarmManager = (AlarmManager) getActivity().getSystemService(Activity.ALARM_SERVICE);
			 usuarioDao = new UsuarioDAO(getActivity().getApplicationContext());
			 usuarioDao.open();
			 setAlarm();
			 Vibra();
				// tocamos la alarma:
				 String _uri = Util.getRingtone(getActivity().getApplicationContext());
					if(_uri!=null && _uri.length()>0)
					{
						
						//handler.post(vibrate);
						playSound(getActivity().getApplicationContext(), Uri.parse(_uri));
					}
			//preparamos handler para terminar alarma;
			handler.postDelayed(stop, 1000*60*2);		
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
			            if(usuarioDao.getPassword(value.trim())) //buscar en la BD la contraseña
			            {
				        	Log.v("Detener Rastreo");
				        	getActivity().stopService(new Intent(getActivity().getApplicationContext()
									,LocationManagement.class));
				        	
				        	alarmManager.cancel(Util.getPendingAlarmPanicButton(getActivity().getApplicationContext()));
				        	
				            Toast.makeText(getActivity(), "Rastreo Detenido", Toast.LENGTH_SHORT).show();
				            getActivity().finish();
			            return;                  
			           }else
			           {
			        	   Toast.makeText(getActivity(), "Contraseña Incorrecta", 
			        			   Toast.LENGTH_SHORT).show();
			        	   return;
			           }
			        }
			        });  

			        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

			            public void onClick(DialogInterface dialog, int which) {
			            	
			            	getActivity().finish();
			                return;   
			            }
			        });
			
			return alert.create();
			
		}
		
		private void setAlarm() {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, Util.getPanicDelay(getActivity().getApplicationContext()));
			alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 
					  Util.getPendingAlarmPanicButton(getActivity().getApplicationContext()));
			
		}
		private void playSound(Context context, Uri alert) {
	        mMediaPlayer = new MediaPlayer();
	        try {
	            mMediaPlayer.setDataSource(context, alert);
	            final AudioManager audioManager = (AudioManager) context
	                    .getSystemService(Context.AUDIO_SERVICE);
	            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
	                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
	                mMediaPlayer.prepare();
	                mMediaPlayer.start();
	            }
	        } catch (IOException e) {
	            System.out.println("Error tocando alarma");
	        }
	    }


	}
