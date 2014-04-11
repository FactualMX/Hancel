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
import java.util.Date;

import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ManageRemindersActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//usuario dio clic a la notificación reseteamos contador
		PreferenciasHancel.setReminderCount(getApplicationContext(), 0);
		 AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		 am.cancel(Util.getReminderPendingIntennt(getApplicationContext()));
		//cancelamos la alarma de recordatorio del servicio de 3 minutos
		//iniciamos una alarma cada 2.4,5, horas para preguntar de nuevo
		 //nueva alarma dentro de X horas
		 //detenenmos servicio de alarma
		 Intent cancelAlarmSound = new Intent(ReminderService.CANCEL_ALARM_BROADCAST);
		 sendBroadcast(cancelAlarmSound);
		 
		 long due = System.currentTimeMillis() +PreferenciasHancel.getAlarmPreferenceInMilis(getApplicationContext());
		 // long due = System.currentTimeMillis() + (60000*2); // 10 minutos de prueba
	        Log.v("User clic siguiente actualización " + new Date(due));
	        am.set(AlarmManager.RTC_WAKEUP, due, Util.getReminderPendingIntennt(getApplicationContext()));
	    	NotificationManager notificationManager = (NotificationManager) 
					  getSystemService(NOTIFICATION_SERVICE); 
		notificationManager.cancel(ReminderService.NOTIFICATION_ID);
		 finish();
	}

}
