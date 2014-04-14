package org.hancel.adapters;
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

import org.hancel.customclass.Ong;
import org.hansel.myAlert.R;
import org.hansel.myAlert.Utils.PreferenciasHancel;
import org.hansel.myAlert.Utils.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class OngAdapter extends ArrayAdapter<Ong>{

	private ArrayList<Ong> items;
	private String[] idValues; 
	private Context context;
	private ArrayList<String> idCurrentChecked;
	private String selectedtOng="";
	public OngAdapter(Context context, int resource,
			ArrayList<Ong> objects,String[] values) {
		super(context, resource, objects);
		if(values.length!= objects.size())
		{
			throw new IllegalArgumentException("Values/names deben contener items de la misma cantidad");
		}
		selectedtOng= PreferenciasHancel.getSelectedOng(context);
		this.context = context;
		items = objects;
		this.idValues = values;
		idCurrentChecked = new ArrayList<String>();
		
		
	}
	@Override
	public int getCount() {
		return items.size();
	}
	@Override
	public Ong getItem(int position) {
		return items.get(position);
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = li.inflate(R.layout.single_layout_ong, null);
		//obtenemos las vistas
		ImageView imgHelp = (ImageView) view.findViewById(R.id.imgHelpOng);
		TextView txtOngName = (TextView)view.findViewById(R.id.txtOngName);
		CheckBox chkSelectOng = (CheckBox) view.findViewById(R.id.chkAdd);
		//buscamos si debemos hacer check del item
		String[] val = selectedtOng.split(",");
		for (int i = 0; i < val.length; i++) {
			if(idValues[position].equals(val[i]))
			{
				chkSelectOng.setChecked(true);
				idCurrentChecked.add(idValues[position]);
			}
			
		}
		final Ong selectedItem = getItem(position);
		txtOngName.setText(selectedItem.getNombreOng());
		imgHelp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//muestra dialog de ayuda
				View alert = li.inflate(R.layout.alert_dialog_ong, null);
				TextView txtAboutOng = (TextView)alert.findViewById(R.id.txtAlertOng);
				ImageView imgOng = (ImageView)alert.findViewById(R.id.imgAlertOng);
				//obtenemos id del recurso
				int idRes = Util.getDrawable(context, "drawable/ong_"+selectedItem.getId());
				imgOng.setImageResource(idRes);
				txtAboutOng.setText(selectedItem.getOngDescripcion());
				AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
			    alertbox.setTitle(selectedItem.getNombreOng());
			    alertbox.setView(alert);
			    alertbox.setIcon(R.drawable.ic_launcher);
			    alertbox.setNeutralButton("OK",
			            new DialogInterface.OnClickListener() {

			                public void onClick(DialogInterface dialog,
			                        int arg1) {
			                	dialog.dismiss();
			                }
			            });
			  alertbox.show();
				
				
			}
		});
		chkSelectOng.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					idCurrentChecked.add(idValues[position]);
				}else
				{
					idCurrentChecked.remove(idValues[position]);
				}
			}
		});
		
		
		return view;
	}

	public ArrayList<String> getCheckedOngId()
	{
		return idCurrentChecked;
	}

}
