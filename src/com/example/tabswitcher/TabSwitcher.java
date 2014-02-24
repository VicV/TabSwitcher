package com.example.tabswitcher;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TabSwitcher extends LinearLayout {
    final String LOGTAG = "TabSwitcher";
    final ListView mList;
    final Context mContext;
    final Animation mAnimationFadeIn;
    final Animation mAnimationFadeOut;
    final ArrayList<Tab> mTabs;
    OnTabItemHoverListener mOnTabItemHoverListener;

    public TabSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.switcher, this);
        mContext = context;
        mList = (ListView) findViewById(R.id.list);

        mTabs = new ArrayList<Tab>();
        mTabs.add(new Tab(R.drawable.s1));
        mTabs.add(new Tab(R.drawable.s2));
        mTabs.add(new Tab(R.drawable.s3));
        mTabs.add(new Tab(R.drawable.s4)) ;
        mList.setAdapter(new TabListAdapter(mTabs));
        setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_EXITED:
                    case DragEvent.ACTION_DROP:
                        hide(); break;
                    case DragEvent.ACTION_DRAG_STARTED:
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_ENDED:
                   default: break;
                }
                return true;
            }
        });
        mAnimationFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
        mAnimationFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) {
               setVisibility(View.GONE);
            }
        });

        mAnimationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
    }

    public void show() {
        setVisibility(View.VISIBLE);
        startAnimation(mAnimationFadeIn);
        mList.requestFocus();
    }

    public void hide() {
        startAnimation(mAnimationFadeOut);
    }

    class TabListAdapter extends ArrayAdapter {
        final ArrayList<Tab> mTabs;

        public TabListAdapter(ArrayList<Tab> tabs) {
            super(mContext, R.layout.switcher_list_item, tabs);
            mTabs = tabs;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           final Tab tab = mTabs.get(position);
           if (convertView == null) {
               convertView =  LayoutInflater.from(getContext()).inflate(R.layout.switcher_list_item, null);
               convertView.setOnDragListener(new TabItemTouchListener());
                ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail);
                image.setImageResource(tab.mResId);
           }
           return convertView;
       }
    }

    class TabItemTouchListener implements OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED: {
                    Tab tab = mTabs.get(mList.getPositionForView(v));
                    mOnTabItemHoverListener.onTabItemHover(tab);
                    break;
                }
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    hide();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Tab tab = mTabs.get(mList.getPositionForView(v));
                    mOnTabItemHoverListener.onDrop(tab);
               default: break;
            }
            return true;
        }
    }

    interface OnTabItemHoverListener {
        public void onTabItemHover(Tab item);
        public void onDrop(Tab item);
    }
}
