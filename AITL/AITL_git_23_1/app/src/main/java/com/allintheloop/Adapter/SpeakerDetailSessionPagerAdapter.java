package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.Speaker.SpeakerAgendaSessionData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 3/5/17.
 */

public class SpeakerDetailSessionPagerAdapter extends PagerAdapter {

    ArrayList<SpeakerAgendaSessionData> sessionDatas;
    Context context;
    BoldTextView sessionName, txt_sessionType, txt_sessionTime, txt_sessionDate, txt_sessionLocation;
    SessionManager sessionManager;
    LinearLayout linear_speakerAgenda;

    public SpeakerDetailSessionPagerAdapter(ArrayList<SpeakerAgendaSessionData> sessionDatas, Context context) {
        this.sessionDatas = sessionDatas;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public int getCount() {
        return sessionDatas.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.speakerdetail_sessionlistadapter, container, false);
        final SpeakerAgendaSessionData imgObj = sessionDatas.get(position);
        sessionName = (BoldTextView) viewItem.findViewById(R.id.sessionName);
        txt_sessionTime = (BoldTextView) viewItem.findViewById(R.id.txt_sessionTime);
        txt_sessionDate = (BoldTextView) viewItem.findViewById(R.id.txt_sessionDate);
        txt_sessionType = (BoldTextView) viewItem.findViewById(R.id.txt_sessionType);
        txt_sessionLocation = (BoldTextView) viewItem.findViewById(R.id.txt_sessionLocation);
        linear_speakerAgenda = (LinearLayout) viewItem.findViewById(R.id.linear_speakerAgenda);

        sessionName.setText(imgObj.getHeading());
        txt_sessionTime.setText(imgObj.getStart_time());
        txt_sessionDate.setText(imgObj.getStart_date());
        txt_sessionType.setText(imgObj.getType());
        txt_sessionLocation.setText(imgObj.getLocation());


        linear_speakerAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.agenda_id(imgObj.getId());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                ((MainActivity) context).loadFragment();
            }
        });

        viewItem.setTag(imgObj);
        container.addView(viewItem, 0);
        return viewItem;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }

}
