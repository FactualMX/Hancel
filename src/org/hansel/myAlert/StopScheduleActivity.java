package org.hansel.myAlert;

import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class StopScheduleActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		 am.cancel(Util.getReminderPendingIntennt(getApplicationContext()));
		 PreferenciasHancel.setAlarmStartDate(getApplicationContext(), 0);
			//ya se termino el tiempo de la programación cancelamos alarma y salimos
			Log.v("Fin de la programación");
			 NotificationCompat.Builder mBuilder =
					    new NotificationCompat.Builder(this)
					    .setSmallIcon(R.drawable.ic_launcher)
					    .setContentTitle("Rastreo Finalizado")
					    .setContentText("Gracias por usar Hancel")
					    ;
			
				Notification notif = mBuilder.build();
				notif.flags =  Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
				NotificationManager notificationManager = (NotificationManager) 
						  getSystemService(NOTIFICATION_SERVICE); 
			notificationManager.notify(0, notif);
			finish();
			return;
	}

}
