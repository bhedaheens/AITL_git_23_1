package com.allintheloop.Fragment.FundraisingModule;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.allintheloop.Adapter.GallaryAdepter;
import com.allintheloop.Bean.GallaryBean;
import com.allintheloop.Fragment.FundraisingModule.CartDetail_Fragment;
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
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.crosswall.photo.pick.PickConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddItem_Fragment extends Fragment implements VolleyInterface {


    Spinner spr_type, spr_category, spr_feature, spr_status;
    Button btn_save;
    ImageView img_addPhoto;

    ArrayList<String> typeArray;
    ArrayList<String> categoryArray;
    ArrayList<String> featureArray;
    ArrayList<String> statusArray;
    SessionManager sessionManager;

    public static LinearLayout linear_photo, linearimage_load;
    public static RecyclerView recycler_img_gallary_picker;

    public static ArrayList<String> selectImages;
    public static ArrayList<GallaryBean> gallaryBeansarraylist;
    static GallaryAdepter gallaryAdepter;

    private static final int RESULT_OK = -1;
    public static int counter = 0;
    String picturePath = " ";
    Bitmap bitmapImage = null;
    static Context context;


    EditText edt_title, edt_desc, edt_startPrice, edt_reservePrice, edt_price, edt_qty;
    TextView txt_startPrice, txt_reservePrice, txt_Price;
    TextView txt_startdate, txt_starttime, txt_enddate, txt_endtime;
    LinearLayout linear_checkBox, linear_startPrice, linear_reservePrice, linear_auctionstart, linear_auctionend, linear_price;
    RadioGroup radioGroup;
    String StrRadio, str_type;
    String str_startDate, str_endtDate, startTime, endTime, str_category = "", str_idtype = "0", str_idFeature = "0", str_idstatus = "0", str_auctionId = "1";

    String str_title, str_desc, str_startPrice = "0", str_reserve = "0", str_reserveBidvisible = "0", str_startDateTime, str_endDateTime, str_price = "0", str_qty = "0";
    String product_id;
    int isLast = 0;
    String str_currency;

    public AddItem_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        // Image
        sessionManager.strModuleId = "Items";
        selectImages = new ArrayList<>();
        gallaryBeansarraylist = new ArrayList<>();

        linear_photo = (LinearLayout) rootView.findViewById(R.id.linear_photo);
        linearimage_load = (LinearLayout) rootView.findViewById(R.id.linearimage_load);

        recycler_img_gallary_picker = (RecyclerView) rootView.findViewById(R.id.recycler_img_gallary_picker);
        context = getContext();
        //

        typeArray = new ArrayList<>();
        categoryArray = new ArrayList<>();
        featureArray = new ArrayList<>();
        statusArray = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());

        radioGroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);

        linear_checkBox = (LinearLayout) rootView.findViewById(R.id.linear_checkBox);
        linear_startPrice = (LinearLayout) rootView.findViewById(R.id.linear_startPrice);
        linear_reservePrice = (LinearLayout) rootView.findViewById(R.id.linear_reservePrice);
        linear_auctionstart = (LinearLayout) rootView.findViewById(R.id.linear_auctionstart);
        linear_auctionend = (LinearLayout) rootView.findViewById(R.id.linear_auctionend);
        linear_price = (LinearLayout) rootView.findViewById(R.id.linear_price);

        spr_type = (Spinner) rootView.findViewById(R.id.spr_type);
        spr_category = (Spinner) rootView.findViewById(R.id.spr_category);
        spr_feature = (Spinner) rootView.findViewById(R.id.spr_feature);
        spr_status = (Spinner) rootView.findViewById(R.id.spr_status);

        btn_save = (Button) rootView.findViewById(R.id.btn_save);

        txt_startPrice = (TextView) rootView.findViewById(R.id.txt_startPrice);
        txt_reservePrice = (TextView) rootView.findViewById(R.id.txt_reservePrice);
        txt_Price = (TextView) rootView.findViewById(R.id.txt_Price);
        txt_startdate = (TextView) rootView.findViewById(R.id.txt_startdate);
        txt_starttime = (TextView) rootView.findViewById(R.id.txt_starttime);
        txt_enddate = (TextView) rootView.findViewById(R.id.txt_enddate);
        txt_endtime = (TextView) rootView.findViewById(R.id.txt_endtime);

        edt_desc = (EditText) rootView.findViewById(R.id.edt_desc);
        edt_title = (EditText) rootView.findViewById(R.id.edt_title);
        edt_startPrice = (EditText) rootView.findViewById(R.id.edt_startPrice);
        edt_reservePrice = (EditText) rootView.findViewById(R.id.edt_reservePrice);
        edt_price = (EditText) rootView.findViewById(R.id.edt_price);
        edt_qty = (EditText) rootView.findViewById(R.id.edt_qty);


        img_addPhoto = (ImageView) rootView.findViewById(R.id.img_addPhoto);

        typeArray.add("Silent Auction");
        typeArray.add("Buy Now");

        featureArray.add("No");
        featureArray.add("YES");


        txt_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                Log.d("AITL Hours ", "" + hourOfDay);
                                startTime = hourOfDay + ":" + minute;
                                txt_starttime.setText(startTime);
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        false
                );

                timePickerDialog.show(getActivity().getFragmentManager(), "Time");
            }

        });

        txt_endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                endTime = hourOfDay + ":" + minute;
                                txt_endtime.setText(endTime);
                            }
                        },
                        c.get(Calendar.HOUR_OF_DAY),
                        c.get(Calendar.MINUTE),
                        false
                );

                timePickerDialog.show(getActivity().getFragmentManager(), "Time");
            }


        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str_startDateTime = str_startDate + " " + startTime;
                str_endDateTime = str_endtDate + " " + endTime;
                str_title = edt_title.getText().toString();
                str_desc = edt_desc.getText().toString();
                str_startPrice = edt_startPrice.getText().toString();
                str_reserve = edt_reservePrice.getText().toString();
                str_price = edt_price.getText().toString();
                str_qty = edt_qty.getText().toString();
                if (str_idtype.equalsIgnoreCase("0")) {
                    if (str_title.trim().length() == 0) {
                        edt_title.setError("Enter Item Title");
                    } else if (str_desc.trim().length() == 0) {
                        edt_desc.setError("Enter Item Description");
                    } else if (str_startPrice.trim().length() == 0) {
                        edt_startPrice.setError("Enter Start Price");
                    } else if (str_reserve.trim().length() == 0) {
                        edt_reservePrice.setError("Enter Reserve Price");
                    } else if (str_startDate.trim().length() == 0) {
                        txt_startdate.setError("Please Select StartDate");
                    } else if (startTime.trim().length() == 0) {
                        txt_starttime.setError("Please Select StartTime");
                    } else if (str_endtDate.trim().length() == 0) {
                        txt_enddate.setError("Please Select EndDate");
                    } else if (endTime.trim().length() == 0) {
                        txt_enddate.setError("Please Select EndTime");
                    } else if (gallaryBeansarraylist.size() == 0) {
                        ToastC.show(getActivity(), "Please Select Image");
                    } else {
                        addItem();
                    }

                } else if (str_idtype.equalsIgnoreCase("1")) {
                    if (str_title.trim().length() == 0) {
                        edt_title.setError("Enter Item Title");
                    } else if (str_desc.trim().length() == 0) {
                        edt_desc.setError("Enter Item Description");
                    } else if (str_price.trim().length() == 0) {
                        edt_price.setError("Enter Price");
                    } else if (str_qty.trim().length() == 0) {
                        edt_qty.setError("Enter Quantity");
                    } else if (gallaryBeansarraylist.size() == 0) {
                        ToastC.show(getActivity(), "Please Select Image");
                    } else {
                        addItem();
                    }
                }
            }
        });

        txt_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 10)
                            str_startDate = year + "/" + "0" + (monthOfYear + 1) + "/" + dayOfMonth;
                        else
                            str_startDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

                        //         SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                        try {
//                            Date newDate = format.parse(str_startDate);
//                            Calendar end = Calendar.getInstance();
//                            end.setTime(format.parse(str_startDate));
//
////                            format = new SimpleDateFormat("MMM dd,yyyy");
////                            String date = format.format(newDate);
//
//                            //find date difference
                            txt_startdate.setText(str_startDate);
                        } catch (Exception e) {
                            Log.e("DateExp", e.toString());
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.vibrate(false);
                dialog.show(getActivity().getFragmentManager(), "Add Date");
            }
        });

        txt_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear < 10)
                            str_endtDate = year + "/" + "0" + (monthOfYear + 1) + "/" + dayOfMonth;
                        else
                            str_endtDate = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;

                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");

                        try {
                            Date newDate = format.parse(str_startDate);
                            Calendar end = Calendar.getInstance();
                            end.setTime(format.parse(str_startDate));

//                            format = new SimpleDateFormat("MMM dd,yyyy");
//                            String date = format.format(newDate);

                            //find date difference
                            txt_enddate.setText(str_endtDate);
                        } catch (ParseException e) {
                            Log.e("DateExp", e.toString());
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                dialog.vibrate(false);
                dialog.show(getActivity().getFragmentManager(), "Add Date");
            }

        });


        img_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PickConfig.Builder(getActivity())
                        .pickMode(PickConfig.MODE_MULTIP_PICK)
                        .maxPickSize(10)
                        .spanCount(3)
                        .flag(7)
                        .toolbarColor(R.color.colorPrimary)
                        .build();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rdButton = (RadioButton) group.findViewById(checkedId);
                StrRadio = rdButton.getText().toString();
                if (StrRadio.equalsIgnoreCase("Reserve Price Public Options")) {
                    str_reserveBidvisible = "1";
                } else if (StrRadio.equalsIgnoreCase("Reserve Price Hidden")) {
                    str_reserveBidvisible = "0";
                }
                Log.d("AITL", "Radio button" + StrRadio);

            }
        });

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, typeArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_type.setAdapter(dataAdapter);

        spr_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_type = parent.getItemAtPosition(position).toString();

                str_idtype = String.valueOf(parent.getSelectedItemId());
                Log.d("profileLst", str_type + "ID:-" + str_idtype);

                if (str_idtype.equalsIgnoreCase("0")) {
                    str_auctionId = "1";
                    linear_checkBox.setVisibility(View.VISIBLE);
                    linear_startPrice.setVisibility(View.VISIBLE);
                    linear_reservePrice.setVisibility(View.VISIBLE);
                    linear_auctionstart.setVisibility(View.VISIBLE);
                    linear_auctionend.setVisibility(View.VISIBLE);
                    linear_price.setVisibility(View.GONE);
                } else if (str_idtype.equalsIgnoreCase("1")) {
                    str_auctionId = "3";
                    linear_checkBox.setVisibility(View.GONE);
                    linear_startPrice.setVisibility(View.GONE);
                    linear_reservePrice.setVisibility(View.GONE);
                    linear_auctionstart.setVisibility(View.GONE);
                    linear_auctionend.setVisibility(View.GONE);
                    linear_price.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, featureArray);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_feature.setAdapter(dataAdapter1);

        spr_feature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String profileLst = parent.getItemAtPosition(position).toString();

                str_idFeature = String.valueOf(parent.getSelectedItemId());
                Log.d("profileLst", profileLst + "ID:-" + str_idFeature);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, featureArray);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spr_status.setAdapter(dataAdapter2);

        spr_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String profileLst = parent.getItemAtPosition(position).toString();

                str_idstatus = String.valueOf(parent.getSelectedItemId());
                Log.d("profileLst", profileLst + "ID:-" + str_idstatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getItemCategory, Param.getCategory(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType()), 0, false, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }

        return rootView;
    }


    public static void selectimage(String images) {

        gallaryBeansarraylist.add(new GallaryBean(images, "addItemFragment"));

        gallaryAdepter = new GallaryAdepter(gallaryBeansarraylist, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recycler_img_gallary_picker.setLayoutManager(mLayoutManager);
        recycler_img_gallary_picker.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recycler_img_gallary_picker.setAdapter(gallaryAdepter);

    }


    private void addItem() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.addproduct_Item, Param.addItem(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getEventType(), sessionManager.getUserId(), str_auctionId, str_category, str_title, str_desc, str_idFeature, str_idstatus, str_startPrice, str_reserve, str_reserveBidvisible, str_startDateTime, str_endDateTime, str_price, str_qty), 1, true, this);
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    private void UploadePhotoAPI(String multipleImg) {
        Log.d("AITL MultipleImageAPI", multipleImg);
        new VolleyRequest(getActivity(), MyUrls.saveItemImage, Param.message_img(new File(multipleImg)), Param.addItemImageRequest(sessionManager.getUserId(), product_id), 2, true, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        str_currency = jsonObject.getString("currency");

                        if (str_currency.equalsIgnoreCase("euro")) {
                            txt_startPrice.setText("Start Price" + context.getResources().getString(R.string.euro));
                            txt_reservePrice.setText("Reserve Price" + context.getResources().getString(R.string.euro));
                            txt_Price.setText("Price" + context.getResources().getString(R.string.euro));
                        } else if (str_currency.equalsIgnoreCase("gbp")) {
                            txt_startPrice.setText("Start Price" + context.getResources().getString(R.string.pound_sign));
                            txt_reservePrice.setText("Reserve Price" + context.getResources().getString(R.string.pound_sign));
                            txt_Price.setText("Price" + context.getResources().getString(R.string.pound_sign));
                        } else if (str_currency.equalsIgnoreCase("usd") || CartDetail_Fragment.str_currency.equalsIgnoreCase("aud")) {
                            txt_startPrice.setText("Start Price" + context.getResources().getString(R.string.dollor));
                            txt_reservePrice.setText("Reserve Price" + context.getResources().getString(R.string.dollor));
                            txt_Price.setText("Price" + context.getResources().getString(R.string.dollor));
                        }


                        JSONArray jsonArray = jsonObject.getJSONArray("categories");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            str_category = jsonArray.getString(0);
                            categoryArray.add(jsonArray.getString(i));
                        }
                        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryArray);
                        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spr_category.setAdapter(dataAdapter3);

                        spr_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                str_category = parent.getItemAtPosition(position).toString();

                                String spr_profile_id = String.valueOf(parent.getSelectedItemId());
                                Log.d("profileLst", str_category + "ID:-" + spr_profile_id);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonaddResponse = new JSONObject(volleyResponse.output);
                    if (jsonaddResponse.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL ResonseAddItem", jsonaddResponse.toString());
                        JSONObject jsonData = jsonaddResponse.getJSONObject("data");

                        product_id = jsonData.getString("product_id");

                        for (int j = 0; j < gallaryBeansarraylist.size(); j++) {
                            isLast = j + 1;
                            UploadePhotoAPI(gallaryBeansarraylist.get(j).getImages());
                            Log.d("AITL arrayList SiZe", "" + gallaryBeansarraylist.size());
                            Log.d("UploadRecipePhotoAPI", gallaryBeansarraylist.get(j).getImages());
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject imgupload = new JSONObject(volleyResponse.output);
                    Log.d("AITL UploadImg", imgupload.toString());
                    if (isLast == gallaryBeansarraylist.size()) {

                        Log.d("AITL IMAGE", "ARRAYLIST GET CLEARED");
                        gallaryBeansarraylist.clear();
                        linearimage_load.setVisibility(View.GONE);

                        GlobalData.CURRENT_FRAG = GlobalData.ItemListFragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
