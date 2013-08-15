package org.hansel.myAlert.WelcomeInfo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ScreenSlidePageAdapter extends FragmentStatePagerAdapter {

	public ScreenSlidePageAdapter(FragmentManager fragmentManager){
		super(fragmentManager);
	}
	
	@Override
	public Fragment getItem(int arg0) {
		
		Welcome01Fragment w = new Welcome01Fragment();
		w.setNumber(arg0);
		return w;
	}
	@Override
	public int getCount() {
		return 5;
	}

}
