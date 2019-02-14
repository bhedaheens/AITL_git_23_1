package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.DataModel;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * Created by nteam on 7/9/17.
 */

public class MapDailogStandNumberAdapter extends RecyclerView.Adapter<MapDailogStandNumberAdapter.ViewHolder> implements Filterable {

    ArrayList<DataModel> dataModelArrayList;
    ArrayList<DataModel> tmp_dataModelArrayList;
    Context context;
    RecyclerViewClickListener recyclerViewClickListener;
    int tmp_position = 0;

    public MapDailogStandNumberAdapter(ArrayList<DataModel> dataModelArrayList, Context context, RecyclerViewClickListener recyclerViewClickListener) {
        this.dataModelArrayList = dataModelArrayList;
        tmp_dataModelArrayList = new ArrayList<>();
        tmp_dataModelArrayList.addAll(dataModelArrayList);
        this.context = context;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.standlist_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        DataModel dataModelobj = tmp_dataModelArrayList.get(position);
        holder.textName.setText(dataModelobj.getComapany_name() + " " + dataModelobj.getStand_number());

        holder.cardHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClickListener.onItemClick(v, position, tmp_dataModelArrayList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return tmp_dataModelArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new StandNumberFilter(this, dataModelArrayList, tmp_position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout cardHeader;
        TextView textName;

        public ViewHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textName);
            cardHeader = (LinearLayout) itemView.findViewById(R.id.cardHeader);
        }
    }


    private static class StandNumberFilter extends Filter {
        MapDailogStandNumberAdapter mapDailogStandNumberAdapter;
        ArrayList<DataModel> dataModelArrayList;
        ArrayList<DataModel> tmp_dataModelArrayList;
        int postiion;

        public StandNumberFilter(MapDailogStandNumberAdapter mapDailogStandNumberAdapter, ArrayList<DataModel> dataModelArrayList, int postiion) {
            this.mapDailogStandNumberAdapter = mapDailogStandNumberAdapter;
            this.dataModelArrayList = dataModelArrayList;
            tmp_dataModelArrayList = new ArrayList<>();
            this.postiion = postiion;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            FilterResults results = new FilterResults();
            DataModel modelObj = dataModelArrayList.get(postiion);
            if (charSequence.length() <= 0) {
                tmp_dataModelArrayList.addAll(dataModelArrayList);
            } else {
                String filterString = charSequence.toString().toLowerCase();
                for (DataModel portalObj1 : dataModelArrayList) {
                    String title = portalObj1.getComapany_name().toLowerCase() + " " + portalObj1.getStand_number().toLowerCase();
                    if (title.contains(filterString)) {
                        tmp_dataModelArrayList.add(portalObj1);
                    }
                }
            }

            results.values = tmp_dataModelArrayList;
            results.count = tmp_dataModelArrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mapDailogStandNumberAdapter.tmp_dataModelArrayList.clear();
            mapDailogStandNumberAdapter.tmp_dataModelArrayList.addAll((ArrayList<DataModel>) filterResults.values);
            mapDailogStandNumberAdapter.notifyDataSetChanged();
        }
    }

}
