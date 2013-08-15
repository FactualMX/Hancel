package org.hansel.myAlert;


import android.os.Bundle;


public class AlarmReceiver extends org.holoeverywhere.app.Activity{
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		AlarmFragment alarm = new AlarmFragment();
		alarm.show(getSupportFragmentManager(), "AlarmHansel");
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	


}
