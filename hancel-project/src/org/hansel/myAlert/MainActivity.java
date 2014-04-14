package org.hansel.myAlert;
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
import java.util.ArrayList;

import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.Util;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends org.holoeverywhere.app.Activity{
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	   ViewPager mViewPager;
	@Override
	protected void onResume() {
		super.onResume();
		
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	boolean presionaPanico;
	//private Rastreo mFragmentTwo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		ServicioLeeBotonEncendido.login = MainActivity.this;
		startService(new Intent(MainActivity.this,ServicioLeeBotonEncendido.class));
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		presionaPanico = getIntent().getBooleanExtra("panico",false);
		Bundle data=null;
		if(presionaPanico)
		{
			data = new Bundle();
			data.putBoolean("panico", presionaPanico);
		}
		setContentView(R.layout.tabs);
		PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		if(ConnectionResult.SUCCESS ==GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()))
		{
			PanicButtonFragment mFragmentOne = new PanicButtonFragment();
			mFragmentOne.setArguments(data);
			 mPagerAdapter.addFragment(mFragmentOne,"Bot—n de P‡nico");
		}else
		{
			mPagerAdapter.addFragment(new NoPlayServicesFragment(), "Error");
		}

	     mViewPager = (ViewPager) super.findViewById(R.id.pager);
	    mViewPager.setAdapter(mPagerAdapter);
	    TabPageIndicator  indicator = (TabPageIndicator)findViewById(R.id.indicator);
	    indicator.setViewPager(mViewPager);
	    mViewPager.setOffscreenPageLimit(2);
	   // mViewPager.setCurrentItem(0);
	}
	@Override
    public void onConfigurationChanged(Configuration newConfig) 
    {
        super.onConfigurationChanged(newConfig);
        Log.v("onConfigurationChangedMain");
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(outState!=null)
		{
			//outState.putBoolean("panico", presionaPanico);
		}
	}
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		this.moveTaskToBack(true);
		/*if(mFragmentTwo!=null && mFragmentTwo.isTracking())
		{
			Toast.makeText(getApplicationContext(), "No se puede salir durante el rastreo", 
					Toast.LENGTH_SHORT).show();
		}else
		{
			super.onBackPressed();
		} */
	}

	public class PagerAdapter extends FragmentPagerAdapter   {

	    @Override
		public CharSequence getPageTitle(int position) {
		return titulos.get(position);
		}

		private final ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	    private final ArrayList<String> titulos = new ArrayList<String>();
	    

	    public PagerAdapter(FragmentManager manager) {
	        super(manager);
	    }

	    public void addFragment(Fragment fragment,String title) {
	        mFragments.add(fragment);
	        titulos.add(title);
	        notifyDataSetChanged();
	    }

	    @Override
	    public int getCount() {
	        return mFragments.size();
	    }

	    @Override
	    public Fragment getItem(int position) {
	        return mFragments.get(position);
	    }

		
	}
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	 	MenuInflater infla = getSupportMenuInflater();
			infla.inflate(R.menu.main, menu);
	     return true;
	    }
	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.CMD_PREFERENCES:
	        	if(!Util.isMyServiceRunning(getApplicationContext()))
	        	{
	        		startActivity(new Intent(this , Preferencias.class ));
	        	}else
	        	{
	        		Toast.makeText(getApplicationContext(), "No se peude modificar en este momento...", 
	        				Toast.LENGTH_SHORT).show();
	        	}
	            return true;
	        default:
	            return false;
	        }
	    }
}
