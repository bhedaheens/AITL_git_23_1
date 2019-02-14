package com.allintheloop.Fragment.QandAModule;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.QADetailModuleVoteListing_Adapter;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.QAList.QandADetail_VoteListing;
import com.allintheloop.Bean.UidCommonKeyClass;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.allintheloop.Util.GlobalData.updateQandADetailModule;

/**
 * A simple {@link Fragment} subclass.
 */
public class QADetailModule_Fragment extends Fragment implements VolleyInterface {

    TextView txt_sessionName, txt_sessiondesc, txt_sessiondate, txt_startTime, txt_endTime;
    EditText edt_message;
    CheckBox chk_anonymus;
    Button btn_send;
    TextView ask_question, txt_viewHidden, ask_question_fullLayout;
    SessionManager sessionManager;
    Bundle bundle;
    String sessionID = "";
    String res_sessionId, res_sessionName, res_sessionDesc, res_Moderator_speaker_id, str_message, str_time;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "Q&ADetailModule";
    ImageView img_close, img_question;
    Dialog dialog, voteDialog;
    RecyclerView rv_viewQaVoteData;
    ArrayList<QandADetail_VoteListing> qandADetail_voteListings;
    LinearLayout linear_askHideQuestion, linear_askQuestion;
    QADetailModuleVoteListing_Adapter voteListing_adapter;
    String android_id;
    Button btn_refresh;
    LinearLayout linear_timeDate, linear_moderatorInstruction;
    DefaultLanguage.DefaultLang defaultLang;
    TextView txt_start, txt_end, txt_hint, txt_moderaotInstrucion;
    String str_closedQa = "";
    UidCommonKeyClass uidCommonKeyClass;

    EditText edt_number;
    ImageView img_closeVoteUpDialog;
    Button btn_submitvoteUpdialog;
    GradientDrawable drawable;

    private BroadcastReceiver updateData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateData();
        }
    };


    public QADetailModule_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        View rootView = inflater.inflate(R.layout.fragment_qadetail_module, container, false);
        initView(rootView);
        varibleInitialize();
        setdailog();
        textviewValueset();
        onClick();
        setButtonDrawble();
        getListData();
        return rootView;

    }

    private void onClick() {
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.keyboradHidden(edt_message);
                dialog.dismiss();
            }
        });


        sessionID = sessionManager.getQaSessionId();
        sessionManager.strModuleId = sessionID;
        sessionManager.strMenuId = "50";


        ask_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
            }
        });

        linear_askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        txt_viewHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.QaHideQuestionModule_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    if (edt_message.getText().length() != 0) {
                        sessionManager.keyboradHidden(edt_message);
                        str_message = edt_message.getText().toString();
                        if (sessionManager.isLogin()) {
                            if (chk_anonymus.isChecked()) {
                                Log.d("AITL isChecked", "" + chk_anonymus.isChecked());
                                sendMessagewithoutUserId();
                            } else {
                                Log.d("AITL isChecked", "" + chk_anonymus.isChecked());
                                sendMessagewithtUserId();
                            }
                        } else {
                            Log.d("AITL  isChecked", "" + chk_anonymus.isChecked());
                            sendMessagewithoutUserId();
                        }
                    } else {
                        ToastC.show(getActivity(), "Please Enter Message");
                    }
                } else {
                    ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
                }

            }
        });


        linear_moderatorInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.QaModeratorInstructionModule_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRefreshData();
            }
        });

    }

    private void textviewValueset() {
        chk_anonymus.setText(defaultLang.get50Anonymous());
        ask_question.setText(defaultLang.get50AskAQuestion());
        btn_refresh.setText(defaultLang.get50Refresh());
        txt_hint.setText(defaultLang.get50AskYourOwnQuestionOrVoteOtherToTheTop());
        txt_end.setText(defaultLang.get50End() + " : ");
        txt_start.setText(defaultLang.get50Start() + " : ");
    }

    private void varibleInitialize() {
        android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        qandADetail_voteListings = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());

        defaultLang = sessionManager.getMultiLangString();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        bundle = getArguments();


        if (GlobalData.checkForUIDVersion()) {
            uidCommonKeyClass = sessionManager.getUidCommonKey();
            if (uidCommonKeyClass.getIsQaModeratorUser().equalsIgnoreCase("1")) {
                linear_askHideQuestion.setVisibility(View.VISIBLE);
                linear_moderatorInstruction.setVisibility(View.VISIBLE);
                linear_askQuestion.setVisibility(View.GONE);
            } else {
                linear_askHideQuestion.setVisibility(View.GONE);
                linear_moderatorInstruction.setVisibility(View.GONE);
                linear_askQuestion.setVisibility(View.VISIBLE);
            }
        } else {

            if (sessionManager.isModerater().equalsIgnoreCase("1") && // changes applied
                    sessionManager.getRolId().equalsIgnoreCase("7")) { // changes applied
                linear_askHideQuestion.setVisibility(View.VISIBLE);
                linear_moderatorInstruction.setVisibility(View.VISIBLE);
                linear_askQuestion.setVisibility(View.GONE);
            } else {
                linear_askHideQuestion.setVisibility(View.GONE);
                linear_moderatorInstruction.setVisibility(View.GONE);
                linear_askQuestion.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setdailog() {
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.qa_askquestion_dialog);
        edt_message = (EditText) dialog.findViewById(R.id.edt_message);
        chk_anonymus = (CheckBox) dialog.findViewById(R.id.chk_anonymus);
        img_close = (ImageView) dialog.findViewById(R.id.img_close);
        btn_send = (Button) dialog.findViewById(R.id.btn_send);
    }

    private void initView(View rootView) {
        txt_sessionName = (TextView) rootView.findViewById(R.id.txt_sessionName);
        txt_sessiondate = (TextView) rootView.findViewById(R.id.txt_sessiondate);
        txt_start = (TextView) rootView.findViewById(R.id.txt_start);
        txt_end = (TextView) rootView.findViewById(R.id.txt_end);
        txt_hint = (TextView) rootView.findViewById(R.id.txt_hint);
        txt_endTime = (TextView) rootView.findViewById(R.id.txt_endTime);
        txt_startTime = (TextView) rootView.findViewById(R.id.txt_startTime);
        txt_moderaotInstrucion = (TextView) rootView.findViewById(R.id.txt_moderaotInstrucion);
        txt_sessiondesc = (TextView) rootView.findViewById(R.id.txt_sessiondesc);
        rv_viewQaVoteData = (RecyclerView) rootView.findViewById(R.id.rv_viewQaVoteData);
        linear_timeDate = (LinearLayout) rootView.findViewById(R.id.linear_timeDate);
        linear_askHideQuestion = (LinearLayout) rootView.findViewById(R.id.linear_askHideQuestion);
        linear_askQuestion = (LinearLayout) rootView.findViewById(R.id.linear_askQuestion);
        linear_moderatorInstruction = (LinearLayout) rootView.findViewById(R.id.linear_moderatorInstruction);

        ask_question = (TextView) rootView.findViewById(R.id.ask_question);
        ask_question_fullLayout = (TextView) rootView.findViewById(R.id.ask_question_fullLayout);
        txt_viewHidden = (TextView) rootView.findViewById(R.id.txt_viewHidden);
        btn_refresh = (Button) rootView.findViewById(R.id.btn_refresh);

        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(13.0f);
    }

    private void setButtonDrawble() {

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {

            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_send.setBackgroundDrawable(drawable);
            btn_send.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));


            ask_question.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            ask_question.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

            ask_question_fullLayout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            ask_question_fullLayout.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

            linear_moderatorInstruction.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            txt_moderaotInstrucion.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));


            txt_viewHidden.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            txt_viewHidden.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));


        } else {
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_send.setBackgroundDrawable(drawable);
            btn_send.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));


            ask_question.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            ask_question.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            ask_question_fullLayout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            ask_question_fullLayout.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            linear_moderatorInstruction.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            txt_moderaotInstrucion.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));


            txt_viewHidden.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            txt_viewHidden.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

    }

    private void getListData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            qandADetail_voteListings.clear();
            Cursor cursor = sqLiteDatabaseHandler.getAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionID, tag);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.AtteendeeSpeakerDetail_Data)));
                        Log.d("AITL Q&A Oflline", jsonObject.toString());
                        loadData(jsonObject, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getQaDetailSession, Param.getSesionDetail(sessionManager.getEventId(), sessionID, sessionManager.getUserId(), android_id), 0, false, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getQaDetailSession, Param.getSesionDetail(sessionManager.getEventId(), sessionID, sessionManager.getUserId(), android_id), 0, false, this);
            }


        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionID, tag);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.AtteendeeSpeakerDetail_Data)));
                        Log.d("AITL  Q&A Oflline", jsonObject.toString());
                        loadData(jsonObject, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void sendMessagewithoutUserId() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.qAsendMessage, Param.qASendMessage(sessionManager.getEventId(), sessionID, "", res_Moderator_speaker_id, str_message, str_closedQa), 1, true, this);
    }

    private void sendMessagewithtUserId() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.qAsendMessage, Param.qASendMessage(sessionManager.getEventId(), sessionID, sessionManager.getUserId(), res_Moderator_speaker_id, str_message, str_closedQa), 1, true, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        qandADetail_voteListings.clear();
                        if (sqLiteDatabaseHandler.isAttdeeSpeaker_DetailExist(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionID, tag)) {
                            sqLiteDatabaseHandler.deleteAttdeeSpeaker_DetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionID, tag);
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionID, tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionID, tag);
                        }
                        loadData(jsonObject, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        if (chk_anonymus.isChecked())
                            chk_anonymus.setChecked(false);
                        updateData();
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        edt_message.setText("");
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        qandADetail_voteListings.clear();
                        if (voteDialog != null && voteDialog.isShowing())
                            voteDialog.dismiss();
                        if (sqLiteDatabaseHandler.isAttdeeSpeaker_DetailExist(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionID, tag)) {
                            sqLiteDatabaseHandler.deleteAttdeeSpeaker_DetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionID, tag);
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionID, tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionID, tag);
                        }
                        loadData(jsonObject, false);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    public void loadData(JSONObject jsonObject, boolean isFromAdaper) {
        try {

            qandADetail_voteListings.clear();
            if (isFromAdaper) {
                if (sqLiteDatabaseHandler.isAttdeeSpeaker_DetailExist(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionID, tag)) {
                    sqLiteDatabaseHandler.deleteAttdeeSpeaker_DetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionID, tag);
                    sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionID, tag);
                } else {
                    sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObject.toString(), sessionID, tag);
                }
            }

            setButtonDrawble();
            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject jsonData = jsonArrayData.getJSONObject(i);
                res_sessionId = jsonData.getString("session_id");
                res_sessionName = jsonData.getString("Session_name");
                res_sessionDesc = jsonData.getString("Session_description");
                str_closedQa = jsonData.getString("is_closed_qa");
                res_Moderator_speaker_id = jsonData.getString("Moderator_speaker_id");
                linear_timeDate.setVisibility(View.GONE);
                txt_sessiondate.setText(jsonData.getString("session_date"));
//                txt_sessiontime.setText("START : "+jsonData.getString("start_time")+" END : "+jsonData.getString("end_time"));
                txt_startTime.setText(jsonData.getString("start_time"));
                txt_endTime.setText(jsonData.getString("end_time"));
                txt_sessionName.setText(res_sessionName);
                if (res_sessionDesc.equalsIgnoreCase("")) {
                    txt_sessiondesc.setVisibility(View.GONE);
                } else {
                    txt_sessiondesc.setVisibility(View.GONE);
                    txt_sessiondesc.setText(res_sessionDesc);
                }

                JSONArray jsonArraymessages = jsonData.getJSONArray("messages");
                for (int j = 0; j < jsonArraymessages.length(); j++) {
                    JSONObject index = jsonArraymessages.getJSONObject(j);
                    qandADetail_voteListings.add(new QandADetail_VoteListing(index.getString("Id")
                            , index.getString("Message")
                            , index.getString("username")
                            , index.getString("Logo")
                            , index.getString("votes")
                            , index.getString("users_vote")
                            , index.getString("is_higgest")
                            , sessionID
                            , index.getString("Role_id")
                            , index.getString("user_id")
                            , android_id
                            , index.getString("qa_approved")
                            , index.getString("show_on_web")));
                }

                if (qandADetail_voteListings.size() == 0) {

                    rv_viewQaVoteData.setVisibility(View.GONE);
                } else {
                    voteListing_adapter = new QADetailModuleVoteListing_Adapter(qandADetail_voteListings, getActivity(), this, sessionManager);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rv_viewQaVoteData.setVisibility(View.VISIBLE);
                    rv_viewQaVoteData.setLayoutManager(mLayoutManager);
                    rv_viewQaVoteData.setItemAnimator(new DefaultItemAnimator());
                    rv_viewQaVoteData.setAdapter(voteListing_adapter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openModeratorVoteDialog(QandADetail_VoteListing voteListingObj) {
        voteDialog = new Dialog(getActivity());
        voteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        voteDialog.setContentView(R.layout.qa_voteup_dialog);
        edt_number = (EditText) voteDialog.findViewById(R.id.edt_number);
        img_closeVoteUpDialog = (ImageView) voteDialog.findViewById(R.id.img_close);
        btn_submitvoteUpdialog = (Button) voteDialog.findViewById(R.id.btn_submitvoteUpdialog);

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            btn_submitvoteUpdialog.setBackgroundDrawable(drawable);
            btn_submitvoteUpdialog.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_submitvoteUpdialog.setBackgroundDrawable(drawable);
            btn_submitvoteUpdialog.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

        img_closeVoteUpDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.keyboradHidden(edt_number);
                voteDialog.dismiss();
            }
        });

        btn_submitvoteUpdialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_number.getText().length() > 0) {

                    String str = edt_number.getText().toString();
                    StringBuilder digits = new StringBuilder();
                    for (int i = 0; i < str.length(); i++) {
                        int c = Integer.parseInt(String.valueOf(str.charAt(i)));
                        digits.append(c);
                    }
                    String[] digitArray = digits.toString().split("");
                    int sum = 0;
                    for (String d : digitArray) {
                        if (!d.isEmpty()) {
                            sum += Integer.parseInt(d);
                        }
                    }
                    if (edt_number.getText().toString().equalsIgnoreCase("0") || sum == 0) {
                        ToastC.show(getActivity(), getString(R.string.str_qagreaterthazero));
                    } else {
                        sessionManager.keyboradHidden(edt_number);
                        updateModeratorVote(voteListingObj.getId(), edt_number.getText().toString());
                    }

                } else {
                    ToastC.show(getActivity(), getString(R.string.qa_customevote_edtstring));
                }
            }
        });
        voteDialog.setCancelable(false);
        voteDialog.show();

    }


    public void updateModeratorVote(String messageId, String vote) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.qAModeratorUpVote, Param.qaModeratorUpVote(sessionManager.getEventId(), sessionID, sessionManager.getUserId(), messageId, vote), 2, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void updateData() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getQaDetailSession, Param.getSesionDetail(sessionManager.getEventId(), sessionID, sessionManager.getUserId(), android_id), 0, false, this);
    }

    private void updateRefreshData() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getQaDetailSession, Param.getSesionDetail(sessionManager.getEventId(), sessionID, sessionManager.getUserId(), android_id), 0, true, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(updateData, new IntentFilter(updateQandADetailModule));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateData);
    }

}
