package com.allintheloop.Adapter.Attendee;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Bean.Attendee.AttendeeDetailShare;
import com.allintheloop.Fragment.AttandeeFragments.Attendance_Detail_Fragment;
import com.allintheloop.Fragment.ExhibitorFragment.Exhibitor_Detail_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 7/12/16.
 */

public class AttendeeDetailPagerAdapter extends PagerAdapter implements VolleyInterface {
    Context context;
    ArrayList<AttendeeDetailShare> attendeeDetailShares;
    ProgressBar progressBar1;
    SessionManager sessionManager;
    TextView txt_email, txt_number, txt_conutry;
    Button btn_saveDevice, btn_reject;
    String tag;

    public AttendeeDetailPagerAdapter(Context context, ArrayList<AttendeeDetailShare> attendeeDetailShares) {
        this.context = context;
        this.attendeeDetailShares = attendeeDetailShares;
        sessionManager = new SessionManager(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.attendee_detailshare_pageradapter, container, false);
        final AttendeeDetailShare attendeeDetailShareObj = attendeeDetailShares.get(position);
        txt_email = (TextView) viewItem.findViewById(R.id.txt_email);
        txt_number = (TextView) viewItem.findViewById(R.id.txt_number);
        txt_conutry = (TextView) viewItem.findViewById(R.id.txt_conutry);
        btn_saveDevice = (Button) viewItem.findViewById(R.id.btn_saveDevice);
        btn_reject = (Button) viewItem.findViewById(R.id.btn_reject);

        tag = attendeeDetailShareObj.getTag();
        if (attendeeDetailShareObj.getEmailId().equalsIgnoreCase("")) {
            txt_email.setVisibility(View.GONE);
        } else {
            txt_email.setVisibility(View.VISIBLE);
            txt_email.setText("Email: " + attendeeDetailShareObj.getEmailId());
        }

        if (attendeeDetailShareObj.getCountry().equalsIgnoreCase("")) {
            txt_conutry.setVisibility(View.GONE);
        } else {
            txt_conutry.setVisibility(View.VISIBLE);
            txt_conutry.setText("Country: " + attendeeDetailShareObj.getCountry());
        }

        if (attendeeDetailShareObj.getPhoneNumber().equalsIgnoreCase("")) {
            txt_number.setVisibility(View.GONE);
        } else {
            txt_number.setVisibility(View.VISIBLE);
            txt_number.setText("Phone No: " + attendeeDetailShareObj.getPhoneNumber());
        }

        btn_saveDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(context)) {
                    saveTodevice(attendeeDetailShareObj.getAttendeeId());
                } else {
                    ToastC.show(context, "No Internet Connection");
                }
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(context)) {
                    rejectTodevice(attendeeDetailShareObj.getAttendeeId());
                } else {
                    ToastC.show(context, "No Internet Connection");
                }
            }
        });

        container.addView(viewItem, 0);
        return viewItem;
    }

    private void saveTodevice(String attendeeId) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.approvedshareContactDetail, Param.shareContactInformation(sessionManager.getEventId(), attendeeId, sessionManager.getUserId()), 0, true, this);
    }

    private void rejectTodevice(String attendeeId) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.rejectshareContactDetail, Param.shareContactInformation(sessionManager.getEventId(), attendeeId, sessionManager.getUserId()), 0, true, this);
    }

    @Override
    public int getCount() {
        return attendeeDetailShares.size();
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

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
