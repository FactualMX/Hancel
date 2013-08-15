package org.hansel.myAlert.dataBase;

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
