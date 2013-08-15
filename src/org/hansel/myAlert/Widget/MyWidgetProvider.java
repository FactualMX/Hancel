package org.hansel.myAlert.Widget;


	import org.hansel.myAlert.MainActivity;

import android.app.PendingIntent;
import org.hansel.myAlert.R;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

	public class MyWidgetProvider extends AppWidgetProvider {

	    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
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
	            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	            views.setOnClickPendingIntent(R.id.btnWidgetPanic, pendingIntent);

	            // Tell the AppWidgetManager to perform an update on the current app widget
	            appWidgetManager.updateAppWidget(appWidgetId, views);
	        }
	    }
	}