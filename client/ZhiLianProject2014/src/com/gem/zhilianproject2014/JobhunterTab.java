package com.gem.zhilianproject2014;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class JobhunterTab extends TabActivity 
	implements OnCheckedChangeListener {
	
	private TabHost tabhost;
	private TabSpec tabspec;
	private RadioGroup radiogroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobhunter_tab);
		
		setUpTabHost();
	}
	
	private void setUpTabHost() {
		tabhost = this.getTabHost();
		
		tabspec = tabhost.newTabSpec("jobhunter_person");
		tabspec.setIndicator("jobhunter_person");
		tabspec.setContent(new Intent(JobhunterTab.this, JobhunterPersonActivity.class));
		tabhost.addTab(tabspec);
		
		tabspec = tabhost.newTabSpec("jobhunter_applicationhistory");
		tabspec.setIndicator("jobhunter_applicationhistory");
		tabspec.setContent(new Intent(JobhunterTab.this, JobhunterApplicationhistoryActivity.class));
		tabhost.addTab(tabspec);
		
		tabspec = tabhost.newTabSpec("jobhunter_settings");
		tabspec.setIndicator("jobhunter_settings");
		tabspec.setContent(new Intent(JobhunterTab.this, JobhunterSettingsActivity.class));
		tabhost.addTab(tabspec);
		
		radiogroup = (RadioGroup) findViewById(R.id.jobhunter_tab_radiogroup);
		radiogroup.setOnCheckedChangeListener(JobhunterTab.this);
		
		radiogroup.check(R.id.jobhunter_tab_person);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int id) {
		// TODO Auto-generated method stub
		switch(id) {
		case R.id.jobhunter_tab_person:
			tabhost.setCurrentTabByTag("jobhunter_person");
			break;
		case R.id.jobhunter_tab_applicationhistory:
			tabhost.setCurrentTabByTag("jobhunter_applicationhistory");
			break;
		case R.id.jobhunter_tab_settings:
			tabhost.setCurrentTabByTag("jobhunter_settings");
			break;	
		}
	}

	// http://blog.csdn.net/cstarbl/article/details/7207256
}
