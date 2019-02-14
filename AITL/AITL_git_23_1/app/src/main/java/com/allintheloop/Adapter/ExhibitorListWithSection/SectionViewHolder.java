package com.allintheloop.Adapter.ExhibitorListWithSection;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;

/**
 * Created by nteam on 7/11/17.
 */

public class SectionViewHolder extends RecyclerView.ViewHolder {

    public BoldTextView name;
    public CardView cardHeader;

    public SectionViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.sectionHeader);
        cardHeader = (CardView) itemView.findViewById(R.id.cardHeader);
    }
}
