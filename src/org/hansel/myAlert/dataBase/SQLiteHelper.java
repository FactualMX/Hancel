package org.hansel.myAlert.dataBase;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class SQLiteHelper{
	
	private Context mCtx = null;
	private DataBaseHelperInternal mDBHelper = null;
	protected SQLiteDatabase mDb = null;
	private static final String DATABASE_NAME = "ALLER";
	private static final int DATABASE_VERSION = 3;
	 
    
	private static final String sqlTablaUsuario = "CREATE TABLE t_Usuario (_id INTEGER PRIMARY KEY  NOT NULL , usuario TEXT, password TEXT, mail TEXT)";
	private static final String sqlTablaContactoMail = "CREATE TABLE C_ContactoMail (_id INTEGER PRIMARY KEY  NOT NULL , mail TEXT,fkIdContacto INTEGER)";
	private static final String sqlTablaContactoNumero = "CREATE TABLE C_ContactoNumero (_id INTEGER PRIMARY KEY  NOT NULL , numero INTEGER,fkIdContacto INTEGER)";
	private static final String sqlTablaContacto = "CREATE TABLE t_Contacto (_id INTEGER PRIMARY KEY  NOT NULL , id_padre_interno INTEGER, phoneNumbers TEXT, emails TEXT)";
	private static final String sqlTablaConfig = "CREATE TABLE t_Config (_id INTEGER PRIMARY KEY  NOT NULL , intevalo TEXT, esperaAlerta TEXT, fkIdSesion INTEGER, mensaje TEXT)";
	private static final String sqlTablaAlerta = "CREATE TABLE t_Alerta (_id INTEGER PRIMARY KEY  NOT NULL , esperaAlerta TEXT, fkIdSesion INTEGER)";
	private static final String sqlTablaEvento = "CREATE TABLE t_Evento (_id INTEGER PRIMARY KEY  NOT NULL , estatus INTEGER, fkIdSesion INTEGER)";
	public static final String sqlTablaTrack = "CREATE TABLE t_Track (_id INTEGER PRIMARY KEY  NOT NULL ,androidId INTEGER NOT NULL, fecha TEXT)";
	
	
        
    /** constructor **/
    public SQLiteHelper(Context ctx){
    	this.mCtx = ctx;
    }
    
    public SQLiteHelper open() throws SQLException{
    	System.out.println("**** Abriendo la Base de datos *****");
    	mDBHelper = new DataBaseHelperInternal(mCtx);
    	mDb = mDBHelper.getWritableDatabase();
    	return this;
    }
    
    public void close(){
    	System.out.println("**** Cerrando la Base de datos *****");
    	mDBHelper.close();
    }
    
    
    /**  Clase privada para el control del SQLite **/
    private static class DataBaseHelperInternal extends SQLiteOpenHelper{
    	
    	public DataBaseHelperInternal(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
    	public void onCreate(SQLiteDatabase db) {
    		//Se ejecuta la sentencia SQL de creación de la tabla
			System.out.println("Se estan creando las tablas------------->>>");
			db.execSQL(sqlTablaUsuario);
			db.execSQL(sqlTablaContactoMail);
			db.execSQL(sqlTablaContactoNumero);
			db.execSQL(sqlTablaContacto);
			db.execSQL(sqlTablaConfig);
			db.execSQL(sqlTablaAlerta);
			db.execSQL(sqlTablaEvento);
			db.execSQL(sqlTablaTrack);
	    	insertarDemo(db);
    	}    

	    @Override
	    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
	        /**NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
	                 eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
	                 Sin embargo lo normal será que haya que migrar datos de la tabla antigua
	                 a la nueva, por lo que este método debería ser más elaborado.**/
	 
	        //Se elimina la versión anterior de la tabla
	    	
	        db.execSQL("DROP TABLE IF EXISTS t_Contacto");
	    	onCreate(db);
	    }
	    	    
	    public void insertarDemo(SQLiteDatabase db){
	    	System.out.println("Se estan insertando en las tablas------------->>>");
	 //   	db.execSQL("insert into sesion (nombre, descripcion) values ('Seleccione un servicio','uno')");
	    		    		    
	   }
	    
    }
}