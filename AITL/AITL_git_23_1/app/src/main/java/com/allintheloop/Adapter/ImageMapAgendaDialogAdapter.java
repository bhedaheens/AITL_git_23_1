package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.Map.MapImageSession;
import com.allintheloop.Fragment.MapModule.Map_Detail_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;


/**
 * Created by nteam on 20/9/16.
 */
public class ImageMapAgendaDialogAdapter extends RecyclerView.Adapter<ImageMapAgendaDialogAdapter.ViewHolder> {
    ArrayList<MapImageSession> mapImageSessions;
    Context context;
    SessionManager sessionManager;

    public ImageMapAgendaDialogAdapter(ArrayList<MapImageSession> mapImageSessions, Context context) {
        this.mapImageSessions = mapImageSessions;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_imagemapagenda_dialog, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MapImageSession mapSession = mapImageSessions.get(position);
        holder.txt_heading.setText(mapSession.getStr_sHeading());
        holder.txt_time.setText(mapSession.getStr_sSDate() + "-" + mapSession.getStr_sTime());


        holder.layout_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("AgendaID", mapSession.getStr_sId());
                Log.d("AITL FRSGMENT BACK", " " + GlobalData.CURRENT_FRAG);
                sessionManager.agenda_id(mapSession.getStr_sId());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                ((MainActivity) context).loadFragment();
                Map_Detail_Fragment.agenda_dialog.dismiss();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mapImageSessions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_heading, txt_time;
        LinearLayout layout_data;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_heading = (TextView) itemView.findViewById(R.id.txt_heading);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            layout_data = (LinearLayout) itemView.findViewById(R.id.layout_data);
        }
    }
}
