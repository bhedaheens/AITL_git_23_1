package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.allintheloop.Bean.Fundraising.Fundraising_Product;
import com.allintheloop.Fragment.FundraisingModule.Fundrising_Home_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

/**
 * Created by nteam on 10/6/16.
 */
public class Fundraising_Product_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<Fundraising_Product> arrayList;
    Context context;

    SessionManager sessionManager;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    LinearLayoutManager lytManager;
    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private static final int TYPE_ITEM = 1;

    public Fundraising_Product_Adapter(ArrayList<Fundraising_Product> arrayList, RecyclerView recyclerView, Context context, LinearLayoutManager layoutManager) {

        this.arrayList = arrayList;
        this.context = context;
        sessionManager = new SessionManager(this.context);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            this.lytManager = layoutManager;

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = lytManager.getItemCount();
                    lastVisibleItem = lytManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }

                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fundraising_product, parent, false);
            return new MyViewHolder(itemView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder && getItem(position) != null) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;

            Fundraising_Product productObj = arrayList.get(position);

            Glide.with(context).load(productObj.getImg()).placeholder(R.drawable.product_no_image).centerCrop().fitCenter().into(myViewHolder.product_image);
            myViewHolder.product_name.setText(productObj.getName());
            myViewHolder.product_desc.setText(Html.fromHtml(productObj.getDesc()));

            if (productObj.getFlag_price().equalsIgnoreCase("1")) {
                if (Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("euro")) {

                    myViewHolder.product_price.setText(context.getResources().getString(R.string.euro) + productObj.getPrice());
                } else if (Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("gbp")) {

                    myViewHolder.product_price.setText(context.getResources().getString(R.string.pound_sign) + productObj.getPrice());
                } else if (Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("usd") || Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("aud")) {
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.dollor) + productObj.getPrice());
                }


            } else {
                if (Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("euro")) {
                    myViewHolder.txt_current_bid.setVisibility(View.VISIBLE);
                    myViewHolder.txt_current_bid.setText("Current Bid :");
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.euro) + productObj.getMax_bid());
                } else if (Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("gbp")) {

                    myViewHolder.txt_current_bid.setVisibility(View.VISIBLE);
                    myViewHolder.txt_current_bid.setText("Current Bid :");
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.pound_sign) + productObj.getMax_bid());
                } else if (Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("usd") || Fundrising_Home_Fragment.currency_status.equalsIgnoreCase("aud")) {
                    myViewHolder.txt_current_bid.setVisibility(View.VISIBLE);
                    myViewHolder.txt_current_bid.setText("Current Bid :");
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.dollor) + productObj.getMax_bid());
                }

            }

            if (sessionManager.getFunThemeColor().equalsIgnoreCase("0")) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(5, Color.parseColor("#3f8acd"));
                myViewHolder.product_card.setBackgroundDrawable(drawable);
            } else {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setStroke(5, Color.parseColor(sessionManager.getFunThemeColor()));
                myViewHolder.product_card.setBackgroundDrawable(drawable);
            }
        } else if (holder instanceof ProgressViewHolder) {

            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    public Fundraising_Product getItem(int position) {

        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (arrayList.get(position) == null) {

            return VIEW_PROG;
        } else {

            return TYPE_ITEM;
        }
    }

    public void addFooter() {

        arrayList.add(null);
        notifyItemInserted(arrayList.size() - 1);
    }

    public void removeFooter() {

        arrayList.remove(arrayList.size() - 1);
        notifyItemRemoved(arrayList.size());
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView product_card;
        ImageView product_image;
        TextView product_name, txt_current_bid;
        AutofitTextView product_price;
        TextView product_desc;

        public MyViewHolder(View itemView) {
            super(itemView);

            product_image = (ImageView) itemView.findViewById(R.id.product_image);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_desc = (TextView) itemView.findViewById(R.id.product_desc);
            product_price = (AutofitTextView) itemView.findViewById(R.id.product_price);
            product_card = (CardView) itemView.findViewById(R.id.product_card);
            txt_current_bid = (TextView) itemView.findViewById(R.id.txt_current_bid);
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
