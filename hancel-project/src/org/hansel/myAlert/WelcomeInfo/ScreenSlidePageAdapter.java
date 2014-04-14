package org.hansel.myAlert.WelcomeInfo;
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
