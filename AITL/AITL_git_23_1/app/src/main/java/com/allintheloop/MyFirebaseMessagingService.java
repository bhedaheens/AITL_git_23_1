package com.allintheloop;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.allintheloop.Activity.Splash_Activity;
import com.allintheloop.Bean.GeoLocation.EventBusGeoLocationData;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService implements VolleyInterface {

    private static final String TAG = "MyFirebaseInstanceIDSer";

    String message = "", title = "", sender_name = "", message_type = "", message_id = "", notification_id;
    PendingIntent pi;
    private SessionManager sessionManager;
    public static final String CHANNEL_ONE_ID = "com.allintheloop.ONE";
    public static final String CHANNEL_ONE_NAME = "Channel One";
    Intent i;

    @Override
    public void onCreate() {
        sessionManager = new SessionManager(getApplicationContext());
        super.onCreate();
    }

    @Override
    public void onNewToken(String s) {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        try {

            if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
                i = new Intent(MyFirebaseMessagingService.this, MainActivity.class);

                message_type = remoteMessage.getData().get("msg_type");
                message_id = remoteMessage.getData().get("message_id");
                title = remoteMessage.getData().get("title");
                message = remoteMessage.getData().get("body");
                notification_id = remoteMessage.getData().get("notification_id");

                i.putExtra("message_type", message_type);
                i.putExtra("product_id", message_id);
                i.putExtra("notification_id", notification_id);
                sessionManager.slilentAuctionP_id = message_id;
                sessionManager.private_senderId = message_id;
                sessionManager.mettingId = message_id;


                if (message_type.equalsIgnoreCase("cms")) {
                    new VolleyRequest(MyFirebaseMessagingService.this, VolleyRequest.Method.POST, MyUrls.NotificationUpdateLogs, Param.notificationUpdateLog(sessionManager.getUserId(), sessionManager.getEventId(), notification_id, "1"), 1, false, this);
                    sessionManager.notificationId = notification_id;
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
                    sendNotification();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendNotification() {

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

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {

            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("NotificationUpdateLog", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
