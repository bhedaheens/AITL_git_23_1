package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.Fundraising.checkOutDetail;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;

import java.util.ArrayList;


/**
 * Created by nteam on 26/7/16.
 */
public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    ArrayList<checkOutDetail> checkOutDetailArrayList;
    Context context;

    public CheckoutAdapter(ArrayList<checkOutDetail> checkOutDetailArrayList, Context context) {
        this.checkOutDetailArrayList = checkOutDetailArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_checkout, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        checkOutDetail checkOutDetailObj = checkOutDetailArrayList.get(position);
//        holder.product_image
        holder.productName.setText(checkOutDetailObj.getName());
        holder.txt_qty.setText(checkOutDetailObj.getQty());
        holder.txt_price.setText(checkOutDetailObj.getPrice());


        Log.d("AITL CheckOutDetailImg", MyUrls.Fund_Imgurl + checkOutDetailObj.getThumb_img());
        Glide.with(context).load(MyUrls.Fund_Imgurl + checkOutDetailObj.getThumb_img()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                holder.product_image.setImageResource(R.drawable.noimage);
                return true;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        })
                .centerCrop()
                .fitCenter()
                .into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return checkOutDetailArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView productName, txt_qty, txt_price;

        public ViewHolder(View itemView) {
            super(itemView);
            product_image = (ImageView) itemView.findViewById(R.id.product_image);
            productName = (TextView) itemView.findViewById(R.id.productName);
            txt_qty = (TextView) itemView.findViewById(R.id.txt_qty);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
        }
    }
}
