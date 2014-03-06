package com.example.tabswitcher;

public class Tab {
	private int mResId;
	private int mFaviconId;

	public Tab(int resId, int faviconId) {
		mResId = resId;
		mFaviconId = faviconId;
	}

	public void setResId(int resId) {
		mResId = resId;
	}

	public int getResId() {
		return mResId;
	}

	public void setFaviconId(int faviconId) {
		mFaviconId = faviconId;
	}

	public int getFaviconId() {
		return mFaviconId;
	}
}