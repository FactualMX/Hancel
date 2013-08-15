package org.hancel.adapters.customUi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hansel.myAlert.R;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import org.hancel.customclass.PickCalendar;
import org.hancel.customclass.TrackDate;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class TrackDialog extends SherlockFragmentActivity 
implements OnClickListener , OnDateSetListener,
OnTimeSetListener, OnCancelListener, android.content.DialogInterface.OnClickListener{
	private static final long ONE_MINUTE_IN_MILLIS=60000;
	private Button btnStartTrackingDate;
	private Button btnEndTrackingDate;
	private Button btnStartTrackingHour;
	private Button btnEndTrackingHour;
	private Button btnResetDateTime;
	private Button btnCancel;
	private Button btnOk;
	//variables para indicar cual "boton" se selecciono
	private boolean isDateStart=true;
	private boolean isTimeStart=true;
	private TrackDate trackDate= new TrackDate();
	//private boolean isOkButtonPressed;
	public static final String TRACK_EXTRA="track_extra";
	public static final int TRACK_RESULT_CODE=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_track_dialog);
		btnStartTrackingDate = (Button) findViewById(R.id.btnStartTrackDate);
		btnEndTrackingDate= (Button) findViewById(R.id.btnEndTrackDate);
		btnStartTrackingHour= (Button) findViewById(R.id.btnStartTrackHour);
		btnEndTrackingHour= (Button) findViewById(R.id.btnEndTrackHour);
		btnResetDateTime = (Button) findViewById(R.id.btnResetInictalTrack);
		btnCancel = (Button) findViewById(R.id.btnCancelDateTrack);
		btnOk= (Button) findViewById(R.id.btnAceptDateTrack);
		this.setTitle("Programar Rastreo");
		setCurrentDate();
		setEndingDate();
		setupUI();
	}
	
	private void setupUI() {
		btnStartTrackingDate.setOnClickListener(this);
		btnEndTrackingDate.setOnClickListener(this);
		btnStartTrackingHour.setOnClickListener(this);
		btnEndTrackingHour.setOnClickListener(this);
		btnResetDateTime.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		
	}

	private void setCurrentDate()
	{
		Calendar cal = Calendar.getInstance();
		//guardamos datos iniciales para usarlos después
		trackDate.setStartDateTrack(cal);
		trackDate.setStartTimeTrack(cal);
		
		SimpleDateFormat sdf =getDateSimpleFormat();
		btnStartTrackingDate.setText(sdf.format(cal.getTime()));
		sdf = getTimeSimpleFormat();
		btnStartTrackingHour.setText(sdf.format(cal.getTime()));
	}
	private void setEndingDate()
	{
		Calendar cal = Calendar.getInstance();
		long time = cal.getTimeInMillis()+  (60 * ONE_MINUTE_IN_MILLIS );
		cal.setTimeInMillis(time);
		trackDate.setEndDateTrack(cal);
		trackDate.setEndTimeTrack(cal);
		
		SimpleDateFormat sdf = getDateSimpleFormat();
		btnEndTrackingDate.setText(sdf.format(new Date(time)  ));
		sdf = getTimeSimpleFormat();
		btnEndTrackingHour.setText(sdf.format(new Date(time)));
	}
	
	private void showTimePickerDialog	(Calendar cal) {
		FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(
				"timePicker");
 
		if (prev != null) {
			ft.remove(prev);
		}
 
		// Create and show the dialog.
		try {
			  DialogFragment newFragment =new TimePickerFragment(cal);
			    newFragment.show(getSupportFragmentManager(), "timePicker");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showDatePickerDialog(View v) {
		FragmentTransaction ft =  getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag(
				"datePicker");
 
		if (prev != null) {
			ft.remove(prev);
		}
 
		// Create and show the dialog.
		try {
			DialogFragment newFragment = new DatePickerFragment();
		    newFragment.show(getSupportFragmentManager(), "datePicker");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SimpleDateFormat getTimeSimpleFormat()
	{
		return new SimpleDateFormat("hh:mm aa");
	}
	private SimpleDateFormat getDateSimpleFormat()
	{
		return new SimpleDateFormat("MMMM dd, yyyy",Locale.getDefault());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
		if(requestCode==0 && resultCode == RESULT_OK)
		{
			Button v ;
			Calendar cal = (Calendar)data.getExtras().get("DATE");
			Calendar end =Calendar.getInstance();
			end.setTimeInMillis(cal.getTimeInMillis());
			if(isDateStart)
			{
				v= btnStartTrackingDate;
				trackDate.setStartDateTrack(cal);
				end.set(Calendar.MINUTE,   trackDate.getStartTimeTrack().get(Calendar.MINUTE));
				end.set(Calendar.HOUR, trackDate.getStartTimeTrack().get(Calendar.HOUR));
				end.set(Calendar.SECOND, trackDate.getStartTimeTrack().get(Calendar.SECOND));
				
				trackDate.setStartTimeTrack(end);
				
			}else
			{
				v=btnEndTrackingDate;
				trackDate.setEndDateTrack(cal);
				end.set(Calendar.MINUTE,   trackDate.getEndTimeTrack().get(Calendar.MINUTE));
				end.set(Calendar.HOUR, trackDate.getEndTimeTrack().get(Calendar.HOUR));
				end.set(Calendar.SECOND, trackDate.getEndTimeTrack().get(Calendar.SECOND));
				trackDate.setEndTimeTrack(end);
			}
			SimpleDateFormat sdf = getDateSimpleFormat();
			v.setText(sdf.format(cal.getTime()));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStartTrackDate:
			isDateStart=true;
			startActivityForResult(new Intent(getApplicationContext(), PickCalendar.class), 0);
			//showDatePickerDialog(v);
			break;
		case R.id.btnEndTrackDate:
			isDateStart=false;
			startActivityForResult(new Intent(getApplicationContext(), PickCalendar.class), 0);
			//showDatePickerDialog(v);
			break;
		case R.id.btnStartTrackHour:
			isTimeStart=true;
			showTimePickerDialog(trackDate.getStartDateTrack());
			break;
		case R.id.btnEndTrackHour:
			isTimeStart=false;
			showTimePickerDialog(trackDate.getEndDateTrack());
			break;
		case R.id.btnResetInictalTrack:
			setCurrentDate();
			break;
		case R.id.btnAceptDateTrack:
			long timeMilis  = trackDate.getStartTimeTrack().getTimeInMillis() + (ONE_MINUTE_IN_MILLIS * 10);
			Calendar stop = Calendar.getInstance();
			stop.setTimeInMillis(timeMilis);
			
		   if(trackDate.getEndTimeTrack().compareTo(stop)!=1  || trackDate.getEndDateTrack().compareTo(stop) !=1 ){
			   Toast.makeText(getApplicationContext(), "La hora para detener debe  ser mayor a los minutos configurados", Toast.LENGTH_SHORT).show();
			   return;
		   }
			setupResult();
			break;
		case R.id.btnCancelDateTrack:
			finish();
			break;
		default:
			break;
		}
	}

	private void setupResult() {
		Intent result = new Intent();
		result.putExtra(TRACK_EXTRA, trackDate);
		setResult(RESULT_OK,result);
		finish();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		//seteamos la fecha conforme lo seleccionado
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		Calendar end =Calendar.getInstance();
		end.setTimeInMillis(cal.getTimeInMillis());
		
		
		Button v ;
		if(isDateStart)
		{
			v= btnStartTrackingDate;
			trackDate.setStartDateTrack(cal);
			end.set(Calendar.MINUTE,   trackDate.getStartTimeTrack().get(Calendar.MINUTE));
			end.set(Calendar.HOUR, trackDate.getStartTimeTrack().get(Calendar.HOUR));
			end.set(Calendar.SECOND, trackDate.getStartTimeTrack().get(Calendar.SECOND));
			
			trackDate.setStartTimeTrack(end);
			
		}else
		{
			v=btnEndTrackingDate;
			trackDate.setEndDateTrack(cal);
			end.set(Calendar.MINUTE,   trackDate.getEndTimeTrack().get(Calendar.MINUTE));
			end.set(Calendar.HOUR, trackDate.getEndTimeTrack().get(Calendar.HOUR));
			end.set(Calendar.SECOND, trackDate.getEndTimeTrack().get(Calendar.SECOND));
			trackDate.setEndTimeTrack(end);
		}
		SimpleDateFormat sdf = getDateSimpleFormat();
		v.setText(sdf.format(cal.getTime()));
		
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		//seteamos la hora conforme lo seleccionado

		Calendar cal =  isTimeStart? trackDate.getStartDateTrack() : trackDate.getEndDateTrack();
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		
		SimpleDateFormat sdf = getTimeSimpleFormat();
		Button v;
		if(isTimeStart)
		{
			v= btnStartTrackingHour;
			trackDate.setStartDateTrack(cal);
			
		}else
		{
			v=btnEndTrackingHour;
			trackDate.setEndTimeTrack(cal);
		}
		v.setText(sdf.format(cal.getTime()));
		
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		//isOkButtonPressed=false;
		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
	//	isOkButtonPressed=false;
	}
}
