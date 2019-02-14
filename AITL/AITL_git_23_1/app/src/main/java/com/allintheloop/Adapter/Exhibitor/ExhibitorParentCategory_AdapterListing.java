package com.allintheloop.Adapter.Exhibitor;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorParentCategoryData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by nteam on 11/10/17.
 */

public class ExhibitorParentCategory_AdapterListing extends RecyclerView.Adapter<ExhibitorParentCategory_AdapterListing.ViewHolder> implements Filterable {
    ArrayList<ExhibitorParentCategoryData> objectArrayList;
    ArrayList<ExhibitorParentCategoryData> tmp_objectArrayList;
    Context context;
    SessionManager sessionManager;
    int tmp_postion;

    public ExhibitorParentCategory_AdapterListing(ArrayList<ExhibitorParentCategoryData> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        tmp_objectArrayList = new ArrayList<>();
        tmp_objectArrayList.addAll(objectArrayList);
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_exhibitor_parentcategory_listing, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        this.tmp_postion = i;
        Glide.with(context)
                .load(tmp_objectArrayList.get(i).getCategorie_icon())
                .into(viewHolder.img_category);
        viewHolder.category_name.setText(tmp_objectArrayList.get(i).getCategory());

        viewHolder.app_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setExhibitorParentCategoryId(tmp_objectArrayList.get(i).getC_id());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//                GlobalData.CURRENT_FRAG=GlobalData.ExhibitorSubCategoryListFragment;
                GlobalData.CURRENT_FRAG = GlobalData.ExhibitorCategoryWiseData;
                ((MainActivity) context).loadFragment();

            }
        });

    }

    @Override
    public int getItemCount() {
        return tmp_objectArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new ExhibitorParentCateforyFilter(this, objectArrayList, tmp_postion);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category_name;
        ImageView img_category;
        CardView app_back;

        public ViewHolder(View itemView) {
            super(itemView);
            category_name = (TextView) itemView.findViewById(R.id.category_name);
            img_category = (ImageView) itemView.findViewById(R.id.img_category);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
        }
    }

    private static class ExhibitorParentCateforyFilter extends Filter {
        private final ExhibitorParentCategory_AdapterListing adapterListing;
        private final ArrayList<ExhibitorParentCategoryData> objectArrayList;
        private final ArrayList<ExhibitorParentCategoryData> tmp_objectArrayList;
        private final int postion;


        public ExhibitorParentCateforyFilter(ExhibitorParentCategory_AdapterListing adapterListing, ArrayList<ExhibitorParentCategoryData> objectArrayList, int postion) {
            this.adapterListing = adapterListing;
            this.objectArrayList = objectArrayList;
            tmp_objectArrayList = new ArrayList<>();
            this.postion = postion;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            final FilterResults results = new FilterResults();
            if (charSequence.length() <= 0) {
                tmp_objectArrayList.addAll(objectArrayList);
            } else {
                final String filterString = charSequence.toString().toLowerCase();
                for (ExhibitorParentCategoryData attenObj : objectArrayList) {
                    String title = attenObj.getCategory().toLowerCase();
                    if (title.contains(filterString)) {
                        tmp_objectArrayList.add(attenObj);
                    }
                }
            }
            results.values = tmp_objectArrayList;
            results.count = tmp_objectArrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapterListing.tmp_objectArrayList.clear();
            adapterListing.tmp_objectArrayList.addAll((ArrayList<ExhibitorParentCategoryData>) filterResults.values);
            adapterListing.notifyDataSetChanged();
        }
    }


}
