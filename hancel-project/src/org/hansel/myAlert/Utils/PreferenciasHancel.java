package org.hansel.myAlert.Utils;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.hansel.myAlert.Log.Log;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.preference.SharedPreferences;
import org.holoeverywhere.preference.SharedPreferences.Editor;

import android.content.Context;

public class PreferenciasHancel {
	
	public static final String PREF_GENERAL="GeneralPrefs";//files
	
	public static final String PREF_GENERAL_PANIC_ALERT="lastPanic";//files
	private static final String INSTALLATION_DEVICE  = "data.garbage";
	public static final String PREF_GENERAL_USER_ID="id";//files
	public static final String PREF_GENERAL_ONG="ong_id_list";//files
	public static final String PREF_GENERAL_WIZARD_STEP="wizard_step";//files
	public static final String PREF_GENERAL_REMINDER_COUNT="reminder_count";//files
	public static final String PREF_GENERAL_ALARM_START="alarm_start";
	private static String ID;
	
	public static boolean getLoginOk(Context context)
	{
		
		SharedPreferences prefe1=PreferenceManager.wrap(context, Util.PREF_SESSION, Context.MODE_PRIVATE);
		return prefe1.getBoolean(Util.PREF_GET_LOGIN_OK,false);
	}
	public static void setLastPanicAlert(Context context,String time)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 Editor ed = prefs.edit();
		 ed.putString(PREF_GENERAL_PANIC_ALERT, time);
		 ed.commit();
	 }
	public static String getLastPanicAlert(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 return prefs.getString(PREF_GENERAL_PANIC_ALERT, "Sin Alertas");
	 }
	public static long getAlarmPreferenceInMilis(Context context)
	{
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		int intervalo = Integer.valueOf(sp.getString("pref_key_intervalo_recordatorio","2"));
		return intervalo*3600000; // 3 600 000 = 1 hora
	}
	public static void setUserId(Context context,int id)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 Editor ed = prefs.edit();
		 ed.putInt(PREF_GENERAL_USER_ID, id);
		 ed.commit();
	 }
	public static int getUserId(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 return prefs.getInt(PREF_GENERAL_USER_ID, 0);
	 }

	public static void setSelectedOng(Context context,String ongCsv)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 Editor ed = prefs.edit();
		 ed.putString(PREF_GENERAL_ONG, ongCsv);
		 ed.commit();
	 }
	public static String getSelectedOng(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 return prefs.getString(PREF_GENERAL_ONG, "");
	 }
	public static void setReminderCount(Context context,int count)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 Editor ed = prefs.edit();
		 ed.putInt(PREF_GENERAL_REMINDER_COUNT, count);
		 ed.commit();
	 }
	public static int getReminderCount(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 return prefs.getInt(PREF_GENERAL_REMINDER_COUNT, 0);
	 }
	public static void setAlarmStartDate(Context context,long dateInMilis)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 Editor ed = prefs.edit();
		 ed.putLong(PREF_GENERAL_ALARM_START, dateInMilis);
		 ed.commit();
	 }
	public static long getAlarmStartDate(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 return prefs.getLong(PREF_GENERAL_ALARM_START, 0);
	 }
	
	public static void setCurrentWizardStep(Context context,int step)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 Editor ed = prefs.edit();
		 ed.putInt(PREF_GENERAL_WIZARD_STEP, step);
		 ed.commit();
	 }
	public static int getCurrentWizardStep(Context context)
	 {
		 SharedPreferences prefs = PreferenceManager.wrap(context,PREF_GENERAL,Context.MODE_PRIVATE);
		 return prefs.getInt(PREF_GENERAL_WIZARD_STEP, Util.REGISTRO_PASO_1);
	 }
	
	
	
	public static boolean setDeviceId(Context context,String device_id)
	 {
		File path = new File(context.getFilesDir(), INSTALLATION_DEVICE);
		//creamos un id diferente por cada "login" o registro
		try {
			if(path.exists())
			{
				path.delete();
			}

			String id = SimpleCrypto.md5(device_id);
			FileOutputStream outputStream;
			 outputStream = context.openFileOutput(path.getAbsolutePath(), Context.MODE_PRIVATE);
			  outputStream.write(id.getBytes());
			  outputStream.close();
			  ID = id;
			  return true;
		} catch (Exception e) {
			Log.v("Error al generar el ID");
		}
		return false;
	 }
	public synchronized static String getDeviceId(Context context)
	 {
		if(ID!=null)
		{
			return ID;
		}
		File path = new File(context.getFilesDir(), INSTALLATION_DEVICE);
		if(!path.exists())
		{
			return "1";
		}
		try {
			
		} catch (Exception e) {
			Log.v("Error al obtener el ID");
		}
		RandomAccessFile randomAccessFile;
		try {
			randomAccessFile = new RandomAccessFile(path, "r");
		  byte[] fileBytes = new byte[(int) randomAccessFile.length()];
		  randomAccessFile.readFully(fileBytes);
		  randomAccessFile.close();
	        return new String(fileBytes);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
		return "1";
		
	 }
	
	
}
