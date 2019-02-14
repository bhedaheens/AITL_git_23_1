package com.allintheloop.Fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Activity.ScanQRbarCodeActivity;
import com.allintheloop.Adapter.PortalListingAdapter;
import com.allintheloop.Bean.checkInProtal;
import com.allintheloop.MainActivity;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckIn_Portal_Fragment extends Fragment implements VolleyInterface {

    Dialog dialog;
    TextView textViewNoDATA;
    EditText edt_search;
    ArrayList<checkInProtal> checkInProtalArrayList;
    RecyclerView rv_viewAttendanceCheckIn;
    PortalListingAdapter portalListingAdapter;
    SessionManager sessionManager;
    Button btn_scan;
    String res_email, str_badge_logo;
    LinearLayout linear_button, linear_attendeeTotal;
    ImageView img_scan;
    TextView txt_totalRegister, txt_checkin;
    String pdfName = "", id;

    public CheckIn_Portal_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_in_portal, container, false);
        ((MainActivity) getActivity()).setDrawerState(false);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        txt_totalRegister = (TextView) rootView.findViewById(R.id.txt_totalRegister);
        txt_checkin = (TextView) rootView.findViewById(R.id.txt_checkin);
        linear_button = (LinearLayout) rootView.findViewById(R.id.linear_button);
        linear_attendeeTotal = (LinearLayout) rootView.findViewById(R.id.linear_attendeeTotal);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        btn_scan = (Button) rootView.findViewById(R.id.btn_scan);
        img_scan = (ImageView) rootView.findViewById(R.id.img_scan);
        rv_viewAttendanceCheckIn = (RecyclerView) rootView.findViewById(R.id.rv_viewAttendanceCheckIn);
        sessionManager = new SessionManager(getActivity());

        sessionManager.strModuleId = "CheckIn Portal";

        txt_totalRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllPortalListing();
            }
        });

        txt_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCheckingListing();
            }
        });

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(13.0f);
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_scan.setBackgroundDrawable(drawable);
            linear_button.setBackground(drawable);
            btn_scan.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

        } else {
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_scan.setBackgroundDrawable(drawable);
            linear_button.setBackground(drawable);
            btn_scan.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanActivity();
            }
        });

        img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanActivity();
            }
        });


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (checkInProtalArrayList.size() != 0) {
                    if (charSequence.length() != 0) {
                        portalListingAdapter.getFilter().filter(charSequence);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (checkInProtalArrayList.size() != 0) {
                    if (editable.length() != 0) {
                        portalListingAdapter.getFilter().filter(editable);
                    }
                }
            }
        });
        getAllPortalListing();
        return rootView;
    }


    private void getAllPortalListing() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeePortalListing, Param.getPortalListing(sessionManager.getEventId()), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void getCheckingListing() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getChekedInList, Param.getPortalListing(sessionManager.getEventId()), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void openScanActivity() {
        Intent intent = new Intent(getActivity(), ScanQRbarCodeActivity.class);
        startActivityForResult(intent, 1000);
    }

    private void checkInScan() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.checkinPortalScan, Param.getVirtualMarketDetail(sessionManager.getEventId(), res_email), 1, true, this);
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
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        loadData(jsonObject);
                    } else {
                        edt_search.setVisibility(View.VISIBLE);
                        textViewNoDATA.setVisibility(View.VISIBLE);
                        rv_viewAttendanceCheckIn.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        Log.d("AITL JSONDATA", jsonObject.toString());
                        loadData(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2: {
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonPdf = jsonObject.getJSONObject("data");
                        pdfName = jsonPdf.getString("pdf_name");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            if (!pdfName.isEmpty())
                                generatePdf();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void loadData(JSONObject jsonObject) {
        try {
            linear_attendeeTotal.setVisibility(View.VISIBLE);
            if (jsonObject.has("data")) {
                checkInProtalArrayList = new ArrayList<>();
                JSONObject jsonData = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonData.getJSONArray("attendee_list");

                txt_checkin.setText(jsonData.getString("total_checkedin") + " " + "CheckedIn");
                txt_totalRegister.setText(jsonData.getString("total_registerd") + " " + "Registered");
                sessionManager.set_checkingBadgeLogo(jsonData.getString("badge_logo"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    checkInProtalArrayList.add(new checkInProtal(object.getString("Id")
                            , object.getString("Firstname")
                            , object.getString("Lastname")
                            , object.getString("Company_name")
                            , object.getString("Title")
                            , object.getString("Email")
                            , object.getString("Logo")
                            , object.getString("is_checked_in")));
                }

                if (checkInProtalArrayList.size() > 0) {

                    textViewNoDATA.setVisibility(View.GONE);
                    rv_viewAttendanceCheckIn.setVisibility(View.VISIBLE);
                    portalListingAdapter = new PortalListingAdapter(checkInProtalArrayList, getActivity(), CheckIn_Portal_Fragment.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    rv_viewAttendanceCheckIn.setLayoutManager(mLayoutManager);
                    rv_viewAttendanceCheckIn.setItemAnimator(new DefaultItemAnimator());
                    rv_viewAttendanceCheckIn.setAdapter(portalListingAdapter);
                } else {
                    textViewNoDATA.setVisibility(View.VISIBLE);
                    rv_viewAttendanceCheckIn.setVisibility(View.GONE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                if (data.getExtras() != null) {
                    res_email = data.getExtras().getString("result");
                    checkInScan();
                }
            }
        }
    }


    public void openPDFView(String id) {
        this.id = id;
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getCheckinPdf, Param.getCheckinPdf(sessionManager.getEventId(), id), 2, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void generatePdf() {
//        pdfView.getSettings().setJavaScriptEnabled(true);
//        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +pdfName);

        PrintDocumentAdapter pda = new PrintDocumentAdapter() {
            @Override
            public void onWrite(PageRange[] pages, final ParcelFileDescriptor destination, CancellationSignal cancellationSignal, final WriteResultCallback callback) {
                new OpenPDFView(pdfName, destination, callback).execute();
            }

            @Override
            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {

                if (cancellationSignal.isCanceled()) {
                    callback.onLayoutCancelled();
                    return;
                }
                PrintDocumentInfo pdi = new PrintDocumentInfo.Builder(id).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build();

                callback.onLayoutFinished(pdi, true);
            }
        };

        PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
        printManager.print("FirstFile", pda, null);
    }


    public class OpenPDFView extends AsyncTask<Void, Void, Void> {
        String url;
        ParcelFileDescriptor destination;
        PrintDocumentAdapter.WriteResultCallback callback;

        public OpenPDFView(String url, ParcelFileDescriptor destination, PrintDocumentAdapter.WriteResultCallback callback) {
            this.url = url;
            this.destination = destination;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            InputStream input = null;
            OutputStream output = null;

            try {
                input = new URL(url).openStream();
                output = new FileOutputStream(destination.getFileDescriptor());

                byte[] buf = new byte[1024];
                int bytesRead;

                while ((bytesRead = input.read(buf)) > 0) {
                    output.write(buf, 0, bytesRead);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                }

            } catch (FileNotFoundException ee) {
                //TODO Handle Exception
            } catch (Exception e) {
                //TODO Handle Exception
            }

            try {
                input.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            new getExhibitorInAsyncTask().execute();
            //  getExhibitors();
            super.onPostExecute(aVoid);
        }
    }
}
