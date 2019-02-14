package com.allintheloop.Fragment;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Adapter.NotificationListingAdapter;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.NotificationListingData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationListing_Fragment extends Fragment implements VolleyInterface {


    RecyclerView rv_viewNotification;
    TextView no_data;
    ImageView imageView2;
    SessionManager sessionManager;
    ArrayList<NotificationListingData> notificationListingDatas;
    NotificationListingAdapter listingAdapter;
    private Paint p;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "NotificationListing";
    DefaultLanguage.DefaultLang defaultLang;

    public NotificationListing_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification_listing, container, false);

        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        rv_viewNotification = (RecyclerView) rootView.findViewById(R.id.rv_viewNotification);
        no_data = (TextView) rootView.findViewById(R.id.no_data);
        imageView2 = (ImageView) rootView.findViewById(R.id.imageView2);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        p = new Paint();
        sessionManager.strModuleId = "Notification Center";
        initSwipe();

        return rootView;
    }


    private void getNotificationList() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getnotificationListing, Param.getNotificationListing(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONArray jsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        Log.d("AITL   Oflline", jsonArray.toString());
                        loadData(jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                rv_viewNotification.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                no_data.setText(defaultLang.getNotificationsNoNotificationsFound());
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getNotificationList();
    }


    private void loadData(JSONArray jArrayData) {
        try {
            notificationListingDatas = new ArrayList<>();
            for (int i = 0; i < jArrayData.length(); i++) {
                JSONObject jsonObject1 = jArrayData.getJSONObject(i);
                notificationListingDatas.add(new NotificationListingData(jsonObject1.getString("id"), jsonObject1.getString("title"), jsonObject1.getString("content")));

            }
            if (notificationListingDatas.size() == 0) {

                rv_viewNotification.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.VISIBLE);
                no_data.setText(defaultLang.getNotificationsNoNotificationsFound());
            } else {
                imageView2.setVisibility(View.GONE);
                rv_viewNotification.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
                listingAdapter = new NotificationListingAdapter(notificationListingDatas, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                rv_viewNotification.setLayoutManager(mLayoutManager);
                rv_viewNotification.setItemAnimator(new DefaultItemAnimator());
                rv_viewNotification.setAdapter(listingAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                NotificationListingData notificationData = notificationListingDatas.get(position);

                if (direction == ItemTouchHelper.LEFT) {
                    listingAdapter.removeItem(position, notificationData.getId());
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;


                    p.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_rubbish_bin);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv_viewNotification);
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        JSONArray jArrayData = jsonObject.getJSONArray("data");
                        if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                            sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jArrayData.toString(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jArrayData.toString(), tag);
                        }
                        loadData(jArrayData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
