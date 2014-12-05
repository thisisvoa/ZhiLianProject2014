package com.gem.zhilianproject2014;

import java.util.ArrayList;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhilian.auth.AuthApplication;
import com.zhilian.click.AntiMadclick;
import com.zhilian.mail.FileUtils;

public class JobhunterPersonActivity extends Activity implements OnClickListener {
	private AuthApplication auth;
	private SharedPreferences auth_sharedpreference;
	
	private TextView jobhunter_person_textview_username;
	private TextView jobhunter_person_textview_userphone;
	private TextView jobhunter_person_textview_userdemand_field;
	private TextView jobhunter_person_textview_userdemand_job;
	private TextView jobhunter_person_textview_userdemand_salary;
	private TextView jobhunter_person_textview_userdemand_experience;
	private TextView jobhunter_person_textview_resume;
	private RelativeLayout person_resume;
	
	private ImageView jobhunter_person_imageview_person_edit;
	
	private Handler handler;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobhunter_person);
		
		auth = (AuthApplication)getApplication();
		auth_sharedpreference = JobhunterPersonActivity.this
				.getSharedPreferences("auth_sharedpreference", MODE_PRIVATE);
		String resume_path = auth_sharedpreference.getString("resume_path", null);
		String resume_name = auth_sharedpreference.getString("resume_name", null);
		
		jobhunter_person_textview_username = (TextView) findViewById(R.id.jobhunter_person_textview_username);
		jobhunter_person_textview_userphone = (TextView) findViewById(R.id.jobhunter_person_textview_userphone);
		jobhunter_person_textview_userdemand_field = (TextView) findViewById(R.id.jobhunter_person_textview_userdemand_field);
		jobhunter_person_textview_userdemand_job = (TextView) findViewById(R.id.jobhunter_person_textview_userdemand_job);
		jobhunter_person_textview_userdemand_salary = (TextView) findViewById(R.id.jobhunter_person_textview_userdemand_salary);
		jobhunter_person_textview_userdemand_experience = (TextView) findViewById(R.id.jobhunter_person_textview_userdemand_experience);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getPerson();
			}
			
		}).start();
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what) {
				case -1:
					Toast.makeText(JobhunterPersonActivity.this, "网络异常", Toast.LENGTH_LONG).show();
					break;
				case 0:
					try {
						JSONObject jsonObject = new JSONObject(msg.obj.toString());
						jobhunter_person_textview_username.setText(jsonObject.getString("username"));
						jobhunter_person_textview_userphone.setText(auth.getUserphone());
						
						jobhunter_person_textview_userdemand_field.setText(jsonObject.getString("userdemand_field"));
						jobhunter_person_textview_userdemand_job.setText(jsonObject.getString("userdemand_job"));
						jobhunter_person_textview_userdemand_salary.setText(jsonObject.getString("userdemand_salary"));
						jobhunter_person_textview_userdemand_experience.setText(jsonObject.getString("userdemand_experience") + "年");
						
						auth.setUsername(jsonObject.getString("username"));
						auth.setUserdemand_field(jsonObject.getString("userdemand_field"));
						auth.setUserdemand_job(jsonObject.getString("userdemand_job"));
						auth.setUserdemand_salary(jsonObject.getString("userdemand_salary"));
						auth.setUserdemand_experience(jsonObject.getString("userdemand_experience"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		};
		
		jobhunter_person_imageview_person_edit = (ImageView) findViewById(R.id.jobhunter_person_imageview_person_edit);
		jobhunter_person_imageview_person_edit.setOnClickListener(JobhunterPersonActivity.this);
		
		jobhunter_person_textview_resume = (TextView) findViewById(R.id.jobhunter_person_textview_resume);
		if (resume_name != null) {
			jobhunter_person_textview_resume.setText(resume_name);
		}
		person_resume = (RelativeLayout) findViewById(R.id.person_resume);
		person_resume.setOnClickListener(JobhunterPersonActivity.this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (AntiMadclick.isFastDoubleClick() == true) {
			return;
		}
		
		int id = view.getId();
		switch(id) {
		case R.id.jobhunter_person_imageview_person_edit:
			Intent intent = new Intent(JobhunterPersonActivity.this, JobhunterPersonEditActivity.class);
			startActivity(intent);
			break;
		case R.id.person_resume:
			showFileChooser();
			break;
		}
	}
	
	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		
		try {
			startActivityForResult(Intent.createChooser(intent, "选择简历"), 0);
		} catch (android.content.ActivityNotFoundException e) {
			Toast.makeText(JobhunterPersonActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
		}
		
		// http://blog.csdn.net/zqchn/article/details/8770913
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			switch(requestCode) {
			case 0:
				Uri uri = data.getData();
				String url = FileUtils.getPath(JobhunterPersonActivity.this, uri);
				String fileName = url.substring(url.lastIndexOf("/") + 1);
				jobhunter_person_textview_resume.setText(fileName);
				
				auth.setResume_path(url);
				auth.setResume_name(fileName);
				
				Editor editor = auth_sharedpreference.edit();
				editor.putString("resume_path", url);
				editor.putString("resume_name", fileName);
				editor.commit();
				
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getPerson();
			}
			
		}).start();
	}
	
	private void getPerson() {
		String url = "http://"+JobhunterLoginActivity.SERVER_IP+":8080/ZhiLianProjectServer2014/Jobhunter/PersonServlet";  
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
				String responseMsg = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
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
