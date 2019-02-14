package com.allintheloop.Adapter.Agenda;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.AgendaData.AgendaSpeaker_SupporterLink;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 5/5/17.
 */

public class AgendaSpeakerSupporterLink extends RecyclerView.Adapter<AgendaSpeakerSupporterLink.ViewHolder> {
    ArrayList<AgendaSpeaker_SupporterLink> agendaSpeaker_supporterLinks;
    Context context;
    SessionManager sessionManager;

    public AgendaSpeakerSupporterLink(ArrayList<AgendaSpeaker_SupporterLink> agendaSpeaker_supporterLinks, Context context) {
        this.agendaSpeaker_supporterLinks = agendaSpeaker_supporterLinks;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_agenda_supportinglisting, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AgendaSpeaker_SupporterLink ageObj = agendaSpeaker_supporterLinks.get(position);
        holder.doc_name.setText(ageObj.getValue());
        holder.type_name.setText(ageObj.getType());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(13.0f);

        if (ageObj.getType().equalsIgnoreCase("presentation")) {
            holder.type_name.setText("Presentations");
            holder.agenda_Doc_image.setImageDrawable(context.getResources().getDrawable(R.drawable.agenda_presantation));
            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));

                holder.agenda_Doc_image.setBackgroundDrawable(drawable);

            } else {
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                holder.agenda_Doc_image.setBackgroundDrawable(drawable);
            }

        } else if (ageObj.getType().equalsIgnoreCase("survey")) {
            holder.type_name.setText("Survey");
            holder.agenda_Doc_image.setImageDrawable(context.getResources().getDrawable(R.drawable.agenda_surveypoll));
            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                holder.agenda_Doc_image.setBackgroundDrawable(drawable);

            } else {
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                holder.agenda_Doc_image.setBackgroundDrawable(drawable);
            }
        } else if (ageObj.getType().equalsIgnoreCase("qa_session")) {
            holder.type_name.setText("Q&A");
            holder.agenda_Doc_image.setImageDrawable(context.getResources().getDrawable(R.drawable.agenda_qanda));
            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                holder.agenda_Doc_image.setBackgroundDrawable(drawable);


            } else {
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                holder.agenda_Doc_image.setBackgroundDrawable(drawable);
            }
        } else if (ageObj.getType().equalsIgnoreCase("document")) {
            holder.type_name.setText("Document");
            holder.agenda_Doc_image.setImageDrawable(context.getResources().getDrawable(R.drawable.agenda_docs));
            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                holder.agenda_Doc_image.setBackgroundDrawable(drawable);

            } else {
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                holder.agenda_Doc_image.setBackgroundDrawable(drawable);
            }
        }
    }

    @Override
    public int getItemCount() {
        return agendaSpeaker_supporterLinks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView agenda_Doc_image;
        TextView doc_name, type_name;

        public ViewHolder(View itemView) {
            super(itemView);
            agenda_Doc_image = (ImageView) itemView.findViewById(R.id.agenda_Doc_image);
            doc_name = (TextView) itemView.findViewById(R.id.doc_name);
            type_name = (TextView) itemView.findViewById(R.id.type_name);
        }
    }
}
