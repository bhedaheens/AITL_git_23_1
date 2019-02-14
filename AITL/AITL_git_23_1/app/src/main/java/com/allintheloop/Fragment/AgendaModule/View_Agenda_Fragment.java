package com.allintheloop.Fragment.AgendaModule;


import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.allintheloop.Adapter.Agenda.AgendaSpeakerAdapter;
import com.allintheloop.Adapter.Agenda.AgendaSpeakerSupporterLink;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.AgendaData.AgendaSpeaker_SupporterLink;
import com.allintheloop.Bean.AgendaData.Agenda_SpeakerList;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class View_Agenda_Fragment extends Fragment implements VolleyInterface {


    SessionManager sessionManager;

    TextView txt_date, txt_start_time, txt_end_time, session_name, txt_map_name, txt_placeleft, txt_ratesession, txt_check_DateTime, txt_cancel, map_link, txt_supportMaterial;
    WebView description;
    ArrayList<Agenda_SpeakerList> speakerLists;
    ArrayList<AgendaSpeaker_SupporterLink> speakerSupporLinkLists;
    AgendaSpeakerAdapter adapter;
    AgendaSpeakerSupporterLink agendaSpeakerSupporterLink;
    RecyclerView recyclerView, rv_supportMaterials;
    LinearLayout map_layout, desc_layout, btn_layout, layout_speker_profile, layout_support_materials, layout_commentBox;
    Button btn_agenda_personal, btn_removeFromAgenda, btn_delete, btn_checkin, btn_setReminder, btn_addToCalendar;
    Button btnSend, btnFeedback;
    ImageView map_img, session_image;
    String time_format, user_flag, start_date, start_time = "", end_time = "", heading, document_file, map_title, agenda_desc, map_id, dateformat, check_dwg_files, confirm_clash = "";
    String show_places_remaining, show_checking_in, str_rating, str_maximum_people, str_checking_dateTime, show_reminder_button, end_date, check_max_people, questionId;
    String str_session_image, str_allow_comments, str_show_comment_box, str_show_feedback;
    String agenda_timezone, agenda_rating;
    String flag_checkin, isRatingShow;
    String reminderTime = "";
    String cal_startDate = "", cal_endDate = "";
    RatingBar rating;
    boolean isCheckedIn = false;
    boolean isFirstTimeCalled = true;

    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    long startMillis, endMillis;
    int sday = 0, smonth = 0, syear = 0;
    int eday = 0, emonth = 0, eyear = 0;
    int sHours = 0, sMin = 0;
    int eHours = 0, eMin = 0;
    LinearLayout layout_whole;
    CardView card_noagenda;
    EditText edtMessage;
    TextView txt_comments;
    Dialog dialog;
    String str_is_visible_view_btn, map_coords;
    DefaultLanguage.DefaultLang defaultLanguage;
    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            ToastC.show(getActivity(), "Download Completed");
            getActivity().unregisterReceiver(onComplete);
        }
    };
    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
            getActivity().unregisterReceiver(onNotificationClick);
        }
    };

    public View_Agenda_Fragment() {
        // Required empty public constructor
    }

    public static String getToday(String format) {
        Date date = new Date();
        return new SimpleDateFormat(format).format(date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view__agenda, container, false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.strModuleId = sessionManager.agenda_id();
//        sessionManager.setMenuid("1");
        sessionManager.strMenuId = "1";
        defaultLanguage = sessionManager.getMultiLangString();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
//        bundle = getArguments();
//        Log.d("AITL", "AgendaId" + bundle.getString("agenda_id")); // get AgendaId from AgendaTime Fragment

        GlobalData.currentModuleForOnResume = GlobalData.agendaModuleid;
        speakerLists = new ArrayList<>();
        speakerSupporLinkLists = new ArrayList<>();
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        txt_date = (TextView) rootView.findViewById(R.id.date);
        txt_start_time = (TextView) rootView.findViewById(R.id.txt_start_time);
        txt_end_time = (TextView) rootView.findViewById(R.id.txt_end_time);

        //   txt_end_time = (TextView) rootView.findViewById(R.id.end_time);
        session_name = (TextView) rootView.findViewById(R.id.session_name);
        txt_map_name = (TextView) rootView.findViewById(R.id.mapName);
        txt_supportMaterial = (TextView) rootView.findViewById(R.id.txt_supportMaterial);

        map_link = (TextView) rootView.findViewById(R.id.map_link);
        txt_placeleft = (TextView) rootView.findViewById(R.id.txt_placeleft);
        txt_check_DateTime = (TextView) rootView.findViewById(R.id.txt_check_DateTime);
//        doc_link = (TextView) rootView.findViewById(R.id.doc_link);

        btnFeedback = (Button) rootView.findViewById(R.id.btnFeedback);

        layout_whole = (LinearLayout) rootView.findViewById(R.id.layout_whole);
        layout_speker_profile = (LinearLayout) rootView.findViewById(R.id.layout_speker_profile);
        layout_support_materials = (LinearLayout) rootView.findViewById(R.id.layout_support_materials);
        map_layout = (LinearLayout) rootView.findViewById(R.id.map_layout);
//        document_layout = (LinearLayout) rootView.findViewById(R.id.document_layout);
        desc_layout = (LinearLayout) rootView.findViewById(R.id.desc_layout);
        btn_layout = (LinearLayout) rootView.findViewById(R.id.btn_layout);
        card_noagenda = (CardView) rootView.findViewById(R.id.card_noagenda);

        description = (WebView) rootView.findViewById(R.id.agenda_desc);
        description.getSettings().setJavaScriptEnabled(true);
        description.getSettings().setAllowContentAccess(true);
        description.setVerticalScrollBarEnabled(true);
        description.getSettings().setDefaultTextEncodingName("utf-8");
        description.setHorizontalScrollBarEnabled(true);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_viewspeaker);
        rv_supportMaterials = (RecyclerView) rootView.findViewById(R.id.rv_supportMaterials);

//        doc_img = (ImageView) rootView.findViewById(R.id.document_img);
        map_img = (ImageView) rootView.findViewById(R.id.map_image);
        session_image = (ImageView) rootView.findViewById(R.id.session_image);

        btn_agenda_personal = (Button) rootView.findViewById(R.id.personal_agenda);

        btn_removeFromAgenda = (Button) rootView.findViewById(R.id.btn_removeFromAgenda);
        btn_setReminder = (Button) rootView.findViewById(R.id.btn_setReminder);
        btn_addToCalendar = (Button) rootView.findViewById(R.id.btn_addToCalendar);

        btn_delete = (Button) rootView.findViewById(R.id.btn_delete);
        btn_checkin = (Button) rootView.findViewById(R.id.btn_checkin);

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.feedback_dialog);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        rating = (RatingBar) dialog.findViewById(R.id.rating);
        edtMessage = (EditText) dialog.findViewById(R.id.edtMessage);
        txt_comments = (TextView) dialog.findViewById(R.id.txt_comments);
        layout_commentBox = (LinearLayout) dialog.findViewById(R.id.layout_commentBox);
        txt_ratesession = (TextView) dialog.findViewById(R.id.txt_ratesession);
        btnSend = (Button) dialog.findViewById(R.id.btnSend);
        txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);

        txt_cancel.setText(defaultLanguage.get1Cancel());
        txt_comments.setText(defaultLanguage.get1Comments());
        txt_ratesession.setText(defaultLanguage.get1RateThisSession());
        btnSend.setText(defaultLanguage.get1Submit());
        btn_agenda_personal.setText(defaultLanguage.get1SaveSession());
        btn_checkin.setText(defaultLanguage.get1CheckIn());
        btn_removeFromAgenda.setText(defaultLanguage.get1Saved());
        btn_delete.setText(defaultLanguage.get1Saved());
        btnFeedback.setText(defaultLanguage.get1ProvideFeedback());


        setButton();
        if (GlobalData.isNetworkAvailable(getActivity())) {
            Cursor cursor = sqLiteDatabaseHandler.getAgendaDetail(sessionManager.getEventId(), sessionManager.agenda_id(), sessionManager.getUserId());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsondata = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.Detailagenda_Data)));
                        laodAgendaDetail(jsondata);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_agenda_byId, Param.getAgenda_byId(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), sessionManager.agenda_id(), sessionManager.getUserId()), 0, false, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_agenda_byId, Param.getAgenda_byId(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), sessionManager.agenda_id(), sessionManager.getUserId()), 0, false, this);
            }

        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAgendaDetail(sessionManager.getEventId(), sessionManager.agenda_id(), sessionManager.getUserId());
            if (cursor.moveToFirst()) {

                try {
                    JSONObject jsondata = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.Detailagenda_Data)));
                    laodAgendaDetail(jsondata);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//        btn_askQuestion.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });


        btnFeedback.setOnClickListener(view -> dialog.show());


        btnSend.setOnClickListener(view -> {


            if (sessionManager.isLogin()) {
                if (edtMessage.getText().toString().trim().length() != 0) {
                    Save_agenda_comment();
                } else {
                    Toast.makeText(getActivity(), "Please enter comment", Toast.LENGTH_SHORT).show();
                }
            } else {
                sessionManager.alertDailogLogin(getActivity());
            }
        });


        rating.setOnRatingBarChangeListener((ratingBar, ratingC, fromUser) -> {

            if (sessionManager.isLogin()) {
                if (fromUser) {
//                    ToastC.show(getContext(), "called =====");
                    agenda_rating = rating.getRating() + "";
                    if (GlobalData.isNetworkAvailable(getActivity())) {
                        Rate_agenda();
                    } else {
                        ToastC.show(getActivity(), "No Internet Connection");
                        rating.setRating(0.0f);
                    }
                }
            } else {
                rating.setRating(0.0f);
            }
        });


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), (view, position) -> {
            Agenda_SpeakerList speaker_Obj = speakerLists.get(position);

            SessionManager.speaker_id = speaker_Obj.getUser_id();
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
            ((MainActivity) getActivity()).loadFragment();


        }));

        rv_supportMaterials.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), (view, position) -> {
            AgendaSpeaker_SupporterLink agenda_docObj = speakerSupporLinkLists.get(position);


            if (agenda_docObj.getType().equalsIgnoreCase("document")) {

                openDocument(agenda_docObj.getId());

            } else if (agenda_docObj.getType().equalsIgnoreCase("qa_session")) {
                Bundle bundle = new Bundle();
                bundle.putString("session_id", agenda_docObj.getId());
                sessionManager.setQaSessionId(agenda_docObj.getId());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.QAListDetail_Fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);
            } else if (agenda_docObj.getType().equalsIgnoreCase("presentation")) {
                Bundle bundle = new Bundle();
                bundle.putString("presantation_id", agenda_docObj.getId());

                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Presantation_Detail_Fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);
            } else if (agenda_docObj.getType().equalsIgnoreCase("survey")) {
                sessionManager.setCategoryId(agenda_docObj.getId());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.SurveyCategoryWiseFragment;
                ((MainActivity) getActivity()).loadFragment();
            }

        }));

//        slide_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("presantation_id", slide_name);
//
//                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//                GlobalData.CURRENT_FRAG = GlobalData.Presantation_Detail_Fragment;
//                ((MainActivity) getActivity()).loadFragment(bundle);
//            }
//        });
        map_layout.setOnClickListener(v -> {

            if (check_dwg_files.equalsIgnoreCase("1")) {
                sessionManager.exhibitor_standNumber = "";
                GlobalData.temp_Fragment = 0;
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.NewMapDetail_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            } else {
                sessionManager.setMapid(map_id);
                sessionManager.Map_coords = map_coords;
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Map_Detail_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            }


        });

        map_link.setOnClickListener(v -> {
            if (check_dwg_files.equalsIgnoreCase("1")) {
                sessionManager.exhibitor_standNumber = "";
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.NewMapDetail_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            } else {
                sessionManager.setMapid(map_id);
                sessionManager.Map_coords = map_coords;
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Map_Detail_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        btn_checkin.setOnClickListener(v -> {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                CheckIn_agenda();
            } else {
                ToastC.show(getActivity(), "No Internet Connection");
            }
        });

        btn_setReminder.setOnClickListener(view -> {
            FragmentManager fm = getFragmentManager();
            AgendaReminderFragmentDailog dailog = new AgendaReminderFragmentDailog();
            Bundle bundle = new Bundle();
            bundle.putString("agendaId", sessionManager.agenda_id());
            bundle.putString("time", reminderTime);
            dailog.setArguments(bundle);
            dailog.show(fm, "show");
        });


        txt_cancel.setOnClickListener(view ->
        {
            setButton();
            dialog.dismiss();
        });

        btn_agenda_personal.setOnClickListener(v -> {


            if (check_max_people.equalsIgnoreCase("0")) {
                btn_saveClick();
            } else {
                if (str_maximum_people.equalsIgnoreCase("0")) {
                    new AlertDialogWrapper.Builder(getActivity())
                            .setMessage("No Places left")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    btn_saveClick();
                }
            }
        });

        btn_delete.setOnClickListener(v -> {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                deletePendingAgenda();
            } else {

                if (sqLiteDatabaseHandler.IsPendingAgendaRequestExist("0", sessionManager.agenda_id(), sessionManager.getEventId(), sessionManager.getUserId(), MyUrls.deletePendingAgenda)) {
                    new AlertDialogWrapper.Builder(getActivity())
                            .setMessage("Your agenda is already deleted offline")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    sqLiteDatabaseHandler.insertPendingRequestData(MyUrls.deletePendingAgenda, sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), sessionManager.agenda_id(), "0");
                    new AlertDialogWrapper.Builder(getActivity())
                            .setMessage("Agenda Delete Successfully")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
//                        ToastC.show(getActivity(), "No Internet Connection");
            }

        });

        btn_removeFromAgenda.setOnClickListener(v -> {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                Delete_agenda();
            } else {
                if (sqLiteDatabaseHandler.IsPendingAgendaRequestExist("0", sessionManager.agenda_id(), sessionManager.getEventId(), sessionManager.getUserId(), MyUrls.Delete_agenda)) {
                    new AlertDialogWrapper.Builder(getActivity())
                            .setMessage("User agenda is already Removed offline")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    sqLiteDatabaseHandler.insertPendingRequestData(MyUrls.Delete_agenda, sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), sessionManager.agenda_id(), "0");
                    new AlertDialogWrapper.Builder(getActivity())
                            .setMessage("User Agenda Removed Successfully")
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        });


        btn_addToCalendar.setOnClickListener(view ->
                writeCalendarEvent(cal_startDate, start_time, heading));

        return rootView;

    }

    private void setButton() {
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));

            map_img.setColorFilter(Color.parseColor(sessionManager.getTopBackColor()));

            btnSend.setBackgroundDrawable(drawable);
            btnSend.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            btn_agenda_personal.setBackgroundDrawable(drawable);
            btn_agenda_personal.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            btnFeedback.setBackgroundDrawable(drawable);
            btnFeedback.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            Drawable progress = rating.getProgressDrawable();
            DrawableCompat.setTint(progress, Color.parseColor(sessionManager.getTopBackColor()));
//            stars.getDrawable(2).setColorFilter(Color.parseColor(sessionManager.getTopBackColor()), PorterDuff.Mode.SRC_ATOP);

        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));

            map_img.setColorFilter(Color.parseColor(sessionManager.getFunTopBackColor()));

            btnFeedback.setBackgroundDrawable(drawable);
            btnFeedback.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

            btn_agenda_personal.setBackgroundDrawable(drawable);
            btn_agenda_personal.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

            btnSend.setBackgroundDrawable(drawable);
            btnSend.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            Drawable progress = rating.getProgressDrawable();
            DrawableCompat.setTint(progress, Color.parseColor(sessionManager.getFunTopBackColor()));
        }

    }

    private void openDocument(String document_file) {
        if (!document_file.equalsIgnoreCase("")) {
            try {
                String[] file_split = document_file.split("\\.");
                String str_exten = file_split[1];


                if (str_exten.equalsIgnoreCase("ppt") || str_exten.equalsIgnoreCase("odg")) {
                    File storagePath = new File(Environment.getExternalStorageDirectory(), "AllInTheLoop");

                    if (!storagePath.exists()) {
                        storagePath.mkdirs();
                    }
                    getActivity().registerReceiver(onComplete,
                            new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    getActivity().registerReceiver(onNotificationClick,
                            new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
                    downloadFile(MyUrls.doc_url + document_file, storagePath.getPath(), document_file);
                } else {
                    Bundle bundle = new Bundle();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                    bundle.putString("document_file", MyUrls.doc_url + document_file);
                    bundle.putString("file_name", MyUrls.doc_url + document_file);
                    ((MainActivity) getActivity()).loadFragment(bundle);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void btn_saveClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            confirm_clash = "0";
            Save_agenda();
        } else {
            if (sqLiteDatabaseHandler.IsPendingAgendaRequestExist("0", sessionManager.agenda_id(), sessionManager.getEventId(), sessionManager.getUserId(), MyUrls.saveUser_agenda)) {
                new AlertDialogWrapper.Builder(getActivity())
                        .setMessage("Your agenda is already saved offline")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                sqLiteDatabaseHandler.insertPendingRequestData(MyUrls.saveUser_agenda, sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), sessionManager.agenda_id(), "0");
                new AlertDialogWrapper.Builder(getActivity())
                        .setMessage("Agenda Saved Successfully")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }

//                        ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    private boolean isExpire(String date) {
        if (date.isEmpty() || date.trim().equals("")) {
            return false;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss a"); // Jan-20-2015 1:30:55 PM
            Date d = null;
            Date d1 = null;
            String today = getToday("MMM-dd-yyyy hh:mm:ss a");
            try {
                //System.out.println("expdate>> "+date);
                //System.out.println("today>> "+today+"\n\n");
                d = sdf.parse(date);
                d1 = sdf.parse(today);
                if (d1.compareTo(d) < 0) {// not expired
                    return false;
                } else if (d.compareTo(d1) == 0) {// both date are same
                    if (d.getTime() < d1.getTime()) {// not expired
                        return false;
                    } else if (d.getTime() == d1.getTime()) {//expired
                        return true;
                    } else {//expired
                        return true;
                    }
                } else {//expired
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private void writeCalendarEvent(String cal_startDate, String start_time, String name) {

        int sday = 0, smonth = 0, syear = 0;
        int sHours = 0, sMin = 0;
        long startMillis;
        GregorianCalendar calDate = new GregorianCalendar();
        if (!(cal_startDate.equalsIgnoreCase(""))) {
            String[] startDateSepretor = cal_startDate.split("/");
            syear = Integer.parseInt(startDateSepretor[2]);
            smonth = Integer.parseInt(startDateSepretor[1]);
            sday = Integer.parseInt(startDateSepretor[0]);

        }

        if (!(start_time.equalsIgnoreCase(""))) {
            String[] startTimeSepretor = start_time.split(":");
            sHours = Integer.parseInt(startTimeSepretor[0]);
            sMin = Integer.parseInt(startTimeSepretor[1]);
        }
        calDate.set(syear, smonth - 1, sday, sHours, sMin);
        startMillis = calDate.getTimeInMillis();
        Calendar calendarEvent = Calendar.getInstance();
        Intent i = new Intent(Intent.ACTION_EDIT);
        i.setType("vnd.android.cursor.item/event");
        i.putExtra("beginTime", startMillis);
        i.putExtra("allDay", false);
        i.putExtra("rule", "FREQ=YEARLY");
        i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
        i.putExtra("title", name);
        startActivity(i);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    writeCalendarEvent(cal_startDate, start_time, heading);
                }
                break;
        }
    }

    private void Save_agenda() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.saveUser_agenda, Param.Update_Agenda(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.agenda_id(), sessionManager.getUserId()), 1, true, this);
    }

    private void Delete_agenda() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Delete_agenda, Param.Update_Agenda(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.agenda_id(), sessionManager.getUserId()), 2, true, this);
    }

    private void CheckIn_agenda() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.CheckIn_agenda, Param.Update_Agenda(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.agenda_id(), sessionManager.getUserId()), 3, true, this);
    }

    private void Rate_agenda() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.SaveRating_agenda, Param.SaveRating_agenda(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.agenda_id(), sessionManager.getUserId(), agenda_rating), 4, true, this);
    }

    private void deletePendingAgenda() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.deletePendingAgenda, Param.Update_Agenda(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.agenda_id(), sessionManager.getUserId()), 5, true, this);
    }

    private void Save_agenda_comment() {

        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.save_agenda_comment, Param.Agenda_comments(sessionManager.getEventId(), sessionManager.agenda_id(), edtMessage.getText().toString(), sessionManager.getUserId()), 6, true, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);

                    if (sqLiteDatabaseHandler.isAgendaDetailExist(sessionManager.getEventId(), sessionManager.agenda_id(), sessionManager.getUserId())) {
                        sqLiteDatabaseHandler.UpdateAgendaDetail(sessionManager.getEventId(), sessionManager.agenda_id(), jsonObject.toString(), sessionManager.getUserId());
                    } else {
                        sqLiteDatabaseHandler.insertAgendaDetail(sessionManager.getEventId(), sessionManager.agenda_id(), jsonObject.toString(), sessionManager.getUserId());
                    }

                    laodAgendaDetail(jsonObject);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        GlobalData.agendaRefresh(getActivity());
                        GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();

                    } else {
                        new AlertDialogWrapper.Builder(getActivity())
                                .setMessage(jsonObject.getString("msg").toString())
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {


                        GlobalData.agendaRefresh(getActivity());
                        GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();

                    } else {
                        ToastC.show(getContext(), jsonObject.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();

//                        rating.setVisibility(View.VISIBLE);
//                        btn_checkin.setBackgroundColor(Color.parseColor(jsonObject.getString("btn_color")));
                    } else if (jsonObject.getString("success").equalsIgnoreCase("false")) {
                        flag_checkin = jsonObject.getString("flag");
                        if (flag_checkin.equalsIgnoreCase("0")) {
                            ToastC.show(getContext(), jsonObject.getString("msg"));

                        } else {

                            JSONObject jsonMessage2 = jsonObject.getJSONObject("message2");
                            Bundle bundle = new Bundle();
                            bundle.putString("title", jsonObject.getString("message1"));
                            bundle.putString("name", jsonMessage2.getString("Heading"));
                            bundle.putString("time", jsonMessage2.getString("Start_time"));
                            bundle.putString("agenda_id", jsonMessage2.getString("Id"));
                            bundle.putString("warning", jsonObject.getString("message3"));
                            bundle.putString("isShowCheckIn", jsonObject.getString("show_check_in"));
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            RatingDailog_Fragment fragment = new RatingDailog_Fragment();
                            fragment.setArguments(bundle);
                            fragment.show(fm, "DialogFragment");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        dialog.dismiss();

                        GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();

//                        str_rating = rating.getRating() + "";
//                        rating.setRating(Float.parseFloat(str_rating));

                    } else {
                        //  ToastC.show(getActivity(),jsonObject.getString("msg"));
                        new AlertDialogWrapper.Builder(getActivity())
                                .setMessage(jsonObject.getString("msg").toString())
                                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                        rating.setRating(0.0f);
                    }
                } catch (Exception e) {

                }
                break;

            case 5:
                try {
                    JSONObject jsonRemove = new JSONObject(volleyResponse.output);
                    if (jsonRemove.getString("success").equalsIgnoreCase("true")) {

                        GlobalData.agendaRefresh(getActivity());
                        GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        dialog.dismiss();
                        GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                        ((MainActivity) getActivity()).reloadFragment();
                    } else if (jsonObject.getString("success").equalsIgnoreCase("false")) {
                        setButton();
                        dialog.dismiss();
                        ToastC.show(getContext(), jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }

    private void laodAgendaDetail(JSONObject jsonObject) {
        try {


            setButton();

            user_flag = jsonObject.getString("user_flag");
            time_format = jsonObject.getString("time_format");

            JSONArray json_agenda_Array = jsonObject.getJSONArray("agenda");
            JSONArray json_reminder_array = jsonObject.getJSONArray("reminder");
            speakerLists = new ArrayList<>();
            speakerSupporLinkLists.clear();
            if (json_agenda_Array.length() == 0) {
                layout_whole.setVisibility(View.GONE);
                card_noagenda.setVisibility(View.VISIBLE);
            } else {
                layout_whole.setVisibility(View.VISIBLE);
                card_noagenda.setVisibility(View.GONE);

                for (int r = 0; r < json_reminder_array.length(); r++) {
                    JSONObject jsonObject1 = json_reminder_array.getJSONObject(r);
                    reminderTime = jsonObject1.getString("time");
                }

                for (int i = 0; i < json_agenda_Array.length(); i++) {

                    JSONObject json_agenda = json_agenda_Array.getJSONObject(i);

                    start_time = json_agenda.getString("Start_time");
                    end_time = json_agenda.getString("End_time");
                    heading = json_agenda.getString("Heading");
                    start_date = json_agenda.getString("Start_date");
                    end_date = json_agenda.getString("End_date");
                    document_file = json_agenda.getString("document_file");
                    map_title = json_agenda.getString("Map_title");
//                    slide_name = json_agenda.getString("presentation_id");
                    agenda_desc = json_agenda.getString("description");
                    show_checking_in = json_agenda.getString("show_checking_in");
                    show_places_remaining = json_agenda.getString("show_places_remaining");
                    str_rating = json_agenda.getString("rating");
                    str_maximum_people = json_agenda.getString("Maximum_People");
                    check_max_people = json_agenda.getString("check_max_people");
                    isCheckedIn = json_agenda.getBoolean("is_checked_in");
                    str_checking_dateTime = json_agenda.getString("checking_datetime");
                    cal_startDate = json_agenda.getString("Start_date_cal");
                    cal_endDate = json_agenda.getString("End_date_cal");
                    isRatingShow = json_agenda.getString("show_rating");
                    show_reminder_button = json_agenda.getString("show_reminder_button");
                    dateformat = json_agenda.getString("start_date_time");
                    questionId = json_agenda.getString("qasession_id");
                    str_session_image = json_agenda.getString("session_image");
                    map_id = json_agenda.getString("Address_map_id");
                    str_allow_comments = json_agenda.getString("allow_comments");
                    str_show_comment_box = json_agenda.getString("show_comment_box");
                    str_show_feedback = json_agenda.getString("show_feedback");
                    check_dwg_files = json_agenda.getString("check_dwg_files");
                    str_is_visible_view_btn = json_agenda.getString("is_map_visible_btn");
                    if (!str_session_image.equalsIgnoreCase("")) {
                        session_image.setVisibility(View.VISIBLE);
                        Glide.with(getContext()).load(MyUrls.Imgurl + str_session_image).into(session_image);
                    } else {
                        session_image.setVisibility(View.GONE);
                    }


                    if (str_is_visible_view_btn.equalsIgnoreCase("1")) {
                        JSONObject jObjectMap = json_agenda.getJSONObject("map_details");
                        if (jsonObject.length() == 0) {
                            map_coords = jObjectMap.getString("coords");
                            map_id = jObjectMap.getString("map_id");

                        }


                    }


                    if (isCheckedIn) {
                        btn_checkin.setBackground(getResources().getDrawable(R.drawable.survey_btn_green));
                    } else {
                        btn_checkin.setBackground(getResources().getDrawable(R.drawable.survey_btn));
                    }
//                        str_checking_dateTime= json_agenda.getString("str_checking_dateTime");


                    agenda_timezone = json_agenda.getString("time_zone");

                    JSONArray jArray_speaker = json_agenda.getJSONArray("speaker");

                    for (int j = 0; j < jArray_speaker.length(); j++) {
                        JSONObject jsonSpeaker = jArray_speaker.getJSONObject(j);

                        speakerLists.add(new Agenda_SpeakerList(jsonSpeaker.getString("user_id"), jsonSpeaker.getString("Logo"), jsonSpeaker.getString("Firstname"), jsonSpeaker.getString("Lastname"), jsonSpeaker.getString("Speaker_desc")));

                    }

                    JSONArray jArray_supportLink = json_agenda.getJSONArray("support_materials");

                    for (int j = 0; j < jArray_supportLink.length(); j++) {
                        JSONObject jsonSupportLink = jArray_supportLink.getJSONObject(j);
                        speakerSupporLinkLists.add(new AgendaSpeaker_SupporterLink(jsonSupportLink.getString("type"), jsonSupportLink.getString("value"), jsonSupportLink.getString("id")));
                    }
                }

                if (speakerLists.size() != 0) {
                    layout_speker_profile.setVisibility(View.VISIBLE);
                    adapter = new AgendaSpeakerAdapter(speakerLists, getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);

                } else {
                    layout_speker_profile.setVisibility(View.GONE);
                }

                if (speakerSupporLinkLists.size() != 0) {
                    layout_support_materials.setVisibility(View.VISIBLE);
                    agendaSpeakerSupporterLink = new AgendaSpeakerSupporterLink(speakerSupporLinkLists, getActivity());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    rv_supportMaterials.setLayoutManager(mLayoutManager);
                    rv_supportMaterials.setItemAnimator(new DefaultItemAnimator());
                    rv_supportMaterials.setAdapter(agendaSpeakerSupporterLink);
                } else {
                    layout_support_materials.setVisibility(View.GONE);
                }

                if (sessionManager.isLogin()) {
                    btn_layout.setVisibility(View.VISIBLE);
                    if (user_flag.equalsIgnoreCase("pending_1")) {
                        btn_delete.setVisibility(View.VISIBLE);
                        btn_agenda_personal.setVisibility(View.GONE);
                    } else if (user_flag.equalsIgnoreCase("save_1")) {

                        if (show_reminder_button.equalsIgnoreCase("1")) {
                            btn_setReminder.setVisibility(View.VISIBLE);
                            btn_addToCalendar.setVisibility(View.VISIBLE);
                        } else {
                            btn_setReminder.setVisibility(View.GONE);
                            btn_addToCalendar.setVisibility(View.GONE);
                        }
                        if (show_checking_in.equalsIgnoreCase("1")) {
                            btn_checkin.setVisibility(View.VISIBLE);
                        } else {
                            btn_checkin.setVisibility(View.GONE);
                        }
                        btn_removeFromAgenda.setVisibility(View.VISIBLE);
                        btn_delete.setVisibility(View.GONE);
                        btn_agenda_personal.setVisibility(View.GONE);
                    } else {
                        btn_agenda_personal.setVisibility(View.VISIBLE);
                        btn_removeFromAgenda.setVisibility(View.GONE);
                        btn_checkin.setVisibility(View.GONE);
                        btn_delete.setVisibility(View.GONE);

                    }
                } else {
                    btn_layout.setVisibility(View.GONE);
                }

                if (str_show_feedback.equalsIgnoreCase("1")) {
                    if (sessionManager.isLogin())
                        btnFeedback.setVisibility(View.VISIBLE);
                    else
                        btnFeedback.setVisibility(View.GONE);
                } else {
                    btnFeedback.setVisibility(View.GONE);
                }

                if (str_allow_comments.equalsIgnoreCase("1") && str_show_comment_box.equalsIgnoreCase("1")) {
                    layout_commentBox.setVisibility(View.VISIBLE);
                } else {

                    layout_commentBox.setVisibility(View.GONE);
                }

                if (isRatingShow.equalsIgnoreCase("1")) {

                    txt_ratesession.setVisibility(View.VISIBLE);
                    rating.setVisibility(View.VISIBLE);
                    if (!(str_rating.equalsIgnoreCase("")))
                        rating.setRating(Float.parseFloat(str_rating));
                } else {
                    txt_ratesession.setVisibility(View.GONE);
                    rating.setVisibility(View.GONE);
                }


                if (!(str_checking_dateTime.equalsIgnoreCase(""))) {
                    txt_check_DateTime.setVisibility(View.VISIBLE);
                    txt_check_DateTime.setText("Check In Time:" + str_checking_dateTime);
                } else {
                    txt_check_DateTime.setVisibility(View.GONE);
                }

                if (!heading.equalsIgnoreCase("")) {
                    session_name.setVisibility(View.VISIBLE);
                    session_name.setText(heading);
                } else {
                    session_name.setVisibility(View.GONE);
                }

                if (show_places_remaining.equalsIgnoreCase("0")) {
                    txt_placeleft.setVisibility(View.GONE);

                } else {
                    txt_placeleft.setVisibility(View.VISIBLE);
                    txt_placeleft.setText("Places Left : " + str_maximum_people);
                }
                if (!start_date.equals("")) {
                    txt_date.setVisibility(View.VISIBLE);
                    txt_date.setText(start_date);
                } else {
                    txt_date.setVisibility(View.GONE);
                }
                if (!(start_time.equals("") || end_time.equals(""))) {
                    txt_start_time.setVisibility(View.VISIBLE);
                    txt_end_time.setVisibility(View.VISIBLE);
                    if (time_format.equalsIgnoreCase("1")) {

                        if (agenda_timezone.equalsIgnoreCase("")) {
                            txt_start_time.setText(defaultLanguage.get1Start() + ": " + start_time);
                            txt_end_time.setText(defaultLanguage.get1End() + ": " + end_time);
                        } else {
                            txt_start_time.setText(defaultLanguage.get1Start() + ": " + start_time + " (" + agenda_timezone + ") ");
                            txt_end_time.setText(defaultLanguage.get1End() + ": " + end_time + " (" + agenda_timezone + ")");

                        }

                    } else {
                        DateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
                        Date start = f1.parse(start_time);
                        Date end = f1.parse(end_time);
                        DateFormat f2 = new SimpleDateFormat("h:mm a");


                        if (agenda_timezone.equalsIgnoreCase("")) {
                            txt_start_time.setText(defaultLanguage.get1Start() + ": " + f2.format(start));
                            txt_end_time.setText(defaultLanguage.get1End() + ": " + f2.format(end));
                            //  txt_end_time.setText("End Time : " + f2.format(end));
                        } else {
                            txt_start_time.setText(defaultLanguage.get1Start() + ": " + f2.format(start) + " (" + agenda_timezone + ") ");

                            txt_end_time.setText(defaultLanguage.get1End() + ": " + f2.format(end) + " (" + agenda_timezone + ")");
                            //    txt_end_time.setText("End Time : " + f2.format(end) + " (" + agenda_timezone + ")");
                        }
                    }
                } else {
                    txt_start_time.setVisibility(View.GONE);
                    txt_end_time.setVisibility(View.GONE);
                }

                if (!agenda_desc.equalsIgnoreCase("")) {
                    desc_layout.setVisibility(View.VISIBLE);
                    String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: left;}</style></head><body>";
                    String pas = "</body></html>";
                    String myHtmlString = pish + agenda_desc + pas;
                    description.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);
//                description.loadData(agenda_desc, "text/html; charset=utf-8", "utf-8");
                } else {
                    desc_layout.setVisibility(View.GONE);
                }


                if (str_is_visible_view_btn.equalsIgnoreCase("1")) {
                    map_layout.setVisibility(View.VISIBLE);
                    txt_map_name.setText("" + map_title);
                    //map_img.setImageResource(R.drawable.map_icon);
                } else {
                    map_layout.setVisibility(View.GONE);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadFile(String uRl, String dir, String filename) {
        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Downloading")
                .setDestinationInExternalPublicDir(dir, filename);
        mgr.enqueue(request);
    }

    private void setDefaultText() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.agendaModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.agendaModuleid);
        }
    }
}