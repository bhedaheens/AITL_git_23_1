package com.allintheloop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.allintheloop.Bean.CountryList;
import com.allintheloop.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by nteam on 26/5/16.
 */
public class CountryAdapter extends ArrayAdapter<CountryList> {
    Context context;
    List<CountryList> countryList;
    private ArrayList<CountryList> arraylist;
    LayoutInflater inflater;
    int position;

    public CountryAdapter(Context context, int resource, List<CountryList> countryList) {
        super(context, resource, countryList);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.countryList = countryList;
        this.arraylist = new ArrayList<CountryList>();
        this.arraylist.addAll(countryList);

    }

    private class ViewHolder {
        TextView txtCountryName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        this.position = position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_countrylist, null);
            holder.txtCountryName = (TextView) convertView.findViewById(R.id.textName);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtCountryName.setText(countryList.get(position).getName());
        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        CountryList coutrnObj = arraylist.get(position);
        if (coutrnObj.getTag().equals("country")) {
            countryList.clear();
            if (charText.length() == 0) {
                countryList.addAll(arraylist);

            } else {

                for (CountryList wp : arraylist) {
                    if (wp.getName().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        countryList.add(wp);
                    }
                }
            }
        } else if (coutrnObj.getTag().equals("state")) {
            Log.d("StateTag", coutrnObj.getTag());
            countryList.clear();
            if (charText.length() == 0) {
                countryList.addAll(arraylist);
            } else {
                for (CountryList wp : arraylist) {
                    if (wp.getName().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        countryList.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
