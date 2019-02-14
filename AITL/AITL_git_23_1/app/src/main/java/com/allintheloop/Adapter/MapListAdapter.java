package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.Map.MapListData;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


/**
 * Created by nteam on 1/6/16.
 */
public class MapListAdapter extends RecyclerView.Adapter<MapListAdapter.ViewHolder> {

    Context context;
    ArrayList<MapListData.MapNewData> arrayList;

    public MapListAdapter(Context context, ArrayList<MapListData.MapNewData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_map, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        MapListData.MapNewData map = arrayList.get(position);

        holder.map_title.setText(map.getMapTitle());

        if (map.getIncludeMap().equalsIgnoreCase("1")) {
            Glide.with(context)
                    .load(map.getGoogleMapIcon())
                    .into(holder.iv_icon);

            Glide.with(context)
                    .load("http://maps.google.com/maps/api/staticmap?" +
                            "&zoom=14" +
                            "&markers=color:red%7Clabel:%7C" + map.getLat() + ", " + map.getLong() +
                            "&size=900x200" +
                            "&maptype=roadmap" +
                            "&key=AIzaSyC1vt_-YyqVhJ5rq1aD9W2luKR2cH-2Euw")
                    .into(holder.iv_map);
        } else {
            Glide.with(context)
                    .load(map.getFloorPlanIcon())
                    .into(holder.iv_icon);

            if (!(map.getImages().equalsIgnoreCase(""))) {

                String img_url = MyUrls.Imgurl + map.getImages();

                Glide.with(context)
                        .load(img_url)
                        .into(holder.iv_map);

            }
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView map_title;
        CardView card_map;
        ImageView iv_icon, iv_map;


        public ViewHolder(View itemView) {
            super(itemView);
            map_title = (TextView) itemView.findViewById(R.id.place_name);
            iv_icon = (ImageView) itemView.findViewById(R.id.map_icon);
            iv_map = (ImageView) itemView.findViewById(R.id.map_image);
            card_map = (CardView) itemView.findViewById(R.id.card_map);
        }
    }
}