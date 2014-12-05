package com.gem.zhilianproject2014;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.amap.api.cloud.model.AMapCloudException;
import com.amap.api.cloud.model.CloudItem;
import com.amap.api.cloud.model.CloudItemDetail;
import com.amap.api.cloud.model.LatLonPoint;
import com.amap.api.cloud.search.CloudResult;
import com.amap.api.cloud.search.CloudSearch;
import com.amap.api.cloud.search.CloudSearch.OnCloudSearchListener;
import com.amap.api.cloud.search.CloudSearch.SearchBound;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnCameraChangeListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.VisibleRegion;
import com.zhilian.auth.AuthApplication;
import com.zhilian.click.AntiMadclick;
import com.zhilian.map.AMapUtil;
import com.zhilian.map.CloudSearchFilterModel;
import com.zhilian.map.PoiOverlay;
import com.zhilian.map.ToastUtil;

public class MapActivity extends Activity 
implements OnClickListener, AMapLocationListener, 
LocationSource, OnCloudSearchListener, 
OnCameraChangeListener, OnMarkerClickListener {

	private MapView map_mapview;
	private AMap map_amap;

	private OnLocationChangedListener map_location_listener;
	private LocationManagerProxy map_location_manager;

	private ImageView map_imageview_markers;
	private ImageView map_imageview_jobhunter;

	private CloudSearch cloud_search;
	public static final String cloud_table_id = "545b5c66e4b0ffa891143223";
	private CloudSearch.Query cloud_query;
	private String cloud_keyword = "";
	private ArrayList<CloudItem> cloud_items = new ArrayList<CloudItem>();
	private List<Map.Entry<CloudItem, Double>> cloud_items_sort = new ArrayList<Map.Entry<CloudItem, Double>>();

	private PoiOverlay cloud_poi_overlay;

	public static final String log_tag = "zhilian";

	private AuthApplication auth;
	private SharedPreferences auth_sharedpreference;

	private String userdemand_industry = "IT";
	private String userdemand_field = "";
	private String userdemand_job = "";
	private String userdemand_salary = "0";
	private String userdemand_experience = "0";

	public static final String PAR_KEY = "map_markers";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map_mapview = (MapView) findViewById(R.id.map_mapview);
		map_mapview.onCreate(savedInstanceState);
		init();

	}

	private void init() {
		if(map_amap == null) {
			map_amap = map_mapview.getMap();
			setUpMap();
		}

		setUpUserInfo();

		map_imageview_markers = (ImageView) findViewById(R.id.map_imageview_markers);
		map_imageview_markers.setOnClickListener(MapActivity.this);

		map_imageview_jobhunter = (ImageView) findViewById(R.id.map_imageview_jobhunter);
		map_imageview_jobhunter.setOnClickListener(MapActivity.this);

		cloud_search = new CloudSearch(MapActivity.this);
		cloud_search.setOnCloudSearchListener(MapActivity.this);
	}

	private void setUpMap() {
		map_amap.setLocationSource(MapActivity.this);// 设置定位监听
		map_amap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		map_amap.getUiSettings().setRotateGesturesEnabled(false);// 设置地图是否允许通过手势来旋转
		map_amap.getUiSettings().setTiltGesturesEnabled(false);// 设置了地图是否允许通过手势来倾斜
		map_amap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
		map_amap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);
		map_amap.setMyLocationEnabled(true);
		map_amap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

		map_amap.setOnCameraChangeListener(this);// 添加移动地图事件监听器
		map_amap.setOnMarkerClickListener(MapActivity.this);
	}

	private void setUpUserInfo() {
		auth = (AuthApplication) getApplication();

		userdemand_field = auth.getUserdemand_field();
		userdemand_job = auth.getUserdemand_job();
		userdemand_salary = auth.getUserdemand_salary();
		userdemand_experience = auth.getUserdemand_experience();
		
		auth_sharedpreference = MapActivity.this
				.getSharedPreferences("auth_sharedpreference", MODE_PRIVATE);
		
		String resume_path = auth_sharedpreference.getString("resume_path", null);
		String resume_name = auth_sharedpreference.getString("resume_name", null);
		if (resume_path != null && resume_name != null) {
			auth.setResume_path(resume_path);
			auth.setResume_name(resume_name);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (AntiMadclick.isFastDoubleClick() == true) {
			return;
		}
		
		switch (view.getId()) {
		case R.id.map_imageview_markers:
			if (cloud_items.size() > 0) {
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList(PAR_KEY, cloud_items);// com.amap.api.cloud.model.CloudItem implements android.os.Parcelable
				Intent intent = new Intent(MapActivity.this, MapMarkersActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			break;

		case R.id.map_imageview_jobhunter:
			
			String userphone = auth_sharedpreference.getString("userphone", null);
			String userpassword = auth_sharedpreference.getString("userpassword", null);
			
			AuthApplication auth = (AuthApplication)getApplication();
			if (userphone != null && userpassword != null) {
				auth.setUserphone(userphone);
				auth.setUserpassword(userpassword);
				Intent intent = new Intent(MapActivity.this, JobhunterTab.class);
				startActivity(intent);
			} else if (auth.getUserphone() != null && auth.getUserpassword() != null) {
				Intent intent = new Intent(MapActivity.this, JobhunterTab.class);
				startActivity(intent);
			} else {
				Intent intent = new Intent(MapActivity.this, JobhunterLoginActivity.class);
				startActivity(intent);
			}
			break;
		}
	}

	public void searchByPolygon(SearchBound bound, CloudSearchFilterModel cloud_search_filter) {
		cloud_items.clear();
		try {
			cloud_query = new CloudSearch.Query(cloud_table_id, cloud_keyword, bound);
			cloudSearchFilter(cloud_search_filter);
			cloud_search.searchCloudAsyn(cloud_query);
		} catch (AMapCloudException e) {
			e.printStackTrace();
		}

	}

	private void cloudSearchFilter(CloudSearchFilterModel cloud_search_filter) {
		Map<String, String> map = cloud_search_filter.getFilterRules();
		Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
			cloud_query.addFilterString(entry.getKey(), entry.getValue());
			Log.d(log_tag,"entry.getKey(), entry.getValue() " + entry.getKey() + entry.getValue());
		}
		cloud_query.setSortingrules(cloud_search_filter.getSortingRules());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		map_mapview.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		map_mapview.onPause();
		deactivate();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		map_mapview.onResume();
		setUpUserInfo(); // 刷新用户信息
//		cloudSearchScreen();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		map_mapview.onDestroy();
	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation map_location) {
		// TODO Auto-generated method stub
		if (map_location_listener != null && map_location != null) {
			map_location_listener.onLocationChanged(map_location);
		}
	}

	/**
	 * 激活定位
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		map_location_listener = listener;
		if (map_location_manager == null) {
			map_location_manager = LocationManagerProxy.getInstance(MapActivity.this);
			//			map_location_manager.requestLocationData(LocationProviderProxy.AMapNetwork, 0, 0, MapActivity.this);
			map_location_manager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 2000, 10, MapActivity.this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		map_location_listener = null;
		if (map_location_manager != null) {
			map_location_manager.removeUpdates(MapActivity.this);
			map_location_manager.destroy();
		}
		map_location_manager = null;
	}

	@Override
	public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {

	}

	@Override
	public void onCloudSearched(CloudResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(cloud_query)) {
					cloud_items = result.getClouds();
					Log.d(log_tag,"total "+cloud_items.size()+" pois");
					cloudItemsFilter();
					
					if (cloud_poi_overlay != null) {
						cloud_poi_overlay.removeFromMap();
					}

					if (cloud_items != null && cloud_items.size() > 0) {
						//						map_amap.clear();
						cloud_poi_overlay = new PoiOverlay(map_amap, cloud_items);
						cloud_poi_overlay.removeFromMap();
						cloud_poi_overlay.addToMap();

						Log.d(log_tag,"showing "+cloud_items.size()+" pois");
					}
				}
			}
		} else {
//			ToastUtil.show(MapActivity.this, "网络异常");
		}

	}

	public void cloudItemsFilter() {
		String salary_min = "0";
		String salary_max = "0";
		String experience = "0";

		Iterator<CloudItem> iterator_cloud_items = cloud_items.iterator();
		while(iterator_cloud_items.hasNext()){
			HashMap<String, String> customField = iterator_cloud_items.next().getCustomfield();
			for (Entry<String, String> map : customField.entrySet()) {
				if(map.getKey().equals("salary_max")) {
					salary_max = map.getValue();
					break;
				}
			}
			if(Integer.parseInt(salary_max) < Integer.parseInt(userdemand_salary)){
				iterator_cloud_items.remove();// 移除cloud_items中salary_max小于jobhunter_salary的数据
			}
		}

		cloud_items_sort.clear();

		for(CloudItem item : cloud_items) {
			HashMap<String, String> customField = item.getCustomfield();
			for (Entry<String, String> map : customField.entrySet()) {
				if(map.getKey().equals("salary_min")) {
					salary_min = map.getValue();
					continue;
				}
				if(map.getKey().equals("salary_max")) {
					salary_max = map.getValue();
					continue;
				}
				if(map.getKey().equals("experience")) {
					experience = map.getValue();
					continue;
				}
			}

			if(Integer.parseInt(experience) == 0) {
				Map<CloudItem, Double> map= new HashMap<CloudItem, Double>();
				map.put(item, Double.parseDouble(salary_max));
				cloud_items_sort.add((Map.Entry<CloudItem, Double>)map.entrySet().iterator().next());
			} else {
				double weight = 0.5
						* (Double.parseDouble(userdemand_experience) / Double.parseDouble(experience))
						* (Double.parseDouble(salary_max) - Double.parseDouble(salary_min))
						+ Double.parseDouble(salary_min);
				Map<CloudItem, Double> map= new HashMap<CloudItem, Double>();
				map.put(item, weight);
				cloud_items_sort.add((Map.Entry<CloudItem, Double>)map.entrySet().iterator().next());
			}
		}

		Collections.sort(cloud_items_sort, new Comparator<Map.Entry<CloudItem, Double>>() {
			@Override
			public int compare(Entry<CloudItem, Double> arg0,
					Entry<CloudItem, Double> arg1) {
				// TODO Auto-generated method stub
				return arg1.getValue().compareTo(arg0.getValue());
			}
		});

		cloud_items.clear();
		Iterator<Map.Entry<CloudItem, Double>> iterator = cloud_items_sort.iterator();
		while(iterator.hasNext()) {
			Map.Entry<CloudItem, Double> entry = (Map.Entry<CloudItem, Double>)iterator.next();
			cloud_items.add(entry.getKey());
		}
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 对移动地图结束事件回调
	 */
	@Override
	public void onCameraChangeFinish(CameraPosition camera_position) {
		// TODO Auto-generated method stub
		/*if (cloud_poi_overlay != null) {
			cloud_poi_overlay.removeFromMap();
		}*/
		cloudSearchScreen();
	}

	public void cloudSearchScreen() {
		VisibleRegion visible_region = map_amap.getProjection().getVisibleRegion();// 获取可视区域

		LatLng latlng_nearleft = visible_region.nearLeft;
		LatLng latlng_farright = visible_region.farRight;

		LatLonPoint latlngpoint_nearleft = AMapUtil.convertToLatLonPoint(latlng_nearleft);
		LatLonPoint latlngpoint_farright = AMapUtil.convertToLatLonPoint(latlng_farright);
		Log.d(log_tag,latlngpoint_nearleft.toString());
		SearchBound bound = new SearchBound(latlngpoint_nearleft, latlngpoint_farright);

		CloudSearchFilterModel cloud_search_filter = new CloudSearchFilterModel();
		cloud_search_filter.addFilterRules("industry", userdemand_industry);
		if (userdemand_field.equals("") == false) {
			cloud_search_filter.addFilterRules("field", userdemand_field);
		} 
		if (userdemand_job.equals("") == false) {
			cloud_search_filter.addFilterRules("job", userdemand_job);
		}
		cloud_search_filter.setSortingRules("salary_max", false);// 根据salary_max降序
		searchByPolygon(bound, cloud_search_filter);
	}


	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		ArrayList<CloudItem> cloud_items_marker_click = new ArrayList<CloudItem>();
		String title = arg0.getTitle();
		boolean flag = false;
		for(CloudItem cloud_item : cloud_items) {
			if(title.equals(cloud_item.getTitle())) {
				flag = true;
				cloud_items_marker_click.add(cloud_item);
			}
		}
		if (flag == true) {
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(PAR_KEY, cloud_items_marker_click);// com.amap.api.cloud.model.CloudItem implements android.os.Parcelable
			Intent intent = new Intent(MapActivity.this, RecruiterTab.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}

		return true;// true表示 点击marker后 marker不会移动到地图中心
	}

}
