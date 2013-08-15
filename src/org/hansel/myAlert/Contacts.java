package org.hansel.myAlert;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.Util;


import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;

public class Contacts {
	private Context context;
	private String[] groupsid;
	private HashMap<String, ContactInfo> contactInfo;
	public static final int CONTACT_PHOTO_MAXSIZE = 1024;
	public Contacts(Context con)
	{
		context=con;
	}
	  public static Bitmap loadContactPhoto(String id,Context mContext,int size)
	  {
		  Bitmap t=null;
		  try
		  {
			  final Uri ur = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id);
				 t = Contacts.getPersonPhoto(mContext, ur, size, id);
				if(t==null)
				{
					t = getDefaultContactBitmap(mContext, size);
				    
				}
		  }
			catch (Exception e) {
				Log.v("Error loading picture, using default:"+ e.getMessage());
				t = getDefaultContactBitmap(mContext, size);
			}
			return t;
	  }
	  public static Bitmap getDefaultContactBitmap(Context mContext,int size)
	  {
		Bitmap t=null;
		  Drawable x = mContext.getResources().getDrawable(R.drawable.ic_contact_picture);
			Bitmap d = ((BitmapDrawable)x).getBitmap();
		     t= Bitmap.createScaledBitmap(d, size, size, false);
		     return t;
	  }  
		  
	  public  Bitmap loadContactPhoto(ContentResolver cr, long  id) {
		    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
		    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
		    if (input == null) {
		        return null;
		    }
		    Bitmap photo = BitmapFactory.decodeStream(input);
			return photo;
		    
		}
	
	 private Cursor getContacts() {
		    // Run query
		    Uri uri = ContactsContract.Contacts.CONTENT_URI;
		    String[] projection = new String[] { ContactsContract.Contacts._ID,
		        ContactsContract.Contacts.DISPLAY_NAME };
		    String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
		        + ("1") + "' AND "+ ContactsContract.Contacts.HAS_PHONE_NUMBER +"='" +("1")+"'";
		    String sortOrder =" "+  ContactsContract.Contacts.DISPLAY_NAME +" ASC";

		    CursorLoader query = new CursorLoader(context,uri, projection,
		    		selection, null,
		        sortOrder);
		    return query.loadInBackground();
		  }
	 private Cursor getContactsFilter(String _where) {
		    // Run query
		    Uri uri = ContactsContract.Contacts.CONTENT_URI;
		    String[] projection = new String[] { ContactsContract.Contacts._ID,
		        ContactsContract.Contacts.DISPLAY_NAME };
		    String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
		        + ("1") + "' AND "+ ContactsContract.Contacts.HAS_PHONE_NUMBER +"='" +("1")+"' AND "
		        +ContactsContract.Contacts.DISPLAY_NAME +" LIKE '%"+_where+"%'";
		    
		    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +" COLLATE LOCALIZED ASC";

		    CursorLoader query = new CursorLoader(context,uri, projection,
		    		selection, null,
		        sortOrder);
		    return query.loadInBackground();
		  }
	 
	 public HashMap<String, ContactInfo> getContactSearch(String _where)
	 {
		 contactInfo = new HashMap<String, ContactInfo>();
		 Cursor cursor = null ;
		 if(_where.length()==0)
		 {
			cursor = getContacts();
		 }else
		 {
			 cursor = getContactsFilter(_where);
		 }
		 while (cursor.moveToNext()) {
			  String id = cursor.getString(cursor
			          .getColumnIndex(ContactsContract.Contacts._ID));
			  if(!contactInfo.containsKey(id))
			  {
				  
				  String displayName = cursor.getString(cursor
				          .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				//obtenemos el telefono principal
				 String number =getPrimaryNumber(id);
				  
				  ContactInfo ci = new ContactInfo(displayName,number,id);
				  
				  contactInfo.put(id,ci);
				  //arr.add(displayName);
			  }
		    }
		 return contactInfo;
	 }
	 public HashMap<String,ContactInfo> getContactsPerGroup(String groupId)
	 {
		 contactInfo = new HashMap<String, ContactInfo>();
		 contactInfo = new HashMap<String, ContactInfo>();
		 Cursor c = context.getApplicationContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI
				, new String[] {
				ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID, 
				ContactsContract.RawContacts.CONTACT_ID },
				ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = ?" + " And " +
							ContactsContract.Contacts.HAS_PHONE_NUMBER +"='" +("1")+"'",
                new String[] { groupId }, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
		 while(c.moveToNext())
		 {
			 
			 String displayName = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			 String id = c.getString(c.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
			 if(!contactInfo.containsKey(id))
			 {
				 String number =getPrimaryNumber(id);
				 ContactInfo ci = new ContactInfo(displayName,number,id);
				 contactInfo.put(id, ci);
				// s.add(displayName);
			 }
		 
		 }
		 
		return contactInfo;
		 //llenamos los id de los grupos
	 }
	 
	 
	
	 public int getGroupId(int pos)
	 {
		 if(pos==0)
		 {
			 return 0;
		 }
		 else
		 {
			 return Integer.valueOf(groupsid[pos-1]) ;
		 }
	 }
	
	 
	 public HashMap<String,ContactInfo> getContactList()
	 {
		// ArrayList<String> arr = new  ArrayList<String>();
		 contactInfo = new HashMap<String, ContactInfo>();
		 Cursor cursor = getContacts();
		 while (cursor.moveToNext()) {
			  String id = cursor.getString(cursor
			          .getColumnIndex(ContactsContract.Contacts._ID));
			  if(!contactInfo.containsKey(id))
			  {
				  
				  String displayName = cursor.getString(cursor
				          .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
				  String number =getPrimaryNumber(id);
				  ContactInfo ci = new ContactInfo(displayName,number,id);
				  contactInfo.put(id,ci);
				  //arr.add(displayName);
			  }
		      
		    }
		return contactInfo;
		 
	 }
	 private Cursor getPhoneNumbersFromContactCursor(String ContactId)
	 {
		 String[] whereArgs = new String[] { ContactId };
		 Cursor cursor = context.getContentResolver().query(
                 ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                 null,
                 ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", 
                 whereArgs, 
                 null);
		 return cursor;
	 }
	 	
	 public ArrayList<String> getPhoneNumbersFromContact(String contactId)
	 {
		 ArrayList<String> data = new ArrayList<String>();
		 ArrayList<String> duplicates = new ArrayList<String>();
		 Cursor cursor = getPhoneNumbersFromContactCursor(contactId);
		 if(cursor!=null)
		 {
			 while (cursor.moveToNext()) {
				 String phone = cursor.getString(cursor
				          .getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
				// int x = cursor.getInt(cursor
				  //        .getColumnIndex( ContactsContract.CommonDataKinds.Phone.TYPE));
				 String phoneTmp = phone;
				 phoneTmp = phone.replace("-", "").replace("(", "").replace(")","").trim();
	
				 if(!duplicates.contains(phoneTmp))
				 {
					 duplicates.add(phoneTmp);
					 data.add(phone);
				 }
				 
			 }
		 }
		return data;
		 
	 }
	 public  String getPrimaryNumber(String ContactId)
	 {
		 String phone="";
		 Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				 null, ContactsContract.CommonDataKinds.Phone.IS_SUPER_PRIMARY + "= ? AND " +ContactsContract.CommonDataKinds.Phone.CONTACT_ID +"=?" , 
				 new String[] { "0",ContactId},
				 null);
		 if(cursor.moveToNext())
		 {
			 
			 phone= cursor.getString(cursor
			          .getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
		 }else
		 {
			 cursor = getPhoneNumbersFromContactCursor(ContactId);
			 if(cursor!=null)
			 {
				 if(cursor.moveToNext())
				 {
					 phone = cursor.getString(cursor
					          .getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
				 }
			 }
		 }
		 return phone;
		 
	 }
	 
	   public static Bitmap getPersonPhoto(Context context, final Uri contactUri,
	    		final int thumbSize,final String id) {

	        if (contactUri == null || id ==null) {
	            return null;
	        }
	       // Log.v("person photo");
	        // First let's just check the dimensions of the contact photo
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        options.inTempStorage = new byte[16*1024]; 
	        
	        // The height and width are stored in 'options' but the photo itself is not loaded
	        loadContactPhoto(context, contactUri, options);
	        //Log.v("resize");
	        // Raw height and width of contact photo
	        final int height = options.outHeight;
	        final int width = options.outWidth;

	        //if (BuildConfig.DEBUG)
	          //  Log.v("Contact photo size = " + height + "x" + width);

	        // If photo is too large or not found get out
	        if (height > CONTACT_PHOTO_MAXSIZE || width > CONTACT_PHOTO_MAXSIZE ||
	                width == 0 || height == 0) {
	            return null;
	        }

	        // This time we're going to do it for real
	        options.inJustDecodeBounds = false;
	        options.inTempStorage = new byte[16*1024]; 
	        int newHeight = thumbSize;
	        int newWidth = thumbSize;

	        // If we have an abnormal photo size that's larger than thumbsize then sample it down
	        boolean sampleDown = false;

	        if (height > thumbSize || width > thumbSize) {
	            sampleDown = true;
	        }

	        // If the dimensions are not the same then calculate new scaled dimenions
	        if (height < width) {
	            if (sampleDown) {
	                options.inSampleSize = Math.round(height / thumbSize);
	            }
	            newHeight = Math.round(thumbSize * height / width);
	        } else {
	            if (sampleDown) {
	                options.inSampleSize = Math.round(width / thumbSize);
	            }
	            newWidth = Math.round(thumbSize * width / height);
	        }

	        // Fetch the real contact photo (sampled down if needed)
	        Bitmap contactBitmap = null;
	        try {
	            contactBitmap = loadContactPhoto(context, contactUri, options);
	        } catch (OutOfMemoryError e) {
	            Log.v("Out of memory when loading contact photo");
	        }

	        // Not found or error, get out
	        if (contactBitmap == null)
	            return null;
	       // Log.v("Creating Bitmap size:"+newWidth);
	        // Bitmap scaled to new height and width
	        return Bitmap.createScaledBitmap(contactBitmap, newWidth, newHeight, true);
	    }
	   @SuppressLint("NewApi")
	public static Bitmap loadContactPhoto(Context context, Uri contactUri,
	    		BitmapFactory.Options options) {
		  // Log.v("LoadcontactPhoto");
	        if (contactUri == null) {
	            return null;
	        }

	         InputStream stream = null;
	        if (Util.isICS()) {
	        	//Log.v("uri: "+contactUri.toString());
	        	try
	        	{
	            stream = android.provider.ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
	                    contactUri, true);
	        	}catch(Exception ex)
	        	{
	        		Log.v(ex.getMessage());
	        	}
	        } else {
	        	
	            stream =  android.provider.ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
	                    contactUri);
	        }
	        return stream != null ? BitmapFactory.decodeStream(stream, null, options) : null;
	    }


}
