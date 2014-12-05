package com.gem.zhilianproject2014;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amap.api.cloud.model.CloudItem;
import com.zhilian.click.AntiMadclick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MapMarkersActivity extends Activity implements OnItemClickListener {
	private ArrayList<CloudItem> cloud_items = new ArrayList<CloudItem>();
	private ListView listview;
	private List<Map<String, Object>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_markers);

		cloud_items = getIntent().getParcelableArrayListExtra(MapActivity.PAR_KEY);

		list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		String id_cloud = null;
		String recruiter_name = null;
		String recruiter_demand_salary_min = null;
		String recruiter_demand_salary_max = null;
		String recruiter_demand_jobname = null;
		int i = 0;
		for(CloudItem cloud_item : cloud_items) {
			map = new HashMap<String, Object>();
			i = i + 1;
			switch(i) {
			case 1:
				map.put("item_imageview_color", R.drawable.map_marker_red);
				break;
			case 2:
				map.put("item_imageview_color", R.drawable.map_marker_orange);
				break;
			case 3:
				map.put("item_imageview_color", R.drawable.map_marker_yellow);
				break;
			default:
				map.put("item_imageview_color", R.drawable.map_marker_grey);
				break;
			}
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
				new SimpleAdapter(MapMarkersActivity.this, list, 
						R.layout.activity_map_markers_item, 
						new String[]{"item_imageview_color", "item_job", "item_salary", "item_recruiter"},
						new int[]{
							R.id.item_imageview_color, 
							R.id.item_job,
							R.id.item_salary,
							R.id.item_recruiter});
		listview = (ListView) findViewById(R.id.map_markers_listview);
		listview.setOnItemClickListener(MapMarkersActivity.this);
		listview.setAdapter(adapter);
	}


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
		Intent intent = new Intent(MapMarkersActivity.this, RecruiterTab.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

}