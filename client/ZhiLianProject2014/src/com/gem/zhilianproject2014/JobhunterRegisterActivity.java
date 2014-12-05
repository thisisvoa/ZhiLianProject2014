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

import com.zhilian.click.AntiMadclick;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class JobhunterRegisterActivity extends Activity implements OnClickListener {

	private EditText register_edittext_phone;
	private EditText register_edittext_smscode;
	private EditText register_edittext_password;
	private EditText register_edittext_password_retype;
	private Button smscode_send;
	private Button register;

	private String userphone;
	private String smscode;
	private String userpassword;
	private String userpassword_retype;

	private EventHandler handler_sms_send;
	private EventHandler handler_sms_auth;
	private int status_sms_auth = -1;
	private Handler handler;

	private final Object lock = new Object();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SMSSDK.initSDK(JobhunterRegisterActivity.this, "405f63fa0fc5", "03a665a987978cfc65c245e4e1aab59d");
		setContentView(R.layout.activity_register);

		register_edittext_phone = (EditText) findViewById(R.id.register_edittext_phone);
		register_edittext_smscode = (EditText) findViewById(R.id.register_edittext_smscode);
		register_edittext_password = (EditText) findViewById(R.id.register_edittext_password);
		register_edittext_password.setTypeface(Typeface.DEFAULT);
		register_edittext_password_retype = (EditText) findViewById(R.id.register_edittext_password_retype);
		register_edittext_password_retype.setTypeface(Typeface.DEFAULT);
		smscode_send = (Button) findViewById(R.id.smscode_send);
		register = (Button) findViewById(R.id.register);
		smscode_send.setOnClickListener(JobhunterRegisterActivity.this);
		register.setOnClickListener(JobhunterRegisterActivity.this);

		handler_sms_send = new EventHandler(){
			@Override
			public void afterEvent(int event, int result, Object data) {
				// TODO Auto-generated method stub
				if(event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					afterSubmitSendSMS(result, data);
				}
			}
		};
		SMSSDK.registerEventHandler(handler_sms_send);

		handler_sms_auth = new EventHandler(){
			@Override
			public void afterEvent(int event, int result, Object data) {
				// TODO Auto-generated method stub
				if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					afterSubmitAuthSMS(result, data);
				}
			}
		};
		SMSSDK.registerEventHandler(handler_sms_auth);

		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what) {
				case -1:
					Toast.makeText(JobhunterRegisterActivity.this, "网络异常", Toast.LENGTH_LONG).show();
					break;
				case 0:
					if (msg.obj.equals("true")) {
						Toast.makeText(JobhunterRegisterActivity.this, "注册成功", Toast.LENGTH_LONG).show();
						finish();
					} else {
						Toast.makeText(JobhunterRegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
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
		
		userphone = register_edittext_phone.getText().toString();
		smscode = register_edittext_smscode.getText().toString();
		int id = view.getId();
		switch(id) {
		case R.id.smscode_send:
			SMSSDK.getVerificationCode("86", userphone);
			break;
		case R.id.register:
			SMSSDK.submitVerificationCode("86", userphone, smscode);
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					register();
				}
			}).start();
			break;
		}
	}



	private void afterSubmitSendSMS(final int result, final Object data) {
		JobhunterRegisterActivity.this.runOnUiThread(
				new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(result == SMSSDK.RESULT_COMPLETE) {
							Toast.makeText(JobhunterRegisterActivity.this, "已发送验证短信 请注意查收", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(JobhunterRegisterActivity.this, "验证短信发送失败", Toast.LENGTH_LONG).show();
						}
					}

				});
	}

	private void afterSubmitAuthSMS(final int result, final Object data) {
		JobhunterRegisterActivity.this.runOnUiThread(
				new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(result == SMSSDK.RESULT_COMPLETE) {
//							Toast.makeText(JobhunterRegisterActivity.this, "短信验证成功", Toast.LENGTH_LONG).show();
							status_sms_auth = 1;
							synchronized(lock){lock.notify();}
						} else {
							Toast.makeText(JobhunterRegisterActivity.this, "验证码错误 请重新发送验证短信", Toast.LENGTH_LONG).show();
							status_sms_auth = 0;
						}
					}

				});
	}

	private void register(){
		try{
			synchronized(lock){lock.wait();}
		}catch(InterruptedException x){}
		
		if(status_sms_auth == 1) {
			userpassword = register_edittext_password.getText().toString();
			userpassword_retype = register_edittext_password_retype.getText().toString();
			if (userpassword.equals(userpassword_retype)) {
				String url = "http://"+JobhunterLoginActivity.SERVER_IP+":8080/ZhiLianProjectServer2014/Jobhunter/RegisterServlet";  
				HttpPost request = new HttpPost(url);

				List<NameValuePair> params = new ArrayList<NameValuePair>();  
				params.add(new BasicNameValuePair("userphone",userphone));  
				params.add(new BasicNameValuePair("userpassword",userpassword));  

				try {
					request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpClient client = new DefaultHttpClient();
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
			} else {
				register_edittext_password_retype.setText("");
				Toast.makeText(JobhunterRegisterActivity.this, "确认密码错误", Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SMSSDK.unregisterEventHandler(handler_sms_send);
		SMSSDK.unregisterEventHandler(handler_sms_auth);
		super.onDestroy();
	}
}
