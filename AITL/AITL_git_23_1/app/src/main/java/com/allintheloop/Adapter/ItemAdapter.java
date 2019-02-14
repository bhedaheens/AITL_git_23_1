package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.ItemSilentBuyNowList;
import com.allintheloop.Fragment.FundraisingModule.Item_BuyNow_Fragment;
import com.allintheloop.Fragment.FundraisingModule.Item_SlientAuction_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by nteam on 22/8/16.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> implements VolleyInterface, Filterable {

    ArrayList<ItemSilentBuyNowList> silentBuyNowLists;
    ArrayList<ItemSilentBuyNowList> tmp_silentBuyNowLists;
    Context context;
    int position;
    SessionManager sessionManager;
    String tag;
    int tmp;

    public ItemAdapter(ArrayList<ItemSilentBuyNowList> silentBuyNowLists, Context context) {
        this.silentBuyNowLists = silentBuyNowLists;
        this.context = context;
        tmp_silentBuyNowLists = new ArrayList<>();
        tmp_silentBuyNowLists.addAll(silentBuyNowLists);
        sessionManager = new SessionManager(context);
    }

    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpater_item_fragment, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.ViewHolder holder, final int position) {
        final ItemSilentBuyNowList itemObj = tmp_silentBuyNowLists.get(position);
        holder.txt_item_name.setText(itemObj.getItem_name());
        tag = itemObj.getTag();


        Glide.with(context)
                .load(MyUrls.Fund_Imgurl + itemObj.getThumb_img())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        holder.product_image.setVisibility(View.VISIBLE);
                        holder.progressBar1.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        holder.product_image.setVisibility(View.VISIBLE);
                        holder.progressBar1.setVisibility(View.GONE);
                        return false;
                    }
                })
                .centerCrop()
                .fitCenter()
                .into(holder.product_image);

        holder.img_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context)
                        .items(R.array.itemAction)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                if (which == 0) {

                                    Log.d("AITL ProductID", itemObj.getProduct_id());
                                    Log.d("AITL AuctionType", itemObj.getAuction_type());

                                    sessionManager.itemProduct_id = itemObj.getProduct_id();
                                    sessionManager.itemAuctionType = itemObj.getAuction_type();

                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.EditItemFragment;
                                    ((MainActivity) context).loadFragment();
                                } else if (which == 1) {
                                    deleteItem(itemObj.getProduct_id());
                                    tmp = position;
                                }
                            }
                        }).show();
            }
        });


        if (position % 2 == 0) {
            holder.card_item.setCardBackgroundColor(context.getResources().getColor(R.color.card_back));
        } else {
            holder.card_item.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        if (itemObj.getTag().equalsIgnoreCase("silent")) {
            holder.txt_Rprice.setVisibility(View.VISIBLE);
            holder.lbl_Rprice.setVisibility(View.VISIBLE);

            if (Item_SlientAuction_Fragment.str_currencyStatus.equalsIgnoreCase("euro")) {

                holder.txt_Sprice.setText(context.getResources().getString(R.string.euro) + itemObj.getStartPrice());
                holder.txt_Rprice.setText(context.getResources().getString(R.string.euro) + itemObj.getReserveBid());

            } else if (Item_SlientAuction_Fragment.str_currencyStatus.equalsIgnoreCase("gbp")) {

                holder.txt_Sprice.setText(context.getResources().getString(R.string.pound_sign) + itemObj.getStartPrice());
                holder.txt_Rprice.setText(context.getResources().getString(R.string.pound_sign) + itemObj.getReserveBid());
            } else if (Item_SlientAuction_Fragment.str_currencyStatus.equalsIgnoreCase("usd") || Item_SlientAuction_Fragment.str_currencyStatus.equalsIgnoreCase("aud")) {
                holder.txt_Sprice.setText(context.getResources().getString(R.string.dollor) + itemObj.getStartPrice());
                holder.txt_Rprice.setText(context.getResources().getString(R.string.dollor) + itemObj.getReserveBid());

            }
        } else if (itemObj.getTag().equalsIgnoreCase("buynow")) {
            holder.txt_Rprice.setVisibility(View.GONE);
            holder.lbl_Rprice.setVisibility(View.GONE);

            holder.lbl_Sprice.setText("Price");

            if (Item_BuyNow_Fragment.str_currencyStatus.equalsIgnoreCase("euro")) {
                holder.txt_Sprice.setText(context.getResources().getString(R.string.euro) + itemObj.getPrice());
            } else if (Item_BuyNow_Fragment.str_currencyStatus.equalsIgnoreCase("gbp")) {
                holder.txt_Sprice.setText(context.getResources().getString(R.string.pound_sign) + itemObj.getPrice());
            } else if (Item_BuyNow_Fragment.str_currencyStatus.equalsIgnoreCase("usd") || Item_BuyNow_Fragment.str_currencyStatus.equalsIgnoreCase("aud")) {
                holder.txt_Sprice.setText(context.getResources().getString(R.string.dollor) + itemObj.getStartPrice());
            }
        }
//        Glide.with(context).load(MyUrls.thumImgUrl+itemObj.getThumb_img()).centerCrop().fitCenter().into(holder.product_image);

        if (itemObj.getApproved_status().equalsIgnoreCase("1")) {
            holder.txt_approved.setText("Yes");
            holder.txt_approved.setTextColor(context.getResources().getColor(R.color.white));
            holder.txt_approved.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else if (itemObj.getApproved_status().equalsIgnoreCase("0")) {
            holder.txt_approved.setText("No");
            holder.txt_approved.setTextColor(context.getResources().getColor(R.color.white));
            holder.txt_approved.setBackgroundColor(context.getResources().getColor(R.color.red));
        }


//        holder.txt_approved.setText(itemObj.getApproved_status());
        Log.d("AITL ProductImage", MyUrls.Fund_Imgurl + itemObj.getThumb_img());
    }

    private void deleteItem(String product_id) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.delete_item, Param.delete_item(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), sessionManager.getUserId(), product_id), 0, true, this);
    }

    @Override
    public int getItemCount() {
        return tmp_silentBuyNowLists.size();
    }

    @Override
    public Filter getFilter() {
        return new ItemListFilter(this, silentBuyNowLists);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        if (tag.equalsIgnoreCase("silent")) {
                            tmp_silentBuyNowLists.remove(tmp);
                            silentBuyNowLists.remove(tmp);
                            Item_SlientAuction_Fragment.rv_viewSilentList.removeViewAt(tmp);
                            notifyItemRemoved(tmp);
                            notifyItemRangeChanged(tmp, tmp_silentBuyNowLists.size());
                            notifyDataSetChanged();
                        } else if (tag.equalsIgnoreCase("buynow")) {
                            tmp_silentBuyNowLists.remove(tmp);
                            silentBuyNowLists.remove(position);
                            Item_BuyNow_Fragment.rv_viewSilentList.removeViewAt(tmp);
                            notifyItemRemoved(tmp);
                            notifyItemRangeChanged(tmp, tmp_silentBuyNowLists.size());
                            notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_action;
        CircleImageView product_image;
        ProgressBar progressBar1;
        TextView txt_item_name, txt_Sprice, txt_Rprice, txt_approved, lbl_Rprice, lbl_Sprice;
        CardView card_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img_action = (ImageView) itemView.findViewById(R.id.img_action);
            product_image = (CircleImageView) itemView.findViewById(R.id.product_image);
            txt_item_name = (TextView) itemView.findViewById(R.id.txt_item_name);
            txt_Sprice = (TextView) itemView.findViewById(R.id.txt_Sprice);
            txt_Rprice = (TextView) itemView.findViewById(R.id.txt_Rprice);
            txt_approved = (TextView) itemView.findViewById(R.id.txt_approved);
            lbl_Rprice = (TextView) itemView.findViewById(R.id.lbl_Rprice);
            lbl_Sprice = (TextView) itemView.findViewById(R.id.lbl_Sprice);
            card_item = (CardView) itemView.findViewById(R.id.card_item);
            progressBar1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);

        }
    }

    private static class ItemListFilter extends Filter {

        private final ItemAdapter adapter;

        ArrayList<ItemSilentBuyNowList> silentBuyNowLists;
        ArrayList<ItemSilentBuyNowList> tmp_silentBuyNowLists;

        public ItemListFilter(ItemAdapter adapter, ArrayList<ItemSilentBuyNowList> silentBuyNowLists) {
            this.adapter = adapter;
            this.silentBuyNowLists = silentBuyNowLists;
            tmp_silentBuyNowLists = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("AdapterSequnce", constraint.toString());
            tmp_silentBuyNowLists.clear();

            final FilterResults results = new FilterResults();
            if (constraint.length() <= 0) {
                tmp_silentBuyNowLists.addAll(silentBuyNowLists);

            } else {
                final String filterString = constraint.toString().toLowerCase();
                for (ItemSilentBuyNowList ItemObj : silentBuyNowLists) {
                    String title = ItemObj.getItem_name().toLowerCase();
                    if (title.contains(filterString)) {
                        tmp_silentBuyNowLists.add(ItemObj);
                    }
                }
            }
            results.values = tmp_silentBuyNowLists;
            results.count = tmp_silentBuyNowLists.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.tmp_silentBuyNowLists.clear();
            adapter.tmp_silentBuyNowLists.addAll((ArrayList<ItemSilentBuyNowList>) results.values);
            adapter.notifyDataSetChanged();
        }
    }


}
