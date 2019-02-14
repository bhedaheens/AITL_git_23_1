package com.allintheloop.Adapter;

import android.view.View;

/**
 * Created by nteam on 7/9/17.
 */

public interface RecyclerViewClickListener {


    public void onItemClick(View view, int position, Object o);

    /*GestureDetector gestureDetector;

    public RecyclerViewClickListener(Context context, OnItemClickListener listener){
        itemClickListener = listener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(),e.getY());
        if(childView!=null && itemClickListener != null && gestureDetector.onTouchEvent(e)){
            itemClickListener.onItemClick(childView,rv.getChildAdapterPosition(childView), rv.getAdapter().getList().get(position));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }*/
}
