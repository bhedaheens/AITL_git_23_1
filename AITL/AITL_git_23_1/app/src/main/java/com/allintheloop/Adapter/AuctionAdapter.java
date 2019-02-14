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

import com.allintheloop.Bean.Fundraising.AuctionItemList;
import com.allintheloop.Fragment.FundraisingModule.Order_AuctionItem_Fragment;
import com.allintheloop.R;

import java.util.ArrayList;


/**
 * Created by nteam on 16/8/16.
 */
public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.ViewHolder> implements Filterable {
    ArrayList<AuctionItemList> auctionItemLists;
    ArrayList<AuctionItemList> tmpauctionItemLists;
    Context context;


    public AuctionAdapter(ArrayList<AuctionItemList> auctionItemLists, Context context) {
        this.auctionItemLists = auctionItemLists;
        this.context = context;
        tmpauctionItemLists = new ArrayList<>();
        tmpauctionItemLists.addAll(auctionItemLists);
    }

    @Override
    public AuctionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_auctionitemlist_fragment, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(AuctionAdapter.ViewHolder holder, int position) {


        AuctionItemList auctionobj = tmpauctionItemLists.get(position);

        holder.txt_product_name.setText(auctionobj.getName());


        if (Order_AuctionItem_Fragment.str_currencyStatus.equalsIgnoreCase("euro")) {
            holder.txt_amt.setText(context.getResources().getString(R.string.euro) + auctionobj.getBid_amt());

        } else if (Order_AuctionItem_Fragment.str_currencyStatus.equalsIgnoreCase("gbp")) {

            holder.txt_amt.setText(context.getResources().getString(R.string.pound_sign) + auctionobj.getBid_amt());
        } else if (Order_AuctionItem_Fragment.str_currencyStatus.equalsIgnoreCase("usd") || Order_AuctionItem_Fragment.str_currencyStatus.equalsIgnoreCase("aud")) {
            holder.txt_amt.setText(context.getResources().getString(R.string.dollor) + auctionobj.getBid_amt());

        }
        holder.txt_dateTime.setText(auctionobj.getDate());
        holder.txt_status.setText(auctionobj.getWin_status());
        if (auctionobj.getPayment_status().equalsIgnoreCase("0")) {
            holder.txt_payNow.setText("Failed");
        } else {
            holder.txt_payNow.setText("Completed Order");
        }


        if (position % 2 == 0) {
            holder.card_order.setCardBackgroundColor(context.getResources().getColor(R.color.card_back));
        } else {
            holder.card_order.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return tmpauctionItemLists.size();
    }

    @Override
    public Filter getFilter() {
        return new AuctionListFilter(this, auctionItemLists);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_product_name, txt_amt, txt_dateTime, txt_status, txt_payNow;
        CardView card_order;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_product_name = (TextView) itemView.findViewById(R.id.txt_product_name);
            txt_amt = (TextView) itemView.findViewById(R.id.txt_amt);
            txt_dateTime = (TextView) itemView.findViewById(R.id.txt_dateTime);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            txt_payNow = (TextView) itemView.findViewById(R.id.txt_payNow);
            card_order = (CardView) itemView.findViewById(R.id.card_order);
        }
    }

    private static class AuctionListFilter extends Filter {

        private final AuctionAdapter adapter;
        ArrayList<AuctionItemList> auctionItemLists;
        ArrayList<AuctionItemList> tmpauctionItemLists;

        public AuctionListFilter(AuctionAdapter adapter, ArrayList<AuctionItemList> auctionItemLists) {

            this.adapter = adapter;
            this.auctionItemLists = auctionItemLists;
            tmpauctionItemLists = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("AdapterSequnce", constraint.toString());
            tmpauctionItemLists.clear();

            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                tmpauctionItemLists.addAll(auctionItemLists);

            } else {
                final String filterString = constraint.toString().toLowerCase();
                for (AuctionItemList auctionObj : auctionItemLists) {
                    String title = auctionObj.getName().toLowerCase();
                    if (title.contains(filterString)) {
                        tmpauctionItemLists.add(auctionObj);
                    }
                }
            }
            results.values = tmpauctionItemLists;
            results.count = tmpauctionItemLists.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.tmpauctionItemLists.clear();
            adapter.tmpauctionItemLists.addAll((ArrayList<AuctionItemList>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

}
