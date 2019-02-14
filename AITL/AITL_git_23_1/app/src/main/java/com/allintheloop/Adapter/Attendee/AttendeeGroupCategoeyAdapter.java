package com.allintheloop.Adapter.Attendee;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.Attendee.AttendeeCategoryList;
import com.allintheloop.R;
import com.allintheloop.Util.FastScrollRecyclerViewInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nteam on 28/3/18.
 */

public class AttendeeGroupCategoeyAdapter extends RecyclerView.Adapter<AttendeeGroupCategoeyAdapter.ViewHolder> implements FastScrollRecyclerViewInterface, Filterable {

    private ArrayList<AttendeeCategoryList.AttendeeCategory> mDataset;
    private ArrayList<AttendeeCategoryList.AttendeeCategory> mDatasetOriginal = new ArrayList<>();
    private HashMap<String, Integer> mMapIndex;
    private CustomFilter mFilter;

    // Provide a suitable constructor (depends on the kind of dataset)
    public AttendeeGroupCategoeyAdapter(ArrayList<AttendeeCategoryList.AttendeeCategory> myDataset, HashMap<String, Integer> mapIndex) {
        mDataset = myDataset;
        mDatasetOriginal.addAll(mDataset);

        mMapIndex = mapIndex;
        mFilter = new CustomFilter(AttendeeGroupCategoeyAdapter.this);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        final AttendeeCategoryList.AttendeeCategory category = mDataset.get(position);

        holder.mTextView.setText(category.getCategory());
        Log.d("Bhavdip NAME", "" + category.getCategory());
        if (category.isCheck()) {
            holder.ivCheck.setVisibility(View.VISIBLE);
        } else {
            holder.ivCheck.setVisibility(View.INVISIBLE);
        }

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category.isCheck()) {
                    category.setCheck(false);
                    holder.ivCheck.setVisibility(View.INVISIBLE);
                } else {
                    category.setCheck(true);
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
    public HashMap<String, Integer> getMapIndex() {
        return this.mMapIndex;
    }

    // Do Search...
    public void filter(final String text) {

        mDataset.clear();

        // If there is no search value, then add all original list items to filter list
        if (TextUtils.isEmpty(text)) {

            mDataset.addAll(mDatasetOriginal);

        } else {
            // Iterate in the original List and add it to filter list...
            for (AttendeeCategoryList.AttendeeCategory item : mDatasetOriginal) {
                if (item.getCategory().toLowerCase().contains(text.toLowerCase())) {
                    // Adding Matched items
                    mDataset.add(item);
                }
            }
        }
        // Notify the List that the DataSet has changed...
        notifyDataSetChanged();

    }

    public ArrayList<AttendeeCategoryList.AttendeeCategory> getSelectedList() {
        ArrayList<AttendeeCategoryList.AttendeeCategory> selected = new ArrayList<>();
        for (int i = 0; i < mDatasetOriginal.size(); i++) {
            if (mDatasetOriginal.get(i).isCheck()) {
                selected.add(mDatasetOriginal.get(i));
            }
        }
        return selected;
    }

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

    public class CustomFilter extends Filter {
        private AttendeeGroupCategoeyAdapter mAdapter;

        private CustomFilter(AttendeeGroupCategoeyAdapter mAdapter) {
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
                for (final AttendeeCategoryList.AttendeeCategory mWords : mDatasetOriginal) {
                    if (mWords.getCategory().toLowerCase().contains(filterPattern)) {
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
            System.out.println("Count Number 2 " + ((List<AttendeeCategoryList.AttendeeCategory>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
