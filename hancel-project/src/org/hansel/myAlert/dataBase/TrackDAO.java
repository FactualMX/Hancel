package org.hansel.myAlert.dataBase;
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
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TrackDAO extends SQLiteHelper{

	public static final String DATABASE_NAME ="t_Track";
	public static final String KEY_ID = "_id";
	public static final String FECHA = "fecha";
	public static final String ANDROID_ID = "androidId";
	
	
	public TrackDAO(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	} 
	
	public int getList(String mUsr,String mPass){
		Cursor c= super.mDb.rawQuery("select top 1 max(_id) from "+SQLiteHelper.sqlTablaTrack
				,null );
		int id=0;
		if(c.moveToFirst()){
			id = c.getInt(0);
		}
		return id;
	}
	private int getLastAndroidId(){
		Cursor c= super.mDb.rawQuery("select max("+  ANDROID_ID   +") from "+DATABASE_NAME + " LIMIT 1"
				,null );
		int id=0;
		if(c.moveToFirst()){
			id = c.getInt(0);
		}
		return id;
	}
	public long Insertar(String mUsr) {
		ContentValues newValues = new ContentValues();		
		int idAnd = (getLastAndroidId() +1 );
		newValues.put(ANDROID_ID,(idAnd));
		newValues.put(FECHA, mUsr);
		super.mDb.insert(DATABASE_NAME, null, newValues);
		return idAnd;
	}
	public void borraTabla()
	{
		mDb.delete(DATABASE_NAME, null, null);
	}
	public long InsertaNewId(String fecha,int id) {
		borraTabla();
		ContentValues newValues = new ContentValues();		
		newValues.put(ANDROID_ID,id);
		newValues.put(FECHA, fecha);
		return super.mDb.insert(DATABASE_NAME, null, newValues);
	}	
	

}
