package com.example.tabswitcher;

import java.util.LinkedList;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TabSwitcher extends LinearLayout {

	final String LOGTAG = "TabSwitcher";
	final ListView mList;
	final Context mContext;
	final Animation mAnimationFadeIn;
	final Animation mAnimationFadeOut;
	final Animation mAnimationTabsListEntrance;
	final Animation mAnimationTabsListExit;
	final Animation mAnimationTabsHoverEnter;
	final Animation mAnimationTabsHoverExit;
	final LinkedList<Tab> mTabs;
	OnTabItemHoverListener mOnTabItemHoverListener;
	public boolean mInDragMode = true;
	int mCurrentTabIndex;

	public TabSwitcher(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.switcher, this);
		mContext = context;
		mList = (ListView) findViewById(R.id.list);

		mTabs = new LinkedList<Tab>();
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j <= 4; j++)
				// Where J is the resource name (s1, s2, s3, s4, etc)
				mTabs.add(new Tab(getResources().getIdentifier("s" + j, "drawable", mContext.getPackageName()),
						R.drawable.fb));
		}

		mList.setAdapter(new TabListAdapter());
		setOnDragListener(new OnDragListener() {
			@Override public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_EXITED:
				case DragEvent.ACTION_DROP:
					hide();
					break;
				case DragEvent.ACTION_DRAG_STARTED:
				case DragEvent.ACTION_DRAG_ENTERED:
				case DragEvent.ACTION_DRAG_ENDED:
				default:
					break;
				}
				return true;
			}
		});
		mAnimationFadeOut = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
		mAnimationFadeOut.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {
				mList.startAnimation(mAnimationTabsListExit);
			}

			@Override public void onAnimationRepeat(Animation animation) {}

			@Override public void onAnimationEnd(Animation animation) {
				mList.setVisibility(View.INVISIBLE);
				setVisibility(View.GONE);
			}
		});

		mAnimationFadeIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
		mAnimationTabsListEntrance = AnimationUtils.loadAnimation(mContext, R.anim.tabs_list_entrance);
		mAnimationTabsListEntrance.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}

			@Override public void onAnimationRepeat(Animation animation) {}

			@Override public void onAnimationEnd(Animation animation) {
				mList.setVisibility(View.VISIBLE);
			}
		});

		mAnimationTabsListExit = AnimationUtils.loadAnimation(mContext, R.anim.tabs_list_exit);
		mAnimationFadeIn.setAnimationListener(new Animation.AnimationListener() {
			@Override public void onAnimationStart(Animation animation) {}

			@Override public void onAnimationRepeat(Animation animation) {}

			@Override public void onAnimationEnd(Animation animation) {
				mList.startAnimation(mAnimationTabsListEntrance);
			}
		});

		mAnimationTabsHoverEnter = AnimationUtils.loadAnimation(mContext, R.anim.tab_item_move_right);
		mAnimationTabsHoverEnter.setFillAfter(true);
		mAnimationTabsHoverExit = AnimationUtils.loadAnimation(mContext, R.anim.tab_item_move_left);
		mAnimationTabsHoverExit.setFillAfter(true);
		setOnClickListener(new OnClickListener() {
			@Override public void onClick(View v) {
				hide();
			}
		});
	}

	public void show() {
		setVisibility(View.VISIBLE);
		startAnimation(mAnimationFadeIn);
	}

	public void hide() {
		startAnimation(mAnimationFadeOut);
	}

	public void setCurrentTabAndClose() {
		Log.d(LOGTAG, "Current Tab: " + mCurrentTabIndex);
		final Tab currentTab = mTabs.remove(mCurrentTabIndex);
		Log.d(LOGTAG, "Current Tab Size: " + mTabs.size());
		mTabs.addFirst(currentTab);
		mList.setAdapter(new TabListAdapter());
		hide();
	}

	class TabListAdapter extends ArrayAdapter<Tab> {
		public TabListAdapter() {
			super(mContext, R.layout.switcher_list_item, mTabs);
		}

		@Override public View getView(final int position, View convertView, ViewGroup parent) {
			final Tab tab = mTabs.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.switcher_list_item, null);
				ImageView image = (ImageView) convertView.findViewById(R.id.thumbnail);
				image.setImageResource(tab.getResId());
				ImageView favicon = (ImageView) convertView.findViewById(R.id.favicon);
				favicon.setImageResource(tab.getFaviconId());
				convertView.setOnDragListener(new TabItemDragListener(position));
				convertView.setOnClickListener(new OnClickListener() {
					@Override public void onClick(View v) {
						if (!mInDragMode) {
							mOnTabItemHoverListener.onDrop(tab);
							mCurrentTabIndex = position;
							setCurrentTabAndClose();
						}
					}
				});
			}
			return convertView;
		}
	}

	class TabItemDragListener implements OnDragListener {
		int mTabIndex;

		public TabItemDragListener(int tabIndex) {
			mTabIndex = tabIndex;
		}

		@Override public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_ENTERED:
				mOnTabItemHoverListener.onTabItemHover(mTabs.get(mList.getPositionForView(v)));
				mCurrentTabIndex = mTabIndex;
				break;
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				if (mInDragMode) {
					setCurrentTabAndClose();
					mOnTabItemHoverListener.onDrop(mTabs.get(mTabIndex));
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
			default:
				break;
			}
			return true;
		}
	}

	interface OnTabItemHoverListener {
		public void onTabItemHover(Tab item);

		public void onDrop(Tab item);
	}
}
