package org.hancel.adapters.customUi;

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