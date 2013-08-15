package org.hansel.myAlert.Log;

public class Log {

	public static String LOGTAG = "Hansel";
	public static void v(String mensaje)
	{
		android.util.Log.v(LOGTAG, mensaje);
	}
}
