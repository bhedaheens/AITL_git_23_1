package com.allintheloop.Adapter.Attendee;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Adapter.Exhibitor.Exhibitor_ImageAdapter;
import com.allintheloop.Bean.DefaultLanguage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.Attendee.Attendee_Comment;
import com.allintheloop.Bean.Attendee.Attendee_message;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.Fragment.PrivateMessage.Private_Message_Fragment;
import com.allintheloop.Fragment.PublicMessage_Fragment;
import com.allintheloop.Fragment.ViewImageDialog;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by nteam on 23/6/16.
 */
public class Attendee_message_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VolleyInterface {

    ArrayList<Attendee_message> messageArrayList;
    ArrayList<Exhibitor_DetailImage> imageArrayList;
    ArrayList<Attendee_Comment> commentArrayList;
    Context context;
    Exhibitor_ImageAdapter imageAdapter;
    Attendee_Comment_Adapter comment_adapter;
    SessionManager sessionManager;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    LinearLayoutManager lytManager;
    private OnLoadMoreListener onLoadMoreListener;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    int position;
    Activity activity;
    public String picturePath = "";
    Bitmap bitmapImage = null;
    public ArrayList<ImageView> commImageView = new ArrayList<>();
    public ArrayList<LinearLayout> commLinearLayout = new ArrayList<>();
    public ArrayList<LinearLayout> linear_commentBox_array = new ArrayList<>();
    boolean isHide = false;
    private static final int TYPE_ITEM = 1;
    DefaultLanguage.DefaultLang defaultLang;

    List<String> permissionsNeeded;
    List<String> permissionsList;


    public Attendee_message_Adapter(ArrayList<Attendee_message> messageArrayList, RecyclerView recyclerView, Context context, final LinearLayoutManager layoutManager, Activity activity, NestedScrollView scrollView) {
        this.messageArrayList = messageArrayList;
        this.context = context;
        this.activity = activity;
        sessionManager = new SessionManager(this.context);
        defaultLang = sessionManager.getMultiLangString();
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (scrollView1, scrollX, scrollY, oldScrollX, oldScrollY) -> {


            totalItemCount = layoutManager.getItemCount();
            lastVisibleItem = layoutManager.findLastVisibleItemPosition();

            if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                if (onLoadMoreListener != null) {
                    onLoadMoreListener.onLoadMore();
                }

                loading = true;
            }
        });

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attendee_message, parent, false);
            return new MyViewHolder(itemView);
        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof MyViewHolder && getItem(position) != null) {
            try {
                final MyViewHolder myViewHolder = (MyViewHolder) holder;
                commImageView.add(myViewHolder.imggallaryimages);
                commLinearLayout.add(myViewHolder.linear_loadPhoto);
                linear_commentBox_array.add(myViewHolder.linear_commentBox);
                final Attendee_message attendee_messageObj = messageArrayList.get(position);
                imageArrayList = attendee_messageObj.getImageArrayList();
                commentArrayList = attendee_messageObj.getCommentArrayList();
                Spannable user_sender = new SpannableString(attendee_messageObj.getRes_senderName());
                user_sender.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.survey_question)), 0, user_sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                myViewHolder.user_sender.setText(user_sender);


                if (attendee_messageObj.getTag().equalsIgnoreCase("private_message")) {
                    if (attendee_messageObj.getDesiredRecevier().equalsIgnoreCase("")) {
                        myViewHolder.txt_desiredReciver.setVisibility(View.GONE);
                    } else {
                        myViewHolder.txt_desiredReciver.setVisibility(View.VISIBLE);
                        myViewHolder.txt_desiredReciver.setText("Desired Receiver : " + attendee_messageObj.getDesiredRecevier());
                    }
                }
                if (attendee_messageObj.getRes_reciverName().equalsIgnoreCase("")) {
                    myViewHolder.user_receiver.setText("");
                    myViewHolder.txt_label.setText("");
                } else {
                    // myViewHolder.user_receiver.setText(attendee_messageObj.getRes_reciverName());
                    Spannable with = new SpannableString("With");
                    with.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.GrayColor)), 0, with.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    myViewHolder.user_sender.append("   ");
                    myViewHolder.user_sender.append(with);

                    Spannable user_reciver = new SpannableString(attendee_messageObj.getRes_reciverName());
                    user_reciver.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.survey_question)), 0, user_reciver.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    myViewHolder.user_sender.append("   ");
                    myViewHolder.user_sender.append(user_reciver);
                }
                myViewHolder.txt_time.setText(attendee_messageObj.getRes_timeStamp());
                if (!(attendee_messageObj.getRes_message().equalsIgnoreCase(""))) {

                    myViewHolder.txt_message.setOnClickListener(view -> {

                        if (attendee_messageObj.getTag().equalsIgnoreCase("private_message")) {
                            if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("0")) {
                                Log.d("AITL IS Messge Click", attendee_messageObj.getIs_clickable());
                            } else if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("1")) {
                                approvedStatus(attendee_messageObj.getSender_id());
                                Log.d("AITL IS Messge Click", attendee_messageObj.getIs_clickable());
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                                ((MainActivity) context).loadFragment();
                            } else if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("2")) {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                                ((MainActivity) context).loadFragment();
                            } else if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("3")) {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                                ((MainActivity) context).loadFragment();
                            } else if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("4")) {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                                ((MainActivity) context).loadFragment();
                            } else {
                                Log.d("AITL IS Messge Click", attendee_messageObj.getIs_clickable());
                            }
                        } else if (attendee_messageObj.getTag().equalsIgnoreCase("attendee")) {
                            if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("0")) {
                                Log.d("AITL IS Messge Click", attendee_messageObj.getIs_clickable());
                            } else if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("1")) {
                                approvedStatus(attendee_messageObj.getSender_id());
                                Log.d("AITL IS Messge Click", attendee_messageObj.getIs_clickable());
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                                ((MainActivity) context).loadFragment();
                            } else if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("2")) {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                                ((MainActivity) context).loadFragment();
                            } else if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("3")) {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                                ((MainActivity) context).loadFragment();
                            } else if (attendee_messageObj.getIs_clickable().equalsIgnoreCase("4")) {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                                ((MainActivity) context).loadFragment();
                            } else {
                                Log.d("AITL IS Messge Click", attendee_messageObj.getIs_clickable());
                            }
                        }


                    });
                    myViewHolder.txt_message.setText(attendee_messageObj.getRes_message());
                    Linkify.addLinks(myViewHolder.txt_message, Linkify.WEB_URLS);

                }

                Glide.with(context).load(MyUrls.Imgurl + attendee_messageObj.getRes_senderLogo()).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(myViewHolder.sender_image);

                Glide.with(context)
                        .load(MyUrls.thumImgUrl + sessionManager.getImagePath())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                myViewHolder.comment_image.setVisibility(View.VISIBLE);
                                Glide.with(context).load(MyUrls.thumImgUrl + sessionManager.getImagePath()).placeholder(R.drawable.defult_attende).into(myViewHolder.comment_image);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                myViewHolder.comment_image.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(myViewHolder.comment_image);

                Glide.with(context)
                        .load(MyUrls.Imgurl + attendee_messageObj.getRes_senderLogo())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                myViewHolder.progress_senderImage.setVisibility(View.GONE);
                                myViewHolder.sender_image.setVisibility(View.VISIBLE);
                                Glide.with(context).load(MyUrls.Imgurl + attendee_messageObj.getRes_senderLogo()).centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(myViewHolder.sender_image);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                myViewHolder.progress_senderImage.setVisibility(View.GONE);
                                myViewHolder.sender_image.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(myViewHolder.sender_image);
                myViewHolder.btn_txtcomment.setText(defaultLang.get13Comment());
                myViewHolder.btn_txtcomment.setOnClickListener(v -> {
                    if (!isHide) {
                        linear_commentBox_array.get(position).setVisibility(View.VISIBLE);
                        isHide = true;
                    } else {
                        linear_commentBox_array.get(position).setVisibility(View.GONE);
                        isHide = false;
                    }

                });


                if (attendee_messageObj.getStr_img().equalsIgnoreCase("")) {
                    myViewHolder.image.setVisibility(View.GONE);
                    myViewHolder.progressBar1.setVisibility(View.GONE);
                } else {
                    Log.d("AITL Adapter Private", attendee_messageObj.getRes_id() + " " + attendee_messageObj.getStr_img());

                    myViewHolder.image.setVisibility(View.VISIBLE);

                    Glide.with(context)
                            .load(attendee_messageObj.getStr_img())
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                    myViewHolder.image.setVisibility(View.GONE);
                                    myViewHolder.progressBar1.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                    myViewHolder.progressBar1.setVisibility(View.GONE);
                                    myViewHolder.image.setVisibility(View.VISIBLE);
                                    return false;
                                }
                            })
                            .crossFade()
                            .centerCrop()
                            .fitCenter()
                            .into(myViewHolder.image);
                    myViewHolder.image.setOnClickListener(v -> {

//                        if(sessionManager.isLogin())
//                        {
//                            if (sessionManager.viewImg_tag.equalsIgnoreCase("speaker"))
//                            {
//                                Log.d("AITL Speaker MessageId", Speaker_Detail_Fragment.str_message_id);
//                                ViewImageDialog imageDialog = new ViewImageDialog();
//                                imageDialog.show(((Activity) context).getFragmentManager(), "IMAGE");
//
//                            }
//                            else
//                            if (sessionManager.viewImg_tag.equalsIgnoreCase("Exhibitor"))
//                            {
//                                Log.d("AITL Exhibitor MessageId", Exhibitor_Detail_Fragment.str_message_id);
//                                ViewImageDialog imageDialog = new ViewImageDialog();
//                                imageDialog.show(((Activity) context).getFragmentManager(), "IMAGE");
//                            }
//                            else
                        if (sessionManager.viewImg_tag.equalsIgnoreCase("privateMessage")) {
                            Log.d("AITL  MessageId", Private_Message_Fragment.str_message_id);
                            ViewImageDialog imageDialog = new ViewImageDialog();
                            imageDialog.show(((Activity) context).getFragmentManager(), "IMAGE");
                        } else if (sessionManager.viewImg_tag.equalsIgnoreCase("publicMessage")) {
                            Log.d("AITL  MessageId", PublicMessage_Fragment.str_message_id);
                            ViewImageDialog imageDialog = new ViewImageDialog();
                            imageDialog.show(((Activity) context).getFragmentManager(), "IMAGE");
                        }
//                            else if (sessionManager.viewImg_tag.equalsIgnoreCase("attendee"))
//                            {
//                                Log.d("AITL Attendee MessageId", Attendance_Detail_Fragment.str_message_id);
//                                ViewImageDialog imageDialog = new ViewImageDialog();
//                                imageDialog.show(((Activity) context).getFragmentManager(), "IMAGE");
//                            }
                        else if (sessionManager.viewImg_tag.equalsIgnoreCase("activity")) {
//                                Log.d("AITL Attendee MessageId", Attendance_Detail_Fragment.str_message_id);
                            ViewImageDialog imageDialog = new ViewImageDialog();
                            imageDialog.show(((Activity) context).getFragmentManager(), "IMAGE");
                        }
                    });


                }
                if (commentArrayList.size() == 0) {
                    myViewHolder.rv_viewCommt.setVisibility(View.GONE);
                } else {
//                myViewHolder.txt_viewComment.setVisibility(View.VISIBLE);

                    myViewHolder.rv_viewCommt.setVisibility(View.VISIBLE);
                    comment_adapter = new Attendee_Comment_Adapter(commentArrayList, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    myViewHolder.rv_viewCommt.setLayoutManager(mLayoutManager);
                    myViewHolder.rv_viewCommt.setItemAnimator(new DefaultItemAnimator());
                    myViewHolder.rv_viewCommt.setAdapter(comment_adapter);
                }
                myViewHolder.btn_comment.setOnClickListener(v -> {

                    String str_comment = myViewHolder.edt_comment.getText().toString();
                    if (str_comment.trim().length() == 0) {
                        if (picturePath.length() != 0) {
                            commentMessage(str_comment, attendee_messageObj.getRes_id(), attendee_messageObj.getTag());
                        } else {
                            ToastC.show(context, "Please enter comment");
                        }
                    } else {
                        if (sessionManager.isLogin()) {
                            commentMessage(str_comment, attendee_messageObj.getRes_id(), attendee_messageObj.getTag());
                            myViewHolder.edt_comment.setText("");
                        } else {
                            ToastC.show(context, "Please Login First");
                        }
                    }
                });
                if (attendee_messageObj.getSender_id().equalsIgnoreCase(sessionManager.getUserId())) {
                    myViewHolder.delete_img.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.delete_img.setVisibility(View.GONE);
                }
                myViewHolder.delete_img.setOnClickListener(v -> {
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title("Delete")
                            .items("Are you sure you want to Delete this Message?")
                            .positiveColor(context.getResources().getColor(R.color.colorAccent))
                            .positiveText("Delete")
                            .negativeText("Cancel")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {
                                    if (attendee_messageObj.getTag().equalsIgnoreCase("public_message")) {

                                        if (messageArrayList.size() != 0) {
                                            try {
                                                DeleteMessage(attendee_messageObj.getRes_id(), attendee_messageObj.getTag());
//                                                messageArrayList.remove(position);
//                                                PublicMessage_Fragment.rv_viewMessage.removeViewAt(position);
//                                                notifyItemRemoved(position);
//                                                notifyItemRangeChanged(position, messageArrayList.size());
//                                                notifyDataSetChanged();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    } else if (attendee_messageObj.getTag().equalsIgnoreCase("private_message")) {

                                        if (messageArrayList.size() != 0) {
                                            try {
                                                DeleteMessage(attendee_messageObj.getRes_id(), attendee_messageObj.getTag());
//                                                messageArrayList.remove(position);
//                                                Private_Message_Fragment.rv_viewMessage.removeViewAt(position);
//                                                notifyItemRemoved(position);
//                                                notifyItemRangeChanged(position, messageArrayList.size());
//                                                notifyDataSetChanged();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    } else if (attendee_messageObj.getTag().equalsIgnoreCase("exhibitor_detail")) {

                                        if (messageArrayList.size() != 0) {
                                            try {
                                                DeleteMessage(attendee_messageObj.getRes_id(), attendee_messageObj.getTag());
//                                                messageArrayList.remove(position);
//                                                Exhibitor_Detail_Fragment.rv_viewMessage.removeViewAt(position);
//                                                notifyItemRemoved(position);
//                                                notifyItemRangeChanged(position, messageArrayList.size());
//                                                notifyDataSetChanged();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }

                                }
                            })
                            .onNeutral((dialog1, which) -> dialog1.dismiss())
                            .cancelable(false)
                            .build();
                    dialog.show();
                });

                myViewHolder.btndelete.setOnClickListener(v -> {
                    picturePath = "";
                    commLinearLayout.get(position).setVisibility(View.GONE);

                });


                if (attendee_messageObj.getTag().equalsIgnoreCase("public_message")) {

                    myViewHolder.img_commentUpload.setOnClickListener(v -> {
                        MaterialDialog dialog = new MaterialDialog.Builder(context)
                                .title("Choose Image From")
                                .items(R.array.imageItem)
                                .itemsCallback((dialog12, itemView, which, text) -> {
                                    if (which == 0) {
                                        //gallery
                                        GlobalData.pos = position;
                                        GlobalData.currentFragment = GlobalData.CurrentFragment.PublicMessage;
                                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        i.setType("image/*");
                                        activity.startActivityForResult(i, 5);

                                    } else if (which == 1) {
                                        //camera
                                        GlobalData.pos = position;
                                        GlobalData.currentFragment = GlobalData.CurrentFragment.PublicMessage;

                                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        activity.startActivityForResult(i, 2);

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
                                })
                                .build();
                        dialog.show();
                    });

                } else if (attendee_messageObj.getTag().equalsIgnoreCase("private_message")) {

                    myViewHolder.img_commentUpload.setOnClickListener(v -> {
                        MaterialDialog dialog = new MaterialDialog.Builder(context)
                                .title("Choose Image From")
                                .items(R.array.imageItem)
                                .itemsCallback((dialog13, itemView, which, text) -> {
                                    if (which == 0) {
                                        //gallery
                                        GlobalData.pos = position;
                                        GlobalData.currentFragment = GlobalData.CurrentFragment.PrivateMessage;
                                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        i.setType("image/*");
                                        activity.startActivityForResult(i, 5);

                                    } else if (which == 1) {
                                        //camera
                                        GlobalData.pos = position;
                                        GlobalData.currentFragment = GlobalData.CurrentFragment.PrivateMessage;
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
                                })
                                .build();
                        dialog.show();
                    });


                } else if (attendee_messageObj.getTag().equalsIgnoreCase("exhibitor_detail")) {

                    myViewHolder.img_commentUpload.setOnClickListener(v -> {
                        MaterialDialog dialog = new MaterialDialog.Builder(context)
                                .title("Choose Image From")
                                .items(R.array.imageItem)
                                .itemsCallback((dialog14, itemView, which, text) -> {
                                    if (which == 0) {
                                        //gallery
                                        GlobalData.pos = position;
                                        GlobalData.currentFragment = GlobalData.CurrentFragment.ExhibitorDetail;
                                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        i.setType("image/*");
                                        activity.startActivityForResult(i, 5);

                                    } else if (which == 1) {
                                        //camera
                                        GlobalData.pos = position;
                                        GlobalData.currentFragment = GlobalData.CurrentFragment.ExhibitorDetail;
                                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                                        activity.startActivityForResult(i, 2);
                                    }
                                })
                                .build();
                        dialog.show();
                    });

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (holder instanceof ProgressViewHolder) {

            ProgressViewHolder viewHolder = (ProgressViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }


    private void loadCamera() {
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(i, 2);
    }


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((MainActivity) context).checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ((MainActivity) context).checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean camerAaddPermission(List<String> permissionsList, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (((MainActivity) context).checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!((MainActivity) context).shouldShowRequestPermissionRationale(permission))
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

                ((MainActivity) context).requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
    }

    public void onRequestPermessionView(int requestCode, String[] permissions, int[] grantResults) {
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
        }
    }


    private void approvedStatus(String attndeeId) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.approvedshareContactDetail, Param.shareContactInformation(sessionManager.getEventId(), attndeeId, sessionManager.getUserId()), 6, true, this);
    }

    private void commentMessage(String comment_msg, String res_MessageId, String tag) {
        Log.d("AITL  CommentMessage", comment_msg);
        Log.d("AITL  PicturePath", picturePath);
        if (tag.equalsIgnoreCase("public_message")) {
            new VolleyRequest((Activity) context, MyUrls.public_commentRequest, Param.message_img(new File(picturePath)), Param.CommentMessage(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), res_MessageId, comment_msg), 2, true, this);
        } else if (tag.equalsIgnoreCase("private_message")) {
            new VolleyRequest((Activity) context, MyUrls.public_commentRequest, Param.message_img(new File(picturePath)), Param.CommentMessage(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), res_MessageId, comment_msg), 4, true, this);
        } else if (tag.equalsIgnoreCase("exhibitor_detail")) {
            new VolleyRequest((Activity) context, MyUrls.exhibitor_makeComment, Param.message_img(new File(picturePath)), Param.CommentMessage(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), res_MessageId, comment_msg), 5, true, this);
        }
    }

    private void DeleteMessage(String res_messId, String tag) {
        Log.d("AITL  MessageId", res_messId);
//        new VolleyRequest(context, VolleyRequest.Method.POST, MyUrls.attendee_Delete_Message, Param.attendance_delete_message(sessionManager.getToken(), sessionManager.getUserId(), res_messId, sessionManager.getEventId()), 0, true, context);
        if (tag.equalsIgnoreCase("public_message")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.PubPriv_Delete_Message, Param.attendance_delete_message(sessionManager.getToken(), sessionManager.getUserId(), res_messId, sessionManager.getEventId()), 2, true, this);
        } else if (tag.equalsIgnoreCase("private_message")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.PubPriv_Delete_Message, Param.attendance_delete_message(sessionManager.getToken(), sessionManager.getUserId(), res_messId, sessionManager.getEventId()), 4, true, this);
        } else if (tag.equalsIgnoreCase("exhibitor_detail")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.exhibitor_deleteMessage, Param.attendance_delete_message(sessionManager.getToken(), sessionManager.getUserId(), res_messId, sessionManager.getEventId()), 5, true, this);
        }

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                        ((MainActivity) context).reloadFragment();
                    } else {
                        Log.d("AITL", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //  MyViewHolder.edt_comment.setText("");
                        GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                        ((MainActivity) context).reloadFragment();
                    } else {
                        Log.d("AITL", jsonObject.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //  MyViewHolder.edt_comment.setText("");
                        GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                        ((MainActivity) context).reloadFragment();
                    } else {
                        Log.d("AITL", jsonObject.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        //  MyViewHolder.edt_comment.setText("");
                        GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                        ((MainActivity) context).reloadFragment();
                    } else {
                        Log.d("AITL", jsonObject.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL STATUS APRROVED", jsonObject.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (messageArrayList.get(position) == null) {
            return VIEW_PROG;
        } else {
            return TYPE_ITEM;
        }
    }

    public void addFooter() {
        messageArrayList.add(null);


        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(messageArrayList.size() - 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFooter() {

        try {

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    messageArrayList.remove(messageArrayList.size() - 1);
                    notifyItemRemoved(messageArrayList.size());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public Attendee_message getItem(int position) {
        return messageArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("AITL", "Gallaray");

        if (resultCode == Activity.RESULT_OK) {

            Log.d("AITL", "Gallaray");
            ImageView imageView = commImageView.get(GlobalData.pos);
            LinearLayout linearLayout = commLinearLayout.get(GlobalData.pos);
            if (requestCode == 5) {

                Log.d("AITL", "Gallaray");
                Log.d("AITL Data", "" + data);

                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.d("AITL picturepath", picturePath);
                    //  notifyDataSetChanged();


                    Glide.with(context).load("file://" + picturePath).into(imageView);
                    if (picturePath.length() == 0) {
                        linearLayout.setVisibility(View.GONE);
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                        Glide.with(context).load("file://" + picturePath).into(imageView);
                        //Attendee_message_Adapter.MyViewHolder.imggallaryimages.setImageBitmap(BitmapFactory.decodeFile(adapterPicturePath));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Log.d("AITL", "Camera");

                try {
                    bitmapImage = (Bitmap) data.getExtras().get("data");
                    Log.d("AITL ImageBitMap", bitmapImage.toString());
                    Uri tempUri = getImageUri(context, bitmapImage);
                    picturePath = getRealPathFromURI(tempUri);
                    Log.d("AITL Camerapath", picturePath);
                    if (picturePath.length() == 0) {
                        linearLayout.setVisibility(View.GONE);
                    } else {
                        linearLayout.setVisibility(View.VISIBLE);
                        Glide.with(context).load("file://" + picturePath).into(imageView);
                        //Attendee_message_Adapter.MyViewHolder.imggallaryimages.setImageBitmap(BitmapFactory.decodeFile(adapterPicturePath));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Log.d("AITL ImageUri", inImage.toString());
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.d("AITL ImageUri", path.toString());
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView sender_image, comment_image;
        TextView user_sender, user_receiver, txt_time, txt_message, txt_label, btn_txtcomment, txt_desiredReciver;
        //        CircleIndicator

        public RecyclerView rv_viewCommt;
        public ImageView delete_img, img_commentUpload, imggallaryimages, image, btndelete;
        ProgressBar progressBar1, progress_senderImage;
        final EditText edt_comment;
        public LinearLayout linear_loadPhoto, linear_commentBox;
        public static FrameLayout linear_viewpager;

        Button btn_comment;


        public MyViewHolder(View itemView) {
            super(itemView);
            sender_image = (CircleImageView) itemView.findViewById(R.id.sender_image);
            comment_image = (CircleImageView) itemView.findViewById(R.id.comment_image);
            user_sender = (TextView) itemView.findViewById(R.id.user_sender);
            user_receiver = (TextView) itemView.findViewById(R.id.user_receiver);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_message = (TextView) itemView.findViewById(R.id.txt_message);
            txt_desiredReciver = (TextView) itemView.findViewById(R.id.txt_desiredReciver);
            txt_label = (TextView) itemView.findViewById(R.id.txt_label);
            delete_img = (ImageView) itemView.findViewById(R.id.delete_img);
            image = (ImageView) itemView.findViewById(R.id.image);
            img_commentUpload = (ImageView) itemView.findViewById(R.id.img_commentUpload);
            edt_comment = (EditText) itemView.findViewById(R.id.edt_comment);
            btn_comment = (Button) itemView.findViewById(R.id.btn_comment);
            rv_viewCommt = (RecyclerView) itemView.findViewById(R.id.rv_viewCommt);
            progressBar1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            progress_senderImage = (ProgressBar) itemView.findViewById(R.id.progress_senderImage);
            imggallaryimages = (ImageView) itemView.findViewById(R.id.imggallaryimages);
            btndelete = (ImageView) itemView.findViewById(R.id.btndelete);
            linear_loadPhoto = (LinearLayout) itemView.findViewById(R.id.linear_loadPhoto);
            linear_commentBox = (LinearLayout) itemView.findViewById(R.id.linear_commentBox);
            linear_viewpager = (FrameLayout) itemView.findViewById(R.id.linear_viewpager);
            btn_txtcomment = (TextView) itemView.findViewById(R.id.btn_txtcomment);
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }


}