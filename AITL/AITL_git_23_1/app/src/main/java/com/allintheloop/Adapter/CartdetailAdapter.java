package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Bean.Fundraising.CartDetail;
import com.allintheloop.Fragment.FundraisingModule.CartDetail_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nteam on 23/7/16.
 */
public class CartdetailAdapter extends RecyclerView.Adapter<CartdetailAdapter.ViewHolder> implements VolleyInterface {
    ArrayList<CartDetail> cartDetailArrayList;
    Context context;
    int count_value = 0;
    int txt_value = 0;
    int quntity;
    int position;
    SessionManager sessionManager;
    JSONObject jObjectproduct;
    JSONArray jArrayproduct;

    public CartdetailAdapter(ArrayList<CartDetail> cartDetailArrayList, Context context) {
        this.cartDetailArrayList = cartDetailArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
        jObjectproduct = new JSONObject();
        jArrayproduct = new JSONArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cartdetail, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CartDetail cartDetailobj = cartDetailArrayList.get(position);
        this.position = position;
        holder.product_name.setText(cartDetailobj.getName());
        holder.edt_qty.setText(cartDetailobj.getQuantity());
//            holder.unit_price.setText(cartDetailobj.getPrice());


        if (cartDetailobj.getAuctionType().equalsIgnoreCase("3")) {

            holder.edt_qty.setFocusable(true);
            holder.edt_qty.setClickable(true);
            holder.img_remove.setVisibility(View.VISIBLE);
            holder.btn_minus.setVisibility(View.VISIBLE);
            holder.btn_plus.setVisibility(View.VISIBLE);

            holder.edt_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 0) {
                        holder.edt_qty.setText(cartDetailobj.getQuantity());
                    }
                }
            });
//            holder.edt_qty.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s)
//                {
//                    try {
//                        if (s.equals("")) {
//                            ToastC.show(context, "Please Enter quantity");
//                        } else {
//                            cartDetailobj.setIsChange(true);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            });

            CartDetail_Fragment.btn_updatecart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cartDetailobj.isChange()) {

                        try {

                            if (count_value > 0) {
                                jObjectproduct.put("product_id", cartDetailobj.getProduct_id());
                                jObjectproduct.put("qty", String.valueOf(count_value));

                                jArrayproduct.put(jObjectproduct);

                                if (GlobalData.isNetworkAvailable(context)) {
                                    updateCart();
                                }

                            } else {
                                ToastC.show(context, "Value Must be greater than or equal to 1");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.d("AITL BOOL Value", "" + cartDetailobj.isChange());
                    }
                }
            });


            holder.btn_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long totalPrice = 0;
                    txt_value = Integer.parseInt(holder.edt_qty.getText().toString());
                    count_value = txt_value;
                    count_value++;
                    totalPrice = count_value * Integer.parseInt(cartDetailArrayList.get(position).getPrice());

                    if (CartDetail_Fragment.str_currency.equalsIgnoreCase("euro"))
                        holder.sub_total.setText(context.getResources().getString(R.string.euro) + totalPrice);
                    else if (CartDetail_Fragment.str_currency.equalsIgnoreCase("gbp"))
                        holder.sub_total.setText(context.getResources().getString(R.string.pound_sign) + totalPrice);
                    else if (CartDetail_Fragment.str_currency.equalsIgnoreCase("usd") || CartDetail_Fragment.str_currency.equalsIgnoreCase("aud"))
                        holder.sub_total.setText(context.getResources().getString(R.string.dollor) + totalPrice);

                    if (count_value > 50) {
                        holder.btn_plus.setEnabled(false);
                    } else {
                        holder.btn_minus.setEnabled(true);
                    }
                    Log.d("AITL CountValue", "" + count_value);
                    holder.edt_qty.setText("" + count_value);

                    //cartDetailobj.setIsChange(true);
                    cartDetailArrayList.get(position).setIsChange(true);
                }
            });

            holder.btn_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long totalPrice = 0;
                    txt_value = Integer.parseInt(holder.edt_qty.getText().toString());
                    count_value = txt_value;
                    count_value--;
                    totalPrice = count_value * Integer.parseInt(cartDetailArrayList.get(position).getPrice());

                    if (CartDetail_Fragment.str_currency.equalsIgnoreCase("euro"))
                        holder.sub_total.setText(context.getResources().getString(R.string.euro) + totalPrice);
                    else if (CartDetail_Fragment.str_currency.equalsIgnoreCase("gbp"))
                        holder.sub_total.setText(context.getResources().getString(R.string.pound_sign) + totalPrice);
                    else if (CartDetail_Fragment.str_currency.equalsIgnoreCase("usd") || CartDetail_Fragment.str_currency.equalsIgnoreCase("aud"))
                        holder.sub_total.setText(context.getResources().getString(R.string.dollor) + totalPrice);

                    if (count_value <= 1) {
                        holder.btn_minus.setEnabled(false);
                    } else {
                        holder.btn_plus.setEnabled(true);
                    }
                    Log.d("AITL CountValue", "" + count_value);
                    holder.edt_qty.setText("" + count_value);
                    cartDetailArrayList.get(position).setIsChange(true);

                }
            });

            holder.img_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("Delete")
                            .items("Are you sure you want to Delete this Message?")
                            .positiveColor(context.getResources().getColor(R.color.colorAccent))
                            .positiveText("Delete")
                            .negativeText("Cancel")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {

                                    if (cartDetailArrayList.size() != 0) {
                                        try {
                                            deleteCartProduct(cartDetailobj.getProduct_id());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            })
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .cancelable(false)
                            .build();
                    dialog.show();
                }
            });

        } else {
            holder.img_remove.setVisibility(View.GONE);
            holder.btn_minus.setVisibility(View.GONE);
            holder.btn_plus.setVisibility(View.GONE);
            holder.edt_qty.setFocusable(false);
            holder.edt_qty.setClickable(false);
        }
        if (CartDetail_Fragment.str_currency.equalsIgnoreCase("euro")) {
            holder.unit_price.setText(context.getResources().getString(R.string.euro) + cartDetailobj.getPrice());
            holder.sub_total.setText(context.getResources().getString(R.string.euro) + cartDetailobj.getSubtotal());
        } else if (CartDetail_Fragment.str_currency.equalsIgnoreCase("gbp")) {
            holder.unit_price.setText(context.getResources().getString(R.string.pound_sign) + cartDetailobj.getPrice());
            holder.sub_total.setText(context.getResources().getString(R.string.pound_sign) + cartDetailobj.getSubtotal());
        } else if (CartDetail_Fragment.str_currency.equalsIgnoreCase("usd") || CartDetail_Fragment.str_currency.equalsIgnoreCase("aud")) {
            holder.unit_price.setText(context.getResources().getString(R.string.dollor) + cartDetailobj.getPrice());
            holder.sub_total.setText(context.getResources().getString(R.string.dollor) + cartDetailobj.getSubtotal());
        }
//            holder.sub_total.setText(cartDetailobj.getSubtotal());
    }


    private void deleteCartProduct(String product_id) {

        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.deleteCartProduct, Param.deleteCartProduct(sessionManager.getEventId(), sessionManager.getEventType(), product_id, sessionManager.getUserId(), sessionManager.getToken()), 0, true, this);
        //  new VolleyRequest((Activity) context, MyUrls.deleteCartProduct, Param.deleteCartProduct(sessionManager.getEventId(),sessionManager.getEventType(),product_id,sessionManager.getUserId(),sessionManager.getToken()), 0, true, this);
    }

    private void updateCart() {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.update_cartQty, Param.update_cartQty(sessionManager.getEventId(), sessionManager.getEventType(), jArrayproduct.toString(), sessionManager.getUserId(), sessionManager.getToken()), 1, true, this);
    }


    @Override
    public int getItemCount() {
        return cartDetailArrayList.size();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsondelete = new JSONObject(volleyResponse.output);
                    Log.d("AITL", jsondelete.toString());

                    if (jsondelete.getString("success").equalsIgnoreCase("true")) {

                        cartDetailArrayList.remove(position);
                        CartDetail_Fragment.rv_viewCart.removeViewAt(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartDetailArrayList.size());
                        notifyDataSetChanged();
                        GlobalData.CURRENT_FRAG = GlobalData.CartDetailFragment;
                        ((MainActivity) context).reloadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonupdate = new JSONObject(volleyResponse.output);
                    Log.d("AITL ResPonse Update", jsonupdate.toString());
                    GlobalData.CURRENT_FRAG = GlobalData.CartDetailFragment;
                    ((MainActivity) context).reloadFragment();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name, unit_price, sub_total;
        ImageView btn_minus, btn_plus, img_remove;
        EditText edt_qty;

        public ViewHolder(View itemView) {
            super(itemView);
            product_name = (TextView) itemView.findViewById(R.id.product_name);
            unit_price = (TextView) itemView.findViewById(R.id.unit_price);
            sub_total = (TextView) itemView.findViewById(R.id.sub_total);

            btn_minus = (ImageView) itemView.findViewById(R.id.btn_minus);
            btn_plus = (ImageView) itemView.findViewById(R.id.btn_plus);
            img_remove = (ImageView) itemView.findViewById(R.id.img_remove);

            edt_qty = (EditText) itemView.findViewById(R.id.edt_qty);

        }
    }
}
