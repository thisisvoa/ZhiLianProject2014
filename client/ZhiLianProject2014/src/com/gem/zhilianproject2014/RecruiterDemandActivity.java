package com.gem.zhilianproject2014;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.amap.api.cloud.model.CloudItem;
import com.zhilian.click.AntiMadclick;

public class RecruiterDemandActivity extends Activity implements OnItemClickListener {

	private ArrayList<CloudItem> cloud_items;
	private ListView listview;
	private List<Map<String, String>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recruiter_demand);

		cloud_items = getIntent().getParcelableArrayListExtra(MapActivity.PAR_KEY);

		list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		String id_cloud = null;
		String recruiter_name = null;
		String recruiter_demand_salary_min = null;
		String recruiter_demand_salary_max = null;
		String recruiter_demand_jobname = null;
		for(CloudItem cloud_item : cloud_items) {
			map = new HashMap<String, String>();
			
			id_cloud = cloud_item.getID();
			recruiter_name =  cloud_item.getTitle();
			HashMap<String, String> customField = cloud_item.getCustomfield();
			for (Entry<String, String> temp : customField.entrySet()) {
				if(temp.getKey().equals("salary_min")){
					recruiter_demand_salary_min = temp.getValue();
					continue;
				}
				if(temp.getKey().equals("salary_max")){
					recruiter_demand_salary_max = temp.getValue();
					continue;
				}
				if(temp.getKey().equals("jobname")){
					recruiter_demand_jobname = temp.getValue();
					continue;
				}
			}
			map.put("item_job", recruiter_demand_jobname);
			map.put("item_salary", recruiter_demand_salary_min+"~"+recruiter_demand_salary_max);
			map.put("item_recruiter", recruiter_name);
			map.put("id_cloud", id_cloud);

			list.add(map);
		}

		SimpleAdapter adapter = 
				new SimpleAdapter(RecruiterDemandActivity.this, list, 
						R.layout.activity_recruiter_demand_item, 
						new String[]{"item_job", "item_salary", "item_recruiter"},
						new int[]{
							R.id.item_job,
							R.id.item_salary,
							R.id.item_recruiter});
		listview = (ListView) findViewById(R.id.recruiter_demand_listview);
		listview.setOnItemClickListener(RecruiterDemandActivity.this);
		listview.setAdapter(adapter);
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (AntiMadclick.isFastDoubleClick() == true) {
			return;
		}
		
		String id_cloud_click = (String) list.get(position).get("id_cloud");
		ArrayList<CloudItem> cloud_item_click = new ArrayList<CloudItem>();
		for(CloudItem cloud_item : cloud_items) {
			if(id_cloud_click.equals(cloud_item.getID())) {
				cloud_item_click.add(cloud_item);
				break;
			}
		}
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList(MapActivity.PAR_KEY, cloud_item_click);// com.amap.api.cloud.model.CloudItem implements android.os.Parcelable
		Intent intent = new Intent(RecruiterDemandActivity.this, RecruiterDemandDetailActivity.class);
		intent.putExtras(bundle);
		
		RecruiterDemandGroup.RecruiterDemand_Group
		.getLocalActivityManager().destroyActivity("RecruiterDemandDetail", true);
		
		View decorView = RecruiterDemandGroup.RecruiterDemand_Group
				.getLocalActivityManager().startActivity("RecruiterDemandDetail", intent).getDecorView();  
		RecruiterDemandGroup.RecruiterDemand_Group.replaceContentView(decorView);
	}
}
