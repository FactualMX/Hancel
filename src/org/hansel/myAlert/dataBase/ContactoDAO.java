package org.hansel.myAlert.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ContactoDAO extends SQLiteHelper{
 
	public static final String DATABASE_TABLE = "t_Contacto";
	public static final String KEY_ID = "_id";
	public final static String IdPadre = "id_padre_interno";
	public static final String telefonos= "phoneNumbers";
	public static final String emails= "emails";
	public static final String fkIdUsuario = "fkIdUsuario";
	
	
	public ContactoDAO(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	} 
	
	public Cursor getList(){
		return super.mDb.rawQuery("select * from "+DATABASE_TABLE, null);
	}
	public int getList(int idPadre){
		int id=0;
		Cursor c=super.mDb.rawQuery("select * from "+DATABASE_TABLE+" where "+IdPadre+" = "+idPadre, null);		
		if(c.moveToFirst()){
			id = c.getInt(0);
		}		
		return id;
		 
	}
	public boolean deleteTurno(int mPadre) {				
		return super.mDb.delete(DATABASE_TABLE, IdPadre+" = "+mPadre, null) > 0;
	}
	
	public boolean updateTurno(int id,int mPadre,String mNom) {
		ContentValues newValues = new ContentValues();		
		newValues.put(IdPadre, mPadre);
		newValues.put(telefonos, mNom);		
		return super.mDb.update(DATABASE_TABLE,newValues , KEY_ID +"= "+id , null) > 0;
	} 
	public long Insertar(int mPadre,String mNom,String emailList) {		
		ContentValues newValues = new ContentValues();		
		newValues.put(IdPadre, mPadre);
		newValues.put(telefonos, mNom);
		newValues.put(emails, emailList);
		System.out.println("Se quiere insertar en la tabla de Equipos ---> " + newValues.toString());		
		return super.mDb.insert(DATABASE_TABLE, null, newValues);
	}		
}