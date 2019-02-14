package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.DrawerItem;
import com.allintheloop.Bean.Languages;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


/**
 * Created by nteam on 25/4/16.
 */
public class LanguageAdapter extends ArrayAdapter<Languages> {
    Context mContext;
    int layoutResourceId;
    ArrayList<Languages> Objdata;
    SessionManager sessionManager;
    Context context;

    public LanguageAdapter(Context context, int resource, String color) {
        super(context, resource);
    }

    public LanguageAdapter(Context mContext, int layoutResourceId, ArrayList<Languages> objdata, Context context) {
        super(mContext, layoutResourceId, objdata);
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        this.Objdata = objdata;
        this.context = context;
        sessionManager = new SessionManager(context);
        // preferences=context.getSharedPreferences(GlobalData.USER_PREFRENCE,Context.MODE_PRIVATE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Languages languages = Objdata.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder.txt_langName = (TextView) convertView.findViewById(R.id.txt_langName);
            holder.img_langIcon = (ImageView) convertView.findViewById(R.id.img_langIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txt_langName.setText(languages.lang_name);
//        holder.txt_langName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        Glide.with(mContext)
                .load(MyUrls.LangImgUrl + languages.lang_icon)
                .into(holder.img_langIcon);

        return convertView;
    }

    static class ViewHolder {
        private TextView txt_langName;
        private ImageView img_langIcon;
    }
}
