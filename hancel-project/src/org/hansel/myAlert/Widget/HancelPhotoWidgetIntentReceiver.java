package org.hansel.myAlert.Widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Jacek Milewski
 * looksok.wordpress.com
 */

public class HancelPhotoWidgetIntentReceiver extends BroadcastReceiver {

	private static int clickCount = 0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if(intent.getAction().equals("pl.looksok.intent.action.CHANGE_PICTURE")){
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
	}
	
}
