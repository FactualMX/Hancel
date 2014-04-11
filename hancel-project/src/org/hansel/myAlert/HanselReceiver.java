package org.hansel.myAlert;

import org.hansel.myAlert.Utils.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HanselReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
		{
			//si se reinicia, verificamos si estaba corriendo el servicio
			if(Util.getRunningService(context))
			{
				if(!Util.isMyServiceRunning(context)) // solo para verificar que no esta corriendo e intrrumpir el "timer"
				{
					Util.inicarServicio(context);
				}
			}
		}
	}

}
