package com.gem.zhilianproject2014;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.cloud.model.CloudItem;
import com.zhilian.auth.AuthApplication;
import com.zhilian.click.AntiMadclick;
import com.zhilian.mail.SendMail;

public class RecruiterDemandDetailActivity extends Activity implements OnClickListener {

	private TextView item_jobname = null;
	private TextView item_field = null;
	private TextView item_job = null;
	private TextView item_salary = null;
	private TextView item_experience = null;
	private TextView item_detail = null;
	private ImageView recruiter_demanddetail_imagebutton_sendmail = null;

	private ArrayList<CloudItem> cloud_items;

	private Handler send_mail_handler;

	private AuthApplication auth;

	private String id_cloud = null;
	private String recruitername = null;
	private String demand_field = null;
	private String demand_job = null;
	private String demand_jobname = null;
	private String demand_salary_min = null;
	private String demand_salary_max = null;
	private String demand_experience = null;

	private String to = "";
	private String subject = null;
	private String body = null;

	boolean flag_sendmail = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recruiter_demanddetail);

		cloud_items = getIntent().getParcelableArrayListExtra(MapActivity.PAR_KEY);

		auth = (AuthApplication) getApplication();

		for(CloudItem cloud_item : cloud_items) {
			id_cloud = cloud_item.getID();
			recruitername = cloud_item.getTitle();
			HashMap<String, String> customField = cloud_item.getCustomfield();
			for (Entry<String, String> map : customField.entrySet()) {
				if(map.getKey().equals("field")){
					demand_field = map.getValue();
					continue;
				}
				if(map.getKey().equals("job")){
					demand_job = map.getValue();
					continue;
				}
				if(map.getKey().equals("jobname")){
					demand_jobname = map.getValue();
					continue;
				}
				if(map.getKey().equals("salary_min")){
					demand_salary_min = map.getValue();
					continue;
				}
				if(map.getKey().equals("salary_max")){
					demand_salary_max = map.getValue();
					continue;
				}
				if(map.getKey().equals("experience")){
					demand_experience = map.getValue();
				}
			}

			break;
		}
		
		item_jobname = (TextView) findViewById(R.id.item_jobname);
		item_field = (TextView) findViewById(R.id.item_field);
		item_job = (TextView) findViewById(R.id.item_job);
		item_salary = (TextView) findViewById(R.id.item_salary);
		item_experience = (TextView) findViewById(R.id.item_experience);
		item_detail = (TextView) findViewById(R.id.item_detail);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getJobDetail();
			}
			
		}).start();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getRecruiterEmail();
			}
			
		}).start();
		
		item_jobname.setText(demand_jobname);
		item_field.setText(demand_field);
		item_job.setText(demand_job);
		item_salary.setText(demand_salary_min+"~" + demand_salary_max);
		item_experience.setText(demand_experience + "年");

		recruiter_demanddetail_imagebutton_sendmail = 
				(ImageView) findViewById(R.id.recruiter_demanddetail_imageview_sendmail);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getApplicationhistory();
			}
			
		}).start();
		recruiter_demanddetail_imagebutton_sendmail.setOnClickListener(RecruiterDemandDetailActivity.this);
		recruiter_demanddetail_imagebutton_sendmail.setImageResource(R.drawable.recruiter_sendmail_unclickable);


		send_mail_handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch(msg.what) {
				case -1:
					Toast.makeText(RecruiterDemandDetailActivity.this, 
							"网络异常", Toast.LENGTH_LONG).show();
					break;
				case 0:
					if (msg.obj.equals(true)) {
						Toast.makeText(RecruiterDemandDetailActivity.this, 
								"简历投递成功", Toast.LENGTH_LONG).show();
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								addApplicationhistory();
							}
							
						}).start();
					} else if (msg.obj.equals(false)) {
						Toast.makeText(RecruiterDemandDetailActivity.this, 
								"网络异常", Toast.LENGTH_LONG).show();
					}
				case 1:
					flag_sendmail = true;
					JSONArray jsonArray;
					try {
						jsonArray = new JSONArray(msg.obj.toString());
						JSONObject jsonObject = null;
						for (int i = 0; i < jsonArray.length(); i++) {
							jsonObject = jsonArray.optJSONObject(i);
							if (id_cloud.equals(jsonObject.optString("id_cloud"))) {
								flag_sendmail = false;
								break;
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (flag_sendmail == true) {
						recruiter_demanddetail_imagebutton_sendmail.setImageResource(R.drawable.selecter_recruiter_sendmail);
					}
					break;
				case 2:
					item_detail.setText(msg.obj.toString());
					break;
				case 3:
					to = msg.obj.toString();
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
		switch (id) {
		case R.id.recruiter_demanddetail_imageview_sendmail:
			if (flag_sendmail == false) {
				Toast.makeText(RecruiterDemandDetailActivity.this, "无法重复投递", Toast.LENGTH_SHORT).show();
			} else {
				if (auth.getUserphone() == null) {
					Toast.makeText(RecruiterDemandDetailActivity.this, "无法投递 请先登录", Toast.LENGTH_SHORT).show();
				} else if (auth.getUsername() == "") {
					Toast.makeText(RecruiterDemandDetailActivity.this, "无法投递 请先完善个人信息", Toast.LENGTH_SHORT).show();
				} else if (auth.getResume_path() == null || auth.getResume_name() == null){
					Toast.makeText(RecruiterDemandDetailActivity.this, "无法投递 请先设置简历文件路径", Toast.LENGTH_SHORT).show();
				} else {
					recruiter_demanddetail_imagebutton_sendmail.setImageResource(R.drawable.recruiter_sendmail_unclickable);
					flag_sendmail = false;
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							sendMail();
						}
					}).start();
				}
			}
			break;
		}
	}

	private void sendMail() {
		//		to = 
		subject = "【求职】" + demand_jobname + "-" + auth.getUsername();
		body = "<p align=\"center\">"
				+ "<strong><span style=\"font-size:16px;\">"+ auth.getUsername()+"</span></strong>"
				+ "</p>"
				+ "<strong><span style=\"font-size:16px;\"></span></strong>"
				+ "<hr />"
				+ "<div align=\"center\">"
				+ "<p>"
				+ "<strong></strong>"
				+ "</p>"
				+ "<p>"
				+ "<span style=\"font-size:14px;\">意向职位</span><strong><span style=\"font-size:14px;\">&nbsp; " + demand_jobname + "</span></strong>"
				+ "</p>"
				+ "</div>"
				+ "<p align=\"center\">"
				+ "<span style=\"font-size:14px;\">期望月薪&nbsp; </span><strong><span style=\"font-size:14px;\"> " + auth.getUserdemand_salary() + " </span></strong><span style=\"font-size:14px;\">元</span>"
				+ "</p>";
		int userdemand_experience = Integer.parseInt(auth.getUserdemand_experience());
		if (userdemand_experience > 0) {
			subject = subject + "-" + auth.getUserdemand_experience() +"年经验";
			body = body
					+ "<p align=\"center\">"
					+ "<span style=\"font-size:14px;\">相关经验</span><strong><span style=\"font-size:14px;\">&nbsp; " + auth.getUserdemand_experience() + "</span></strong><span style=\"font-size:14px;\">年</span>"
					+ "</p>";
		}
		body = body
				+ "<p align=\"center\">"
				+ "<span style=\"font-size:14px;\">联系手机</span><strong><span style=\"font-size:14px;\">&nbsp; " + auth.getUserphone() + "</span></strong>"
				+ "</p>"
				+ "<hr />"
				+ "<p align=\"center\">"
				+ "<span style=\"font-size:12px;\">详细简历见附件</span>"
				+ "</p>";

		SendMail send_mail = new SendMail(to, subject, body);
		Message msg = new Message();
		msg.what = 0;
		try {
			send_mail.addAttachment(auth.getResume_path());
			msg.obj = send_mail.send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send_mail_handler.sendMessage(msg);
	}
	
	private void getRecruiterEmail() {
		String url = "http://"+JobhunterLoginActivity.SERVER_IP+":8080/ZhiLianProjectServer2014/Recruiter/EmailServlet";  
		HttpPost request = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		params.add(new BasicNameValuePair("recruitername", recruitername));

		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);

			Message msg = new Message();
			if(response.getStatusLine().getStatusCode() == 200) {
				msg.what = 3;
				String responseMsg = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				msg.obj = responseMsg;
			} else {
				msg.what = -1;
			}
			send_mail_handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getJobDetail() {
		String url = "http://"+JobhunterLoginActivity.SERVER_IP+":8080/ZhiLianProjectServer2014/Recruiter/JobDetailServlet";  
		HttpPost request = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();  
		params.add(new BasicNameValuePair("id_cloud", id_cloud));

		try {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);

			Message msg = new Message();
			if(response.getStatusLine().getStatusCode() == 200) {
				msg.what = 2;
				String responseMsg = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				msg.obj = responseMsg;
			} else {
				msg.what = -1;
			}
			send_mail_handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				msg.what = 1;
				String responseMsg = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
				msg.obj = responseMsg;
			} else {
				msg.what = -1;
			}
			send_mail_handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addApplicationhistory() {
		String url = "http://"+JobhunterLoginActivity.SERVER_IP+":8080/ZhiLianProjectServer2014/Jobhunter/ApplicationhistoryAddServlet";  
		HttpPost request = new HttpPost(url);
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		String year = String.valueOf(calendar.get(Calendar.YEAR)); 
	    String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
	    String day = String.valueOf(calendar.get(Calendar.DATE));
/*	    if (cal.get(Calendar.AM_PM) == 0)
	      hour = String.valueOf(cal.get(Calendar.HOUR));
	    else
	      hour = String.valueOf(cal.get(Calendar.HOUR)+12);*/
	    String hour = String.valueOf(calendar.get(Calendar.HOUR));
	    String minute = String.valueOf(calendar.get(Calendar.MINUTE));
	    
	    String time_day = year + "/" + month + "/" + day;
	    String time_hour = hour + ":" + minute;
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userphone", auth.getUserphone());
			jsonObject.put("application_recruitername", recruitername);
			jsonObject.put("id_cloud", id_cloud);
			jsonObject.put("application_jobname", demand_jobname);
			jsonObject.put("application_salary", demand_salary_min + "~" + demand_salary_max);
			jsonObject.put("application_time_hour", time_hour);
			jsonObject.put("application_time_day", time_day);
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
			send_mail_handler.sendMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
