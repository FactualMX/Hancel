package org.hansel.myAlert.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class UsuarioDAO extends SQLiteHelper{
 
	public static final String DATABASE_TABLE = "t_Usuario";
	public static final String KEY_ID = "_id";
	public final static String usuario = "usuario";
	public static final String password = "password";
	public static final String mail = "mail";
	
	
	public UsuarioDAO(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	} 
	
	public int getList(String mUsr,String mPass){
		Cursor c= super.mDb.rawQuery("select * from "+DATABASE_TABLE+
				" where "+usuario+" like '"+mUsr+
				"' and "+password+" like '"+mPass+"'" ,null );
		int id=0;
		if(c.moveToFirst()){
			id = c.getInt(0);
		}
		return id;
	}
	public boolean updateTurno(int id,String mUsr,String mPass,String mMail) {
		ContentValues newValues = new ContentValues();		
		newValues.put(usuario, mUsr);
		newValues.put(password, mPass);
		newValues.put(mail, mMail);
		return super.mDb.update(DATABASE_TABLE,newValues , KEY_ID +"= "+id , null) > 0;
	} 
	public boolean getPassword(String Password)
	{
		Cursor c= super.mDb.rawQuery("select * from "+DATABASE_TABLE+
					" where "+password +"='"+Password+"'",null
				);
		if(c.moveToFirst())
		{
			return true;
		}
		return false;
	}
	public String getUser()
	{ //solo un usuario por session
		String user ="";
		Cursor usr = super.mDb.rawQuery("select usuario from "+DATABASE_TABLE
				, null);
		if(usr.moveToFirst())
		{
			user =usr.getString(0);
		}
		return user;
	}
	public long Insertar(String mUsr,String mPass,String mMail) {
		
		mDb.delete(DATABASE_TABLE, null, null);
		ContentValues newValues = new ContentValues();		
		newValues.put(usuario, mUsr);
		newValues.put(password, mPass);
		newValues.put(mail, mMail);			
		System.out.println("Se quiere insertar en la tabla de Equipos ---> " + newValues.toString());		
		return super.mDb.insert(DATABASE_TABLE, null, newValues);
	}		
}