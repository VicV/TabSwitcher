package com.example.tabswitcher;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MainActivity extends Activity {

    Context mContext;
    TabSwitcher mSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        View tabsButton = findViewById(R.id.tabs_button);
        mSwitcher = (TabSwitcher) findViewById(R.id.switcher);
        tabsButton.setOnClickListener(new TabButtonClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    class TabButtonClickListener implements View.OnClickListener {
        final Animation mAnimationFadeOut;
        final Animation mAnimationFadeIn;

        public TabButtonClickListener() {
            mAnimationFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
            mAnimationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {}
                @Override public void onAnimationRepeat(Animation animation) {}
                @Override public void onAnimationEnd(Animation animation) {
                    mSwitcher.setVisibility(View.GONE);
                }
            });

            mAnimationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        }

        @Override
        public void onClick(final View v) {
            if (mSwitcher.isShown()) {
                mSwitcher.startAnimation(mAnimationFadeOut);
            } else {
                mSwitcher.setVisibility(View.VISIBLE);
                mSwitcher.startAnimation(mAnimationFadeIn);
            }
        }
    }
}
