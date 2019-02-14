package com.allintheloop.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.allintheloop.BuildConfig;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by nteam on 25/4/16.
 */
public class GlobalData {
    public final static int HOME_FRAGMENT = 1;
    public final static int CMS_FRAGMENT_TEST1 = 2;
    public final static int AGENDA_FRAGMENT = 6;
    public final static int PRIVATE_MESSAGE = 8;
    public final static int NOTES_FRAGMENT = 9;
    public final static int EXHIBITOR_FRAGMENT = 11;
    public final static int PROFILE_FRAGMENT = 12;
    public final static int View_Agenda_Fragment = 13;
    public final static int SPEAKER_FRAGMENT = 15;
    public final static int SOCIAL_FRAGMENT = 16;
    public final static int Webview_fragment = 17;
    public final static int Map_fragment = 18;
    public final static int Map_Detail_Fragment = 19;
    public final static int View_Note_Fragment = 20;
    public final static int View_UserWise_Agenda = 21;
    public final static int Attendance_Detail_Fragment = 22;
    public final static int Exhibitor_Detail_Fragment = 23;
    public final static int Speaker_Detail_Fragment = 24;
    public final static int Presantation_Fragment = 25;
    public final static int Document_Fragment = 27;
    public final static int Fundrising_Home = 26;
    public final static int Document_file_Fragment = 28;
    public final static int Survey_Fragment = 29;
    public final static int Sponsor_Fragment = 30;
    public final static int Sponsor_Detail_Fragment = 31;
    public final static int Presantation_Detail_Fragment = 32;
    public final static int PublicMessage_Fragment = 33;
    public final static int Photo_Fragment = 34;
    public final static int Silent_Auction = 35;
    public final static int Online_shop = 36;
    public final static int live_Auction = 37;
    public final static int DonateFor_veteran = 38;
    public final static int SilentAuctionDetail_Fragment = 39;
    public final static int LiveAuction_Detail = 40;
    public final static int OnlineShop_Detail = 41;
    public final static int PledgeDetailFragment = 42;
    public final static int CartDetailFragment = 43;
    public final static int CheckOut_Fragment = 44;
    public final static int TweitterFeed = 45;
    public final static int fund_donation_fragment = 46;
    public final static int Order_Fragment = 47;
    public final static int Pending_AgendaFragment = 48;
    public final static int Agenda_BookStatus = 49;
    public final static int ItemListFragment = 50;
    public final static int AdditemFragment = 51;
    public final static int EditItemFragment = 52;
    public final static int activityFeed = 53;
    public final static int activityDetailFeed = 54;
    public final static int InstagramFeed = 55;
    public final static int CheckIn_Portal = 56;
    public final static int attndeeShareContactFragment = 57;
    public final static int attndeeShareContactFragmentTag = 58;
    public final static int exhibitorRequestMettingFragment = 59;
    public final static int notificationListingFragment = 60;
    public final static int viewSuggestedTimeList = 61;
    public final static int facebookFeed = 62;
    public final static int attendeeRequestMettingFragment = 63;
    public final static int FavoriteList_Fragment = 64;
    public final static int QAList_Fragment = 65;
    public final static int QAListDetail_Fragment = 66;
    public final static int PrivateMessageSenderWiseDetail = 67;
    public final static int getAllCommonData = 68;
    public final static int privateMessageViewProfileFragment = 69;
    public final static int VirtualSuperMarket = 70;
    public final static int BeaconFinder_Fragment = 71;
    public final static int getAllBeaconList = 72;
    public final static int TwitterDetailFragment = 73;
    public final static int OpenPdfFragment = 74;
    public final static int moderatorRequestMettingFragment = 75;
    public final static int Attendee_EditProfile_Fragment = 76;
    public final static int SurveyCategoryWiseFragment = 77;
    public final static int Gamification_Fragment = 78;
    public final static int NotesDetail_Fragment = 79;
    public final static int ExhibiorLead_Fragment = 80;
    public final static int NewMapDetail_Fragment = 81;
    public final static int PhotoFilter_Fragment = 82;
    public final static int ExhibitorCategoryWiseData = 83;
    public final static int MapGroupWiseFragment = 84;
    public final static int SponsorGroupWiseFragment = 85;
    public final static int QandAGroupWiseFragment = 86;
    public final static int AgendaGroupListFragment = 87;
    public final static int AgendaGroupWiseFragment = 88;
    public final static int CMSGroupListFragment = 89;
    public final static int CMSGroupChildListFragment = 90;
    public final static int CMSListFragment = 91;
    public final static int ExhibitorSubCategoryListFragment = 92;
    public final static int ExhibitorSubCategoryWiseListFragment = 93;
    public final static int requestMettingInvteMoreFragment = 94;
    public final static int QrcodeScannerSharecontactFragment = 95;
    public final static int MatchMakingFragment = 96;
    public final static int AttendeeMainGroupFragment = 97;
    public final static int QaHideQuestionModule_Fragment = 98;
    public final static int QaModeratorInstructionModule_Fragment = 99;
    public final static int ActivitySharePost_Fragment = 100;
    public final static int ActivityCommentView_Fragment = 101;
    public final static int PhotoFilter_seeAllFitlers = 102;
    public final static int PhotoFilter_seeAllPhotos = 103;
    public static final String Update_Profile = "com.allintheloop.updateProfile";
    public static final String Update_presantation = "com.allintheloop.presantation";
    public static final String logoutNoti = "com.allintheloop.logout";
    public static final String NotificationsimpleDialog = "com.allintheloop.simpleDialog";
    public static final String NotificationsimpleDialogwithAction = "com.allintheloop.simpleDialogWithAction";
    public static final String getexhibitorListingData = "com.allintheloop.exhibitorListingData";
    public static final String getSubexhibitorListingData = "com.allintheloop.SubexhibitorListingData";
    public static final String getexhibitorLeadSettingUpdate = "com.allintheloop.exhibitorLeadSettingUpdate";
    public static final String getexhibitorMyLeadUpdate = "com.allintheloop.exhibitorMyLeadUpdate";
    public static final String attendeeFavListingData = "com.allintheloop.attendeeFavListingData";
    public static final String SocketIoException = "com.allintheloop.socketException";
    public static final String UserwiseAgewndaRefresh = "com.allintheloop.userwiseAgendaRefresh";
    public static final String updateQandADetailModule = "com.allintheloop.updateQandADetailModule";
    public static final String updateGamticationModule = "com.allintheloop.GamtificationData";
    public static final String UpdateNoteListing = "com.allintheloop.UpdateNoteListing";
    public static final String UpdateCounterFromNotification = "com.allintheloop.UpdateNotiCounter";
    public static final String PresantationSendMessage = "com.allintheloop.presantationSendMessage";
    public static final String exhibitorMyLeadLoad = "com.arab_health.exhibitorMyLeadLoad";
    public static final String exhibitorMyLeadCount = "com.allintheloop.exhibitorMyLeadCount";
    public static final String updateRequestMeetingInvitedList = "com.allintheloop.exhibitorMyLeadLoad";
    public static final String updateRequestMeetingReloadData = "com.allintheloop.RequestMeetingReloadData";
    public static final String cameraPermission = "com.allintheloop.cameraPermissionBroadCast";
    public static final String exhibitorModuleid = "exhibitor";
    public static final String newactivityReloadedFromSharePost = "com.allintheloop.activity_reloaded";
    public static final String deepLinkbroadcast = "com.allintheloop.deepLinkbroadcast";
    public static final String groupModuleid = "group";
    public static final String cmsModuleid = "cms";
    public static final String mapModuleid = "map";
    public static final String sponsorModuleid = "sponsor";
    public static final String agendaModuleid = "agenda";
    public static final String speakerModuleid = "speaker";
    public static final String exhibitorModuleNo = "3";
    public static final String groupModuleNo = "100";
    public static final String cmsModuleNo = "21";
    public static final String mapModuleNo = "10";
    public static final String sponsorModuleNo = "43";
    public static final String agendaModuleNo = "1";
    public static final String speakerModuleNo = "7";
    public static final String deeplinkHostName = "allintheloop";
    public final static Stack<Integer> Fragment_Stack = new Stack();
    public static String USER_PREFRENCE = "UserSetting";
    public static int temp_Fragment = 0;
    public static int CURRENT_FRAG = HOME_FRAGMENT;
    public static String currentModuleForOnResume = "";
    public static SessionManager sessionManager;

    public static Boolean is_user_agenda = false;

    //    public final static String android_idandroid_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);;
    public static CurrentFragment currentFragment;
    public static int pos;

    // Attendee        4
    // Exhibitor       6
    // Speaker         7
    // Sponsor
    // Organizer       3
    // CheckingPortal  95
    // Moderator      role id 7 and isModeraotr 1
    public static void SocketUpdate(Context context) {
        Intent intent = new Intent(SocketIoException);
        context.sendBroadcast(intent);
    }

    public static void UpdateNoteListingFragment(Context context) {
        Intent intent = new Intent(UpdateNoteListing);
        context.sendBroadcast(intent);
    }

    public static void UpdateProfile(Context context) {
        Intent intent = new Intent(Update_Profile);
        context.sendBroadcast(intent);
    }

    public static void deeplinkBroadCast(Context context, Intent deepintent) {
        Intent intent = new Intent(deepLinkbroadcast);
        intent.putExtra("deepintent", deepintent);
        context.sendBroadcast(intent);
    }

    public static void updatePresantation(Context context) {
        try {
            Intent intent = new Intent(Update_presantation);
            context.sendBroadcast(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void presantationSendMessage(Context context) {
        Intent intent = new Intent(PresantationSendMessage);
        context.sendBroadcast(intent);
    }

    public static void activiyReloadedfromSharePicture(Context context) {
        Intent intent = new Intent(newactivityReloadedFromSharePost);
        context.sendBroadcast(intent);
    }

    public static void logoutNotification(Context context) {
        Intent intent = new Intent(logoutNoti);
        context.sendBroadcast(intent);
    }

    public static void agendaRefresh(Context context) {
        Intent intent = new Intent(UserwiseAgewndaRefresh);
        context.sendBroadcast(intent);
    }

    public static void updateQandADetailModule(Context context) {
        Intent intent = new Intent(updateQandADetailModule);
        context.sendBroadcast(intent);
    }

    public static void UpdateGamicationDara(Context context) {
        Intent intent = new Intent(updateGamticationModule);
        context.sendBroadcast(intent);
    }

    public static void exhibitorListngData(Context context, String exid, String status) {
        Intent intent = new Intent(getexhibitorListingData);
        intent.putExtra("exhi_id", exid);
        intent.putExtra("status", status);
        context.sendBroadcast(intent);
    }

    public static void exhibitorLeadSettingTab(Context context) {
        Intent intent = new Intent(getexhibitorLeadSettingUpdate);
        context.sendBroadcast(intent);
    }

    public static void exhibitorMyLeadTab(Context context) {
        Intent intent = new Intent(getexhibitorMyLeadUpdate);
        context.sendBroadcast(intent);
    }

    public static void exhibitorMyLeadTabLoad(Context context) {
        Intent intent = new Intent(exhibitorMyLeadLoad);
        context.sendBroadcast(intent);
    }

    public static void exhibitorMyLeadCountUpdate(Context context, int size) {
        Intent intent = new Intent(exhibitorMyLeadCount);
        intent.putExtra("count", size);
        context.sendBroadcast(intent);
    }

    public static void updateRequestMeetingInvitedListMetod(Context context) {
        Intent intent = new Intent(updateRequestMeetingInvitedList);
        context.sendBroadcast(intent);
    }

    public static void reuestMeetingReloadData(Context context) {
        Intent intent = new Intent(updateRequestMeetingReloadData);
        context.sendBroadcast(intent);
    }

    public static void cameraPermissionbroadCast(Context context) {
        Intent intent = new Intent(cameraPermission);
        context.sendBroadcast(intent);
    }

    public static void AttendeeFavListngData(Context context, String attnedeeId, String status) {
        Intent intent = new Intent(attendeeFavListingData);
        intent.putExtra("attnedeeId", attnedeeId);
        intent.putExtra("status", status);
        context.sendBroadcast(intent);
    }

    public static void setNotificationsimpleDialog(Context context) {
        Intent intent = new Intent(NotificationsimpleDialog);
        context.sendBroadcast(intent);
    }

    public static void UpdateNotiCounter(Context context) {
        Intent intent = new Intent(UpdateCounterFromNotification);
        context.sendBroadcast(intent);
    }

    public static void setNotificationwithAction(Context context) {
        Intent intent = new Intent(NotificationsimpleDialogwithAction);
        context.sendBroadcast(intent);
    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean checkEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isNullString(String string) {
        try {
            if (string.trim().equalsIgnoreCase("null") || string.trim() == null || string.trim().length() < 0 || string.trim().equals("")) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    public static String getURLFromParams(String url, Map<String, String> params) {
        Iterator entries = params.entrySet().iterator();
        int i = 0;
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (i == 0) {
                url = url + "?" + key + "=" + value;
            } else {
                url = url + "&" + key + "=" + value;
            }
            i++;
        }
        return url;
    }

    public static MyCustomProgressDialog getProgressDialog(Context c) {
        MyCustomProgressDialog mProgressDialog = new MyCustomProgressDialog(c);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }

    public static void dismissDialog(MyCustomProgressDialog pd) {
        try {
            if (pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppIsInBackground(Context context) {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void customeRadioColorChange(RadioButton radioButton, SessionManager sessionManager) {
        try {

            int[] colorIntArray;
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                colorIntArray = new int[]
                        {
                                Color.parseColor(sessionManager.getFunTopTextColor()) //disabled
                                , Color.parseColor(sessionManager.getFunTopTextColor()) //enabled

                        };
            } else {
                colorIntArray = new int[]
                        {
                                Color.parseColor(sessionManager.getTopTextColor()) //disabled
                                , Color.parseColor(sessionManager.getTopTextColor()) //enabled

                        };
            }
            ColorStateList colorStateList = new ColorStateList(new int[][]{
                    new int[]{-android.R.attr.state_enabled}, //disabled
                    new int[]{android.R.attr.state_enabled} //enabled
            },
                    colorIntArray
            );
            radioButton.setButtonTintList(colorStateList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void customeRadioColorForActivitySurveyChange(RadioButton radioButton, SessionManager sessionManager) {
        try {

            int[] colorIntArray;
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                colorIntArray = new int[]
                        {
                                Color.parseColor(sessionManager.getFunTopBackColor()) //disabled
                                , Color.parseColor(sessionManager.getFunTopBackColor()) //enabled

                        };
            } else {
                colorIntArray = new int[]
                        {
                                Color.parseColor(sessionManager.getTopBackColor()) //disabled
                                , Color.parseColor(sessionManager.getTopBackColor()) //enabled

                        };
            }
            ColorStateList colorStateList = new ColorStateList(new int[][]{
                    new int[]{-android.R.attr.state_enabled}, //disabled
                    new int[]{android.R.attr.state_enabled} //enabled
            },
                    colorIntArray
            );
            radioButton.setButtonTintList(colorStateList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void customeCheckboxColorChange(CheckBox checkBox, SessionManager sessionManager) {
        try {

            int[] colorIntArray;
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                colorIntArray = new int[]
                        {
                                Color.parseColor(sessionManager.getFunTopTextColor()) //disabled
                                , Color.parseColor(sessionManager.getFunTopTextColor()) //enabled

                        };
            } else {
                colorIntArray = new int[]
                        {
                                Color.parseColor(sessionManager.getTopTextColor()) //disabled
                                , Color.parseColor(sessionManager.getTopTextColor()) //enabled

                        };
            }
            ColorStateList colorStateList = new ColorStateList(new int[][]{
                    new int[]{-android.R.attr.state_enabled}, //disabled
                    new int[]{android.R.attr.state_enabled} //enabled
            },
                    colorIntArray
            );
            checkBox.setButtonTintList(colorStateList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void customeCheckboxColorChangeAttendeeFilter(CheckBox checkBox, SessionManager sessionManager) {
        try {

            int[] colorIntArray;
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                colorIntArray = new int[]
                        {
                                Color.parseColor(sessionManager.getFunTopBackColor()) //disabled
                                , Color.parseColor(sessionManager.getFunTopBackColor()) //enabled

                        };
            } else {
                colorIntArray = new int[]
                        {
                                Color.parseColor(sessionManager.getTopBackColor()) //disabled
                                , Color.parseColor(sessionManager.getTopBackColor()) //enabled

                        };
            }
            ColorStateList colorStateList = new ColorStateList(new int[][]{
                    new int[]{-android.R.attr.state_enabled}, //disabled
                    new int[]{android.R.attr.state_enabled} //enabled
            },
                    colorIntArray
            );
            checkBox.setButtonTintList(colorStateList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static void Viewanimation(View view, Techniques techniques) {
        YoYo.with(techniques)
                .duration(400)
                .playOn(view);
    }

    public static Drawable getAdaptiveRippleDrawable(
            int normalColor, int pressedColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(pressedColor),
                    null, getRippleMask(normalColor));
        } else {
            return getStateListDrawable(normalColor, pressedColor);
        }
    }

    private static Drawable getRippleMask(int color) {
        float[] outerRadii = new float[8];
        // 3 is radius of final ripple,
        // instead of 3 you can give required final radius
        Arrays.fill(outerRadii, 6);

        RoundRectShape r = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(r);
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public static StateListDrawable getStateListDrawable(
            int normalColor, int pressedColor) {
        StateListDrawable states = new StateListDrawable();
        states.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_focused},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_activated},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{},
                new ColorDrawable(normalColor));
        return states;
    }

    public String compressImage(String imageUri, Context context) {

        String filePath = getRealPathFromURI(imageUri, context);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/.Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    private String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public enum CurrentFragment {AttendeeDetail, PublicMessage, PrivateMessage, SpeakerDetail, ExhibitorDetail, photo, ActivityTab_AllFragment, ExhibitorList_Fragment_New}


    public static String getDataFromBaseEncrypt(String id) {
        byte[] data = Base64.decode(id, Base64.DEFAULT);
        String text = "";
        try {
            text = new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static boolean checkForUIDVersion() {
        return BuildConfig.name.equals("aitlUid");
    }

}
