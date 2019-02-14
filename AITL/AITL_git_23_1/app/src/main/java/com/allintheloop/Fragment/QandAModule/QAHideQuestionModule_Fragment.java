package com.allintheloop.Fragment.QandAModule;


import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.QAHideQuestionModule_Adapter;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.QAList.QandADetail_VoteListing;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class QAHideQuestionModule_Fragment extends Fragment implements VolleyInterface {


    TextView txt_sessionName, txt_sessiondesc, txt_sessiondate, txt_startTime, txt_endTime;

    TextView txt_viewAllQuestion;
    SessionManager sessionManager;
    Bundle bundle;
    String sessionID = "";
    String res_sessionId, res_sessionName, res_sessionDesc, res_Moderator_speaker_id, str_message, str_time;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    RecyclerView rv_viewQaVoteData;
    ArrayList<QandADetail_VoteListing> qandADetail_voteListings;
    QAHideQuestionModule_Adapter qaHideQuestionModule_adapter;
    String android_id;
    Button btn_refresh;
    LinearLayout linear_timeDate;
    DefaultLanguage.DefaultLang defaultLang;
    TextView txt_start, txt_end, txt_hint;
    String str_closedQa = "";

    public QAHideQuestionModule_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qahide_question_module_, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        initView(rootView);
        varibleInitialize();
        textviewValueset();
        onClick();
        setButtonDrawble();
        getListData();
        return rootView;
    }

    private void onClick() {

        sessionID = sessionManager.getQaSessionId();
        sessionManager.strModuleId = sessionID;
        sessionManager.strMenuId = "50";


        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListData();
            }
        });


        txt_viewAllQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).fragmentBackStackMaintain();
            }
        });

    }


    private void textviewValueset() {
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


    }

    private void initView(View rootView) {
        txt_sessionName = (TextView) rootView.findViewById(R.id.txt_sessionName);
        txt_sessiondate = (TextView) rootView.findViewById(R.id.txt_sessiondate);
        txt_start = (TextView) rootView.findViewById(R.id.txt_start);
        txt_end = (TextView) rootView.findViewById(R.id.txt_end);
        txt_hint = (TextView) rootView.findViewById(R.id.txt_hint);
        txt_endTime = (TextView) rootView.findViewById(R.id.txt_endTime);
        txt_startTime = (TextView) rootView.findViewById(R.id.txt_startTime);
        txt_sessiondesc = (TextView) rootView.findViewById(R.id.txt_sessiondesc);
        rv_viewQaVoteData = (RecyclerView) rootView.findViewById(R.id.rv_viewQaVoteData);
        linear_timeDate = (LinearLayout) rootView.findViewById(R.id.linear_timeDate);

        txt_viewAllQuestion = (TextView) rootView.findViewById(R.id.txt_viewAllQuestion);
        btn_refresh = (Button) rootView.findViewById(R.id.btn_refresh);

    }

    private void setButtonDrawble() {

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            txt_viewAllQuestion.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            txt_viewAllQuestion.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            txt_viewAllQuestion.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            txt_viewAllQuestion.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

    }

    private void getListData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getHiddenQuestion, Param.getSesionDetail(sessionManager.getEventId(), sessionID, sessionManager.getUserId(), android_id), 0, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:

                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        loadData(jsonObject);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public void loadData(JSONObject jsonObject) {
        try {

            qandADetail_voteListings.clear();
            setButtonDrawble();
            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject jsonData = jsonArrayData.getJSONObject(i);
                res_sessionId = jsonData.getString("session_id");
                res_sessionName = jsonData.getString("Session_name");
                res_sessionDesc = jsonData.getString("Session_description");
                str_closedQa = jsonData.getString("is_closed_qa");
                res_Moderator_speaker_id = jsonData.getString("Moderator_speaker_id");
                linear_timeDate.setVisibility(View.VISIBLE);
                txt_sessiondate.setText(jsonData.getString("session_date"));
//                txt_sessiontime.setText("START : "+jsonData.getString("start_time")+" END : "+jsonData.getString("end_time"));
                txt_startTime.setText(jsonData.getString("start_time"));
                txt_endTime.setText(jsonData.getString("end_time"));
                txt_sessionName.setText(res_sessionName);
                if (res_sessionDesc.equalsIgnoreCase("")) {
                    txt_sessiondesc.setVisibility(View.GONE);
                } else {
                    txt_sessiondesc.setVisibility(View.VISIBLE);
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
                    qaHideQuestionModule_adapter = new QAHideQuestionModule_Adapter(qandADetail_voteListings, getActivity(), this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rv_viewQaVoteData.setVisibility(View.VISIBLE);
                    rv_viewQaVoteData.setLayoutManager(mLayoutManager);
                    rv_viewQaVoteData.setItemAnimator(new DefaultItemAnimator());
                    rv_viewQaVoteData.setAdapter(qaHideQuestionModule_adapter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
