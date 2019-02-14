package com.allintheloop.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.allintheloop.Adapter.Attendee.Attendee_Comment_Adapter;
import com.allintheloop.Adapter.Exhibitor.Exhibitor_ImageAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.Attendee.Attendee_Comment;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.Bean.photoSection;
import com.allintheloop.Fragment.Photo_Fragment;
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
 * Created by nteam on 9/7/16.
 */
public class Photo_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VolleyInterface {
    public static ArrayList<photoSection> photoSectionArrayList;
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
    public ArrayList<TextView> liketxtView = new ArrayList<>();
    public ArrayList<LinearLayout> linear_commentBox_array = new ArrayList<>();
    boolean isHide = false;
    private static final int TYPE_ITEM = 1;
    Handler handler;

    List<String> permissionsNeeded;
    List<String> permissionsList;

    public Photo_Adapter(ArrayList<photoSection> photoSectionArrayList, RecyclerView recyclerView, Context context, final LinearLayoutManager layoutManager, Activity activity, NestedScrollView scrollView) {
        this.photoSectionArrayList = photoSectionArrayList;
        this.context = context;
        this.activity = activity;
        sessionManager = new SessionManager(this.context);
        handler = new Handler();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            this.lytManager = layoutManager;

            scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

                @Override
                public void onScrollChange(NestedScrollView scrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {


                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }

                        loading = true;
                    }
                }
            });

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_photo_fragment, parent, false);
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
                liketxtView.add(myViewHolder.txt_like);
                linear_commentBox_array.add(myViewHolder.linear_commtBox);
                final photoSection photoSection_obj = photoSectionArrayList.get(position);

                imageArrayList = photoSection_obj.getImageArrayList();
                commentArrayList = photoSection_obj.getCommentArrayList();

                myViewHolder.user_sender.setText(photoSection_obj.getSender_name());
                myViewHolder.txt_time.setText(photoSection_obj.getTime_stamp());
                myViewHolder.txt_peopleLike.setText(photoSection_obj.getTotal_like() + " " + "people like this");
                myViewHolder.txt_comment.setText(photoSection_obj.getTotal_comment() + " " + "comments");


                //  Glide.with(context).load(MyUrls.Imgurl + photoSection_obj.getSender_logo()).centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(myViewHolder.sender_image);
                Log.d("AITL SenderLogo", MyUrls.Imgurl + photoSection_obj.getSender_logo());


                Glide.with(context)
                        .load(MyUrls.Imgurl + photoSection_obj.getSender_logo())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                myViewHolder.progress_senderImage.setVisibility(View.GONE);
                                myViewHolder.sender_image.setVisibility(View.VISIBLE);
                                Glide.with(context).load("").centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(myViewHolder.sender_image);
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

                Glide.with(context)
                        .load(MyUrls.thumImgUrl + sessionManager.getImagePath())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
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


                myViewHolder.btn_txtcomment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isHide) {
                            linear_commentBox_array.get(position).setVisibility(View.VISIBLE);
                            isHide = true;
                        } else {
                            linear_commentBox_array.get(position).setVisibility(View.GONE);
                            isHide = false;
                        }

                    }
                });


                if (photoSection_obj.getStr_image().equalsIgnoreCase("")) {
                    myViewHolder.image.setVisibility(View.GONE);
                    myViewHolder.progressBar1.setVisibility(View.GONE);
                } else {


                    myViewHolder.image.setVisibility(View.VISIBLE);

                    Glide.with(context)
                            .load(photoSection_obj.getStr_image())
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


                    myViewHolder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

//                        if (sessionManager.isLogin())
//                        {
//                            Log.d("AITL Photo FEEDID", "Photo Feed"+ Photo_Fragment.str_feedID);
                            ViewImageDialog imageDialog = new ViewImageDialog();
                            imageDialog.show(((Activity) context).getFragmentManager(), "IMAGE");
//                        }
//                        else
//                        {
//                            ToastC.show(context,"Please Login First");
//                        }
//

                        }
                    });


                }
                if (commentArrayList.size() == 0) {
                    myViewHolder.rv_viewCommt.setVisibility(View.GONE);
                } else {
                    myViewHolder.rv_viewCommt.setVisibility(View.VISIBLE);
                    comment_adapter = new Attendee_Comment_Adapter(commentArrayList, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                    myViewHolder.rv_viewCommt.setLayoutManager(mLayoutManager);
                    myViewHolder.rv_viewCommt.setItemAnimator(new DefaultItemAnimator());
                    myViewHolder.rv_viewCommt.setAdapter(comment_adapter);
                }


                myViewHolder.btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        picturePath = "";
                        commLinearLayout.get(position).setVisibility(View.GONE);

                    }
                });

                if (photoSection_obj.getIs_like().equalsIgnoreCase("1")) {
//                myViewHolder.txt_like.setTE
                    liketxtView.get(position).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumbs_blue, 0, 0, 0);
//                myViewHolder.txt_like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumbs_blue, 0, 0, 0);
//                 myViewHolder.txt_like.setText("Like");
                    photoSection_obj.setIs_like("1");
                } else {
                    liketxtView.get(position).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumbs, 0, 0, 0);
                    photoSection_obj.setIs_like("0");
                }

                if (sessionManager.isLogin()) {
                    myViewHolder.txt_like.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (photoSection_obj.getIs_like().equalsIgnoreCase("1")) {
                                Log.d("AITL TotalLike", photoSection_obj.getTotal_like());
                                dislikeFeed(photoSection_obj.getFeed_id());
                                int like = Integer.parseInt(photoSection_obj.getTotal_like());
                                Log.d("AITL Like Variable", "" + like);
                                myViewHolder.txt_peopleLike.setText("" + (like - 1) + " " + "people like this");
                                photoSection_obj.setTotal_like("" + (like - 1));
                                liketxtView.get(position).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumbs, 0, 0, 0);
                                photoSection_obj.setIs_like("0");
                            } else {

                                Log.d("AITL TotalLike", photoSection_obj.getTotal_like());
                                likeFeed(photoSection_obj.getFeed_id());
                                int like = Integer.parseInt(photoSection_obj.getTotal_like());
                                Log.d("AITL Like Variable", "" + like);
                                myViewHolder.txt_peopleLike.setText("" + (like + 1) + " " + "people like this");
                                photoSection_obj.setTotal_like("" + (like + 1));
                                liketxtView.get(position).setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_thumbs_blue, 0, 0, 0);
                                photoSection_obj.setIs_like("1");
                            }
                        }
                    });
                }

                myViewHolder.btn_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("AITL Button Click", myViewHolder.edt_comment.getText().toString());
                        String str_comment = myViewHolder.edt_comment.getText().toString();
                        Log.d("AITL Stringcomment", str_comment);
                        if (str_comment.trim().length() == 0) {
                            if (picturePath.length() != 0) {
                                commentMessage(str_comment, photoSection_obj.getFeed_id());
                            } else {
                                ToastC.show(context, "Please enter comment");
                            }
                        } else {
                            if (sessionManager.isLogin()) {
                                commentMessage(str_comment, photoSection_obj.getFeed_id());
                                myViewHolder.edt_comment.setText("");
                            }
                        }
                    }
                });

                myViewHolder.img_commentUpload.setOnClickListener(new View.OnClickListener() {
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
                if (photoSection_obj.getSender_id().equalsIgnoreCase(sessionManager.getUserId())) {
                    myViewHolder.delete_img.setVisibility(View.VISIBLE);
                    myViewHolder.delete_img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MaterialDialog dialog = new MaterialDialog.Builder(context)
                                    .title("Delete")
                                    .items("Are you sure you want to Delete this Feed?")
                                    .positiveColor(context.getResources().getColor(R.color.colorAccent))
                                    .positiveText("Delete")
                                    .negativeText("Cancel")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(MaterialDialog dialog, DialogAction which) {

                                            if (photoSectionArrayList.size() != 0) {
                                                try {
                                                    DeleteMessage(photoSection_obj.getFeed_id());
                                                    photoSectionArrayList.remove(position);
                                                    Photo_Fragment.rv_viewMessage.removeViewAt(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, photoSectionArrayList.size());
                                                    notifyDataSetChanged();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    })
                                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
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
                } else {
                    myViewHolder.delete_img.setVisibility(View.GONE);
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
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("Choose Image From")
                .items(R.array.imageItem)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        if (which == 0) {
                            //gallery
                            GlobalData.pos = position;
                            GlobalData.currentFragment = GlobalData.CurrentFragment.photo;
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            i.setType("image/*");
                            activity.startActivityForResult(i, 5);

                        } else if (which == 1) {
                            //camera
                            GlobalData.pos = position;
                            GlobalData.currentFragment = GlobalData.CurrentFragment.photo;
                            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            activity.startActivityForResult(i, 2);

                        }
                    }
                })
                .build();
        dialog.show();
//        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        activity.startActivityForResult(i, 2);
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


    private void DeleteMessage(String res_feedId) {
        Log.d("AITL Adapter MessageId", res_feedId);
//        new VolleyRequest(context, VolleyRequest.Method.POST, MyUrls.attendee_Delete_Message, Param.attendance_delete_message(sessionManager.getToken(), sessionManager.getUserId(), res_messId, sessionManager.getEventId()), 0, true, context);
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.photoDeleteFeed, Param.photoDeleteFeed(sessionManager.getToken(), sessionManager.getUserId(), res_feedId, sessionManager.getEventId()), 0, true, this);


    }

    private void likeFeed(String res_feedId) {
        Log.d("AITL Adapter MessageId", res_feedId);
//        new VolleyRequest(context, VolleyRequest.Method.POST, MyUrls.attendee_Delete_Message, Param.attendance_delete_message(sessionManager.getToken(), sessionManager.getUserId(), res_messId, sessionManager.getEventId()), 0, true, context);
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.photolikeFeed, Param.photolikeFeed(sessionManager.getToken(), sessionManager.getUserId(), res_feedId, sessionManager.getEventId()), 2, true, this);


    }

    private void dislikeFeed(String res_feedId) {
        Log.d("AITL Adapter MessageId", res_feedId);
//        new VolleyRequest(context, VolleyRequest.Method.POST, MyUrls.attendee_Delete_Message, Param.attendance_delete_message(sessionManager.getToken(), sessionManager.getUserId(), res_messId, sessionManager.getEventId()), 0, true, context);
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.photodislikeFeed, Param.photolikeFeed(sessionManager.getToken(), sessionManager.getUserId(), res_feedId, sessionManager.getEventId()), 3, true, this);


    }

    private void commentMessage(String comment_msg, String feed_id) {
        Log.d("AITL CommentMessage", comment_msg);
        Log.d("AITL  PicturePath", picturePath);

        new VolleyRequest((Activity) context, MyUrls.photo_makeComment, Param.message_img(new File(picturePath)), Param.Commentphoto(sessionManager.getEventId(), sessionManager.getToken(), sessionManager.getUserId(), feed_id, comment_msg), 1, true, this);
    }

    public photoSection getItem(int position) {
        return photoSectionArrayList.get(position);
    }


    @Override
    public int getItemViewType(int position) {

        if (photoSectionArrayList.get(position) == null) {
            return VIEW_PROG;
        } else {
            return TYPE_ITEM;
        }
    }

    public void addFooter() {

        photoSectionArrayList.add(null);
        try {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(photoSectionArrayList.size() - 1);
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
                    photoSectionArrayList.remove(photoSectionArrayList.size() - 1);
                    notifyItemRemoved(photoSectionArrayList.size());
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


    @Override
    public int getItemCount() {
        return photoSectionArrayList.size();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL DeleteFeed", jsonObject.getString("message"));

                        GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                        ((MainActivity) context).reloadFragment();
                    } else {
                        ToastC.show(context, jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL MakeComment", jsonObject.getString("message"));
                        GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                        ((MainActivity) context).reloadFragment();
                    } else {
                        ToastC.show(context, jsonObject.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL Like Comment ", jsonObject.getString("message"));

                    } else {
                        ToastC.show(context, jsonObject.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

        }

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView sender_image, comment_image;
        TextView user_sender, txt_time, txt_like, txt_comment, txt_peopleLike, btn_txtcomment;
        //        CircleIndicator
        public static RecyclerView rv_viewCommt;
        public ImageView delete_img, img_commentUpload, imggallaryimages, btndelete, image;
        final EditText edt_comment;
        public LinearLayout linear_loadPhoto, linear_commtBox;
        public static FrameLayout frame_viewpager;
        Button btn_comment;
        ProgressBar progressBar1, progress_senderImage;


        public MyViewHolder(View itemView) {
            super(itemView);
            sender_image = (CircleImageView) itemView.findViewById(R.id.sender_image);
            comment_image = (CircleImageView) itemView.findViewById(R.id.comment_image);
            progress_senderImage = (ProgressBar) itemView.findViewById(R.id.progress_senderImage);
            user_sender = (TextView) itemView.findViewById(R.id.user_sender);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            delete_img = (ImageView) itemView.findViewById(R.id.delete_img);
            image = (ImageView) itemView.findViewById(R.id.image);
            progressBar1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            img_commentUpload = (ImageView) itemView.findViewById(R.id.img_commentUpload);
            edt_comment = (EditText) itemView.findViewById(R.id.edt_comment);
            btn_comment = (Button) itemView.findViewById(R.id.btn_comment);
            rv_viewCommt = (RecyclerView) itemView.findViewById(R.id.rv_viewCommt);

            imggallaryimages = (ImageView) itemView.findViewById(R.id.imggallaryimages);
            btndelete = (ImageView) itemView.findViewById(R.id.btndelete);
            linear_loadPhoto = (LinearLayout) itemView.findViewById(R.id.linear_loadPhoto);
            frame_viewpager = (FrameLayout) itemView.findViewById(R.id.frame_viewpager);
            linear_commtBox = (LinearLayout) itemView.findViewById(R.id.linear_commtBox);
            txt_peopleLike = (TextView) itemView.findViewById(R.id.txt_peopleLike);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment);
            txt_like = (TextView) itemView.findViewById(R.id.txt_like);
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
