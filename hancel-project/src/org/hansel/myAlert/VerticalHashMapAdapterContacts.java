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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hansel.myAlert.Utils.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class VerticalHashMapAdapterContacts extends BaseAdapter implements SectionIndexer
															, Filterable{  
    private Activity  context;
    private ArrayList<ContactInfo> lista;
    private HashMap<String, Integer> alphaIndexer;
    private String[] sections;
    List<ContactInfo> mOriginalValues;
	public boolean mBusy=false;
    
    public VerticalHashMapAdapterContacts(HashMap<String, ContactInfo> hashMap,Activity context){
         lista= new ArrayList<ContactInfo>();
         alphaIndexer = new HashMap<String, Integer>();
         orderHashMap(hashMap);
    	this.context = context;
    }
    public void  changeAdapter(HashMap<String, ContactInfo> hashmap)
    {
    	lista.clear();
    	alphaIndexer.clear();
    	orderHashMap(hashmap);
    	this.notifyDataSetChanged();
    }

    private void orderHashMap(HashMap<String, ContactInfo> map) {
    	ArrayList<String> keyList = new ArrayList<String>();
    	int contador=0;
  	  for (Iterator<String> i = Util.orderHashMap(map).iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            ContactInfo cinf =(ContactInfo) map.get(key); 
            lista.add(cinf);
            String letter  =Util.stripAccents(cinf.getDisplayName()).substring(0,1).toUpperCase();
        	keyList.add(letter);
        	if(!alphaIndexer.containsKey(letter))
        	{
        		alphaIndexer.put(letter, contador);
        	}
            contador++;
            
        }
  	 sections = new String[keyList.size()];
  	  keyList.toArray(sections);
  	}

  @Override  
  public int getCount() {  
	  return lista.size();
  }  

  @Override  
  public Object getItem(int position) {  
	  return lista.get(position);
  }  

  @Override  
  public long getItemId(int position) {  
      return 0;  
  }  

  @Override  
  public View getView(int position, View convertView, ViewGroup parent) { 
	  View v = convertView;
	   ImageView img = null;
	  TextView name =null;
	  //ImageView imgMore = null;
      if (v == null || img==null) {
          LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          v = vi.inflate(R.layout.single_search_contact, null);
    	  img =  (ImageView) v.findViewById(R.id.imgContact);
    	  name  =(TextView) v.findViewById(R.id.txtDisplayName);
    	 // imgMore = (ImageView)v.findViewById(R.id.imgMore);
      }
      final ContactInfo ci = (ContactInfo) getItem(position);
      final ImageView imgHolder = img;
    
      //genera PlaceHolder
    	 
	         new Thread(new Runnable() {
	    	    public void run() {
	    	    	
	    	    	final Bitmap photo= Contacts.loadContactPhoto(ci.getPhotoId(),context.getBaseContext(),50);
	    	    	imgHolder.post(new Runnable() {
	    	        public void run() {
	    	        	imgHolder.setImageBitmap(photo);
	    	        }
	    	      });
	    	    }
	    	  }).start();
    	 
    	  
      name.setText(ci.getDisplayName());
      return v;  
  }

@Override
public int getPositionForSection(int section) {
	 String letter = sections[section];
     return alphaIndexer.get(letter);
}

@Override
public int getSectionForPosition(int position) {
	return 0;
}

@Override
public Object[] getSections() {
	return sections;
}

@SuppressLint("DefaultLocale")
@Override
public Filter getFilter() {
	Filter filter = new Filter(){

		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
            List<ContactInfo> FilteredArrList = new ArrayList<ContactInfo>();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<ContactInfo>(lista); // saves the original data in mOriginalValues
            }

            /********
             * 
             *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
             *  else does the Filtering and returns FilteredArrList(Filtered)  
             *
             ********/
            if (constraint == null || constraint.length() == 0) {

                // set the Original result to return  
                results.count = mOriginalValues.size();
                results.values = mOriginalValues;
            } else {
                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < mOriginalValues.size(); i++) {
                	ContactInfo data = mOriginalValues.get(i);
                    if (data.getDisplayName().toLowerCase().startsWith(constraint.toString())) {
                        FilteredArrList.add(data);
                    }
                }
                // set the Filtered result to return
                results.count = FilteredArrList.size();
                results.values = FilteredArrList;
            }
            return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			lista = (ArrayList<ContactInfo>) results.values; 
			notifyDataSetChanged();			
		}
		
	};
	return filter;
}



}