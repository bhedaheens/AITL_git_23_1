package com.allintheloop.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;


public class Map_Detail_Activity extends AppCompatActivity implements VolleyInterface, OnMapReadyCallback {

    SessionManager sessionManager;
    String map_id;
    String latitude, longitude, sateliteview, include_map, map_desc, str_area_name, img, place_name, img_url;
    WebView webView;
    TextView area_name;
    ImageView detail_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map__detail);

        sessionManager = new SessionManager(getApplicationContext());
        webView = (WebView) findViewById(R.id.map_desc);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);

        area_name = (TextView) findViewById(R.id.area_name);

        detail_image = (ImageView) findViewById(R.id.detail_image);


        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(Map_Detail_Activity.this, VolleyRequest.Method.POST, MyUrls.get_MapDetailUid, Param.getMapDetail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), getIntent().getStringExtra("map_id")), 0, true, this);
            else
                new VolleyRequest(Map_Detail_Activity.this, VolleyRequest.Method.POST, MyUrls.get_MapDetail, Param.getMapDetail(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), getIntent().getStringExtra("map_id")), 0, true, this);
        }

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL", "MapDetail :-" + jsonObject.toString());

                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("map_details");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                str_area_name = object.getString("area");
                                map_desc = object.getString("Map_desc");
                                sateliteview = object.getString("satellite_view");
                                include_map = object.getString("include_map");
                                place_name = object.getString("place");
                                latitude = object.getString("lat");
                                longitude = object.getString("long");
                                img = object.getString("Images");
                            }
                        }

                        Log.d("AITL", str_area_name);
                        Log.d("AITL", map_desc);
                        Log.d("AITL", place_name);
                        Log.d("AITL", latitude);
                        Log.d("AITL", longitude);
                        if (!str_area_name.equalsIgnoreCase("")) {
                            area_name.setText("Area :" + str_area_name);
                        } else {
                            area_name.setVisibility(View.GONE);
                        }

                        if (!map_desc.equalsIgnoreCase("")) {
                            webView.loadData(map_desc, "text/html", "charset=UTF-8");
                        } else {
                            webView.setVisibility(View.GONE);
                        }

                        if (include_map.equalsIgnoreCase("1")) {
                            MapFragment mapFragment = (MapFragment) getFragmentManager()
                                    .findFragmentById(R.id.map);
                            mapFragment.getMapAsync(this);
                        }

                        img_url = MyUrls.Imgurl + img;

                        Glide.with(getApplicationContext())
                                .load(img_url)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                        detail_image.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        detail_image.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                })
                                .centerCrop()
                                .fitCenter()
                                .into(detail_image);
                    } else {
                        ToastC.show(getApplicationContext(), jsonObject.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lati = Double.parseDouble(latitude);
        double longi = Double.parseDouble(longitude);
        LatLng cordinate = new LatLng(lati, longi);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cordinate, 13));

        googleMap.addMarker(new MarkerOptions()
                .title(place_name)
                .position(cordinate));
        if (sateliteview.equalsIgnoreCase("1")) {
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

    }
}
