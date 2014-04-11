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
import java.io.IOException;
import java.util.Date;

import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

public class ReminderService extends Service {


	public static int NOTIFICATION_ID=0;
	private MediaPlayer mMediaPlayer;
	public static final String CANCEL_ALARM_BROADCAST = "org.hancel.myalert.cancel_alarm";
	private Vibrator mVibrator;
	private Runnable run;
	
	private BroadcastReceiver alarmReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			cancelAlarm();
		}
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
		getApplicationContext().unregisterReceiver(alarmReceiver);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		//obtenemos el "número de intentos"
		getApplicationContext().registerReceiver(alarmReceiver, new IntentFilter(CANCEL_ALARM_BROADCAST));
		int count = PreferenciasHancel.getReminderCount(getApplicationContext());
		count++;
		PreferenciasHancel.setReminderCount(getApplicationContext(), count);
		Log.v("Conteo: "+ count);
		if(count>=3)
		{
			//detenemos la alarma del servicio de recordatorios. y lanzamos el servicio de Tracking 
			 AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			 am.cancel(Util.getReminderPendingIntennt(getApplicationContext()));
			 NotificationManager notificationManager = (NotificationManager) 
					  getSystemService(NOTIFICATION_SERVICE); 
			 notificationManager.cancel(NOTIFICATION_ID);
			 Log.v("Servicio de Rastreo....");
			 Util.inicarServicio(getApplicationContext());
			 
			 startService(new Intent(getApplicationContext(), SendPanicService.class));
			 stopSelf();
		}else
		{
			//mandamos una alerta de notificación
			showNotifciation();
			playSound();
			 mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			 Vibra();
			Handler han = new Handler();
			run = new Runnable() {
				
				@Override
				public void run() {
					cancelAlarm();
					stopSelf();
				}
			};
			han.postDelayed(run, 1000*10);
			
			//alarma para "regresar" en caso que el usuario no de "click" en la notificación
			 AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		        long due = System.currentTimeMillis() + (60000*3); // 3 minutos
		        Log.v("Scheduling next update at " + new Date(due));
		        am.set(AlarmManager.RTC_WAKEUP, due, Util.getReminderPendingIntennt(getApplicationContext()));
			
		}
		
	}

	protected void cancelAlarm() {
		if(mMediaPlayer!=null && mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();
		}
		if(mVibrator!=null) mVibrator.cancel();
	}

	private void playSound() {
		
		 mMediaPlayer = new MediaPlayer();
	        try {
	            mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(Util.getRingtone(getApplicationContext())) );
	            final AudioManager audioManager = (AudioManager) 
	                    getSystemService(Context.AUDIO_SERVICE);
	            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
	                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
	                mMediaPlayer.prepare();
	                mMediaPlayer.setLooping(true);
	                mMediaPlayer.start();
	            }
	        } catch (IOException e) {
	            System.out.println("Error tocando alarma");
	        }
	}
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

	private void showNotifciation()
	{
		 Intent i=new Intent(this, ManageRemindersActivity.class);
		 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		    i.addCategory(Intent.CATEGORY_LAUNCHER);
			PendingIntent pi=PendingIntent.getActivity(this, 0,
			                         i, 0);
			
		 NotificationCompat.Builder mBuilder =
				    new NotificationCompat.Builder(this)
				    .setSmallIcon(R.drawable.ic_launcher)
				    .setContentTitle("Todo esta Bien?")
				    .setContentText("Toca aqui para reportarte?")
				    .setContentIntent(pi)
				    ;
		
			Notification notif = mBuilder.build();
			NotificationManager notificationManager = (NotificationManager) 
					  getSystemService(NOTIFICATION_SERVICE); 
		notificationManager.notify(NOTIFICATION_ID, notif);
		//startForeground(1237, notif);
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
