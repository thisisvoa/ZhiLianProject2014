package com.gem.zhilianproject2014;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhilian.auth.AuthApplication;
import com.zhilian.mail.FileUtils;

public class JobhunterSettingsActivity extends Activity 
	implements OnCheckedChangeListener {
	
	private AuthApplication auth;
	private SharedPreferences auth_sharedpreference;
	private CheckBox jobhunter_settings_checkbox_remember;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobhunter_settings);
		
		auth = (AuthApplication) getApplication();
		
		auth_sharedpreference = JobhunterSettingsActivity.this
				.getSharedPreferences("auth_sharedpreference", MODE_PRIVATE);
		String userphone = auth_sharedpreference.getString("userphone", null);
		String userpassword = auth_sharedpreference.getString("userpassword", null);
		
		jobhunter_settings_checkbox_remember = 
				(CheckBox) findViewById(R.id.jobhunter_settings_checkbox_remember);
		if (userphone != null && userpassword != null) {
			jobhunter_settings_checkbox_remember.setChecked(true);
		} else {
			jobhunter_settings_checkbox_remember.setChecked(false);
		}
		jobhunter_settings_checkbox_remember.setOnCheckedChangeListener(JobhunterSettingsActivity.this);
		
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if (arg1 == true) {
			if (auth.getUserphone() != null && auth.getUserpassword() != null) {
				Editor editor = auth_sharedpreference.edit();
				editor.putString("userphone", auth.getUserphone());
				editor.putString("userpassword", auth.getUserpassword());
				editor.commit();
			}
		} else if (arg1 == false) {
			Editor editor = auth_sharedpreference.edit();
			editor.putString("userphone", null);
			editor.putString("userpassword", null);
			editor.commit();
		}
	}

}
