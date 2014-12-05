package com.gem.zhilianproject2014;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.amap.api.cloud.model.CloudItem;
import com.zhilian.tab.AbstractActivityGroup;


public class RecruiterDemandGroup extends AbstractActivityGroup {

	public static RecruiterDemandGroup RecruiterDemand_Group;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		ArrayList<CloudItem> cloud_items = getIntent().getParcelableArrayListExtra(MapActivity.PAR_KEY);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(MapActivity.PAR_KEY, cloud_items);
		
        viewHistory = new ArrayList<View>();
        RecruiterDemand_Group = this;
        
        if (cloud_items.size() == 1) {
        	View decorView = getLocalActivityManager()
        			.startActivity("RecruiterDemandDetail", new Intent(this, RecruiterDemandDetailActivity.class).putExtras(bundle))
        			.getDecorView();  
        	replaceContentView(decorView); 
        } else if (cloud_items.size() > 1) {
        	View decorView = getLocalActivityManager()
        			.startActivity("RecruiterDemand", new Intent(this, RecruiterDemandActivity.class).putExtras(bundle))
        			.getDecorView();  
        	replaceContentView(decorView); 
        }
	}
}
