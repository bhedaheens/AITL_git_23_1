package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.allintheloop.Bean.Fundraising.FundraisingHome_footer;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by nteam on 6/6/16.
 */
public class FundraisingHome_footer_adapter extends PagerAdapter {

    Context context;
    ArrayList<FundraisingHome_footer> arrayList;
    CircleImageView circleImageView;
    TextView txt_full_name, txt_product_name, txt_product_amt;
    CardView footer_card;
    SessionManager sessionManager;
    String currency_status;

    public FundraisingHome_footer_adapter(Context context, ArrayList<FundraisingHome_footer> arrayList, String currency_status) {
        this.context = context;
        this.arrayList = arrayList;
        this.currency_status = currency_status;
        sessionManager = new SessionManager(context);

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.adapter_fundraishinghome_footer, container, false);
        FundraisingHome_footer footerObj = arrayList.get(position);

        Log.d("ArrayImg", footerObj.getFirst_name());
        circleImageView = (CircleImageView) viewItem.findViewById(R.id.user_image);
        txt_full_name = (TextView) viewItem.findViewById(R.id.txt_full_name);
        txt_product_name = (TextView) viewItem.findViewById(R.id.txt_product_name);
        txt_product_amt = (TextView) viewItem.findViewById(R.id.txt_product_amt);
        footer_card = (CardView) viewItem.findViewById(R.id.footer_card);
        footer_card.setCardBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
        Log.d("AITL FOOTER IMAGE", footerObj.getLogo());
        Glide.with(context).load(footerObj.getLogo()).placeholder(R.drawable.profile).centerCrop().fitCenter().into(circleImageView);
        txt_full_name.setText(footerObj.getFirst_name() + " " + footerObj.getLast_name());
        txt_product_name.setText(footerObj.getProduct_name());


        if (currency_status.equalsIgnoreCase("euro")) {
            txt_product_amt.setText(context.getResources().getString(R.string.euro) + footerObj.getAmt());
        } else if (currency_status.equalsIgnoreCase("gbp")) {
            txt_product_amt.setText(context.getResources().getString(R.string.pound_sign) + footerObj.getAmt());
        } else if (currency_status.equalsIgnoreCase("usd") || currency_status.equalsIgnoreCase("aud")) {
            txt_product_amt.setText(context.getResources().getString(R.string.dollor) + footerObj.getAmt());
        }
        container.addView(viewItem);
        return viewItem;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }

}
