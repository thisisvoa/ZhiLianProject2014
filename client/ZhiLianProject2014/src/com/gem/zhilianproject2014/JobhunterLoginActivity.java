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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhilian.auth.AuthApplication;
import com.zhilian.click.AntiMadclick;

public class JobhunterLoginActivity extends Activity 
	implements OnClickListener, OnCheckedChangeListener {
	
	private EditText login_edittext_phone;
	private EditText login_edittext_password;
	private Button login;
	private Button register;
	private CheckBox login_checkbox_remember;
	private boolean login_remember = false;
	private SharedPreferences auth_sharedpreference;
	
	private Handler handler;
	
//	public static final String SERVER_IP = "192.168.56.1";
	public static final String SERVER_IP = "192.168.0.140";
	public static final int REQUEST_TIMEOUT = 5*1000;  
    public static final int SO_TIMEOUT = 10*1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		auth_sharedpreference = JobhunterLoginActivity.this
				.getSharedPreferences("auth_sharedpreference", MODE_PRIVATE);
		
		login_edittext_phone = (EditText) findViewById(R.id.login_edittext_phone);
		login_edittext_password = (EditText) findViewById(R.id.login_edittext_password);
		login_edittext_password.setTypeface(Typeface.DEFAULT);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		login_checkbox_remember = (CheckBox) findViewById(R.id.login_checkbox_remember);
		
		login.setOnClickListener(JobhunterLoginActivity.this);
		register.setOnClickListener(JobhunterLoginActivity.this);
		login_checkbox_remember.setOnCheckedChangeListener(JobhunterLoginActivity.this);
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what) {
				case -1:
					Toast.makeText(JobhunterLoginActivity.this, "网络异常", Toast.LENGTH_LONG).show();
					break;
				case 0:
					if (msg.obj.equals("false")) {
						Toast.makeText(JobhunterLoginActivity.this, "手机号或密码错误", Toast.LENGTH_LONG).show();
					} else if (msg.obj.equals("true")) {
						if (login_remember == true) {
							Editor editor = auth_sharedpreference.edit();
							editor.putString("userphone", login_edittext_phone.getText().toString());
							editor.putString("userpassword", login_edittext_password.getText().toString());
							editor.commit();
						}
						
						AuthApplication auth = (AuthApplication)getApplication();
						auth.setUserphone(login_edittext_phone.getText().toString());
						auth.setUserpassword(login_edittext_password.getText().toString());
						
						Intent intent = new Intent(JobhunterLoginActivity.this, JobhunterTab.class);
						startActivity(intent);
						finish();
					}
					break;
				}
			}
		};
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (AntiMadclick.isFastDoubleClick() == true) {
			return;
		}
		
		int id = view.getId();
		
		switch(id) {
		case R.id.login:
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					login();
				}
				
			}).start();
			break;
		case R.id.register:
			Intent intent = new Intent(JobhunterLoginActivity.this, JobhunterRegisterActivity.class);
			startActivity(intent);
			break;
		}
	}
	

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		login_remember = arg1;
	}
	
	private void login() {
		String url = "http://"+SERVER_IP+":8080/ZhiLianProjectServer2014/Jobhunter/LoginServlet";  
        HttpPost request = new HttpPost(url);
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();  
		String phone = login_edittext_phone.getText().toString();
		String password = login_edittext_password.getText().toString();
		params.add(new BasicNameValuePair("userphone",phone));  
	    params.add(new BasicNameValuePair("userpassword",password));
	    
	    try {
	    	request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
	    	HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			
			Message msg = new Message();
			if(response.getStatusLine().getStatusCode() == 200) {
				msg.what = 0;
				String responseMsg = EntityUtils.toString(response.getEntity());
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
	
	public static final HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpClient client = new DefaultHttpClient(httpParams);
		return client;
	}

	
}
