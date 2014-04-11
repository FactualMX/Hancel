package org.hansel.myAlert;

import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class FinalizarRegistroActivity extends org.holoeverywhere.app.Activity implements OnClickListener{

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		//super.onBackPressed(); //no se puede regresar solo aceptar
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro_layout_04);
		PreferenciasHancel.setCurrentWizardStep(getApplicationContext(), Util.REGISTRO_PASO_4);
		Button btnFinalizar = (Button)findViewById(R.id.btnFinalizarRegistro);
		Button cancelButton = (Button)findViewById(R.id.btnCancelarRegistro);
		cancelButton.setOnClickListener(this);
		btnFinalizar.setOnClickListener(this);
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnFinalizarRegistro:
			Intent i = new Intent(getApplicationContext(), MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
			if(Util.isICS()) i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); 
			startActivity(i);
			break;
		case R.id.btnCancelarRegistro:
			Intent cancel = new Intent(getApplicationContext(), Registro.class);
			cancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			if(Util.isICS()) cancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(cancel);
			break;
		default:
			break;
		}
		
	}

}
