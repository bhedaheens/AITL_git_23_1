package com.allintheloop;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.allintheloop.Bean.GeoLocation.EventBusGeoLocationData;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;
import com.google.android.gms.gcm.GcmListenerService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by nteam on 8/8/16.
 */
public class GCMPushReceiverService extends GcmListenerService {
    String message = "", title = "", sender_name = "", message_type = "", message_id = "";
    PendingIntent pi;
    private SessionManager sessionManager;
    public static final String CHANNEL_ONE_ID = "com.allintheloop.ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";

    @Override
    public void onCreate() {
        super.onCreate();
        sessionManager = new SessionManager(getApplicationContext());
        Log.d("AITL", "NOTIFICATION");

    }

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(String from, Bundle data) {

        //Getting the message from the bundle
        try {
            Log.d("AITL Notification", data.toString());
            if (data.containsKey("message")) {

                JSONObject jsonText = new JSONObject(data.getString("message"));
                message = jsonText.getString("message");
                title = jsonText.getString("title");

                Intent i = new Intent(GCMPushReceiverService.this, MainActivity.class);
                if (data.containsKey("extra")) {
                    if (!(data.getString("extra").equalsIgnoreCase(""))) {
                        JSONObject jsonExtra = new JSONObject(data.getString("extra"));
                        if (jsonExtra.has("sender_name"))
                            sender_name = jsonExtra.getString("sender_name");
                        if (jsonExtra.has("message_type")) {
                            message_type = jsonExtra.getString("message_type");
                            i.putExtra("message_type", message_type);
                        }
                        if (jsonExtra.has("message_id")) {
                            message_id = jsonExtra.getString("message_id");
                            sessionManager.slilentAuctionP_id = message_id;
                            sessionManager.private_senderId = message_id;
                            sessionManager.mettingId = message_id;
                            i.putExtra("product_id", message_id);
                        }
                    } else {
                        if (data.containsKey("profile")) {

                            if (!(data.getString("profile").equalsIgnoreCase(""))) {
                                JSONObject jsonExtra = new JSONObject(data.getString("profile"));
                                sessionManager.setUserProfile(jsonExtra.getString("image"));
                                sessionManager.setNotification_Userid(jsonExtra.getString("user_id"));
                                sessionManager.setNotification_role(jsonExtra.getString("role"));
                                GlobalData.UpdateProfile(getApplicationContext());
                            }
                        }
                    }
                }


                if (message_type.equalsIgnoreCase("updateGCMID")) {
                    if (isAppIsInBackground(getApplicationContext())) {
                        sessionManager.isLoginNoti = true;
                    } else {
                        GlobalData.logoutNotification(getApplicationContext());
                    }
                } else if (message_type.equalsIgnoreCase("presentation")) {
                    GlobalData.updatePresantation(getApplicationContext());
                } else if (message_type.equalsIgnoreCase("QAQusetion")) {
                    GlobalData.updateQandADetailModule(getApplicationContext());
                } else if (message_type.equalsIgnoreCase("updateGeoNoti")) {
                    EventBus.getDefault().post(new EventBusGeoLocationData(""));
                } else {
                    if (!(isAppIsInBackground(getApplicationContext()))) {
                        sessionManager.notiTitle = title;
                        sessionManager.notiMessage = message;
                        if (message_type.equalsIgnoreCase("Private") || message_type.equalsIgnoreCase("RequestMeeting") || message_type.equalsIgnoreCase("Outbid Auction") || message_type.equalsIgnoreCase("AttendeeRequestMeeting") || message_type.equalsIgnoreCase("cms") || message_type.equalsIgnoreCase("Attendee") || message_type.equalsIgnoreCase("ModeratorRequestMeeting") || message_type.equalsIgnoreCase("custom_page")) {
                            sessionManager.notiMessageType = message_type;
                            sessionManager.slilentAuctionP_id = message_id;
                            sessionManager.mettingId = message_id;
                            sessionManager.Notificationmenu_id = message_id;
                            GlobalData.setNotificationwithAction(getApplicationContext());
                        } else {
                            GlobalData.setNotificationsimpleDialog(getApplicationContext());
                        }
                    }

                    if (sessionManager.isLogin()) {
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationCompat.Builder b;
                        NotificationChannel notificationChannel = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                                    CHANNEL_ONE_NAME, notificationManager.IMPORTANCE_HIGH);

                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            notificationChannel.setShowBadge(true);
                            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                            notificationManager.createNotificationChannel(notificationChannel);

                            b = new NotificationCompat.Builder(this, CHANNEL_ONE_ID);
                        } else {
                            b = new NotificationCompat.Builder(this);
                        }

                        GlobalData.UpdateNotiCounter(getApplicationContext());
                        pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                        b.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(getNotificationIcon(b))
                                .setTicker(sessionManager.getEventName())
                                .setContentTitle(title)
                                .setContentText(message)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                .setContentIntent(pi)
                                .setContentInfo(sender_name);


                        notificationManager.notify((int) System.currentTimeMillis(), b.build());


                    } else {

                        NotificationCompat.Builder b = new NotificationCompat.Builder(this);

                        b.setAutoCancel(true)
                                .setDefaults(Notification.DEFAULT_ALL)
                                .setWhen(System.currentTimeMillis())
                                .setSmallIcon(getNotificationIcon(b))
                                .setTicker(sessionManager.getEventName())
                                .setContentTitle(title)
                                .setContentText(message)
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                                .setContentIntent(pi)
                                .setContentInfo(sender_name);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify((int) System.currentTimeMillis(), b.build());

                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // int color = 0x008000;
            notificationBuilder.setColor(Color.parseColor("#5b8fc1"));
            return R.drawable.notification;

        } else {
            return R.drawable.notification;
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
