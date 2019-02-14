package com.allintheloop.Fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteDetail_Fragment extends Fragment implements VolleyInterface {


    ImageView img_more;
    Bundle bundle;
    String str_edit, str_heading, str_title, str_desc, str_note_id, strMenuId, strModuleId, strIscustomeTitle, is_cms = "";
    EditText edt_heading, edt_description;
    TextView txt_title;
    Dialog delete_Dialog;
    SessionManager sessionManager;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Button btn_save;
    private UidCommonKeyClass uidCommonKeyClass;

    public NoteDetail_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_note_detail, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        img_more = (ImageView) rootView.findViewById(R.id.img_more);
        txt_title = (TextView) rootView.findViewById(R.id.txt_title);
        edt_description = (EditText) rootView.findViewById(R.id.edt_description);
        edt_heading = (EditText) rootView.findViewById(R.id.edt_heading);
        sessionManager = new SessionManager(getActivity());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        btn_save = (Button) rootView.findViewById(R.id.btn_save);

        btn_save.setOnClickListener(view -> {

            str_heading = edt_heading.getText().toString();
            str_desc = edt_description.getText().toString();
            if (str_edit.equalsIgnoreCase("1")) {
                Log.d("AITL ButtonClick", "" + str_heading);
                if (str_heading.trim().equalsIgnoreCase("")) {
                    edt_heading.setError("Please Enter Title");
                } else if (str_desc.trim().equalsIgnoreCase("")) {
                    edt_description.setError("Please Enter Note");
                } else {
                    sessionManager.keyboradHidden(edt_heading);
                    editNote();
                }
            } else if (str_edit.equalsIgnoreCase("0")) {
                if (str_heading.trim().equalsIgnoreCase("")) {
                    edt_heading.setError("Please Enter Title");
                } else if (str_desc.trim().equalsIgnoreCase("")) {
                    edt_description.setError("Please Enter Note");
                } else {
                    sessionManager.keyboradHidden(edt_heading);
                    saveNote();
                }
            }

        });


        txt_title.setOnClickListener(view -> {

            if (strIscustomeTitle.equalsIgnoreCase("0")) {

                sessionManager.strMenuId = strMenuId;
                sessionManager.strModuleId = strModuleId;

                if (is_cms.equalsIgnoreCase("1")) {
                    sessionManager.cms_id(strModuleId);
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    if (strMenuId.equalsIgnoreCase("0")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("1")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {

                            sessionManager.agenda_id(strModuleId);
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("2")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            ((MainActivity) getActivity()).attendeeRedirection(false, false);
                        } else {
                            SessionManager.AttenDeeId = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("3")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            SessionManager.exhibitor_id = strModuleId;
                            SessionManager.exhi_pageId = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("20")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("12")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            sessionManager.private_senderId = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("6")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("7")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {

                            SessionManager.speaker_id = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("7")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {

                            SessionManager.speaker_id = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("9")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("presantation_id", strModuleId);
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Presantation_Detail_Fragment;
                            ((MainActivity) getActivity()).loadFragment(bundle1);
                        }
                    } else if (strMenuId.equalsIgnoreCase("10")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            sessionManager.Map_coords = "";
//                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//                            GlobalData.CURRENT_FRAG = GlobalData.Map_fragment;
                            ((MainActivity) getActivity()).isMapGroupExist();
                        } else {
                            sessionManager.Map_coords = "";
                            sessionManager.setMapid(strModuleId);
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Map_Detail_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("11")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("12")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("15")) {
                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            sessionManager.setCategoryId(strModuleId);
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.SurveyCategoryWiseFragment;
                            ((MainActivity) getActivity()).loadFragment();

                        }
                    } else if (strMenuId.equalsIgnoreCase("16")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            Bundle bundle = new Bundle();
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                            Log.d("AITL FolderId", MyUrls.doc_url + strModuleId);
                            bundle.putString("document_file", MyUrls.doc_url + strModuleId);
                            bundle.putString("file_name", strModuleId);
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("17")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("Social_url", strModuleId);
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                            ((MainActivity) getActivity()).loadFragment(bundle);
                        }
                    } else if (strMenuId.equalsIgnoreCase("22")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            sessionManager.slilentAuctionP_id = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.SilentAuctionDetail_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("23")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            sessionManager.liveAuctionP_id = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.LiveAuction_Detail;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("24")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            sessionManager.OnlineShop_id = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.OnlineShop_Detail;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("25")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            sessionManager.pledge_id = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PledgeDetailFragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("43")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            ((MainActivity) getActivity()).isSponsorGroupExist();
                        } else {
                            sessionManager.sponsor_id = strModuleId;
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Sponsor_Detail_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (strMenuId.equalsIgnoreCase("44")) {


                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                        ((MainActivity) getActivity()).loadFragment();

                    } else if (strMenuId.equalsIgnoreCase("45")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("46")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("47")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("49")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("48")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.VirtualSuperMarket;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("54")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (strMenuId.equalsIgnoreCase("50")) {

                        if (strModuleId.equalsIgnoreCase("0")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("session_id", strModuleId);
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.QAListDetail_Fragment;
                            ((MainActivity) getActivity()).loadFragment(bundle);
                        }
                    }
                }

            } else {
                if (str_title.equalsIgnoreCase("Suggested Meetings")) {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (str_title.equalsIgnoreCase("Meetings")) {


                    if (sessionManager.getRolId().equalsIgnoreCase("7") &&
                            sessionManager.isModerater().equalsIgnoreCase("1")) { //will removed after discussion

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                        ((MainActivity) getActivity()).loadFragment();

                    } else {

                        if (sessionManager.getRolId().equalsIgnoreCase("6")) {//will removed after discussion
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (sessionManager.getRolId().equalsIgnoreCase("4")) {//will removed after discussion
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }

                    }
                } else if (str_title.equalsIgnoreCase("My Cart")) {

                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CartDetailFragment;
                    ((MainActivity) getActivity()).loadFragment();

                } else if (str_title.equalsIgnoreCase("Home")) {

                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                    ((MainActivity) getActivity()).loadFragment();

                } else if (str_title.equalsIgnoreCase("Checkout")) {

                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CheckOut_Fragment;
                    ((MainActivity) getActivity()).loadFragment();

                } else if (str_title.equalsIgnoreCase("Donation")) {

                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.fund_donation_fragment;
                    ((MainActivity) getActivity()).loadFragment();

                } else if (str_title.equalsIgnoreCase("Orders")) {

                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Order_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (str_title.equalsIgnoreCase("Items")) {

                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.ItemListFragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (str_title.equalsIgnoreCase("CheckIn Portal")) {

                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.CheckIn_Portal;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (str_title.equalsIgnoreCase("Beacon")) {

                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.BeaconFinder_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (str_title.equalsIgnoreCase("Notification Center")) {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                    ((MainActivity) getActivity()).loadFragment();
                }

            }

        });

        delete_Dialog = new Dialog(getActivity());
        delete_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        delete_Dialog.setContentView(R.layout.dialog_notes_delete);
        delete_Dialog.getWindow().setLayout(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);


        bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("edit")) {
                str_edit = bundle.getString("edit");
                if (str_edit.equalsIgnoreCase("1")) {
                    img_more.setVisibility(View.VISIBLE);
                    txt_title.setVisibility(View.VISIBLE);
                    str_heading = bundle.getString("heading");
                    str_title = bundle.getString("title");
                    str_desc = bundle.getString("desc");
                    str_note_id = bundle.getString("note_id");
                    strMenuId = bundle.getString("menu_id");
                    strModuleId = bundle.getString("Module_id");
                    strIscustomeTitle = bundle.getString("is_custome_title");
                    is_cms = bundle.getString("isCMS");
                    Log.d("AITL Clicked", strIscustomeTitle);

                    txt_title.setText(str_title);
                    edt_heading.setText(str_heading);
                    edt_description.setText(str_desc);

                    edt_heading.setEnabled(false);
                    edt_description.setEnabled(false);
                    btn_save.setVisibility(View.GONE);
                } else if (str_edit.equalsIgnoreCase("0")) {
                    img_more.setVisibility(View.GONE);
                    txt_title.setVisibility(View.GONE);
                    btn_save.setVisibility(View.VISIBLE);
                }

            }
        }

        final PopupMenu popup = new PopupMenu(getActivity(), img_more);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.note_popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Edit")) {
                str_edit = "1";
                edt_heading.setEnabled(true);
                btn_save.setVisibility(View.VISIBLE);
                edt_description.setEnabled(true);
            } else {

                delete_Dialog.show();
                TextView alertDialog_txt_yes = (TextView) delete_Dialog.findViewById(R.id.alertDialog_txt_yes);
                TextView alertDialog_txt_no = (TextView) delete_Dialog.findViewById(R.id.alertDialog_txt_no);

                alertDialog_txt_yes.setOnClickListener(view -> {

                    if (GlobalData.isNetworkAvailable(getActivity())) {
                        deleteNote(str_note_id);
                    } else {
                        ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
                    }
                });

                alertDialog_txt_no.setOnClickListener(view -> delete_Dialog.dismiss());
            }
            return true;
        });

        img_more.setOnClickListener(view -> popup.show());


        return rootView;
    }


    private void saveNote() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Add_Notes, Param.AddNotes(sessionManager.strMenuId, sessionManager.strModuleId, sessionManager.getUserId(), str_heading, str_desc, sessionManager.getEventId(), sessionManager.getToken(), sessionManager.isNoteCms), 2, true, this);
        } else {
            ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
        }

    }

    private void editNote() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.update_Notes, Param.update_note(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getUserId(), str_note_id, str_heading, str_desc), 1, true, this);
        } else {
            ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
        }
    }

//    private void addNotes()
//    {
//        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.noteDetele, Param.DeleteNote(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), noteId), 1, false, this);
//    }

    private void deleteNote(String noteId) {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.noteDetele, Param.DeleteNote(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), noteId), 0, false, this);
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL  DELETE QUERY", jsonObject.toString());
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonData.getJSONArray("note_list");

                        if (sqLiteDatabaseHandler.isNotesListExist(sessionManager.getEventId(), sessionManager.getUserId())) {
                            sqLiteDatabaseHandler.deleteNotesListingData(sessionManager.getEventId(), sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertNotesListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonArray.toString());
                        } else {
                            sqLiteDatabaseHandler.insertNotesListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonArray.toString());
                        }
                        GlobalData.UpdateNoteListingFragment(getActivity());
                        delete_Dialog.dismiss();
                        GlobalData.CURRENT_FRAG = GlobalData.Fragment_Stack.pop();
//                        if(GlobalData.CURRENT_FRAG)
                        Log.e("aiyaz", "POP up " + GlobalData.CURRENT_FRAG);
                        //loadFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        GlobalData.UpdateNoteListingFragment(getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        str_note_id = jsonObject.getString("insert_id");
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        btn_save.setVisibility(View.GONE);
                        img_more.setVisibility(View.VISIBLE);
                        edt_heading.setEnabled(false);
                        edt_description.setEnabled(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
