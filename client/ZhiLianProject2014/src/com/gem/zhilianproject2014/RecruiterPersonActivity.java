package com.gem.zhilianproject2014;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.cloud.model.CloudItem;


public class RecruiterPersonActivity extends Activity {

	private ArrayList<CloudItem> cloud_items;
	private TextView recruiter_name;
	private TextView recruiter_address;
	private TextView recruiter_email;
	private TextView recruiter_detail;
	
	private String recruitername = null;
	
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recruiter_person);
		
		cloud_items = getIntent().getParcelableArrayListExtra(MapActivity.PAR_KEY);
		
		recruiter_name = (TextView) findViewById(R.id.recruiter_name);
		recruiter_address = (TextView) findViewById(R.id.recruiter_address);
		recruiter_email = (TextView) findViewById(R.id.recruiter_email);
		recruiter_detail = (TextView) findViewById(R.id.recruiter_detail);
		
		for(CloudItem cloud_item : cloud_items) {
			recruitername =  cloud_item.getTitle();
			recruiter_name.setText(recruitername);
			recruiter_address.setText(cloud_item.getSnippet());
			
			break;
		}
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getRecruiterDetail();
			}
			
		}).start();
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what) {
				case -1:
					Toast.makeText(RecruiterPersonActivity.this, 
							"网络异常", Toast.LENGTH_LONG).show();
					break;
				case 0:
					try {
						JSONObject jsonObject = new JSONObject(msg.obj.toString());
						recruiter_email.setText(jsonObject.getString("email"));
						recruiter_detail.setText(jsonObject.getString("detail"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		};
	}
	
	private void getRecruiterDetail() {
		String url = "http://"+JobhunterLoginActivity.SERVER_IP+":8080/ZhiLianProjectServer2014/Recruiter/DetailServlet";  
		HttpPost request = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		params.add(new BasicNameValuePair("recruitername", recruitername));

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
}
