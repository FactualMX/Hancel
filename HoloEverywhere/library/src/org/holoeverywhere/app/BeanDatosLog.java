package org.holoeverywhere.app;

import android.content.Context;
import android.util.Log;

public class BeanDatosLog {


	public static final String marcaCel = android.os.Build.MODEL;
	public static final int versionAndroid = android.os.Build.VERSION.SDK_INT;
	public String tagLog="HANCEL";
	public String descripcion="perdida";

	String[] mapper = new String[] { "ANDROID BASE", "ANDROID BASE 1.1",
			"CUPCAKE", "CUR_DEVELOPER","DONUT", "ECLAIR", "ECLAIR_0_1", "ECLAIR_MR1", "FROYO",
			"GINGERBREAD", "GINGERBREAD_MR1", "HONEYCOMB", "HONEYCOMB_MR1",
			"HONEYCOMB_MR2", "ICE_CREAM_SANDWICH", "ICE_CREAM_SANDWICH_MR1",
			"JELLY_BEAN","JELLY_BEAN_MR1","JELLY_BEAN_MR2","KITKAT" };

	public BeanDatosLog() {


	}

	public String getMarcaCel() {
		return marcaCel;
	}

	public String getVersionAndroid() {
		 Log.d("************", versionAndroid+"");
		int index = versionAndroid ;
		String versionName = index < mapper.length ? mapper[index]
				: "UNKNOWN_VERSION"; // > JELLY_BEAN)
		return versionName;
	}

	public String getTagLog() {
		return tagLog;
	}

	public void setTagLog(String tagLog) {
		this.tagLog = tagLog;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
