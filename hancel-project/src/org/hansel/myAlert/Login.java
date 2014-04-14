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

import org.hancel.exceptions.NoInternetException;
import org.hancel.http.HttpUtils;
import org.hansel.myAlert.Log.Log;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.SimpleCrypto;
import org.hansel.myAlert.Utils.Util;
import org.hansel.myAlert.WelcomeInfo.ScreenSlidePageAdapter;
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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Login extends org.holoeverywhere.app.Activity {

	private String mUser;
	private String mPasswd;
	private EditText user;
	private EditText passwd;
	private UserLoginTask mAuthTask;
	private TextView errores;
	private View mLoginFormView;
	private View mLoginStatusView;
	private String mErrores;
	private TextView mLoginStatusMessageView;
	private UsuarioDAO usuarioDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		
		//buscamos si quedo en un paso del registro y no termino
		int step= PreferenciasHancel.getCurrentWizardStep(getApplicationContext());
		switch (step) {
		case Util.REGISTRO_PASO_2:
			Intent step2 = new Intent(getApplicationContext(),ConfigContactsActivity.class);
			step2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK 
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			step2.putExtra("registro", true);
			startActivity(step2);
			finish();
			return;
		case Util.REGISTRO_PASO_3:
			Intent step3 = new Intent(getApplicationContext(),PreferenceOng.class);
			step3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK 
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			step3.putExtra("registro", true);
			startActivity(step3);
			finish();
			return;
		default:
			break;
		}
		if(PreferenciasHancel.getLoginOk(getApplicationContext()))
		{
			Intent i = new Intent(getApplicationContext(),MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK 
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			finish();
		}
		
		user = (EditText)findViewById(R.id.txtUser);
		passwd = (EditText) findViewById(R.id.txtPassword);
		Button btnLogin = (Button)findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AttempLogin();
			}
		});
		
		TextView txt = (TextView)findViewById(R.id.link_to_register);
		txt.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), Registro.class);
				startActivity(i);
			}
		});
		
		errores =(TextView)findViewById(R.id.tvError);
		mLoginFormView = findViewById(R.id.login_form1);
		mLoginStatusView = findViewById(R.id.login_status1);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message1);

		
		ViewPager mpager = (ViewPager) findViewById(R.id.pager);
		PagerAdapter mpagerAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
		mpager.setAdapter(mpagerAdapter);
		CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.pagerIndicator);
		indicator.setViewPager(mpager);
		
		
		
	}
	
	private void AttempLogin()
	{
		if(mAuthTask!=null) //sal , si la tarea aun esta ejecutandose
		{
			return;
		}
		user.setError(null);
		passwd.setError(null);
		//obtenemos los valores antes de hacer log in
		mUser = user.getText().toString();
		mPasswd = passwd.getText().toString();
		boolean cancel = false;
		View focusView = null;
		
		// Check for a valid user.
		if (TextUtils.isEmpty(mUser)) {
			user.setError(getString(R.string.error_field_required));
			focusView = user;
			cancel = true;
		} 
		// Check for a valid password.
		if (TextUtils.isEmpty(mPasswd)) {
			passwd.setError(getString(R.string.error_field_required));
			focusView = passwd;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			//escondemos teclado
			try
			{
				((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(passwd.getWindowToken(), 0);
			}catch(Exception ex)
			{
				Log.v("Error al esconder teclado: "+ex.getMessage() );
			}
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}

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


	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			errores.setVisibility(View.GONE);
			mErrores="";
			
		}
		@SuppressLint("DefaultLocale")
		@Override
		protected Boolean doInBackground(Void... arg0) {
			//conexion a la BD para obtener Login.						
			try
			{
				String id_device = String.valueOf(Calendar.getInstance().getTimeInMillis());
				String PassMd5= SimpleCrypto.md5(mPasswd);
				id_device = SimpleCrypto.md5(id_device);
				//obtenemos Json para login válido
				JSONObject result =  HttpUtils.Login(mUser, PassMd5, id_device);
				 try {
					 if(result.optString("resultado").equals("ok"))
					 {
						 //parseamos data del ID
						 JSONObject jObject = result.getJSONObject("descripcion");
						 int androidId = Integer.parseInt(jObject.getString("usr-id"));
						 int trackId = Integer.valueOf(jObject.getString("id-tracking"));
						 PreferenciasHancel.setDeviceId(getApplicationContext(), id_device);
						 PreferenciasHancel.setUserId(getApplicationContext(), androidId);
						 Util.insertNewTrackId(getApplicationContext(), trackId);
						  Util.setLoginOkInPreferences(getApplicationContext(), true);
						//abre Base de datos
							usuarioDao=new UsuarioDAO(Login.this);
							usuarioDao.open();
						  usuarioDao.Insertar( mUser, mPasswd, id_device);
						  usuarioDao.close();
					 }else
					 {
						 mErrores=result.optString("descripcion");
						 return false;
					 }
					 
					
				} catch (Exception e) {
					Log.v("Error al parsear Json: "+ result);
					return false;
				}
				//encriptamos password
				/*String hash = SimpleCrypto.md5(SimpleCrypto.MD5_KEY);
				if(hash.length()>0)
				{
					String encrypted= SimpleCrypto.encrypt(mPasswd, hash);
					if(encrypted.length()>0)
					{
						mPasswd = encrypted;
					}
				}*/
				
			//	usuarioDao.Insertar( mUser, mPasswd, IMEI);
				
			        return true;
						
			}catch(NoInternetException ni)
			{
				mErrores = "Error al conectarse al servidor";
				Log.v("Error login: "+ni.getMessage());
			}
			catch(Exception ex)
			{
				
				mErrores = "Error al procear la información";
				Log.v("Error login: "+ex.getMessage());
			}
			
			return false;
		}
		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);
			if (success) {
				//mostramos pantalla principal en caso de éxito
				Intent i = new Intent(getApplicationContext(),MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK 
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				
			} else {
				errores.setVisibility(View.VISIBLE);
				errores.setText(mErrores);
			}
		}
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
		
	}
}
