package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.DataModel;
import com.allintheloop.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 19/7/17.
 */

public class ListAdapter extends ArrayAdapter<DataModel> implements Filterable {
    private ArrayList<DataModel> dataSet;
    private ArrayList<DataModel> dataSetfilter;
    Context mContext;

    public ListAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;
        this.dataSetfilter = new ArrayList<>();
        this.dataSetfilter.addAll(data);

    }

    private static class ViewHolder {
        TextView txtName, txt_standNumber;
        ImageView img_user;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.txt_standNumber = (TextView) convertView.findViewById(R.id.txt_standNumber);
            viewHolder.img_user = (ImageView) convertView.findViewById(R.id.img_user);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtName.setText(dataModel.getComapany_name());
        viewHolder.txt_standNumber.setVisibility(View.VISIBLE);
        viewHolder.txt_standNumber.setText(dataModel.getStand_number());
//        Log.d("AITL ","http://www.allintheloop.net/assets/user_files/"+dataModel.getCompany_logo());
        Glide.with(mContext).load("http://www.allintheloop.net/assets/user_files/" + dataModel.getCompany_logo()).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                viewHolder.img_user.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                viewHolder.img_user.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(new BitmapImageViewTarget(viewHolder.img_user) {
            @Override
            protected void setResource(Bitmap resource) {
                viewHolder.img_user.setImageBitmap(resource);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    public String filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());

        final String filterString = charText.toString().toLowerCase();
        Log.d("AITL DATA", "" + dataSet.size());
        for (DataModel qaModuleDataObj : dataSet) {
            String title = qaModuleDataObj.getComapany_name().toLowerCase();
            String standNumber = qaModuleDataObj.getStand_number().toLowerCase();
            if (title.contains(filterString) || standNumber.contains(filterString)) {
                Log.d("AITL Adapter", "File Latitude :" + qaModuleDataObj.getLatitude() + "Longitude" + qaModuleDataObj.getLongitude());
                Log.d("AITL Adapter", "Name :" + filterString);

                return "" + qaModuleDataObj.getLatitude() + "," + qaModuleDataObj.getLongitude();

            }
        }
        return "";

    }


}
