package com.zhilian.click;

public class AntiMadclick {
	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		if ( time - lastClickTime < 500) {
			lastClickTime = time;
			return true;
		}
		lastClickTime = time;
		return false;
	} 
}
