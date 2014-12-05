package com.gem.zhilianproject2014;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.amap.api.cloud.model.CloudItem;
import com.amap.api.cloud.model.CloudItemDetail;
import com.amap.api.cloud.search.CloudResult;
import com.amap.api.cloud.search.CloudSearch;
import com.amap.api.cloud.search.CloudSearch.OnCloudSearchListener;
import com.zhilian.auth.AuthApplication;
import com.zhilian.click.AntiMadclick;

public class JobhunterApplicationhistoryActivity extends Activity 
	implements OnItemClickListener, OnCloudSearchListener {
	
	private AuthApplication auth;
	private Handler handler;
	private ListView jobhunter_applicationhistory_listview;
	private ArrayList<HashMap<String, String>> arraylist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobhunter_applicationhistory);
		
		auth = (AuthApplication)getApplication();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getApplicationhistory();
			}
			
		}).start();
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what) {
				case -1:
					Toast.makeText(JobhunterApplicationhistoryActivity.this, "网络异常", Toast.LENGTH_LONG).show();
					break;
				case 0:
					arraylist = new ArrayList<HashMap<String, String>>();
					HashMap<String, String> hashmap = null;
					try {
						JSONArray jsonArray = new JSONArray(msg.obj.toString());
						JSONObject jsonObject = null;
						for (int i = jsonArray.length()-1; i >= 0 ; i--) {
							jsonObject = jsonArray.optJSONObject(i);
							hashmap = new HashMap<String, String>();
							hashmap.put("application_recruitername", jsonObject.optString("application_recruitername"));
							hashmap.put("id_cloud", jsonObject.optString("id_cloud"));
							hashmap.put("application_jobname", jsonObject.optString("application_jobname"));
							hashmap.put("application_salary", jsonObject.optString("application_salary"));
							hashmap.put("application_time_hour", jsonObject.optString("application_time_hour"));
							hashmap.put("application_time_day", jsonObject.optString("application_time_day"));
							arraylist.add(hashmap);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					SimpleAdapter adapter = 
							new SimpleAdapter(JobhunterApplicationhistoryActivity.this, arraylist, 
									R.layout.activity_jobhunter_applicationhistory_item, 
									new String[]{"application_recruitername", "application_jobname", "application_salary", "application_time_hour", "application_time_day"},
									new int[]{
										R.id.applicationhistory_recruiter,
										R.id.applicationhistory_jobname,
										R.id.applicationhistory_salary,
										R.id.applicationhistory_time_hour,
										R.id.applicationhistory_time_day});
					jobhunter_applicationhistory_listview = 
							(ListView) findViewById(R.id.jobhunter_applicationhistory_listview);
					jobhunter_applicationhistory_listview.setOnItemClickListener(JobhunterApplicationhistoryActivity.this);
					jobhunter_applicationhistory_listview.setAdapter(adapter);
					break;
				}
			}
		};
	}
	
	private void getApplicationhistory() {
		String url = "http://"+JobhunterLoginActivity.SERVER_IP+":8080/ZhiLianProjectServer2014/Jobhunter/ApplicationhistoryServlet";  
		HttpPost request = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		params.add(new BasicNameValuePair("userphone",auth.getUserphone()));

		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);

			Message msg = new Message();
			if(response.getStatusLine().getStatusCode() == 200) {
				msg.what = 0;
				String responseMsg = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				msg.obj = responseMsg;
			} else {
				msg.what = -1;
			}
			handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (AntiMadclick.isFastDoubleClick() == true) {
			return;
		}
		
		jobhunter_applicationhistory_listview.setEnabled(false);
		
	    String id_cloud = arraylist.get(position).get("id_cloud");
	    
	    CloudSearch cloud_search;
	    cloud_search = new CloudSearch(this);
	    cloud_search.setOnCloudSearchListener(this);
	    cloud_search.searchCloudDetailAsyn(MapActivity.cloud_table_id, id_cloud);
	}

	@Override
	public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
		// TODO Auto-generated method stub
		if (rCode == 0 && item != null) {
			ArrayList<CloudItem> cloud_items_click = new ArrayList<CloudItem>();
			cloud_items_click.add(item);
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(MapActivity.PAR_KEY, cloud_items_click);// com.amap.api.cloud.model.CloudItem implements android.os.Parcelable
			Intent intent = new Intent(JobhunterApplicationhistoryActivity.this, RecruiterTab.class);
			intent.putExtras(bundle);
			jobhunter_applicationhistory_listview.setEnabled(true);
			startActivity(intent);
		} else {
			jobhunter_applicationhistory_listview.setEnabled(true);
			Toast.makeText(JobhunterApplicationhistoryActivity.this, "查询失败", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onCloudSearched(CloudResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
