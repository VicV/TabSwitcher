package com.example.tabswitcher;

import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.Window;

public class MainActivity extends Activity {
    final String LOGTAG = "MainActivity";
    Context mContext;
    TabSwitcher mSwitcher;
    View mRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        mRoot = findViewById(R.id.root);
        mContext = this;
        View tabsButton = findViewById(R.id.tabs_button);
        mSwitcher = (TabSwitcher) findViewById(R.id.switcher);
        tabsButton.setOnTouchListener(new TabButtonClickListener());
        mSwitcher.mOnTabItemHoverListener = new TabSwitcher.OnTabItemHoverListener() {
            @Override
            public void onTabItemHover(Tab tab) {
                mRoot.setBackgroundResource(tab.mResId);
            }

            @Override
            public void onDrop(Tab item) {}
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    private void makeVibration() {
        final long[] vibrationPattern = {0L, 18L};
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(vibrationPattern, -1);
    }

    class TabButtonClickListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(null, shadowBuilder, null, 0);
                mSwitcher.show();
                makeVibration();
                return true;
            }
            return false;
        }
    }
}
