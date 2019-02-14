package com.allintheloop.Adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Util.MyUrls;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.EventList;
import com.allintheloop.R;

import java.util.ArrayList;


/**
 * Created by nteam on 30/4/16.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> implements Filterable {

    ArrayList<EventList> aEventList;
    ArrayList<EventList> aEvent;
    Context context;

    public EventListAdapter(ArrayList<EventList> aEventList, Context context) {
        this.aEventList = aEventList;
        this.context = context;
        aEvent = new ArrayList<>();
        aEvent.addAll(aEventList);
    }

    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_eventlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventListAdapter.ViewHolder holder, int position) {
        EventList Objevent = aEvent.get(position);

        // holder.imgView.setImageResource(R.drawable.ic_action_lock);


        Log.d("AITL IMAGE ADAPTER", "SECURE" + Objevent.getImg());

        Glide.with(context)
                .load(Objevent.getImg())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        holder.progressBar1.setVisibility(View.GONE);
                        holder.imgView.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        holder.progressBar1.setVisibility(View.GONE);
                        holder.imgView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.imgView);
        holder.Ename.setText(Objevent.geteName());


    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public EventList getItem(int position) {
        return aEvent.get(position);
    }

    @Override
    public int getItemCount() {
        return aEvent.size();
    }

//    public void setFilter(ArrayList<EventList> countryModels) {
//        aEventList = new ArrayList<>();
//        aEventList.addAll(countryModels);
//        notifyDataSetChanged();
//    }  // for Filter Data

    @Override
    public Filter getFilter() {
        return new EventListFilter(this, aEventList);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Ename;
        ImageView imgView;

        //        ProgressBar progressBar1;
        public ViewHolder(View itemView) {
            super(itemView);

            Ename = (TextView) itemView.findViewById(R.id.Eventname);
            imgView = (ImageView) itemView.findViewById(R.id.EventImg);
//            progressBar1= (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }

    private static class EventListFilter extends Filter {

        private final EventListAdapter adapter;
        private final ArrayList<EventList> aEventList;
        private final ArrayList<EventList> aEvent;

        public EventListFilter(EventListAdapter adapter, ArrayList<EventList> aEventList) {

            this.adapter = adapter;
            this.aEventList = aEventList;
            aEvent = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("AdapterSequnce", constraint.toString());
            aEvent.clear();

            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                aEvent.addAll(aEventList);

            } else {
                final String filterString = constraint.toString().toLowerCase();
                for (EventList eventObj : aEventList) {
                    String title = eventObj.geteName().toLowerCase();
                    if (title.contains(filterString)) {
                        aEvent.add(eventObj);
                    }
                }
            }
            results.values = aEvent;
            results.count = aEvent.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.aEvent.clear();
            adapter.aEvent.addAll((ArrayList<EventList>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}