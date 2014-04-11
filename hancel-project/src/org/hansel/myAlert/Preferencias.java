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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.hansel.myAlert.Utils.Util;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.preference.Preference;
import org.holoeverywhere.preference.Preference.OnPreferenceClickListener;
import org.holoeverywhere.preference.PreferenceScreen;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;

public class Preferencias extends org.holoeverywhere.preference.PreferenceActivity{

	
	
	private int RESULT_LOAD_IMAGE = 100;
	private int RESULT_LOAD_FOTO = 200;
	private AlertDialog customDialog = null; // Creamos el dialogo generico
	private String foto;
	
	
	//
	  private String selectedImagePath;
	    //ADDED
	    private String filemanagerstring;

	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences);
		final PreferenceScreen contactsPS = (PreferenceScreen) findPreference("pref_contacts_key");
        contactsPS.setIntent(new Intent(this, ConfigContactsActivity.class));
        
        final PreferenceScreen ongPref =(PreferenceScreen) findPreference("pref_key_select_ong");
        ongPref.setIntent( new Intent(this, PreferenceOng.class));
        
        final Preference prefAbout =(Preference) findPreference("pref_about");
        prefAbout.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				showDialogPre("Acerca",R.raw.cred);				
				return true;
			}
		});
        final Preference prefLegal =(Preference) findPreference("pref_legal");
        prefLegal.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				showDialogPre("Licencia",R.raw.gpl);
				return true;
			}
		});
        
        final Preference prefPic =(Preference) findPreference("pref_pic");
        prefPic.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				
				origenDeLaImagen().show();
	
				return true;
			}
		});
        
        
	}
	private void showDialogPre(String title,int res)
	{
		LayoutInflater li = (LayoutInflater)Preferencias.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
		View v =li.inflate(R.layout.pref_dialog,null,false);
		TextView tv = (TextView) v.findViewById(R.id.txtDialogPref);
		tv.setText(Util.getStringFromRaw(getApplicationContext(), res));
		AlertDialog.Builder alert= new AlertDialog.Builder(Preferencias.this)
		.setTitle(title)
		.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}) ;
		if (v.getParent() == null) {
			alert.setView(v);
		} else {
		    v = null; //set it to null
		    // now initialized yourView and its component again
		    alert.setView(v);
		}
		alert.create().show();
		
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
	 switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            break;
        }
        return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_LOAD_FOTO) {
			File file = new File(foto);
			Bitmap myBitmap = BitmapFactory.decodeFile(file
					.getAbsolutePath());
			Matrix mat = new Matrix();
			mat.postRotate(-90);
			Bitmap bMapRotate = Bitmap.createBitmap(myBitmap, 0, 0,
					myBitmap.getWidth(), myBitmap.getHeight(), mat, true);
			 try{
			        File file2 = new File(foto);
			        FileOutputStream fOut = new FileOutputStream(file2);
			        bMapRotate.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			        fOut.flush();
			        fOut.close();}
			    catch (Exception e) {
			        e.printStackTrace();
			        Log.i(null, "Save file error!");
			}
			

		}else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK	&& null != data) {
			Uri imageUri = data.getData();
			 Uri selectedImageUri = data.getData();

             //OI FILE Manager
             filemanagerstring = selectedImageUri.getPath();

             //MEDIA GALLERY
             selectedImagePath = getPath(selectedImageUri);

             //DEBUG PURPOSE - you can delete this if you want
             if(selectedImagePath!=null)
                 System.out.println(selectedImagePath+"  1");
             else System.out.println("selectedImagePath is null");
             if(filemanagerstring!=null)
                 System.out.println(filemanagerstring+"  2");
             else System.out.println("filemanagerstring is null");

             //NOW WE HAVE OUR WANTED STRING
             if(selectedImagePath!=null){
                 System.out.println("selectedImagePath is the right one for you!");
               
                	 copyFile(selectedImagePath,foto);
				
             }else{
                 System.out.println("filemanagerstring is the right one for you!");
             }
         }
			   
		//	savefile(imageUri);
			
		}

	
	
	/**
	 * Dialogo para que el usuario reporte una anomalia en el taxi o el chofer
	 * 
	 * @param Activity
	 *            (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	public Dialog origenDeLaImagen() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater().inflate(
				R.layout.dialogo_tipo_de_imagen, null);
		builder.setView(view);
		builder.setCancelable(true);
		//fuentes
((TextView) view.findViewById(R.id.dialogo_tipo_de_imagen_tv_texto)).setTextColor(getResources().getColor(R.color.rojo_logo));


		// escucha del boton aceptar
		((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnCancelar)).setOnClickListener(new OnClickListener() {


					@Override
					public void onClick(View view) {
						foto = Environment.getExternalStorageDirectory() + "/hancel/imagenhancel.jpg";
						// Camara
						  File dir = new File (Environment.getExternalStorageDirectory() + "/hancel"); 
			    	       if (!dir.exists())
			    	       {
			    	           dir.mkdirs();
			    	       }
						Intent intent = new Intent(	MediaStore.ACTION_IMAGE_CAPTURE);
						Uri output = Uri.fromFile(new File(foto));
						intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
						startActivityForResult(intent, RESULT_LOAD_FOTO); 
						customDialog.dismiss(); // cerramos el di‡logo
					}
				});

		// escucha del boton cancelar
		((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnAceptar)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						foto = Environment.getExternalStorageDirectory() + "/hancel";
						// Galeria
					/*	Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, RESULT_LOAD_IMAGE);
						
						*/
						
						 Intent intent = new Intent();
			                intent.setType("image/*");
			                intent.setAction(Intent.ACTION_GET_CONTENT);
			                startActivityForResult(Intent.createChooser(intent,
			                        "Select Picture"), RESULT_LOAD_IMAGE);
			                customDialog.dismiss(); // cerramos el di‡logo
						
						
						
						/* Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				       //     imagepath = Environment.getExternalStorageDirectory()+"/sharedresources/"+HelperFunctions.getDateTimeForFileName()+".png";
				            Uri uriImagePath = Uri.fromFile(new File(foto));
				            photoPickerIntent.setType("image/*");
				            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,uriImagePath);
				            photoPickerIntent.putExtra("outputFormat",Bitmap.CompressFormat.PNG.name());
				            photoPickerIntent.putExtra("return-data", true);
				            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE);
				            customDialog.dismiss();*/
					}

				});
		return (customDialog = builder.create());
	}
	
	//UPDATED!
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null)
        {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }
    
    private void copyFile(String inputPath, String outputPath) {
    	   InputStream in = null;
    	   OutputStream out = null;
    	   try {
    	       //create output directory if it doesn't exist
    	       File dir = new File (outputPath); 
    	       if (!dir.exists())
    	       {
    	           dir.mkdirs();
    	       }
    	       in = new FileInputStream(inputPath);        
    	       out = new FileOutputStream(outputPath + "/imagenhancel.jpg");
    	       byte[] buffer = new byte[1024];
    	       int read;
    	       while ((read = in.read(buffer)) != -1) {
    	           out.write(buffer, 0, read);
    	       }
    	       in.close();
    	       in = null;

    	           // write the output file (You have now copied the file)
    	           out.flush();
    	       out.close();
    	       out = null;        

    	   }  catch (FileNotFoundException fnfe1) {
    	       Log.e("tag", fnfe1.getMessage());
    	   }
    	           catch (Exception e) {
    	       Log.e("tag", e.getMessage());
    	   }

    	}
}
