package com.allintheloop.Fragment.ActivityModule;


import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Adapter.AdapterActivity.Adapter_ActivityInternalLikeListingView;
import com.allintheloop.Adapter.AdapterActivity.Adapter_InternalCommentMessageViewType;
import com.allintheloop.Bean.ActivityModule.ActivityCommentClass;
import com.allintheloop.Bean.ActivityModule.ActivityCommonClass;
import com.allintheloop.Bean.ActivityModule.EventBusAcitivtyLikeCountRefresh;
import com.allintheloop.Bean.ActivityModule.InternalLike;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.Fragment.FacebookModule.FacebookDailog_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityDetailAllInOneModule_Fragment extends Fragment implements VolleyInterface {


    private static final int RESULT_OK = -1;
    private static final int RESULT_LOAD_IMAGE = 5;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    ImageView img_userProfile, img_post, img_socialtop;
    TextView txt_profileName, txt_userNme, txt_designaion, txt_messageText;
    SessionManager sessionManager;
    BoldTextView txt_delete;
    Bundle bundle;
    String type = "";
    int position;
    BoldTextView txt_likeCount, txt_message_count;
    ImageView img_message, img_like;
    RelativeLayout relativeimg_likeCount, relativeimg_messageCount;
    FrameLayout frame_messageFullView;
    LinearLayout linear_editCommentBox;
    ImageView img_send;
    RecyclerView rv_commentView;
    ArrayList<ActivityCommentClass> commentArrayList;
    Adapter_InternalCommentMessageViewType adapterCommentMessageViewType;
    List<InternalLike> internalLikesArrayList;
    Adapter_ActivityInternalLikeListingView internalLikeListingView;
    LinearLayout linearLayout_CommentVew;
    ImageView img_likeTriangle, img_messageTriangle;
    FrameLayout frame_image;
    ImageView imggallaryimages, img_btndelete, img_selectImage;
    EditText edt_message;
    String picturePath = "";
    List<String> permissionsNeeded;
    List<String> permissionsList;
    Bitmap bitmapImage = null;
    ActivityCommonClass activityCommonClass;
    Context context;
    UidCommonKeyClass uidCommonKeyClass;

    public ActivityDetailAllInOneModule_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        View rootView = inflater.inflate(R.layout.fragment_activity_detailmodule, container, false);
        initView(rootView);
        bundleInit();
        OnClick();
        return rootView;
    }

    private void OnClick() {

        img_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity activity = (FragmentActivity) (getActivity());
                FragmentManager fm = activity.getSupportFragmentManager();
                Bundle bundle = new Bundle();
                FacebookDailog_Fragment fragment = new FacebookDailog_Fragment();
                bundle.putInt("position", position);
                bundle.putStringArrayList("img_array", (ArrayList<String>) activityCommonClass.getImage());
                bundle.putString("isActivity", "0");
                fragment.setArguments(bundle);
                fragment.show(fm, "DialogFragment");
            }
        });

        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLogin()) {
                    String commentMeg = edt_message.getText().toString();
                    if (commentMeg.trim().length() != 0) {
                        commentMessage(activityCommonClass.getType(), activityCommonClass.getId(), sessionManager.getUserId(), commentMeg, sessionManager.getEventId(), picturePath);
                    } else {
                        ToastC.show(getActivity(), "Please Enter Message");
//                        Log.d("AITL Comment", "Please Enter Message");
                    }
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }

            }
        });

        img_selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (isCameraPermissionGranted()) {
                        loadCamera();
                    } else {
                        requestPermission();
                    }
                } else {
                    loadCamera();
                }
            }
        });


        img_btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picturePath = "";
                bitmapImage = null;
                frame_image.setVisibility(View.GONE);
            }
        });


        img_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityCommonClass.getPostType().equalsIgnoreCase("1") ||
                        activityCommonClass.getPostType().equalsIgnoreCase("2")) {
                    linearLayout_CommentVew.setVisibility(View.VISIBLE);
                    setUpRecyclerCommentView(true);
                } else {
                    linearLayout_CommentVew.setVisibility(View.GONE);
                    setUpRecyclerCommentView(false);
                }

                rv_commentView.setVisibility(View.VISIBLE);
            }
        });


        txt_message_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityCommonClass.getPostType().equalsIgnoreCase("1") ||
                        activityCommonClass.getPostType().equalsIgnoreCase("2")) {
                    linearLayout_CommentVew.setVisibility(View.VISIBLE);
                    setUpRecyclerCommentView(true);
                } else {
                    linearLayout_CommentVew.setVisibility(View.GONE);
                    setUpRecyclerCommentView(false);
                }

                rv_commentView.setVisibility(View.VISIBLE);
            }
        });


        txt_likeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpRecyclerLikeView();
                linearLayout_CommentVew.setVisibility(View.GONE);
                rv_commentView.setVisibility(View.VISIBLE);

            }
        });

        img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activityCommonClass.getIsLike().equalsIgnoreCase("1")) {

                    int like = Integer.parseInt(activityCommonClass.getLikeCount());
                    int temp = like - 1;
                    if (temp > 0) {
//                        relativeimg_likeCount.setVisibility(View.VISIBLE);

                        if (like < 9) {
                            txt_likeCount.setText(temp + " Likes");
                        } else {
                            txt_likeCount.setText("9+" + " Likes");
                        }

                        txt_likeCount.setVisibility(View.VISIBLE);

                    } else {
//                        relativeimg_likeCount.setVisibility(View.GONE);
                        txt_likeCount.setVisibility(View.INVISIBLE);

                    }
                    activityCommonClass.setLikeCount("" + temp);
                    img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                    activityCommonClass.setIsLike("0");

                    if (activityCommonClass.getLikes().size() != 0) {
                        for (int i = 0; i < activityCommonClass.getLikes().size(); i++) {
                            if (activityCommonClass.getLikes().get(i).getUserId().equalsIgnoreCase(sessionManager.getUserId())) {

                                try {
                                    activityCommonClass.getLikes().remove(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                } else {
                    int like = Integer.parseInt(activityCommonClass.getLikeCount());
                    int temp = like + 1;
                    if (temp > 0) {
                        if (like < 9) {
                            txt_likeCount.setText(temp + " Likes");
                        } else {
                            txt_likeCount.setText("9+" + " Likes");
                        }
                        txt_likeCount.setVisibility(View.VISIBLE);
                    } else {
                        txt_likeCount.setVisibility(View.INVISIBLE);
                    }

                    activityCommonClass.setLikeCount("" + temp);
                    img_like.setColorFilter(context.getResources().getColor(R.color.red));
                    activityCommonClass.setIsLike("1");

                    InternalLike internalLike = new InternalLike();
                    if (sessionManager.getTitle() != null && !sessionManager.getTitle().isEmpty()) {
                        if (sessionManager.getCompany_name() != null && !sessionManager.getCompany_name().isEmpty()) {
                            internalLike.setCompanyName(sessionManager.getTitle() + " at " + sessionManager.getCompany_name());
                        } else {
                            internalLike.setCompanyName(sessionManager.getTitle());
                        }
                    } else {
                        internalLike.setCompanyName("");
                    }
                    internalLike.setDatetime("");
                    internalLike.setLogo(MyUrls.thumImgUrl + sessionManager.getImagePath());
                    internalLike.setName(sessionManager.getFirstName() + " " + sessionManager.getLastName());
                    internalLike.setUserId(sessionManager.getUserId());
                    activityCommonClass.getLikes().add(internalLike);

                }
                internalLikeListingView.notifyDataSetChanged();
                ActivityDetailAllInOneModule_Fragment.this.likeFeed(activityCommonClass.getType(), activityCommonClass.getId(), sessionManager.getUserId(), sessionManager.getEventId());

            }
        });


    }

    private void commentMessage(String moduleType, String id, String userId, String message, String eventId, String image) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), MyUrls.ActivtyCommentMessage, Param.message_img(new File(image)), Param.activtyCommentMessage(moduleType, id, userId, eventId, message), 1, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }


    }

    private void bundleInit() {
        bundle = getArguments();
        if (bundle != null) {
            position = bundle.getInt("position");
            activityCommonClass = bundle.getParcelable("mainClassObj");
            setUpData();
//            setUpSocialData();
        }
    }


    private void loadSocialFeedProfileImageAndPost() {
        if (activityCommonClass.getLogo() != null) {
            try {
                Glide.with(getActivity()).load(activityCommonClass.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        img_userProfile.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        img_userProfile.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).placeholder(getActivity().getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(img_userProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        img_userProfile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, getActivity()));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (activityCommonClass.getImage() != null && activityCommonClass.getImage().size() != 0) {
            try {
                Glide.with(context).load(activityCommonClass.getImage().get(0).toString()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        img_post.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        img_post.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(img_post) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        img_post.setImageBitmap(resource);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            img_post.setVisibility(View.GONE);
        }


    }


    private void setFeedwiseData() {

        switch (activityCommonClass.getPostType()) {
            case "1":
                linearLayout_CommentVew.setVisibility(View.GONE);
                img_like.setClickable(true);
                img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
                img_message.setImageDrawable(context.getResources().getDrawable(R.drawable.message_activity));
                img_socialtop.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_internal_img));
                break;
            case "2":
                linearLayout_CommentVew.setVisibility(View.GONE);
                img_like.setClickable(true);
                img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
                img_message.setImageDrawable(context.getResources().getDrawable(R.drawable.message_activity));
                img_socialtop.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tab_alerts));
                break;
            case "3":
                linearLayout_CommentVew.setVisibility(View.GONE);
                txt_delete.setVisibility(View.GONE);
                txt_designaion.setVisibility(View.GONE);
                img_like.setImageDrawable(getResources().getDrawable(R.drawable.hearts_acitvity));
                img_message.setImageDrawable(getResources().getDrawable(R.drawable.message_activity));
                img_socialtop.setImageDrawable(context.getResources().getDrawable(R.drawable.facebook_activity));
                break;
            case "4":
                linearLayout_CommentVew.setVisibility(View.GONE);
                txt_designaion.setVisibility(View.GONE);
                txt_delete.setVisibility(View.GONE);
                frame_messageFullView.setVisibility(View.GONE);
                img_like.setImageDrawable(getResources().getDrawable(R.drawable.hearts_acitvity));
                img_socialtop.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_twitter));
                break;
            case "7":
                linearLayout_CommentVew.setVisibility(View.GONE);
                txt_delete.setVisibility(View.GONE);
                img_like.setImageDrawable(getResources().getDrawable(R.drawable.hearts_acitvity));
                img_message.setImageDrawable(getResources().getDrawable(R.drawable.message_activity));
                img_message.setClickable(false);
                txt_designaion.setVisibility(View.GONE);
                img_socialtop.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_instagram_logo));
                break;
            default:
                break;
        }
    }


    private void initView(View rootView) {
        img_userProfile = (ImageView) rootView.findViewById(R.id.img_userProfile);
        img_socialtop = (ImageView) rootView.findViewById(R.id.img_socialtop);
        img_post = (ImageView) rootView.findViewById(R.id.img_post);
        img_like = (ImageView) rootView.findViewById(R.id.img_like);
        img_likeTriangle = (ImageView) rootView.findViewById(R.id.img_likeTriangle);
        img_messageTriangle = (ImageView) rootView.findViewById(R.id.img_messageTriangle);
        img_message = (ImageView) rootView.findViewById(R.id.img_message);
        img_send = (ImageView) rootView.findViewById(R.id.img_send);
        txt_profileName = (TextView) rootView.findViewById(R.id.txt_profileName);
        txt_userNme = (TextView) rootView.findViewById(R.id.txt_userNme);
        txt_designaion = (TextView) rootView.findViewById(R.id.txt_designaion);
        txt_messageText = (TextView) rootView.findViewById(R.id.txt_messageText);
        txt_likeCount = rootView.findViewById(R.id.txt_likeCount);
        txt_message_count = rootView.findViewById(R.id.txt_message_count);
        relativeimg_likeCount = (RelativeLayout) rootView.findViewById(R.id.relativeimg_likeCount);
        relativeimg_messageCount = (RelativeLayout) rootView.findViewById(R.id.relativeimg_messageCount);
        linear_editCommentBox = (LinearLayout) rootView.findViewById(R.id.linear_editCommentBox);
        linearLayout_CommentVew = (LinearLayout) rootView.findViewById(R.id.linearLayout_CommentVew);
        frame_image = (FrameLayout) rootView.findViewById(R.id.frame_image);
        rv_commentView = (RecyclerView) rootView.findViewById(R.id.rv_commentView);
        frame_messageFullView = (FrameLayout) rootView.findViewById(R.id.frame_messageFullView);
        edt_message = (EditText) rootView.findViewById(R.id.edt_message);
        txt_delete = (BoldTextView) rootView.findViewById(R.id.txt_delete);
        txt_delete.setPaintFlags(txt_delete.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        sessionManager = new SessionManager(getActivity());

        imggallaryimages = (ImageView) rootView.findViewById(R.id.imggallaryimages);
        img_btndelete = (ImageView) rootView.findViewById(R.id.img_btndelete);
        img_selectImage = (ImageView) rootView.findViewById(R.id.img_selectImage);


        sessionManager.strModuleId = "0";
        context = getActivity();
        commentArrayList = new ArrayList<>();
        internalLikesArrayList = new ArrayList<>();
        GradientDrawable drawableShareButton = new GradientDrawable();
        drawableShareButton.setShape(GradientDrawable.RECTANGLE);
        drawableShareButton.setCornerRadius(70.0f);

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            drawableShareButton.setStroke(3, Color.parseColor(sessionManager.getFunTopBackColor()));
            drawableShareButton.setColor(Color.parseColor("#ffffff"));
            linear_editCommentBox.setBackgroundDrawable(drawableShareButton);
            img_send.setColorFilter(Color.parseColor(sessionManager.getFunTopBackColor()));

        } else {
            drawableShareButton.setStroke(3, Color.parseColor(sessionManager.getTopBackColor()));
            drawableShareButton.setColor(Color.parseColor("#ffffff"));
            linear_editCommentBox.setBackgroundDrawable(drawableShareButton);
            img_send.setColorFilter(Color.parseColor(sessionManager.getTopBackColor()));
        }
    }


    private void setUpData() {


        txt_userNme.setText(activityCommonClass.getName());
        if (activityCommonClass.getPostType().equalsIgnoreCase("1")
                || activityCommonClass.getPostType().equalsIgnoreCase("2")) {
            setUpRecyclerLikeView();
            if (activityCommonClass.getLikeCount() == null || activityCommonClass.getLikeCount().equalsIgnoreCase("")) {
                txt_likeCount.setVisibility(View.INVISIBLE);
                img_like.setColorFilter(getResources().getColor(R.color.activity_heartcolor));
            } else {

                int like = activityCommonClass.getLikes().size();
                if (like > 0) {
                    if (like < 9) {
                        txt_likeCount.setText(activityCommonClass.getLikeCount() + " Likes");
                    } else {
                        txt_likeCount.setText("9+" + " Likes");
                    }
                    txt_likeCount.setVisibility(View.VISIBLE);
                    if (activityCommonClass.getIsLike().equalsIgnoreCase("1")) {
                        img_like.setColorFilter(getResources().getColor(R.color.red));
                    } else {
                        img_like.setColorFilter(getResources().getColor(R.color.activity_heartcolor));
                    }
                } else {
                    txt_likeCount.setVisibility(View.INVISIBLE);
                    img_like.setColorFilter(getResources().getColor(R.color.activity_heartcolor));
                }
            }

            if (activityCommonClass.getComments() == null) {
                txt_message_count.setVisibility(View.INVISIBLE);
                img_message.setColorFilter(getResources().getColor(R.color.activity_heartcolor));
            } else {
                int like = activityCommonClass.getComments().size();
                if (like > 0) {
                    if (like < 9) {
                        txt_message_count.setText(like + " Comments");
                    } else {
                        txt_message_count.setText("9+" + " Likes");
                    }

                    txt_message_count.setVisibility(View.VISIBLE);
                    img_message.setColorFilter(getResources().getColor(R.color.dark_gray));
                } else {
                    txt_message_count.setVisibility(View.INVISIBLE);
                    img_message.setColorFilter(getResources().getColor(R.color.activity_heartcolor));
                }
            }
            onUsernameAndDelelteClick();
            setupInternalAndAlertProfileandPost();

        } else {

            txt_likeCount.setVisibility(View.INVISIBLE);
            img_like.setVisibility(View.GONE);
            img_likeTriangle.setVisibility(View.INVISIBLE);

            txt_message_count.setVisibility(View.INVISIBLE);
            img_message.setVisibility(View.GONE);
            img_messageTriangle.setVisibility(View.INVISIBLE);

            loadSocialFeedProfileImageAndPost();
        }


        if (activityCommonClass.getSubTitle() != null && !(activityCommonClass.getSubTitle().equalsIgnoreCase(""))) {

            txt_designaion.setVisibility(View.VISIBLE);
            txt_designaion.setText(activityCommonClass.getSubTitle());

        } else {
            txt_designaion.setVisibility(View.GONE);
        }

        if (activityCommonClass.getMessage() != null) {

            if (activityCommonClass.getMessage().equalsIgnoreCase("")) {
                txt_messageText.setVisibility(View.GONE);
            } else {
                txt_messageText.setVisibility(View.VISIBLE);
                txt_messageText.setText(activityCommonClass.getMessage());
            }
        }
        setFeedwiseData();

    }


    private void likeFeed(String type, String id, String userId, String eventid) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.ActivityLikeFeed, Param.activityLikeFeed(type, id, userId, eventid), 2, false, this);
    }

    private void onUsernameAndDelelteClick() {
        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                        .title("Delete Post")
                        .items("Are you sure to delete this post?")
                        .positiveColor(getResources().getColor(R.color.colorAccent))
                        .positiveText("Delete")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                deletePost(activityCommonClass.getId(), activityCommonClass.getActivityNo());
                                dialog.dismiss();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .cancelable(false)
                        .build();
                dialog.show();

            }
        });

        if (activityCommonClass.getUserId().equalsIgnoreCase(sessionManager.getUserId())) {
            if (activityCommonClass.getActivityNo().equalsIgnoreCase("5")
                    || activityCommonClass.getActivityNo().equalsIgnoreCase("7")
                    || activityCommonClass.getActivityNo().equalsIgnoreCase("8")) {
                txt_delete.setVisibility(View.GONE);
            } else {
                txt_delete.setVisibility(View.VISIBLE);
            }
        } else {
            txt_delete.setVisibility(View.GONE);
        }


        txt_userNme.setOnClickListener(view -> {


            if (activityCommonClass.getRoleId().equalsIgnoreCase("4")) {//changes applied
                SessionManager.AttenDeeId = activityCommonClass.getUserId();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            } else if (activityCommonClass.getRoleId().equalsIgnoreCase("6")) {//changes applied
                sessionManager.exhibitor_id = activityCommonClass.getId();
                sessionManager.exhi_pageId = activityCommonClass.getId();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            } else if (activityCommonClass.getRoleId().equalsIgnoreCase("7")) {//changes applied
                SessionManager.speaker_id = activityCommonClass.getUserId();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                ((MainActivity) getActivity()).loadFragment();
            }

        });
    }

    private void setupInternalAndAlertProfileandPost() {
        if (activityCommonClass.getImage().size() != 0) {
            try {
                Glide.with(getActivity()).load(activityCommonClass.getImage().get(0).toString()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        img_post.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        img_post.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(img_post) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        img_post.setImageBitmap(resource);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            img_post.setVisibility(View.GONE);
        }

        GradientDrawable drawable = new GradientDrawable();
        if (activityCommonClass.getLogo() != null) {
            if (activityCommonClass.getLogo().equalsIgnoreCase("")) {
                img_userProfile.setVisibility(View.GONE);
                txt_profileName.setVisibility(View.VISIBLE);

                if (!(activityCommonClass.getName().equalsIgnoreCase(""))) {
                    txt_profileName.setText("" + activityCommonClass.getName().charAt(0));
                    if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                        txt_profileName.setBackgroundDrawable(drawable);
                        txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                    } else {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                        txt_profileName.setBackgroundDrawable(drawable);
                        txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                    }
                }

            } else {
                try {
                    Glide.with(getActivity()).load(activityCommonClass.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            img_userProfile.setVisibility(View.VISIBLE);
                            txt_profileName.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            img_userProfile.setVisibility(View.VISIBLE);
                            txt_profileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).placeholder(getActivity().getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(img_userProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            img_userProfile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, getActivity()));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private void deletePost(String activityId, String activityno) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.activityDeletePost, Param.activityDeletePost(activityId, sessionManager.getEventId(), sessionManager.getUserId(), activityno), 0, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void setUpRecyclerCommentView(boolean isInternal) {
        commentArrayList = (ArrayList<ActivityCommentClass>) activityCommonClass.getComments();
        if (commentArrayList != null) {
            adapterCommentMessageViewType = new Adapter_InternalCommentMessageViewType(commentArrayList, getActivity(), sessionManager.getUserId(), isInternal);
            rv_commentView.setItemAnimator(new DefaultItemAnimator());
            rv_commentView.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_commentView.setAdapter(adapterCommentMessageViewType);
            if (commentArrayList.size() != 0) {
                rv_commentView.setVisibility(View.VISIBLE);
                img_messageTriangle.setVisibility(View.VISIBLE);
                img_likeTriangle.setVisibility(View.INVISIBLE);
//                txt_noComments.setVisibility(View.GONE);
            } else {
                img_messageTriangle.setVisibility(View.VISIBLE);
                img_likeTriangle.setVisibility(View.INVISIBLE);
                rv_commentView.setVisibility(View.GONE);
            }
        } else {
            img_messageTriangle.setVisibility(View.VISIBLE);
            img_likeTriangle.setVisibility(View.INVISIBLE);
            rv_commentView.setVisibility(View.GONE);
//            txt_noComments.setVisibility(View.VISIBLE);
        }

    }

    private void setUpRecyclerLikeView() {
        internalLikesArrayList = activityCommonClass.getLikes();
        if (internalLikesArrayList != null) {
            rv_commentView.setVisibility(View.VISIBLE);
            img_messageTriangle.setVisibility(View.INVISIBLE);
            img_likeTriangle.setVisibility(View.VISIBLE);
            internalLikeListingView = new Adapter_ActivityInternalLikeListingView(internalLikesArrayList, getActivity());
            rv_commentView.setItemAnimator(new DefaultItemAnimator());
            rv_commentView.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_commentView.setAdapter(internalLikeListingView);
        } else {
            rv_commentView.setVisibility(View.GONE);
            img_messageTriangle.setVisibility(View.INVISIBLE);
            img_likeTriangle.setVisibility(View.VISIBLE);
        }
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((MainActivity) getActivity()).checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ((MainActivity) getActivity()).checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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

        if (!camerAaddPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");
        if (!camerAaddPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");

        if (permissionsList.size() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
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

    private void loadCamera() {
        String[] item = {"Gallery", "Camera"};
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity())
                .title("Choose Image From")
                .items(item)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            try {
                                startActivityForResult(Intent.createChooser(intent,
                                        "Complete action using"), RESULT_LOAD_IMAGE);

                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else if (which == 1) {
                            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(i, 1);
                        }
                    }

                })
                .build();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    Log.i("niral", permissions[i] + " :" + perms.get(permissions[i]));
                }
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    loadCamera();
//                    GlobalData.cameraPermissionbroadCast(MainActivity.this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    frame_image.setVisibility(View.VISIBLE);
                    bitmapImage = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(getActivity(), bitmapImage);
                    picturePath = getRealPathFromURI(tempUri);
                    selectimage(picturePath);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestCode == RESULT_LOAD_IMAGE) {
                try {
                    Bundle extras2 = data.getExtras();
                    if (extras2 != null) {
                        frame_image.setVisibility(View.VISIBLE);
                        Bitmap photo = extras2.getParcelable("data");
                        Uri uri = getImageUri(getActivity(), photo);
                        picturePath = getRealPathFromURI(uri);
                        selectimage(picturePath);
                    } else {
                        try {
                            frame_image.setVisibility(View.VISIBLE);
                            final Uri imageUri = data.getData();
                            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                            picturePath = getRealPathFromURI(imageUri);
                            selectimageBitymap(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void selectimage(String images) {
        Context context = getActivity();
        Glide.with(context).load(images).placeholder(R.drawable.defult_attende).centerCrop().into(imggallaryimages);
    }

    public void selectimageBitymap(Bitmap images) {
        imggallaryimages.setImageBitmap(images);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Log.d("AITL ImageUri", inImage.toString());
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.d("AITL ImageUri", path.toString());
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));

                        GlobalData.activiyReloadedfromSharePicture(getActivity());
                        ((MainActivity) getActivity()).fragmentBackStackMaintain();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject1 = new JSONObject(volleyResponse.output);
                    if (jsonObject1.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL CommentMessage", jsonObject1.toString());
                        sessionManager.keyboradHidden(edt_message);
                        picturePath = "";
                        bitmapImage = null;
                        frame_image.setVisibility(View.GONE);
                        edt_message.setText("");

                        GlobalData.activiyReloadedfromSharePicture(getActivity());
                        ((MainActivity) getActivity()).fragmentBackStackMaintain();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        EventBus.getDefault().post(new EventBusAcitivtyLikeCountRefresh("Data"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}