package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.Speaker.SpeakerDocumentListing;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 5/5/17.
 */

public class SpeakerDocumentListingAdapter extends RecyclerView.Adapter<SpeakerDocumentListingAdapter.ViewHolder> {
    ArrayList<SpeakerDocumentListing> speakerDocumentListings;
    Context context;


    public SpeakerDocumentListingAdapter(ArrayList<SpeakerDocumentListing> speakerDocumentListings, Context context) {
        this.speakerDocumentListings = speakerDocumentListings;
        this.context = context;
    }


    @Override
    public SpeakerDocumentListingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_speaker_document, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SpeakerDocumentListingAdapter.ViewHolder holder, int position) {
        holder.name.setText(speakerDocumentListings.get(position).getTitle());
        holder.speaker_Doc_image.setColorFilter(Color.parseColor("#7E7C7E"));

    }

    @Override
    public int getItemCount() {
        return speakerDocumentListings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView speaker_Doc_image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            speaker_Doc_image = (ImageView) itemView.findViewById(R.id.speaker_Doc_image);
        }
    }
}
