package org.hansel.myAlert;

import java.util.ArrayList;
import java.util.Collections;

import org.hancel.adapters.OngAdapter;
import org.hancel.customclass.Ong;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;


public class PreferenceOng extends org.holoeverywhere.app.Activity{

	private OngAdapter adapter;
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ArrayList<String> id  = adapter.getCheckedOngId();
		Collections.sort(id);
		StringBuilder sb = new StringBuilder();
		for (String idValue : id) {
			sb.append(idValue+",");
		}
		//salvamos la ong
		PreferenciasHancel.setSelectedOng(getApplicationContext(), sb.toString());
	}
	private boolean isFromRegister=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_ong_preferences);
		//obtenemos Arrays
		String[] ongNames = getResources().getStringArray(R.array.lista_ongs);
		String[] ongId= getResources().getStringArray(R.array.lista_ongs_id);
		adapter = new OngAdapter(getApplicationContext(), R.layout.single_layout_ong, getOngByArray(ongNames), ongId);
		ListView list = (ListView)findViewById(R.id.listViewOng);
		list.setAdapter(adapter);
		Button btnAcepta = (Button)findViewById(R.id.btnAceptaOng);
		Button btnCancela = (Button)findViewById(R.id.btnCancelaOng);
		isFromRegister = getIntent().getExtras()==null? false : getIntent().getExtras().getBoolean("registro",false);
		if(isFromRegister)
		{
			PreferenciasHancel.setCurrentWizardStep(getApplicationContext(), Util.REGISTRO_PASO_3);
			findViewById(R.id.stepWrapperONG).setVisibility(View.VISIBLE);
			findViewById(R.id.ongButtonWrapper).setVisibility(View.VISIBLE);
			
		}
		btnAcepta.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("InlinedApi")
			@Override
			public void onClick(View v) {
				
				if(isFromRegister)
				{
					
					Intent next = new Intent(getApplicationContext(), FinalizarRegistroActivity.class);
					next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
					if(Util.isICS()) next.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(next);
				}
				
				//Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
			}
		});
		btnCancela.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent reg = new Intent(getApplicationContext(), Registro.class);
				reg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				if(Util.isICS()) reg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(reg);
				finish();
			}
		});
		
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if(!isFromRegister)
			super.onBackPressed();
	}
	private ArrayList<Ong> getOngByArray(String[] array)
	{
		String[] ongDescription = getResources().getStringArray(R.array.lista_ongs_descripcion);
		
		ArrayList<Ong> ongList = new ArrayList<Ong>();
		for (int i = 0; i < array.length; i++) {
			ongList.add(new Ong(String.valueOf(i+1),array[i],ongDescription[i]));
		}
		return ongList;
	}


}
