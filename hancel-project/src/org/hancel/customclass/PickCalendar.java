package org.hancel.customclass;
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
import java.util.Calendar;

import org.hansel.myAlert.R;
import org.holoeverywhere.widget.CalendarView;
import org.holoeverywhere.widget.CalendarView.OnDateChangeListener;
import org.holoeverywhere.widget.LinearLayout;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class PickCalendar extends org.holoeverywhere.app.Activity {
	private Calendar currentDate = Calendar.getInstance();
	private final View.OnClickListener mActionBarListener = new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
	        onActionBarItemSelected(v.getId());
	    }
	};

	private boolean onActionBarItemSelected(int itemId) {
	    switch (itemId) {
	    case R.id.action_done:
	    	Intent i = new Intent();
	    	i.putExtra("DATE", currentDate);
	    	setResult(RESULT_OK, i);
	    	finish();
	        break;
	    case R.id.action_cancel:
	        System.err.println("cancel");
	        this.onBackPressed();
	        break;
	    }
	    return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_calendar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("Pick Date");
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 View actionBarButtons = inflater.inflate(R.layout.edit_event_custom_actionbar, new LinearLayout(this), false);

		    View cancelActionView = actionBarButtons.findViewById(R.id.action_cancel);
		    cancelActionView.setOnClickListener(mActionBarListener);

		    View doneActionView = actionBarButtons.findViewById(R.id.action_done);
		    doneActionView.setOnClickListener(mActionBarListener);
		    getSupportActionBar().setHomeButtonEnabled(false);
		    getSupportActionBar().setDisplayShowHomeEnabled(false);
		    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		    getSupportActionBar().setDisplayShowTitleEnabled(false);

		    getSupportActionBar().setDisplayShowCustomEnabled(true);
		    getSupportActionBar().setCustomView(actionBarButtons);
		    CalendarView calendar = (CalendarView)findViewById(R.id.calendarWeight);
		    
		    calendar.setOnDateChangeListener(new OnDateChangeListener() {
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				currentDate.set(year, month, dayOfMonth);
				
			}
		});	    
	
	}

}
