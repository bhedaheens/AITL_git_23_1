package com.allintheloop.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCategoryListing;
import com.allintheloop.R;
import com.allintheloop.Util.FastScrollRecyclerViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by flaviusmester on 23/02/15.
 */
public class ProductCategoryCardAdapter extends RecyclerView.Adapter<ProductCategoryCardAdapter.ViewHolder> implements FastScrollRecyclerViewInterface, Filterable {

    private ArrayList<ExhibitorCategoryListing> mDataset;
    private ArrayList<ExhibitorCategoryListing> mDatasetOriginal = new ArrayList<>();
    private HashMap<String, Integer> mMapIndex;
    private CustomFilter mFilter;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView ivCheck;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.txtName);
            ivCheck = (ImageView) v.findViewById(R.id.ivCheck);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductCategoryCardAdapter(ArrayList<ExhibitorCategoryListing> myDataset, HashMap<String, Integer> mapIndex) {
        mDataset = myDataset;
        mDatasetOriginal.addAll(myDataset);
        mMapIndex = mapIndex;
        mFilter = new CustomFilter(ProductCategoryCardAdapter.this);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_countrylist_card, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ExhibitorCategoryListing country = mDataset.get(position);

        holder.mTextView.setText(country.getSector());

        if (country.isCheck()) {
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.ivCheck.setVisibility(View.INVISIBLE);
        }

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (country.isCheck()) {
                    country.setCheck(false);
                    holder.ivCheck.setVisibility(View.INVISIBLE);
                } else {
                    country.setCheck(true);
                    holder.ivCheck.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return this.mMapIndex;
    }

    public void filter(String text) {
        mDataset.clear();

        for (ExhibitorCategoryListing d : mDatasetOriginal) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getSector().toLowerCase().contains(text.toLowerCase())) {
                mDataset.add(d);
            }
        }
        //update recyclerview
        notifyDataSetChanged();
    }

    public class CustomFilter extends Filter {
        private ProductCategoryCardAdapter mAdapter;

        private CustomFilter(ProductCategoryCardAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            mDataset.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                mDataset.addAll(mDatasetOriginal);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final ExhibitorCategoryListing mWords : mDatasetOriginal) {
                    if (mWords.getSector().toLowerCase().contains(filterPattern)) {
                        mDataset.add(mWords);
                    }
                }
            }
            System.out.println("Count Number " + mDataset.size());
            results.values = mDataset;
            results.count = mDataset.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("Count Number 2 " + ((List<ExhibitorCategoryListing>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public ArrayList<ExhibitorCategoryListing> getSelectedList() {
        ArrayList<ExhibitorCategoryListing> selected = new ArrayList<>();
        for (int i = 0; i < mDatasetOriginal.size(); i++) {
            if (mDatasetOriginal.get(i).isCheck()) {
                selected.add(mDatasetOriginal.get(i));
            }
        }
        return selected;
    }
}