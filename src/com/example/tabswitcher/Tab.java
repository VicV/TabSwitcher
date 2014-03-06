package com.example.tabswitcher;

public class Tab {
	private int mResId;
	private int mFaviconId;
	private String mTitle;

	public Tab(int resId, int faviconId, String title) {
		setResId(resId);
		setFaviconId(faviconId);
		setTitle(title);
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

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}
}
