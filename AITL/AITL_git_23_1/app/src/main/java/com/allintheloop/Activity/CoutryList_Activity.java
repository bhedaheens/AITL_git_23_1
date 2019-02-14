package com.allintheloop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.allintheloop.Adapter.CountryAdapter;
import com.allintheloop.Bean.CountryList;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CoutryList_Activity extends AppCompatActivity implements VolleyInterface {


    ListView listView;
    EditText search;
    ArrayList<CountryList> countryArray;
    ArrayList<CountryList> stateArray;
    String country, countryId;
    CountryAdapter adapter;
    Intent i;
    String status;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coutry_list);
        listView = (ListView) findViewById(R.id.listView);
        search = (EditText) findViewById(R.id.edtSearch);
        countryArray = new ArrayList<CountryList>();
        stateArray = new ArrayList<>();
        sessionManager = new SessionManager(getApplicationContext());
        i = getIntent();

        status = i.getStringExtra("status");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (status.equalsIgnoreCase("country")) {

                    Log.d("AITL", "CountryList" + countryArray.get(position).getId() + "Name : " + countryArray.get(position).getName());

                    Intent i = new Intent().putExtra("code", countryArray.get(position).getId()).putExtra("name", countryArray.get(position).getName()).putExtra("status", status);
                    setResult(RESULT_OK, i);
                    finish();
                } else if (status.equalsIgnoreCase("state")) {
                    Log.d("AITL", "StateList" + stateArray.get(position).getId() + "Name : " + stateArray.get(position).getName());

                    Intent i = new Intent().putExtra("code", stateArray.get(position).getId()).putExtra("name", stateArray.get(position).getName()).putExtra("status", status);
                    setResult(RESULT_OK, i);
                    finish();

                }
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (status.equalsIgnoreCase("country")) {
                    if (countryArray.size() > 0) {
                        adapter.filter(s.toString());
                    }

                } else if (status.equalsIgnoreCase("state")) {
                    if (stateArray.size() > 0) {
                        adapter.filter(s.toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (status.equalsIgnoreCase("country")) {
                    if (countryArray.size() > 0) {
                        adapter.filter(s.toString());
                    }

                } else if (status.equalsIgnoreCase("state")) {
                    if (stateArray.size() > 0) {
                        adapter.filter(s.toString());
                    }
                }
            }
        });
        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
            if (status.equalsIgnoreCase("country")) {

                new VolleyRequest(CoutryList_Activity.this, VolleyRequest.Method.POST, MyUrls.Get_CountryList, Param.GetCountryList(), 0, true, this);
            } else if (status.equalsIgnoreCase("state")) {

                new VolleyRequest(CoutryList_Activity.this, VolleyRequest.Method.POST, MyUrls.Get_StateList, Param.GetStateList(sessionManager.getEventId(), getIntent().getStringExtra("country_code"), sessionManager.getToken()), 1, true, this);
            }
        } else {
            ToastC.show(getApplicationContext(), getResources().getString(R.string.noInernet));
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {

            case 0:
                try {

                    JSONObject mainObj = new JSONObject(volleyResponse.output);
                    if (mainObj.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        JSONArray jArrayContry = mainObj.getJSONArray("data");
                        Log.d("JsonArray", jArrayContry.toString());

                        for (int i = 0; i < jArrayContry.length(); i++) {
                            JSONObject jsonCountry = jArrayContry.optJSONObject(i);
                            country = jsonCountry.getString("country_name");
                            countryId = jsonCountry.getString("id");

                            countryArray.add(new CountryList(countryId, country, status));

                        }
                        adapter = new CountryAdapter(getApplicationContext(), R.layout.adapter_countrylist, countryArray);
                        listView.setAdapter(adapter);


                    } else if (mainObj.optString("status").equalsIgnoreCase("false")) {
                        ToastC.show(getApplicationContext(), mainObj.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                try {

                    JSONObject mainObj = new JSONObject(volleyResponse.output);
                    if (mainObj.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        JSONArray jArraystate = mainObj.getJSONArray("data");
                        Log.d("JsonArray", jArraystate.toString());
                        for (int i = 0; i < jArraystate.length(); i++) {
                            JSONObject jsonstate = jArraystate.optJSONObject(i);

                            stateArray.add(new CountryList(jsonstate.getString("id"), jsonstate.getString("state_name"), status));

                        }

                        adapter = new CountryAdapter(getApplicationContext(), R.layout.adapter_countrylist, stateArray);
                        listView.setAdapter(adapter);

                    } else if (mainObj.optString("status").equalsIgnoreCase("false")) {
                        ToastC.show(getApplicationContext(), mainObj.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }


    }
}
