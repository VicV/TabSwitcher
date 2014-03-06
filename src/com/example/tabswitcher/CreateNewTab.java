package com.example.tabswitcher;

public class CreateNewTab extends Tab {

	private int mTabImage = R.drawable.newtab_dark;

	public CreateNewTab(int resId, int faviconId) {
		super(resId, faviconId);
	}

	public CreateNewTab(int resId, int faviconId, int tabImage) {
		super(resId, faviconId);
		setTabImage(tabImage);

	}

	public int getTabImage() {
		return mTabImage;
	}

	public void setTabImage(int tabImage) {
		mTabImage = tabImage;
	}

}
