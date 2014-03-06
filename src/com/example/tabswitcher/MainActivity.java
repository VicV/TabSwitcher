package com.example.tabswitcher;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.DragEvent;
import android.view.KeyEvent;
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

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mRoot = findViewById(R.id.root);
		mContext = this;
		View tabsButton = findViewById(R.id.tabs_button);
		mSwitcher = (TabSwitcher) findViewById(R.id.switcher);
		tabsButton.setOnTouchListener(new TabButtonClickListener());
		mSwitcher.mOnTabItemHoverListener = new TabSwitcher.OnTabItemHoverListener() {
			@Override public void onTabItemHover(Tab tab) {
				mRoot.setBackgroundResource(tab.getResId());
			}

			@Override public void onDrop(Tab tab) {
				mRoot.setBackgroundResource(tab.getResId());
			}
		};
		mSwitcher.setOnDragListener(new TabButtonDragListener());
		tabsButton.setOnTouchListener(new TabButtonClickListener());
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}

	private void makeVibration() {
		final long[] vibrationPattern = { 0L, 18L };
		final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(vibrationPattern, -1);
	}

	@Override public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && mSwitcher.isShown() && !mSwitcher.mInDragMode) {
			mSwitcher.hide();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class TabButtonClickListener implements View.OnTouchListener {
		@Override public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
				v.startDrag(null, shadowBuilder, null, 0);
				mSwitcher.mInDragMode = true;
				mSwitcher.show();
				makeVibration();
				return true;
			}
			return false;
		}
	}

	class TabButtonDragListener implements View.OnDragListener {
		@Override public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				mSwitcher.mInDragMode = false;
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				break;
			default:
				break;
			}
			return true;
		}
	}
}
