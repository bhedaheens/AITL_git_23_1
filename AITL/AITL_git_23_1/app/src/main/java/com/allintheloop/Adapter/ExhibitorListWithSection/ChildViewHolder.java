package com.allintheloop.Adapter.ExhibitorListWithSection;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nteam on 7/11/17.
 */

public class ChildViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView imageView;
    public TextView txt_profileName;
    public BoldTextView userDesc, userName;
    public ImageView user_sqrimage, img_star;
    public RelativeLayout layout_relative;
    public CardView app_back;

    public ChildViewHolder(View itemView) {
        super(itemView);
        imageView = (CircleImageView) itemView.findViewById(R.id.user_image);
        user_sqrimage = (ImageView) itemView.findViewById(R.id.user_sqrimage);
        userName = itemView.findViewById(R.id.user_name);
        userDesc = itemView.findViewById(R.id.user_desc);
        txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
        layout_relative = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
        app_back = (CardView) itemView.findViewById(R.id.app_back);
        img_star = (ImageView) itemView.findViewById(R.id.img_star);
    }
}
