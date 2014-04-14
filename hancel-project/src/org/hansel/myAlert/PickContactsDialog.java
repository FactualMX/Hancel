package org.hansel.myAlert;
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
