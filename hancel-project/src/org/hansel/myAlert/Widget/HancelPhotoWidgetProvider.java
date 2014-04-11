package org.hansel.myAlert.Widget;
import org.hansel.myAlert.MainActivity;
import org.hansel.myAlert.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.RemoteViews;

/**
 * Created by Jacek Milewski
 * looksok.wordpress.com
 */

public class HancelPhotoWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
	
		 final int N = appWidgetIds.length;

	        // Perform this loop procedure for each App Widget that belongs to this provider
	        for (int i=0; i<N; i++) {
	            int appWidgetId = appWidgetIds[i];

	            // Create an Intent to launch ExampleActivity
	            Intent intent = new Intent(context, MainActivity.class);
	            intent.putExtra("panico", true);
	            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

	            // Get the layout for the App Widget and attach an on-click listener
	            // to the button
	            RemoteViews remoteViews_foto = new RemoteViews(context.getPackageName(), R.layout.widget_foto_hancel);
	    		remoteViews_foto.setImageViewBitmap(R.id.widget_image, getImageToSet());
	    		//remoteViews_foto.setOnClickPendingIntent(R.id.widget_image, HancelPhotoWidgetProvider.buildButtonPendingIntent(context));	
	            
	           
	    		remoteViews_foto.setOnClickPendingIntent(R.id.widget_image, pendingIntent);

	            // Tell the AppWidgetManager to perform an update on the current app widget
	            appWidgetManager.updateAppWidget(appWidgetId, remoteViews_foto);
	        }
	}

	
	private Bitmap getImageToSet() {
		return BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/hancel/imagenhancel.jpg");
	}
	
}
