package com.example.tabswitcher;

public class CreateNewTab extends Tab {

	private int mTabImage = R.drawable.newtab_dark;
	private static String mTitle = "Create new tab.";

	public CreateNewTab(int resId, int faviconId) {
		super(resId, faviconId, mTitle);
	}

	public CreateNewTab(int resId, int faviconId, int tabImage) {
		super(resId, faviconId, mTitle);
		setTabImage(tabImage);

	}

	public int getTabImage() {
		return mTabImage;
	}

	public void setTabImage(int tabImage) {
		mTabImage = tabImage;
	}

}
