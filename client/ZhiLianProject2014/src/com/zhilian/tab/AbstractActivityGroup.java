package com.zhilian.tab;

import java.util.List;

import android.app.ActivityGroup;
import android.view.KeyEvent;
import android.view.View;

@SuppressWarnings("deprecation")
public abstract class AbstractActivityGroup extends ActivityGroup {

	public List<View> viewHistory;
	
	public void replaceContentView(View view) {
        viewHistory.add(view);  
        setContentView(view);  
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK 
				&& event.getRepeatCount() == 0) {
			onBackPressed();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		back();
	}
	
	public void back() {
		if(viewHistory.isEmpty() != true) {
			viewHistory.remove(viewHistory.get(viewHistory.size() - 1));
			if(viewHistory.isEmpty() == true) {
				finish();
				return;
			}
			setContentView(viewHistory.get(viewHistory.size() - 1));
		} else {
			finish();
		}
	}
	
	// http://blog.csdn.net/lzg08_08/article/details/8566639
}
