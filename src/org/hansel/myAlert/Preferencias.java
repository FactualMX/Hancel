package org.hansel.myAlert;

import org.holoeverywhere.preference.PreferenceScreen;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.view.MenuItem;

public class Preferencias extends org.holoeverywhere.preference.PreferenceActivity{

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences);
		final PreferenceScreen contactsPS =
                (PreferenceScreen) findPreference("pref_contacts_key");
        contactsPS.setIntent(
                new Intent(this, ConfigContactsActivity.class));
        
        final PreferenceScreen ongPref =
                (PreferenceScreen) findPreference("pref_key_select_ong");
        ongPref.setIntent(
                new Intent(this, PreferenceOng.class));
        
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

}
