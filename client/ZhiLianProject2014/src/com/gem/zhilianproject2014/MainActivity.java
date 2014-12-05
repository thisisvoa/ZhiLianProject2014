package com.gem.zhilianproject2014;


import com.zhilian.click.AntiMadclick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new Handler().postDelayed(new jump(), 3000);
	}
	
	public class jump implements Runnable  {  
	    @Override  
	    public void run() {  
	    	Intent intent = new Intent(MainActivity.this, MapActivity.class);
			startActivity(intent);
	        finish();  
	    }  
	}  
}
