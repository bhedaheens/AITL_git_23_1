package com.allintheloop.Adapter.ExhibitorListWithSection;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by nteam on 1/5/18.
 */

public class ExhibitorParentCategoryGroupChildViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;
    public ImageView ivCheck;

    public ExhibitorParentCategoryGroupChildViewHolder(View itemView) {

        super(itemView);
        mTextView = (TextView) itemView.findViewById(R.id.txtName);
        ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);
    }
}
