package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allintheloop.Fragment.ActivityModule.ActivitySharePost_Fragment;
import com.allintheloop.Fragment.PrivateMessage.PrivateMessageDetail_Fragment;
import com.bumptech.glide.Glide;
import com.allintheloop.Bean.GallaryBean;
import com.allintheloop.Fragment.FundraisingModule.AddItem_Fragment;
import com.allintheloop.Fragment.FundraisingModule.EditItemFragment;
import com.allintheloop.Fragment.Photo_Fragment;
import com.allintheloop.Fragment.PublicMessage_Fragment;
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


/**
 * Created by KRUNAL on 6/1/2016.
 */
public class GallaryAdepter extends RecyclerView.Adapter<GallaryAdepter.MyViewHolder> implements VolleyInterface {

    ArrayList<GallaryBean> data;
    Context con;
    String flag;
    MyViewHolder holder;
    SessionManager sessionManager;


    public GallaryAdepter(ArrayList<GallaryBean> data, Context con) {
        this.data = data;
        this.con = con;
        sessionManager = new SessionManager(con);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallary_image_recycle, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final GallaryBean gallaryBean = data.get(position);
        this.holder = holder;
        flag = data.get(position).getStatus();


        if (flag.equalsIgnoreCase("EditItemFragment")) {
            holder.btn_landscape.setVisibility(View.VISIBLE);

            if (gallaryBean.getImg_status().equalsIgnoreCase("1")) {
                holder.btn_landscape.setImageDrawable(con.getResources().getDrawable(R.drawable.ic_landscape_green));
                holder.imgbtndelete.setVisibility(View.GONE);
            } else {
                holder.btn_landscape.setImageDrawable(con.getResources().getDrawable(R.drawable.ic_landscape));
                holder.imgbtndelete.setVisibility(View.VISIBLE);
            }

            if (sessionManager.gallryimg.equalsIgnoreCase("gallryimg")) {
                Glide.with(con).load(data.get(position).getImages()).placeholder(R.drawable.defult_attende).centerCrop().into(holder.imggallaryimages);
            } else {
                Log.d("AITL AdapterImg", data.get(position).getImages());
                Log.d("AITL AdapterImg WithURl", MyUrls.Fund_Imgurl + data.get(position).getImages());

                Glide.with(con).load(MyUrls.Fund_Imgurl + data.get(position).getImages()).placeholder(R.drawable.defult_attende).centerCrop().into(holder.imggallaryimages);
            }

            holder.btn_landscape.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thumLineImg(gallaryBean.getImages());
                }
            });
        } else {
            holder.btn_landscape.setVisibility(View.GONE);
            Glide.with(con).load(data.get(position).getImages()).placeholder(R.drawable.defult_attende).centerCrop().into(holder.imggallaryimages);
        }

        holder.imgbtndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag.equalsIgnoreCase("private_message")) {

                    data.remove(position);
                    PrivateMessageDetail_Fragment.recycler_img_gallary_picker.removeViewAt(position);
                    PrivateMessageDetail_Fragment.selectImages.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());

                    PrivateMessageDetail_Fragment.counter--;
                    notifyDataSetChanged();
//                        txtphoto.setVisibility(View.VISIBLE);

                } else if (flag.equalsIgnoreCase("photo_section")) {

                    data.remove(position);
                    Photo_Fragment.recycler_img_gallary_picker.removeViewAt(position);
                    Photo_Fragment.selectImages.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());

                    Photo_Fragment.counter--;
                    notifyDataSetChanged();
//                        txtphoto.setVisibility(View.VISIBLE);

                } else if (flag.equalsIgnoreCase("public_message")) {

                    data.remove(position);
                    PublicMessage_Fragment.recycler_img_gallary_picker.removeViewAt(position);
                    PublicMessage_Fragment.selectImages.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());

                    PublicMessage_Fragment.counter--;
                    notifyDataSetChanged();
//                        txtphoto.setVisibility(View.VISIBLE);

                }
                if (flag.equalsIgnoreCase("addItemFragment")) {

                    data.remove(position);
                    AddItem_Fragment.recycler_img_gallary_picker.removeViewAt(position);
                    AddItem_Fragment.selectImages.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());

                    AddItem_Fragment.counter--;
                    notifyDataSetChanged();
//                        txtphoto.setVisibility(View.VISIBLE);

                } else if (flag.equalsIgnoreCase("EditItemFragment")) {

                    data.remove(position);
                    EditItemFragment.recycler_img_gallary_picker.removeViewAt(position);
                    EditItemFragment.selectImages.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());

                    EditItemFragment.counter--;
                    notifyDataSetChanged();
//                        txtphoto.setVisibility(View.VISIBLE);

                } else if (flag.equalsIgnoreCase("ActivityModule")) {

                    data.remove(position);
                    ActivitySharePost_Fragment.recycler_img_gallary_picker.removeViewAt(position);
                    ActivitySharePost_Fragment.selectImages.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, data.size());

                    ActivitySharePost_Fragment.counter--;
                    notifyDataSetChanged();
//                        txtphoto.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void thumLineImg(String name) {
        if (GlobalData.isNetworkAvailable(con)) {
            new VolleyRequest((Activity) con, VolleyRequest.Method.POST, MyUrls.item_thumblineImg, Param.thumbline_img(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), sessionManager.getUserId(), sessionManager.itemProduct_id, name), 0, true, this);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        GlobalData.CURRENT_FRAG = GlobalData.EditItemFragment;
                        ((MainActivity) con).loadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imggallaryimages, imgbtndelete, btn_landscape;

        public MyViewHolder(View itemView) {
            super(itemView);
            imggallaryimages = (ImageView) itemView.findViewById(R.id.imggallaryimages);
            imgbtndelete = (ImageView) itemView.findViewById(R.id.btndelete);
            btn_landscape = (ImageView) itemView.findViewById(R.id.btn_landscape);
        }
    }
}
