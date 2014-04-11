package org.hansel.myAlert;

import java.util.ArrayList;
import java.util.HashMap;

import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.Contactos;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;
import org.hansel.myAlert.dataBase.ContactoDAO;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ConfigContactsActivity extends org.holoeverywhere.app.Activity{
	private static final int REQ_CODE_CHOOSE_CONTACT = 0;
	private static final int REQ_CODE_CHOOSE_NUMBERS = 1;
	private ListView lvContactos;
	private boolean isFromRegister=false;
	private Contactos c;
	private ContactoDAO contactoDAO; 
	private HashMap<String, ContactInfo> contactos;
	private ArrayList<String> users;
	private VerticalHashMapAdapterContacts mAdapter;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_chooser);
        c= new Contactos(getApplicationContext());
        contactoDAO=new ContactoDAO(this);
        contactoDAO.open();
        
        
        contactos = creaAdapter();
        mAdapter= new VerticalHashMapAdapterContacts(contactos, this);
        lvContactos = (ListView)findViewById(R.id.lvContactos);
        lvContactos.setAdapter(mAdapter);
        View buttonWrapper = findViewById(R.id.btnWrapperContacts);
        if(getIntent().getExtras()!=null && getIntent().getExtras().getBoolean("registro",false) )
        { // si viene de registro mostramos botones
        	isFromRegister=true;
        	//guardamos el paso en que se quedo
        	PreferenciasHancel.setCurrentWizardStep(ConfigContactsActivity.this, Util.REGISTRO_PASO_2);
        	buttonWrapper.setVisibility(View.VISIBLE);
        	findViewById(R.id.stepWrapperContacts).setVisibility(View.VISIBLE);
        	Button aceptar = (Button)findViewById(R.id.btnAceptarContactos);
        	aceptar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//pasamos al ultimo paso de las ONG
					Intent i = new Intent(getApplicationContext(), PreferenceOng.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("registro", true);
					startActivity(i);
				}
			});
        	Button cancButton= (Button)findViewById(R.id.btnCancelarContactos);
        	cancButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent reg = new Intent(getApplicationContext(), Registro.class);
					reg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					if(Util.isICS()) reg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(reg);
					
				}
			});
        	
        }else
        {
        	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        lvContactos.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ContactInfo _contInf = (ContactInfo) mAdapter.getItem(arg2);
				//delete base de datos sql
				String contactId = _contInf.get_id();
				contactoDAO.deleteTurno(Integer.valueOf(contactId));
				Toast.makeText(getApplicationContext(), "Removiendo: "+contactId, Toast.LENGTH_SHORT).show();
				//remueve contacto basado en el ID contactId usar try, catch en caso de error
				
				updateListView();
				return true;
			}
		});
  
	}
	   /* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if(!isFromRegister) // solo permite "atras" si no viene de registro
			super.onBackPressed();
	}
	@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	 	MenuInflater infla = getSupportMenuInflater();
			infla.inflate(R.menu.menu_contactos, menu);
	     return true;
	    }
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
	        case android.R.id.home:
	            finish();
	            break;
	        case R.id.CMD_ADD_CONTACT:
	        startContactPicker();
	            break;
	        }
	        return super.onOptionsItemSelected(item);
		 
	 }
	 private void startContactPicker() {
		// startActivityForResult(
	      //          new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQ_CODE_CHOOSE_CONTACT);
	        startActivityForResult(
	                new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI), REQ_CODE_CHOOSE_CONTACT);
	    }
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        switch (requestCode) {
	        case REQ_CODE_CHOOSE_CONTACT:
	            if (resultCode == RESULT_OK) { // Success, contact chosen
		                // final String lookupKey = segments.get(segments.size() - 2);
	            	final String contactId = data.getData().getLastPathSegment();
	                // final String lookupKey = segments.get(segments.size() - 2);
	                //final String contactId = segments.get(segments.size() - 1);
	                ContactInfo ci = c.getContacts(contactId);
	                if(ci!=null)
	                {
	                	Log.v(ci.get_id());
	                	if(contactoDAO.getList(Integer.valueOf(contactId))==0 && ci.getPhoneNumbers().size()>1)
	                	{
		                	launchPicker(contactId);
	                	}else if(contactoDAO.getList(Integer.valueOf(contactId))==0 && ci.getPhoneNumbers().size()==1)
	                	{
	                		//si solo tiene un contacto no es necesario preguntar por que números agregar
	                		InsertaBD(ci.getPhoneNumbers(),contactId,ci.getPhotoId());
	                	}else
	                	{
	                		Toast.makeText(getApplicationContext(), "Contacto Existente", Toast.LENGTH_SHORT).show();
	                	}
	                }else
	                {
	                	Toast.makeText(getApplicationContext(), "No contiene Números telefónicos", Toast.LENGTH_SHORT).show();
	                	//no inserta, no existen numeros telefónicos
	                }
	               // updateListView();
	            }
	            break;
	        case REQ_CODE_CHOOSE_NUMBERS:
	        	//actualizamos BD con los numeros seleccionados
	        	if(resultCode==RESULT_OK)
	        	{
	        		String contactoId = data.getExtras().getString("id");
	        		ArrayList<String> phoneNumbers = data.getExtras().getStringArrayList("phones");
	        		//guardar los teléfonos en la BD
	        		InsertaBD(phoneNumbers, contactoId,contactoId);
	        	}
	        	
	        	break;
	        }
	    }
	private void InsertaBD(ArrayList<String> phoneNumbers,String contactId,String photID) {
		String csv ="";
		try { //en caso que exista algún error
			for (int i = 0; i < phoneNumbers.size(); i++) {
				csv +=  phoneNumbers.get(i)+",";
			}
			csv = csv.length()>0?  csv.substring(0,csv.length()-1) :"" ; //quitamos última "coma,"
			//csv =phoneNumbers.toString().substring(1, phoneNumbers.toString().length() - 2).replace(", ", ",");
		} catch (Exception e) {
			//usamos for para concatenar 
			for (int i = 0; i < phoneNumbers.size(); i++) {
				csv +=  phoneNumbers.get(i)+",";
				
			}
			csv = csv.length()>0?  csv.substring(0,csv.length()-1) :"" ; //quitamos última "coma,"
		}
		//guardamos email
		//String lista="";
		/*if(emailList!=null && emailList.size()>0)
		{
			for (int i = 0; i < emailList.size(); i++) {
				lista +=  emailList.get(i)+"|";
				
			}
		}*/
		
		contactoDAO.Insertar(Integer.parseInt(contactId), csv,photID);
		mAdapter.changeAdapter(creaAdapter());
		
	}
	private void launchPicker(String contactId) {
		Log.v("Lanzando picker");
		Intent cont = new Intent(getApplicationContext(),PickContactsDialog.class);
    	cont.putExtra("id", contactId);
    	startActivityForResult(cont, REQ_CODE_CHOOSE_NUMBERS);		
	}
	private void updateListView() {
		mAdapter.changeAdapter(creaAdapter());
	}
	private HashMap<String, ContactInfo> creaAdapter()
	{
		HashMap<String, ContactInfo> con= new HashMap<String, ContactInfo>();
		ArrayList<String> us = getUsersSqlite();
		for (String _usr : us) {
			ContactInfo ci = c.getContacts(_usr);
			if(ci!=null)
			{
				con.put(_usr, ci);
			}
		}
		return con;
	}


	private ArrayList<String> getUsersSqlite()
	{
		
		users= new ArrayList<String>();
		users.clear();
		Cursor c = contactoDAO.getList();
		if(c!=null)
		{
		  while(c.moveToNext()){
		   int id = c.getInt(1);
		   users.add(Integer.toString(id));
		  }
		} 
		//obtienes cursor y hace foreach y le haces put al ID de usuario
		return users;
		
	}

}
