package com.example.tabswitcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class TabItemView extends View {
	private Context mContext;

    public TabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
		//LayoutInflater.from(context).inflate(R.layout.switcher, this);
		mContext = context;
    }

}
