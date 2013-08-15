package org.hancel.adapters.customUi;

import java.util.Calendar;

import org.hansel.myAlert.Log.Log;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public  class DatePickerFragment extends DialogFragment implements
	android.app.DatePickerDialog.OnDateSetListener, OnDateChangedListener{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		DatePickerDialog dt =new DatePickerDialog(getActivity(), (TrackDialog)getActivity() , year, month, day);
		dt.setButton(DialogInterface.BUTTON_POSITIVE, "OK",(TrackDialog)getActivity());
		dt.setOnCancelListener((TrackDialog)getActivity());
		return dt;
	}
	@Override
	public void onDateSet(DatePicker view, int year, int month, int day) {
		Log.v("fecha");
	}
	   @Override
       public void onDateChanged(DatePicker view, int year, int month, int day) {
		   Log.v("fecha");
       }
}