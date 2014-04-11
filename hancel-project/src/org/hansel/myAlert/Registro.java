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
import java.util.Calendar;

import org.hancel.http.HttpUtils;
import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.SimpleCrypto;
import org.hansel.myAlert.Utils.Util;
import org.hansel.myAlert.dataBase.UsuarioDAO;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.MenuItem;

public class Registro extends org.holoeverywhere.app.Activity{

	private EditText vUsuario;
	private EditText vPassword;
	private EditText vEmail;
	private String mUsuario;
	private String mPassword;
	private String mEmail;
	private UserCreateTask mAuthTask;
	private TextView errores;
	private View mLoginFormView;
	private View mLoginStatusView;
	private String mErrores;
	private TextView mLoginStatusMessageView;
	private UsuarioDAO usuarioDAO;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro_layout);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		vUsuario =(EditText)findViewById(R.id.reg_fullname);
		vPassword =(EditText)findViewById(R.id.reg_password);
		vEmail = (EditText)findViewById(R.id.reg_email);
		errores=(TextView)findViewById(R.id.err_registro);
		Button btnCreate = (Button)findViewById(R.id.btnRegister);
		btnCreate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AttempCreate();
			}
		});
		Button btnCancelar = (Button)findViewById(R.id.btnCancelar);
		btnCancelar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Registro.this, Login.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				if(Util.isICS()) i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				finish();
			}
		});
		usuarioDAO=new UsuarioDAO(this);
		usuarioDAO.open();
		mLoginFormView = findViewById(R.id.reg_form);
		mLoginStatusView = findViewById(R.id.reg_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.reg_status_message);
		
	}

	protected void AttempCreate() {
		if(mAuthTask!=null)
		{
			return;
		}
		//obtenemos datos
		mUsuario = vUsuario.getText().toString().trim();
		mPassword = vPassword.getText().toString().trim();
		mEmail = vEmail.getText().toString().trim();
	//	mEmailContacto = vEmailContacto.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// Check for a valid user.
		if (TextUtils.isEmpty(mUsuario)) {
			vUsuario.setError(getString(R.string.error_field_required));
			focusView = vUsuario;
			cancel = true;
		}
		if (TextUtils.isEmpty(mPassword)) {
			vPassword.setError(getString(R.string.error_field_required));
			focusView = vPassword;
			cancel = true;
		}
		if (TextUtils.isEmpty(mEmail) ) {
			vEmail.setError(getString(R.string.error_field_required));
			focusView = vEmail;
			cancel = true;
		}else if(!mEmail.contains("@"))
		{
			vEmail.setError("Correo no válido");
			focusView = vEmail;
			cancel = true;
		}
		/*if (TextUtils.isEmpty(mEmailContacto) ) {
			vEmail.setError(getString(R.string.error_field_required));
			focusView = vEmailContacto;
			cancel = true;
		}else */
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			//escondemos teclado
			try
			{
				((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(vPassword.getWindowToken(), 0);
			}catch(Exception ex)
			{
				Log.v("Error al esconder teclado: "+ex.getMessage() );
			}
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			
			mAuthTask = new UserCreateTask();
			mAuthTask.execute((Void) null);
		}
		
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
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}


	@SuppressLint("DefaultLocale")
	public class UserCreateTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			//conexion a la BD para obtener Login.
			
			try
			{
				String IMEI = Util.getIMEI(getApplicationContext());
				
				String id = SimpleCrypto.md5(String.valueOf(Calendar.getInstance().getTimeInMillis()));
				JSONObject result=  HttpUtils.Register(id, mUsuario, SimpleCrypto.md5(mPassword), mEmail,"", IMEI);
				try {
					 
					 if(result.optString("resultado").equals("ok"))
					 {
						 //parseamos data del ID
						 JSONObject jObject = result.getJSONObject("descripcion");
						 int androidId = Integer.parseInt(jObject.getString("usr-id"));
						 
						 PreferenciasHancel.setDeviceId(getApplicationContext(), id);
						 PreferenciasHancel.setUserId(getApplicationContext(), androidId);
						 Util.insertNewTrackId(getApplicationContext(), 0);
						  Util.setLoginOkInPreferences(getApplicationContext(), true);
						  int idUsr=(int) usuarioDAO.Insertar( mUsuario, mPassword, mEmail);
							if(idUsr!=0){
								return true;
							}
					 }else if(result.optString("resultado").equals("error"))
					 {
						 JSONObject jObject = result.getJSONObject("descripcion");
						 mErrores = jObject.getString("usuario");
						 //buscamos el error:
						 return false;
					 }
					 
					
				} catch (Exception e) {
					Log.v("Error al parsear Json: "+ result);
					mErrores= "Error al obtener los datos";
					return false;
				}
			
				return false;
			}catch(Exception ex)
			{
				mErrores="Error al intentar la conexión";
				Log.v("Error login: "+ex.getMessage());
			}
			return false;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress(true);
			findViewById(R.id.actions1).setVisibility(View.GONE);
			mErrores ="";
			errores.setVisibility(View.GONE);
		}
		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				//mostramos el Intent
				//pasamos al siguiente paso
				Intent i = new Intent(getApplicationContext(),ConfigContactsActivity.class);
				i.putExtra("registro", true);
				/*Intent i = new Intent(getApplicationContext(),MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK 
						| Intent.FLAG_ACTIVITY_CLEAR_TOP); */
				startActivity(i);
			} else {
				errores.setVisibility(View.VISIBLE);
				findViewById(R.id.actions1).setVisibility(View.VISIBLE);
				errores.setText(mErrores);
				Toast.makeText(getApplicationContext(), "Error en login:\n"+mErrores, Toast.LENGTH_SHORT).show();
			}
		}
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			findViewById(R.id.actions1).setVisibility(View.VISIBLE);
			showProgress(false);
		}
		
	}
	
}

