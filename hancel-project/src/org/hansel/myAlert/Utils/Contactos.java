package org.hansel.myAlert.Utils;
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
import java.util.ResourceBundle.Control;

import org.hansel.myAlert.ContactInfo;
import org.hansel.myAlert.Log.Log;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;

public class Contactos {
	private Context mContext;
	public Contactos(Context cont)
	{
		mContext =cont;
	}
	 public ContactInfo getContacts(String id) {
		    // Run query
	        try {
	        	 Cursor c = mContext.getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
	     				, new String[] {ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Data.CONTACT_ID
	     				},
	     				 Contacts._ID +"=?",
	                     new String[] { id },null);
	        	 if(c.moveToFirst())
	        	 {
	        		 String displayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	        		 String contactId = c.getString(c.getColumnIndex(ContactsContract.Data.CONTACT_ID));
	        		 ContactInfo ci = new ContactInfo(displayName, id);
	        		 ci.setPhotoId(contactId);
	        		 //buscamos todos los teléfonos del contacto para mostrar
	        		 ArrayList<String> numbers =  getAllPhoneNumbers(id);
	        		 ci.setContactNumbers(numbers);
	        		 //buscamos email
	        		 return ci;
	        	 }
	        	 c.close();
	        } finally {
	             
	        }
			return null;
		  }
	 
	 
	 
	private ArrayList<String> getAllPhoneNumbers(String contactId) {
		ArrayList<String> phoneArray = new ArrayList<String>();
		 Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
		          null,  Contacts._ID  +" = "+ contactId, 
		          null, null); 
		   while (phones.moveToNext()) { 
	           String phoneNumber = phones.getString( 
	                  phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	           phoneArray.add(phoneNumber);
	        } 
		   phones.close(); 
		   return phoneArray;
	}

}
