package org.hansel.myAlert;

import java.util.ArrayList;

import org.hancel.adapters.ContactNumbersAdapter;
import org.hansel.myAlert.Utils.Contactos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class PickContactsDialog extends org.holoeverywhere.app.Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_pick_number);
		ListView lv = (ListView) findViewById(R.id.listViewNumeros);
		final String contactId = getIntent().getExtras().getString("id");
		Contactos c = new Contactos(getApplicationContext());
		ContactInfo ci =  c.getContacts(contactId);
		this.setTitle(ci.getDisplayName());
		ArrayList<String> arr = ci.getPhoneNumbers();
		final ContactNumbersAdapter adapter = new ContactNumbersAdapter(getApplicationContext()
				, R.layout.single_phone_list, arr);
		lv.setAdapter(adapter);
		Button btnOk = (Button) findViewById(R.id.btnAceptaNumeros);
		Button btnCancela = (Button) findViewById(R.id.btnCancelarNumero);
		
		btnOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			//obtenemos los "seleccionados"
				ArrayList<String> numeros= new ArrayList<String>();
				boolean checked[] =adapter.getCheckedPhones();
				for (int i = 0; i <checked .length; i++) {
					if(checked[i])
					{
						numeros.add(adapter.getItem(i));
					}
				}
				if(numeros.size()==0)
				{
					Toast.makeText(getApplicationContext(), "Necesita seleccionar al menos un Número", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent i = new Intent(getApplicationContext(), ConfigContactsActivity.class);
				i.putExtra("phones", numeros);
				i.putExtra("id", contactId);
				setResult(RESULT_OK,i);
				finish();
				
			}
		});
		btnCancela.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
	}


}
