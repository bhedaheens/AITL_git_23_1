package com.allintheloop.Fragment.MapModule;


//import android.app.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.ImageMapAgendaDialogAdapter;
import com.allintheloop.Bean.Map.ImageMappingDetail;
import com.allintheloop.Bean.Map.MapCoordinatesDetails;
import com.allintheloop.Bean.Map.MapImageSession;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * A simple {@link Fragment} subclass.
 */
public class Map_Detail_Fragment extends Fragment implements VolleyInterface {

    public static Dialog agenda_dialog;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    Matrix mat;
    SessionManager sessionManager;
    String latitude, longitude, sateliteview, include_map, map_desc, str_area_name, img, place_name, img_url, map_title;
    String coords, user_id, headig, stand_number, exPage_id, exid, firstname, lastname, email, company_logo;
    String top, left, right, bottom;
    WebView webView;
    TextView area_name;
    // ImageView detail_image;
    ImageView detail_image;
    MapView mMapView;
    GoogleMap googleMap;
    Bundle bundle;
    ArrayList<ImageView> img_arrayList;
    ArrayList<ImageMappingDetail> img_MappingArray;
    ArrayList<String> coordArrayList;
    int index;
    RelativeLayout img_layout;
    // FrameLayout img_layout;
    int imgHeight, imgwidth;
    LinearLayout linear_speakerDialog;
    TextView txt_heading, txt_stndNumber, txt_nodata;
    ImageView speaker_img, btnclose;
    Dialog exhibiore_dialog;
    List<MapCoordinatesDetails> rectangles = new ArrayList<>();
    ArrayList<MapImageSession> imageSessions;
    RecyclerView rv_agendaList;
    ImageMapAgendaDialogAdapter imageMapAgendaDialogAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    String str_sId, str_sHeading, str_sSDate, str_sTime, isSession = "";
    Paint paint;
    Bitmap bg;
    Canvas canvas;
    ProgressBar progressBar1;
    PhotoViewAttacher mAttacher;
    JSONArray jArrayimage;
    int pos = 0;
    int height;
    LinearLayout desc_layout;
    CardView card_nomap;
    Canvas canvas1;
    Paint paint1;
    DisplayMetrics metrics;
    List<String> permissionsNeeded;
    List<String> permissionsList;
    boolean isagendaDialog = false;

    //    ZoomControls zoomControls1;
//    int x=0,y=0;
    public Map_Detail_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map__detail, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        bundle = getArguments();
        sessionManager = new SessionManager(getActivity());

        GlobalData.currentModuleForOnResume = GlobalData.mapModuleid;
        webView = (WebView) rootView.findViewById(R.id.map_desc);
        area_name = (TextView) rootView.findViewById(R.id.area_name);
        detail_image = (ImageView) rootView.findViewById(R.id.detail_image);
        desc_layout = (LinearLayout) rootView.findViewById(R.id.desc_layout);
        card_nomap = (CardView) rootView.findViewById(R.id.card_nomap);

        img_MappingArray = new ArrayList<>();
        coordArrayList = new ArrayList<>();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        img_arrayList = new ArrayList<>();

        img_layout = (RelativeLayout) rootView.findViewById(R.id.img_layout);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();// needed to get the map to display immediately

        sessionManager.strModuleId = sessionManager.getMapid();
//        sessionManager.setMenuid("10");
        sessionManager.strMenuId = "10";

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_MapDetailUid, Param.getMapDetail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getMapid()), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_MapDetail, Param.getMapDetail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getMapid()), 0, false, this);
        }


        return rootView;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {
                            final JSONObject jsonData = jsonObject.getJSONObject("data");
                            final JSONArray jsonArray = jsonData.getJSONArray("map_details");
                            jArrayimage = jsonData.getJSONArray("image_mapping_details");

                            if (jsonArray.length() == 0) {
                                desc_layout.setVisibility(View.GONE);
                                card_nomap.setVisibility(View.VISIBLE);
                            } else {
                                desc_layout.setVisibility(View.VISIBLE);
                                card_nomap.setVisibility(View.GONE);


                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    str_area_name = object.getString("area");
                                    map_desc = object.getString("Map_desc");
                                    sateliteview = object.getString("satellite_view");
                                    include_map = object.getString("include_map");
                                    place_name = object.getString("place");
                                    latitude = object.getString("lat");
                                    longitude = object.getString("long");
                                    map_title = object.getString("Map_title");
                                    img = object.getString("Images");
                                    if (!(img.equalsIgnoreCase(""))) {
                                        JSONObject dimention = object.getJSONObject("dimensions");
                                        imgHeight = dimention.getInt("height");
                                        imgwidth = dimention.getInt("width");

                                        Resources resources = getActivity().getResources();
                                        metrics = resources.getDisplayMetrics();

                                        img_url = MyUrls.Imgurl + img;
                                        Log.d("AITL MAP IMAGE", img_url);
                                        Log.d("AITL MAP HEIGHT", "" + imgHeight);
                                        Log.d("AITL MAP WIDTH", "" + imgwidth);
                                        Log.i("niral", "HEIGHT  :" + (metrics.heightPixels - 800));
                                        height = metrics.heightPixels - 400;

                                        Glide.with(getActivity()).load(img_url).asBitmap().dontTransform().override(imgwidth, imgHeight).listener(new RequestListener<String, Bitmap>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                                detail_image.setImageBitmap(resource);

                                                ViewGroup.LayoutParams layoutParams = detail_image.getLayoutParams();
                                                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                layoutParams.height = height;
                                                detail_image.setLayoutParams(layoutParams);

                                                mAttacher = new PhotoViewAttacher(detail_image);
                                                mAttacher.setMaximumScale(20.0f);
                                                coordinateData(jArrayimage, resource);
                                                mAttacher.setOnScaleChangeListener(new PhotoViewAttacher.OnScaleChangeListener() {
                                                    @Override
                                                    public void onScaleChange(float scaleFactor, float focusX, float focusY) {
                                                        detail_image.setImageMatrix(mat);
                                                    }
                                                });
                                                mAttacher.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                                                    @Override
                                                    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {

                                                        float[] values = new float[9];
                                                        mat.getValues(values);

                                                        float touchX = (motionEvent.getX() - values[2]) / values[0];
                                                        float touchY = (motionEvent.getY() - values[5]) / values[4];

                                                        switch (motionEvent.getAction()) {

                                                            case MotionEvent.ACTION_DOWN:

                                                                for (int i = 0; i < img_MappingArray.size(); i++) {


                                                                    Rect rect = rectangles.get(i).getRect();

                                                                    if (rect.contains((int) touchX, (int) touchY)) {

                                                                        Log.d("AITL SESSION ", "" + img_MappingArray.get(i).getIsSession());
                                                                        Log.d("AITL", "ID" + img_MappingArray.get(i).getExid());

                                                                        if (img_MappingArray.get(i).getIsSession().equalsIgnoreCase("1")) {

                                                                            agenda_dialog = new Dialog(getActivity());
                                                                            agenda_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                            agenda_dialog.setContentView(R.layout.image_map_agenda_dailog);
                                                                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                                                            Window window = agenda_dialog.getWindow();
                                                                            lp.copyFrom(window.getAttributes());
                                                                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                                                            window.setAttributes(lp);

                                                                            rv_agendaList = (RecyclerView) agenda_dialog.findViewById(R.id.rv_agendaList);
                                                                            btnclose = (ImageView) agenda_dialog.findViewById(R.id.btnclose);
                                                                            txt_nodata = (TextView) agenda_dialog.findViewById(R.id.txt_nodata);

                                                                            Log.d("AITL", "DIlog array size" + img_MappingArray.get(i).getImageSessions().size());

//                                                                        if (img_MappingArray.get(pos).getImageSessions().size() != 0)
//                                                                        {
                                                                            rv_agendaList.setVisibility(View.VISIBLE);
                                                                            txt_nodata.setVisibility(View.GONE);
                                                                            imageMapAgendaDialogAdapter = new ImageMapAgendaDialogAdapter(img_MappingArray.get(i).getImageSessions(), getActivity());
                                                                            mLayoutManager = new LinearLayoutManager(getActivity());
                                                                            rv_agendaList.setLayoutManager(mLayoutManager);
                                                                            rv_agendaList.setItemAnimator(new DefaultItemAnimator());
                                                                            rv_agendaList.setAdapter(imageMapAgendaDialogAdapter);

                                                                            btnclose.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    agenda_dialog.dismiss();
                                                                                }
                                                                            });

                                                                            agenda_dialog.show();
                                                                        } else if (!(img_MappingArray.get(i).getExid().equalsIgnoreCase(""))) {
                                                                            Log.d("AITL", "Speaker");
                                                                            exhibiore_dialog = new Dialog(getActivity());
                                                                            exhibiore_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                            exhibiore_dialog.setContentView(R.layout.image_map_speaker_dialog);
                                                                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                                                            Window window = exhibiore_dialog.getWindow();
                                                                            lp.copyFrom(window.getAttributes());
                                                                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                                                            window.setAttributes(lp);
                                                                            txt_heading = (TextView) exhibiore_dialog.findViewById(R.id.txt_heading);
                                                                            txt_stndNumber = (TextView) exhibiore_dialog.findViewById(R.id.txt_stndNumber);

                                                                            linear_speakerDialog = (LinearLayout) exhibiore_dialog.findViewById(R.id.linear_speakerDialog);
                                                                            btnclose = (ImageView) exhibiore_dialog.findViewById(R.id.btnclose);
                                                                            speaker_img = (ImageView) exhibiore_dialog.findViewById(R.id.speaker_img);
                                                                            progressBar1 = (ProgressBar) exhibiore_dialog.findViewById(R.id.progressBar1);
                                                                            txt_heading.setText(img_MappingArray.get(i).getHeading());
                                                                            txt_stndNumber.setText("Stand Number :" + img_MappingArray.get(i).getStand_number());

                                                                            Log.d("AITL SpeakerImg", img_MappingArray.get(i).getCompanylogo());

                                                                            Glide.with(getActivity())
                                                                                    .load(img_MappingArray.get(i).getCompanylogo())
                                                                                    .crossFade()
                                                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                                                    .listener(new RequestListener<String, GlideDrawable>() {
                                                                                        @Override
                                                                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                                            progressBar1.setVisibility(View.GONE);
                                                                                            speaker_img.setVisibility(View.VISIBLE);
                                                                                            return false;
                                                                                        }

                                                                                        @Override
                                                                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                                            progressBar1.setVisibility(View.GONE);
                                                                                            speaker_img.setVisibility(View.VISIBLE);

                                                                                            return false;
                                                                                        }
                                                                                    })
                                                                                    .into(speaker_img);


                                                                            final int finalI = i;
                                                                            speaker_img.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    exhibiore_dialog.dismiss();
                                                                                    sessionManager.exhibitor_id = img_MappingArray.get(finalI).getUser_id();
                                                                                    sessionManager.exhi_pageId = img_MappingArray.get(finalI).getExid();
                                                                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                                                                    Log.d("AITL ExhibitorId", sessionManager.exhibitor_id);
                                                                                    GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                                                                                    ((MainActivity) getActivity()).loadFragment();
                                                                                }
                                                                            });
                                                                            btnclose.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    exhibiore_dialog.dismiss();
                                                                                }
                                                                            });
                                                                            exhibiore_dialog.show();
                                                                        } else if (img_MappingArray.get(i).getIsSession().equalsIgnoreCase("0")) {


                                                                            agenda_dialog = new Dialog(getActivity());
                                                                            agenda_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                            agenda_dialog.setContentView(R.layout.image_map_agenda_dailog);
                                                                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                                                            Window window = agenda_dialog.getWindow();
                                                                            lp.copyFrom(window.getAttributes());
                                                                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                                                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                                                            window.setAttributes(lp);
                                                                            rv_agendaList = (RecyclerView) agenda_dialog.findViewById(R.id.rv_agendaList);
                                                                            btnclose = (ImageView) agenda_dialog.findViewById(R.id.btnclose);
                                                                            txt_nodata = (TextView) agenda_dialog.findViewById(R.id.txt_nodata);
                                                                            rv_agendaList.setVisibility(View.GONE);
                                                                            txt_nodata.setVisibility(View.VISIBLE);
                                                                            btnclose.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    agenda_dialog.dismiss();
                                                                                }
                                                                            });


//                                                                            Log.d("Bhavdip BEFORE AGENDADAILOG",""+agenda_dialog.isShowing());
                                                                            agenda_dialog.show();
//                                                                            Log.d("Bhavdip AFTER AGENDADAILOG",""+agenda_dialog.isShowing());
                                                                        }
                                                                    }
                                                                }
                                                                break;
                                                        }

                                                        return false;

                                                    }

                                                    @Override
                                                    public boolean onDoubleTap(MotionEvent motionEvent) {
                                                        return false;
                                                    }

                                                    @Override
                                                    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
                                                        return false;
                                                    }
                                                });

                                                return false;

                                            }
                                        }).signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.RESULT).into(detail_image);
                                        //  detail_image.setLayoutParams(new RelativeLayout.LayoutParams(displayWidth, displayHeight));
                                        mat = detail_image.getImageMatrix();
                                    }
                                }
                            }
                            ((MainActivity) getActivity()).setTitle(map_title);
                            if (!str_area_name.equalsIgnoreCase("")) {
                                area_name.setVisibility(View.VISIBLE);
                                area_name.setText("Area :" + str_area_name);
                            } else {
                                area_name.setVisibility(View.GONE);
                            }

                            if (!map_desc.equalsIgnoreCase("")) {
                                webView.setVisibility(View.VISIBLE);
                                String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/Lato_Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
                                String pas = "</body></html>";
                                String myHtmlString = pish + map_desc + pas;
                                webView.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);

                                //  webView.loadData(map_desc, "text/html; charset=utf-8", "utf-8");

                            } else {
                                webView.setVisibility(View.GONE);
                            }

                            if (include_map.equalsIgnoreCase("1")) {
                                mMapView.setVisibility(View.VISIBLE);

                                if (Build.VERSION.SDK_INT >= 23) {
                                    if (isCameraPermissionGranted()) {
                                        setUpMap();
                                    } else {
                                        requestPermission();
                                    }
                                } else {
                                    setUpMap();
                                }
                            } else {
                                mMapView.setVisibility(View.GONE);
                            }
                        } else {
                            ToastC.show(getActivity(), jsonObject.getString("message"));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

    }

    private void setUpMap() {
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                double lati = Double.parseDouble(latitude);
                double longi = Double.parseDouble(longitude);


                MarkerOptions marker = new MarkerOptions().position(new LatLng(lati, longi)).title(place_name);

                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lati, longi)).zoom(10).build();
                googleMap.getUiSettings().setMapToolbarEnabled(true);
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition));
                googleMap.addMarker(marker).showInfoWindow();
                Log.d("AITL  Enabled", "" + googleMap.getUiSettings().isMapToolbarEnabled());
                if (sateliteview.equalsIgnoreCase("1")) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });

    }

    private void coordinateData(JSONArray jArrayimage, Bitmap result) {

        canvas = new Canvas(result);
        try {

            for (int j = 0; j < jArrayimage.length(); j++) {

                JSONObject jObjectimg = jArrayimage.getJSONObject(j);

                imageSessions = new ArrayList<>();

                coords = jObjectimg.getString("coords");
                user_id = jObjectimg.getString("user_id");
                headig = jObjectimg.getString("Heading");
                stand_number = jObjectimg.getString("stand_number");
                exid = jObjectimg.getString("exid");
                firstname = jObjectimg.getString("Firstname");
                lastname = jObjectimg.getString("Lastname");
                email = jObjectimg.getString("Email");
                isSession = jObjectimg.getString("is_session");
                company_logo = jObjectimg.getString("company_logo");

                JSONArray jArraySession = jObjectimg.getJSONArray("session");

                for (int s = 0; s < jArraySession.length(); s++) {

                    JSONObject jObjectSession = jArraySession.getJSONObject(s);
                    str_sId = jObjectSession.getString("sid");
                    str_sHeading = jObjectSession.getString("session_heading");
                    str_sSDate = jObjectSession.getString("Start_date");
                    str_sTime = jObjectSession.getString("Start_time");

                    imageSessions.add(new MapImageSession(str_sId, str_sHeading, str_sSDate, str_sTime));
                }

                img_MappingArray.add(new ImageMappingDetail(coords, user_id, headig, stand_number, exid, firstname, lastname, email, MyUrls.Imgurl + company_logo, isSession, imageSessions));

                String[] split_coords = coords.split(",");
                top = split_coords[0];
                left = split_coords[1];
                right = split_coords[2];
                bottom = split_coords[3];

                paint = new Paint();
                ImageView iV = new ImageView(getActivity());
                iV.setClickable(false);
                iV.setTag(j);

                paint.setStrokeWidth(8);
                paint.setColor(Color.parseColor("#00000000"));
                paint.setStyle(Paint.Style.STROKE);

                Rect rect = new Rect(Integer.parseInt(top), Integer.parseInt(left), Integer.parseInt(right), Integer.parseInt(bottom));
                canvas.drawRect(rect, paint);
                rectangles.add(new MapCoordinatesDetails(rect, headig));
                iV.setAdjustViewBounds(true);
                img_arrayList.add(iV);
                if (!(sessionManager.Map_coords.equalsIgnoreCase(""))) {
                    String ci_top = "", ci_left = "", ci_right = "", ci_bottom = "";
                    String[] split_coords1 = sessionManager.Map_coords.split(",");
                    ci_top = split_coords1[0];
                    ci_left = split_coords1[1];
                    ci_right = split_coords1[2];
                    ci_bottom = split_coords1[3];

                    if (rectangles.get(j).getRect().contains(Integer.parseInt(ci_top), Integer.parseInt(ci_left), Integer.parseInt(ci_right), Integer.parseInt(ci_bottom))) {

                        float[] pts = {rect.centerX(), rect.centerY()};

                        int calculatedX = (int) ((pts[0]) - metrics.widthPixels / 2);
                        int calculatedY = (int) ((pts[1]) - metrics.heightPixels / 2);
                        mAttacher.setScale(2.0f, calculatedX, calculatedY, true);
                        mAttacher.setZoomTransitionDuration(800);

                        Canvas canvas1 = new Canvas(result);
                        Paint paint1 = new Paint();
                        paint1.setColor(Color.RED);
                        paint1.setStyle(Paint.Style.STROKE);
                        paint1.setStrokeWidth(16);
                        canvas1.drawCircle(rect.centerX(), rect.centerY(), 130, paint1);

                        Paint paint2 = new Paint();
                        paint2.setColor(Color.RED);
                        paint2.setStyle(Paint.Style.STROKE);
                        paint2.setStrokeWidth(12);
                        canvas1.drawCircle(rect.centerX(), rect.centerY(), 60, paint2);

                        iV.setAdjustViewBounds(true);

                        int paddingPixel = 10;
                        float density = getActivity().getResources().getDisplayMetrics().density;
                        int paddingDp = (int) (paddingPixel * density);
                        detail_image.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);

                    }
                }
                mat = detail_image.getImageMatrix();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Log.d("AITL ONDESTROY", "CALL");
        if (paint1 != null)
            paint1.reset();
        super.onDestroy();
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((MainActivity) getActivity()).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean camerAaddPermission(List<String> permissionsList, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (((MainActivity) getActivity()).checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    public void requestPermission() {
        permissionsNeeded = new ArrayList<String>();
        permissionsNeeded.clear();
        permissionsList = new ArrayList<String>();
        permissionsList.clear();

        if (!camerAaddPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("Write External Storage");

        if (permissionsList.size() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    Log.i("niral", permissions[i] + " :" + perms.get(permissions[i]));
                }
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

//                    GlobalData.cameraPermissionbroadCast(MainActivity.this);
                    setUpMap();
                    Log.d("Bhavdip", "MainActivty PermissionGranted");

                } else {
                    // Permission Denied
                    requestPermission();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.mapModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.mapModuleid);
        }
    }
}