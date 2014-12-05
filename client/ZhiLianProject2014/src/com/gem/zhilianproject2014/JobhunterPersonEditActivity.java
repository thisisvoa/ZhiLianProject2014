package com.gem.zhilianproject2014;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zhilian.auth.AuthApplication;
import com.zhilian.click.AntiMadclick;

public class JobhunterPersonEditActivity extends Activity 
	implements OnItemSelectedListener, OnClickListener {
	
	private AuthApplication auth;
	
	private EditText jobhunter_person_edit_username;
	private EditText jobhunter_person_edit_userphone;
	private Spinner jobhunter_person_edit_userdemand_field;
	private Spinner jobhunter_person_edit_userdemand_job;
	private EditText jobhunter_person_edit_userdemand_salary;
	private EditText jobhunter_person_edit_userdemand_experience;
	
	private ImageView jobhunter_person_edit_check;
	
	private Handler handler;
	private JSONArray jsonArray;
	
	private ArrayAdapter<String> adapter_field;
	private ArrayAdapter<String> adapter_job;
	private List<String> list_field;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobhunter_person_edit);
		
		auth = (AuthApplication)getApplication();
		
		jobhunter_person_edit_username = (EditText) findViewById(R.id.jobhunter_person_edit_username);
		jobhunter_person_edit_userphone = (EditText) findViewById(R.id.jobhunter_person_edit_userphone);
		jobhunter_person_edit_userdemand_field = (Spinner) findViewById(R.id.jobhunter_person_edit_userdemand_field);
		jobhunter_person_edit_userdemand_job = (Spinner) findViewById(R.id.jobhunter_person_edit_userdemand_job);
		jobhunter_person_edit_userdemand_salary = (EditText) findViewById(R.id.jobhunter_person_edit_userdemand_salary);
		jobhunter_person_edit_userdemand_experience = (EditText) findViewById(R.id.jobhunter_person_edit_userdemand_experience);
		
		jobhunter_person_edit_username.setText(auth.getUsername());
		jobhunter_person_edit_userphone.setText(auth.getUserphone());
		jobhunter_person_edit_userdemand_salary.setText(auth.getUserdemand_salary());
		jobhunter_person_edit_userdemand_experience.setText(auth.getUserdemand_experience());
		
		jobhunter_person_edit_userphone.setEnabled(false);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getJob();
			}
			
		}).start();
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what) {
				case -1:
					Toast.makeText(JobhunterPersonEditActivity.this, "网络异常", Toast.LENGTH_LONG).show();
					break;
					
				case 0:
					list_field = new ArrayList<String>();
					try {
						jsonArray = new JSONArray(msg.obj.toString());
						JSONObject jsonObject = null;
						for (int i = 0; i < jsonArray.length(); i++) {
							jsonObject = jsonArray.optJSONObject(i);
							String field = jsonObject.optString("field");
							boolean isRepeat = false;
							for (String temp: list_field) {
								if (temp.equals(field)) {
									isRepeat = true;
									break;
								}
							}
							if (isRepeat == false) {
								list_field.add(field);
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					adapter_field = new ArrayAdapter<String>(
							JobhunterPersonEditActivity.this, 
							android.R.layout.simple_spinner_item, 
							list_field);
					adapter_field.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					jobhunter_person_edit_userdemand_field.setAdapter(adapter_field);
					jobhunter_person_edit_userdemand_field.setOnItemSelectedListener(JobhunterPersonEditActivity.this);
					
					int position_field = adapter_field.getPosition(auth.getUserdemand_field());
					adapter_field.notifyDataSetChanged();
					jobhunter_person_edit_userdemand_field.setSelection(position_field, true); // 设置默认值
					setUserdemandJob(position_field);
					break;
				
				case 1:
					if (msg.obj.equals("false")) {
						Toast.makeText(JobhunterPersonEditActivity.this, "个人信息修改失败", Toast.LENGTH_LONG).show();
					} else if (msg.obj.equals("true")) {
						Toast.makeText(JobhunterPersonEditActivity.this, "个人信息修改成功", Toast.LENGTH_LONG).show();
						finish();
					}
					break;
				}
			}
		};
		
		jobhunter_person_edit_check = (ImageView) findViewById(R.id.jobhunter_person_edit_check);
		jobhunter_person_edit_check.setOnClickListener(JobhunterPersonEditActivity.this);
	}
	
	private void getJob() {
		String url = "http://"+JobhunterLoginActivity.SERVER_IP+":8080/ZhiLianProjectServer2014/JobServlet";  
		HttpPost request = new HttpPost(url);

		try {
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
	public void onItemSelected(AdapterView<?> parent, View view, 
	        int pos, long id) {
		// TODO Auto-generated method stub
		switch (parent.getId()) {
		case R.id.jobhunter_person_edit_userdemand_field:
			if (pos == 0) {
				jobhunter_person_edit_userdemand_experience.setText("0");
				jobhunter_person_edit_userdemand_experience.setEnabled(false);
			} else {
				jobhunter_person_edit_userdemand_experience.setEnabled(true);
			}
			setUserdemandJob(pos);
			break;
		}
	}
	
	private void setUserdemandJob(int pos) {
		List<String> list_job = new ArrayList<String>();
		JSONObject jsonObject = null;
		for (int i = 0; i < jsonArray.length(); i++) {
			jsonObject = jsonArray.optJSONObject(i);
			if (list_field.get(pos).equals(jsonObject.opt("field"))){
				String job = jsonObject.optString("job");
				list_job.add(job);
			}
		}
		adapter_job = new ArrayAdapter<String>(JobhunterPersonEditActivity.this, 
				android.R.layout.simple_spinner_item, list_job);
		adapter_job.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		jobhunter_person_edit_userdemand_job.setAdapter(adapter_job);
//		jobhunter_person_edit_userdemand_job.setOnItemSelectedListener(JobhunterPersonEditActivity.this);
		
		int position_job = adapter_job.getPosition(auth.getUserdemand_job());
		jobhunter_person_edit_userdemand_job.setSelection(position_job, true);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		if (AntiMadclick.isFastDoubleClick() == true) {
			return;
		}
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				editPerson();
			}
			
		}).start();
	}
	
	private void editPerson() {
		String url = "http://"+JobhunterLoginActivity.SERVER_IP+":8080/ZhiLianProjectServer2014/Jobhunter/PersonEditServlet";  
		HttpPost request = new HttpPost(url);
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("username", jobhunter_person_edit_username.getText().toString());
			jsonObject.put("userphone", auth.getUserphone());
			jsonObject.put("userdemand_field", jobhunter_person_edit_userdemand_field.getSelectedItem().toString());
			jsonObject.put("userdemand_job", jobhunter_person_edit_userdemand_job.getSelectedItem().toString());
			jsonObject.put("userdemand_salary", jobhunter_person_edit_userdemand_salary.getText().toString());
			jsonObject.put("userdemand_experience", jobhunter_person_edit_userdemand_experience.getText().toString());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			request.setEntity(new StringEntity(jsonObject.toString(), HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);

			Message msg = new Message();
			if(response.getStatusLine().getStatusCode() == 200) {
				msg.what = 1;
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
