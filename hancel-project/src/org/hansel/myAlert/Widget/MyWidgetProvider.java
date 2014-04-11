package org.hansel.myAlert.Widget;
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