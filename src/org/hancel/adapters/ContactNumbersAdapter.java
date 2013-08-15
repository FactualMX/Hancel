package org.hancel.adapters;

import java.util.List;

import org.hansel.myAlert.R;
import org.hansel.myAlert.Log.Log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ContactNumbersAdapter extends ArrayAdapter<String>{

	private Context context;
	private List<String> telefonos;
	private boolean checked[];
	public ContactNumbersAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.telefonos = objects;
		checked = new boolean[objects.size()];
		
	}
	@Override
	public int getCount() {
		return telefonos.size();
	}
	@Override
	public String getItem(int position) {
		return telefonos.get(position);
	}
	public boolean[] getCheckedPhones()
	{
		return checked;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater li =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vi = li.inflate(R.layout.single_phone_list, null);
		TextView num = (TextView) vi.findViewById(R.id.txtNumero);
		CheckBox chk = (CheckBox)vi.findViewById(R.id.chkSeleccionaNumero);
		chk.setChecked(checked[position]);
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.v("position checked "+position);
				checked[position] = isChecked;
			}
		});
		String numero = getItem(position);
		num.setText(numero);
		return vi;
	}


}
