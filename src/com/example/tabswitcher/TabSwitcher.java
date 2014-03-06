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
import android.widget.TextView;

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
        	mTabs.add(new Tab(R.drawable.s1, R.drawable.fb, "Francis Has Changed American CatholicsÕ Attitudes, but Not Their Behavior, a Poll Finds - NYTimes.com"));
            mTabs.add(new Tab(R.drawable.s2, R.drawable.fb, "Democrats in Senate Reject Pick by Obama - NYTimes.com"));
            mTabs.add(new Tab(R.drawable.s3, R.drawable.fb, "Home of the Mozilla Project Ñ Mozilla"));
            mTabs.add(new Tab(R.drawable.s4, R.drawable.fb, "Google"));
        }

		mList.setAdapter(new TabListAdapter());
		setOnDragListener(new OnDragListener() {
			@Override public boolean onDrag(View v, DragEvent event) {
				switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_EXITED:
				case DragEvent.ACTION_DROP: hide(); break;
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
		hide();
		final Tab currentTab = mTabs.remove(mCurrentTabIndex);
		mTabs.addFirst(currentTab);
		mList.setAdapter(new TabListAdapter());
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
				TextView titleView = (TextView) convertView.findViewById(R.id.title);
				titleView.setText(tab.getTitle());
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
