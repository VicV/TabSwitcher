package com.example.tabswitcher;

public class Tab {
	private int mResId;
	private int mFaviconId;

	public Tab(int resId, int faviconId) {
		setResId(resId);
		setFaviconId(faviconId);
	}

	public int getResId() {
		return mResId;
	}

	public void setResId(int resId) {
		mResId = resId;
	}

	public int getFaviconId() {
		return mFaviconId;
	}

	public void setFaviconId(int faviconId) {
		mFaviconId = faviconId;
	}

}
