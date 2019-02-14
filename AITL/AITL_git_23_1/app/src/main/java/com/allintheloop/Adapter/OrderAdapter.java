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

import com.allintheloop.Bean.Fundraising.OrderListItem;
import com.allintheloop.R;

import java.util.ArrayList;


/**
 * Created by nteam on 16/8/16.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> implements Filterable {

    ArrayList<OrderListItem> orderListItems;
    ArrayList<OrderListItem> tmporderListItems;
    Context context;

    public OrderAdapter(ArrayList<OrderListItem> orderListItems, Context context) {
        this.orderListItems = orderListItems;
        this.context = context;
        tmporderListItems = new ArrayList<>();
        tmporderListItems.addAll(orderListItems);
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_orderlistitem_fragment, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        OrderListItem orderItem = tmporderListItems.get(position);

        holder.txt_mode.setText(orderItem.getMode());
        holder.txt_orderStatus.setText(orderItem.getOrderStatus());
        holder.txt_createdDate.setText(orderItem.getCreated_date());


        if (position % 2 == 0) {
            holder.card_order.setCardBackgroundColor(context.getResources().getColor(R.color.card_back));
        } else {
            holder.card_order.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }


    @Override
    public int getItemCount() {
        return tmporderListItems.size();
    }

    @Override
    public Filter getFilter() {
        return new OrderListFilter(this, orderListItems);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_mode, txt_orderStatus, txt_createdDate;
        CardView card_order;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_mode = (TextView) itemView.findViewById(R.id.txt_mode);
            txt_orderStatus = (TextView) itemView.findViewById(R.id.txt_orderStatus);
            txt_createdDate = (TextView) itemView.findViewById(R.id.txt_createdDate);
            card_order = (CardView) itemView.findViewById(R.id.card_order);


        }
    }

    private static class OrderListFilter extends Filter {

        private final OrderAdapter adapter;
        private final ArrayList<OrderListItem> orderListItems;
        private final ArrayList<OrderListItem> tmporderListItems;

        public OrderListFilter(OrderAdapter adapter, ArrayList<OrderListItem> orderListItems) {

            this.adapter = adapter;
            this.orderListItems = orderListItems;
            tmporderListItems = new ArrayList<>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("AdapterSequnce", constraint.toString());
            tmporderListItems.clear();

            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                tmporderListItems.addAll(orderListItems);

            } else {
                final String filterString = constraint.toString().toLowerCase();
                for (OrderListItem orderObj : orderListItems) {
                    String title = orderObj.getMode().toLowerCase();
                    if (title.contains(filterString)) {
                        tmporderListItems.add(orderObj);
                    }
                }
            }
            results.values = tmporderListItems;
            results.count = tmporderListItems.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.tmporderListItems.clear();
            adapter.tmporderListItems.addAll((ArrayList<OrderListItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

}
