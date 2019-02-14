package com.allintheloop.Fragment.EditProfileModule;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

public class ProfileEditGolas_Activity extends AppCompatActivity implements VolleyInterface {

    BoldTextView txt_count, btn_save;
    EditText edt_goals;
    ImageView img_back;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_golas_);
        edt_goals = findViewById(R.id.edt_goals);
        txt_count = findViewById(R.id.txt_count);
        btn_save = findViewById(R.id.btn_save);
        img_back = findViewById(R.id.img_back);
        sessionManager = new SessionManager(this);
        if (!sessionManager.getProfileGoal().isEmpty()) {
            txt_count.setText(sessionManager.getProfileGoal().length() + "/140");
            edt_goals.setText(sessionManager.getProfileGoal());
        }
        edt_goals.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt_count.setText(s.length() + "/140");
                if (s.length() <= 140) {
                    btn_save.setEnabled(true);

                    txt_count.setTextColor(getResources().getColor(R.color.black));
                } else {
                    btn_save.setEnabled(false);
                    txt_count.setTextColor(getResources().getColor(R.color.red));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_goals.getText().length() > 0) {
                    saveGoals(edt_goals.getText().toString());
                } else {
                    ToastC.show(getApplicationContext(), "Please enter goal");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GlobalData.hideSoftKeyboard(ProfileEditGolas_Activity.this);
        finish();

    }

    public void saveGoals(String goals) {
        if (GlobalData.isNetworkAvailable(this)) {
            new VolleyRequest(this, VolleyRequest.Method.POST, MyUrls.saveProfileGoals, Param.saveProfileGoals(sessionManager.getUserId(), sessionManager.getEventId(), goals), 0, true, this);
        } else {
            ToastC.show(getApplicationContext(), getResources().getString(R.string.noInernet));
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        GlobalData.hideSoftKeyboard(this);
                        setResult(RESULT_OK, new Intent().putExtra("goals", edt_goals.getText().toString()));
                        finish();
                    }
                } catch (Exception e) {

                }
                break;
        }
    }
}
