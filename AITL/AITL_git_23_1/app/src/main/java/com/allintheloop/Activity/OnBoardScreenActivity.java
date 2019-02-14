package com.allintheloop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.allintheloop.Adapter.HomeImagePagerAdpater;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.CustomViewPager;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class OnBoardScreenActivity extends AppCompatActivity {

    SessionManager sessionManager;
    CustomViewPager home_viewPager;
    HomeImagePagerAdpater homeImagePagerAdpater;
    ArrayList<Exhibitor_DetailImage> arrayList;
    int sec;
    Timer homeswipeTimer, pagertimer;
    int currentPage = 0;
    int NUM_PAGES = 0;
    int miliSec;
    String change_screen_on_seconds = "", change_on_tap = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board_screen);
        sessionManager = new SessionManager(OnBoardScreenActivity.this);
        home_viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        arrayList = new ArrayList<>();
        try {
            if (!(sessionManager.onBoradData.equalsIgnoreCase(""))) {


                JSONObject jsonObject = new JSONObject(sessionManager.onBoradData);
                sec = Integer.parseInt(jsonObject.getString("seconds"));
                miliSec = sec * 1000;
                change_screen_on_seconds = jsonObject.getString("change_screen_on_seconds");
                change_on_tap = jsonObject.getString("change_on_tap");
                JSONArray jArrayImage = jsonObject.getJSONArray("o_screen");
                for (int i = 0; i < jArrayImage.length(); i++) {
                    arrayList.add(new Exhibitor_DetailImage(jArrayImage.get(i).toString()));

                }
                if (arrayList.size() == 0) {
                    home_viewPager.setVisibility(View.GONE);
//                    img_frame.show();
                } else {
                    home_viewPager.setVisibility(View.VISIBLE);
//                    img_frame.show();
                    homeImagePagerAdpater = new HomeImagePagerAdpater(OnBoardScreenActivity.this, arrayList);
                    home_viewPager.setAdapter(homeImagePagerAdpater);
                }


                NUM_PAGES = arrayList.size();
                final Handler handler = new Handler();

                if (change_screen_on_seconds.equalsIgnoreCase("1")) {
                    home_viewPager.setPagingEnabled(false);
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == NUM_PAGES) {
                                if (homeswipeTimer != null)
                                    homeswipeTimer.cancel();
                                if (pagertimer != null)
                                    pagertimer.cancel();
                                if (sessionManager.getEventType().equalsIgnoreCase("3")) {
                                    startActivity(new Intent(OnBoardScreenActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    if (sessionManager.getEventType().equalsIgnoreCase("4")) {
                                        eventType4Data();
                                    } else {
                                        eventTypeOneTWoData();
                                    }
                                }
                            }
                            home_viewPager.setCurrentItem(currentPage++, true);
                        }
                    };
                    homeswipeTimer = new Timer();
                    homeswipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 0, miliSec);
                }

                if (change_on_tap.equalsIgnoreCase("1")) {
                    home_viewPager.setPagingEnabled(true);
                    home_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }

                        @Override
                        public void onPageSelected(int position) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                            if (state == ViewPager.SCROLL_STATE_DRAGGING && NUM_PAGES - 1 == home_viewPager.getCurrentItem()) {

                                if (homeswipeTimer != null)
                                    homeswipeTimer.cancel();

                                if (sessionManager.getEventType().equalsIgnoreCase("3")) {
                                    startActivity(new Intent(OnBoardScreenActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    if (sessionManager.getEventType().equalsIgnoreCase("4")) {
                                        eventType4Data();
                                    } else {
                                        eventTypeOneTWoData();
                                    }
                                }
                            }

                        }
                    });
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void eventType4Data() {
        if (sessionManager.isLogin()) {
            startActivity(new Intent(OnBoardScreenActivity.this, MainActivity.class));
            finish();
        } else {

            if (sessionManager.get_show_login_screen().equalsIgnoreCase("1")) {
                if (GlobalData.isNetworkAvailable(getApplicationContext())) {
                    startActivity(new Intent(OnBoardScreenActivity.this, LoginMainScreen.class));
                    finish();
                } else {
                    ToastC.show(getApplicationContext(), getString(R.string.noInernet));
                }
            } else {
                startActivity(new Intent(OnBoardScreenActivity.this, MainActivity.class));
                finish();
            }
        }
    }

    private void eventTypeOneTWoData() {
        if (sessionManager.isLogin()) {
            startActivity(new Intent(OnBoardScreenActivity.this, MainActivity.class));
            finish();
        } else {
            if (GlobalData.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(OnBoardScreenActivity.this, LoginMainScreen.class));
                finish();
            } else {
                ToastC.show(getApplicationContext(), getString(R.string.noInernet));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
