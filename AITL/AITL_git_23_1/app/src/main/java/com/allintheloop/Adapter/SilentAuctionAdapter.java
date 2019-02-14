package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.allintheloop.Bean.Fundraising.SilentAuction;
import com.allintheloop.R;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;


/**
 * Created by nteam on 12/7/16.
 */
public class SilentAuctionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<SilentAuction> arrayList;
    Context context;

    SessionManager sessionManager;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    LinearLayoutManager lytManager;
    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private static final int TYPE_ITEM = 1;
    int itemHeight, itemWidth;
    String currency_status;

    public SilentAuctionAdapter(ArrayList<SilentAuction> arrayList, RecyclerView recyclerView, Context context, LinearLayoutManager layoutManager, String currency_status) {

        this.arrayList = arrayList;
        this.context = context;
        this.currency_status = currency_status;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_silentauction, parent, false);
            return new MyViewHolder(itemView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder && getItem(position) != null) {
            final MyViewHolder myViewHolder = (MyViewHolder) holder;

            final SilentAuction productObj = arrayList.get(position);


            if (itemHeight > 0) {


                Glide.with(context).load(productObj.getImg()).asBitmap()
                        .override(itemWidth, itemHeight)
                        .placeholder(context.getResources().getDrawable(R.drawable.product_no_image))
                        .into(new BitmapImageViewTarget(myViewHolder.product_image) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                Log.e("SilentAuctionAdapter", " resource width: " + resource.getWidth() + " h: " + resource.getHeight());
                                myViewHolder.product_image.setImageBitmap(resource);
                            }
                        })

                ;
                Log.d("Height", "" + itemHeight);


            } else {

                myViewHolder.product_image.post(new Runnable() {
                    @Override
                    public void run() {
                        itemHeight = myViewHolder.textLinear.getHeight();
                        itemWidth = myViewHolder.textLinear.getWidth();

                        Glide.with(context).load(productObj.getImg()).asBitmap()
                                .override(itemWidth, itemHeight)
                                .placeholder(context.getResources().getDrawable(R.drawable.product_no_image))
                                .into(new BitmapImageViewTarget(myViewHolder.product_image) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        Log.e("SilentAuctionAdapter", " resource width: " + resource.getWidth() + " h: " + resource.getHeight());
                                        myViewHolder.product_image.setImageBitmap(resource);
                                    }
                                })
                        ;
                        Log.d("Height", "" + itemHeight);
                    }

                });

            }


//            Glide.with(context).load(productObj.getImg()).placeholder(R.drawable.product_no_image).into(myViewHolder.product_image);
            myViewHolder.product_name.setText(productObj.getName());
            myViewHolder.product_desc.setText(Html.fromHtml(productObj.getDesc()));


//            if (productObj.getProduct_preview().equalsIgnoreCase("1"))
//            {
//                MainActivity.img_cart.setVisibility(View.GONE);
//                MainActivity.txt_cart_badge.setVisibility(View.GONE);
//                MainActivity.badge_layout.setVisibility(View.GONE);
//                myViewHolder.product_price.setVisibility(View.GONE);
//                MainActivity.frme_cart.setVisibility(View.GONE);
//            }
//            else
//            {
//                MainActivity.frme_cart.setVisibility(View.VISIBLE);
//                MainActivity.img_cart.setVisibility(View.VISIBLE);
//                MainActivity.txt_cart_badge.setVisibility(View.VISIBLE);
//                MainActivity.badge_layout.setVisibility(View.VISIBLE);
//                myViewHolder.product_price.setVisibility(View.VISIBLE);
//            }
            if (productObj.getMax_bid().equalsIgnoreCase("")) {
                myViewHolder.txt_current_bid.setVisibility(View.GONE);
            }

            if (productObj.getTag().equalsIgnoreCase("1")) // Silent Auction
            {
                if (currency_status.equalsIgnoreCase("euro")) {
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.euro) + productObj.getPrice());
                    if (productObj.getMax_bid().equalsIgnoreCase("")) {
                        myViewHolder.txt_current_bid.setText("Current Bid :");
                    } else {
                        myViewHolder.txt_current_bid.setText("Current Bid :" + context.getResources().getString(R.string.euro) + productObj.getMax_bid());
                    }
                } else if (currency_status.equalsIgnoreCase("gbp")) {

                    myViewHolder.product_price.setText(context.getResources().getString(R.string.pound_sign) + productObj.getPrice());
                    if (productObj.getMax_bid().equalsIgnoreCase("")) {
                        myViewHolder.txt_current_bid.setText("Current Bid :");
                    } else {
                        myViewHolder.txt_current_bid.setText("Current Bid :" + context.getResources().getString(R.string.pound_sign) + productObj.getMax_bid());
                    }
                } else if (currency_status.equalsIgnoreCase("usd") || currency_status.equalsIgnoreCase("aud")) {
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.dollor) + productObj.getPrice());
                    if (productObj.getMax_bid().equalsIgnoreCase("")) {
                        myViewHolder.txt_current_bid.setText("Current Bid :");
                    } else {
                        myViewHolder.txt_current_bid.setText("Current Bid :" + context.getResources().getString(R.string.dollor) + productObj.getMax_bid());
                    }

                }
            } else if (productObj.getTag().equalsIgnoreCase("2")) // Live Auction
            {
                if (currency_status.equalsIgnoreCase("euro")) {
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.euro) + productObj.getPrice());
                    myViewHolder.txt_current_bid.setVisibility(View.GONE);
                } else if (currency_status.equalsIgnoreCase("gbp")) {
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.pound_sign) + productObj.getPrice());
                    myViewHolder.txt_current_bid.setVisibility(View.GONE);
                } else if (currency_status.equalsIgnoreCase("usd") || currency_status.equalsIgnoreCase("aud")) {
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.dollor) + productObj.getPrice());
                    myViewHolder.txt_current_bid.setVisibility(View.GONE);

                }
            } else if (productObj.getTag().equalsIgnoreCase("3")) // Online shop
            {
                if (currency_status.equalsIgnoreCase("euro")) {
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.euro) + productObj.getPrice());
                    myViewHolder.txt_current_bid.setVisibility(View.GONE);
                } else if (currency_status.equalsIgnoreCase("gbp")) {
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.pound_sign) + productObj.getPrice());
                    myViewHolder.txt_current_bid.setVisibility(View.GONE);
                } else if (currency_status.equalsIgnoreCase("usd") || currency_status.equalsIgnoreCase("aud")) {
                    myViewHolder.product_price.setText(context.getResources().getString(R.string.dollor) + productObj.getPrice());
                    myViewHolder.txt_current_bid.setVisibility(View.GONE);

                }
            } else if (productObj.getTag().equalsIgnoreCase("4")) // Online shop
            {
                myViewHolder.txt_current_bid.setVisibility(View.GONE);
                myViewHolder.product_price.setVisibility(View.GONE);
            }


//
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setStroke(5, Color.parseColor(sessionManager.getFunThemeColor()));
            myViewHolder.product_card.setBackgroundDrawable(drawable);

        } else if (holder instanceof ProgressViewHolder) {

            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    public SilentAuction getItem(int position) {

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
        LinearLayout textLinear;

        public MyViewHolder(View itemView) {
            super(itemView);

            product_image = (ImageView) itemView.findViewById(R.id.product_image);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_desc = (TextView) itemView.findViewById(R.id.product_desc);
            product_price = (AutofitTextView) itemView.findViewById(R.id.product_price);
            product_card = (CardView) itemView.findViewById(R.id.product_card);
            textLinear = (LinearLayout) itemView.findViewById(R.id.textLinear);
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
