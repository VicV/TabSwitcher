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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TabSwitcher extends LinearLayout {
    final String LOGTAG = "TabSwitcher";
    final ListView mList;
    final Context mContext;
    final Animation mAnimationFadeIn;
    final Animation mAnimationFadeOut;

    public TabSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.switcher, this);
        mContext = context;
        mList = (ListView) findViewById(R.id.list);

        final ArrayList<Tab> tabs = new ArrayList<Tab>();
        tabs.add(new Tab());
        tabs.add(new Tab());
        tabs.add(new Tab());
        tabs.add(new Tab()) ;
        mList.setAdapter(new TabListAdapter(tabs));
        setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DROP) {
                    hide();
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
           }
           return convertView;
       }
    }

    class TabItemTouchListener implements OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            //Log.w(LOGTAG, event.getAction());
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setAlpha(1f);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setAlpha(0.5f);
                    break;
                case DragEvent.ACTION_DROP:
                    hide();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                  // do nothing
               default: break;
            }
            return true;
        }
    }
}
