package org.hancel.adapters.customUi;
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

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public  class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {
	private Calendar calendar;
	private boolean isCustomDate=false;
	public TimePickerFragment(Calendar calendar)
	{
		this.calendar=calendar;
		isCustomDate=true;
		
	}
	public TimePickerFragment()
	{
		super();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		if(!isCustomDate)
		{
			calendar =  Calendar.getInstance();
		}
		
		int hour = calendar .get(Calendar.HOUR_OF_DAY); 
		int minute = calendar .get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), (TrackDialog) getActivity(), hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
	}
}