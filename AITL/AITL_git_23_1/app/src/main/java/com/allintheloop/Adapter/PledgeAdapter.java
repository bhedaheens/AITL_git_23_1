package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.allintheloop.Bean.Fundraising.PledgeItemList;
import com.allintheloop.Fragment.FundraisingModule.Order_PleadgeItem_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by nteam on 17/8/16.
 */
public class PledgeAdapter extends RecyclerView.Adapter<PledgeAdapter.ViewHolder> implements Filterable {

    ArrayList<PledgeItemList> pledgeItemLists;
    ArrayList<PledgeItemList> tmppledgeItemLists;
    Context context;

    public PledgeAdapter(ArrayList<PledgeItemList> pledgeItemLists, Context context) {
        this.pledgeItemLists = pledgeItemLists;
        this.context = context;
        tmppledgeItemLists = new ArrayList<>();
        tmppledgeItemLists.addAll(pledgeItemLists);
    }

    @Override
    public PledgeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pledgeitemlist_fragment, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(PledgeAdapter.ViewHolder holder, int position) {

        PledgeItemList pledgeObj = tmppledgeItemLists.get(position);

        holder.txt_product_name.setText(pledgeObj.getName());
        holder.txt_dateTime.setText(pledgeObj.getCreated_date());

        if (!(pledgeObj.getPledge_amt().equalsIgnoreCase(""))) {
            if (Order_PleadgeItem_Fragment.str_currencyStatus.equalsIgnoreCase("euro")) {

                holder.txt_donate.setText(context.getResources().getString(R.string.euro) + pledgeObj.getPledge_amt());
            } else if (Order_PleadgeItem_Fragment.str_currencyStatus.equalsIgnoreCase("gbp")) {

                holder.txt_donate.setText(context.getResources().getString(R.string.pound_sign) + pledgeObj.getPledge_amt());
            } else if (Order_PleadgeItem_Fragment.str_currencyStatus.equalsIgnoreCase("usd") || Order_PleadgeItem_Fragment.str_currencyStatus.equalsIgnoreCase("aud")) {
                holder.txt_donate.setText(context.getResources().getString(R.string.dollor) + pledgeObj.getPledge_amt());

            }
        }

        Glide.with(context).load(MyUrls.Fund_Imgurl + pledgeObj.getThumb()).centerCrop().fitCenter().into(holder.product_image);

        if (position % 2 == 0) {
            holder.card_order.setCardBackgroundColor(context.getResources().getColor(R.color.card_back));
        } else {
            holder.card_order.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return tmppledgeItemLists.size();
    }

    @Override
    public Filter getFilter() {
        return new PledgeListFilter(this, pledgeItemLists);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView card_order;
        TextView txt_product_name, txt_donate, txt_dateTime;
        CircleImageView product_image;

        public ViewHolder(View itemView) {
            super(itemView);
            card_order = (CardView) itemView.findViewById(R.id.card_order);
            txt_product_name = (TextView) itemView.findViewById(R.id.txt_product_name);
            txt_donate = (TextView) itemView.findViewById(R.id.txt_donate);
            txt_dateTime = (TextView) itemView.findViewById(R.id.txt_dateTime);
            product_image = (CircleImageView) itemView.findViewById(R.id.product_image);
        }
    }

    private static class PledgeListFilter extends Filter {

        private final PledgeAdapter adapter;
        private final ArrayList<PledgeItemList> pledgeItemLists;
        private final ArrayList<PledgeItemList> tmppledgeItemLists;

        public PledgeListFilter(PledgeAdapter adapter, ArrayList<PledgeItemList> pledgeItemLists) {

            this.adapter = adapter;
            this.pledgeItemLists = pledgeItemLists;
            tmppledgeItemLists = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("AdapterSequnce", constraint.toString());
            tmppledgeItemLists.clear();

            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                tmppledgeItemLists.addAll(pledgeItemLists);

            } else {
                final String filterString = constraint.toString().toLowerCase();
                for (PledgeItemList pledgeObj : pledgeItemLists) {
                    String title = pledgeObj.getName().toLowerCase();
                    if (title.contains(filterString)) {
                        tmppledgeItemLists.add(pledgeObj);
                    }
                }
            }
            results.values = tmppledgeItemLists;
            results.count = tmppledgeItemLists.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.tmppledgeItemLists.clear();
            adapter.tmppledgeItemLists.addAll((ArrayList<PledgeItemList>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
