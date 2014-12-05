package com.gem.zhilianproject2014;

import java.util.ArrayList;

import com.amap.api.cloud.model.CloudItem;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class RecruiterTab extends TabActivity 
	implements OnCheckedChangeListener {
	
	private TabHost tabHost;
	private TabSpec tabSpec;
	private RadioGroup radiogroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recruiter_tab);
		
		setUpTabHost();
	}
	
	private void setUpTabHost() {
		ArrayList<CloudItem> cloud_items = getIntent().getParcelableArrayListExtra(MapActivity.PAR_KEY);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(MapActivity.PAR_KEY, cloud_items);
		
		tabHost = getTabHost();// 获取TabHost对象
		
		tabSpec = tabHost.newTabSpec("recruiter_demand");
		tabSpec.setIndicator("recruiter_demand");
		tabSpec.setContent(new Intent(this, RecruiterDemandGroup.class).putExtras(bundle));
		tabHost.addTab(tabSpec);
		
		tabSpec = tabHost.newTabSpec("recruiter_person");
		tabSpec.setIndicator("recruiter_person");
		tabSpec.setContent(new Intent(this, RecruiterPersonActivity.class).putExtras(bundle));
		tabHost.addTab(tabSpec);
		
		radiogroup = (RadioGroup) findViewById(R.id.recruiter_tab_radiogroup);
		radiogroup.setOnCheckedChangeListener(RecruiterTab.this);
		
		radiogroup.check(R.id.recruiter_tab_demand);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int id) {
		// TODO Auto-generated method stub
		switch(id) {
		case R.id.recruiter_tab_demand:
			tabHost.setCurrentTabByTag("recruiter_demand");
			break;
		case R.id.recruiter_tab_person:
			tabHost.setCurrentTabByTag("recruiter_person");
			break;
		}
	}
}
