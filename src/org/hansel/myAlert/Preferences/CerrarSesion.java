package org.hansel.myAlert.Preferences;

import org.hansel.myAlert.Login;
import org.hansel.myAlert.Utils.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;

public class CerrarSesion extends org.holoeverywhere.preference.DialogPreference{
	  @Override
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		if(which == DialogInterface.BUTTON_POSITIVE)
		{
			//quitamos autologin
			Util.setLoginOkInPreferences(getContext(), false);
			Intent i = new Intent(getContext(),Login.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK 
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			getContext().startActivity(i);
		}
	}

	public CerrarSesion(Context context, AttributeSet attrs) {
		    super(context, attrs);
		  
		  }

	@Override
	public void setPositiveButtonText(CharSequence positiveButtonText) {
		super.setPositiveButtonText(positiveButtonText);
		 
	}

	@Override
	public void setPositiveButtonText(int positiveButtonTextResId) {
		super.setPositiveButtonText(positiveButtonTextResId);
		
	}
}
